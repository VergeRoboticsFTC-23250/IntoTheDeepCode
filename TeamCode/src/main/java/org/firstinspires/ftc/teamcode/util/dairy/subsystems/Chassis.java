package org.firstinspires.ftc.teamcode.util.dairy.subsystems;

import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.config.Config;
import com.pedropathing.follower.Follower;
import com.pedropathing.follower.FollowerConstants;
import com.pedropathing.localization.Pose;
import com.pedropathing.pathgen.Path;
import com.pedropathing.pathgen.PathChain;
import com.pedropathing.util.Constants;
import com.pedropathing.util.DashboardPoseTracker;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.PIDCoefficients;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.util.BezierCurve;
import org.firstinspires.ftc.teamcode.util.PIDController;
import org.firstinspires.ftc.teamcode.util.Util;
import org.firstinspires.ftc.teamcode.util.dairy.Robot;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.concurrent.atomic.AtomicReference;

import dev.frozenmilk.dairy.core.FeatureRegistrar;
import dev.frozenmilk.dairy.core.dependency.Dependency;
import dev.frozenmilk.dairy.core.dependency.annotation.SingleAnnotation;
import dev.frozenmilk.dairy.core.wrapper.Wrapper;
import dev.frozenmilk.mercurial.Mercurial;
import dev.frozenmilk.mercurial.bindings.BoundGamepad;
import dev.frozenmilk.mercurial.commands.Command;
import dev.frozenmilk.mercurial.commands.Lambda;
import dev.frozenmilk.mercurial.subsystems.Subsystem;

import org.firstinspires.ftc.teamcode.util.pedroPathing.constants.FConstants;
import org.firstinspires.ftc.teamcode.util.pedroPathing.constants.LConstants;
import kotlin.annotation.MustBeDocumented;
@Config
public class Chassis implements Subsystem {
    public static final Chassis INSTANCE = new Chassis();
    public static Follower follower;
    public static boolean isSlowed = false;
    public static double slowSpeed = 0.25;
    public static DcMotorEx fl;
    public static DcMotorEx fr;
    public static DcMotorEx bl;
    public static DcMotorEx br;
    public static Telemetry telemetry;
    public static DashboardPoseTracker dashboardPoseTracker;

    //Custom Follower
    public static Pose startingPose = new Pose(8, 65.5, 0);
    public static double exponentialTransformHeading = 0.5;
    public static double exponentialTransformTranslationalDefault = 0.5;
    public static double exponentialTransformTranslational = exponentialTransformTranslationalDefault;
    public static double headingP = 0.9;
    public static double headingScaleFactorD = 0.05;
    public static double defaultTranslationalP = 0.05;
    public static double translationalP = defaultTranslationalP;
    public static double translationalScaleFactorD = 0.06;
    public static PIDCoefficients headingGains = new PIDCoefficients(headingP * Math.pow(exponentialTransformHeading, 2), 0, headingP*headingScaleFactorD * Math.pow(exponentialTransformHeading, 2));
    public static PIDCoefficients translationalGains = new PIDCoefficients(translationalP * Math.pow(exponentialTransformTranslational, 2), 0, translationalP*translationalScaleFactorD * Math.pow(exponentialTransformTranslational, 2));
    public static double headingScaleFactor = 1;

    public static boolean holdPoint = true;
    static boolean isFollowerBusy = false;
    static long currentPathDeltaT = 0;
    static double constantDrivePower = 0;
    public static PIDController translationalErrorController = new PIDController(translationalGains, 0);
    static PIDController headingController = new PIDController(headingGains, startingPose.getHeading());
    static double targetX = 0;
    static double targetY = 0;
    public static boolean faceSetpoint = false;
    public static boolean faceSetpointReverse = false;
    public static boolean suppressHeading = true;

    public static Pose drivePowers = new Pose(0, 0, 0);

    public static double translationalTolerance = 3;
    public static double headingTolerance = 5;

    public Chassis() {}

    public static Lambda runWhenBelowX(double x, Command command){
        return new Lambda("run-when-below-x").setFinish(() -> follower.getPose().getX() < x).setEnd((interrupted) -> command.schedule());
    }

    public static Lambda runWhenDeltaX(double delta, Command command){
        AtomicReference<Double> x = new AtomicReference<>((double) 0);
        return new Lambda("run-when-delta-x")
                .setInit(() -> {
                    x.set(follower.getPose().getX());
                })
                .setFinish(() -> Math.abs(x.get() - follower.getPose().getX()) > delta)
                .setEnd((interrupted) -> command.schedule());
    }

    public static double getDist(Pose pose1, Pose pose2){
        return Math.hypot(pose1.getX() - pose2.getX(), pose1.getY() - pose2.getY());
    }

    public static Lambda setHoldPoint(boolean bool){
        return new Lambda("set-hold-point").setInit(() -> {
            driveFieldCentric(0, 0, 0);
            holdPoint = bool;
        });
    }

    public static Lambda setSloppy(){
        return new Lambda("set-sloppy")
                .setExecute(() -> {
                    translationalErrorController.setTolerance(8);
                    headingController.setTolerance(10);
                });
    }

    public static Lambda resetTolerance(){
        return new Lambda("reset-tolerance")
                .setExecute(() -> {
                    translationalErrorController.setTolerance(translationalTolerance);
                    headingController.setTolerance(headingTolerance);
                });
    }

    public static Lambda followBezierCurve(BezierCurve curve){
        return new Lambda("follow-bezier-curve")
                .setExecute(() -> {
                    Pose pose = follower.getPose();
                    Pose lookaheadPoint = Util.extrapolateLookaheadPoint(pose, curve.getClosestLookaheadPoint(pose));
                    telemetry.addData("lookahead", lookaheadPoint);
                    setDrivePointManual(lookaheadPoint);
                })
                .setFinish(() -> getDist(follower.getPose(), curve.getEnd()) < translationalTolerance)
                .setEnd((interrupted) -> {
                    setDrivePointManual(curve.getEnd());
                });
    }
    public static Lambda runFollower() {
        return new Lambda("follower-pid")
                .setInterruptible(false)
                .setExecute(() -> {
                    if (holdPoint) {
                        //Get Pose
                        Pose pose = follower.getPose();

                        //Raw Translational
                        double errorX = targetX - pose.getX();
                        double errorY = targetY - pose.getY();
                        double errorDist = Math.hypot(errorX, errorY);

                        double translationalAngle = Math.atan2(errorY, errorX);

                        if(faceSetpoint){
                            headingController.setReference(translationalAngle);
                        }else if(faceSetpointReverse){
                            headingController.setReference(translationalAngle - Math.PI);
                        }
                        //Raw Heading
                        double heading = pose.getHeading();
                        double headingPower = headingController.getPowerHeading(heading);

                        double translationalPower = translationalErrorController.getPower(errorDist);
                        translationalPower = Util.transformExponential(translationalPower, exponentialTransformTranslational);

                        double drivePower = translationalPower * Math.cos(translationalAngle);
                        double lateralPower = translationalPower * Math.sin(translationalAngle);

                        //Scale Translational
                        double maxTranslationalVal = Math.max(Math.abs(drivePower), Math.abs(lateralPower));
                        if(maxTranslationalVal > 1){
                            drivePower /= maxTranslationalVal;
                            lateralPower /= maxTranslationalVal;
                        }

                        //Transform Heading
                        headingPower = Util.transformExponential(headingPower, exponentialTransformHeading);

                        double scaleFactor = 1;
                        //Suppress Heading
                        if(suppressHeading){
                            scaleFactor = constantDrivePower == 0? 1 : 1 / (1 + headingScaleFactor * Math.abs(translationalPower));
                        }

                        headingPower *= scaleFactor;

                        //Apply Powers
                        driveFieldCentric(constantDrivePower == 0? -drivePower : constantDrivePower, -lateralPower * 1.2, headingPower);

                        //drive(0, 0, headingPower);

                        logTele();
                    }
                    follower.update();
                })
                .setFinish(() -> false);
    }

    @Override
    public void preUserInitHook(@NonNull Wrapper opMode) {
        telemetry = opMode.getOpMode().telemetry;
        Constants.setConstants(FConstants.class, LConstants.class);
        follower = new Follower(FeatureRegistrar.getActiveOpMode().hardwareMap);
        follower.startTeleopDrive();

        dashboardPoseTracker = Chassis.follower.getDashboardPoseTracker();

        if (Robot.isAuto) {
            follower.setStartingPose(startingPose);
            setDefaultCommand(runFollower());
            Chassis.holdPoint = true;
        } else {
            follower.startTeleopDrive();
            setDefaultCommand(driveTele(Mercurial.gamepad1()));
            Chassis.holdPoint = false;
        }

        HardwareMap hMap = opMode.getOpMode().hardwareMap;
        fl = hMap.get(DcMotorEx.class, FollowerConstants.leftFrontMotorName);
        bl = hMap.get(DcMotorEx.class, FollowerConstants.leftRearMotorName);
        fr = hMap.get(DcMotorEx.class, FollowerConstants.rightFrontMotorName);
        br = hMap.get(DcMotorEx.class, FollowerConstants.rightRearMotorName);

        //Custom Follower
        translationalErrorController.reset();
        headingController.reset();
        translationalErrorController.setDerivativeFilterAlpha(1);
        headingController.setDerivativeFilterAlpha(1);
        translationalErrorController.setTolerance(translationalTolerance);
        headingController.setTolerance(Math.toRadians(headingTolerance));
    }

    @Retention(RetentionPolicy.RUNTIME) @Target(ElementType.TYPE) @MustBeDocumented
    @Inherited
    public @interface Attach { }

    private Dependency<?> dependency = Subsystem.DEFAULT_DEPENDENCY.and(new SingleAnnotation<>(Attach.class));

    @NonNull
    @Override
    public Dependency<?> getDependency() { return dependency; }

    @Override
    public void setDependency(@NonNull Dependency<?> dependency) { this.dependency = dependency; }

    @Override
    public void postUserInitHook(@NonNull Wrapper opMode) {}

    @Override
    public void preUserStartHook(@NonNull Wrapper opMode) {
    }

    @Override
    public void postUserLoopHook(@NonNull Wrapper opMode) {}
    public static Lambda driveTele(BoundGamepad gamepad){
        return new Lambda("drive-tele")
                .setExecute(() -> {
                    follower.setTeleOpMovementVectors(
                            gamepad.rightStickY().state() * (isSlowed? slowSpeed : 1),
                            -gamepad.rightStickX().state() * (isSlowed? slowSpeed : 1),
                            -gamepad.leftStickX().state() * (isSlowed? slowSpeed : 1)
                    );
                    follower.update();
                })
                .setFinish(() -> false);
    }

    public static void driveFieldCentric(double x, double y, double z) {
        follower.setTeleOpMovementVectors(
                x * (isSlowed? slowSpeed : 1),
                y * (isSlowed? slowSpeed : 1),
                z * (isSlowed? slowSpeed : 1), false
        );
        follower.update();
        drivePowers = new Pose(x, y, z);
        telemetry.addData("Chassis Vectors", "x: %f, y: %f, z: %f", x, y, z);
    }

    public static Lambda slow(){
        return new Lambda("slow-chassis")
                .setInit(() -> {
                    slowSpeed = .25;
                    isSlowed = true;
                });
    }

    public static Lambda slow(double val){
        return new Lambda("slow-chassis")
                .setInit(() -> {
                    isSlowed = true;
                    slowSpeed = val;
                });
    }

    public static Lambda fast(){
        return new Lambda("fast-chassis")
                .setInit(() -> isSlowed = false);
    }

    //Custom Follower
    public static void setDrivePointManual(Pose pose){
        targetX = pose.getX();
        targetY = pose.getY();
        headingController.setReference(pose.getHeading());
    }

    public static Lambda setDrivePoint(Pose pose){
        return new Lambda("set-drive-point")
                .setInit(() -> setDrivePointManual(pose));
    }

    public static Pose getDrivePoint(){
        return new Pose(targetX, targetY, headingController.getReference());
    }

    public static boolean isAtPoint(){
        return translationalErrorController.isAtReference() && headingController.isAtReference();
    }

    public static boolean isStuck(){
        return follower.getVelocityMagnitude() <= 0.00005 ;
    }

    private static long startTime = 0;

    public static Lambda setConstantDrivePower(double pow){
        return new Lambda("set-constant-drive-power")
                .setInit(() -> constantDrivePower = pow)
                .setFinish(() -> true);
    }

    public static Lambda releaseConstantDrivePower(){
        return new Lambda("release-constant-drive-power")
                .setInit(() -> constantDrivePower = 0)
                .setFinish(() -> true);
    }

    public static Lambda releaseConstantDrivePowerAndHold(){
        return new Lambda("release-constant-drive-power-and-hold")
                .setInit(() -> {
                    constantDrivePower = 0;
                    setDrivePointManual(follower.getPose());
                })
                .setFinish(() -> true);
    }

    public static void offsetRobotCentric(double errx, double erry){
        Pose pose = follower.getPose().copy();
        double heading = pose.getHeading();

        double x = (errx * Math.cos(heading)) - erry * Math.sin(heading);
        double y = (errx * Math.sin(heading)) + erry * Math.cos(heading);

        pose.add(new Pose(x, y, 0));

        setDrivePointManual(new Pose(pose.getX(), pose.getY(), headingController.getReference()));
    }

    public static Lambda setAggressiveGains(){
        return new Lambda("set-aggressive-gains")
                .setInit(() -> {
                    translationalP = defaultTranslationalP * 2;
                    //exponentialTransformTranslational = exponentialTransformTranslationalDefault/2;
                    translationalGains = new PIDCoefficients(translationalP * Math.pow(exponentialTransformTranslational, 2), 0, translationalP*translationalScaleFactorD * Math.pow(exponentialTransformTranslational, 2));
                    translationalErrorController.setGains(translationalGains);
                });
    }

    public static Lambda setNormalGains(){
        return new Lambda("set-normal-gains")
                .setInit(() -> {
                    translationalP = defaultTranslationalP;
                    exponentialTransformTranslational = exponentialTransformTranslationalDefault;
                    translationalGains = new PIDCoefficients(translationalP * Math.pow(exponentialTransformTranslational, 2), 0, translationalP*translationalScaleFactorD * Math.pow(exponentialTransformTranslational, 2));
                    translationalErrorController.setGains(translationalGains);
                });
    }

    public static Lambda homeToSamp(){
        return new Lambda("home-to-samp")
                .setFinish(() -> Robot.vision.isSampleVisible())
                .setEnd((interrupted) -> {
                    offsetRobotCentric(Robot.vision.getY() - 1, -Robot.vision.getX() * 0.975);
                });
    }

    public static Lambda driveToPoint(Pose pose){
        return new Lambda("drive-to-point")
                .setInterruptible(true)
                .setInit(() -> {
                    setDrivePointManual(pose);
                    startTime = System.currentTimeMillis();
                })
                .setExecute(() -> {
                    currentPathDeltaT = System.currentTimeMillis() - startTime;
                })
                .setFinish(Chassis::isAtPoint);
    }

    public static void logTele(){
        telemetry.addData("isStuck", Chassis.isStuck());
        telemetry.addData("isAtPoint", Chassis.isAtPoint());
        telemetry.addData("driveX", Chassis.follower.driveVector.getXComponent());

    }

    public static Lambda followPath(Path path) {
        return new Lambda("follow-path")
                .addRequirements(INSTANCE)
                .setInit(() -> follower.followPath(path, true))
                .setExecute(() -> {
                    follower.update();
                    follower.telemetryDebug(telemetry);
                })
                .setFinish(() -> !follower.isBusy() || follower.isRobotStuck())
                .setEnd((interrupted) -> {
                    if (interrupted) follower.breakFollowing();
                });
    }

    public static Lambda followPath(Path path, boolean hold) {
        return new Lambda("follow-path")
                .addRequirements(INSTANCE)
                .setInterruptible(true)
                .setInit(() -> follower.followPath(path, hold))
                .setExecute(() -> {
                    follower.update();
                    follower.telemetryDebug(telemetry);
                })
                .setFinish(() -> !follower.isBusy() || follower.isRobotStuck())
                .setEnd((interrupted) -> {
                    if (interrupted) follower.breakFollowing();
                });
    }

    public static Lambda followPathChain(PathChain chain) {
        return new Lambda("follow-path-chain")
                .addRequirements(INSTANCE)
                .setInit(() -> follower.followPath(chain, true))
                .setExecute(() -> {
                    follower.update();
                    telemetry.addData("pinpoint cooked", follower.isPinpointCooked());
                })
                .setFinish(() -> !follower.isBusy() || follower.isRobotStuck());
    }
}
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
import org.firstinspires.ftc.robotcore.internal.opmode.OpModeMeta;
import org.firstinspires.ftc.teamcode.util.BezierSolver;
import org.firstinspires.ftc.teamcode.util.PIDController;
import org.firstinspires.ftc.teamcode.util.Util;
import org.firstinspires.ftc.teamcode.util.dairy.Robot;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.concurrent.atomic.AtomicLong;

import dev.frozenmilk.dairy.core.FeatureRegistrar;
import dev.frozenmilk.dairy.core.dependency.Dependency;
import dev.frozenmilk.dairy.core.dependency.annotation.SingleAnnotation;
import dev.frozenmilk.dairy.core.wrapper.Wrapper;
import dev.frozenmilk.mercurial.Mercurial;
import dev.frozenmilk.mercurial.bindings.BoundGamepad;
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
    public static Pose startingPose = new Pose(8, 35, 0);
    public static double exponentialTransformHeading = 0.5;
    public static double exponentialTransformTranslational = 0.5;
    public static double exponentialTransformLookahead = 0.125;
    public static double headingP = 0.9;
    public static double headingScaleFactorD = 0.05;
    public static double translationalP = 0.05;
    public static double translationalScaleFactorD = 0.06;
    public static PIDCoefficients headingGains = new PIDCoefficients(headingP * Math.pow(exponentialTransformHeading, 2), 0, headingP*headingScaleFactorD * Math.pow(exponentialTransformHeading, 2));
    public static PIDCoefficients translationalGains = new PIDCoefficients(translationalP * Math.pow(exponentialTransformTranslational, 2), 0, translationalP*translationalScaleFactorD * Math.pow(exponentialTransformTranslational, 2));
    public static double headingScaleFactor = 1;

    public static boolean holdPoint = true;
    static boolean isFollowerBusy = false;
    static long currentPathDeltaT = 0;
    static double constantDrivePower = 0;
    static PIDController translationalErrorController = new PIDController(translationalGains, 0);
    static PIDController headingController = new PIDController(headingGains, startingPose.getHeading());
    static double targetX = 0;
    static double targetY = 0;
    public static boolean faceSetpoint = false;
    public static boolean faceSetpointReverse = false;
    public static double lookaheadMultiplier = 200;
    public static boolean suppressHeading = true;

    public static Pose drivePowers = new Pose(0, 0, 0);
    public Chassis() {}
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
                        drive(constantDrivePower == 0? -drivePower : constantDrivePower, -lateralPower * 1.1, headingPower);

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

        if (Robot.flavor == OpModeMeta.Flavor.AUTONOMOUS) {
            follower.setStartingPose(startingPose);
            follower.setXOffset(startingPose.getX());
            follower.setYOffset(startingPose.getY());
        } else {
            setDefaultCommand(drive(Mercurial.gamepad1()));
        }

        HardwareMap hMap = opMode.getOpMode().hardwareMap;
        fl = hMap.get(DcMotorEx.class, FollowerConstants.leftFrontMotorName);
        bl = hMap.get(DcMotorEx.class, FollowerConstants.leftRearMotorName);
        fr = hMap.get(DcMotorEx.class, FollowerConstants.rightFrontMotorName);
        br = hMap.get(DcMotorEx.class, FollowerConstants.rightRearMotorName);

        //Custom Follower
        translationalErrorController.reset();
        headingController.reset();
        headingController.setDerivativeFilterAlpha(1);
        translationalErrorController.setDerivativeFilterAlpha(1);
        setCleanManual();
        setDefaultCommand(runFollower());
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
    public void postUserInitHook(@NonNull Wrapper opMode) {
        if (Robot.flavor.equals(OpModeMeta.Flavor.TELEOP)) follower.startTeleopDrive();
    }

    @Override
    public void preUserStartHook(@NonNull Wrapper opMode) {
    }

    @Override
    public void postUserLoopHook(@NonNull Wrapper opMode) {}

    public static Lambda drive(BoundGamepad gamepad){
        return new Lambda("drive")
                .addRequirements(INSTANCE)
                .setExecute(() -> {
                    drive(
                            gamepad.rightStickY().state(),
                            -gamepad.rightStickX().state(),
                            -gamepad.leftStickX().state()
                    );
                })
                .setFinish(() -> false);
    }

    public void speedSlow(){
        isSlowed = true;
    }

    public void speedFast(){
        isSlowed = false;
    }

    public static void drive(double x, double y, double z) {
        follower.setTeleOpMovementVectors(
                x * (isSlowed? slowSpeed : 1),
                y * (isSlowed? slowSpeed : 1),
                z * (isSlowed? slowSpeed : 1), false
        );
        follower.update();
        drivePowers = new Pose(x, y, z);
        telemetry.addData("Chassis Vectors", "x: %f, y: %f, z: %f", x, y, z);
    }
    public static Lambda pushOld(double pow, long time){
        AtomicLong startTime = new AtomicLong();
        return new Lambda("push-drive")
                .setInit(() -> {
                    startTime.set(System.currentTimeMillis());
                })
                .setExecute(
                        () -> {
                            fl.setPower(pow);
                            fr.setPower(pow);
                            bl.setPower(pow);
                            br.setPower(pow);
                        }
                )
                .setFinish(() -> System.currentTimeMillis() - startTime.get() > time);
    }

    public static Lambda slow(){
        return new Lambda("slow")
                .setInit(() -> isSlowed = true);
    }

    public static Lambda fast(){
        return new Lambda("fast")
                .setInit(() -> isSlowed = false);
    }

    //Custom Follower
    public static void setDrivePoint(Pose pose){
        targetX = pose.getX();
        targetY = pose.getY();
        headingController.setReference(pose.getHeading());
    }

    public static Lambda setDrivePointCommand(Pose pose){
        return new Lambda("set-drive-point-command")
                .setInit(() -> setDrivePoint(pose));
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
    public static void setFaceSetpointManual(boolean bool){
        faceSetpoint = bool;
    }
    public static Lambda setFaceSetpoint(boolean bool){
        return new Lambda("set-face-setpoint")
                .setInit(() -> {
                    setFaceSetpointManual(bool);
                })
                .setFinish(() -> true);
    }
    public static void setFaceSetpointReverseManual(boolean bool){
        faceSetpointReverse = bool;
    }
    public static Lambda setFaceSetpointReverse(boolean bool){
        return new Lambda("set-face-setpoint-reverse")
                .setInit(() -> {
                    setFaceSetpointReverseManual(bool);
                })
                .setFinish(() -> true);
    }
    public static Lambda setSloppy(){
        return new Lambda("set-sloppy")
                .setInit(() -> {
                    translationalErrorController.setTolerance(8);
                    headingController.setTolerance(Math.toRadians(14));
                })
                .setFinish(() -> true);
    }
    public static void setCleanManual(){
        translationalErrorController.setTolerance(4);
        headingController.setTolerance(Math.toRadians(7));
    }
    public static Lambda setClean(){
        return new Lambda("set-clean")
                .setInit(Chassis::setCleanManual)
                .setFinish(() -> true);
    }

    public static Lambda suppressHeading(){
        return new Lambda("suppress-heading")
                .setInit(() -> suppressHeading = true)
                .setFinish(() -> true);
    }

    public static Lambda releaseHeading(){
        return new Lambda("suppress-heading")
                .setInit(() -> suppressHeading = false)
                .setFinish(() -> true);
    }

    public static void setExactManual(){
        translationalErrorController.setTolerance(0);
        headingController.setTolerance(Math.toRadians(0));
    }
    public static Lambda setExact(){
        return new Lambda("set-exact")
                .setInit(Chassis::setExactManual)
                .setFinish(() -> true);
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

    public static Lambda driveToPoint(Pose pose){
        return new Lambda("drive-to-point")
                .setInterruptible(true)
                .setInit(() -> {
                    setDrivePoint(pose);
                    startTime = System.currentTimeMillis();
                })
                .setExecute(() -> {
                    currentPathDeltaT = System.currentTimeMillis() - startTime;
                })
                .setFinish(Chassis::isAtPoint);
    }

    public static Lambda pushUntilStuck(Pose pose, double pow){
        return new Lambda("push-to-point")
                .setInit(() -> {
                    setDrivePoint(pose);
                    startTime = System.currentTimeMillis();
                    constantDrivePower = pow;
                })
                .setExecute(() -> {
                    currentPathDeltaT = System.currentTimeMillis() - startTime;
                })
                .setFinish(() -> follower.getVelocityMagnitude() < 0.05);
    }
    public static Lambda driveToPointUntilStuck(Pose pose){
        return new Lambda("drive-to-point-until-stuck")
                .setInterruptible(true)
                .setInit(() -> {
                    setDrivePoint(pose);
                    startTime = System.currentTimeMillis();
                })
                .setExecute(() -> {
                    currentPathDeltaT = System.currentTimeMillis() - startTime;
                })
                .setFinish(Chassis::isStuck);
    }

    public static void logTele(){
        telemetry.addData("isStuck", Chassis.isStuck());
        telemetry.addData("isAtPoint", Chassis.isAtPoint());
        telemetry.addData("driveX", Chassis.follower.driveVector.getXComponent());

    }

    public static Lambda followBezierCurve(Pose[] controlPoints, boolean tangent, boolean forward){
        Pose pathEnd = controlPoints[controlPoints.length - 1];
        return new Lambda("follow-bezier-curve")
                .setInterruptible(true)
                .setInit(() -> {
                    startTime = System.currentTimeMillis();
                    if(tangent){
                        Chassis.setFaceSetpointManual(forward);
                        Chassis.setFaceSetpointReverseManual(!forward);
                    }
                })
                .setExecute(() -> {
                    Pose currentPose = follower.getPose();
                    Pose targetPose = pathEnd;

                    Pose closestPointOnCurve = BezierSolver.getClosestBezierPoseWithX(
                            currentPose,
                            controlPoints
                    );

                    if(closestPointOnCurve != null){
                        Pose lookAheadPoint = BezierSolver.getClosestBezierPoint(closestPointOnCurve, 7, controlPoints, true);

                        if (lookAheadPoint != null){
                            if(!lookAheadPoint.roughlyEquals(pathEnd)){
                                double distX = lookAheadPoint.getX() - closestPointOnCurve.getX();
                                double distY = lookAheadPoint.getY() - closestPointOnCurve.getY();
                                double dist = Math.hypot(distX, distY);
                                dist = Math.sqrt(Math.pow(dist, exponentialTransformLookahead)) * lookaheadMultiplier;
                                double angle = Math.atan2(distY, distX);
                                distX = dist * Math.cos(angle);
                                distY = dist * Math.sin(angle);
                                targetPose = new Pose(lookAheadPoint.getX() + distX, lookAheadPoint.getY() + distY, 0);
                            }
                        }
                    }

                    setDrivePoint(targetPose);

                    if(Chassis.getDrivePoint().roughlyEquals(pathEnd)){
                        Chassis.setFaceSetpointManual(false);
                        Chassis.setFaceSetpointReverseManual(false);
                    }
                    currentPathDeltaT = System.currentTimeMillis() - startTime;
                })
                .setFinish(() -> Chassis.getDrivePoint().roughlyEquals(pathEnd) && (Chassis.isAtPoint() || Chassis.isStuck()))
                .setEnd((interrupted) -> {
                    Chassis.setFaceSetpointManual(false);
                    Chassis.setFaceSetpointReverseManual(false);
                    Chassis.setDrivePoint(pathEnd);
                });
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
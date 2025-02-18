package org.firstinspires.ftc.teamcode.util.dairy.subsystems;

import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.config.Config;
import com.arcrobotics.ftclib.controller.PIDFController;
import com.pedropathing.follower.Follower;
import com.pedropathing.follower.FollowerConstants;
import com.pedropathing.localization.Pose;
import com.pedropathing.pathgen.Path;
import com.pedropathing.pathgen.PathChain;
import com.pedropathing.util.Constants;
import com.pedropathing.util.DashboardPoseTracker;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.internal.opmode.OpModeMeta;
import org.firstinspires.ftc.teamcode.util.PIDController;
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
    public static boolean enableSQUID = true;
    public static boolean holdPoint = true;
    public static boolean isFollowerBusy = false;
    private static long currentPathDeltaT = 0;
    public static double Kp_drive = .3;
    public static double Ki_drive = 0;
    public static double Kd_drive = 0.03;
    public static double Kp_lateral = .3;
    public static double Ki_lateral = 0;
    public static double Kd_lateral = 0.03;
    public static double Kp_heading = 4; //.4
    public static double Ki_heading = 0;
    public static double Kd_heading = 0.35; //.025
    static Pose startingPose = new Pose(9.000, 65.000, 0);
    public static PIDController driveController = new PIDController(Kp_drive, Ki_drive, Kd_drive, startingPose.getX());
    public static PIDController lateralController = new PIDController(Kp_lateral, Ki_lateral, Kd_lateral, startingPose.getY());
    public static PIDController headingController = new PIDController(Kp_heading, Ki_heading, Kd_heading, startingPose.getHeading());
    public Chassis() {}

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
        telemetry.addData("Chassis Vectors", "x: %f, y: %f, z: %f", x, y, z);
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
    public void preUserInitHook(@NonNull Wrapper opMode) {
        telemetry = opMode.getOpMode().telemetry;
        Constants.setConstants(FConstants.class, LConstants.class);
        follower = new Follower(FeatureRegistrar.getActiveOpMode().hardwareMap);
        follower.startTeleopDrive();

        dashboardPoseTracker = Chassis.follower.getDashboardPoseTracker();

        if (Robot.flavor == OpModeMeta.Flavor.AUTONOMOUS) {
            follower.setStartingPose(startingPose);
        } else {
            setDefaultCommand(drive(Mercurial.gamepad1()));
        }

        HardwareMap hMap = opMode.getOpMode().hardwareMap;
        fl = hMap.get(DcMotorEx.class, FollowerConstants.leftFrontMotorName);
        bl = hMap.get(DcMotorEx.class, FollowerConstants.leftRearMotorName);
        fr = hMap.get(DcMotorEx.class, FollowerConstants.rightFrontMotorName);
        br = hMap.get(DcMotorEx.class, FollowerConstants.rightRearMotorName);

        //Custom Follower
        driveController.reset();
        lateralController.reset();
        headingController.reset();
        driveController.setTolerance(1);
        lateralController.setTolerance(1);
        headingController.setTolerance(Math.PI/60);
        setDefaultCommand(runFollower());
    }

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
    public static Lambda push(double pow, long time){
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

    public static Lambda toggleSlow() {
        return new Lambda("toggle-slow")
                .setInit(() -> isSlowed = !isSlowed)
                .setFinish(() -> true);
    }

    public static Lambda slow(){
        return new Lambda("slow")
                .setInit(() -> isSlowed = true);
    }

    public static Lambda fast(){
        return new Lambda("fast")
                .setInit(() -> isSlowed = false);
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

    //Custom Follower
    private static void setDrivePoint(Pose pose){
        driveController.setReference(pose.getX());
        lateralController.setReference(pose.getY());
        headingController.setReference(pose.getHeading());
    }

    public static boolean isAtPoint(){
        return driveController.isAtReference() && lateralController.isAtReference() && headingController.isAtReference();
    }

    public static boolean isStuck(){
        return follower.getVelocityMagnitude() < 0.5 ;
    }
    public static Lambda setSloppy(){
        return new Lambda("set-sloppy")
                .setInit(() -> {
                    driveController.setTolerance(3);
                    lateralController.setTolerance(3);
                    headingController.setTolerance(Math.PI/30);
                })
                .setFinish(() -> true);
    }
    public static Lambda setClean(){
        return new Lambda("set-clean")
                .setInit(() -> {
                    driveController.setTolerance(1);
                    lateralController.setTolerance(1);
                    headingController.setTolerance(Math.PI/60);
                })
                .setFinish(() -> true);
    }
    public static Lambda runFollower() {
        return new Lambda("follower-pid")
                .setInterruptible(false)
                .setExecute(() -> {
                    if (holdPoint) {
                        Pose pose = follower.getPose();
                        double heading = pose.getHeading();

                        double drivePower = driveController.getPower(pose.getX());
                        double lateralPower = lateralController.getPower(pose.getY());
                        double headingPower = headingController.getPowerHeading(heading);

                        //SQUID (Square Root Input Determination)
                        if(enableSQUID){
                            drivePower = Math.min(Math.pow(Math.abs(drivePower), 2), 1) * Math.signum(drivePower);
                            lateralPower = Math.min(Math.pow(Math.abs(lateralPower), 2), 1) * Math.signum(lateralPower);
                            headingPower = Math.min(Math.pow(Math.abs(headingPower), 2), 1) * Math.signum(headingPower);
                        }

//                        double robotDrivePower = drivePower * Math.cos(heading) - lateralPower * Math.sin(heading);
//                        double robotLateralPower = drivePower * Math.sin(heading) + lateralPower * Math.cos(heading);



                        drive(drivePower, lateralPower, -headingPower);

                        telemetry.addData("velocity", follower.getVelocityMagnitude());
                    }
                })
                .setFinish(() -> false);
    }

    private static long startTime = 0;

    public static Lambda driveToPoint(Pose pose){
        return new Lambda("drive-to-point")
                .setInterruptible(true)
                .setInit(() -> {
                    setDrivePoint(pose);
                    startTime = System.currentTimeMillis();
                })
                .setExecute(() -> {
                    telemetry.addData("Drive Error", driveController.getError());
                    telemetry.addData("Lateral Error", lateralController.getError());
                    telemetry.addData("Heading Error", headingController.getError());
                    currentPathDeltaT = System.currentTimeMillis() - startTime;
                })
                .setFinish(Chassis::isAtPoint);
    }

    public static Lambda driveToPointUntilStuck(Pose pose){
        return new Lambda("drive-to-point-until-stuck")
                .setInterruptible(true)
                .setInit(() -> {
                    setDrivePoint(pose);
                    startTime = System.currentTimeMillis();
                })
                .setExecute(() -> {
                    telemetry.addData("Drive Error", driveController.getError());
                    telemetry.addData("Lateral Error", lateralController.getError());
                    telemetry.addData("Heading Error", headingController.getError());
                    currentPathDeltaT = System.currentTimeMillis() - startTime;
                })
                .setFinish(() -> (Chassis.isAtPoint() || Chassis.isStuck()) && currentPathDeltaT > 800);
    }
}
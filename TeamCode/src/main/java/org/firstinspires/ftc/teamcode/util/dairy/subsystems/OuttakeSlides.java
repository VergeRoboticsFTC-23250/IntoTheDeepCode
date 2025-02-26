package org.firstinspires.ftc.teamcode.util.dairy.subsystems;

import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.config.Config;
import com.arcrobotics.ftclib.controller.PIDFController;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.CurrentUnit;
import org.firstinspires.ftc.teamcode.util.dairy.Robot;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import dev.frozenmilk.dairy.core.dependency.Dependency;
import dev.frozenmilk.dairy.core.dependency.annotation.SingleAnnotation;
import dev.frozenmilk.dairy.core.wrapper.Wrapper;
import dev.frozenmilk.mercurial.Mercurial;
import dev.frozenmilk.mercurial.commands.Lambda;
import dev.frozenmilk.mercurial.subsystems.Subsystem;
import kotlin.annotation.MustBeDocumented;

@Config
public class OuttakeSlides implements Subsystem {
    public static final OuttakeSlides INSTANCE = new OuttakeSlides();
    public static DcMotorEx slideR;
    public static DcMotorEx slideL;
    public static DcMotorEx encoder;
    public static Telemetry telemetry;
    public static int tolerance = 3200;
    public static int safePos = 11200;
    public static int submersiblePos = safePos;
    public static int submersiblePushPos = 0;
    public static int scoreSubmersiblePos = 26500;
    public static int maxPos = 70000;
    public static int bucketPos = maxPos;
//    static final OpModeLazyCell<PIDFService> thingy = new OpModeLazyCell<>(() -> new PIDFService(OuttakeSlides.controller, OuttakeSlides.slideL, OuttakeSlides.slideR));
    public static double Kp = 0.00018;
    public static double Ki = 0.0000;
    public static double Kd = 0.000008;
    public static double Kf = 0.0000;
    public static int minPos = 0;
    public static double currentLimit = 1700;
    public static volatile boolean enablePID = true;

    public static PIDFController controller = new PIDFController(Kp, Ki, Kd, Kf);

    private OuttakeSlides() {}

    @Retention(RetentionPolicy.RUNTIME) @Target(ElementType.TYPE) @MustBeDocumented
    @Inherited
    public @interface Attach { }

    @Override
    public void preUserInitHook(@NonNull Wrapper opMode) {
        HardwareMap hMap = opMode.getOpMode().hardwareMap;
        telemetry = opMode.getOpMode().telemetry;
        slideR = hMap.get(DcMotorEx.class, "outtakeSR");
        slideL = hMap.get(DcMotorEx.class, "outtakeSL");
        encoder = hMap.get(DcMotorEx.class, "spintake");
        slideR.setCurrentAlert(currentLimit, CurrentUnit.MILLIAMPS);
        slideL.setCurrentAlert(currentLimit, CurrentUnit.MILLIAMPS);
        slideR.setDirection(DcMotorSimple.Direction.REVERSE);

        reset();

        controller.setTolerance(tolerance);

        setDefaultCommand(runPID());
    }

    @Override
    public void postUserInitHook(@NonNull Wrapper opMode) {
        Robot.init();
    }

    @Override
    public void postUserLoopHook(@NonNull Wrapper opMode) {}

    private Dependency<?> dependency = Subsystem.DEFAULT_DEPENDENCY.and(new SingleAnnotation<>(Attach.class));

    @NonNull
    @Override
    public Dependency<?> getDependency() {
        return dependency;
    }

    @Override
    public void setDependency(@NonNull Dependency<?> dependency) {
        this.dependency = dependency;
    }

    public static void setPower(double power){
        slideL.setPower(power);
        slideR.setPower(power);
    }

    public static Lambda setPowerCommand(double power){
        return new Lambda("set-power")
                .setExecute(() -> {
                    if (power != 0) {
                        enablePID = false;
                        setPower(power);
                        controller.setSetPoint(getPos());
                        logTele();
                    } else {
                        enablePID = true;
                        controller.setSetPoint(getPos());
                        setPower(0);
                    }

                });
    }

    public static boolean isOverCurrent() {
        return slideR.isOverCurrent() || slideL.isOverCurrent();
    }

    public static Lambda increaseGains(){
        return new Lambda("increase-gains")
                .setInterruptible(true)
                .setInit(() -> {
                    controller.setP(Kp * 3);
                })
                .setFinish(() -> true);
    }

    public static Lambda resetGains(){
        return new Lambda("reset-gains")
                .setInterruptible(true)
                .setInit(() -> {
                    controller.setP(Kp);
                })
                .setFinish(() -> true);
    }

    public static void reset() {
        slideL.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        slideR.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        encoder.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        encoder.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        controller.reset();
        controller.setSetPoint(0);
        logTele();
    }

    public static Lambda runToPosition(int pos){
        return new Lambda("set-target-pos")
                .setInterruptible(true)
                .setInit(() -> {
                    controller.setSetPoint(pos);
                    if(pos > 40000){
                        controller.setTolerance(3400);
                    } else {
                        controller.setTolerance(tolerance);
                    }
                })
                .setFinish(() -> controller.atSetPoint());
    }

    public static void logCurrent(){
        telemetry.addData("isOver", isOverCurrent());
    }

    public static double getPos(){
        return encoder.getCurrentPosition();
    }

    public static void logTele(){
        telemetry.addData("Slide Pos", getPos());
        telemetry.addData("Slide Power", slideL.getPower());
        telemetry.addData("Slide Setpoint", controller.getSetPoint());
        telemetry.addData("Slide Error", controller.getPositionError());
        telemetry.addData("At Setpoint?", controller.atSetPoint());
        telemetry.addData("Enable PID", enablePID);
        telemetry.addData("arv joystick", Mercurial.gamepad2().rightStickY().state());
    }

    public static Lambda runPID() {
        return new Lambda("outtake-pid")
                .addRequirements(INSTANCE)
                .setInterruptible(true)
                .setExecute(() -> {
                    if (enablePID) {
                        double power = controller.calculate(getPos());
                        setPower(power);
//                        logTele();
                    }
                })
                .setFinish(() -> false);
    }

    public static Lambda home() {
        return new Lambda("home-outtake")
                .setInit(() -> {
                    enablePID = false;
                    setPower(-0.5);
                })
                .setFinish(OuttakeSlides::isOverCurrent)
                .setEnd((interrupted) -> {
                    setPower(0);
                    reset();
                    enablePID = true;
                });
    }

    public static Lambda waitForPos(int pos) {
        return new Lambda("wait-for-pos")
                .setFinish(() -> Math.abs(getPos() - pos) < tolerance);
    }

    public static Lambda setPowerSafe(int pow){
        return new Lambda("set-power-safe")
                .setInit(() -> {
                    enablePID = false;
                    setPower(pow);
                })
                .setFinish(() -> isOverCurrent());
    }
}
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

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import dev.frozenmilk.dairy.core.dependency.Dependency;
import dev.frozenmilk.dairy.core.dependency.annotation.SingleAnnotation;
import dev.frozenmilk.dairy.core.wrapper.Wrapper;
import dev.frozenmilk.mercurial.commands.Lambda;
import dev.frozenmilk.mercurial.subsystems.Subsystem;
import kotlin.annotation.MustBeDocumented;

@Config
public class OuttakeSlides implements Subsystem {
    public static final OuttakeSlides INSTANCE = new OuttakeSlides();
    public static DcMotorEx slideR;
    public static DcMotorEx slideL;
    public static Telemetry telemetry;
    public static int tolerance = 100;
    public static int submirsiblePos = 0;
    public static int bucketPos = 0;
    public static int scoreSubmersiblePos = 0;
    public static class GAINS{
        public static double Kp = 0.005;
        public static double Ki = 0;
        public static double Kd = 0;
        public static double Kf = 0;
    }
    public static double minArmMoveHeight = 500;
    public static int maxPos = 2200;
    public static int minPos = 0;
    public static double currentLimit = 4;

    private static PIDFController controller = new PIDFController(GAINS.Kp, GAINS.Ki, GAINS.Kd, GAINS.Kf);

    private OuttakeSlides() {}

    @Retention(RetentionPolicy.RUNTIME) @Target(ElementType.TYPE) @MustBeDocumented
    @Inherited
    public @interface Attach { }

    @Override
    public void postUserInitHook(@NonNull Wrapper opMode) {
        HardwareMap hMap = opMode.getOpMode().hardwareMap;
        slideR = hMap.get(DcMotorEx.class, "outtakeSR");
        slideL = hMap.get(DcMotorEx.class, "outtakeSL");
        slideR.setCurrentAlert(currentLimit, CurrentUnit.AMPS);
        slideL.setCurrentAlert(currentLimit, CurrentUnit.AMPS);
        slideR.setDirection(DcMotorSimple.Direction.FORWARD);
        slideL.setDirection(DcMotorSimple.Direction.REVERSE);

        setDefaultCommand(runPID());
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

    public static Lambda setVelo(double power){
        return new Lambda("set-power")
                .addRequirements(INSTANCE)
                .setExecute(() -> {
                    slideR.setVelocity(Chassis.isSlowed? power * Chassis.slowSpeed : power);
                    slideL.setVelocity(Chassis.isSlowed? power * Chassis.slowSpeed : power);
                });
    }

    public static boolean isOverCurrent() {
        return slideR.isOverCurrent() || slideL.isOverCurrent();
    }

    public static void reset() {
        slideL.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        slideR.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        slideL.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        slideR.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        controller.reset();
    }

    public static Lambda setTargetPos(int pos){
        return new Lambda("set-target-pos")
                .setInit(() -> controller.setSetPoint(pos))
                .setFinish(() -> true);
    }

    public static void logCurrent(){
        telemetry.addData("isOver", isOverCurrent());
        telemetry.update();
    }

    public static double getPos(){
        return (slideR.getCurrentPosition() + slideL.getCurrentPosition()) / 2.0;
    }

    public void logPos(){
        telemetry.addData("Slide Pos", getPos());
        telemetry.update();
    }

    public static Lambda runPID() {
        return new Lambda("outtake-pid")
                .addRequirements(INSTANCE)
                .setInterruptible(false)
                .setExecute(() -> {
                    double velo = controller.calculate(getPos());
                    setVelo(velo);
                })
                .setFinish(() -> false);
    }

    public static Lambda home() {
        return new Lambda("home-outtake")
                .setInit(() -> controller.setSetPoint(minPos))
                .setEnd((interrupted) -> {
                    if (!interrupted) reset();
                })
                .setFinish(() -> isOverCurrent() && getPos() < 15);

    }
}
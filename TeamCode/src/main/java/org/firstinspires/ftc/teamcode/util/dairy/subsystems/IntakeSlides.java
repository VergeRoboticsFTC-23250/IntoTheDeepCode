package org.firstinspires.ftc.teamcode.util.dairy.subsystems;

import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.config.Config;
import com.arcrobotics.ftclib.controller.PIDFController;
import com.arcrobotics.ftclib.hardware.ServoEx;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

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
import dev.frozenmilk.mercurial.Mercurial;
import dev.frozenmilk.mercurial.commands.Lambda;
import dev.frozenmilk.mercurial.subsystems.Subsystem;
import kotlin.annotation.MustBeDocumented;

@Config
public class IntakeSlides implements Subsystem {
    public static final IntakeSlides INSTANCE = new IntakeSlides();

    private static DcMotorEx extendo;
    public static Telemetry telemetry;

    public static class Gains{
        public static double Kp = 0.01;
        public static double Ki = 0;
        public static double Kd = 0;
        public static double Kf = 0;
    }
    public static int maxPos = 1000;
    public static int minPos = 0;

    public static int currentLimit = 4;

    private static PIDFController controller = new PIDFController(Gains.Kp, Gains.Ki, Gains.Kd, Gains.Kf);

    @Retention(RetentionPolicy.RUNTIME) @Target(ElementType.TYPE) @MustBeDocumented
    @Inherited
    public @interface Attach { }

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

    @Override
    public void postUserInitHook(@NonNull Wrapper opMode) {
        HardwareMap hMap = opMode.getOpMode().hardwareMap;
        telemetry = opMode.getOpMode().telemetry;

        extendo = hMap.get(DcMotorEx.class, "extendo");
        extendo.setCurrentAlert(currentLimit, CurrentUnit.AMPS);

        setDefaultCommand(runPID());
    }

    @Override
    public void postUserLoopHook(@NonNull Wrapper opMode) {}

    public static Lambda runPID(){
        return new Lambda("intake-pid")
                .addRequirements(INSTANCE)
                .setInterruptible(false)
                .setExecute(() -> {
                    double velo = controller.calculate(extendo.getCurrentPosition());
                    extendo.setVelocity(velo);
                })
                .setFinish(() -> false);
    }

    public static Lambda setTargetPos(int pos){
        return new Lambda("set-target-pos")
                .setInit(() -> controller.setSetPoint(pos))
                .setFinish(() -> true);
    }

    public static Lambda home() {
        return new Lambda("home-intake")
                .setInit(() -> controller.setSetPoint(minPos))
                .setEnd((interrupted) -> reset())
                .setFinish(() -> isOverCurrent() && controller.atSetPoint());
    }

    public static boolean isOverCurrent() { return extendo.isOverCurrent(); }

    public static void reset() {
        extendo.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        extendo.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        controller.reset();
    }
}
package org.firstinspires.ftc.teamcode.util.dairy.subsystems.intake;

import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

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
public class Differential implements Subsystem {
    public static final Differential INSTANCE = new Differential();
    public static Servo diffRight;
    public static Servo diffLeft;

    public static class IntakePivot{
        public static double home = 0;
        public static double init = home;
        public static double intake = 0;
        public static double camera = 0;
        public static double pushSamp = 0;

        //TODO: Implement set-intake-pivot
        public static Lambda setPos(double pos) {
            return new Lambda("set-intake-pivot")
                    .setInit(() -> {});
        }
    }

    public static class IntakeWrist{
        public static double home = 0;
        public static double init = home;
        public static double intake = home;
        public static double camera = 0;
        public static double pushSamp = 0;

        //TODO: Implement set-intake-wrist
        public static Lambda setPos(double pos) {
            return new Lambda("set-intake-wrist")
                    .setInit(() -> {});
        }
    }

    public Differential() {}

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
        HardwareMap hMap = opMode.getOpMode().hardwareMap;
        diffLeft = hMap.get(Servo.class, "diffLeft");
        diffRight = hMap.get(Servo.class, "diffRight");
    }

    @Override
    public void postUserInitHook(@NonNull Wrapper opMode) {}

    @Override
    public void preUserStartHook(@NonNull Wrapper opMode) {}

    @Override
    public void postUserLoopHook(@NonNull Wrapper opMode) {}
}
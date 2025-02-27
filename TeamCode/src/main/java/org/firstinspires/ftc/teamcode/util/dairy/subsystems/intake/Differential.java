package org.firstinspires.ftc.teamcode.util.dairy.subsystems.intake;

import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.util.Util;

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

    private static double wrist = .5;
    private static double pivot = .5;

    //TODO: Tune these values;
    static Util.Scale pivotScale = new Util.Scale(0.25, .75);
    static Util.Scale wristScale = new Util.Scale(-0.25, 0.25);
    static double offsetR = 0;
    static double offsetL = 0;

    public static void setPositions(){
        double rightPos = pivotScale.scale(pivot) - wristScale.scale(wrist) + offsetR;
        double leftPos = pivotScale.scale(pivot) + wristScale.scale(wrist) + offsetL;
        diffRight.setPosition(rightPos);
        diffLeft.setPosition(leftPos);
    }

    public static class IntakePivot{
        public static double home = 0.5;
        public static double init = home;
        public static double intake = 0.5;
        public static double camera = 0.5;
        public static double pushSamp = 0.5;

        public static Lambda setPos(double pos) {
            return new Lambda("set-intake-pivot")
                    .setInit(() -> {
                        pivot = pos;
                        setPositions();
                    });
        }
    }

    public static class IntakeWrist{
        public static double home = 0;
        public static double init = home;
        public static double intake = home;
        public static double camera = 0;
        public static double pushSamp = 0;

        public static Lambda setPos(double pos) {
            return new Lambda("set-intake-wrist")
                    .setInit(() -> {
                        wrist = pos;
                        setPositions();
                    });
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
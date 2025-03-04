package org.firstinspires.ftc.teamcode.util.dairy.subsystems.outtake;

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
public class OuttakePivot implements Subsystem {
    public static final OuttakePivot INSTANCE = new OuttakePivot();
    public static Servo pivot;

    public static double home = .575; //.575 0
    public static double teleopTransfer = 0.65;
    public static double homeOuttakeBack = home; //.575 0
    public static double outtakeFront = 0.70;

    public static double init = outtakeFront;
    public static double outtakeBack = .82;
    public static double dropSamp = 0.65;
    public static double bucket = .575;
    public static double intakeBack = .675;

    public OuttakePivot() {}

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
        pivot = hMap.get(Servo.class, "pivot");
    }

    @Override
    public void postUserInitHook(@NonNull Wrapper opMode) {

    }

    @Override
    public void preUserStartHook(@NonNull Wrapper opMode) {
    }

    @Override
    public void postUserLoopHook(@NonNull Wrapper opMode) {

    }
    public static Lambda setPos(double pos) {
        return new Lambda("set-outtake-pivot-pos")
                .setInit(() -> {
                    pivot.setPosition(pos);
                });
    }
}
package org.firstinspires.ftc.teamcode.util.dairy.subsystems.outtake;

import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.config.Config;
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
public class OuttakeClaw implements Subsystem {
    public static final OuttakeClaw INSTANCE = new OuttakeClaw();
    public static Servo gripper;
    public static double open = 0.875;
    public static double closeFirm = 1;
    public static double closeLoose = 0.15;

    public OuttakeClaw() {}

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
        gripper = opMode.getOpMode().hardwareMap.get(Servo.class, "claw");
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
    public static Lambda open() {
        return new Lambda("open-outtake-claw")
                .setInit(() -> gripper.setPosition(open));
    }

    public static Lambda closeFirm() {
        return new Lambda("close-outtake-claw-firm")
                .setInit(() -> gripper.setPosition(closeFirm));
    }

    public static Lambda closeLoose() {
        return new Lambda("close-outtake-claw-loose")
                .setInit(() -> gripper.setPosition(closeLoose));
    }
}
package org.firstinspires.ftc.teamcode.util.dairy.subsystems.intake;

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
import dev.frozenmilk.mercurial.commands.Command;
import dev.frozenmilk.mercurial.commands.Lambda;
import dev.frozenmilk.mercurial.commands.util.IfElse;
import dev.frozenmilk.mercurial.subsystems.Subsystem;
import kotlin.annotation.MustBeDocumented;
@Config
public class IntakeClaw implements Subsystem {
    public static final IntakeClaw INSTANCE = new IntakeClaw();
    public static boolean isOpen = false;
    public static Servo gripper;
    public static double open = .6;
    public static double closeFirm = 0.325;
    public static double closeLoose = 0.4;

    public IntakeClaw() {}

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
        gripper = opMode.getOpMode().hardwareMap.get(Servo.class, "gripper");
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
        return new Lambda("open-intake-claw")
                .setInit(() -> {
                    gripper.setPosition(open);
                    isOpen = true;
                });
    }

    public static Lambda closeFirm() {
        return new Lambda("close-intake-claw-firm")
                .setInit(() -> {
                    gripper.setPosition(closeFirm);
                    isOpen = false;
                });
    }

    public static Lambda closeLoose() {
        return new Lambda("close-intake-claw-loose")
                .setInit(() -> {
                    gripper.setPosition(closeLoose);
                    isOpen = false;
                });
    }

    public static Command toggle(boolean isLoose){
        return new IfElse(
                    () -> isOpen,
                    new IfElse(
                            () -> isLoose,
                            closeLoose(),
                            closeFirm()
                    ),
                    open()
                );
    }
}
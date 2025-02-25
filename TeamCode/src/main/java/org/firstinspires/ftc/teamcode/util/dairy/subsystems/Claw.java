package org.firstinspires.ftc.teamcode.util.dairy.subsystems;

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
import dev.frozenmilk.mercurial.commands.groups.Sequential;
import dev.frozenmilk.mercurial.commands.util.Wait;
import dev.frozenmilk.mercurial.subsystems.Subsystem;
import kotlin.annotation.MustBeDocumented;
@Config
public class Claw implements Subsystem {
    public static final Claw INSTANCE = new Claw();
    public static Servo diffRight;
    public static Servo diffLeft;
    public static Servo gripper;
    public static Servo dropL;
    public static Servo dropR;
    public static double gripperOpenPos = 1;
    public static double gripperFirmClosePos = 0.325;
    public static double gripperLooseClosePos = 0.35;
    public static double diffTransferPos = 0.51;
    public static double diffGrabPos = 0.45;
    public static double wristGrabPos = 0.05;
    public static double wristTransferPos = 0.05;
    public static double dropGrabPos = 0.015;
    public static double dropTransferPos = 0.55;
    public static double dropPreGrabPos = 0.048;
    public static double dropHoverPos = 1;
    public static Sequential preIntake = new Sequential(
            Outtake.setPivot(Outtake.pivotPreTransferPos),
            Outtake.setArm(Outtake.armPreTransferPos),
            Outtake.openClaw(),
            openGripper(),
            setDrop(dropPreGrabPos),
            setDiff(diffGrabPos, wristGrabPos)
    );
    public static Sequential dropGrab = new Sequential(
            setDrop(dropGrabPos),
            new Wait(0.15),
            closeGripperLoose(),
            new Wait(0.1),
            setDrop(dropPreGrabPos)
    );
    public static Sequential preTransfer = new Sequential(
            setDrop(dropTransferPos),
            setDiff(diffTransferPos, wristTransferPos),
            Outtake.setPivot(Outtake.pivotPreTransferPos),
            Outtake.setArm(Outtake.armPreTransferPos)
    );
    public static Sequential transfer = new Sequential(
            Outtake.setPivot(Outtake.pivotTransferPos),
            Outtake.setArm(Outtake.armTransferPos),
            new Wait(0.1),
            Outtake.closeClaw(),
            openGripper()
    );


    public Claw() {}

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
        diffLeft = opMode.getOpMode().hardwareMap.get(Servo.class, "diffLeft");
        diffRight = opMode.getOpMode().hardwareMap.get(Servo.class, "diffRight");
        dropL = opMode.getOpMode().hardwareMap.get(Servo.class, "dropdownL");
        dropR = opMode.getOpMode().hardwareMap.get(Servo.class, "dropdownR");
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
    public static Lambda setDiff(double diffPos, double wristPos){
        return new Lambda("set-diff")
                .setInit(() -> {
                    diffLeft.setPosition(1-diffPos+wristPos);
                    diffRight.setPosition(diffPos+wristPos);
                });
    }
    public static Lambda openGripper() {
        return new Lambda("open-gripper")
                .setInit(() -> gripper.setPosition(gripperOpenPos));
    }
    public static Lambda closeGripperLoose() {
        return new Lambda("close-gripper")
                .setInit(() -> gripper.setPosition(gripperLooseClosePos));
    }
    public static Lambda closeGripperFirm() {
        return new Lambda("close-gripper")
                .setInit(() -> gripper.setPosition(gripperFirmClosePos));
    }
    public static Lambda incrementWrist(double wristPos) {
        return new Lambda("increment-wrist")
                .setInit(() -> {
                    diffLeft.setPosition(diffLeft.getPosition() + wristPos);
                    diffRight.setPosition(diffRight.getPosition() + wristPos);
                });
    }
    public static Lambda setDrop(double dropPos) {
        return new Lambda("set-drop")
                .setInit(() -> {
                    dropL.setPosition(dropPos);
                    dropR.setPosition(dropPos);
                });

    }

}
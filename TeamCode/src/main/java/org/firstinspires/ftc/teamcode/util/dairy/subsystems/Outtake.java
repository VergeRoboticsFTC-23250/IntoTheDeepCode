package org.firstinspires.ftc.teamcode.util.dairy.subsystems;

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
public class Outtake implements Subsystem {
    public static final Outtake INSTANCE = new Outtake();

    public static Servo armR;
    public static Servo armL;

    public static Servo claw;

    public static Servo pivot;

    public static double clawOpenPos = 0;
    public static double clawClosePos = 0.76;

    public static double armSubmersiblePos = 0.0;
    public static double armHomePos = 0.33;
    public static double armBucketPos = 0.8;
    public static double armTransferPos = 0.2;
    public static double armSpecPos = 0.5;
    public static double armTransitionPos = 1;

    //pivot
    public static double pivotSubmersiblePos = 0.825;
    public static double pivotSpecPos = 0.75;
    public static double pivotHomePos = 0.825;
    public static double pivotBucketPos = 0.86;
    public static double pivotTranferPos = 0.75; // TODO

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
    public void setDependency(@NonNull Dependency<?> dependency) { this.dependency = dependency; }

    @Override
    public void preUserInitHook(@NonNull Wrapper opMode) {
        HardwareMap hMap = opMode.getOpMode().hardwareMap;
        armR = hMap.get(Servo.class, "armR");
        armL = hMap.get(Servo.class, "armL");

        claw = hMap.get(Servo.class, "claw");

        pivot = hMap.get(Servo.class, "pivot");

        setPivot(Outtake.pivotHomePos);
        setArm(Outtake.armHomePos);
        openClaw();
    }

    @Override
    public void postUserLoopHook(@NonNull Wrapper opMode) {}

    public static Lambda openClaw(){
        return new Lambda("open-claw")
                .addRequirements(INSTANCE.claw)
                .setInit(() -> setClaw(clawOpenPos))
                .setFinish(() -> true);
    }

    public static Lambda closeClaw(){
        return new Lambda("close-claw")
                .addRequirements(INSTANCE.claw)
                .setExecute(() -> setClaw(clawClosePos))
                .setFinish(() -> true);
    }

    private static void setPosition(double pos) {
        armL.setPosition(pos);
        armR.setPosition(pos);
    }
    private static void setClaw(double pos){
        claw.setPosition(pos);
    }

    public static Lambda setArm(double pos){
        return new Lambda("set-arm")
                .addRequirements(INSTANCE.armL, INSTANCE.armR)
                .setInit(() -> {
                    setPosition(pos);
                });
    }

    public static Lambda setPivot(double pos){
        return new Lambda("set-pivot")
                .addRequirements(INSTANCE.pivot)
                .setInit(() -> pivot.setPosition(pos));
    }
}
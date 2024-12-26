package org.firstinspires.ftc.teamcode.util.dairy.subsystems;

import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.NextLock;

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
    public static double clawClosePos = 0.35;

    public static double armSubmersiblePos = 0.15;
    public static double armSampleIntakePos = 0;
    public static double armHomePos = 0.33;
    public static double armBucketPos = 0.9;
    public static double armTransferPos = 0; // TODO

    //pivot
    public static double pivotSubmersiblePos = 0.2;
    public static double pivotSampleIntakePos = 0.225;
    public static double pivotHomePos = 0.225;
    public static double pivotBucketPos = 0.3;
    public static double pivotTranferPos = 0; // TODO

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
        armR = hMap.get(Servo.class, "arm1");
        armL = hMap.get(Servo.class, "arm2");

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
                .setInit(() -> setClaw(clawOpenPos))
                .setFinish(() -> true);
    }

    public static Lambda closeClaw(){
        return new Lambda("close-claw")
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
                .addRequirements(INSTANCE)
                .setInit(() -> {
                    setPosition(pos);
                });
    }

    public static Lambda transfer() {
        return new Lambda("transfer-outtake")
                .addRequirements(INSTANCE)
                .setInit(() -> setPosition(pivotTranferPos));
    }

    public static Lambda setPivot(double pos){
        return new Lambda("set-pivot")
                .addRequirements(INSTANCE)
                .setInit(() -> pivot.setPosition(pos));
    }
}
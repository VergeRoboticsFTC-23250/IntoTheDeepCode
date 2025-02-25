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

    public static boolean isClawOpen = true;

    public static double clawOpenPos = 0.1;
    public static double clawClosePos = 0.89;
    public static double clawPartiallyClosePos = 0.7;

    public static double armSubmersiblePos = 0.0;
    public static double armOuttakeSpec = .9;
    public static double armHomePos = 0.575; //0.25
    public static double armBucketPos = 1;
    public static double armPreTransferPos = .19;
    public static double armTransferPos = 0.24;
    public static double armSpecPos = 0.55+.16;

    //pivot
    public static double pivotSubmersiblePos = 0.79;
    public static double pivotSpecPos = 0.77;
    public static double pivotOuttakeSpec = 0.86;
    public static double pivotHomePos = 1;
    public static double pivotBucketPos = 0.86;
    public static double pivotPreTransferPos = 0;
    public static double pivotTransferPos = 0.1;

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

        claw.setDirection(Servo.Direction.REVERSE);

        armR.setDirection(Servo.Direction.REVERSE);
        armL.setDirection(Servo.Direction.REVERSE);

        setPivot(Outtake.pivotHomePos);
        setArm(Outtake.armHomePos);
        setClaw(clawOpenPos);
    }

    @Override
    public void postUserStartHook(@NonNull Wrapper opMode) {
//        if (Robot.flavor.equals(OpModeMeta.Flavor.TELEOP)){
//            Robot.setState(Robot.State.HOME).schedule();
//        }

    }

    @Override
    public void postUserLoopHook(@NonNull Wrapper opMode) {}

    public static Lambda toggleClaw() {
        return new Lambda("toggle-claw")
                .addRequirements(INSTANCE.claw)
                .setInit(() -> {
                    if (isClawOpen) {
                        setClaw(clawClosePos);
                    } else {
                        setClaw(clawOpenPos);
                    }
                })
                .setEnd((interrupted) -> {
                    if (!interrupted) {
                        isClawOpen = !isClawOpen;
                    }
                });
    }

    public static Lambda openClaw(){
        return new Lambda("open-claw")
                .setInit(() -> {
                    setClaw(clawOpenPos);
                    isClawOpen = true;
                })
                .setFinish(() -> true);
    }

    public static Lambda closeClaw(){
        return new Lambda("close-claw")
                .setInit(() -> {
                    setClaw(clawClosePos);
                    isClawOpen = false;
                })
                .setFinish(() -> true);
    }
    public static Lambda closeClawPartially(){
        return new Lambda("close-claw-partially")
                .setInit(() -> {
                    setClaw(clawPartiallyClosePos);
                    isClawOpen = false;
                })
                .setFinish(() -> true);
    }

    public static void setPosition(double pos) {
        armL.setPosition(pos);
        armR.setPosition(pos);
    }
    public static void setClaw(double pos){
        claw.setPosition(pos);
    }
    public static void setPivotManual(double pos){
        pivot.setPosition(pos);
    }

    public static Lambda setArm(double pos) {
        return new Lambda("set-arm")
//                .addRequirements(INSTANCE.armL, INSTANCE.armR)
                .setInit(() -> {
                    setPosition(pos);
                });
    }

    public static Lambda setPivot(double pos){
        return new Lambda("set-pivot")
//                .addRequirements(INSTANCE.pivot)
                .setInit(() -> pivot.setPosition(pos));
    }
}
package org.firstinspires.ftc.teamcode.util.dairy.subsystems.intake;

import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.config.Config;
import com.pedropathing.localization.Pose;
import com.pedropathing.pathgen.BezierLine;
import com.pedropathing.pathgen.Point;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.util.Util;
import org.firstinspires.ftc.teamcode.util.dairy.Paths;
import org.firstinspires.ftc.teamcode.util.dairy.Robot;
import org.firstinspires.ftc.teamcode.util.dairy.subsystems.Chassis;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import dev.frozenmilk.dairy.core.FeatureRegistrar;
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

    public static double wrist = .5;
    public static double pivot = .5;

    public static double pivotRange = 0.2;
    public static double wristRange = 0.2025;
    static Util.Scale pivotScale = new Util.Scale(0.5-pivotRange, 0.5+pivotRange);
    static Util.Scale wristScale = new Util.Scale(-wristRange, wristRange);
    static double offsetR = 0.075;
    static double offsetL = 0;

    public static void setPositions(){
        double rightPos = pivotScale.scale(pivot) - wristScale.scale(wrist) + offsetR;
        double leftPos = pivotScale.scale(pivot) + wristScale.scale(wrist) + offsetL;
        diffRight.setPosition(rightPos);
        diffLeft.setPosition(leftPos);
        diffLeft.setDirection(Servo.Direction.REVERSE);
    }

    public static class IntakePivot{
        public static double home = 1;
        public static double homeOuttakeBack = home;
        public static double init = home;
        public static double intake = 0.25;
        public static double intakeGroundSecondary = 0.35;
        public static double camera = 0;
        public static double pushSamp = 0.375;

        public static Lambda setPos(double pos) {
            return new Lambda("set-intake-pivot")
                    .setInit(() -> {
                        pivot = pos;
                        setPositions();
                    });
        }
    }

    public static class IntakeWrist{
        public enum Direction{
            COUNTER_CLOCKWISE,
            CLOCKWISE,
        }
        public static double home = 0.5;
        public static double init = home;
        public static double intake = home;
        public static double camera = 0.5;
        public static double pushSamp = 0.5;
        public static boolean isAutoAligned = false;

        public static Lambda setPos(double pos) {
            return new Lambda("set-intake-wrist")
                    .setInit(() -> {
                        wrist = pos;
                        setPositions();
                    });
        }

        public static Lambda autoAlign() {
            return new Lambda("auto-align")
                    .setInit(() -> {
                        if(Robot.vision.isSampleVisible()){
                            wrist = 1 - Robot.vision.getAngle();
                            setPositions();
                        }
                    });
        }

        public static Lambda increment(Direction direction){
            return new Lambda("increment-pivot-by-direction")
                    .setInit(() -> {
                        if(Robot.getCurrentIntakeState() == Robot.State.INTAKE_GROUND){
                            wrist = Math.min(Math.max(wrist + (direction == Direction.CLOCKWISE? 0.25 : -0.25), 0), 1);
                            setPositions();
                        }
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
    public void postUserLoopHook(@NonNull Wrapper opMode) {
    }
}
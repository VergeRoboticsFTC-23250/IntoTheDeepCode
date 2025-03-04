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
public class IntakeDropDown implements Subsystem {
    public static final IntakeDropDown INSTANCE = new IntakeDropDown();
    public static double intake = 0.1;
    public static double intakeGroundSecondary = 0.025;
    public static double home = .3275;
    public static double homeSafe = .4;
    public static double init = home;
    public static double homeOuttakeBack = home;
    public static double camera = 0.275;
    public static double pushSamp = 0;
    public static Servo dropL;
    public static Servo dropR;

    public static Lambda setPos(double pos){
        return new Lambda("set-intake-drop-down-position")
                .setInit(() -> {
                    dropL.setPosition(pos);
                    dropR.setPosition(pos);
                });
    }
    private IntakeDropDown() {}

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
    public void setDependency(@NonNull Dependency<?> dependency) {
        this.dependency = dependency;
    }

    @Override
    public void preUserInitHook(@NonNull Wrapper opMode) {
        HardwareMap hMap = opMode.getOpMode().hardwareMap;

        dropL = hMap.get(Servo.class, "dropdownL");
        dropR = hMap.get(Servo.class, "dropdownR");
    }

    @Override
    public void postUserLoopHook(@NonNull Wrapper opMode) {}

    @Override
    public void postUserStartHook(@NonNull Wrapper opMode) {}
}
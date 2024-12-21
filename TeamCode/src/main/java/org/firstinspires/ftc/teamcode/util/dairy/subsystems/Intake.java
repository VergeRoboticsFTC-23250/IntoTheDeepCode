package org.firstinspires.ftc.teamcode.util.dairy.subsystems;

import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.config.Config;
import com.arcrobotics.ftclib.command.SubsystemBase;
import com.qualcomm.robotcore.hardware.DcMotorEx;
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
public class Intake implements Subsystem {
    public static final Intake INSTANCE = new Intake();
    public static double dropPos = 1;
    public static double raisePos = 0;
    public static Servo dropL;
    public static Servo dropR;
    private Intake() {}

    private static DcMotorEx spintake;

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
        spintake = hMap.get(DcMotorEx.class, "spintake");
        //raise();
        //spin(0);
    }

    @Override
    public void postUserLoopHook(@NonNull Wrapper opMode) {}

    private static void drop(){
        dropL.setPosition(dropPos);
        dropR.setPosition(dropPos);
    }

    private static void raise(){
        dropL.setPosition(raisePos);
        dropR.setPosition(raisePos);
    }

    private static void spin(double power){
        spintake.setPower(power);
    }

    private static void stop(){
        spintake.setPower(0);
    }

    public static Lambda spintake(double power){
        return new Lambda("spintake")
                .addRequirements(INSTANCE)
                .setExecute(() -> spin(power))
                .setFinish(() -> true);
    }

    public static Lambda raiseIntake() {
        return new Lambda("raise-intake")
                .addRequirements(INSTANCE)
                .setInit(Intake::raise);
    }
    public static Lambda dropIntake() {
        return new Lambda("drop-intake")
                .addRequirements(INSTANCE)
                .setInit(Intake::drop);
    }
}
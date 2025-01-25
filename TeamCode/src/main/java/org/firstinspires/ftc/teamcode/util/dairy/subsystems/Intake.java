package org.firstinspires.ftc.teamcode.util.dairy.subsystems;

import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.config.Config;
import com.arcrobotics.ftclib.command.SubsystemBase;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.internal.opmode.OpModeMeta;
import org.firstinspires.ftc.teamcode.util.dairy.Robot;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import dev.frozenmilk.dairy.core.dependency.Dependency;
import dev.frozenmilk.dairy.core.dependency.annotation.SingleAnnotation;
import dev.frozenmilk.dairy.core.wrapper.Wrapper;
import dev.frozenmilk.mercurial.Mercurial;
import dev.frozenmilk.mercurial.commands.Lambda;
import dev.frozenmilk.mercurial.subsystems.Subsystem;
import kotlin.annotation.MustBeDocumented;

@Config
public class Intake implements Subsystem {
    public static final Intake INSTANCE = new Intake();
    public static double dropPos = 0.74;
    public static double hoverPos = 0.64;
    public static double raisePos = 0.46;
    public static double extraRaisePos = 0.35;
    public static Servo dropL;
    public static Servo dropR;
    public static DcMotorEx spintake;
    public static boolean raised;
    private Intake() {}

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

    }

    @Override
    public void postUserStartHook(@NonNull Wrapper opMode) {
//        raised = true;
    }

    private static void drop(){
        dropL.setPosition(dropPos);
        dropR.setPosition(dropPos);
        raised = false;
    }

    private static void raise(){
        dropL.setPosition(raisePos);
        dropR.setPosition(raisePos);
        raised = true;
    }

    private static void raiseExtra(){
        dropL.setPosition(extraRaisePos);
        dropR.setPosition(extraRaisePos);
        raised = true;
    }

    private static void setPos(double pos) {
        dropL.setPosition(pos);
        dropR.setPosition(pos);
        if (Math.abs(pos - dropPos) < 0.1) {
            raised = false;
        } else if (Math.abs(pos - raisePos) < 0.1){
            raised = true;
        }
    }

    private static void spin(double power){
        spintake.setPower(power);
    }

    private static void stop(){
        spintake.setPower(0);
    }

    public static Lambda spintake(double power){
        return new Lambda("spintake")
                .addRequirements(INSTANCE.spintake)
                .setExecute(() -> spin(power))
                .setFinish(() -> true);
    }

    public static Lambda raiseIntake() {
        return new Lambda("raise-intake")
                .addRequirements(INSTANCE.dropL, INSTANCE.dropR)
                .setInit(Intake::raise);
    }
    public static Lambda dropIntake() {
        return new Lambda("drop-intake")
                .addRequirements(INSTANCE.dropL, INSTANCE.dropR)
                .setInit(Intake::drop);
    }
    public static Lambda extraIntake() {
        return new Lambda("extra-intake")
                .addRequirements(INSTANCE.dropL, INSTANCE.dropR)
                .setInit(Intake::raiseExtra);
    }
    public static Lambda setIntake(double pos) {
        return new  Lambda("set-intake")
                .addRequirements(INSTANCE.dropL, INSTANCE.dropR)
                .setInit(() -> Intake.setPos(pos));
    }
}
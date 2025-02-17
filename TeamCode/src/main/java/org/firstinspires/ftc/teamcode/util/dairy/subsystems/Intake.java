package org.firstinspires.ftc.teamcode.util.dairy.subsystems;

import static java.lang.Double.NaN;

import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.config.Config;
import com.arcrobotics.ftclib.command.SubsystemBase;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
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
import dev.frozenmilk.mercurial.bindings.BoundBooleanSupplier;
import dev.frozenmilk.mercurial.commands.Lambda;
import dev.frozenmilk.mercurial.subsystems.Subsystem;
import kotlin.annotation.MustBeDocumented;

@Config
public class Intake implements Subsystem {
    public static final Intake INSTANCE = new Intake();
    public static double dropPos = 0.735;
    public static double flatPos = 0.82;
    public static double hoverPos = 0.64;
    public static double raisePos = 0.48;
    public static double extraRaisePos = 0.35;
    public static Servo dropL;
    public static Servo dropR;
    public static DcMotorEx spintake;
    public static boolean raised;
//    public static ColorSensor color;
//    public static DistanceSensor distance;
//    public static boolean prevHasSample;
//    public static boolean hasSample;
//    Gamepad.LedEffect.Builder yellow;
//    Gamepad.LedEffect.Builder blue;
//    Gamepad.LedEffect.Builder red;
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

        setPos(Intake.hoverPos);

//        color = hMap.get(ColorSensor.class, "color");
//        distance = hMap.get(DistanceSensor.class, "color");
//
//        yellow = new Gamepad.LedEffect.Builder().addStep(255,255,0,500);
//        blue = new Gamepad.LedEffect.Builder().addStep(0,0,255,500);
//        red = new Gamepad.LedEffect.Builder().addStep(255,0,0,500);


    }

    @Override
    public void postUserLoopHook(@NonNull Wrapper opMode) {
//        hasSample = false;
//
//        if (!Double.isNaN(distance.getDistance(DistanceUnit.MM))
//                && distance.getDistance(DistanceUnit.MM) < 75) {
//
//            hasSample = true;
//
//            if (!prevHasSample) {
//                opMode.getOpMode().gamepad1.rumble(400);
//                opMode.getOpMode().gamepad2.rumble(400);
//
//            }
//
//            if (color.red() > 2000 && color.green() > 2000) {
//                opMode.getOpMode().gamepad1.runLedEffect(yellow.build());
//                opMode.getOpMode().gamepad2.runLedEffect(yellow.build());
//            } else if (color.red() > 1500 && color.green() < 1150) {
//                opMode.getOpMode().gamepad1.runLedEffect(red.build());
//                opMode.getOpMode().gamepad2.runLedEffect(red.build());
//            } else if (color.red() < 900 && color.green() > 1000) {
//                opMode.getOpMode().gamepad1.runLedEffect(blue.build());
//                opMode.getOpMode().gamepad2.runLedEffect(blue.build());
//            }
//
//        }
//
//        prevHasSample = hasSample;
    }

    @Override
    public void postUserStartHook(@NonNull Wrapper opMode) {
        raised = false;
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

    public static void setPos(double pos) {
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
                .setExecute(() -> spin(power))
                .setFinish(() -> true);
    }

    public static Lambda raiseIntake() {
        return new Lambda("raise-intake")
                .setInit(Intake::raise);
    }
    public static Lambda dropIntake() {
        return new Lambda("drop-intake")
                .setInit(Intake::drop);
    }
    public static Lambda extraIntake() {
        return new Lambda("extra-intake")
                .setInit(Intake::raiseExtra);
    }
    public static Lambda setIntake(double pos) {
        return new  Lambda("set-intake")
                .setInit(() -> Intake.setPos(pos));
    }
}
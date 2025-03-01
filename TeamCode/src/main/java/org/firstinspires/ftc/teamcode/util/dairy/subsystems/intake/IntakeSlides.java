package org.firstinspires.ftc.teamcode.util.dairy.subsystems.intake;

import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.TouchSensor;

import org.firstinspires.ftc.robotcore.external.Telemetry;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.concurrent.atomic.AtomicLong;

import dev.frozenmilk.dairy.core.dependency.Dependency;
import dev.frozenmilk.dairy.core.dependency.annotation.SingleAnnotation;
import dev.frozenmilk.dairy.core.wrapper.Wrapper;
import dev.frozenmilk.mercurial.commands.Lambda;
import dev.frozenmilk.mercurial.commands.groups.Sequential;
import dev.frozenmilk.mercurial.subsystems.Subsystem;
import kotlin.annotation.MustBeDocumented;

@Config
public class IntakeSlides implements Subsystem {
    public static final IntakeSlides INSTANCE = new IntakeSlides();
    private static DcMotorEx extendo;
    private static TouchSensor touch;
    private static Telemetry telemetry;

    public static double constantPower = 0.1;
    public static int timeToExtendOrRetract = 400;

    public static boolean isExtended = false;

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
        telemetry = opMode.getOpMode().telemetry;

        extendo = hMap.get(DcMotorEx.class, "extendo");
        touch = hMap.get(TouchSensor.class, "touchSlide");

        reset();
        setPowerManual(-0.2);
    }

    @Override
    public void postUserLoopHook(@NonNull Wrapper opMode) {}

    public static void reset() {
        extendo.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        extendo.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
    }

    public static void setPowerManual(double power) {
        extendo.setPower(power);
    }

    public static Lambda extend(){
        AtomicLong startTime = new AtomicLong();
        return new Lambda("extend-intake")
                .setInit(() -> {
                    if(!isExtended){
                        extendo.setPower(1);
                        startTime.set(System.currentTimeMillis());
                    }
                })
                .setFinish(() -> isExtended || (System.currentTimeMillis() - startTime.get() > timeToExtendOrRetract))
                .setEnd((interrupted) -> {
                    extendo.setPower(constantPower);
                    isExtended = true;
                });
    }

    public static Lambda retract(){
        AtomicLong startTime = new AtomicLong();
        return new Lambda("retract-intake")
                .setInit(() -> {
                    if(isExtended){
                        extendo.setPower(-1);
                        startTime.set(System.currentTimeMillis());
                    }
                })
                .setFinish(() -> !isExtended || (System.currentTimeMillis() - startTime.get() > timeToExtendOrRetract) || touch.isPressed())
                .setEnd((interrupted) -> {
                    extendo.setPower(-constantPower);
                    isExtended = false;
                });
    }
}
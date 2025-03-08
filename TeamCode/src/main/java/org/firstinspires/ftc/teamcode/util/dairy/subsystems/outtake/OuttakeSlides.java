package org.firstinspires.ftc.teamcode.util.dairy.subsystems.outtake;

import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.config.Config;
import com.arcrobotics.ftclib.controller.PIDFController;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.PIDFCoefficients;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.CurrentUnit;

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
import dev.frozenmilk.mercurial.subsystems.Subsystem;
import kotlin.annotation.MustBeDocumented;

@Config
public class OuttakeSlides implements Subsystem {
    public static final OuttakeSlides INSTANCE = new OuttakeSlides();
    private static DcMotorEx slideR;
    private static DcMotorEx slideL;
    private static DcMotorEx encoder;
    private static Telemetry telemetry;
    private static volatile boolean enablePID = true;
    private static int tolerance = 3000;
    private static double currentLimit = 1700;
    public static int minPos = 0;
    public static int maxPos = 71000;
    public static int scoreOffset = 20000;
    public static int outtakeFront = 11000;
    public static int outtakeFrontAuto = 23000;
    public static int outtakeFrontSecondaryAuto = 11000;
    public static int outtakeBack = 10000;
    public static int bucket = maxPos;
    public static int home = minPos;
    public static int init = minPos;

    static PIDFCoefficients gains = new PIDFCoefficients(0.00018, 0, 0.000008, 0);
    public static PIDFController controller = new PIDFController(gains.p, gains.i, gains.d, gains.f);

    private OuttakeSlides() {}

    @Retention(RetentionPolicy.RUNTIME) @Target(ElementType.TYPE) @MustBeDocumented
    @Inherited
    public @interface Attach { }

    @Override
    public void preUserInitHook(@NonNull Wrapper opMode) {
        HardwareMap hMap = opMode.getOpMode().hardwareMap;
        telemetry = opMode.getOpMode().telemetry;
        slideR = hMap.get(DcMotorEx.class, "outtakeSR");
        slideL = hMap.get(DcMotorEx.class, "outtakeSL");
        encoder = hMap.get(DcMotorEx.class, "spintake");
        slideR.setCurrentAlert(currentLimit, CurrentUnit.MILLIAMPS);
        slideL.setCurrentAlert(currentLimit, CurrentUnit.MILLIAMPS);
        slideR.setDirection(DcMotorSimple.Direction.REVERSE);

        reset();

        controller.setTolerance(tolerance);

        setDefaultCommand(runPID());
    }

    @Override
    public void postUserInitHook(@NonNull Wrapper opMode) {}

    @Override
    public void postUserLoopHook(@NonNull Wrapper opMode) {}

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

    public static void setPower(double power){
        slideL.setPower(power);
        slideR.setPower(power);
    }
    public static boolean isOverCurrent() {
        return slideR.isOverCurrent() || slideL.isOverCurrent();
    }

    public static void reset() {
        slideL.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        slideR.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        encoder.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        encoder.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        controller.reset();
        controller.setSetPoint(0);
    }

    public static Lambda score(long millis){
        AtomicLong startTime = new AtomicLong();
        return new Lambda("outtake-with-time")
                .setInterruptible(true)
                .setInit(() -> {
                    startTime.set(System.currentTimeMillis());
                    enablePID = false;
                    setPower(1);
                })
                .setFinish(() -> System.currentTimeMillis() - startTime.get() > millis)
                .setEnd((interrupted) -> {
                    setPower(0);
                    controller.setSetPoint(getPos());
                    enablePID = true;
                });
    }

    public static Lambda runToPosition(int pos){
        return new Lambda("run-outtake-slides-to-pos")
                .setInterruptible(true)
                .setInit(() -> {
                    controller.setSetPoint(pos);
                })
                .setFinish(() -> controller.atSetPoint());
    }

    public static Lambda setPIDMultiplier(double power){
        return new Lambda("set-pid-aggressive")
                .setInterruptible(true)
                .setInit(() -> {
                    controller.setP(gains.p * power);
                    controller.setI(gains.i * power);
                    controller.setD(gains.d * power);
                    controller.setF(gains.f * power);
                });
    }

    public static Lambda resetPID(){
        return new Lambda("reset-pid")
                .setInterruptible(true)
                .setInit(() -> {
                    controller.setP(gains.p);
                    controller.setI(gains.i);
                    controller.setD(gains.d);
                    controller.setF(gains.f);
                });
    }

    public static Lambda waitForRunToPos(){
        return new Lambda("wait-for-outtake-slides-run-to-pos")
                .setInterruptible(true)
                .setInit(() -> {

                })
                .setFinish(() -> controller.atSetPoint());
    }

    public static double getPos(){
        return encoder.getCurrentPosition();
    }
    public static Lambda runPID() {
        return new Lambda("run-outtake-slide-pid")
                .addRequirements(INSTANCE)
                .setInterruptible(true)
                .setExecute(() -> {
                    if (enablePID) {
                        double power = controller.calculate(getPos());
                        setPower(power);
                    }
                })
                .setFinish(() -> false);
    }
    //TODO: Implement Home Command
    public static Lambda home() {
        return new Lambda("home-outtake")
                .setInit(() -> {
                    enablePID = false;
                    setPower(-0.5);
                })
                .setFinish(OuttakeSlides::isOverCurrent)
                .setEnd((interrupted) -> {
                    setPower(0);
                    reset();
                    enablePID = true;
                });
    }
}
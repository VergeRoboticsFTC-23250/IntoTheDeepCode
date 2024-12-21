package org.firstinspires.ftc.teamcode.util.dairy.features;

import com.arcrobotics.ftclib.controller.PIDFController;
import com.qualcomm.robotcore.hardware.DcMotorEx;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.util.dairy.subsystems.OuttakeSlides;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import dev.frozenmilk.dairy.core.Feature;
import dev.frozenmilk.dairy.core.dependency.Dependency;
import dev.frozenmilk.dairy.core.dependency.lazy.Yielding;
import dev.frozenmilk.dairy.core.wrapper.Wrapper;


public class PIDFService implements Feature {

    private Telemetry telemetry;

    private List<DcMotorEx> motors;
    private PIDFController controller;

    private Dependency<?> dependency = Yielding.INSTANCE;
    private boolean isEnabled = true;
    private boolean homed = false;

    public PIDFService(PIDFController controller, DcMotorEx... motorsIn) {
        init(controller, motorsIn);
        register();
    }


    private void update() {
        if (!isEnabled || (controller.atSetPoint())) {
            if (telemetry != null) {
                telemetry.addData("PIDF Target", getTarget());
                telemetry.addData("PIDF Position", motors.get(0).getCurrentPosition());
                telemetry.update();
            }

            return;
        }

//        if (getTarget() == 0) {
//            if (!homed && motors.stream().anyMatch(DcMotorEx::isOverCurrent)) {
//                reset();
//                homed = true;
//            }
//            if (telemetry != null) telemetry.addData("Homing", homed);
//            return;
//        }

        homed = false;
        double output = controller.calculate(motors.get(0).getCurrentPosition());
        setPower(output);
        if (telemetry != null) {
            telemetry.addData("PIDF Output", output);
            telemetry.addData("PIDF Enabled", isEnabled);
            telemetry.addData("PIDF Target", getTarget());
            telemetry.addData("PIDF Position", motors.get(0).getCurrentPosition());
            telemetry.update();
        }
    }

    public void setPower(double power) {
        motors.forEach(motor -> motor.setPower(power));
    }

    public void home() { setTarget(0); }

    private void reset() {
        motors.forEach(motor -> motor.setMode(DcMotorEx.RunMode.STOP_AND_RESET_ENCODER));
        motors.forEach(motor -> motor.setMode(DcMotorEx.RunMode.RUN_USING_ENCODER));

        controller.reset();
    }

    public double getTarget() {
        return controller.getSetPoint();
    }

    public void setTarget(int target) {
        controller.setSetPoint(target);
    }

    public boolean isEnabled() {
        return isEnabled;
    }

    public void setEnabled(boolean enabled) {
        isEnabled = enabled;
    }

    @Override
    public Dependency<?> getDependency() {
        return dependency;
    }

    public void init(PIDFController controller, DcMotorEx... motorsIn) {
        this.controller = controller;
        motors = Arrays.asList(motorsIn);
        for (int i = 0; i < motors.size(); i++) {
            motors.get(i).setDirection(DcMotorEx.Direction.FORWARD);
            motors.get(i).setMode(DcMotorEx.RunMode.STOP_AND_RESET_ENCODER);
            motors.get(i).setMode(DcMotorEx.RunMode.RUN_USING_ENCODER);
        }
        controller.setTolerance(10);
//        home();
        reset();
        homed = false;
    }

    @Override
    public void postUserInitHook(Wrapper opMode) {
        telemetry = opMode.getOpMode().telemetry;
        init(OuttakeSlides.controller, OuttakeSlides.slideL, OuttakeSlides.slideR);
        register();
    }

    @Override
    public void preUserStartHook(Wrapper opMode) {
        setTarget(0);
    }

    @Override
    public void preUserStopHook(Wrapper opMode) {
        reset();
    }

    @Override
    public void postUserLoopHook(Wrapper opMode) {
        update();
    }

    @Override
    public void cleanup(Wrapper opMode) {
        deregister();
    }

    @Override
    public void setDependency(Dependency<?> dependency) {
        this.dependency = dependency;
    }
}

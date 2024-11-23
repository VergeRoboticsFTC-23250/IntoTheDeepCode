package org.firstinspires.ftc.teamcode.util.ftclib.subsystems;

import android.util.Log;

import com.acmerobotics.dashboard.config.Config;
import com.arcrobotics.ftclib.command.SubsystemBase;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.CurrentUnit;
import org.firstinspires.ftc.teamcode.util.PIDController;

@Config
public class IntakeSlides extends SubsystemBase {
    DcMotorEx slide;
    Telemetry telemetry;
    public static class Gains{
        public double Kp = 0.01;
        public double Ki = 0;
        public double Kd = 0;
    }
    public static int maxPos = 1000;
    public static int minPos = 0;
    public static double zeroCurrent = 4;
    public static Gains GAINS = new Gains();
    PIDController pidController = new PIDController(GAINS.Kp, GAINS.Ki, GAINS.Kd, 0);

    public IntakeSlides(final HardwareMap hMap, final Telemetry telemetry){
        this.telemetry = telemetry;
        slide = hMap.get(DcMotorEx.class, "intakeSlide");
        slide.setCurrentAlert(zeroCurrent, CurrentUnit.AMPS);
    }

    public void setPower(double power){
        slide.setPower(power);
    }

    public boolean isOverCurrent(){
        return slide.isOverCurrent();
    }

    public void updatePID(){
        double power = pidController.getPower(slide.getCurrentPosition());
        slide.setPower(power);
    }
    public void resetEncoder(){
        slide.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        slide.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
    }
    public void resetPID(){
        pidController.reset();
    }

    public void setTargetPos(int pos){
        pidController.setReference(pos);
    }

    public void logCurrent(){
        telemetry.addData("isOver", this.isOverCurrent());
        telemetry.update();
    }

    public int getPos(){
        return slide.getCurrentPosition();
    }

    public void logPos(){
        telemetry.addData("Slide Pos", slide.getCurrentPosition());
        telemetry.update();
    }
}

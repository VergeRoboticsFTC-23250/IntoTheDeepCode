package org.firstinspires.ftc.teamcode.util.ftclib.subsystems;

import android.util.Log;

import com.acmerobotics.dashboard.config.Config;
import com.arcrobotics.ftclib.command.SubsystemBase;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.CurrentUnit;
import org.firstinspires.ftc.teamcode.util.PIDController;

@Config
public class OuttakeSlides extends SubsystemBase {
    DcMotorEx slideR;
    DcMotorEx slideL;
    Telemetry telemetry;
    public static class Gains{
        public double Kp = 0.005;
        public double Ki = 0;
        public double Kd = 0;
    }
    public static double zeroCurrent = 4;
    public static Gains GAINS = new Gains();
    PIDController pidController = new PIDController(GAINS.Kp, GAINS.Ki, GAINS.Kd, 0);

    public OuttakeSlides(final HardwareMap hMap, final Telemetry telemetry){
        this.telemetry = telemetry;
        slideR = hMap.get(DcMotorEx.class, "outtakeSR");
        slideL = hMap.get(DcMotorEx.class, "outtakeSL");
        slideR.setCurrentAlert(zeroCurrent, CurrentUnit.AMPS);
        slideL.setCurrentAlert(zeroCurrent, CurrentUnit.AMPS);
        slideR.setDirection(DcMotorSimple.Direction.FORWARD);
        slideL.setDirection(DcMotorSimple.Direction.REVERSE);
    }

    public void setPower(double power){
        slideR.setPower(power);
        slideR.setPower(power);
    }

    public boolean isOverCurrent(){
        return slideR.isOverCurrent() || slideL.isOverCurrent();
    }

    public void updatePID(){
        double power = pidController.getPower(getPos());
        setPower(power);
    }

    public void resetEncoder(){
        slideR.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        slideR.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        slideL.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        slideL.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
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
        return slideR.getCurrentPosition();
    }

    public void logPos(){
        telemetry.addData("Slide Pos", slideR.getCurrentPosition());
        telemetry.update();
    }
}

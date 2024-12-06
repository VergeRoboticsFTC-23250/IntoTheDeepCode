package org.firstinspires.ftc.teamcode.util.ftclib.subsystems;

import com.acmerobotics.dashboard.config.Config;
import com.arcrobotics.ftclib.command.SubsystemBase;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.navigation.CurrentUnit;
import org.firstinspires.ftc.teamcode.util.PIDController;

@Config
public class VerticalSlides extends SubsystemBase {
    DcMotorEx slideR;
    DcMotorEx slideL;
    public static class Gains{
        public double Kp = 0.005;
        public double Ki = 0;
        public double Kd = 0;
    }
    public static Gains GAINS = new Gains();
    PIDController controllerL = new PIDController(GAINS.Kp, GAINS.Ki, GAINS.Kd, 0);
    PIDController controllerR = new PIDController(GAINS.Kp, GAINS.Ki, GAINS.Kd, 0);
    public static double tolerance = 50;
    public static double homePower = -0.5;
    public static double homeCurrent = 2.5;
    public static int maxPos = 2700;
    public static int minPos = 0;

    public static double multiplierR = .97;
    public static double multiplierL = 1;

    public VerticalSlides(HardwareMap hMap){
        slideR = hMap.get(DcMotorEx.class, "outtakeSR");
        slideL = hMap.get(DcMotorEx.class, "outtakeSL");
        slideL.setDirection(DcMotor.Direction.REVERSE);
        slideR.setDirection(DcMotor.Direction.REVERSE);
        slideR.setCurrentAlert(homeCurrent, CurrentUnit.AMPS);
        slideL.setCurrentAlert(homeCurrent, CurrentUnit.AMPS);

        resetEncoders();
    }

    public boolean isBelowMin(){
        return getPos() < minPos;
    }

    public boolean isAboveMax(){
        return getPos() > maxPos;
    }

    public void setPower(double power){
        slideR.setPower(power);
        slideL.setPower(power);
    }

    public void setPowerSafe(double power){
        if((power < 0 && isBelowMin()) || (power > 0 && isAboveMax())){
            stop();
        }else{
            setPower(power);
        }
    }

    public void setPowerL(double power){
        slideL.setPower(power);
    }

    public void setPowerR(double power){
        slideR.setPower(power);
    }

    public void stop(){
        slideR.setPower(0);
        slideL.setPower(0);
    }

    public int getPosL(){
        return (int)(slideL.getCurrentPosition() * multiplierL);
    }

    public int getPosR(){
        return (int)(slideR.getCurrentPosition() * multiplierR);
    }

    public int getPos(){
        return (getPosL() + getPosR()) / 2;
    }

    public boolean isAtPosL(int pos){
        return Math.abs(getPosL() - pos) < tolerance;
    }

    public boolean isAtPosR(int pos){
        return Math.abs(getPosR() - pos) < tolerance;
    }

    public boolean isAtPos(int pos){
        return isAtPosL(pos) && isAtPosR(pos);
    }

    public boolean isHomedR(){
        return slideR.isOverCurrent();
    }

    public boolean isHomedL(){
        return slideL.isOverCurrent();
    }

    public boolean isHomed(){
        return isHomedL() && isHomedR();
    }

    public void resetEncoderL(){
        slideL.setMode(DcMotorEx.RunMode.STOP_AND_RESET_ENCODER);
        slideL.setMode(DcMotorEx.RunMode.RUN_WITHOUT_ENCODER);
    }

    public void resetEncoderR(){
        slideR.setMode(DcMotorEx.RunMode.STOP_AND_RESET_ENCODER);
        slideR.setMode(DcMotorEx.RunMode.RUN_WITHOUT_ENCODER);
    }

    public void resetEncoders(){
        resetEncoderL();
        resetEncoderR();
    }

    public void resetPID(){
        controllerL.reset();
        controllerR.reset();
    }

    public void updatePID(){
        slideR.setPower(controllerR.getPower(getPosR()));
        slideL.setPower(controllerL.getPower(getPosL()));
    }

    public void setTargetPos(int pos){
        setTargetPosL(pos);
        setTargetPosR(pos);
    }

    public void setTargetPosL(int pos){
        controllerL.setReference(Math.max(pos, 0));
    }

    public void setTargetPosR(int pos){
        controllerR.setReference(Math.max(pos, 0));
    }
}

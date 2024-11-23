package org.firstinspires.ftc.teamcode.util.ftclib.subsystems;

import com.acmerobotics.dashboard.config.Config;
import com.arcrobotics.ftclib.command.SubsystemBase;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
@Config
public class Outtake extends SubsystemBase {
    Servo armR;
    Servo armL;

    Servo clawR;
    Servo clawL;

    Servo pivot;

    public static double clawOpenPos = 0;
    public static double clawClosePos = 0.4;

    public static double armSubmirsiblePos = 0;
    public static double armSampleIntakePos = 0;
    public static double armHomePos = 0.35;
    public static double armBucketPos = 0.9;

    //pivot
    public static double pivotSubmersiblePos = 0.5;
    public static double pivotSampleIntakePos = 0.225;
    public static double pivotHomePos = 0.225;
    public static double pivotBucketPos = 0.5;

    public Outtake(HardwareMap hMap) {
        armR = hMap.get(Servo.class, "arm1");
        armL = hMap.get(Servo.class, "arm2");

        clawR = hMap.get(Servo.class, "gripper1");
        clawL = hMap.get(Servo.class, "gripper2");
        clawL.setDirection(Servo.Direction.REVERSE);

        pivot = hMap.get(Servo.class, "pivot");

        setPivot(Outtake.pivotHomePos);
        setArm(Outtake.armHomePos);
        openClaw();
    }

    public void openClaw(){
        setClaw(clawOpenPos);
    }

    public void closeClaw(){
        setClaw(clawClosePos);
    }

    private void setClaw(double pos){
        clawR.setPosition(pos);
        clawL.setPosition(pos);
    }

    public void  setArm(double pos){
        armR.setPosition(pos);
        armL.setPosition(pos);
    }

    public void setPivot(double pos){
        pivot.setPosition(pos);
    }
}
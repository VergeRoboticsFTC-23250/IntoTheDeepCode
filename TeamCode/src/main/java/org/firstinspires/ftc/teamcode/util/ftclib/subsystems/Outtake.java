package org.firstinspires.ftc.teamcode.util.ftclib.subsystems;

import com.acmerobotics.dashboard.config.Config;
import com.arcrobotics.ftclib.command.SubsystemBase;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

@Config
public class Outtake extends SubsystemBase {
    Servo arm1;
    Servo arm2;
    Servo gripper1;
    Servo gripper2;
    Servo pivot;

    public static double clawOpenPos = 0.5;
    public static double clawClosePos = 0.8;

    public static boolean isClawClosed;

    public Outtake(HardwareMap hMap){
        arm1 = hMap.get(Servo.class, "arm1");
        arm2 = hMap.get(Servo.class, "arm2");
        gripper1 = hMap.get(Servo.class, "gripper1");
        gripper2 = hMap.get(Servo.class, "gripper2");
        gripper2.setDirection(Servo.Direction.REVERSE);
        pivot = hMap.get(Servo.class, "pivot");
    }

    public void setArm(double pos){
        arm1.setPosition(pos);
        arm2.setPosition(pos);
    }

    public void setPivot(double pos){
        pivot.setPosition(pos);
    }

    public void openClaw(){
        gripper1.setPosition(clawOpenPos);
        gripper2.setPosition(clawOpenPos);

        isClawClosed = false;
    }

    public void closeClaw(){
        gripper1.setPosition(clawClosePos);
        gripper2.setPosition(clawClosePos);

        isClawClosed = true;
    }

    public void setIsClawOpen(boolean isOpen){
        if(isOpen){
            openClaw();
        }else{
            closeClaw();
        }
    }

    public void toggleClaw(){
        isClawClosed = !isClawClosed;
        if(isClawClosed){
            openClaw();
        } else{
            closeClaw();
        }
    }
}

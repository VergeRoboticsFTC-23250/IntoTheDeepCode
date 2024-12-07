package org.firstinspires.ftc.teamcode.util.ftclib.subsystems;

import com.acmerobotics.dashboard.config.Config;
import com.arcrobotics.ftclib.command.SubsystemBase;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

@Config
public class Intake extends SubsystemBase {
    public static double dropPos = .65;
    public static double homePos = 0.3;
    public static double initPos = 0.1;
    Servo dropL;
    Servo dropR;
    DcMotorEx spintake;
    public Intake(HardwareMap hMap){
        dropR = hMap.get(Servo.class, "dropdownR");
        dropL = hMap.get(Servo.class, "dropdownL");
        dropL.setDirection(Servo.Direction.REVERSE);

        spintake = hMap.get(DcMotorEx.class, "spintake");

        home();
        spin(0);
    }

    public void drop(){
        dropL.setPosition(dropPos);
        dropR.setPosition(dropPos);
    }

    public void home(){
        dropL.setPosition(homePos);
        dropR.setPosition(homePos);
    }

    public void setInitPos(){
        dropL.setPosition(initPos);
        dropR.setPosition(initPos);
    }

    public void spin(double power){
        spintake.setPower(power);
    }
}

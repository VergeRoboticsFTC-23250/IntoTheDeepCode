package org.firstinspires.ftc.teamcode.util.ftclib.subsystems;

import com.acmerobotics.dashboard.config.Config;
import com.arcrobotics.ftclib.command.SubsystemBase;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

@Config
public class Intake extends SubsystemBase {
    public static double dropPos = 1;
    public static double raisePos = 0;
    Servo dropL;
    Servo dropR;

    DcMotorEx spintake;

    public Intake(HardwareMap hMap){
        dropL = hMap.get(Servo.class, "dropdownL");
        dropR = hMap.get(Servo.class, "dropdownR");
        spintake = hMap.get(DcMotorEx.class, "spintake");
        raise();
        spin(0);
    }

    public void drop(){
        dropL.setPosition(dropPos);
        dropR.setPosition(dropPos);
    }

    public void raise(){
        dropL.setPosition(raisePos);
        dropR.setPosition(raisePos);
    }

    public void spin(double power){
        spintake.setPower(power);
    }
}

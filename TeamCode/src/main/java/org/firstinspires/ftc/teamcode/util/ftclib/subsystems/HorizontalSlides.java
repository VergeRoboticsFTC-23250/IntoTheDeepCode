package org.firstinspires.ftc.teamcode.util.ftclib.subsystems;

import com.acmerobotics.dashboard.config.Config;
import com.arcrobotics.ftclib.command.SubsystemBase;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.Telemetry;

@Config
public class HorizontalSlides extends SubsystemBase {
    public static double homePos = 0.6;
    public static double maxPos = 0;
    Servo slideR;
    Servo slideL;
    public HorizontalSlides(HardwareMap hMap){
        slideR = hMap.get(Servo.class, "slideR");
        slideL = hMap.get(Servo.class, "slideL");
        home();
    }

    public void home(){
        slideR.setPosition(homePos);
        slideL.setPosition(homePos);
    }

    public void setPos(double p) {
        double pos = homePos - Math.max(0, Math.min(1, p)) * Math.abs(homePos - maxPos);
        slideR.setPosition(pos);
        slideL.setPosition(pos);
    }

    public void addPos(double pos){
        slideR.setPosition(Math.min(homePos, Math.max(maxPos, slideR.getPosition() - pos)));
        slideL.setPosition(Math.min(homePos, Math.max(maxPos, slideL.getPosition() - pos)));
    }
}
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
    public static double incrementPos = 0.00001;
    public static double currentPos;
    private Telemetry telemetry;
    Servo slideR;
    Servo slideL;
    public HorizontalSlides(HardwareMap hMap, Telemetry telemetry){
        slideR = hMap.get(Servo.class, "slideR");
        slideL = hMap.get(Servo.class, "slideL");

        this.telemetry = telemetry;
        home();
    }

    public void home(){
        slideR.setPosition(homePos);
        slideL.setPosition(homePos);
        currentPos = 0;
    }

    public void setPos(double p) {
        p = Math.max(0, Math.min(1, p)); // Clamp p to [0, 1]
        double pos = homePos - p * Math.abs(homePos - maxPos);
        slideR.setPosition(pos);
        slideL.setPosition(pos);
        currentPos = p;
    }


    public void setIncrement(double r1, double l1) {
        double newPos = currentPos + (incrementPos * r1 - incrementPos * l1);
        telemetry.addData("r1", r1);
        telemetry.addData("l1", l1);
        telemetry.addData("newPos", newPos);
        setPos(newPos);
    }

}
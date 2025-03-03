package org.firstinspires.ftc.teamcode.teleop;

import com.acmerobotics.dashboard.config.Config;
import com.pedropathing.pathgen.BezierLine;
import com.pedropathing.pathgen.Point;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.TouchSensor;

import org.firstinspires.ftc.teamcode.util.dairy.Paths;

@TeleOp
@Config
public class testop extends LinearOpMode {
    public static boolean invert = false;
    public static boolean enableS2 = false;
    public static String s1 = "gripper";
    public static String s2 = "dropdownR";
    public static double p1 = .6; // down
    public static double p2 = 0.3625; // up
    @Override
    public void runOpMode() {
        Servo servo = hardwareMap.get(Servo.class, s1);
        Servo servo2 = hardwareMap.get(Servo.class, s2);
        if (invert) servo.setDirection(Servo.Direction.REVERSE);
        waitForStart();


        while (opModeIsActive()){
            if (gamepad1.cross) {
                servo.setPosition(p1); //open
                if (enableS2){
                    servo2.setPosition(p1);
                }
            } else if (gamepad1.triangle) {
                servo.setPosition(p2);
                if (enableS2) servo2.setPosition(p2);
            }
        }
    }
}

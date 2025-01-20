package org.firstinspires.ftc.teamcode.teleop;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.Servo;

@TeleOp
@Config
public class testop extends LinearOpMode {
    public static boolean invertS = false;
    public static boolean enableS2 = true;
    public static String s1 = "dropdownL";
    public static String s2 = "dropdownR";
    public static String m = "spintake";
    public static String m2 = "outtakeSL";
    public static boolean invertM = true;
    public static boolean enableM2 = false;

    public static double square = 0.76;
    public static double circle = 0.475;

    @Override
    public void runOpMode() throws InterruptedException {

        Servo servo = hardwareMap.get(Servo.class, s1);
        Servo servo2 = hardwareMap.get(Servo.class, s2);
        if (invertS) servo.setDirection(Servo.Direction.REVERSE);

        DcMotorEx motor = hardwareMap.get(DcMotorEx.class, m);
        DcMotorEx motor2 = hardwareMap.get(DcMotorEx.class, m2);
        if (invertM) motor.setDirection(DcMotorEx.Direction.REVERSE);


        waitForStart();


        while (opModeIsActive()){
            motor.setPower(gamepad1.right_stick_y);
            if (enableM2){
                motor2.setPower(gamepad1.right_stick_y);
            }
            if (gamepad1.square) {
                servo.setPosition(square); //open
                if (enableS2){
                    servo2.setPosition(square);
                }
            } else if (gamepad1.circle) {
                servo.setPosition(circle);
                if (enableS2) servo2.setPosition(circle);
            }
        }
    }
}

package org.firstinspires.ftc.teamcode.teleop;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.Servo;

@TeleOp
@Config
public class testop extends LinearOpMode {
    public static boolean invert = false;
    public static String s1 = "arm1";
    public static String s2 = "arm2";
    public static String m = "spintake";
    @Override
    public void runOpMode() throws InterruptedException {

        Servo servo = hardwareMap.get(Servo.class, s1);
        Servo servo2 = hardwareMap.get(Servo.class, s2);
        DcMotorEx motor = hardwareMap.get(DcMotorEx.class, m);
        waitForStart();
        while (opModeIsActive()){
            servo.setPosition(invert? -gamepad1.left_stick_y / 2 + 0.5 : gamepad1.left_stick_y / 2 + 0.5);
            servo2.setPosition(gamepad1.left_stick_y / 2 + 0.5);
            motor.setPower(gamepad1.right_stick_y);
        }
    }
}

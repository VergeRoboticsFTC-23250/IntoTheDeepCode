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
    @Override
    public void runOpMode() throws InterruptedException {

        Servo servo = hardwareMap.get(Servo.class, "dropdownL");
        Servo servo2 = hardwareMap.get(Servo.class, "dropdownR");
        DcMotorEx motor = hardwareMap.get(DcMotorEx.class, "spintake");
        waitForStart();
        while (opModeIsActive()){
            servo.setPosition(invert? -gamepad1.left_stick_y / 2 + 0.5 : gamepad1.left_stick_y / 2 + 0.5);
            servo2.setPosition(gamepad1.left_stick_y / 2 + 0.5);
            motor.setPower(gamepad1.right_stick_y);
        }
    }
}

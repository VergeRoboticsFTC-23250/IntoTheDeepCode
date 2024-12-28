package org.firstinspires.ftc.teamcode.teleop;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.Servo;

@TeleOp
@Config
public class testop extends LinearOpMode {
    public static boolean invert = true;
    public static boolean enableS2 = false;
    public static String s1 = "claw";
    public static String s2 = "armL";
    public static String m = "extendo";
    @Override
    public void runOpMode() throws InterruptedException {

        Servo servo = hardwareMap.get(Servo.class, s1);
        Servo servo2 = hardwareMap.get(Servo.class, s2);
        if (invert) servo.setDirection(Servo.Direction.REVERSE);
        DcMotorEx motor = hardwareMap.get(DcMotorEx.class, m);
        waitForStart();
        while (opModeIsActive()){
            if (gamepad1.left_stick_y != 0){
                servo.setPosition(invert? -gamepad1.left_stick_y / 2 + 0.5 : gamepad1.left_stick_y / 2 + 0.5);
                if(enableS2){
                    servo2.setPosition(gamepad1.left_stick_y / 2 + 0.5);
                }
            }

            motor.setPower(gamepad1.right_stick_y);
            if (gamepad1.cross){
                servo.setPosition(0);
                if (enableS2){
                    servo2.setPosition(0);
                }
            } else if (gamepad1.triangle){
                servo.setPosition(1);
                if (enableS2){
                    servo2.setPosition(1);
                }
            } else if (gamepad1.square) {
                servo.setPosition(0.5);
                if (enableS2){
                    servo2.setPosition(0.5);
                }
            } else if (gamepad1.circle) {
                servo.setPosition(0.76);
                if (enableS2) servo2.setPosition(.76);
            }
        }
    }
}

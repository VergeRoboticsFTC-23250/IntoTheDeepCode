package org.firstinspires.ftc.teamcode.auto.testing;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.Servo;

@TeleOp
@Config
public class DiffyTesting extends OpMode {

    Servo leftDiff;
    Servo rightDiff;

    @Override
    public void init() {
        leftDiff = hardwareMap.get(Servo.class, "test");
        rightDiff = hardwareMap.get(Servo.class, "test2");
        rightDiff.setDirection(Servo.Direction.REVERSE);
    }

    @Override
    public void init_loop() {

    }

    @Override
    public void start() {
        leftDiff.setPosition(0);
        rightDiff.setPosition(0);
    }

    @Override
    public void loop() {
        if (gamepad1.cross) {
            leftDiff.setPosition(0);
            rightDiff.setPosition(0);
        }
        if (gamepad1.triangle) {
            leftDiff.setPosition(1);
            rightDiff.setPosition(1);
        }
        if (gamepad1.square) {
            leftDiff.setPosition(0.8);
            rightDiff.setPosition(0.8);
        }
        if (gamepad1.circle) {
            leftDiff.setPosition(0.5);
            rightDiff.setPosition(0.5);
        }
    }
}

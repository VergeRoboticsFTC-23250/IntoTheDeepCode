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
//        rightDiff.setDirection(Servo.Direction.REVERSE);
    }

    @Override
    public void init_loop() {

    }

    @Override
    public void start() {
        leftDiff.setPosition(0);
        rightDiff.setPosition(1);
    }

    @Override
    public void loop() {

    }
}

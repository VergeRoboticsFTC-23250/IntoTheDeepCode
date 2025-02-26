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
    public static double wristPos = 0.55;
    public static double wristPos2 = 0.45;
    public static double pivotPos = 0.4;
    public static double pivotPos2 = 0.6;

    @Override
    public void init() {
        leftDiff = hardwareMap.get(Servo.class, "test");
        rightDiff = hardwareMap.get(Servo.class, "test2");
    }

    @Override
    public void init_loop() {

    }

    @Override
    public void start() {
        rightDiff.setPosition(0.5);
        leftDiff.setPosition(0.5);
    }

    @Override
    public void loop() {
        telemetry.addData("left", leftDiff.getPosition());
        telemetry.addData("right", rightDiff.getPosition());
        FtcDashboard.getInstance().getTelemetry().addData("left", leftDiff.getPosition());
        FtcDashboard.getInstance().getTelemetry().addData("right", rightDiff.getPosition());
        FtcDashboard.getInstance().getTelemetry().update();
        if (gamepad1.cross) {
//            rightDiff.setDirection(Servo.Direction.FORWARD);
            rightDiff.setPosition(rightDiff.getPosition()+.05);
            leftDiff.setPosition(leftDiff.getPosition()+.05);
        } if (gamepad1.square) {
            rightDiff.setDirection(Servo.Direction.FORWARD);
            rightDiff.setPosition(1-pivotPos);
            leftDiff.setPosition(pivotPos);
        } if (gamepad1.circle) {
            rightDiff.setDirection((Servo.Direction.FORWARD));
            rightDiff.setPosition(1-pivotPos2);
            leftDiff.setPosition(pivotPos2);
        } if (gamepad1.triangle) {
            rightDiff.setDirection(Servo.Direction.FORWARD);
            rightDiff.setPosition(wristPos2);
            leftDiff.setPosition(wristPos2);
        }
    }
}

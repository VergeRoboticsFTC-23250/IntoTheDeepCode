package org.firstinspires.ftc.teamcode.teleop;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

import dev.frozenmilk.mercurial.commands.Lambda;

@TeleOp
public class ChassisDriving extends OpMode {
    DcMotor frontLeft;
    DcMotor frontRight;
    DcMotor backRight;
    DcMotor backLeft;

    @Override
    public void init() {
        frontLeft = hardwareMap.get(DcMotor.class, "leftFront");
        frontRight = hardwareMap.get(DcMotor.class, "rightFront");
        backLeft = hardwareMap.get(DcMotor.class, "leftBack");
        backRight = hardwareMap.get(DcMotor.class, "rightBack");

        frontLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        frontRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

    }

    /**
     * User-defined loop method
     * <p>
     * This method will be called repeatedly during the period between when
     * the play button is pressed and when the OpMode is stopped.
     */
    @Override
    public void loop() {
        if (gamepad1.dpad_up) {
            frontLeft.setPower(1);
            backLeft.setPower(1);
            frontRight.setPower(-1);
            backRight.setPower(-1);
        } else if (gamepad1.dpad_down) {
            frontLeft.setPower(-1);
            backLeft.setPower(-1);
            frontRight.setPower(1);
            backRight.setPower(1);
        }

        double y = -gamepad1.right_stick_y; // Remember, Y stick value is reversed
        double x = gamepad1.right_stick_x * 1.1; // Counteract imperfect strafing
        double rx = gamepad1.left_stick_x;

        // Denominator is the largest motor power (absolute value) or 1
        // This ensures all the powers maintain the same ratio,
        // but only if at least one is out of the range [-1, 1]
        double denominator = Math.max(Math.abs(y) + Math.abs(x) + Math.abs(rx), 1);
        double frontLeftPower = (y + x + rx) / denominator;
        double backLeftPower = (y - x + rx) / denominator;
        double frontRightPower = (y - x - rx) / denominator;
        double backRightPower = (y + x - rx) / denominator;

        boolean slow = gamepad1.right_bumper;

        frontLeft.setPower(slow? frontLeftPower * 0.5 : frontLeftPower);
        backLeft.setPower(slow? backLeftPower * 0.5 : backLeftPower);
        frontRight.setPower(slow? frontRightPower * 0.5 : frontRightPower);
        backRight.setPower(slow? backRightPower * 0.5: backRightPower);

    }
}

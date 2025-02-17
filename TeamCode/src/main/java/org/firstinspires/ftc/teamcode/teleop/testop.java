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
    public static String s1 = "armL";
    public static String s2 = "armR";
    public static String m = "leftFront";
    public static String m2 = "outtakeSL";
    public static boolean invertM = false;
    public static boolean enableM2 = false;
    public static BezierLine line;
    @Override
    public void runOpMode() throws InterruptedException {
        TouchSensor touch = hardwareMap.get(TouchSensor.class, "touchSlide");
        Servo servo = hardwareMap.get(Servo.class, s1);
        Servo servo2 = hardwareMap.get(Servo.class, s2);
        if (invert) servo.setDirection(Servo.Direction.REVERSE);

        DcMotorEx motor = hardwareMap.get(DcMotorEx.class, m);
        DcMotorEx motor2 = hardwareMap.get(DcMotorEx.class, m2);
        if (invertM) motor.setDirection(DcMotorEx.Direction.REVERSE);
        line = new BezierLine(
                new Point(9.000, 65.000, Point.CARTESIAN),
                new Point(40.00, 67.000, Point.CARTESIAN)
        );


        waitForStart();


        while (opModeIsActive()){
            if (gamepad1.left_stick_y != 0){
                servo.setPosition(invert? -gamepad1.left_stick_y / 2 + 0.5 : gamepad1.left_stick_y / 2 + 0.5);
                if(enableS2){
                    servo2.setPosition(gamepad1.left_stick_y / 2 + 0.5);
                }
            }
            telemetry.addData("touch pressed", touch.isPressed());
            motor.setPower(gamepad1.right_stick_y);
            if (enableM2){
                motor2.setPower(gamepad1.right_stick_y);
            }
            telemetry.addData("point x", line.getLastControlPoint().getX());
            telemetry.addData("point x", line.getLastControlPoint().getY());
            telemetry.update();
            if (gamepad1.square) {
                servo.setPosition(0); //open
                if (enableS2){
                    servo2.setPosition(0);
                }
            } else if (gamepad1.circle) {
                servo.setPosition(.715);
                if (enableS2) servo2.setPosition(0.715);
            }
        }
    }
}

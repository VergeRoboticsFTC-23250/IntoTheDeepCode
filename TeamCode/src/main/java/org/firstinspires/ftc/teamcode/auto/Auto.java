package org.firstinspires.ftc.teamcode.auto;


import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.Vector2d;
import com.acmerobotics.roadrunner.ftc.Actions;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.util.ftclib.subsystems.Chassis;

@Autonomous(name="Auto")
public class Auto extends LinearOpMode {
    private Chassis chassis;

    public static double outtakeDelay = 0.7;
    public static double intakeDelay = 1;

    public static double outtakePosX = 62;
    public static double outtakePosY = 57;
    public static double outtakeHeading = Math.toRadians(250);

    @Override
    public void runOpMode() throws InterruptedException {
        chassis = new Chassis(hardwareMap, new Pose2d(13,60,Math.toRadians(270)));


        waitForStart();

        Actions.runBlocking(chassis.md.actionBuilder(chassis.md.pose)
                .strafeTo(new Vector2d(10, 33))
                .waitSeconds(outtakeDelay)
                .lineToY(40)
                .strafeToLinearHeading(new Vector2d(54,47),Math.toRadians(250))
                .waitSeconds(intakeDelay)
                .strafeToLinearHeading(new Vector2d(outtakePosX,outtakePosY),outtakeHeading)
                .waitSeconds(outtakeDelay)
                .strafeToLinearHeading(new Vector2d(58,47),Math.toRadians(270))
                .waitSeconds(intakeDelay)
                .strafeToLinearHeading(new Vector2d(outtakePosX,outtakePosY),outtakeHeading)
                .waitSeconds(outtakeDelay)
                .strafeToLinearHeading(new Vector2d(53,44),Math.toRadians(315))
                .waitSeconds(intakeDelay)
                .strafeToLinearHeading(new Vector2d(outtakePosX,outtakePosY),outtakeHeading)
                .waitSeconds(outtakeDelay)
                .build());
        // Add your code here
    }
}

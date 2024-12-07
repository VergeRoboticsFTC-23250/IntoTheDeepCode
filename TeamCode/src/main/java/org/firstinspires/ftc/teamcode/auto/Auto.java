package org.firstinspires.ftc.teamcode.auto;


import com.acmerobotics.roadrunner.Vector2d;
import com.acmerobotics.roadrunner.ftc.Actions;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.util.ftclib.subsystems.Chassis;

@Autonomous(name="Auto")
public class Auto extends LinearOpMode {
    private Chassis chassis;

    @Override
    public void runOpMode() throws InterruptedException {
        chassis = new Chassis(hardwareMap);

        waitForStart();

        Actions.runBlocking(chassis.md.actionBuilder(chassis.md.pose)
                .strafeTo(new Vector2d(10, 33))
                .waitSeconds(.7)
                .lineToY(40)
                .strafeToLinearHeading(new Vector2d(54,47),Math.toRadians(250))
                .waitSeconds(1)
                .strafeToLinearHeading(new Vector2d(60,58),Math.toRadians(240))
                .waitSeconds(.7)
                .strafeToLinearHeading(new Vector2d(58,47),Math.toRadians(270))
                .waitSeconds(1)
                .strafeToLinearHeading(new Vector2d(60,58),Math.toRadians(250))
                .waitSeconds(.7)
                .strafeToLinearHeading(new Vector2d(53,44),Math.toRadians(315))
                .waitSeconds(1)
                .strafeToLinearHeading(new Vector2d(62,57),Math.toRadians(250))
                .waitSeconds(.7)
                .build());
        // Add your code here
    }
}

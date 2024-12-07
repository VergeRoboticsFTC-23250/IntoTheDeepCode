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
        waitForStart();
        Actions.runBlocking(chassis.md.actionBuilder(chassis.md.pose)
                .strafeTo(new Vector2d(-10, 33))
                .build());
        // Add your code here
    }
}

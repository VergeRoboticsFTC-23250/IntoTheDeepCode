package org.firstinspires.ftc.teamcode.util.ftclib.commands;

import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.ftc.Actions;
import com.arcrobotics.ftclib.command.CommandBase;

import org.firstinspires.ftc.teamcode.util.ftclib.subsystems.Chassis;

public class DriveTo extends CommandBase {
    Chassis chassis;
    Pose2d pose;
    public DriveTo(Chassis chassis, Pose2d pose) {
        this.chassis = chassis;
        this.pose = pose;
        addRequirements(chassis);
    }

    @Override
    public void execute() {
        Actions.runBlocking(
                chassis.rr.actionBuilder(chassis.rr.pose)
                        .splineToLinearHeading(pose, pose.heading)
                        .build()
        );
    }

    @Override
    public boolean isFinished() {
        return true;
    }
}

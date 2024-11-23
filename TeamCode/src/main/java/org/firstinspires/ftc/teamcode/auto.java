package org.firstinspires.ftc.teamcode;

import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.Trajectory;
import com.acmerobotics.roadrunner.Vector2d;
import com.acmerobotics.roadrunner.ftc.Actions;
import com.arcrobotics.ftclib.command.CommandOpMode;
import com.arcrobotics.ftclib.command.CommandScheduler;
import com.arcrobotics.ftclib.command.InstantCommand;

import org.firstinspires.ftc.teamcode.util.ftclib.commands.Movement;
import org.firstinspires.ftc.teamcode.util.ftclib.commands.RunIntakeSlidesPID;
import org.firstinspires.ftc.teamcode.util.ftclib.commands.RunOuttakeSlidesPID;
import org.firstinspires.ftc.teamcode.util.ftclib.subsystems.Chassis;
import org.firstinspires.ftc.teamcode.util.ftclib.subsystems.Intake;
import org.firstinspires.ftc.teamcode.util.ftclib.subsystems.IntakeSlides;
import org.firstinspires.ftc.teamcode.util.ftclib.subsystems.Outtake;
import org.firstinspires.ftc.teamcode.util.ftclib.subsystems.OuttakeSlides;

public class auto extends CommandOpMode {
    private Chassis chassis;
    private IntakeSlides intakeSlides;
    private OuttakeSlides outtakeSlides;
    private Intake intake;
    private Outtake outtake;

    Trajectory runToCenter;

    @Override
    public void initialize() {
        chassis = new Chassis(hardwareMap);

        intakeSlides = new IntakeSlides(hardwareMap, telemetry);
        intakeSlides.setDefaultCommand(new RunIntakeSlidesPID(intakeSlides));
        intakeSlides.resetEncoder();

        outtakeSlides = new OuttakeSlides(hardwareMap, telemetry);
        outtakeSlides.setDefaultCommand(new RunOuttakeSlidesPID(outtakeSlides));
        outtakeSlides.resetEncoder();

        intake = new Intake(hardwareMap);

        outtake = new Outtake(hardwareMap);

        schedule(new InstantCommand(() -> {
//            OuttakeSlides.setTargetPos(1000);
            Actions.runBlocking(
                    chassis.rr.actionBuilder(new Pose2d(-15, 62, Math.toRadians(270)))
                            .splineTo(new Vector2d(-15, 36), Math.toRadians(270))
                            .waitSeconds(1)
                            .splineToConstantHeading(new Vector2d(-50, 60), Math.toRadians(270))
                            .build()
            )
            ;
        }));
    }
}

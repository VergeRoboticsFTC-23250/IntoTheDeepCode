package org.firstinspires.ftc.teamcode.auto;

import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.ParallelAction;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.SequentialAction;
import com.acmerobotics.roadrunner.Trajectory;
import com.acmerobotics.roadrunner.Vector2d;
import com.acmerobotics.roadrunner.ftc.Actions;
import com.arcrobotics.ftclib.command.CommandOpMode;
import com.arcrobotics.ftclib.command.CommandScheduler;
import com.arcrobotics.ftclib.command.InstantCommand;
import com.arcrobotics.ftclib.command.ParallelCommandGroup;
import com.arcrobotics.ftclib.command.ScheduleCommand;
import com.arcrobotics.ftclib.command.SequentialCommandGroup;
import com.arcrobotics.ftclib.command.WaitCommand;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.util.ftclib.commands.DriveTo;
import org.firstinspires.ftc.teamcode.util.ftclib.commands.Movement;
import org.firstinspires.ftc.teamcode.util.ftclib.commands.RunIntakeSlidesPID;
import org.firstinspires.ftc.teamcode.util.ftclib.commands.RunOuttakeSlideToPos;
import org.firstinspires.ftc.teamcode.util.ftclib.commands.RunOuttakeSlidesPID;
import org.firstinspires.ftc.teamcode.util.ftclib.subsystems.Chassis;
import org.firstinspires.ftc.teamcode.util.ftclib.subsystems.Intake;
import org.firstinspires.ftc.teamcode.util.ftclib.subsystems.IntakeSlides;
import org.firstinspires.ftc.teamcode.util.ftclib.subsystems.Outtake;
import org.firstinspires.ftc.teamcode.util.ftclib.subsystems.OuttakeSlides;

@Autonomous
public class auto extends LinearOpMode {
    private Chassis chassis;
    private IntakeSlides intakeSlides;
    private OuttakeSlides outtakeSlides;
    private Intake intake;
    private Outtake outtake;

    @Override
    public void runOpMode() {
        chassis = new Chassis(hardwareMap);
        intakeSlides = new IntakeSlides(hardwareMap, telemetry);
        outtakeSlides = new OuttakeSlides(hardwareMap, telemetry);
        intake = new Intake(hardwareMap);
        outtake = new Outtake(hardwareMap);

        intakeSlides.resetEncoder();
        intakeSlides.resetPID();

        outtakeSlides.resetEncoder();
        outtakeSlides.resetPID();

        //outtakeSlides.setTargetPos(1100);

        outtake.setArm(Outtake.armHomePos);
        outtake.setPivot(Outtake.pivotHomePos);
        outtake.openClaw();

        waitForStart();

        Actions.runBlocking(
                new ParallelAction(
                        (e) -> {
                            outtakeSlides.updatePID();
                            return true;
                        },
                        (e) -> {
                            intakeSlides.updatePID();
                            return true;
                        },
                        chassis.rr.actionBuilder(chassis.rr.pose)
                                .strafeToLinearHeading(new Vector2d(0, -48), Math.toRadians(0))
                                .build()
                )
        );
    }
}
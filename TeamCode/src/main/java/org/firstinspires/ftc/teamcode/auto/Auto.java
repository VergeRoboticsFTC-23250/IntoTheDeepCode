package org.firstinspires.ftc.teamcode.auto;


import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.Vector2d;
import com.acmerobotics.roadrunner.ftc.Actions;
import com.arcrobotics.ftclib.command.Command;
import com.arcrobotics.ftclib.command.CommandOpMode;
import com.arcrobotics.ftclib.command.CommandScheduler;
import com.arcrobotics.ftclib.command.InstantCommand;
import com.arcrobotics.ftclib.command.SequentialCommandGroup;
import com.arcrobotics.ftclib.command.WaitCommand;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.util.Robot;
import org.firstinspires.ftc.teamcode.util.ftclib.commands.DriveHorizontalSlides;
import org.firstinspires.ftc.teamcode.util.ftclib.commands.HomeVerticalSlides;
import org.firstinspires.ftc.teamcode.util.ftclib.commands.Movement;
import org.firstinspires.ftc.teamcode.util.ftclib.commands.RunVerticalSlidePID;
import org.firstinspires.ftc.teamcode.util.ftclib.subsystems.Chassis;
import org.firstinspires.ftc.teamcode.util.ftclib.subsystems.HorizontalSlides;
import org.firstinspires.ftc.teamcode.util.ftclib.subsystems.Intake;
import org.firstinspires.ftc.teamcode.util.ftclib.subsystems.Outtake;
import org.firstinspires.ftc.teamcode.util.ftclib.subsystems.VerticalSlides;

@Config
@Autonomous(name="Auto")
public class Auto extends CommandOpMode {
    private Chassis chassis;
    private Outtake outtake;
    private Intake intake;
    private HorizontalSlides hSlides;
    private VerticalSlides vSlides;

    public static long outtakeDelay = 700;
    public static long intakeDelay = 1000;

    public static double outtakePosX = 62;
    public static double outtakePosY = 57;
    public static double outtakeHeading = 250;
    public static Action traj1;

    @Override
    public void initialize() {
        //Initialize subsystems.
        chassis = new Chassis(hardwareMap, new Pose2d(13,60,Math.toRadians(270)));
        intake = new Intake(hardwareMap);
        hSlides = new HorizontalSlides(hardwareMap);
        vSlides = new VerticalSlides(hardwareMap);
        outtake = new Outtake(hardwareMap);

        outtake.setArm(Robot.init.armPos);
        outtake.setPivot(Robot.init.pivotPos);
        outtake.setIsClawOpen(Robot.init.isClawOpen);

        intake.setInitPos();

        Command home = new HomeVerticalSlides(vSlides);

        home.initialize();
        while (!home.isFinished()){
            home.execute();
        }
        home.end(false);

        //Set default commands.
        vSlides.setDefaultCommand(new RunVerticalSlidePID(vSlides));

//        traj1 = chassis.md.actionBuilder(chassis.md.pose)
//                .strafeTo(new Vector2d(4, 33))
//                .lineToY(40)
//                .strafeToLinearHeading(new Vector2d(54,47),Math.toRadians(250))
//                .waitSeconds(intakeDelay)
//                .strafeToLinearHeading(new Vector2d(outtakePosX,outtakePosY), Math.toRadians(outtakeHeading))
//                .waitSeconds(outtakeDelay)
//                .strafeToLinearHeading(new Vector2d(58,47),Math.toRadians(270))
//                .waitSeconds(intakeDelay)
//                .strafeToLinearHeading(new Vector2d(outtakePosX,outtakePosY), Math.toRadians(outtakeHeading))
//                .waitSeconds(outtakeDelay)
//                .strafeToLinearHeading(new Vector2d(53,44),Math.toRadians(315))
//                .waitSeconds(intakeDelay)
//                .strafeToLinearHeading(new Vector2d(outtakePosX,outtakePosY), Math.toRadians(outtakeHeading))
//                .waitSeconds(outtakeDelay)
//                .build();

        schedule(new SequentialCommandGroup(
                Robot.GoToState(outtake, Robot.State.OUTTAKE_SUBMERSIBLE, telemetry),
                new WaitCommand(outtakeDelay),
                new InstantCommand(() -> {
                    Actions.runBlocking(chassis.md.actionBuilder(chassis.md.pose)
                            .strafeTo(new Vector2d(4, 33))
                            .build()
                        );}),
                new WaitCommand(outtakeDelay),
                Robot.GoToState(outtake, Robot.State.OUTTAKE_SUBMERSIBLE_SCORE, telemetry),
                new WaitCommand(outtakeDelay),
                new InstantCommand(outtake::openClaw),
                new InstantCommand(() -> {
                    Actions.runBlocking(chassis.md.actionBuilder(chassis.md.pose)
                            .lineToY(40)
                            .strafeToLinearHeading(new Vector2d(54,47),Math.toRadians(250))
                            .build()
                    );}),
                new InstantCommand(() -> {
                    intake.drop();
                    intake.spin(1);
                    hSlides.setPos(1);
                }),
                Robot.GoToState(outtake, Robot.State.HOME, telemetry),
                new WaitCommand(intakeDelay),
                new InstantCommand(() -> {
                    intake.home();
                    intake.spin(0);
                    hSlides.setPos(0);
                }),
                new WaitCommand(intakeDelay),
                Robot.GoToState(outtake, Robot.State.OUTTAKE_BUCKET, telemetry),
                new WaitCommand(outtakeDelay),
                new InstantCommand(() -> {
                    Actions.runBlocking(chassis.md.actionBuilder(chassis.md.pose)
                            .strafeToLinearHeading(new Vector2d(outtakePosX,outtakePosY), Math.toRadians(outtakeHeading))
                            .build()
                    );}),
                new InstantCommand(outtake::openClaw)
        ));
    }
}

package org.firstinspires.ftc.teamcode.auto.testing;

import com.pedropathing.pathgen.BezierLine;
import com.pedropathing.pathgen.Path;
import com.pedropathing.pathgen.Point;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.util.dairy.Paths;
import org.firstinspires.ftc.teamcode.util.dairy.Robot;
import org.firstinspires.ftc.teamcode.util.dairy.features.LoopTimes;
import org.firstinspires.ftc.teamcode.util.dairy.subsystems.Chassis;
import org.firstinspires.ftc.teamcode.util.dairy.subsystems.Intake;
import org.firstinspires.ftc.teamcode.util.dairy.subsystems.IntakeSlides;
import org.firstinspires.ftc.teamcode.util.dairy.subsystems.Outtake;
import org.firstinspires.ftc.teamcode.util.dairy.subsystems.OuttakeSlides;

import dev.frozenmilk.dairy.core.util.features.BulkRead;
import dev.frozenmilk.mercurial.Mercurial;
import dev.frozenmilk.mercurial.commands.groups.Parallel;
import dev.frozenmilk.mercurial.commands.groups.Sequential;
import dev.frozenmilk.mercurial.commands.util.Wait;

@Mercurial.Attach
@Chassis.Attach
@Outtake.Attach
@OuttakeSlides.Attach
@Intake.Attach
@IntakeSlides.Attach
//@LoopTimes.Attach
@BulkRead.Attach
@Autonomous
public class PathTesting extends OpMode {

    @Override
    public void init() {
        Robot.init();
        Outtake.setClaw(Outtake.clawClosePos);
        Outtake.isClawOpen = false;
        Outtake.setPosition(Outtake.armSpecPos);
        Outtake.setPivotManual(Outtake.pivotSpecPos);
        Robot.stateMachine.setState(Robot.State.INTAKE_SPEC);
    }

    @Override
    public void loop() {
        telemetry.addData("heading", Chassis.follower.getPose().getHeading());
        telemetry.addData("x", Chassis.follower.getPose().getX());
        telemetry.addData("y", Chassis.follower.getPose().getY());
        Chassis.follower.update();
    }

    @Override
    public void start() {
        new Sequential(
                IntakeSlides.setPower(-0.3),
                Intake.setIntake(Intake.hoverPos),
                new Wait(0.5),

                new Parallel(
                        Robot.setState(Robot.State.OUTTAKE_BUCKET),
                        Chassis.followPath(Paths.fourSamps.get(0),true)
                ),

                new Wait(0.3),

                new Parallel(
                        Chassis.followPath(Paths.fourSamps.get(1)),
                        Outtake.openClaw()
                ),

                new Parallel(
                        Chassis.followPath(Paths.fourSamps.get(2)),
                        new Sequential(
                                new Wait(0.35),
                                Robot.setState(Robot.State.HOME),
                                new Wait(0.2)
                        )
                ),

                IntakeSlides.extend(),
                new Wait(0.25),
                IntakeSlides.home(),
                new Wait(0.3),

                Robot.setState(Robot.State.OUTTAKE_BUCKET),
                new Wait(0.5),
                Chassis.followPath(Paths.fourSamps.get(3)),

                new Parallel(
                        Chassis.followPath(Paths.fourSamps.get(4)),
                        Outtake.openClaw()
                ),

                new Parallel(
                        Chassis.followPath(Paths.fourSamps.get(5)),
                        new Sequential(
                                new Wait(0.35),
                                Robot.setState(Robot.State.HOME),
                                new Wait(0.2)
                        )
                ),

                IntakeSlides.extend(),
                new Wait(0.25),
                IntakeSlides.home(),
                new Wait(0.3),

                Robot.setState(Robot.State.OUTTAKE_BUCKET),
                new Wait(0.6),
                Chassis.followPath(Paths.fourSamps.get(6)),

                new Parallel(
                        Chassis.followPath(Paths.fourSamps.get(7)),
                        Outtake.openClaw()
                ),

                new Parallel(
                        Chassis.followPath(Paths.fourSamps.get(8)),
                        new Sequential(
                                new Wait(0.35),
                                Robot.setState(Robot.State.HOME),
                                new Wait(0.2)
                        )
                ),

                IntakeSlides.extend(),
                new Wait(0.25),
                IntakeSlides.home(),
                new Wait(0.3),

                Robot.setState(Robot.State.OUTTAKE_BUCKET),
                new Wait(0.6),
                Chassis.followPath(Paths.fourSamps.get(9)),

                new Parallel(
                        Chassis.followPath(Paths.fourSamps.get(10)),
                        Outtake.openClaw()
                ),

                new Parallel(
                        Chassis.followPath(Paths.fourSamps.get(11)),
                        new Sequential(
                                new Wait(0.35),
                                Robot.setState(Robot.State.HOME),
                                new Wait(0.2)
                        )
                )

        )
                .schedule();
    }
}
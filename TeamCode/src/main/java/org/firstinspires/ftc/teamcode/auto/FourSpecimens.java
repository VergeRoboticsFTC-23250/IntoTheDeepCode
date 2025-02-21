package org.firstinspires.ftc.teamcode.auto;

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

@Mercurial.Attach
@Chassis.Attach
@Outtake.Attach
@OuttakeSlides.Attach
@Intake.Attach
@IntakeSlides.Attach
@LoopTimes.Attach
@BulkRead.Attach
@Autonomous
public class FourSpecimens extends OpMode {

    public static long intakeDelay = 250;
    public static double intakePower = -0.5;
    public static long outtakeDelay = 500;
    public static double outtakePower = 1;

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
    }

    @Override
    public void start() {
        new Sequential(
                IntakeSlides.setPower(-0.3),
                Intake.setIntake(Intake.hoverPos),
                OuttakeSlides.runToPosition(OuttakeSlides.submersiblePos),

                // Preload
                new Parallel(
                        Robot.setState(Robot.State.OUTTAKE_SUBMERSIBLE),
                        Chassis.followPath(Paths.fiveSpecs.get(0))
                ),
                new Parallel(
                        Chassis.pushOld(outtakePower, outtakeDelay),
                        Robot.setState(Robot.State.OUTTAKE_SUBMERSIBLE_SCORE)
                ),
                Outtake.openClaw(),

                // Pushing samples
                new Parallel(
                        Chassis.followPath(Paths.fiveSpecs.get(1)),
                        Robot.setState(Robot.State.INTAKE_SPEC)
                ),
                Chassis.followPath(Paths.fiveSpecs.get(2)),
                Chassis.followPath(Paths.fiveSpecs.get(3)),
                Chassis.followPath(Paths.fiveSpecs.get(4)),
                Chassis.followPath(Paths.fiveSpecs.get(5)),
                Chassis.followPath(Paths.fiveSpecs.get(6)),
                Chassis.followPath(Paths.fiveSpecs.get(7)),

                // plus 1 intake
                Chassis.pushOld(intakePower, intakeDelay),
                Outtake.closeClaw(),

                // plus 1 outtake
                new Parallel(
                        Robot.setState(Robot.State.OUTTAKE_SUBMERSIBLE),
                        Chassis.followPath(Paths.fiveSpecs.get(8))
                ),
                new Parallel(
                        Chassis.pushOld(outtakePower, outtakeDelay),
                        Robot.setState(Robot.State.OUTTAKE_SUBMERSIBLE_SCORE)
                ),
                Chassis.followPath(Paths.fiveSpecs.get(9)),
                Outtake.openClaw(),

                // plus 2 intake
                new Parallel(
                        Robot.setState(Robot.State.INTAKE_SPEC),
                        Chassis.followPath(Paths.fiveSpecs.get(10))
                ),
                Chassis.pushOld(intakePower, intakeDelay),
                Outtake.closeClaw(),

                // plus 2 outtake
                new Parallel(
                        Robot.setState(Robot.State.OUTTAKE_SUBMERSIBLE),
                        Chassis.followPath(Paths.fiveSpecs.get(11))
                ),
                new Parallel(
                        Chassis.pushOld(outtakePower, outtakeDelay),
                        Robot.setState(Robot.State.OUTTAKE_SUBMERSIBLE_SCORE)
                ),
                Chassis.followPath(Paths.fiveSpecs.get(12)),
                Outtake.openClaw(),

                // plus 3 intake
                new Parallel(
                        Robot.setState(Robot.State.INTAKE_SPEC),
                        Chassis.followPath(Paths.fiveSpecs.get(13))
                ),
                Chassis.pushOld(intakePower, intakeDelay),
                Outtake.closeClaw(),

                // plus 3 outtake
                new Parallel(
                        Robot.setState(Robot.State.OUTTAKE_SUBMERSIBLE),
                        Chassis.followPath(Paths.fiveSpecs.get(14))
                ),
                new Parallel(
                        Chassis.pushOld(outtakePower, outtakeDelay),
                        Robot.setState(Robot.State.OUTTAKE_SUBMERSIBLE_SCORE)
                ),
                Chassis.followPath(Paths.fiveSpecs.get(15)),
                Outtake.openClaw(),

                // plus 4 intake
                new Parallel(
                        Robot.setState(Robot.State.INTAKE_SPEC),
                        Chassis.followPath(Paths.fiveSpecs.get(16))
                )

//                Chassis.push(intakePower, intakeDelay),
//                Outtake.closeClaw(),
//
//                // plus 4 outtake
//                new Parallel(
//                        Robot.setState(Robot.State.OUTTAKE_SUBMERSIBLE),
//                        Chassis.followPath(Paths.plusFourSpec.get(17))
//                ),
//                new Parallel(
//                        Chassis.push(outtakePower, outtakeDelay),
//                        Robot.setState(Robot.State.OUTTAKE_SUBMERSIBLE_SCORE)
//                ),
//                Outtake.openClaw(),
//
//                // park
//                new Parallel(
//                        Robot.setState(Robot.State.INTAKE_SPEC),
//                        Chassis.followPath(Paths.plusFourSpec.get(18))
//                )
        )
                .schedule();
    }
}
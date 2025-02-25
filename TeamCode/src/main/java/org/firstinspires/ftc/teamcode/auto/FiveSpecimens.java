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
import dev.frozenmilk.mercurial.commands.util.Wait;

@Mercurial.Attach
@Chassis.Attach
@Outtake.Attach
@OuttakeSlides.Attach
@Intake.Attach
@IntakeSlides.Attach
@LoopTimes.Attach
@BulkRead.Attach
@Autonomous
public class FiveSpecimens extends OpMode {

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
                        Chassis.push(outtakePower, outtakeDelay),
                        Robot.setState(Robot.State.OUTTAKE_SUBMERSIBLE_SCORE)
                ),

                // Pushing samples
                new Parallel(
                        Chassis.followPath(Paths.fiveSpecs.get(1)),
                        new Sequential(
                                Outtake.openClaw(),
                                new Wait(0.2),
                                Robot.setState(Robot.State.INTAKE_SPEC)
                        )
                ),
                Chassis.followPathChain(Paths.robotPush),
                Chassis.followPath(Paths.fiveSpecs.get(7), true),

                // plus 1 intake
                Chassis.push(intakePower, intakeDelay),
                Outtake.closeClaw(),

                // plus 1 outtake
                new Parallel(
                        Robot.setState(Robot.State.OUTTAKE_SUBMERSIBLE),
                        Chassis.followPath(Paths.fiveSpecs.get(8))
                ),
                new Parallel(
                        Chassis.push(outtakePower, outtakeDelay),
                        Robot.setState(Robot.State.OUTTAKE_SUBMERSIBLE_SCORE)
                ),
//                Chassis.followPath(Paths.fiveSpecs.get(9)),
                Outtake.openClaw(),

                // plus 2 intake
                new Parallel(
                        Chassis.followPath(Paths.fiveSpecs.get(10)),
                        new Sequential(
                                Outtake.openClaw(),
                                new Wait(0.2),
                                Robot.setState(Robot.State.INTAKE_SPEC)
                        )
                ),
                Chassis.push(intakePower, intakeDelay),
                Outtake.closeClaw(),

                // plus 2 outtake
                new Parallel(
                        Robot.setState(Robot.State.OUTTAKE_SUBMERSIBLE),
                        Chassis.followPath(Paths.fiveSpecs.get(11))
                ),
                new Parallel(
                        Chassis.push(outtakePower, outtakeDelay),
                        Robot.setState(Robot.State.OUTTAKE_SUBMERSIBLE_SCORE)
                ),
//                Chassis.followPath(Paths.fiveSpecs.get(12)),
                Outtake.openClaw(),

                // plus 3 intake
                new Parallel(
                    Chassis.followPath(Paths.fiveSpecs.get(13)),
                    new Sequential(
                            Outtake.openClaw(),
                            new Wait(0.2),
                            Robot.setState(Robot.State.INTAKE_SPEC)
                    )
                ),
                Chassis.push(intakePower, intakeDelay),
                Outtake.closeClaw(),

                // plus 3 outtake
                new Parallel(
                        Robot.setState(Robot.State.OUTTAKE_SUBMERSIBLE),
                        Chassis.followPath(Paths.fiveSpecs.get(14))
                ),
                new Parallel(
                        Chassis.push(outtakePower, outtakeDelay),
                        Robot.setState(Robot.State.OUTTAKE_SUBMERSIBLE_SCORE)
                ),
//                Chassis.followPath(Paths.fiveSpecs.get(15)),
                Outtake.openClaw(),

                // plus 4 intake
                new Parallel(
                        Chassis.followPath(Paths.fiveSpecs.get(16)),
                        new Sequential(
                                Outtake.openClaw(),
                                new Wait(0.2),
                                Robot.setState(Robot.State.INTAKE_SPEC)
                        )
                ),

                Chassis.push(intakePower, intakeDelay),
                Outtake.closeClaw(),

                // plus 4 outtake
                new Parallel(
                        Robot.setState(Robot.State.OUTTAKE_SUBMERSIBLE),
                        Chassis.followPath(Paths.fiveSpecs.get(17))
                ),
                new Parallel(
                        Chassis.push(outtakePower, outtakeDelay),
                        Robot.setState(Robot.State.OUTTAKE_SUBMERSIBLE_SCORE)
                ),
                Outtake.openClaw(),

                // park
                new Parallel(
                        Chassis.followPath(Paths.fiveSpecs.get(18)),
                        new Sequential(
                                Outtake.openClaw(),
                                new Wait(0.2),
                                Robot.setState(Robot.State.INTAKE_SPEC)
                        )
                )
            )
        .schedule();
    }
}
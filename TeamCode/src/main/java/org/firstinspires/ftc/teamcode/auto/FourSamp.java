package org.firstinspires.ftc.teamcode.auto;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.util.dairy.Paths;
import org.firstinspires.ftc.teamcode.util.dairy.Robot;
import org.firstinspires.ftc.teamcode.util.dairy.subsystems.Chassis;
import org.firstinspires.ftc.teamcode.util.dairy.subsystems.intake.Differential;
import org.firstinspires.ftc.teamcode.util.dairy.subsystems.intake.IntakeClaw;
import org.firstinspires.ftc.teamcode.util.dairy.subsystems.intake.IntakeDropDown;
import org.firstinspires.ftc.teamcode.util.dairy.subsystems.intake.IntakeSlides;
import org.firstinspires.ftc.teamcode.util.dairy.subsystems.outtake.OuttakeArm;
import org.firstinspires.ftc.teamcode.util.dairy.subsystems.outtake.OuttakeClaw;
import org.firstinspires.ftc.teamcode.util.dairy.subsystems.outtake.OuttakePivot;
import org.firstinspires.ftc.teamcode.util.dairy.subsystems.outtake.OuttakeSlides;

import dev.frozenmilk.dairy.core.util.features.BulkRead;
import dev.frozenmilk.mercurial.Mercurial;
import dev.frozenmilk.mercurial.commands.groups.Parallel;
import dev.frozenmilk.mercurial.commands.groups.Sequential;
import dev.frozenmilk.mercurial.commands.util.Wait;

@Mercurial.Attach
@Chassis.Attach
@OuttakeSlides.Attach
@OuttakeClaw.Attach
@OuttakePivot.Attach
@OuttakeArm.Attach
@IntakeDropDown.Attach
@IntakeClaw.Attach
@Differential.Attach
@IntakeSlides.Attach
//@LoopTimes.Attach
@BulkRead.Attach
@Autonomous
//Differential.IntakeWrist.increment(Differential.IntakeWrist.Direction.COUNTER_CLOCKWISE)
public class FourSamp extends OpMode {

    @Override
    public void init() {
        Robot.init(hardwareMap);
        Robot.setState(Robot.State.INIT).schedule();
        Chassis.follower.setStartingPose(Paths.bucketStart);
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
                //preload
                new Parallel(
                        Robot.setState(Robot.State.BUCKET),
                        Chassis.followPath(Paths.fourSamps.get(0),true)
                ),
                new Wait(0.3),
                Chassis.followPath(Paths.fourSamps.get(1)),
                new Wait(0.3),
                OuttakeClaw.open(),


                //pickup 1
                new Parallel(
                        Chassis.followPath(Paths.fourSamps.get(2)),
                        new Sequential(
                                new Wait(0.35),
                                Robot.setState(Robot.State.INTAKE_GROUND)
                        )
                ),
                new Sequential(
                        Robot.setState(Robot.State.INTAKE_GROUND_SECONDARY),
                        new Wait(0.125),
                        IntakeClaw.closeFirm(),
                        new Wait(0.25),
                        Robot.setState(Robot.State.HOME)
                ),
                Robot.setState(Robot.State.TELEOP_TRANSFER),
                new Wait(0.3),
                new Parallel(
                        OuttakeClaw.closeFirm(),
                        IntakeClaw.open()
                ),

                //outtake 1 and home
                new Parallel(
                        Robot.setState(Robot.State.BUCKET),
                        new Sequential(
                                new Wait(0.5),
                                Chassis.followPath(Paths.fourSamps.get(3))
                        )
                ),
                Chassis.followPath(Paths.fourSamps.get(4)),
                new Wait(0.3),
                OuttakeClaw.open(),
                new Parallel(
                        Chassis.followPath(Paths.fourSamps.get(5)),
                        new Sequential(
                                new Wait(0.35),
                                Robot.setState(Robot.State.INTAKE_GROUND)
                        )
                ),


                //pickup 2
                new Sequential(
                        Robot.setState(Robot.State.INTAKE_GROUND_SECONDARY),
                        new Wait(0.125),
                        IntakeClaw.closeFirm(),
                        new Wait(0.25),
                        Robot.setState(Robot.State.HOME)
                ),
                Robot.setState(Robot.State.TELEOP_TRANSFER),
                new Wait(0.3),
                new Parallel(
                        OuttakeClaw.closeFirm(),
                        IntakeClaw.open()
                ),


                //outtake 2 and home
                new Parallel(
                        Robot.setState(Robot.State.BUCKET),
                        new Sequential(
                                new Wait(0.35),
                                Chassis.followPath(Paths.fourSamps.get(6))
                        )
                ),
                Chassis.followPath(Paths.fourSamps.get(7)),
                new Wait(0.3),
                OuttakeClaw.open(),
                new Parallel(
                        Chassis.followPath(Paths.fourSamps.get(8)),
                        new Sequential(
                                new Wait(0.35),
                                Robot.setState(Robot.State.INTAKE_GROUND),
                                Differential.IntakeWrist.increment(Differential.IntakeWrist.Direction.COUNTER_CLOCKWISE)
                        )
                ),

                //pickup 3
                new Sequential(
                        Robot.setState(Robot.State.INTAKE_GROUND_SECONDARY),
                        new Wait(0.125),
                        IntakeClaw.closeFirm(),
                        new Wait(0.25),
                        Robot.setState(Robot.State.HOME)
                ),
                Robot.setState(Robot.State.TELEOP_TRANSFER),
                new Wait(0.3),
                new Parallel(
                        OuttakeClaw.closeFirm(),
                        IntakeClaw.open()
                ),

                //outtake 3 and park
                new Parallel(
                        Robot.setState(Robot.State.BUCKET),
                        new Sequential(
                                new Wait(0.3),
                                Chassis.followPath(Paths.fourSamps.get(9))
                        )
                ),
                Chassis.followPath(Paths.fourSamps.get(10)),
                new Wait(0.3),
                OuttakeClaw.open(),
                new Parallel(
                        Chassis.followPath(Paths.fourSamps.get(11)),
                        new Sequential(
                                new Wait(0.35),
                                Robot.setState(Robot.State.HOME)
                        )
                )
        )
                .schedule();
    }
}
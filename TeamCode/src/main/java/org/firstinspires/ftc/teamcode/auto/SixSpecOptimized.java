package org.firstinspires.ftc.teamcode.auto;

import com.pedropathing.localization.Pose;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.util.dairy.Robot;
import org.firstinspires.ftc.teamcode.util.dairy.features.LoopTimes;
import org.firstinspires.ftc.teamcode.util.dairy.subsystems.Chassis;
import org.firstinspires.ftc.teamcode.util.dairy.subsystems.Intake;
import org.firstinspires.ftc.teamcode.util.dairy.subsystems.IntakeSlides;
import org.firstinspires.ftc.teamcode.util.dairy.subsystems.Outtake;
import org.firstinspires.ftc.teamcode.util.dairy.subsystems.OuttakeSlides;

import dev.frozenmilk.dairy.core.util.features.BulkRead;
import dev.frozenmilk.mercurial.Mercurial;
import dev.frozenmilk.mercurial.commands.Command;
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
public class SixSpecOptimized extends OpMode {
    public static Pose intakePose = new Pose(10, 35, 0);
    public static double[] intakeOffsetsY = {0, 0, 0, 0, 0};
    public static double[] intakeOffsetsX = {0, 2, 4, 6, 8};
    public static Pose outtakePose = new Pose(50, 72, 0);
    public static double[] outtakeOffsetsY = {10, 8, 6, 4, 2, 0};
    public static double[] outtakeOffsetsX = {0, 0, 0, 0, 0, 0};
    Command Outtake(int outtakeNum){
        return new Sequential(
                new Parallel(
                        Chassis.setExact(),
                        OuttakeSlides.runToPosition(OuttakeSlides.submersiblePos),
                        new Wait(0.25).then(new Parallel(
                                Outtake.setArm(Outtake.armSubmersiblePos),
                                Outtake.setPivot(Outtake.pivotSubmersiblePos)
                        )),
                        Chassis.driveToPoint(
                                new Pose(
                                        outtakePose.getX() + outtakeOffsetsX[outtakeNum-1],
                                        outtakePose.getY() + outtakeOffsetsY[outtakeNum-1],
                                        outtakePose.getHeading())
                        ),
                        Chassis.setClean()
                ),
                Chassis.setConstantDrivePower(1),
                OuttakeSlides.runToPosition(OuttakeSlides.scoreSubmersiblePos),
                Outtake.openClaw(),
                Chassis.releaseConstantDrivePower()
        );
    }

    Command Intake(int intakeNum){
        return new Parallel(
                new Sequential(
                        new Wait(.5),
                        Outtake.setArm(Outtake.armSpecPos),
                        Outtake.setPivot(Outtake.pivotSpecPos),
                        OuttakeSlides.runToPosition(OuttakeSlides.minPos)
                ),
                new Sequential(
                        Chassis.setClean(),
                        Chassis.driveToPoint(
                                new Pose(
                                        intakePose.getX() + intakeOffsetsX[intakeNum-1],
                                        intakePose.getY() + intakeOffsetsY[intakeNum-1],
                                        intakePose.getHeading()
                                )
                        ),
                        Chassis.setConstantDrivePower(-1),
                        new Wait(.25),
                        Outtake.closeClaw(),
                        Chassis.releaseConstantDrivePower(),
                        Chassis.setClean()
                )
        );
    }

    Command PushSamples = new Parallel(
            Robot.setState(Robot.State.INTAKE_SPEC),
            new Sequential(
                    new Wait(1),
                    IntakeSlides.extend()
            ),
            new Sequential(
                    Chassis.setSloppy(),
                    Chassis.driveToPoint(new Pose(32, 52, Math.toRadians(-130))),
                    Chassis.driveToPoint(new Pose(32, 43, Math.toRadians(-30))),
                    Chassis.driveToPoint(new Pose(28, 44, Math.toRadians(-130))),
                    Chassis.driveToPoint(new Pose(32, 33, Math.toRadians(-30))),
                    Chassis.driveToPoint(new Pose(28, 34, Math.toRadians(-130))),
                    Chassis.driveToPoint(new Pose(32, 27, Math.toRadians(-30))),
                    Chassis.driveToPoint(new Pose(32, 28, Math.toRadians(-130))),
                    Chassis.setClean()
            )
    );

    @Override
    public void init() {
        Robot.init();
        Chassis.holdPoint = true;
        Outtake.setClaw(Outtake.clawClosePos);
        Outtake.isClawOpen = false;
        Outtake.setPosition(Outtake.armInitPos);
        Outtake.setPivotManual(Outtake.pivotOuttakeSpec);
        IntakeSlides.setPowerManual(-0.3);
        Intake.setIntakeManual(Intake.hoverPos);

        Robot.stateMachine.setState(Robot.State.INTAKE_SPEC);
    }

    @Override
    public void start() {
        new Sequential(
                Outtake(1),
                new Parallel(
                        IntakeSlides.retract(),
                        Intake(1)
                ),
                Outtake(2),
                Intake(2),
                Outtake(3),
                Intake(3),
                Outtake(4),
                Intake(4),
                Outtake(5),
                Intake(5),
                Outtake(6)
//                new Parallel(
//                        new Wait(.5).then(new Parallel(
//                                Outtake.setArm(Outtake.armSpecPos),
//                                Outtake.setPivot(Outtake.pivotSpecPos),
//                                OuttakeSlides.runToPosition(OuttakeSlides.minPos),
//                                IntakeSlides.extend(),
//                                Chassis.driveToPoint(new Pose(32, 28, Math.toRadians(-130)))
//                        ))
//                )
        ).schedule();
    }

    @Override
    public void loop() {

    }
}

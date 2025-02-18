package org.firstinspires.ftc.teamcode.auto;

import com.acmerobotics.dashboard.config.Config;
import com.pedropathing.localization.Pose;
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
@Config
@Autonomous
public class SixSpec extends OpMode {
    public static double intakeOffset = -4.5;
    public static double intakeOffset1 = 2;
    public static double intakeOffset2 = 1;
    public static double intakeOffset3 = 1;
    public static double intakeOffset4 = 2;
    public static double intakeOffset5 = 2;

    public static double outtakeOffset1 = 3;
    public static double outtakeOffset2 = 3;
    public static double outtakeOffset3 = 3;
    public static double outtakeOffset4 = 3;
    public static double outtakeOffset5 = 3;
    public static double outtakeOffset6 = 3;

    @Override
    public void init() {
        Robot.init();
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
                new Parallel(
                        Chassis.setClean(),
                        Outtake.setArm(Outtake.armSubmersiblePos),
                        Outtake.setPivot(Outtake.pivotSubmersiblePos),
                        OuttakeSlides.runToPosition(OuttakeSlides.submersiblePos),
                        // Preload
                        Chassis.driveToPointUntilStuck(new Pose(42, 67 + outtakeOffset1, 0))
                ),
                OuttakeSlides.runToPosition(OuttakeSlides.scoreSubmersiblePos),
                Outtake.openClaw(),
                Chassis.setSloppy(),
                new Parallel(
                        Robot.setState(Robot.State.INTAKE_SPEC),
                        new Sequential(
                                new Wait(.5),
                                IntakeSlides.extend()
                        ),
                        new Sequential(
                                Chassis.driveToPointUntilStuck(new Pose(32, 52, Math.toRadians(-130))),
                                Chassis.driveToPointUntilStuck(new Pose(32, 43, Math.toRadians(-30))),
                                Chassis.driveToPointUntilStuck(new Pose(28, 44, Math.toRadians(-130))),
                                Chassis.driveToPoint(new Pose(32, 33, Math.toRadians(-30))),
                                Chassis.driveToPointUntilStuck(new Pose(28, 34, Math.toRadians(-130)))
                        )
                ),
                Chassis.driveToPointUntilStuck(new Pose(32, 27, Math.toRadians(-30))),
                Chassis.driveToPointUntilStuck(new Pose(32, 28, Math.toRadians(-130))),
                Chassis.setClean(),
                //Intake 1
                new Parallel(
                        IntakeSlides.home(),
                        new Sequential(
                                Chassis.driveToPointUntilStuck(new Pose(10.500 + intakeOffset + intakeOffset1, 33.000+3.5, 0)),
                                //new Wait(0.25),
                                Outtake.closeClaw()
                        )
                ),
                //Outtake 2
                new Parallel(
                        Outtake.setArm(Outtake.armSubmersiblePos),
                        Outtake.setPivot(Outtake.pivotSubmersiblePos),
                        OuttakeSlides.runToPosition(OuttakeSlides.submersiblePos),
                        Chassis.driveToPointUntilStuck(new Pose(42, 67.000 + outtakeOffset2, 0))
                ),
                OuttakeSlides.runToPosition(OuttakeSlides.scoreSubmersiblePos),
                //intake2
                new Parallel(
                        Robot.setState(Robot.State.INTAKE_SPEC),
                        new Sequential(
                                Chassis.driveToPointUntilStuck(new Pose(10.500 + intakeOffset + intakeOffset2, 33.000+3.5, 0)),
                                //new Wait(0.25),
                                Outtake.closeClaw()
                        )
                ),
                //Outtake3
                new Parallel(
                        Outtake.setArm(Outtake.armSubmersiblePos),
                        Outtake.setPivot(Outtake.pivotSubmersiblePos),
                        OuttakeSlides.runToPosition(OuttakeSlides.submersiblePos),
                        Chassis.driveToPointUntilStuck(new Pose(42, 67.000 + outtakeOffset3, 0))
                ),
                OuttakeSlides.runToPosition(OuttakeSlides.scoreSubmersiblePos),
                //Intake3
                new Parallel(
                        Robot.setState(Robot.State.INTAKE_SPEC),
                        new Sequential(
                                Chassis.driveToPointUntilStuck(new Pose(10.500 + intakeOffset + intakeOffset3, 33.000+3.5, 0)),
                                //new Wait(0.25),
                                Outtake.closeClaw()
                        )
                ),
                //Outtake 4
                new Parallel(
                        Outtake.setArm(Outtake.armSubmersiblePos),
                        Outtake.setPivot(Outtake.pivotSubmersiblePos),
                        OuttakeSlides.runToPosition(OuttakeSlides.submersiblePos),
                        Chassis.driveToPointUntilStuck(new Pose(42, 67.000 + outtakeOffset4, 0))
                ),
                OuttakeSlides.runToPosition(OuttakeSlides.scoreSubmersiblePos),
                //Intake 4
                new Parallel(
                        Robot.setState(Robot.State.INTAKE_SPEC),
                        new Sequential(
                                Chassis.driveToPointUntilStuck(new Pose(10.500 + intakeOffset + intakeOffset4, 33.000+3.5, 0)),
                                //new Wait(0.25),
                                Outtake.closeClaw()
                        )
                ),
                //Outtake 5
                new Parallel(
                        Outtake.setArm(Outtake.armSubmersiblePos),
                        Outtake.setPivot(Outtake.pivotSubmersiblePos),
                        OuttakeSlides.runToPosition(OuttakeSlides.submersiblePos),
                        Chassis.driveToPointUntilStuck(new Pose(42, 67.000 + outtakeOffset5, 0))
                ),
                OuttakeSlides.runToPosition(OuttakeSlides.scoreSubmersiblePos),
                //Intake 5
                new Parallel(
                        Robot.setState(Robot.State.INTAKE_SPEC),
                        new Sequential(
                                Chassis.driveToPointUntilStuck(new Pose(10.500 + intakeOffset + intakeOffset4, 33.000+3.5, 0)),
                                //new Wait(0.25),
                                Outtake.closeClaw()
                        )
                ),
                //Outtake 6
                new Parallel(
                        Outtake.setArm(Outtake.armSubmersiblePos),
                        Outtake.setPivot(Outtake.pivotSubmersiblePos),
                        OuttakeSlides.runToPosition(OuttakeSlides.submersiblePos),
                        Chassis.driveToPointUntilStuck(new Pose(42, 67.000 + outtakeOffset6, 0))
                ),
                OuttakeSlides.runToPosition(OuttakeSlides.scoreSubmersiblePos)


        ).schedule();
    }

    @Override
    public void loop() {

    }
}

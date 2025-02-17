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
import dev.frozenmilk.mercurial.bindings.BoundGamepad;
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
public class SquidToPointTest extends OpMode {
    private BoundGamepad tejas;
    private BoundGamepad arvind;

    @Override
    public void init() {
        Robot.init();
        Outtake.setClaw(Outtake.clawClosePos);
        Outtake.isClawOpen = false;
        Outtake.setPosition(Outtake.armSpecPos);
        Outtake.setPivotManual(Outtake.pivotSpecPos);
        Robot.stateMachine.setState(Robot.State.INTAKE_SPEC);
        tejas = Mercurial.gamepad1();
        arvind = Mercurial.gamepad2();
    }

    @Override
    public void start() {
        new Sequential(
                IntakeSlides.setPower(-0.3),
                Intake.setIntake(Intake.hoverPos)
        ).schedule();
    }

    @Override
    public void loop() {
        tejas.triangle().onTrue(Chassis.driveToPoint(new Pose(0, 0, Math.PI)));
        tejas.cross().onTrue(Chassis.driveToPoint(new Pose(0, 0, 0)));

        tejas.dpadDown().onTrue(Chassis.driveToPoint(new Pose(0, 0, 0)));
        tejas.dpadLeft().onTrue(Chassis.driveToPoint(new Pose(0, 12, 0)));
        tejas.dpadUp().onTrue(Chassis.driveToPoint(new Pose(12, 0, 0)));
        tejas.dpadRight().onTrue(Chassis.driveToPoint(new Pose(0, -12, 0)));

        tejas.rightBumper().onTrue(Chassis.driveToPoint(new Pose(10, 10, 0)));
    }
}

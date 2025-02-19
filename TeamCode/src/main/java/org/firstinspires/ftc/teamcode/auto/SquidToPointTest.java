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
public class SquidToPointTest extends OpMode {
    private BoundGamepad tejas;
    private BoundGamepad arvind;

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
        tejas = Mercurial.gamepad1();
        arvind = Mercurial.gamepad2();
    }

    @Override
    public void start() {
    }

    @Override
    public void loop() {
        tejas.triangle().onTrue(Chassis.driveToPoint(new Pose(32, 8, 0)));
        tejas.cross().onTrue(Chassis.driveToPoint(new Pose(0, 0, 0)));
    }
}

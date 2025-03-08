package org.firstinspires.ftc.teamcode.auto;

import com.pedropathing.localization.Pose;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.auto.assets.SpecAuto;
import org.firstinspires.ftc.teamcode.auto.assets.SpecAutoParams;
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
import dev.frozenmilk.mercurial.bindings.BoundGamepad;
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
@BulkRead.Attach
@Autonomous
public class Test extends OpMode {
    Pose start = new Pose(0, 0, 0);
    private BoundGamepad tejas;

    @Override
    public void init() {
        Robot.init(hardwareMap);
        Chassis.follower.setStartingPose(start);
        Chassis.setDrivePointManual(start);
        tejas = Mercurial.gamepad1();
    }

    @Override
    public void loop() {
        tejas.cross().onTrue(Robot.setState(Robot.State.HOME));
        tejas.square().onTrue(Robot.setState(Robot.State.CAMERA));
        tejas.triangle().onTrue(new Sequential(
                Chassis.setAggressiveGains(),
                Chassis.homeToSamp(),
                new Wait(0.5),
                Chassis.homeToSamp(),
                IntakeClaw.open(),
                new Wait(0.5),
                Differential.IntakeWrist.autoAlign(),
                Robot.setState(Robot.State.INTAKE_GROUND)

//                new Wait(0.125),
//                IntakeClaw.closeFirm(),
//                new Wait(0.125),
//                Robot.setState(Robot.State.HOME)
        ));

        tejas.circle().onTrue(Robot.manipulateIntake());
    }

    @Override
    public void start() {
        new Sequential(
                Robot.setState(Robot.State.HOME),
                new Wait(0.75),
                Robot.setState(Robot.State.CAMERA)
        ).schedule();
    }
}

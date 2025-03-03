package org.firstinspires.ftc.teamcode.auto;

import com.pedropathing.localization.Pose;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.util.dairy.Robot;
import org.firstinspires.ftc.teamcode.util.dairy.features.LoopTimes;
import org.firstinspires.ftc.teamcode.util.dairy.subsystems.Chassis;
import org.firstinspires.ftc.teamcode.util.dairy.subsystems.intake.Differential;
import org.firstinspires.ftc.teamcode.util.dairy.subsystems.intake.IntakeClaw;
import org.firstinspires.ftc.teamcode.util.dairy.subsystems.intake.IntakeDropDown;
import org.firstinspires.ftc.teamcode.util.dairy.subsystems.intake.IntakeSlides;
import org.firstinspires.ftc.teamcode.util.dairy.subsystems.outtake.OuttakeArm;
import org.firstinspires.ftc.teamcode.util.dairy.subsystems.outtake.OuttakeClaw;
import org.firstinspires.ftc.teamcode.util.dairy.subsystems.outtake.OuttakePivot;
import org.firstinspires.ftc.teamcode.util.dairy.subsystems.outtake.OuttakeSlides;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import dev.frozenmilk.dairy.core.FeatureRegistrar;
import dev.frozenmilk.dairy.core.util.features.BulkRead;
import dev.frozenmilk.mercurial.Mercurial;
import dev.frozenmilk.mercurial.bindings.BoundGamepad;
import dev.frozenmilk.mercurial.commands.Command;
import dev.frozenmilk.mercurial.commands.groups.Parallel;
import dev.frozenmilk.mercurial.commands.groups.Sequential;
import dev.frozenmilk.mercurial.commands.util.Wait;

@Mercurial.Attach
@BulkRead.Attach
@LoopTimes.Attach
@FeatureRegistrar.LogDependencyResolutionExceptions
@IntakeClaw.Attach
@IntakeDropDown.Attach
@IntakeSlides.Attach
@OuttakeArm.Attach
@OuttakeClaw.Attach
@OuttakePivot.Attach
@OuttakeSlides.Attach
@Chassis.Attach
@Differential.Attach
@Autonomous
public class CVTest extends OpMode {
    private BoundGamepad tejas;
    @Override
    public void init() {
        Robot.init(hardwareMap);
        OuttakeArm.setPosManual(OuttakeArm.init);
        tejas = Mercurial.gamepad1();

        tejas.triangle().onTrue(Chassis.homeToSamp(Chassis.startingPose));
        tejas.square().onTrue(Robot.setState(Robot.State.OUTTAKE_FRONT));
        tejas.cross().onTrue(Robot.setState(Robot.State.INTAKE_GROUND));
        tejas.rightBumper().onTrue(Robot.manipulate());

        Chassis.setDrivePointManual(Chassis.startingPose);
    }

    @Override
    public void start() {
        new Sequential(
                Robot.setState(Robot.State.HOME),
                new Wait(0.5),
                Robot.setState(Robot.State.CAMERA)
        ).schedule();
    }

    @Override
    public void loop() {

    }
}
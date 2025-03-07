package org.firstinspires.ftc.teamcode.teleop;

import com.pedropathing.localization.Pose;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

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

import dev.frozenmilk.dairy.core.FeatureRegistrar;
import dev.frozenmilk.dairy.core.util.features.BulkRead;
import dev.frozenmilk.mercurial.Mercurial;
import dev.frozenmilk.mercurial.bindings.BoundGamepad;
import dev.frozenmilk.mercurial.commands.groups.Sequential;
import dev.frozenmilk.mercurial.commands.util.IfElse;
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
@TeleOp(name = "TeleOp")
public class Teleop extends OpMode {
    private BoundGamepad tejas;
    private BoundGamepad arvind;

    @Override
    public void init() {
        Robot.init(hardwareMap);

        tejas = Mercurial.gamepad1();
        arvind = Mercurial.gamepad2();

        tejas.cross().onTrue(Robot.setState(Robot.State.HOME));
        tejas.circle().onTrue(Robot.setState(Robot.State.INTAKE_BACK));
        tejas.square().onTrue(Robot.setState(Robot.State.OUTTAKE_FRONT));

        tejas.rightBumper().onTrue(Chassis.slow()).onFalse(Chassis.fast());
        tejas.leftBumper().onTrue(Robot.manipulate());

        arvind.cross().onTrue(
                new IfElse(
                        () -> Robot.getCurrentIntakeState() == Robot.State.HOME || Robot.getCurrentIntakeState() == Robot.State.INIT,
                        Robot.setState(Robot.State.CAMERA),
                        Robot.setState(Robot.State.HOME)
                )
        );
        tejas.triangle().onTrue(Robot.setState(Robot.State.BUCKET));
        tejas.dpadDown().onTrue(Robot.setState(Robot.State.PUSH_SAMPLE));

        arvind.square().onTrue(new IfElse(
                () -> Robot.getCurrentIntakeState() == Robot.State.HOME,
                Robot.setState(Robot.State.CAMERA),
                new IfElse(
                        () -> Robot.getCurrentIntakeState() == Robot.State.CAMERA || Robot.getCurrentIntakeState() == Robot.State.INTAKE_GROUND,
                        Robot.manipulate(),
                        new Wait(0)
                )
        ));

        arvind.leftTrigger().conditionalBindState().greaterThan(0.0).bind().onTrue(Robot.setState(Robot.State.INTAKE_GROUND));
        arvind.dpadDown().onTrue(Robot.outtakeGroundAndHome());
        arvind.rightBumper().onTrue(Differential.IntakeWrist.increment(Differential.IntakeWrist.Direction.CLOCKWISE));
        arvind.leftBumper().onTrue(Differential.IntakeWrist.increment(Differential.IntakeWrist.Direction.COUNTER_CLOCKWISE));
    }

    @Override
    public void loop() {
        telemetry.addData("currentIntakeState", Robot.getCurrentIntakeState());
        telemetry.addData("currentOuttakeState", Robot.getCurrentOuttakeState());
        telemetry.addData("pivot", Differential.pivot);
        telemetry.addData("wrist", Differential.wrist);
        Chassis.follower.update();
        telemetry.update();
    }
}
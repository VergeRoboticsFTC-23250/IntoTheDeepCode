package org.firstinspires.ftc.teamcode.teleop;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.util.dairy.Robot;
import org.firstinspires.ftc.teamcode.util.dairy.features.LoopTimes;
import org.firstinspires.ftc.teamcode.util.dairy.subsystems.Chassis;
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
import dev.frozenmilk.mercurial.bindings.BoundBooleanSupplier;
import dev.frozenmilk.mercurial.bindings.BoundGamepad;

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
@TeleOp(name = "TeleOp")
public class Teleop extends OpMode {
    private BoundGamepad tejas;
    private BoundGamepad arvind;

    @Override
    public void init() {
        Robot.init();
        Chassis.holdPoint = false;
        Robot.setState(Robot.State.HOME).schedule();

        tejas = Mercurial.gamepad1();
        arvind = Mercurial.gamepad2();

        tejas.square().onTrue(Robot.setState(Robot.State.OUTTAKE_FRONT));
        tejas.rightBumper().onTrue(OuttakeClaw.closeLoose());
        tejas.circle().onTrue(Robot.setState(Robot.State.INTAKE_BACK));
        tejas.cross().onTrue(Robot.setState(Robot.State.HOME));
        tejas.triangle().onTrue(Robot.setState(Robot.State.OUTTAKE_BACK));
    }

    @Override
    public void loop() {

    }
}
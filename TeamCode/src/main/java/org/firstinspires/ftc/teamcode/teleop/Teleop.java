package org.firstinspires.ftc.teamcode.teleop;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.util.dairy.Robot;
import org.firstinspires.ftc.teamcode.util.dairy.features.LoopTimes;
import org.firstinspires.ftc.teamcode.util.dairy.subsystems.Chassis;
import org.firstinspires.ftc.teamcode.util.dairy.subsystems.Claw;
import org.firstinspires.ftc.teamcode.util.dairy.subsystems.Intake;
import org.firstinspires.ftc.teamcode.util.dairy.subsystems.IntakeSlides;
import org.firstinspires.ftc.teamcode.util.dairy.subsystems.Outtake;
import org.firstinspires.ftc.teamcode.util.dairy.subsystems.OuttakeSlides;

import dev.frozenmilk.dairy.core.FeatureRegistrar;
import dev.frozenmilk.dairy.core.util.features.BulkRead;
import dev.frozenmilk.mercurial.Mercurial;
import dev.frozenmilk.mercurial.bindings.BoundBooleanSupplier;
import dev.frozenmilk.mercurial.bindings.BoundGamepad;
import dev.frozenmilk.mercurial.commands.groups.Sequential;
import dev.frozenmilk.mercurial.commands.util.Wait;

@Mercurial.Attach
@Chassis.Attach
@IntakeSlides.Attach
@OuttakeSlides.Attach
@Intake.Attach
@Outtake.Attach
@Claw.Attach
@BulkRead.Attach
@LoopTimes.Attach
@FeatureRegistrar.LogDependencyResolutionExceptions
@TeleOp(name = "TeleOp")
public class Teleop extends OpMode {
    private BoundGamepad tejas;
    private BoundGamepad arvind;

    private BoundBooleanSupplier arvTake;

    @Override
    public void init() {
        tejas = Mercurial.gamepad1();
        arvind = Mercurial.gamepad2();

        tejas.square()
                .onTrue(
                        Robot.setState(Robot.State.OUTTAKE_SUBMERSIBLE)
                );
        tejas.circle()
                .onTrue(Robot.setState(Robot.State.INTAKE_SPEC));
//        tejas.triangle()
//                .onTrue(Robot.setState(Robot.State.OUTTAKE_BUCKET));
//        tejas.cross()
//                .onTrue(
//                        Robot.setState(Robot.State.HOME)
//                );
//        tejas.dpadLeft()
//                .onTrue(
//                        Robot.setState(Robot.State.TRANSFER)
//                );
//
        tejas.dpadUp()
                .onTrue(
                        OuttakeSlides.setPowerCommand(0.5)
                ).onFalse(
                        OuttakeSlides.setPowerCommand(0.0)
                );
        tejas.dpadDown()
                .onTrue(
                        OuttakeSlides.setPowerCommand(-0.5)
                ).onFalse(
                        OuttakeSlides.setPowerCommand(0.0)
                );
//
        tejas.rightBumper()
                .onTrue(
                        Chassis.slow()
                ).onFalse(
                    Chassis.fast()
                );
        tejas.leftBumper()
                .onTrue(
                        Robot.manipulate()
                );
//
//        tejas.share().and(tejas.options()).onTrue(
//                OuttakeSlides.home()
//        );


        arvind.cross().onTrue(
                new Sequential(
                        Claw.dropGrab,
                        IntakeSlides.home()
                )
        );
        arvind.triangle().onTrue(
                Claw.transfer
        );
        arvind.dpadDown().onTrue(
                new Sequential(
                        IntakeSlides.extend().with(Claw.preIntake),
                        new Wait(0.4),
                        Claw.openGripper(),
                        new Wait(0.2),
                        IntakeSlides.home()
                )
        );
        arvind.dpadRight().onTrue(
                new Sequential(
                        Claw.preTransfer,
                        IntakeSlides.home()
                )
        );
        arvind.rightBumper().onTrue(
                Claw.incrementWrist(0.1)
        );
        arvind.leftBumper().onTrue(
                Claw.incrementWrist(-0.1)
        );
        arvind.rightTrigger().conditionalBindState().greaterThan(0.0).bind().whileTrue(
                IntakeSlides.setPower(1.0)
        );
        arvind.rightTrigger().conditionalBindState().lessThanEqualTo(0.0).bind().onTrue(
                IntakeSlides.setPower(IntakeSlides.constantPower)
        );
        arvind.leftTrigger().conditionalBindState().greaterThan(0.0).bind().whileTrue(
                IntakeSlides.setPower(-1.0)
        );
        arvind.leftTrigger().conditionalBindState().lessThanEqualTo(0.0).bind().onTrue(
                IntakeSlides.setPower(-IntakeSlides.constantPower)
        );
        arvind.dpadLeft().onTrue(
                Claw.preIntake
        );
        arvind.square().onTrue(
                Claw.toggleGripper()
        );
    }

    @Override
    public void loop() {
//        telemetry.addData("right current", OuttakeSlides.slideR.getCurrent(CurrentUnit.MILLIAMPS));
//        telemetry.addData("right current", OuttakeSlides.slideL.getCurrent(CurrentUnit.MILLIAMPS));
//        telemetry.addData("pipeline", Robot.pipeline.getPosition());
    }
}
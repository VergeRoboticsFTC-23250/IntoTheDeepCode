package org.firstinspires.ftc.teamcode.teleop;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.util.dairy.Robot;
import org.firstinspires.ftc.teamcode.util.dairy.features.LoopTimes;
import org.firstinspires.ftc.teamcode.util.dairy.subsystems.Chassis;
import org.firstinspires.ftc.teamcode.util.dairy.subsystems.Intake;
import org.firstinspires.ftc.teamcode.util.dairy.subsystems.IntakeSlides;
import org.firstinspires.ftc.teamcode.util.dairy.subsystems.Outtake;
import org.firstinspires.ftc.teamcode.util.dairy.subsystems.OuttakeSlides;

import dev.frozenmilk.dairy.core.FeatureRegistrar;
import dev.frozenmilk.dairy.core.util.features.BulkRead;
import dev.frozenmilk.mercurial.Mercurial;
import dev.frozenmilk.mercurial.bindings.BoundGamepad;
import dev.frozenmilk.mercurial.commands.Lambda;
import dev.frozenmilk.mercurial.commands.groups.Parallel;
import dev.frozenmilk.mercurial.commands.groups.Sequential;
import dev.frozenmilk.mercurial.commands.util.Wait;

@Mercurial.Attach
@Chassis.Attach
@IntakeSlides.Attach
@OuttakeSlides.Attach
@Intake.Attach
@Outtake.Attach
@BulkRead.Attach
@LoopTimes.Attach
@FeatureRegistrar.LogDependencyResolutionExceptions
@TeleOp(name = "TeleOp")
public class Teleop extends OpMode {
    private BoundGamepad tejas;
    private BoundGamepad arvind;


    @Override
    public void init() {
        tejas = Mercurial.gamepad1();
        arvind = Mercurial.gamepad2();

//        tejas.dpadRight().onTrue(
//                Intake.raiseIntake()
//        );

        tejas.square()
                .onTrue(
                        Robot.setState(Robot.State.OUTTAKE_SUBMERSIBLE)
                );
        tejas.circle()
                .onTrue(Robot.setState(Robot.State.INTAKE_SPEC));
        tejas.triangle()
                .onTrue(Robot.setState(Robot.State.OUTTAKE_BUCKET));
        tejas.cross()
                .onTrue(
                        Robot.setState(Robot.State.HOME)
                );
        tejas.dpadLeft()
                .onTrue(
                        Robot.setState(Robot.State.TRANSFER)
                );

        arvind.dpadRight()
                .onTrue(
                        Intake.extraIntake()
                );

        tejas.dpadUp()
                .onTrue(
                        OuttakeSlides.setPowerCommand(1.0)
                ).onFalse(
                        OuttakeSlides.setPowerCommand(0.0)
                );
        tejas.dpadDown()
                .onTrue(
                        OuttakeSlides.setPowerCommand(-1.0)
                ).onFalse(
                        OuttakeSlides.setPowerCommand(0.0)
                );

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

        arvind.rightStickY().conditionalBindState().greaterThan(0.0).bind().onTrue(Intake.dropIntake()).onFalse(Intake.raiseIntake());

        arvind.rightBumper().onTrue(Robot.manipulate());

        arvind.leftBumper().onTrue(Intake.spintake(-1)).onFalse(Intake.spintake(-0.1));

        arvind.dpadUp().onTrue(Intake.spintake(0.5)).onFalse(Intake.spintake(0));

    }

    @Override
    public void loop() {

    }
}
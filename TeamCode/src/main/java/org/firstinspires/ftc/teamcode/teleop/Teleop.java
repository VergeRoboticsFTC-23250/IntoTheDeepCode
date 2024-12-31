package org.firstinspires.ftc.teamcode.teleop;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.util.dairy.Robot;
import org.firstinspires.ftc.teamcode.util.dairy.features.LoopTimes;
import org.firstinspires.ftc.teamcode.util.dairy.subsystems.Chassis;
import org.firstinspires.ftc.teamcode.util.dairy.subsystems.IntakeSlides;
import org.firstinspires.ftc.teamcode.util.dairy.subsystems.Outtake;
import org.firstinspires.ftc.teamcode.util.dairy.subsystems.OuttakeSlides;

import dev.frozenmilk.dairy.core.FeatureRegistrar;
import dev.frozenmilk.dairy.core.util.features.BulkRead;
import dev.frozenmilk.mercurial.Mercurial;
import dev.frozenmilk.mercurial.bindings.BoundGamepad;
import dev.frozenmilk.mercurial.commands.Lambda;
import dev.frozenmilk.mercurial.commands.groups.Parallel;

@Mercurial.Attach
@Chassis.Attach
//@IntakeSlides.Attach
@OuttakeSlides.Attach
//@Intake.Attach
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

        tejas.dpadUp()
                .whileTrue(OuttakeSlides.setPowerCommand(1));
        tejas.dpadDown()
                .whileTrue(OuttakeSlides.setPowerCommand(-1));

        tejas.cross()
                .onTrue(Robot.setState(Robot.State.HOME));
        tejas.triangle()
                .onTrue(Robot.setState(Robot.State.OUTTAKE_SUBMERSIBLE));
        tejas.square()
                .onTrue(Robot.setState(Robot.State.OUTTAKE_BUCKET));
        tejas.circle()
                .onTrue(Robot.setState(Robot.State.INTAKE));
    }

    @Override
    public void loop() {
        // https://github.com/Iris-TheRainbow/27971-IntoTheDeep-Teamcode/tree/main
//        telemetry.addData("sloth load worked",true);
    }
}
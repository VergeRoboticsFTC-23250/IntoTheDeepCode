package org.firstinspires.ftc.teamcode.teleop;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.util.dairy.features.LoopTimes;
import org.firstinspires.ftc.teamcode.util.dairy.subsystems.Outtake;

import dev.frozenmilk.dairy.core.FeatureRegistrar;
import dev.frozenmilk.dairy.core.util.features.BulkRead;
import dev.frozenmilk.mercurial.Mercurial;
import dev.frozenmilk.mercurial.bindings.BoundGamepad;
import dev.frozenmilk.mercurial.commands.Lambda;
import dev.frozenmilk.mercurial.commands.groups.Parallel;

@Mercurial.Attach
//@Chassis.Attach
//@IntakeSlides.Attach
//@OuttakeSlides.Attach
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

        tejas.cross()
                .onTrue(Lambda.from(
                        new Parallel(
                                Outtake.setArm(Outtake.armHomePos),
                                Outtake.setPivot(Outtake.pivotHomePos)
                        )
                ));
        tejas.triangle()
                .onTrue(Lambda.from(
                        new Parallel(
                                Outtake.setArm(Outtake.armSubmersiblePos),
                                Outtake.setPivot(Outtake.pivotSubmersiblePos)
                        )
                ));
        tejas.square()
                .onTrue(Lambda.from(
                        new Parallel(
                                Outtake.setArm(Outtake.armBucketPos),
                                Outtake.setPivot(Outtake.pivotBucketPos)
                        )
                ));
        tejas.circle()
                .onTrue(Lambda.from(
                        new Parallel(
                                Outtake.setArm(Outtake.armSpecPos),
                                Outtake.setPivot(Outtake.pivotSpecPos)
                        )
                ));
        tejas.rightBumper()
                .onTrue(Lambda.from(
                        new Parallel(
                                Outtake.setArm(Outtake.armTransferPos),
                                Outtake.setPivot(Outtake.pivotTranferPos)
                        )
                ));
        tejas.leftBumper()
                .onTrue(Outtake.setArm(Outtake.armTransitionPos));
    }

    @Override
    public void loop() {
        // https://github.com/Iris-TheRainbow/27971-IntoTheDeep-Teamcode/tree/main
//        telemetry.addData("sloth load worked",true);
    }
}
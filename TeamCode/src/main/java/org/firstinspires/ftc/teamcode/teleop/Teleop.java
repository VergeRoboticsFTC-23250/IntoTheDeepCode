package org.firstinspires.ftc.teamcode.teleop;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.util.dairy.features.LoopTimes;
import org.firstinspires.ftc.teamcode.util.dairy.subsystems.Chassis;
import org.firstinspires.ftc.teamcode.util.dairy.subsystems.Intake;
import org.firstinspires.ftc.teamcode.util.dairy.subsystems.IntakeSlides;
import org.firstinspires.ftc.teamcode.util.dairy.subsystems.Outtake;
import org.firstinspires.ftc.teamcode.util.dairy.subsystems.OuttakeSlides;

import dev.frozenmilk.dairy.core.util.OpModeLazyCell;
import dev.frozenmilk.dairy.core.util.features.BulkRead;
import dev.frozenmilk.mercurial.Mercurial;
import dev.frozenmilk.mercurial.bindings.BoundGamepad;
import dev.frozenmilk.mercurial.commands.groups.Parallel;
import dev.frozenmilk.mercurial.commands.groups.Sequential;

@Mercurial.Attach
@Chassis.Attach
//@IntakeSlides.Attach
@OuttakeSlides.Attach
//@Intake.Attach
//@Outtake.Attach
@BulkRead.Attach
@LoopTimes.Attach
@TeleOp(name = "TeleOp")
public class Teleop extends OpMode {
    private BoundGamepad tejas;
    private BoundGamepad arvind;

    @Override
    public void init() {
        tejas = Mercurial.gamepad1();
        arvind = Mercurial.gamepad2();


//        tejas.triangle().onTrue(new Sequential(
//                Outtake.closeClaw(),
//                new Parallel(
//                        OuttakeSlides.setTargetPos(OuttakeSlides.bucketPos),
//                        Outtake.setArm(Outtake.armBucketPos),
//                        Outtake.setPivot(Outtake.pivotBucketPos)
//                )
//        ));
//
//
//        tejas.circle().onTrue(new Sequential(
//                Outtake.closeClaw(),
//                new Parallel(
//                        OuttakeSlides.setTargetPos(OuttakeSlides.submirsiblePos),
//                        Outtake.setArm(Outtake.armSubmersiblePos),
//                        Outtake.setPivot(Outtake.pivotSubmersiblePos)
//                )
//        ));
//
//
//        if (tejas.square().state() && Utils.isWithinTolerance(OuttakeSlides.getPos(), OuttakeSlides.submirsiblePos, 10)) {
//            new Sequential(
//                    OuttakeSlides.setTargetPos(OuttakeSlides.scoreSubmersiblePos),
//                    Outtake.openClaw()
//            );
//        }
//
//        tejas.cross().onTrue(
//                new Sequential(
//                        new Parallel(
//                                OuttakeSlides.returnLeft(),
//                                OuttakeSlides.returnRight(),
//                                Outtake.setArm(Outtake.armHomePos),
//                                Outtake.setPivot(Outtake.pivotHomePos)
//                        ),
//                        Outtake.openClaw()
//                )
//        );
//
//        tejas.rightBumper().onTrue(Outtake.openClaw());

        tejas.dpadUp()
                .onTrue( OuttakeSlides.setVelo(1) )
                .onFalse( OuttakeSlides.setVelo(0) );
        tejas.dpadDown()
                .onTrue( OuttakeSlides.setVelo(-1) )
                .onFalse( OuttakeSlides.setVelo(0) );
        tejas.leftBumper()
                .onTrue( Outtake.closeClaw() );
        tejas.rightBumper()
                .onTrue( Outtake.openClaw() );

//        tejas.rightBumper().onTrue( Chassis.toggleSlow() );
//
        arvind.rightBumper().whileTrue( Intake.spintake(1) );
        arvind.leftBumper().whileTrue( Intake.spintake(-1) );
//
//        arvind.cross().onTrue(
//                new Sequential(
//                        Intake.raiseIntake(),
//                        IntakeSlides.setPosition(IntakeSlides.minPos)
//                )
//        );
//        arvind.triangle().onTrue( Intake.dropIntake() );
    }

    @Override
    public void loop() {
        // https://github.com/Iris-TheRainbow/27971-IntoTheDeep-Teamcode/tree/main
        telemetry.addData("sloth load",true);
    }
}
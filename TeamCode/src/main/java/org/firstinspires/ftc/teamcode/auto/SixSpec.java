package org.firstinspires.ftc.teamcode.auto;

import com.pedropathing.localization.Pose;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

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
@Autonomous
public class SixSpec extends OpMode {
    public static Pose intakePose = new Pose(28, 22, Math.toRadians(-120));
    public static double[] intakeOffsetsY = {0, 0, 0, 0, 0};
    public static double[] intakeOffsetsX = {0, 0, 0, 0, 0};
    public static Pose preloadOuttakePose = new Pose(40, 35, 0);
    public static Pose outtakePose = new Pose(40, 35, Math.PI);
    public static double[] outtakeOffsetsY = {10, 8, 6, 4, 2, 0};
    public static double[] outtakeOffsetsX = {0, 0, 0, 0, 0, 0};
//    Command OuttakePreload(int i){
//        return new Parallel(
//                OuttakeSlides.runToPosition(OuttakeSlides.submersiblePos),
//                new Wait(0.5).then(new Parallel(
//                        Outtake.setArm(Outtake.armSubmersiblePos),
//                        Outtake.setPivot(Outtake.pivotSubmersiblePos)
//                )),
//                new Sequential(
//                        Chassis.setSloppy(),
//                        Chassis.driveToPoint(
//                                new Pose(
//                                        preloadOuttakePose.getX() + outtakeOffsetsX[i],
//                                        preloadOuttakePose.getY() + outtakeOffsetsY[i],
//                                        preloadOuttakePose.getHeading())
//                        ),
//                        Chassis.setExact(),
//                        Chassis.setConstantDrivePower(1),
//                        OuttakeSlides.runToPosition(OuttakeSlides.scoreSubmersiblePos),
//                        Outtake.openClaw(),
//                        Chassis.releaseConstantDrivePower()
//                )
//        );
//    }
//
//    Command Intake(int i){
//        return new Sequential(
//                Chassis.setSloppy(),
//                OuttakeSlides.runToPosition(OuttakeSlides.minPos),
//                Chassis.driveToPoint(
//                        new Pose(
//                                intakePose.getX() + intakeOffsetsX[i],
//                                intakePose.getY() + intakeOffsetsY[i],
//                                intakePose.getHeading()
//                        )
//                ),
//                new Parallel(
//                        IntakeSlides.extend()
//                        //IntakeClaw.preIntake
//                ),
//                new Wait(0.5)
////                IntakeClaw.dropGrab,
////                IntakeSlides.home()
//        );
//    }

    @Override
    public void init() {
//        Robot.init();
//        Chassis.holdPoint = true;
//        Outtake.setClaw(Outtake.clawClosePos);
//        Outtake.isClawOpen = false;
//        Outtake.setPosition(Outtake.armInitPos);
//        Outtake.setPivotManual(Outtake.pivotOuttakeSpec);
//        IntakeSlides.setPowerManual(-0.3);
//        //IntakeDropDown.setIntakeManual(IntakeDropDown.delete);
//
//        Robot.stateMachine.setState(Robot.State.INTAKE_SPEC);
////        new Sequential(
////                IntakeClaw.preIntake,
////                IntakeClaw.closeGripperFirm()
////        ).schedule();
    }

    @Override
    public void start() {
//        new Sequential(
//                OuttakePreload(0),
//                Intake(0),
//                new Parallel(
//                        //IntakeClaw.preTransfer,
//                        Chassis.driveToPoint(outtakePose)
//                )
//        ).schedule();
    }

    @Override
    public void loop() {

    }
}
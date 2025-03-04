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
import dev.frozenmilk.mercurial.commands.Command;
import dev.frozenmilk.mercurial.commands.Lambda;
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
public class SixSpec extends OpMode {
    //Tuning Values
    public static int cycles = 1;

    public static Pose outtakePreloadPose = new Pose(40, 70, 0);

    public static Pose outtakePose = new Pose(40, 70, Math.PI);
    public static double[] outtakeOffsetsY = {0, 0, 0, 0, 0};
    public static double[] outtakeOffsetsX = {0, 0, 0, 0, 0};

    public static Pose dropSampPose = new Pose(27, 58, Math.toRadians(-120));
    public static Pose intakePose = dropSampPose;
    public static double[] intakeOffsetsY = {0, 0, 0, 0, 0};
    public static double[] intakeOffsetsX = {0, 0, 0, 0, 0};
    public static double intakeWristPos = 0.5;

    //Supporting Functions
    public static Pose intakePose(int i) {
        return new Pose(
            intakePose.getX() + intakeOffsetsX[i],
            intakePose.getY() + intakeOffsetsY[i],
            intakePose.getHeading()
        );
    }

    public static Pose outtakePose(int i) {
        return new Pose(
                outtakePose.getX() + outtakeOffsetsX[i],
                outtakePose.getY() + outtakeOffsetsY[i],
                outtakePose.getHeading()
        );
    }
//    Command PreCycle(){
//        return new Sequential(
//                Chassis.setSloppy(),
//                Chassis.driveToPoint(outtakePreloadPose).with(Robot.setIntakeState(Robot.State.INTAKE_GROUND)),
//                Chassis.setConstantDrivePower(1),
//                Robot.manipulateOuttake().with(Robot.setIntakeState(Robot.State.CAMERA)),
//                OuttakeSlides.waitForRunToPos(),
//                Robot.manipulateOuttake(),
//                Chassis.setConstantDrivePower(-.25),
//                new Wait(1),
//                Robot.manipulateIntake(),
//                Chassis.releaseConstantDrivePower(),
//                Chassis.driveToPoint(dropSampPose),
//                Robot.outtakeGroundAndHome()
//        ).with(Robot.setState(Robot.State.OUTTAKE_FRONT));
//    }

    Command PreCycle(){
        return new Sequential(
                Chassis.setSloppy(),
                Chassis.driveToPoint(outtakePreloadPose),
                Chassis.setConstantDrivePower(1).with(Robot.setIntakeState(Robot.State.CAMERA)),
                Robot.manipulateOuttake(),
                OuttakeSlides.waitForRunToPos(),
                Robot.manipulateOuttake(),
                Chassis.setConstantDrivePower(-0.2),
                new Wait(0.75),
                Chassis.releaseConstantDrivePowerAndHold(),
                Robot.setIntakeState(Robot.State.INTAKE_GROUND),
                new Wait(3),
                Robot.setIntakeState(Robot.State.INTAKE_GROUND_SECONDARY),
                new Wait(0.125),
                IntakeClaw.closeLoose(),
                new Wait(0.125),
                new Parallel(
                        Chassis.setClean(),
                        Chassis.driveToPoint(new Pose(12, 23.5, 0)),
                        new Sequential(
                                new Parallel(
                                        Robot.setIntakeState(Robot.State.HOME).then(Robot.setIntakeState(Robot.State.HOME)),
                                        Robot.setOuttakeState(Robot.State.HOME)
                                ),
                                Robot.setState(Robot.State.TELEOP_TRANSFER),
                                OuttakeClaw.closeFirm(),
                                IntakeClaw.open(),
                                new Wait(0.25),
                                Robot.setOuttakeState(Robot.State.DROP_SAMPLE).with(Robot.setIntakeState(Robot.State.INTAKE_GROUND))
                        )
                ),
                OuttakeClaw.open().with(Robot.setIntakeState(Robot.State.INTAKE_GROUND_SECONDARY)),
                new Wait(0.125),
                IntakeClaw.closeLoose(),
                new Wait(0.125),
                new Parallel(
                        Chassis.driveToPoint(new Pose(12, 13, 0)),
                        new Sequential(
                                new Parallel(
                                        Robot.setIntakeState(Robot.State.HOME).then(Robot.setIntakeState(Robot.State.HOME)),
                                        Robot.setOuttakeState(Robot.State.HOME)
                                ),
                                Robot.setState(Robot.State.TELEOP_TRANSFER),
                                OuttakeClaw.closeFirm(),
                                IntakeClaw.open(),
                                new Wait(0.25),
                                Robot.setOuttakeState(Robot.State.DROP_SAMPLE).with(Robot.setIntakeState(Robot.State.INTAKE_GROUND))
                        )
                ),
                OuttakeClaw.open().with(Robot.setIntakeState(Robot.State.INTAKE_GROUND_SECONDARY)),
                new Wait(0.125),
                IntakeClaw.closeLoose(),
                new Wait(0.125),
                new Parallel(
                        Chassis.driveToPoint(new Pose(17, 19, Math.toRadians(-28))),
                        new Sequential(
                                new Parallel(
                                        Robot.setIntakeState(Robot.State.HOME).then(Robot.setIntakeState(Robot.State.HOME)),
                                        Robot.setOuttakeState(Robot.State.HOME)
                                ),
                                Robot.setState(Robot.State.TELEOP_TRANSFER),
                                OuttakeClaw.closeFirm(),
                                IntakeClaw.open(),
                                new Wait(0.25),
                                Robot.setOuttakeState(Robot.State.DROP_SAMPLE).with(Robot.setIntakeState(Robot.State.INTAKE_GROUND)),
                                Differential.IntakeWrist.setPos(0.65)
                        )
                ),
                OuttakeClaw.open().with(Robot.setIntakeState(Robot.State.INTAKE_GROUND_SECONDARY)),
                new Wait(0.125),
                IntakeClaw.closeLoose(),
                new Wait(0.125),
                IntakeDropDown.setPos(IntakeDropDown.intake),
                Chassis.driveToPoint(intakePose)
        ).with(Robot.setOuttakeState(Robot.State.OUTTAKE_FRONT)).with(Robot.setIntakeState(Robot.State.INTAKE_GROUND));
    }

    Command Cycle(int i){
        return new Sequential(
                Chassis.setClean(),
                Chassis.driveToPoint(intakePose(i)),
                Differential.IntakeWrist.setPos(intakeWristPos),
                new Wait(1),
                Robot.setIntakeState(Robot.State.INTAKE_GROUND_SECONDARY),
                new Wait(0.125),
                IntakeClaw.closeLoose(),
                new Wait(0.25),
                Chassis.setSloppy(),
                new Lambda("manual-set-intake-state").setInit(() -> Robot.setCurrentIntakeState(Robot.State.INTAKE_GROUND)),
                new Parallel(
                        Chassis.driveToPoint(outtakePose(i)).then(Chassis.setConstantDrivePower(1)),
                        new Sequential(
                                Robot.setState(Robot.State.HOME),
                                Robot.setState(Robot.State.TELEOP_TRANSFER),
                                OuttakeClaw.closeFirm(),
                                IntakeClaw.open(),
                                Robot.setState(Robot.State.OUTTAKE_BACK)
                        )
                ),
                Robot.setState(Robot.State.OUTTAKE_BACK_SECONDARY),
                Robot.setIntakeState(Robot.State.INTAKE_GROUND),
                Robot.manipulateOuttake(),
                Chassis.releaseConstantDrivePower()
        ).with(Robot.setOuttakeState(Robot.State.HOME));
    }

    @Override
    public void init() {
        Robot.init(hardwareMap);
        OuttakeArm.setPosManual(OuttakeArm.init);
        Robot.setState(Robot.State.INIT).execute();
        Robot.setState(Robot.State.INIT).schedule();
    }

    @Override
    public void start() {
//        List<Command> commands = IntStream.range(0, cycles).mapToObj(this::Cycle).collect(Collectors.toList());
//        commands.add(0, PreCycle());
//        new Sequential(commands).schedule();

        new Sequential(
                PreCycle()
//                Chassis.releaseHeading(),
//                Cycle(0).with(new Wait(0.5).then(Robot.setState(Robot.State.INTAKE_GROUND))),
//                Cycle(1),
//                Cycle(2),
//                Cycle(3),
//                Cycle(4)
        ).schedule();
    }

    @Override
    public void loop() {}
}
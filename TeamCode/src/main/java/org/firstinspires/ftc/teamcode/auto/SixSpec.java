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
    public static int cycles = 0;

    public static Pose outtakePreloadPose = new Pose(40, 70, 0);

    public static Pose outtakePose = new Pose(40, 70, Math.PI);
    public static double[] outtakeOffsetsY = {0, 0, 0, 0, 0};
    public static double[] outtakeOffsetsX = {0, 0, 0, 0, 0};

    public static Pose dropSampPose = new Pose(24.5, 55, Math.toRadians(-120));
    public static Pose intakePose = dropSampPose;
    public static double[] intakeOffsetsY = {0, 0, 0, 0, 0};
    public static double[] intakeOffsetsX = {0, 0, 0, 0, 0};
    public static double intakeWristPos = 0.75;

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
    Command PreCycle(){
        return new Sequential(
                Chassis.setSloppy(),
                Chassis.driveToPoint(outtakePreloadPose).with(Robot.setIntakeState(Robot.State.INTAKE_GROUND)),
                Chassis.setConstantDrivePower(1),
                Robot.manipulateOuttake().with(Robot.setIntakeState(Robot.State.CAMERA)),
                OuttakeSlides.waitForRunToPos(),
                Robot.manipulateOuttake(),
                Chassis.setConstantDrivePower(-.25),
                new Wait(1),
                Robot.manipulateIntake(),
                Chassis.releaseConstantDrivePower(),
                Chassis.driveToPoint(dropSampPose).with(Robot.outtakeGroundAndHome())
        ).with(Robot.setState(Robot.State.OUTTAKE_FRONT));
    }

    Command Cycle(int i){
        return new Sequential(
                Chassis.setClean(),
                Chassis.driveToPoint(intakePose(i)),
                Differential.IntakeWrist.setPos(intakeWristPos),
                Robot.manipulate(),
                new Wait(0.875),
                Chassis.setSloppy(),
                new Parallel(
                        Chassis.driveToPoint(outtakePose(i)).then(Chassis.setConstantDrivePower(1)),
                        new Sequential(
                                Robot.setState(Robot.State.TELEOP_TRANSFER),
                                new Wait(0.25),
                                Robot.manipulate()
                        )
                ),
                Robot.setState(Robot.State.OUTTAKE_BACK_SECONDARY),
                new Wait(0.25),
                Robot.manipulate(),
                Chassis.releaseConstantDrivePower()
        ).with(Robot.setState(Robot.State.INTAKE_GROUND));
    }

    @Override
    public void init() {
        Robot.init();
        OuttakeArm.setPosManual(OuttakeArm.init);
    }

    @Override
    public void start() {
        List<Command> commands = IntStream.range(0, cycles).mapToObj(this::Cycle).collect(Collectors.toList());
        commands.add(0, PreCycle());
        new Sequential(commands).schedule();
    }

    @Override
    public void loop() {}
}
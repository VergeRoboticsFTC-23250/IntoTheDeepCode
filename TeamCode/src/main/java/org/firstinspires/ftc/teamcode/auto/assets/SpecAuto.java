package org.firstinspires.ftc.teamcode.auto.assets;

import static org.firstinspires.ftc.teamcode.auto.assets.SpecAutoParams.*;

import com.pedropathing.localization.Pose;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.util.BezierCurve;
import org.firstinspires.ftc.teamcode.util.dairy.Robot;
import org.firstinspires.ftc.teamcode.util.dairy.subsystems.Chassis;
import org.firstinspires.ftc.teamcode.util.dairy.subsystems.outtake.OuttakeClaw;
import org.firstinspires.ftc.teamcode.util.dairy.subsystems.outtake.OuttakeSlides;

import java.util.Arrays;
import java.util.stream.Collectors;

import dev.frozenmilk.mercurial.commands.Command;
import dev.frozenmilk.mercurial.commands.groups.Sequential;
import dev.frozenmilk.mercurial.commands.util.IfElse;
import dev.frozenmilk.mercurial.commands.util.Wait;

public class SpecAuto {
    boolean plus1;
    boolean park;
    HardwareMap hardwareMap;

    BezierCurve curveToWall = new BezierCurve(
            outtakePose,
            intakePose
    );

    BezierCurve curveToTruss = new BezierCurve(
            intakePose,
            outtakePose
    );

    public SpecAuto(HardwareMap hardwareMap, boolean plus1, boolean park){
        this.plus1 = plus1;
        this.park = park;
        this.hardwareMap = hardwareMap;
    }

    Command OuttakePreload(){
        Pose pose = outtakePose.copy();
        pose.add(new Pose(outtakeOffsetsX[0], outtakeOffsetsY[0], 0));
        return new Sequential(
                Chassis.driveToPoint(pose).with(Robot.setState(Robot.State.OUTTAKE_FRONT)),
                OuttakeSlides.setPIDMultiplier(3),
                Chassis.setConstantDrivePower(outtakePushPower).with(Robot.setState(Robot.State.OUTTAKE_FRONT_SECONDARY)),
                new Wait(preOuttakeDelay),
                OuttakeClaw.open(),
                new Wait(postOuttakeDelay),
                Chassis.releaseConstantDrivePower(),
                OuttakeSlides.resetPID()
        );
    }

    Command Outtake(int i){
        Pose pose = outtakePose.copy();
        pose.add(new Pose(outtakeOffsetsX[i], outtakeOffsetsY[i], 0));
        return new Sequential(
                Chassis.driveToPoint(pose).with(Robot.setState(Robot.State.OUTTAKE_FRONT)),
                OuttakeSlides.setPIDMultiplier(3),
                Chassis.setConstantDrivePower(outtakePushPower).with(Robot.setState(Robot.State.OUTTAKE_FRONT_SECONDARY)),
                new Wait(preOuttakeDelay),
                OuttakeClaw.open(),
                new Wait(postOuttakeDelay),
                Chassis.releaseConstantDrivePower(),
                OuttakeSlides.resetPID()
        );
    }
    Command Outtake(){return Outtake(0);}

    Command IntakePreload(){
        Pose pose = intakePose.copy();
        pose.add(new Pose(intakeOffsetsX[0], intakeOffsetsY[0], 0));
        Pose offsetPose = pose.copy();
        offsetPose.add(new Pose(intakeHelperOffset, 0, 0));

        return new Sequential(
                Chassis.driveToPoint(offsetPose).with(Robot.setState(Robot.State.INTAKE_BACK)),
                Chassis.driveToPoint(pose),
                Chassis.setConstantDrivePower(-intakePushPower),
                new Wait(preIntakeDelay),
                OuttakeClaw.closeFirm().with(Chassis.releaseConstantDrivePower()),
                new Wait(postIntakeDelay)
        );
    }

    Command Intake(int i){
        Pose pose = intakePose.copy();
        pose.add(new Pose(intakeOffsetsX[i], intakeOffsetsY[i], 0));
        Pose offsetPose = pose.copy();
        offsetPose.add(new Pose(intakeHelperOffset, 0, 0));

        return new Sequential(
                Chassis.driveToPoint(offsetPose).with(Robot.setState(Robot.State.INTAKE_BACK)),
                Chassis.driveToPoint(pose),
                Chassis.setConstantDrivePower(-intakePushPower),
                new Wait(preIntakeDelay),
                OuttakeClaw.closeFirm().with(Chassis.releaseConstantDrivePower()),
                new Wait(postIntakeDelay)
        );
    }
    Command Intake(){return Intake(intakeOffsetsY.length - 1);}

    Command Cycle(int i){
        return new Sequential(
            new IfElse(
                () -> i == 0,
                    IntakePreload(),
                    Intake(i)
            ),
            Outtake(i+1)
        );
    }

    Command PushSamps = new Sequential(Arrays.stream(pushSampPoses).map(Chassis::driveToPoint).collect(Collectors.toList()));
    Command PreCycle = OuttakePreload().then(PushSamps.with(Robot.setState(Robot.State.INTAKE_BACK)));
    Command Plus1 = new Sequential(
            Intake(),
            Chassis.driveToPoint(bucketPose).with(Robot.setState(Robot.State.BUCKET)),
            OuttakeClaw.open()
    );
    Command Park = Chassis.driveToPoint(parkPose).with(Robot.setIntakeState(Robot.State.CAMERA)).with(Robot.setOuttakeState(Robot.State.INIT));

    public void init() {
        Robot.init(hardwareMap);
    }

    public void start() {
        new Sequential(
                PreCycle,
                Cycle(0),
                Cycle(1),
                Cycle(2),
                Cycle(3),
                new IfElse(() -> plus1, Plus1, new Wait(0)),
                new IfElse(() -> park, Park, Robot.setState(Robot.State.INIT))
        ).schedule();
    }

    public void loop() {

    }
}

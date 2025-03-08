package org.firstinspires.ftc.teamcode.auto.assets;

import static org.firstinspires.ftc.teamcode.auto.assets.SpecAutoParams.*;

import com.pedropathing.localization.Pose;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.util.BezierCurve;
import org.firstinspires.ftc.teamcode.util.Util;
import org.firstinspires.ftc.teamcode.util.dairy.Robot;
import org.firstinspires.ftc.teamcode.util.dairy.subsystems.Chassis;
import org.firstinspires.ftc.teamcode.util.dairy.subsystems.outtake.OuttakeClaw;
import org.firstinspires.ftc.teamcode.util.dairy.subsystems.outtake.OuttakeSlides;

import java.util.Arrays;
import java.util.stream.Collectors;

import dev.frozenmilk.mercurial.commands.Command;
import dev.frozenmilk.mercurial.commands.groups.Parallel;
import dev.frozenmilk.mercurial.commands.groups.Sequential;
import dev.frozenmilk.mercurial.commands.util.IfElse;
import dev.frozenmilk.mercurial.commands.util.Wait;

public class SpecAuto {
    boolean plus1;
    boolean park;
    HardwareMap hardwareMap;
    Pose intakePoseOffset = intakePose.copy();

    static Util.Scale curveToWallScalar = new Util.Scale(0, outtakePose.getX() - intakePose.getX());

    public static BezierCurve curveToWall(int i){
        Pose pose = outtakePose.copy();
        pose.add(new Pose(0, offsets[i], 0));
        return new BezierCurve(
                pose,
                new Pose(outtakePose.getX() - curveToWallScalar.scale(curveToWallAmount), outtakePose.getY(), 0),
                new Pose(intakePose.getX() + curveToWallScalar.scale(curveToWallAmount), intakePose.getY(), 0),
                intakePose
        );
    }
    static Util.Scale curveToTrussScalar = new Util.Scale(0, outtakePose.getX() - intakePose.getX());
    public static BezierCurve curveToTruss(int i){
        Pose pose = outtakePose.copy();
        pose.add(new Pose(0, offsets[i], 0));
        return new BezierCurve(
                intakePose,
                new Pose(intakePose.getX() + curveToTrussScalar.scale(curveToTrussAmount), intakePose.getY(), 0),
                new Pose(outtakePose.getX() - curveToTrussScalar.scale(curveToTrussAmount), outtakePose.getY(), 0),
                pose
        );
    }
    public SpecAuto(HardwareMap hardwareMap, boolean plus1, boolean park){
        this.plus1 = plus1;
        this.park = park;
        this.hardwareMap = hardwareMap;
        intakePoseOffset.add(new Pose(4, 0, 0));
    }
    Command OuttakePreload(){
        return new Sequential(
                Chassis.driveToPoint(outtakePose).with(Robot.setState(Robot.State.OUTTAKE_FRONT)),
                Chassis.setConstantDrivePower(outtakePushPower),
                OuttakeSlides.score(duringOuttakeDelay),
                Chassis.releaseConstantDrivePower(),
                OuttakeClaw.open()
        );
    }
    Command Outtake(int i){
        return new Sequential(
                Chassis.followBezierCurve(SpecAuto.curveToTruss(i)).with(Robot.setState(Robot.State.OUTTAKE_FRONT)),
                Chassis.setConstantDrivePower(outtakePushPower),
                OuttakeSlides.score(duringOuttakeDelay),
                Chassis.releaseConstantDrivePower(),
                OuttakeClaw.open()
        );
    }
    Command Intake(int i){
        return new Sequential(
                Chassis.followBezierCurve(SpecAuto.curveToWall(i)).with(Robot.setState(Robot.State.INTAKE_BACK)),
                Chassis.setConstantDrivePower(-intakePushPower),
                new Wait(preIntakeDelay),
                OuttakeClaw.closeFirm().with(Chassis.releaseConstantDrivePower()),
                new Wait(postIntakeDelay)
        );
    }
    Command IntakeFirst(){
        return new Sequential(
                Chassis.driveToPoint(intakePose).with(Robot.setState(Robot.State.INTAKE_BACK)),
                Chassis.setConstantDrivePower(-intakePushPower),
                new Wait(preIntakeDelay),
                OuttakeClaw.closeFirm().with(Chassis.releaseConstantDrivePower()),
                new Wait(postIntakeDelay)
        );
    }

    Command Cycle(int i){
        return new Sequential(
                new IfElse(() -> i == 0, IntakeFirst(), Intake(i)),
                Outtake(i+1)
        );
    }

    //Command PushSamps = new Sequential(Arrays.stream(pushSampPoses).map(Chassis::driveToPoint).collect(Collectors.toList()));

    Command PushSamps = new Sequential(
            Chassis.resetTolerance(),
            Chassis.followBezierCurve(samplePushCurves[0]),
            Chassis.driveToPoint(samp1),
            Chassis.followBezierCurve(samplePushCurves[1]),
            Chassis.driveToPoint(samp2),
            Chassis.followBezierCurve(samplePushCurves[2]),
            Chassis.driveToPoint(samp3),
            Chassis.driveToPoint(intakePoseOffset)
    );

    Command PreCycle = OuttakePreload().then(PushSamps.with(Robot.setState(Robot.State.INTAKE_BACK)));

    public void start() {
        new Sequential(
                PreCycle,
                Cycle(0),
                Cycle(1),
                Cycle(2),
                Cycle(3),
                Intake(4)
        ).schedule();
    }

    public void loop() {

    }
}

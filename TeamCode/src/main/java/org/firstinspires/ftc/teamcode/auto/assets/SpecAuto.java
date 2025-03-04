package org.firstinspires.ftc.teamcode.auto.assets;

import static org.firstinspires.ftc.teamcode.auto.assets.SpecAutoParams.*;

import com.pedropathing.localization.Pose;

import org.firstinspires.ftc.teamcode.util.dairy.Robot;
import org.firstinspires.ftc.teamcode.util.dairy.subsystems.Chassis;
import org.firstinspires.ftc.teamcode.util.dairy.subsystems.outtake.OuttakeClaw;

import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import dev.frozenmilk.mercurial.commands.Command;
import dev.frozenmilk.mercurial.commands.Lambda;
import dev.frozenmilk.mercurial.commands.groups.Parallel;
import dev.frozenmilk.mercurial.commands.groups.Sequential;
import dev.frozenmilk.mercurial.commands.util.IfElse;
import dev.frozenmilk.mercurial.commands.util.Wait;

public class SpecAuto {
    int cycles;
    boolean plus1;
    boolean park;

    public SpecAuto(int cycles, boolean plus1, boolean park){
        this.cycles = cycles;
        this.plus1 = plus1;
        this.park = park;
    }

    Command Outtake(){return Outtake(0);}
    Command Outtake(int i){
        Pose pose = outtakePose.copy();
        pose.add(new Pose(outtakeOffsetsX[i], outtakeOffsetsY[i], 0));
        return new Sequential(
                Chassis.setSloppy(),
                Chassis.driveToPoint(pose).with(Robot.setState(Robot.State.OUTTAKE_FRONT)),
                Chassis.setConstantDrivePower(outtakePushPower).with(Robot.setState(Robot.State.OUTTAKE_FRONT_SECONDARY)),
                new Wait(preOuttakeDelay),
                OuttakeClaw.open().with(Chassis.releaseConstantDrivePower()),
                new Wait(postOuttakeDelay)
        );
    }

    Command Intake(){return Intake(intakeOffsetsY.length - 1);}
    Command Intake(int i){
        Pose pose = intakePose.copy();
        pose.add(new Pose(intakeOffsetsX[i], intakeOffsetsY[i], 0));
        return new Sequential(
                Chassis.setSloppy(),
                Chassis.driveToPoint(pose).with(Robot.setState(Robot.State.INTAKE_BACK)),
                Chassis.setConstantDrivePower(-intakePushPower),
                new Wait(preIntakeDelay),
                OuttakeClaw.closeFirm().with(Chassis.releaseConstantDrivePower()),
                new Wait(postIntakeDelay)
        );
    }
    Command Cycle(int i){
        return new Sequential(
            Intake(i),
            Outtake(i+1)
        );
    }

    Command PushSamps = new Sequential(Arrays.stream(pushSampPoses).map(Chassis::driveToPoint).collect(Collectors.toList()));
    Command PreCycle = Outtake().then(PushSamps);
    Command Cycles = new Sequential(IntStream.range(0, cycles).mapToObj(this::Cycle).collect(Collectors.toList()));
    Command Plus1 = new Sequential(
            Intake(),
            Chassis.setClean(),
            Chassis.driveToPoint(bucketPose).with(Robot.setState(Robot.State.BUCKET)),
            OuttakeClaw.open(),
            new IfElse(
                    () -> park,
                    new Parallel(),
                    Robot.setState(Robot.State.INIT)
            )
    );
    Command Park = Chassis.driveToPoint(parkPose).with(Robot.setState(Robot.State.CAMERA));

    public void init() {

    }

    public void start() {
        new Sequential(
                PreCycle,
                Cycles
        ).schedule();
    }

    public void loop() {

    }
}

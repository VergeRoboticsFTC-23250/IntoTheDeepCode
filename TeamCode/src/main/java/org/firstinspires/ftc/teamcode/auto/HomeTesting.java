package org.firstinspires.ftc.teamcode.auto;

import com.pedropathing.localization.Pose;
import com.pedropathing.pathgen.Point;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.util.dairy.Paths;
import org.firstinspires.ftc.teamcode.util.dairy.Robot;
import org.firstinspires.ftc.teamcode.util.dairy.subsystems.Chassis;
import org.firstinspires.ftc.teamcode.util.dairy.subsystems.intake.Differential;
import org.firstinspires.ftc.teamcode.util.dairy.subsystems.intake.IntakeClaw;
import org.firstinspires.ftc.teamcode.util.dairy.subsystems.intake.IntakeDropDown;
import org.firstinspires.ftc.teamcode.util.dairy.subsystems.intake.IntakeSlides;
import org.firstinspires.ftc.teamcode.util.dairy.subsystems.outtake.OuttakeArm;
import org.firstinspires.ftc.teamcode.util.dairy.subsystems.outtake.OuttakeClaw;
import org.firstinspires.ftc.teamcode.util.dairy.subsystems.outtake.OuttakePivot;
import org.firstinspires.ftc.teamcode.util.dairy.subsystems.outtake.OuttakeSlides;

import dev.frozenmilk.dairy.core.util.features.BulkRead;
import dev.frozenmilk.mercurial.Mercurial;
import dev.frozenmilk.mercurial.commands.groups.Parallel;
import dev.frozenmilk.mercurial.commands.groups.Sequential;
import dev.frozenmilk.mercurial.commands.util.Wait;

@Mercurial.Attach
@Chassis.Attach
@OuttakeSlides.Attach
@OuttakeClaw.Attach
@OuttakePivot.Attach
@OuttakeArm.Attach
@IntakeDropDown.Attach
@IntakeClaw.Attach
@Differential.Attach
@IntakeSlides.Attach
//@LoopTimes.Attach
@BulkRead.Attach
@Autonomous
//Differential.IntakeWrist.increment(Differential.IntakeWrist.Direction.COUNTER_CLOCKWISE)
public class HomeTesting extends OpMode {

    @Override
    public void init() {
        Robot.init(hardwareMap);
        Chassis.follower.setStartingPose(new Pose(0,0,0));
    }

    @Override
    public void loop() {
        telemetry.addData("heading", Chassis.follower.getPose().getHeading());
        telemetry.addData("x", Chassis.follower.getPose().getX());
        telemetry.addData("y", Chassis.follower.getPose().getY());
        Chassis.follower.update();
    }

    @Override
    public void start() {
        new Sequential(
                Robot.setState(Robot.State.CAMERA),
                new Wait(1),
                Chassis.followPath(Paths.pathTo(new Point(Chassis.follower.getPose().getX() + Robot.vision.getY(), Chassis.follower.getPose().getY() + Robot.vision.getX()), Chassis.follower.getPose()))
        ).schedule();
    }

}
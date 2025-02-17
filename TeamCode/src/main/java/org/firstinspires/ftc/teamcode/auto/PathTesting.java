package org.firstinspires.ftc.teamcode.auto;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.util.dairy.Paths;
import org.firstinspires.ftc.teamcode.util.dairy.Robot;
import org.firstinspires.ftc.teamcode.util.dairy.features.LoopTimes;
import org.firstinspires.ftc.teamcode.util.dairy.subsystems.Chassis;
import org.firstinspires.ftc.teamcode.util.dairy.subsystems.Intake;
import org.firstinspires.ftc.teamcode.util.dairy.subsystems.IntakeSlides;
import org.firstinspires.ftc.teamcode.util.dairy.subsystems.Outtake;
import org.firstinspires.ftc.teamcode.util.dairy.subsystems.OuttakeSlides;

import dev.frozenmilk.dairy.core.util.features.BulkRead;
import dev.frozenmilk.mercurial.Mercurial;
import dev.frozenmilk.mercurial.commands.groups.Parallel;
import dev.frozenmilk.mercurial.commands.groups.Sequential;
import dev.frozenmilk.mercurial.commands.util.Wait;

@Mercurial.Attach
@Chassis.Attach
@Outtake.Attach
@OuttakeSlides.Attach
@Intake.Attach
@IntakeSlides.Attach
@LoopTimes.Attach
@BulkRead.Attach
@Autonomous
public class PathTesting extends OpMode {

    @Override
    public void init() {
        Robot.init();
        Outtake.setClaw(Outtake.clawClosePos);
        Outtake.isClawOpen = false;
        Outtake.setPosition(Outtake.armSpecPos);
        Outtake.setPivotManual(Outtake.pivotSpecPos);

        Robot.stateMachine.setState(Robot.State.INTAKE_SPEC);
    }

    @Override
    public void loop() {
        Chassis.follower.telemetryDebug(telemetry);

    }

    @Override
    public void start() {
        new Sequential(
                IntakeSlides.setPower(-0.3),
                Intake.setIntake(Intake.hoverPos)

//                Chassis.followPathChain(Paths.fourSamps.get(0))
//                Chassis.followPathChain(Paths.fourSamps.get(1)),
//                Chassis.followPathChain(Paths.fourSamps.get(2)),
//                Chassis.followPathChain(Paths.fourSamps.get(3)),
//                Chassis.followPathChain(Paths.fourSamps.get(4)),
//                Chassis.followPathChain(Paths.fourSamps.get(5)),
//                Chassis.followPathChain(Paths.fourSamps.get(6)),
//                Chassis.followPathChain(Paths.fourSamps.get(7)),
//                Chassis.followPathChain(Paths.fourSamps.get(8))

        )
                .schedule();
    }
}
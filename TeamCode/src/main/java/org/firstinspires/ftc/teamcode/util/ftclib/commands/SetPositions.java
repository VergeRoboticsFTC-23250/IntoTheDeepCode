package org.firstinspires.ftc.teamcode.util.ftclib.commands;

import com.arcrobotics.ftclib.command.CommandBase;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.util.Robot;
import org.firstinspires.ftc.teamcode.util.StatePositions;
import org.firstinspires.ftc.teamcode.util.ftclib.subsystems.Outtake;
import org.firstinspires.ftc.teamcode.util.ftclib.subsystems.VerticalSlides;

public class SetPositions extends CommandBase {
    private final Outtake outtake;
    private final StatePositions positions;
    private final Telemetry telemetry;

    public SetPositions(Outtake outtake, Robot.State state, Telemetry telemetry) {
        this.telemetry = telemetry;
        this.outtake = outtake;
        this.positions = Robot.states.get(state);
        addRequirements(outtake);
    }

    @Override
    public void initialize() {
        VerticalSlides.setTargetPos(positions.horizontalSlidePos);
        outtake.setIsClawOpen(positions.isClawOpen);
        outtake.setArm(positions.armPos);
        outtake.setPivot(positions.pivotPos);

        Robot.currentState = positions.state;
        telemetry.addData("State", Robot.currentState);
        telemetry.update();
    }

    @Override
    public boolean isFinished() {
        return true;
    }
}
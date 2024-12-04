package org.firstinspires.ftc.teamcode.util.ftclib.commands;

import com.arcrobotics.ftclib.command.CommandBase;

import org.firstinspires.ftc.teamcode.util.Robot;
import org.firstinspires.ftc.teamcode.util.ftclib.subsystems.Outtake;
import org.firstinspires.ftc.teamcode.util.ftclib.subsystems.VerticalSlides;

public class SetRobotState extends CommandBase {
    private final VerticalSlides slides;
    private final Outtake outtake;
    private final Robot.RobotState state;

    public SetRobotState(VerticalSlides slides, Outtake outtake, Robot.RobotState state){
        this.slides = slides;
        this.outtake = outtake;
        this.state = state;
        addRequirements(slides, outtake);
    }

    @Override
    public void initialize() {
        slides.setTargetPos(state.horizontalSlidePos);
        outtake.setIsClawOpen(state.isClawOpen);
        outtake.setArm(state.armPos);
        outtake.setPivot(state.pivotPos);
    }

    @Override
    public boolean isFinished() {
        return true;
    }
}

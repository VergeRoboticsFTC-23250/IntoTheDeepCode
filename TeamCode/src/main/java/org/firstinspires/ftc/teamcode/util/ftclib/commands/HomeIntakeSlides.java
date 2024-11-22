package org.firstinspires.ftc.teamcode.util.ftclib.commands;

import android.util.Log;

import com.arcrobotics.ftclib.command.CommandBase;

import org.firstinspires.ftc.teamcode.util.ftclib.subsystems.IntakeSlides;

public class HomeIntakeSlides extends CommandBase {
    IntakeSlides intakeSlides;

    public HomeIntakeSlides(IntakeSlides intakeSlides) {
        this.intakeSlides = intakeSlides;
        addRequirements(intakeSlides);
    }

    @Override
    public void execute() {
        intakeSlides.setPower(-1);
    }

    @Override
    public boolean isFinished() {
        return intakeSlides.isOverCurrent() && intakeSlides.getPos() < 5;
    }

    @Override
    public void end(boolean interrupted) {
        intakeSlides.resetEncoder();
        intakeSlides.setTargetPos(0);

        super.end(interrupted);
    }
}

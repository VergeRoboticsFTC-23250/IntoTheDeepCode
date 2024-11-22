package org.firstinspires.ftc.teamcode.util.ftclib.commands;

import com.arcrobotics.ftclib.command.CommandBase;

import org.firstinspires.ftc.teamcode.util.ftclib.subsystems.IntakeSlides;

public class RunIntakeSlidesPID extends CommandBase {
    private final IntakeSlides intakeSlides;

    public RunIntakeSlidesPID(IntakeSlides intakeSlides) {
        this.intakeSlides = intakeSlides;
        addRequirements(intakeSlides);
        intakeSlides.resetPID();
    }

    @Override
    public void execute() {
        intakeSlides.updatePID();
    }
}

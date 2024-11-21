package org.firstinspires.ftc.teamcode.util.ftclib.commands;

import com.arcrobotics.ftclib.command.CommandBase;

import org.firstinspires.ftc.teamcode.util.ftclib.subsystems.IntakeSlides;

public class RunIntakeSlidePID extends CommandBase {
    private IntakeSlides intakeSlides;

    public RunIntakeSlidePID(IntakeSlides intakeSlides) {
        this.intakeSlides = intakeSlides;
        addRequirements(intakeSlides);
    }

    @Override
    public void execute() {
        intakeSlides.logPos();
    }
}

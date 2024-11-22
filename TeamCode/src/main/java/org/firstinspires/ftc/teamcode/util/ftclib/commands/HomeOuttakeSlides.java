package org.firstinspires.ftc.teamcode.util.ftclib.commands;

import android.util.Log;

import com.arcrobotics.ftclib.command.CommandBase;

import org.firstinspires.ftc.teamcode.util.ftclib.subsystems.IntakeSlides;
import org.firstinspires.ftc.teamcode.util.ftclib.subsystems.OuttakeSlides;

public class HomeOuttakeSlides extends CommandBase {
    OuttakeSlides outtakeSlides;

    public HomeOuttakeSlides(OuttakeSlides outtakeSlides) {
        this.outtakeSlides = outtakeSlides;
        addRequirements(outtakeSlides);
    }

    @Override
    public void execute() {
        outtakeSlides.setPower(-.5);
    }

    @Override
    public boolean isFinished() {
        return outtakeSlides.isOverCurrent() && outtakeSlides.getPos() < 50;
    }

    @Override
    public void end(boolean interrupted) {
        outtakeSlides.resetEncoder();
        outtakeSlides.setTargetPos(0);

        super.end(interrupted);
    }
}

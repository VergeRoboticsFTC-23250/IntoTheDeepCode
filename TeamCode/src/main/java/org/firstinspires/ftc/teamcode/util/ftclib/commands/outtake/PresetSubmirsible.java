package org.firstinspires.ftc.teamcode.util.ftclib.commands.outtake;

import com.arcrobotics.ftclib.command.CommandBase;

import org.firstinspires.ftc.teamcode.util.ftclib.subsystems.Outtake;
import org.firstinspires.ftc.teamcode.util.ftclib.subsystems.OuttakeSlides;

public class PresetSubmirsible extends CommandBase {
    Outtake outtake;
    public PresetSubmirsible(Outtake outtake) {
        this.outtake = outtake;
        addRequirements(outtake);

        OuttakeSlides.setTargetPos(OuttakeSlides.submirsiblePos);
        //outtake.setArm(Outtake.armSubmirsiblePos);
        outtake.setPivot(Outtake.pivotSubmersiblePos);
    }

    @Override
    public boolean isFinished() {
        return Math.abs(OuttakeSlides.getPos() - OuttakeSlides.submirsiblePos) < OuttakeSlides.tolerance;
    }
}

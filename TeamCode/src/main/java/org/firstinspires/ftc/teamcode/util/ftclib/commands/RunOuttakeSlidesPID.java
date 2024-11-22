package org.firstinspires.ftc.teamcode.util.ftclib.commands;
import com.arcrobotics.ftclib.command.CommandBase;
import org.firstinspires.ftc.teamcode.util.ftclib.subsystems.OuttakeSlides;

public class RunOuttakeSlidesPID extends CommandBase {
    private final OuttakeSlides outtakeSlides;

    public RunOuttakeSlidesPID(OuttakeSlides outtakeSlides) {
        this.outtakeSlides = outtakeSlides;
        addRequirements(outtakeSlides);
        outtakeSlides.resetPID();
    }

    @Override
    public void execute() {
        outtakeSlides.updatePID();
        outtakeSlides.logPos();
    }
}

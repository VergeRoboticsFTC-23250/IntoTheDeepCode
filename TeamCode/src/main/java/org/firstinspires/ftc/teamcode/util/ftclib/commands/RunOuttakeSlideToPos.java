package org.firstinspires.ftc.teamcode.util.ftclib.commands;
import com.arcrobotics.ftclib.command.CommandBase;
import org.firstinspires.ftc.teamcode.util.ftclib.subsystems.OuttakeSlides;
import org.opencv.core.Mat;

public class RunOuttakeSlideToPos extends CommandBase {
    private final OuttakeSlides outtakeSlides;
    private final int pos;

    public RunOuttakeSlideToPos(OuttakeSlides outtakeSlides, int pos) {
        this.pos = pos;
        this.outtakeSlides = outtakeSlides;
        addRequirements(outtakeSlides);
        outtakeSlides.resetPID();
        OuttakeSlides.setTargetPos(pos);
    }

    @Override
    public void execute() {
        outtakeSlides.updatePID();
        outtakeSlides.logPos();
    }

    @Override
    public boolean isFinished() {
        return Math.abs(OuttakeSlides.getPos() - pos) < 50;
    }

    @Override
    public void end(boolean interrupted) {
        outtakeSlides.setPower(0);
    }
}

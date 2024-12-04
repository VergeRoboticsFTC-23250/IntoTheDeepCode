package org.firstinspires.ftc.teamcode.util.ftclib.commands;

import com.arcrobotics.ftclib.command.CommandBase;
import org.firstinspires.ftc.teamcode.util.ftclib.subsystems.VerticalSlides;

public class RunVerticalSlidePID extends CommandBase {
    private final VerticalSlides slides;
    public RunVerticalSlidePID(VerticalSlides slides){
        this.slides = slides;
        addRequirements(slides);
        slides.resetPID();
    }

    @Override
    public void execute(){
        slides.updatePID();
    }

    @Override
    public boolean isFinished(){
        return false;
    }
}

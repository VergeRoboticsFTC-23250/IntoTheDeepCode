package org.firstinspires.ftc.teamcode.util.ftclib.commands;

import com.arcrobotics.ftclib.command.CommandBase;

import org.firstinspires.ftc.teamcode.util.ftclib.subsystems.HorizontalSlides;
import org.firstinspires.ftc.teamcode.util.ftclib.subsystems.VerticalSlides;

public class HomeVerticalSlides extends CommandBase {
    VerticalSlides slides;
    public HomeVerticalSlides(VerticalSlides slides){
        this.slides = slides;
        addRequirements(slides);
    }

    @Override
    public void execute(){
        slides.setPower(VerticalSlides.homePower);

        if(slides.isHomedR()){
            slides.setPowerR(0);
        }

        if(slides.isHomedL()){
            slides.setPowerL(0);
        }
    }

    @Override
    public boolean isFinished(){
        return slides.isHomed();
    }

    @Override
    public void end(boolean interrupted) {
        slides.resetEncoders();
        slides.setTargetPos(0);
        slides.stop();
    }
}

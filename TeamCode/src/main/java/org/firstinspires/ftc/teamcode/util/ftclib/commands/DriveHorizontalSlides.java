package org.firstinspires.ftc.teamcode.util.ftclib.commands;

import com.acmerobotics.dashboard.config.Config;
import com.arcrobotics.ftclib.command.CommandBase;
import com.arcrobotics.ftclib.gamepad.GamepadEx;
import com.arcrobotics.ftclib.gamepad.GamepadKeys;

import org.firstinspires.ftc.teamcode.util.ftclib.subsystems.HorizontalSlides;

@Config
public class DriveHorizontalSlides extends CommandBase {
    HorizontalSlides slides;
    GamepadEx gp;
    long lastTime = 0;
    long deltaTime = 0;
    public static double speed = 0.001;
    public DriveHorizontalSlides(HorizontalSlides slides, GamepadEx gp){
        this.slides = slides;
        this.gp = gp;
        addRequirements(slides);
    }

    @Override
    public void initialize() {
        slides.setPos(0);
        lastTime = System.currentTimeMillis();
    }

    @Override
    public void execute(){
        deltaTime = System.currentTimeMillis() - lastTime;
        lastTime = System.currentTimeMillis();

        slides.logPos();

        slides.addPos((gp.getTrigger(GamepadKeys.Trigger.RIGHT_TRIGGER) - gp.getTrigger(GamepadKeys.Trigger.LEFT_TRIGGER)) * deltaTime * speed);
    }

    @Override
    public boolean isFinished(){
        return false;
    }


}

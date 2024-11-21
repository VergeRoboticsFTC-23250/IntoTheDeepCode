package org.firstinspires.ftc.teamcode;

import com.arcrobotics.ftclib.command.CommandOpMode;

import org.firstinspires.ftc.teamcode.util.ftclib.commands.RunIntakeSlidePID;
import org.firstinspires.ftc.teamcode.util.ftclib.subsystems.IntakeSlides;

@com.qualcomm.robotcore.eventloop.opmode.TeleOp
public class TeleOp extends CommandOpMode {
    private IntakeSlides intakeSlides;
    @Override
    public void initialize() {
        intakeSlides = new IntakeSlides(hardwareMap, telemetry);
        intakeSlides.setDefaultCommand(new RunIntakeSlidePID(intakeSlides));
    }
}

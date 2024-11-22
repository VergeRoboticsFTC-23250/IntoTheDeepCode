package org.firstinspires.ftc.teamcode;

import com.acmerobotics.dashboard.config.Config;
import com.arcrobotics.ftclib.command.CommandOpMode;
import com.arcrobotics.ftclib.gamepad.GamepadEx;
import com.arcrobotics.ftclib.gamepad.GamepadKeys;

import org.firstinspires.ftc.teamcode.util.ftclib.commands.RunIntakeSlidePID;
import org.firstinspires.ftc.teamcode.util.ftclib.subsystems.IntakeSlides;

@Config
@com.qualcomm.robotcore.eventloop.opmode.TeleOp
public class TeleOp extends CommandOpMode {
    public static int target;
    private IntakeSlides intakeSlides;
    @Override
    public void initialize() {
        GamepadEx driverOp = new GamepadEx(gamepad1);
        GamepadEx toolOp = new GamepadEx(gamepad2);

        intakeSlides = new IntakeSlides(hardwareMap, telemetry);
        intakeSlides.setDefaultCommand(new RunIntakeSlidePID(intakeSlides));
    }
}

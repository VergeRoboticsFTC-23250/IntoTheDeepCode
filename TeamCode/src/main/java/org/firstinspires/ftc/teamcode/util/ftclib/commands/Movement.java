package org.firstinspires.ftc.teamcode.util.ftclib.commands;

import com.arcrobotics.ftclib.command.CommandBase;
import com.arcrobotics.ftclib.gamepad.GamepadEx;

import org.firstinspires.ftc.teamcode.util.ftclib.subsystems.Chassis;

public class Movement extends CommandBase {

    Chassis chassis;
    GamepadEx gamepad;

    public Movement(Chassis chassis, GamepadEx gamepad) {
        this.chassis = chassis;
        this.gamepad = gamepad;
        addRequirements(chassis);
    }

    @Override
    public void execute() {
        chassis.drive(gamepad.getLeftY(), gamepad.getLeftX(), gamepad.getRightX());
    }
}

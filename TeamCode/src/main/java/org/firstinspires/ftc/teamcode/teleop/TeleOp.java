package org.firstinspires.ftc.teamcode.teleop;

import com.acmerobotics.dashboard.config.Config;
import com.arcrobotics.ftclib.command.CommandOpMode;
import com.arcrobotics.ftclib.gamepad.GamepadEx;
import com.arcrobotics.ftclib.gamepad.GamepadKeys;

import org.firstinspires.ftc.teamcode.util.ftclib.commands.HomeIntakeSlides;
import org.firstinspires.ftc.teamcode.util.ftclib.commands.HomeOuttakeSlides;
import org.firstinspires.ftc.teamcode.util.ftclib.commands.RunIntakeSlidesPID;
import org.firstinspires.ftc.teamcode.util.ftclib.commands.RunOuttakeSlidesPID;
import org.firstinspires.ftc.teamcode.util.ftclib.subsystems.Intake;
import org.firstinspires.ftc.teamcode.util.ftclib.subsystems.IntakeSlides;
import org.firstinspires.ftc.teamcode.util.ftclib.subsystems.OuttakeSlides;

@Config
@com.qualcomm.robotcore.eventloop.opmode.TeleOp
public class TeleOp extends CommandOpMode {
    public static int t1 = 300;
    public static int t2= 600;
    private IntakeSlides intakeSlides;
    private OuttakeSlides outtakeSlides;
    private Intake intake;
    @Override
    public void initialize() {
        GamepadEx driverOp = new GamepadEx(gamepad1);
        GamepadEx toolOp = new GamepadEx(gamepad2);

        intakeSlides = new IntakeSlides(hardwareMap, telemetry);
        intakeSlides.setDefaultCommand(new RunIntakeSlidesPID(intakeSlides));
        intakeSlides.resetEncoder();

        outtakeSlides = new OuttakeSlides(hardwareMap, telemetry);
        outtakeSlides.setDefaultCommand(new RunOuttakeSlidesPID(outtakeSlides));
        outtakeSlides.resetEncoder();

        intake = new Intake(hardwareMap);

        toolOp.getGamepadButton(GamepadKeys.Button.X).whenPressed(() -> intakeSlides.setTargetPos(t1)); // square
        toolOp.getGamepadButton(GamepadKeys.Button.A).whenPressed(new HomeIntakeSlides(intakeSlides));  // X
        driverOp.getGamepadButton(GamepadKeys.Button.X).whenPressed(() -> outtakeSlides.setTargetPos(t2));
        driverOp.getGamepadButton(GamepadKeys.Button.A).whenPressed(new HomeOuttakeSlides(outtakeSlides));
    }
}

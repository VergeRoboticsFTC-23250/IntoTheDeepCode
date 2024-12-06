package org.firstinspires.ftc.teamcode.teleop;

import static org.firstinspires.ftc.teamcode.util.Robot.State.*;

import com.arcrobotics.ftclib.command.CommandOpMode;
import com.arcrobotics.ftclib.command.InstantCommand;
import com.arcrobotics.ftclib.command.SequentialCommandGroup;
import com.arcrobotics.ftclib.gamepad.GamepadEx;
import com.arcrobotics.ftclib.gamepad.GamepadKeys;

import org.firstinspires.ftc.teamcode.util.Robot;
import org.firstinspires.ftc.teamcode.util.ftclib.commands.DriveHorizontalSlides;
import org.firstinspires.ftc.teamcode.util.ftclib.commands.HomeVerticalSlides;
import org.firstinspires.ftc.teamcode.util.ftclib.commands.Movement;
import org.firstinspires.ftc.teamcode.util.ftclib.commands.RunVerticalSlidePID;
import org.firstinspires.ftc.teamcode.util.ftclib.commands.SetPositions;
import org.firstinspires.ftc.teamcode.util.ftclib.subsystems.Chassis;
import org.firstinspires.ftc.teamcode.util.ftclib.subsystems.HorizontalSlides;
import org.firstinspires.ftc.teamcode.util.ftclib.subsystems.Intake;
import org.firstinspires.ftc.teamcode.util.ftclib.subsystems.Outtake;
import org.firstinspires.ftc.teamcode.util.ftclib.subsystems.VerticalSlides;

@com.qualcomm.robotcore.eventloop.opmode.TeleOp(name="TeleOp")
public class teleop extends CommandOpMode {
    private Chassis chassis;
    private Intake intake;
    private HorizontalSlides hSlides;
    private VerticalSlides vSlides;
    private Outtake outtake;

    @Override
    public void initialize() {
        GamepadEx tj = new GamepadEx(gamepad1);
        GamepadEx arvind = new GamepadEx(gamepad2);

        //Initialize subsystems.
        chassis = new Chassis(hardwareMap);
        intake = new Intake(hardwareMap);
        hSlides = new HorizontalSlides(hardwareMap);
        vSlides = new VerticalSlides(hardwareMap);
        outtake = new Outtake(hardwareMap);

        //Set default commands.
        chassis.setDefaultCommand(new Movement(chassis, tj));
        vSlides.setDefaultCommand(new RunVerticalSlidePID(vSlides));
        hSlides.setDefaultCommand(new DriveHorizontalSlides(hSlides, arvind));

        //Schedule initial commands.
        schedule(new SequentialCommandGroup(
                new SetPositions(outtake, HOME, telemetry),
                new HomeVerticalSlides(vSlides)
        ));

        //Chassis Bindings
        tj.getGamepadButton(GamepadKeys.Button.LEFT_BUMPER)
                .whenPressed(chassis::setSpeedSlow)
                .whenReleased(chassis::setSpeedFast);

        //Intake Bindings
        arvind.getGamepadButton(GamepadKeys.Button.A)
                .whenPressed(intake::drop)
                .whenReleased(intake::home);

        arvind.getGamepadButton(GamepadKeys.Button.RIGHT_BUMPER)
                .whenPressed(() -> intake.spin(-1))
                .whenReleased(() -> intake.spin(0));

        arvind.getGamepadButton(GamepadKeys.Button.LEFT_BUMPER)
                .whenPressed(() -> intake.spin(0.5))
                .whenReleased(() -> intake.spin(0));

        //Vertical Slide Bindings
        tj.getGamepadButton(GamepadKeys.Button.DPAD_UP)
                .whileActiveContinuous(new InstantCommand(() -> {
                    vSlides.setPowerSafe(1);
                }, vSlides))
                .whenInactive(new InstantCommand(() -> {
                    vSlides.stop();
                    vSlides.setTargetPosL(vSlides.getPosL());
                    vSlides.setTargetPosR(vSlides.getPosR());
                }, vSlides));

        tj.getGamepadButton(GamepadKeys.Button.DPAD_DOWN)
                .whileActiveContinuous(new InstantCommand(() -> {
                    vSlides.setPowerSafe(-1);
                }, vSlides))
                .whenInactive(new InstantCommand(() -> {
                    vSlides.stop();
                    vSlides.setTargetPosL(vSlides.getPosL());
                    vSlides.setTargetPosR(vSlides.getPosR());
                }, vSlides));

        //Outtake Bindings
        tj.getGamepadButton(GamepadKeys.Button.Y) //triangle
                .whenPressed(Robot.GoToState(outtake, OUTTAKE_BUCKET, telemetry));

        tj.getGamepadButton(GamepadKeys.Button.X) //square
                .whenPressed(Robot.GoToState(outtake, OUTTAKE_SUBMERSIBLE, telemetry));

        tj.getGamepadButton(GamepadKeys.Button.B) //circle
                .whenPressed(Robot.GoToState(outtake, INIT, telemetry));

        tj.getGamepadButton(GamepadKeys.Button.A) //X
                .whenPressed(Robot.GoToState(outtake, HOME, telemetry));

        tj.getGamepadButton(GamepadKeys.Button.LEFT_BUMPER)
                .whenPressed(outtake::toggleClaw);

        tj.getGamepadButton(GamepadKeys.Button.RIGHT_BUMPER)
                .whenPressed(new SetPositions(outtake, TRANSITION, telemetry));
    }
}

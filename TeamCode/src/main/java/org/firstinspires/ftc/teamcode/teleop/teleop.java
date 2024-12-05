package org.firstinspires.ftc.teamcode.teleop;

import com.arcrobotics.ftclib.command.CommandOpMode;
import com.arcrobotics.ftclib.command.InstantCommand;
import com.arcrobotics.ftclib.command.SequentialCommandGroup;
import com.arcrobotics.ftclib.command.button.Trigger;
import com.arcrobotics.ftclib.gamepad.GamepadEx;
import com.arcrobotics.ftclib.gamepad.GamepadKeys;

import org.firstinspires.ftc.teamcode.util.Robot;
import org.firstinspires.ftc.teamcode.util.ftclib.commands.HomeVerticalSlides;
import org.firstinspires.ftc.teamcode.util.ftclib.commands.Movement;
import org.firstinspires.ftc.teamcode.util.ftclib.commands.RunVerticalSlidePID;
import org.firstinspires.ftc.teamcode.util.ftclib.commands.SetRobotState;
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

        //Chassis Code
        chassis = new Chassis(hardwareMap);
        chassis.setDefaultCommand(new Movement(chassis, tj));

        tj.getGamepadButton(GamepadKeys.Button.LEFT_BUMPER)
                .whenPressed(chassis::setSpeedSlow)
                .whenReleased(chassis::setSpeedFast);

        //Intake Code
        intake = new Intake(hardwareMap);
        arvind.getGamepadButton(GamepadKeys.Button.A)
                .whenPressed(intake::drop)
                .whenReleased(intake::home);

        arvind.getGamepadButton(GamepadKeys.Button.RIGHT_BUMPER)
                .whenPressed(() -> intake.spin(-1))
                .whenReleased(() -> intake.spin(0));

        arvind.getGamepadButton(GamepadKeys.Button.LEFT_BUMPER)
                .whenPressed(() -> intake.spin(0.5))
                .whenReleased(() -> intake.spin(0));

        //Horizontal Slide Code
        hSlides = new HorizontalSlides(hardwareMap, telemetry);

        new Trigger(() -> arvind.getTrigger(GamepadKeys.Trigger.RIGHT_TRIGGER) > 0 || arvind.getTrigger(GamepadKeys.Trigger.LEFT_TRIGGER) > 0)
                .whileActiveContinuous(() -> {
                    hSlides.setIncrement(arvind.getTrigger(GamepadKeys.Trigger.RIGHT_TRIGGER), arvind.getTrigger(GamepadKeys.Trigger.LEFT_TRIGGER));
                });

        //Vertical Slide Code
        vSlides = new VerticalSlides(hardwareMap);
        vSlides.setDefaultCommand(new RunVerticalSlidePID(vSlides));

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

        //Outtake Code
        outtake = new Outtake(hardwareMap);

        tj.getGamepadButton(GamepadKeys.Button.Y) //triangle
                .whenPressed(new SetRobotState(vSlides, outtake, Robot.outtakeBucket));

        tj.getGamepadButton(GamepadKeys.Button.X) //square
                .whenPressed(new SetRobotState(vSlides, outtake, Robot.outtakeSubmersible));

        tj.getGamepadButton(GamepadKeys.Button.B) //circle
                .whenPressed(new SetRobotState(vSlides, outtake, Robot.intake));

        tj.getGamepadButton(GamepadKeys.Button.A) //X
                .whenPressed(new SetRobotState(vSlides, outtake, Robot.home));

        tj.getGamepadButton(GamepadKeys.Button.LEFT_BUMPER)
                        .toggleWhenPressed(outtake::openClaw, outtake::closeClaw);

        tj.getGamepadButton(GamepadKeys.Button.RIGHT_BUMPER)
                .whenPressed(() -> outtake.setPivot(Robot.handoff.pivotPos));

        schedule(new SequentialCommandGroup(
                new SetRobotState(vSlides, outtake, Robot.home),
                new HomeVerticalSlides(vSlides)
        ));
    }
}

package org.firstinspires.ftc.teamcode.teleop;

import com.acmerobotics.dashboard.config.Config;
import com.arcrobotics.ftclib.command.CommandOpMode;
import com.arcrobotics.ftclib.command.InstantCommand;
import com.arcrobotics.ftclib.command.button.GamepadButton;
import com.arcrobotics.ftclib.command.button.Trigger;
import com.arcrobotics.ftclib.gamepad.GamepadEx;
import com.arcrobotics.ftclib.gamepad.GamepadKeys;

import org.firstinspires.ftc.teamcode.util.ftclib.commands.HomeOuttakeSlides;
import org.firstinspires.ftc.teamcode.util.ftclib.commands.Movement;
import org.firstinspires.ftc.teamcode.util.ftclib.commands.RunIntakeSlidesPID;
import org.firstinspires.ftc.teamcode.util.ftclib.commands.RunOuttakeSlidesPID;
import org.firstinspires.ftc.teamcode.util.ftclib.commands.outtake.PresetSubmirsible;
import org.firstinspires.ftc.teamcode.util.ftclib.subsystems.Chassis;
import org.firstinspires.ftc.teamcode.util.ftclib.subsystems.Intake;
import org.firstinspires.ftc.teamcode.util.ftclib.subsystems.IntakeSlides;
import org.firstinspires.ftc.teamcode.util.ftclib.subsystems.Outtake;
import org.firstinspires.ftc.teamcode.util.ftclib.subsystems.OuttakeSlides;

@Config
@com.qualcomm.robotcore.eventloop.opmode.TeleOp
public class TeleOp extends CommandOpMode {
    private Chassis chassis;
    private IntakeSlides intakeSlides;
    private OuttakeSlides outtakeSlides;
    private Intake intake;
    private Outtake outtake;

    @Override
    public void initialize() {
        GamepadEx driverOp = new GamepadEx(gamepad1); // tejas
        GamepadEx toolOp = new GamepadEx(gamepad2); // arvind

        chassis = new Chassis(hardwareMap);
        chassis.setDefaultCommand(new Movement(chassis, driverOp));

        intakeSlides = new IntakeSlides(hardwareMap, telemetry);
        intakeSlides.setDefaultCommand(new RunIntakeSlidesPID(intakeSlides));
        intakeSlides.resetEncoder();

        outtakeSlides = new OuttakeSlides(hardwareMap, telemetry);
        outtakeSlides.setDefaultCommand(new RunOuttakeSlidesPID(outtakeSlides));
        outtakeSlides.resetEncoder();

        intake = new Intake(hardwareMap);

        outtake = new Outtake(hardwareMap);

        toolOp.getGamepadButton(GamepadKeys.Button.RIGHT_BUMPER)
                .whenPressed(() -> intake.spin(1))
                .whenReleased(intake::stop);
        toolOp.getGamepadButton(GamepadKeys.Button.LEFT_BUMPER)
                .whenPressed(() -> intake.spin(-1))
                .whenReleased(intake::stop);
        toolOp.getGamepadButton(GamepadKeys.Button.A)
                .toggleWhenActive(new InstantCommand(intake::drop), new InstantCommand(intake::raise));
        new GamepadButton(toolOp, GamepadKeys.Button.DPAD_UP)
                .and(new GamepadButton(toolOp, GamepadKeys.Button.RIGHT_BUMPER))
                .whenActive(() -> intake.spin(0.5));
        new Trigger(() -> toolOp.getTrigger(GamepadKeys.Trigger.RIGHT_TRIGGER) > 0)
                .whileActiveContinuous(new InstantCommand(() -> {
                    if(intakeSlides.getPos() < IntakeSlides.maxPos){
                        intakeSlides.setPower(toolOp.getTrigger(GamepadKeys.Trigger.RIGHT_TRIGGER));
                    }
                    else{
                        intakeSlides.setTargetPos(IntakeSlides.maxPos);
                        intakeSlides.updatePID();
                    }
                }, intakeSlides))
                .whenInactive(new InstantCommand(() -> intakeSlides.setTargetPos(
                        Math.min(intakeSlides.getPos(), IntakeSlides.maxPos)
                ),  intakeSlides));
        new Trigger(() -> toolOp.getTrigger(GamepadKeys.Trigger.LEFT_TRIGGER) > 0)
                .whileActiveContinuous(new InstantCommand(() -> intakeSlides.setPower(
                        -((intakeSlides.getPos() > IntakeSlides.minPos)? toolOp.getTrigger(GamepadKeys.Trigger.LEFT_TRIGGER) : 0)
                ), intakeSlides))
                .whenInactive(new InstantCommand(() -> intakeSlides.setTargetPos(intakeSlides.getPos()),  intakeSlides));

        //driverop

        driverOp.getGamepadButton(GamepadKeys.Button.RIGHT_BUMPER)
                .whenPressed(chassis::speedSlow)
                .whenReleased(chassis::speedFast);

        driverOp.getGamepadButton(GamepadKeys.Button.A)
                .toggleWhenActive(outtake::closeClaw, outtake::openClaw);

        driverOp.getGamepadButton(GamepadKeys.Button.B)
                .whenPressed(() -> {
                    outtake.setArm(Outtake.armSubmirsiblePos);
                    outtake.setPivot(Outtake.pivotSubmersiblePos);
                });

        driverOp.getGamepadButton(GamepadKeys.Button.X)
                .whenPressed(() -> {
                    outtake.setArm(Outtake.armBucketPos);
                    outtake.setPivot(Outtake.pivotBucketPos);
                });

        driverOp.getGamepadButton(GamepadKeys.Button.Y)
                .whenPressed(() -> {
                    outtake.setArm(Outtake.armHomePos);
                    outtake.setPivot(Outtake.pivotHomePos);
                });

        driverOp.getGamepadButton(GamepadKeys.Button.DPAD_UP)
                .whileActiveContinuous(new InstantCommand(() -> {
                    if(OuttakeSlides.getPos() < OuttakeSlides.maxPos){
                        outtakeSlides.setPower(Chassis.isSlowed? Chassis.slowSpeed : 1);
                    }
                    else{
                        OuttakeSlides.setTargetPos(OuttakeSlides.maxPos);
                        outtakeSlides.updatePID();
                    }
                }, outtakeSlides))
                .whenInactive(new InstantCommand(() -> OuttakeSlides.setTargetPos(
                        Math.min(OuttakeSlides.getPos(), OuttakeSlides.maxPos)
                ),  outtakeSlides));

        driverOp.getGamepadButton(GamepadKeys.Button.DPAD_DOWN)
                .whileActiveContinuous(new InstantCommand(() -> outtakeSlides.setPower(
                        (OuttakeSlides.getPos() > OuttakeSlides.minPos)? (Chassis.isSlowed? -Chassis.slowSpeed : -0.75) : 0
                ), outtakeSlides))
                .whenInactive(new InstantCommand(() -> OuttakeSlides.setTargetPos(OuttakeSlides.getPos()),  outtakeSlides));


    }
}

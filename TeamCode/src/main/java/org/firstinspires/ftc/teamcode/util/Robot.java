package org.firstinspires.ftc.teamcode.util;

import com.acmerobotics.dashboard.config.Config;
import com.arcrobotics.ftclib.command.Command;
import com.arcrobotics.ftclib.command.InstantCommand;
import com.arcrobotics.ftclib.command.SequentialCommandGroup;
import com.arcrobotics.ftclib.command.WaitCommand;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.util.ftclib.commands.SetPositions;
import org.firstinspires.ftc.teamcode.util.ftclib.subsystems.Outtake;
import org.firstinspires.ftc.teamcode.util.ftclib.subsystems.VerticalSlides;

import java.util.Map;

@Config
public class Robot {
    public static volatile State currentState = State.INIT;
    public enum State {
        INIT,
        HOME,
        INTAKE,
        OUTTAKE_SUBMERSIBLE,
        OUTTAKE_SUBMERSIBLE_SCORE,
        OUTTAKE_BUCKET,
        TRANSITION
    }

    public static StatePositions init = new StatePositions(0.35, 0.95, true, 0, State.INIT);
    public static StatePositions home = new StatePositions(0.25, 0.7, true, 0, State.HOME);
    public static StatePositions intake = new StatePositions(.75, 0.75, true, 0, State.INTAKE);
    public static StatePositions outtakeSubmersible = new StatePositions(0.05, 0.75, false, 1400, State.OUTTAKE_SUBMERSIBLE);
    public static StatePositions outtakeSubmersibleScore = new StatePositions(0.05, 0.75, false, 1400, State.OUTTAKE_SUBMERSIBLE_SCORE);
    public static StatePositions outtakeBucket = new StatePositions(1, .75, false, VerticalSlides.maxPos, State.OUTTAKE_BUCKET);
    public static StatePositions transition = new StatePositions(0.35, 0.95, false, 1000, State.TRANSITION);

    public static long waitForClawClose = 1000;
    public static long waitForTransition = 1000;

    public static Map<State, StatePositions> states = Map.of(
            State.INIT, init,
            State.HOME, home,
            State.INTAKE, intake,
            State.OUTTAKE_SUBMERSIBLE, outtakeSubmersible,
            State.OUTTAKE_SUBMERSIBLE_SCORE, outtakeSubmersibleScore,
            State.OUTTAKE_BUCKET, outtakeBucket,
            State.TRANSITION, transition
    );

    public static Command GoToState(Outtake outtake, State desiredState, Telemetry telemetry){
        telemetry.addData("State", currentState);
        telemetry.update();
        if(currentState != State.HOME){
            return new SetPositions(outtake, desiredState, telemetry);
        }else{
            return new SequentialCommandGroup(
                    new InstantCommand(outtake::closeClaw, outtake),
                    new WaitCommand(waitForClawClose),
                    new SetPositions(outtake, State.TRANSITION, telemetry),
                    new InstantCommand(() -> Robot.currentState = State.TRANSITION),
                    new WaitCommand(waitForTransition),
                    new InstantCommand(() -> Robot.currentState = desiredState),
                    new SetPositions(outtake, desiredState, telemetry)
            ).andThen(new InstantCommand(() -> Robot.currentState = desiredState));
        }
    }
}

package org.firstinspires.ftc.teamcode.util;

import com.acmerobotics.dashboard.config.Config;

import org.firstinspires.ftc.teamcode.util.ftclib.subsystems.VerticalSlides;

import java.util.Map;

@Config
public class Robot {
    public static State currentState = State.INIT;
    public enum State {
        INIT,
        HOME,
        INTAKE,
        OUTTAKE_SUBMERSIBLE,
        OUTTAKE_SUBMERSIBLE_SCORE,
        OUTTAKE_BUCKET,
        TRANSITION
    }
    public static class StatePositions {
        public StatePositions(double armPos, double pivotPos, boolean isClawOpen, int horizontalSlidePos, State state){
            this.armPos = armPos;
            this.pivotPos = pivotPos;
            this.isClawOpen = isClawOpen;
            this.horizontalSlidePos = horizontalSlidePos;
            this.state = state;
        }

        public StatePositions(){}

        public State state = State.INIT;
        public double armPos = 0.25;
        public double pivotPos = 0.7;
        public boolean isClawOpen = true;
        public int horizontalSlidePos = 0;
    }

    public static StatePositions init = new StatePositions(0.35, 0.95, true, 0, State.INIT);
    public static StatePositions home = new StatePositions(0.25, 0.7, true, 0, State.HOME);
    public static StatePositions intake = new StatePositions(.75, 0.75, true, 0, State.INTAKE);
    public static StatePositions outtakeSubmersible = new StatePositions(0.05, 0.75, false, 1400, State.OUTTAKE_SUBMERSIBLE);
    public static StatePositions outtakeSubmersibleScore = new StatePositions(0.05, 0.75, false, 1400, State.OUTTAKE_SUBMERSIBLE_SCORE);
    public static StatePositions outtakeBucket = new StatePositions(1, .75, false, VerticalSlides.maxPos, State.OUTTAKE_BUCKET);
    public static StatePositions transition = new StatePositions(0.35, 0.95, false, 1000, State.TRANSITION);

    public static Map<State, StatePositions> states = Map.of(
            State.INIT, init,
            State.HOME, home,
            State.INTAKE, intake,
            State.OUTTAKE_SUBMERSIBLE, outtakeSubmersible,
            State.OUTTAKE_SUBMERSIBLE_SCORE, outtakeSubmersibleScore,
            State.OUTTAKE_BUCKET, outtakeBucket,
            State.TRANSITION, transition
    );
}

package org.firstinspires.ftc.teamcode.util;

public class StatePositions {
    public StatePositions() {
    }

    public Robot.State state;
    public double armPos;
    public double pivotPos;
    public boolean isClawOpen;
    public int horizontalSlidePos;

    public StatePositions(double armPos, double pivotPos, boolean isClawOpen, int horizontalSlidePos, Robot.State state) {
        this.armPos = armPos;
        this.pivotPos = pivotPos;
        this.isClawOpen = isClawOpen;
        this.horizontalSlidePos = horizontalSlidePos;
        this.state = state;
    }
}

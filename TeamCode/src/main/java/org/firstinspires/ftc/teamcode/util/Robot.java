package org.firstinspires.ftc.teamcode.util;

import com.acmerobotics.dashboard.config.Config;

@Config
public class Robot {
    public static class RobotState{
        public RobotState(double armPos, double pivotPos, boolean isClawOpen, int horizontalSlidePos){
            this.armPos = armPos;
            this.pivotPos = pivotPos;
            this.isClawOpen = isClawOpen;
            this.horizontalSlidePos = horizontalSlidePos;
        }

        public RobotState(){}
        public double armPos = 0.3;
        public double pivotPos = 0.5;
        public boolean isClawOpen = true;
        public int horizontalSlidePos = 0;
    }

    public static RobotState home = new RobotState();
    public static RobotState intake = new RobotState(0, 0, true, 0);
    public static RobotState outtakeSubmersible = new RobotState(0, 0, false, 1500);
    public static RobotState outtakeSubmersibleScore = new RobotState(0, 0, false, 0);
    public static RobotState outtakeBucket = new RobotState(0, 0, false, 0);

    public static int horizontalSlidesSafePos = 500;
}

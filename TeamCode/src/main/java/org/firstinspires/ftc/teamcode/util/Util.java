package org.firstinspires.ftc.teamcode.util;

import com.pedropathing.localization.Pose;

public class Util {
    public static double shortestAngleDistance(double theta1, double theta2) {
        return Math.atan2(Math.sin(theta2 - theta1), Math.cos(theta2 - theta1));
    }

    public static double transformExponential(double val, double pow){
        return Math.pow(Math.abs(val), pow) * Math.signum(val);
    }

    public static class Scale{
        double min;
        double max;
        double range;

        public Scale(double min, double max){
            this.min = min;
            this.max = max;
            this.range = max-min;
        }

        public double scale(double value) {
            return min + value * (max - min);
        }
    }

    public static Pose extrapolateLookaheadPoint(Pose currentPose, Pose targetPose){
        double errorX = targetPose.getX() - currentPose.getX();
        double errorY = targetPose.getY() - currentPose.getY();

        double angle = Math.atan2(errorY, errorX);

        double x = 9999 * Math.cos(angle) + currentPose.getX();
        double y = 9999 * Math.sin(angle) + currentPose.getY();

        return new Pose(x, y, targetPose.getHeading());
    }

    public static int clamp(int value, int min, int max) {
        return Math.max(min, Math.min(value, max));
    }

    public static double findSlope(Pose pose1, Pose pose2){
        double deltaY= pose2.getY() - pose1.getY();
        double deltaX = pose2.getX() - pose1.getX();
        return (deltaY)/(deltaX);
    }

    public static class StatePositions {
        public boolean isIntakeClawOpen;
        public double intakeDropDown;
        public boolean areIntakeSlidesExtended;
        public double outtakeArm;
        public boolean isOuttakeClawOpen;
        public double outtakePivot;
        public int outtakeSlides;
        public double intakePivot;
        public double intakeWrist;


        public StatePositions(int outtakeSlides, double outtakeArm, double outtakePivot, double intakeDropDown, double intakePivot, double intakeWrist, boolean isIntakeClawOpen, boolean isOuttakeClawOpen, boolean areIntakeSlidesExtended) {
            this.isIntakeClawOpen = isIntakeClawOpen;
            this.intakeDropDown = intakeDropDown;
            this.areIntakeSlidesExtended = areIntakeSlidesExtended;
            this.outtakeArm = outtakeArm;
            this.isOuttakeClawOpen = isOuttakeClawOpen;
            this.outtakePivot = outtakePivot;
            this.outtakeSlides = outtakeSlides;
            this.intakePivot = intakePivot;
            this.intakeWrist = intakeWrist;
        }
    }
}

package org.firstinspires.ftc.teamcode.auto.assets;

import com.pedropathing.localization.Pose;

public class SpecAutoParams {
    public static Pose outtakePose = new Pose(40, 70, 0);
    public static double[] outtakeOffsetsY = {0, 0, 0, 0, 0};
    public static double[] outtakeOffsetsX = {0, 0, 0, 0, 0};
    public static double outtakePushPower = 1;
    public static double preOuttakeDelay = 0;
    public static double postOuttakeDelay = 0;

    public static Pose intakePose = new Pose(8, 36, 0);
    public static double[] intakeOffsetsY = {0, 0, 0, 0, 0};
    public static double[] intakeOffsetsX = {0, 0, 0, 0, 0};
    public static double intakePushPower = 1;
    public static double preIntakeDelay = 0.25;
    public static double postIntakeDelay = 0.25;

    public static Pose bucketPose = new Pose(0, 0, 0);
    public static Pose parkPose = new Pose(0, 0, 0);

    public static Pose[] pushSampPoses = new Pose[]{
            new Pose(0, 0, 0)
    };
}

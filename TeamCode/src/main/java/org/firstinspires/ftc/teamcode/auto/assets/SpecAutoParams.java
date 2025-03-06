package org.firstinspires.ftc.teamcode.auto.assets;

import com.pedropathing.localization.Pose;

public class SpecAutoParams {
    public static double slideExtendDelay = 0.5;
    public static Pose outtakePose = new Pose(40, 70, 0);
    public static double[] outtakeOffsetsY = {8, 6, 4, 2, 0};
    public static double[] outtakeOffsetsX = {0, 0, 0, 0, 0};
    public static double outtakePushPower = 1;
    public static double preOuttakeDelay = 0;
    public static double postOuttakeDelay = 0;

    public static Pose intakePose = new Pose(6, 34, 0);
    public static double[] intakeOffsetsY = {0, 0, 0, 0, 0};
    public static double[] intakeOffsetsX = {0, 0.5, 1, 1.5, 2};
    public static double intakeHelperOffset = 8;
    public static double intakePushPower = 1;
    public static double preIntakeDelay = 0;
    public static double postIntakeDelay = 0;

    public static Pose bucketPose = new Pose(16, 110, Math.toRadians(90));
    public static Pose parkPose = new Pose(0, 0, 0);
    static double pushTo = 18;
    static double upTo = 60;
    static double distBetween = 12;
    static double closeToSamp = 36;

    public static Pose[] pushSampPoses = new Pose[]{
            new Pose(28, closeToSamp, 0),
            new Pose(upTo, closeToSamp, 0),
            new Pose(upTo, closeToSamp -distBetween, 0),
            new Pose(pushTo, closeToSamp -distBetween, 0),
            new Pose(upTo, closeToSamp - 10, 0),
            new Pose(upTo, closeToSamp -distBetween - 10, 0),
            new Pose(pushTo, closeToSamp -distBetween - 10, 0),
            new Pose(upTo, closeToSamp - 20, 0),
            new Pose(upTo, closeToSamp -distBetween - 20, 0),
            new Pose(pushTo, closeToSamp -distBetween - 20, 0),
            new Pose(18, 34, 0)
//            new Pose(58, 30, 0),
//            new Pose(20, 22, 0)
    };
}

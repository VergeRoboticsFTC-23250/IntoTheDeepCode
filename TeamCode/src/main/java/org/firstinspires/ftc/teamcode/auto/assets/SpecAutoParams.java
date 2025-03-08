package org.firstinspires.ftc.teamcode.auto.assets;

import com.pedropathing.localization.Pose;
import com.pedropathing.pathgen.Point;

import org.firstinspires.ftc.teamcode.util.BezierCurve;

public class SpecAutoParams {
    static double releaseSpecAt = 1;
    static double[] offsets = {8, 6, 4, 2, 0};
    static double curveToWallAmount = 0.35;
    static double curveToTrussAmount = 0.35;
    public static Pose outtakePose = new Pose(39, 66, 0);
    public static Pose intakePose = new Pose(8, 38, 0);
    public static double intakePushPower = 1;
    public static double outtakePushPower = 0.5;
    public static double preIntakeDelay = 0.25;
    public static double postIntakeDelay = 0;
    public static long duringOuttakeDelay = 375;

    public static Pose bucketPose = new Pose(16, 110, Math.toRadians(90));
    public static Pose parkPose = new Pose(0, 0, 0);
    public static Pose samp1 = new Pose(21, 22.500, 0);
    public static Pose samp2 = new Pose(21, 12.500, 0);
    public static Pose samp3 = new Pose(21, 9.000, 0);

    public static double offsetCurve = 6;

    public static BezierCurve[] samplePushCurves = {
            new BezierCurve(
                    outtakePose,
                    new Pose(31.500, 66.000, 0),
                    new Pose(11.000, 12.000, 0),
                    new Pose(68.000, 49.500, 0),
                    new Pose(58.500 - offsetCurve, 23.000, 0),
                    new Pose(58.500 - offsetCurve, 23.000, 0)
            ),
            new BezierCurve(
                    samp1,
                    new Pose(30.000 - offsetCurve, 22.500, 0),
                    new Pose(62.500 - offsetCurve, 32.000, 0),
                    new Pose(58.500 - offsetCurve, 12.500, 0)
            ),
            new BezierCurve(
                    samp2,
                    new Pose(62.000 - offsetCurve, 18.500, 0),
                    new Pose(58.500 - offsetCurve, 9.000, 0)
            )
    };
}

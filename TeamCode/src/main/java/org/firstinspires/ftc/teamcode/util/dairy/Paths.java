package org.firstinspires.ftc.teamcode.util.dairy;

import com.pedropathing.follower.Follower;
import com.pedropathing.pathgen.BezierCurve;
import com.pedropathing.pathgen.BezierLine;
import com.pedropathing.pathgen.Path;
import com.pedropathing.pathgen.PathBuilder;
import com.pedropathing.pathgen.PathChain;
import com.pedropathing.pathgen.Point;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Paths {

    public static ArrayList<Path> fiveSpecs = new ArrayList<>();
    public static Path parkWithSpecPush;
    public static PathBuilder builder = new PathBuilder();
    public static PathChain robotPush;
    public static PathChain extendoPush;

    public static double intakeOffset = -2;
    public static double intakeOffset1 = 2;
    public static double intakeOffset2 = 1;
    public static double intakeOffset3 = 1;
    public static double intakeOffset4 = 2;

    public static void init() {
        Collections.addAll(fiveSpecs,
                createPath( //preload 0
                        new BezierLine(
                                new Point(9.000, 65.000, Point.CARTESIAN),
                                new Point(40.00, 67.000, Point.CARTESIAN)
                        )
                ),
                createPath( // curve to first sample 1
                        new BezierCurve(
                                new Point(40.000, 67.000, Point.CARTESIAN),
                                new Point(31.500, 66.000, Point.CARTESIAN),
                                new Point(11.000, 12.000, Point.CARTESIAN),
                                new Point(68.000, 49.500, Point.CARTESIAN),
                                new Point(58.500, 23.000, Point.CARTESIAN)
                        )
                ),
                createPath( // push first sample 2
                        new BezierLine(
                                new Point(58.500, 23.000, Point.CARTESIAN),
                                new Point(20.000, 22.500, Point.CARTESIAN)
                        )
                ),
                createPath( // curve to second sample 3
                        new BezierCurve(
                                new Point(20.000, 22.500, Point.CARTESIAN),
                                new Point(62.500, 32.000, Point.CARTESIAN),
                                new Point(58.500, 12.500, Point.CARTESIAN)
                        )
                ),
                createPath( // push second sample 4
                        new BezierLine(
                                new Point(58.500, 12.500, Point.CARTESIAN),
                                new Point(20.000, 12.500, Point.CARTESIAN)
                        )
                ),
                createPath( // curve to third sample 5
                        new BezierCurve(
                                new Point(20.000, 12.500, Point.CARTESIAN),
                                new Point(62.000, 18.500, Point.CARTESIAN),
                                new Point(58.500, 9.000, Point.CARTESIAN)
                        )
                ),
                createPath( // push third sample 6
                        new BezierLine(
                                new Point(58.500, 9.000, Point.CARTESIAN),
                                new Point(20.000, 9.000, Point.CARTESIAN)
                        )
                ),
                createPath( // curve to plus 1 intake 7
                        new BezierCurve(
                                new Point(20.000, 9.000, Point.CARTESIAN),
                                new Point(27.000, 23.000, Point.CARTESIAN),
                                new Point( 25, 34.5, Point.CARTESIAN),
                                new Point(10.500 + intakeOffset + intakeOffset1, 33.000+3.5, Point.CARTESIAN)
                        )
                ),
                createPath( // plus 1 outtake 8
                        new BezierLine(
                                new Point(10.500, 33.000+2.5, Point.CARTESIAN),
                                new Point(40.000, 74.000, Point.CARTESIAN)
                        )
                ),
                createPath( // plus 1 outtake push 9
                        new BezierLine(
                                new Point(40.000, 74.000, Point.CARTESIAN),
                                new Point(40, 67, Point.CARTESIAN)
                        )
                ),
                createPath( // curve to plus 2 intake 10
                        new BezierCurve(
                                new Point(40.000, 67.000, Point.CARTESIAN),
                                new Point(24.655, 54.670, Point.CARTESIAN),
                                new Point(30.194, 26.084, Point.CARTESIAN),
                                new Point(10.500 + intakeOffset + intakeOffset2, 33.000+1.5, Point.CARTESIAN)
                        )
                ),
                createPath( // plus 2 outtake 11
                        new BezierLine(
                                new Point(10.500, 33.000, Point.CARTESIAN),
                                new Point(40.000, 74.000, Point.CARTESIAN)
                        )
                ),
                createPath( // plus 2 outtake push 12
                        new BezierLine(
                                new Point(40.000, 74.000, Point.CARTESIAN),
                                new Point(40, 67, Point.CARTESIAN)
                        )
                ),
                createPath( // curve to plus 3 intake 13
                        new BezierCurve(
                                new Point(40.000, 67.000, Point.CARTESIAN),
                                new Point(24.655, 54.849, Point.CARTESIAN),
                                new Point(30.372, 25.548, Point.CARTESIAN),
                                new Point(10.500 + intakeOffset + intakeOffset3, 33.000+2, Point.CARTESIAN)
                        )
                ),
                createPath( // plus 3 outtake 14
                        new BezierLine(
                                new Point(10.500, 33.000, Point.CARTESIAN),
                                new Point(40.000, 74.000, Point.CARTESIAN)
                        )
                ),
                createPath( // plus 3 outtake push 15
                        new BezierLine(
                                new Point(40.000, 74.000, Point.CARTESIAN),
                                new Point(40, 67, Point.CARTESIAN)
                        )
                ),
                createPath( // curve to plus 4 intake 16
                        new BezierCurve(
                                new Point(40.000, 67.000, Point.CARTESIAN),
                                new Point(24.655, 54.849, Point.CARTESIAN),
                                new Point(30.372, 25.548, Point.CARTESIAN),
                                new Point(10.500 + intakeOffset + intakeOffset4, 33.000+2, Point.CARTESIAN)
                        )
                ),
                createPath( // plus 4 outtake 17
                        new BezierLine(
                                new Point(10.500, 33.000-1, Point.CARTESIAN),
                                new Point(40.000, 74.000, Point.CARTESIAN)
                        )
                ),
//                createPath( // park
//                        new BezierLine(
//                                new Point(40.000, 74.000, Point.CARTESIAN),
//                                new Point(13.000, 20.000, Point.CARTESIAN)
//                        ), Math.toRadians(0), Math.toRadians(-45)
//                )
                createPath( // park 18
                        new BezierLine(
                                new Point(40.000, 74.000, Point.CARTESIAN),
                                new Point(32.000, 74.000, Point.CARTESIAN)
                        ), Math.toRadians(0), Math.toRadians(-45)
                )
        );
        parkWithSpecPush = createPath(
                new BezierLine(
                        new Point(40.000, 67.000, Point.CARTESIAN),
                        new Point(13.000, 20.000, Point.CARTESIAN)
                ), Math.toRadians(0), Math.toRadians(-45)
        );

        builder.addPath(
                        // Line 1
                        new BezierLine(
                                new Point(9.000, 65.000, Point.CARTESIAN),
                                new Point(39.500, 67.000, Point.CARTESIAN)
                        )
                )
                .setConstantHeadingInterpolation(Math.toRadians(0))
                .addPath(
                        // Line 2
                        new BezierCurve(
                                new Point(39.500, 67.000, Point.CARTESIAN),
                                new Point(31.500, 66.000, Point.CARTESIAN),
                                new Point(27.335, 48.060, Point.CARTESIAN)
                        )
                )
                .setLinearHeadingInterpolation(Math.toRadians(0), Math.toRadians(-25))
                .addPath(
                        // Line 3
                        new BezierLine(
                                new Point(27.335, 48.060, Point.CARTESIAN),
                                new Point(26.799, 42.521, Point.CARTESIAN)
                        )
                )
                .setLinearHeadingInterpolation(Math.toRadians(-25), Math.toRadians(-110))
                .addPath(
                        // Line 4
                        new BezierLine(
                                new Point(26.799, 42.521, Point.CARTESIAN),
                                new Point(26.799, 36.625, Point.CARTESIAN)
                        )
                )
                .setLinearHeadingInterpolation(Math.toRadians(-110), Math.toRadians(-25))
                .addPath(
                        // Line 5
                        new BezierLine(
                                new Point(26.799, 36.625, Point.CARTESIAN),
                                new Point(27.692, 30.194, Point.CARTESIAN)
                        )
                )
                .setLinearHeadingInterpolation(Math.toRadians(-25), Math.toRadians(-120))
                .addPath(
                        // Line 6
                        new BezierLine(
                                new Point(27.692, 30.194, Point.CARTESIAN),
                                new Point(28.943, 30.908, Point.CARTESIAN)
                        )
                )
                .setLinearHeadingInterpolation(Math.toRadians(-120), Math.toRadians(-25))
                .addPath(
                        // Line 7
                        new BezierLine(
                                new Point(28.943, 30.908, Point.CARTESIAN),
                                new Point(31.087, 30.551, Point.CARTESIAN)
                        )
                )
                .setLinearHeadingInterpolation(Math.toRadians(-25), Math.toRadians(-130))
                .addPath(
                        // Line 8
                        new BezierCurve(
                                new Point(31.087, 30.551, Point.CARTESIAN),
                                new Point(27.000, 23.000, Point.CARTESIAN),
                                new Point(25.000, 34.500, Point.CARTESIAN),
                                new Point(13.500, 36.500, Point.CARTESIAN)
                        )
                )
                .setLinearHeadingInterpolation(Math.toRadians(-130), Math.toRadians(0))
                .addPath(
                        // Line 9
                        new BezierLine(
                                new Point(13.500, 36.500, Point.CARTESIAN),
                                new Point(39.500, 74.000, Point.CARTESIAN)
                        )
                )
                .setConstantHeadingInterpolation(Math.toRadians(0))
                .addPath(
                        // Line 10
                        new BezierLine(
                                new Point(39.500, 74.000, Point.CARTESIAN),
                                new Point(39.500, 67.000, Point.CARTESIAN)
                        )
                )
                .setConstantHeadingInterpolation(Math.toRadians(0))
                .addPath(
                        // Line 11
                        new BezierCurve(
                                new Point(39.500, 67.000, Point.CARTESIAN),
                                new Point(24.655, 54.670, Point.CARTESIAN),
                                new Point(30.194, 26.084, Point.CARTESIAN),
                                new Point(13.500, 34.500, Point.CARTESIAN)
                        )
                )
                .setConstantHeadingInterpolation(Math.toRadians(0))
                .addPath(
                        // Line 12
                        new BezierLine(
                                new Point(13.500, 34.500, Point.CARTESIAN),
                                new Point(40.000, 74.000, Point.CARTESIAN)
                        )
                )
                .setConstantHeadingInterpolation(Math.toRadians(0))
                .addPath(
                        // Line 13
                        new BezierLine(
                                new Point(39.500, 74.000, Point.CARTESIAN),
                                new Point(39.500, 67.000, Point.CARTESIAN)
                        )
                )
                .setConstantHeadingInterpolation(Math.toRadians(0))
                .addPath(
                        // Line 14
                        new BezierCurve(
                                new Point(40.000, 67.000, Point.CARTESIAN),
                                new Point(24.655, 54.849, Point.CARTESIAN),
                                new Point(30.372, 25.548, Point.CARTESIAN),
                                new Point(13.500, 35.000, Point.CARTESIAN)
                        )
                )
                .setConstantHeadingInterpolation(Math.toRadians(0))
                .addPath(
                        // Line 15
                        new BezierLine(
                                new Point(13.500, 35.000, Point.CARTESIAN),
                                new Point(39.500, 74.000, Point.CARTESIAN)
                        )
                )
                .setConstantHeadingInterpolation(Math.toRadians(0))
                .addPath(
                        // Line 16
                        new BezierLine(
                                new Point(39.500, 74.000, Point.CARTESIAN),
                                new Point(39.500, 67.000, Point.CARTESIAN)
                        )
                )
                .setConstantHeadingInterpolation(Math.toRadians(0))
                .addPath(
                        // Line 17
                        new BezierCurve(
                                new Point(39.500, 67.000, Point.CARTESIAN),
                                new Point(24.655, 54.849, Point.CARTESIAN),
                                new Point(30.372, 25.548, Point.CARTESIAN),
                                new Point(13.500, 35.000, Point.CARTESIAN)
                        )
                )
                .setConstantHeadingInterpolation(Math.toRadians(0))
                .addPath(
                        // Line 18
                        new BezierLine(
                                new Point(13.500, 35.000, Point.CARTESIAN),
                                new Point(39.500, 74.000, Point.CARTESIAN)
                        )
                )
                .setConstantHeadingInterpolation(Math.toRadians(0))
                .addPath(
                        // Line 19
                        new BezierLine(
                                new Point(39.500, 74.000, Point.CARTESIAN),
                                new Point(32.000, 74.000, Point.CARTESIAN)
                        )
                )
                .setLinearHeadingInterpolation(Math.toRadians(0), Math.toRadians(-45));
        extendoPush = builder.build();

        fiveSpecs.get(1).setPathEndTimeoutConstraint(0);
        fiveSpecs.get(2).setPathEndTimeoutConstraint(0);
        fiveSpecs.get(3).setPathEndTimeoutConstraint(0);
        fiveSpecs.get(4).setPathEndTimeoutConstraint(0);
        fiveSpecs.get(5).setPathEndTimeoutConstraint(0);
        fiveSpecs.get(6).setPathEndTimeoutConstraint(0);

        fiveSpecs.get(1).setZeroPowerAccelerationMultiplier(6);
        fiveSpecs.get(2).setZeroPowerAccelerationMultiplier(6);
        fiveSpecs.get(3).setZeroPowerAccelerationMultiplier(6);
        fiveSpecs.get(4).setZeroPowerAccelerationMultiplier(6);
        fiveSpecs.get(5).setZeroPowerAccelerationMultiplier(6);
        fiveSpecs.get(6).setZeroPowerAccelerationMultiplier(6);

        robotPush = new PathChain(fiveSpecs.get(1), fiveSpecs.get(2), fiveSpecs.get(3), fiveSpecs.get(4), fiveSpecs.get(5), fiveSpecs.get(6));

    }

    public static Path pathTo(BezierLine line, Follower follower) {



        return createPath(
                new BezierLine(
                        new Point(follower.getPose().getX(), follower.getPose().getY()),
                        new Point(line.getLastControlPoint().getX(), line.getLastControlPoint().getY())
                )
        );
    }

    public static Path curveTo(BezierCurve curve, Follower follower) {
        ArrayList<Point> controlPoints = curve.getControlPoints();
        controlPoints.set(0, new Point(follower.getPose().getX(), follower.getPose().getY()));

        return createPath(new BezierCurve(controlPoints));
    }


    private static Path createPath(BezierLine line, double heading) {
        Path path = new Path(line);
        path.setConstantHeadingInterpolation(heading); // Constant heading
        return path;
    }

    private static Path createPath(BezierLine line, double startHeading, double endHeading) {
        Path path = new Path(line);
        path.setLinearHeadingInterpolation(startHeading, endHeading); // Linear heading
        return path;
    }

    private static Path createPath(BezierLine line, boolean tangent) {
        Path path = new Path(line);
        if (tangent) path.setTangentHeadingInterpolation();
        return path;
    }

    private static Path createPath(BezierLine line) {
        Path path = new Path(line);
        path.setConstantHeadingInterpolation(0);
        return path;
    }

    private static Path createPath(BezierCurve curve) {
        Path path = new Path(curve);
        path.setConstantHeadingInterpolation(0);
        return path;
    }

    // Constant Heading
    private static Path createPath(BezierCurve curve, double heading) {
        Path path = new Path(curve);
        path.setConstantHeadingInterpolation(heading); // Constant heading
        return path;
    }

    // Linear Heading
    private static Path createPath(BezierCurve curve, double startHeading, double endHeading) {
        Path path = new Path(curve);
        path.setLinearHeadingInterpolation(startHeading, endHeading); // Linear heading
        return path;
    }

    // Tangent or Default Constant Heading
    private static Path createPath(BezierCurve curve, boolean tangent) {
        Path path = new Path(curve);
        if (tangent) path.setTangentHeadingInterpolation(); // Tangent heading
        else path.setConstantHeadingInterpolation(0);      // Default constant heading
        return path;
    }
}

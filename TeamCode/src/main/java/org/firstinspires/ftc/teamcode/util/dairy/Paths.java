package org.firstinspires.ftc.teamcode.util.dairy;

import com.pedropathing.localization.Pose;
import com.pedropathing.pathgen.BezierCurve;
import com.pedropathing.pathgen.BezierLine;
import com.pedropathing.pathgen.Path;
import com.pedropathing.pathgen.Point;

import org.firstinspires.ftc.teamcode.util.dairy.subsystems.Chassis;

import java.util.ArrayList;
import java.util.Collections;

public class Paths {

    public static ArrayList<Path> plusFourSpec = new ArrayList<>();
    public static Path parkWithSpecPush;

    public static double intakeOffset = -2;
    public static double intakeOffset1 = 2;
    public static double intakeOffset2 = 1;
    public static double intakeOffset3 = 1;
    public static double intakeOffset4 = 0;

    public static void init() {
        Collections.addAll(plusFourSpec,
                createPath( //preload
                        new BezierLine(
                                new Point(9.000, 65.000, Point.CARTESIAN),
                                new Point(40.00, 67.000, Point.CARTESIAN)
                        )
                ),//1
                createPath( // curve to first sample
                        new BezierCurve(
                                new Point(41.000, 67.000, Point.CARTESIAN),
                                new Point(31.500, 66.000, Point.CARTESIAN),
                                new Point(13.000, 26.000, Point.CARTESIAN),
                                new Point(63.000, 45.000, Point.CARTESIAN),
                                new Point(58.500, 23.000, Point.CARTESIAN)
                        )
                ),//2
                createPath( // push first sample
                        new BezierLine(
                                new Point(58.500, 23.000, Point.CARTESIAN),
                                new Point(20.000, 22.500, Point.CARTESIAN)
                        )
                ),//3
                createPath( // curve to second sample
                        new BezierCurve(
                                new Point(20.000, 22.500, Point.CARTESIAN),
                                new Point(62.500, 32.000, Point.CARTESIAN),
                                new Point(58.500, 12.500, Point.CARTESIAN)
                        )
                ),//4
                createPath( // push second sample
                        new BezierLine(
                                new Point(58.500, 12.500, Point.CARTESIAN),
                                new Point(20.000, 12.500, Point.CARTESIAN)
                        )
                ),//5
                createPath( // curve to third sample
                        new BezierCurve(
                                new Point(20.000, 12.500, Point.CARTESIAN),
                                new Point(62.000, 18.500, Point.CARTESIAN),
                                new Point(58.500, 8.000, Point.CARTESIAN)
                        )
                ),//6
                createPath( // push third sample
                        new BezierLine(
                                new Point(58.500, 8.000, Point.CARTESIAN),
                                new Point(20.000, 8.000, Point.CARTESIAN)
                        )
                ),//7
                createPath( // curve to plus 1 intake
                        new BezierCurve(
                                new Point(20.000, 8.000, Point.CARTESIAN),
                                new Point(27.000, 23.000, Point.CARTESIAN),
                                new Point( 25, 34.5, Point.CARTESIAN),
                                new Point(10.500 + intakeOffset + intakeOffset1, 33.000+2.5, Point.CARTESIAN)
                        )
                ),//8
                createPath( // plus 1 outtake
                        new BezierLine(
                                new Point(10.500, 33.000+2.5, Point.CARTESIAN),
                                new Point(41.000, 74.000, Point.CARTESIAN)
                        )
                ),//9
                createPath(
                        new BezierLine(
                                new Point(41.000, 74.000, Point.CARTESIAN),
                                new Point(41, 67, Point.CARTESIAN)
                        )
                ),//10
                createPath( // curve to plus 2 intake
                        new BezierCurve(
                                new Point(41.000, 67.000, Point.CARTESIAN),
                                new Point(24.655, 54.670, Point.CARTESIAN),
                                new Point(30.194, 26.084, Point.CARTESIAN),
                                new Point(10.500 + intakeOffset + intakeOffset2, 33.000+1.5, Point.CARTESIAN)
                        )
                ),//11
                createPath( // plus 2 outtake
                        new BezierLine(
                                new Point(10.500, 33.000, Point.CARTESIAN),
                                new Point(41.000, 74.000, Point.CARTESIAN)
                        )
                ),//12
                createPath(
                        new BezierLine(
                                new Point(41.000, 74.000, Point.CARTESIAN),
                                new Point(41, 67, Point.CARTESIAN)
                        )
                ),//13
                createPath( // curve to plus 3 intake
                        new BezierCurve(
                                new Point(41.000, 67.000, Point.CARTESIAN),
                                new Point(24.655, 54.849, Point.CARTESIAN),
                                new Point(30.372, 25.548, Point.CARTESIAN),
                                new Point(10.500 + intakeOffset + intakeOffset3, 33.000, Point.CARTESIAN)
                        )
                ),//14
                createPath( // plus 3 outtake
                        new BezierLine(
                                new Point(10.500, 33.000, Point.CARTESIAN),
                                new Point(41.000, 74.000, Point.CARTESIAN)
                        )
                ),//15
                createPath(
                        new BezierLine(
                                new Point(41.000, 74.000, Point.CARTESIAN),
                                new Point(41, 67, Point.CARTESIAN)
                        )
                ),//16
                createPath( // curve to plus 4 intake
                        new BezierCurve(
                                new Point(41.000, 67.000, Point.CARTESIAN),
                                new Point(24.655, 54.849, Point.CARTESIAN),
                                new Point(30.372, 25.548, Point.CARTESIAN),
                                new Point(10.500 + intakeOffset + intakeOffset4, 33.000-1, Point.CARTESIAN)
                        )
                ),//17
                createPath( // plus 4 outtake
                        new BezierLine(
                                new Point(10.500, 33.000-1, Point.CARTESIAN),
                                new Point(41.000, 74.000, Point.CARTESIAN)
                        )
                ),//18
                createPath( // park
                        new BezierLine(
                                new Point(41.000, 74.000, Point.CARTESIAN),
                                new Point(13.000, 20.000, Point.CARTESIAN)
                        ), Math.toRadians(0), Math.toRadians(-45)
                )//19
        );
        parkWithSpecPush = createPath(
                new BezierLine(
                        new Point(40.000, 67.000, Point.CARTESIAN),
                        new Point(13.000, 20.000, Point.CARTESIAN)
                ), Math.toRadians(0), Math.toRadians(-45)
        );



//        plusFourSpec.get(7).setPathEndTimeoutConstraint(500);
//        plusFourSpec.get(10).setPathEndTimeoutConstraint(500);
//        plusFourSpec.get(13).setPathEndTimeoutConstraint(500);
//        plusFourSpec.get(16).setPathEndTimeoutConstraint(500);
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

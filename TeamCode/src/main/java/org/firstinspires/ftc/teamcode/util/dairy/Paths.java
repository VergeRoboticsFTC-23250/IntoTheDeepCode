package org.firstinspires.ftc.teamcode.util.dairy;

import com.pedropathing.pathgen.BezierCurve;
import com.pedropathing.pathgen.BezierLine;
import com.pedropathing.pathgen.Path;
import com.pedropathing.pathgen.Point;

import org.firstinspires.ftc.teamcode.util.dairy.subsystems.Chassis;

import java.util.ArrayList;
import java.util.Collections;

public class Paths {

    public static ArrayList<Path> plusThreeBlueSpec = new ArrayList<>();
    public static ArrayList<Path> plusThreeRedSpec = new ArrayList<>();

    public static void init() {
        Collections.addAll(plusThreeBlueSpec,
                createPath(
                        new BezierLine(
                                new Point(9.000, 65.000, Point.CARTESIAN),
                                new Point(40.500, 64.800, Point.CARTESIAN)
                        )
                ),
                createPath(
                        new BezierCurve(
                                new Point(40.500, 64.800, Point.CARTESIAN),
                                new Point(31.500, 66.000, Point.CARTESIAN),
                                new Point(13.000, 26.000, Point.CARTESIAN),
                                new Point(63.000, 45.000, Point.CARTESIAN),
                                new Point(58.500, 23.000, Point.CARTESIAN)
                        )
                ),
                createPath(
                        new BezierLine(
                                new Point(58.500, 23.000, Point.CARTESIAN),
                                new Point(15.500, 22.500, Point.CARTESIAN)
                        )
                ),
                createPath(
                        new BezierCurve(
                                new Point(15.500, 22.500, Point.CARTESIAN),
                                new Point(62.500, 32.000, Point.CARTESIAN),
                                new Point(58.500, 12.500, Point.CARTESIAN)
                        )
                ),
                createPath(
                        new BezierLine(
                                new Point(58.500, 12.500, Point.CARTESIAN),
                                new Point(16.300, 12.500, Point.CARTESIAN)
                        )
                ),
                createPath(
                        new BezierCurve(
                                new Point(16.300, 12.500, Point.CARTESIAN),
                                new Point(62.000, 18.500, Point.CARTESIAN),
                                new Point(58.500, 7.500, Point.CARTESIAN)
                        )
                ),
                createPath(
                        new BezierLine(
                                new Point(58.500, 7.500, Point.CARTESIAN),
                                new Point(16.500, 7.500, Point.CARTESIAN)
                        )
                ),
                createPath(
                        new BezierCurve(
                                new Point(16.500, 7.500, Point.CARTESIAN),
                                new Point(27.000, 23.000, Point.CARTESIAN),
                                new Point(9.000, 24.000, Point.CARTESIAN)
                        )
                ),
                createPath(
                        new BezierLine(
                                new Point(9.000, 24.000, Point.CARTESIAN),
                                new Point(40.500, 66.000, Point.CARTESIAN)
                        )
                ),
                createPath(
                        new BezierLine(
                                new Point(40.500, 66.000, Point.CARTESIAN),
                                new Point(9.000, 24.000, Point.CARTESIAN)
                        )
                ),
                createPath(
                        new BezierLine(
                                new Point(9.000, 24.000, Point.CARTESIAN),
                                new Point(40.500, 68.000, Point.CARTESIAN)
                        )
                ),
                createPath(
                        new BezierLine(
                                new Point(40.500, 68.000, Point.CARTESIAN),
                                new Point(9.000, 24.000, Point.CARTESIAN)
                        )
                ),
                createPath(
                        new BezierLine(
                                new Point(9.000, 24.000, Point.CARTESIAN),
                                new Point(40.500, 70.000, Point.CARTESIAN)
                        )
                ),
                createPath(
                        new BezierLine(
                                new Point(40.500, 70.000, Point.CARTESIAN),
                                new Point(15.000, 24.000, Point.CARTESIAN)
                        ), Math.toRadians(0), Math.toRadians(-45)
                )
        );
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

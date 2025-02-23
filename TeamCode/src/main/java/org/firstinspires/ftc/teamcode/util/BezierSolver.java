package org.firstinspires.ftc.teamcode.util;

import com.pedropathing.localization.Pose;

public class BezierSolver {

    public static Pose getClosestBezierPoseWithX(Pose reference, Pose[] controlPoints) {
        int n = controlPoints.length;
        if (n < 3 || n > 4) {
            throw new IllegalArgumentException("BezierSolver only supports 3 or 4 control points.");
        }

        double[] Px = new double[n];
        double[] Py = new double[n];

        for (int i = 0; i < n; i++) {
            Px[i] = controlPoints[i].getX();
            Py[i] = controlPoints[i].getY();
        }

        double minX = Double.MAX_VALUE, maxX = Double.MIN_VALUE;
        for (double x : Px) {
            minX = Math.min(minX, x);
            maxX = Math.max(maxX, x);
        }

        if (reference.getX() <= minX) return new Pose(Px[0], Py[0], 0);
        if (reference.getX() >= maxX) return new Pose(Px[n - 1], Py[n - 1], 0);

        double t = findRoot(Px, reference.getX(), 0.5);
        if (t >= 0 && t <= 1) {
            double Bx = bezier(t, Px);
            double By = bezier(t, Py);
            return new Pose(Bx, By, 0);
        }

        return null;
    }

    private static double bezier(double t, double[] P) {
        double mt = 1 - t;
        if (P.length == 3) {
            return mt * mt * P[0] + 2 * mt * t * P[1] + t * t * P[2];
        } else {
            return mt * mt * mt * P[0] + 3 * mt * mt * t * P[1] + 3 * mt * t * t * P[2] + t * t * t * P[3];
        }
    }

    private static double bezierDerivative(double t, double[] P) {
        double mt = 1 - t;
        if (P.length == 3) {
            return 2 * mt * (P[1] - P[0]) + 2 * t * (P[2] - P[1]);
        } else {
            return 3 * mt * mt * (P[1] - P[0]) + 6 * mt * t * (P[2] - P[1]) + 3 * t * t * (P[3] - P[2]);
        }
    }

    private static double findRoot(double[] Px, double targetX, double initialGuess) {
        double tolerance = 1e-6;
        double maxIterations = 100;
        double t = initialGuess;

        for (int i = 0; i < maxIterations; i++) {
            double fx = bezier(t, Px) - targetX;
            double fPrimeX = bezierDerivative(t, Px);

            if (Math.abs(fx) < tolerance) return t;
            t -= fx / fPrimeX;
        }

        return -1;
    }

    public static Pose getClosestBezierPoint(Pose target, double distanceThreshold, Pose[] controlPoints, boolean findClosestToEnd) {
        int n = controlPoints.length;
        if (n < 3 || n > 4) {
            throw new IllegalArgumentException("BezierSolver only supports 3 or 4 control points.");
        }

        double[] Px = new double[n];
        double[] Py = new double[n];

        for (int i = 0; i < n; i++) {
            Px[i] = controlPoints[i].getX();
            Py[i] = controlPoints[i].getY();
        }

        Pose startPose = new Pose(Px[0], Py[0], 0);
        Pose endPose = new Pose(Px[n - 1], Py[n - 1], 0);

        double startDistance = Math.hypot(startPose.getX() - target.getX(), startPose.getY() - target.getY());
        double endDistance = Math.hypot(endPose.getX() - target.getX(), endPose.getY() - target.getY());

        if (!findClosestToEnd && startDistance <= distanceThreshold) return startPose;
        if (findClosestToEnd && endDistance <= distanceThreshold) return endPose;

        double bestT = -1;
        double minDistance = Double.MAX_VALUE;

        for (double t = 0; t <= 1; t += 0.01) {
            double Bx = bezier(t, Px);
            double By = bezier(t, Py);
            double distance = Math.hypot(Bx - target.getX(), By - target.getY());

            if (Math.abs(distance - distanceThreshold) < 0.3) {
                if (findClosestToEnd) {
                    if (t > bestT) bestT = t;
                } else {
                    if (t < bestT || bestT == -1) bestT = t;
                }
            }
        }

        if (bestT != -1) {
            return new Pose(bezier(bestT, Px), bezier(bestT, Py), 0);
        }

        return null;
    }
}
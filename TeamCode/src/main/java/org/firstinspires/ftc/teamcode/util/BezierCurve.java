package org.firstinspires.ftc.teamcode.util;

import com.pedropathing.localization.Pose;

import java.util.ArrayList;
import java.util.List;

public class BezierCurve {
    private Pose[] controlPoints;
    private Pose[] sampledPoints;
    public static double SAMPLE_DISTANCE = .1; // Adjustable sample distance
    public static int LOOKAHEAD_DIST = 30;

    public BezierCurve(Pose... controlPoints) {
        this.controlPoints = controlPoints;
        this.sampledPoints = sampleCurve();
    }

    private Pose[] sampleCurve() {
        int highResSteps = 500; // High resolution sampling
        List<Pose> highResPoints = new ArrayList<>();
        List<Double> arcLengths = new ArrayList<>();
        double totalLength = 0.0;

        Pose prev = deCasteljau(controlPoints, 0);
        highResPoints.add(prev);
        arcLengths.add(0.0);

        for (int i = 1; i <= highResSteps; i++) {
            double t = (double) i / highResSteps;
            Pose current = deCasteljau(controlPoints, t);
            totalLength += distance(prev, current);
            highResPoints.add(current);
            arcLengths.add(totalLength);
            prev = current;
        }

        // Resample at fixed arc-length intervals
        List<Pose> samples = new ArrayList<>();
        samples.add(highResPoints.get(0)); // Ensure first point

        double targetLength = SAMPLE_DISTANCE;
        for (int i = 1; i < highResPoints.size(); i++) {
            if (arcLengths.get(i) >= targetLength) {
                samples.add(highResPoints.get(i));
                targetLength += SAMPLE_DISTANCE;
            }
        }

        // Ensure last point is exactly the endpoint
        samples.add(highResPoints.get(highResPoints.size() - 1));

        return samples.toArray(new Pose[0]);
    }

    private Pose deCasteljau(Pose[] points, double t) {
        int n = points.length;
        Pose[] temp = new Pose[n];

        for (int i = 0; i < n; i++) {
            temp[i] = points[i].copy();
        }

        for (int r = 1; r < n; r++) {
            for (int i = 0; i < n - r; i++) {
                double x = (1 - t) * temp[i].getX() + t * temp[i + 1].getX();
                double y = (1 - t) * temp[i].getY() + t * temp[i + 1].getY();
                temp[i] = new Pose(x, y);
            }
        }
        return temp[0];
    }

    private double distance(Pose p1, Pose p2) {
        double dx = p2.getX() - p1.getX();
        double dy = p2.getY() - p1.getY();
        return Math.sqrt(dx * dx + dy * dy);
    }

    public int getClosestSampleIndexToPoint(Pose target) {
        int closestIndex = -1;
        double minDistance = Double.MAX_VALUE;

        for (int i = 0; i < sampledPoints.length; i++) {
            double dist = distance(target, sampledPoints[i]);
            if (dist < minDistance) {
                minDistance = dist;
                closestIndex = i;
            }
        }
        return closestIndex;
    }

    public Pose getClosestLookaheadPoint(Pose target){
        int i = getClosestSampleIndexToPoint(target) + LOOKAHEAD_DIST;
        i = Util.clamp(i, 0, sampledPoints.length - 1);
        return sampledPoints[i];
    }

    public Pose[] getSampledPoints() {
        return sampledPoints;
    }

    public Pose[] getControlPoints() {
        return controlPoints;
    }

    public Pose getEnd(){
        return controlPoints[controlPoints.length - 1];
    }

    public Pose getStart(){
        return controlPoints[0];
    }
}
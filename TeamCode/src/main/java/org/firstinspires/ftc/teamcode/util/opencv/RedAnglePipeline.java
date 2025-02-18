package org.firstinspires.ftc.teamcode.util.opencv;

import org.opencv.core.*;
import org.opencv.imgproc.Imgproc;
import org.opencv.imgproc.Moments;
import org.openftc.easyopencv.OpenCvPipeline;

import java.util.ArrayList;

public class RedAnglePipeline extends OpenCvPipeline {
    private static final double CAMERA_HORIZONTAL_FOV = 70.42;
    private static final double CAMERA_VERTICAL_FOV = 43.3;
    private static double angle;
    private static double tx;
    private static double ty;
    private final Mat hsv = new Mat();
    private final Mat mask1 = new Mat();
    private final Mat mask2 = new Mat();
    private final Mat combinedMask = new Mat();
    private final Mat hierarchy = new Mat();
    private final ArrayList<MatOfPoint> contours = new ArrayList<>();
    public static double MAX_CONTOUR_SIZE = 115000.0;
    public static double PCB_HEIGHT_IN = 8.218;
    public static double LENS_HEIGHT_IN = 0.55;
    public static Scalar lowerRed1 = new Scalar(0, 100, 100);
    public static Scalar upperRed1 = new Scalar(10, 255, 255);
    public static Scalar lowerRed2 = new Scalar(160, 100, 100);
    public static Scalar upperRed2 = new Scalar(180, 255, 255);

    @Override
    public Mat processFrame(Mat input) {
        // Convert to HSV color space
        Imgproc.cvtColor(input, hsv, Imgproc.COLOR_RGB2HSV);

        // Threshold to detect both red ranges
        Core.inRange(hsv, lowerRed1, upperRed1, mask1);
        Core.inRange(hsv, lowerRed2, upperRed2, mask2);

        // Combine masks for red
        Core.bitwise_or(mask1, mask2, combinedMask);

        // Check contour size and adjust erosion and dilation accordingly
        if (Core.countNonZero(combinedMask) > MAX_CONTOUR_SIZE) {
            Imgproc.erode(combinedMask, combinedMask, Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(24, 24)));
            Imgproc.dilate(combinedMask, combinedMask, Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(4, 4)));
        } else {
            Imgproc.dilate(combinedMask, combinedMask, Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(4, 4)));
            Imgproc.erode(combinedMask, combinedMask, Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(24, 24)));
        }

        contours.clear();

        // Find contours
        Imgproc.findContours(combinedMask, contours, hierarchy, Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE);

        // Find the largest contour that is closest to the bottom right and less than MAX_CONTOUR_SIZE
        MatOfPoint bestContour = null;
        double maxArea = 0;
        double minDistanceToBottomRight = Double.MAX_VALUE;
        Point bottomRight = new Point(input.width(), input.height());

        for (MatOfPoint contour : contours) {
            double area = Imgproc.contourArea(contour);
            if (area < MAX_CONTOUR_SIZE) {
                Moments moments = Imgproc.moments(contour);
                Point contourCenter = new Point(moments.m10 / moments.m00, moments.m01 / moments.m00);
                double distanceToBottomRight = Math.hypot(contourCenter.x - bottomRight.x, contourCenter.y - bottomRight.y);

                if (area > maxArea || (area == maxArea && distanceToBottomRight < minDistanceToBottomRight)) {
                    maxArea = area;
                    minDistanceToBottomRight = distanceToBottomRight;
                    bestContour = contour;
                }
            }
        }

        if (bestContour != null) {
            MatOfPoint2f contour2f = new MatOfPoint2f(bestContour.toArray());
            RotatedRect rect = Imgproc.minAreaRect(contour2f);
            Point[] box = new Point[4];
            rect.points(box);
            for (int i = 0; i < 4; i++) {
                Imgproc.line(input, box[i], box[(i + 1) % 4], new Scalar(0, 255, 0), 2);
            }

            // Draw center
            Imgproc.circle(input, rect.center, 5, new Scalar(255, 0, 0), -1);

            // Calculate corrected angle in range [0, 180]
            angle = rect.angle;
            if (rect.size.width < rect.size.height) {
                angle += 90;
            }

            // Calculate offsets as a fraction of the camera's FOV
            double imageCenterX = input.width() / 2.0;
            double imageCenterY = input.height() / 2.0;
            tx = -(rect.center.x - imageCenterX) / imageCenterX * (CAMERA_HORIZONTAL_FOV / 2.0);
            ty = -(rect.center.y - imageCenterY) / imageCenterY * (CAMERA_VERTICAL_FOV / 2.0);

            // Display angle, offsets, and contour size
            String angleText = String.format("Angle: %.2f", angle);
            String offsetText = String.format("X Offset: %.2f, Y Offset: %.2f", tx, ty);
            String sizeText = String.format("Contour Size: %.2f", maxArea);
            double x = Math.tan(Math.toRadians(tx)) * (PCB_HEIGHT_IN - LENS_HEIGHT_IN);
            double y = Math.tan(Math.toRadians(ty)) * (PCB_HEIGHT_IN - LENS_HEIGHT_IN);
            String distanceText = String.format("X Distance: %.2f, Y Distance: %.2f", x, y);
            Imgproc.putText(input, angleText, new Point(rect.center.x + 10, rect.center.y - 10), Imgproc.FONT_HERSHEY_SIMPLEX, 0.65, new Scalar(255, 255, 255), 2);
            Imgproc.putText(input, offsetText, new Point(rect.center.x + 10, rect.center.y + 10), Imgproc.FONT_HERSHEY_SIMPLEX, 0.65, new Scalar(255, 255, 255), 2);
            Imgproc.putText(input, sizeText, new Point(rect.center.x + 10, rect.center.y + 30), Imgproc.FONT_HERSHEY_SIMPLEX, 0.65, new Scalar(255, 255, 255), 2);
            Imgproc.putText(input, distanceText, new Point(rect.center.x + 10, rect.center.y + 50), Imgproc.FONT_HERSHEY_SIMPLEX, 0.65, new Scalar(255, 255, 255), 2);
            Imgproc.circle(input, new Point(imageCenterX, imageCenterY), 5, new Scalar(255, 0, 0), -1);
        }

        return input;
    }

    public double getPosition() { return angle / 180.0; }
    public double getX() { return tx; }
    public double getY() { return ty; }
}

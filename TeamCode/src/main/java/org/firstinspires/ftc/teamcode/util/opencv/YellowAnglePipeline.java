package org.firstinspires.ftc.teamcode.util.opencv;

import org.opencv.core.*;
import org.opencv.imgproc.Imgproc;
import org.openftc.easyopencv.OpenCvPipeline;

import java.util.ArrayList;
import java.util.List;

public class YellowAnglePipeline extends OpenCvPipeline {
    private static final double CAMERA_HORIZONTAL_FOV = 70.42;
    private static final double CAMERA_VERTICAL_FOV = 43.3;
    private static double angle;
    private static double xOffset;
    private static double yOffset;
    private Mat hsv = new Mat();
    private Mat mask = new Mat();
    private Mat hierarchy = new Mat();
    private List<MatOfPoint> contours = new ArrayList<>();

    @Override
    public Mat processFrame(Mat input) {
        // Convert to HSV color space
        Imgproc.cvtColor(input, hsv, Imgproc.COLOR_RGB2HSV);

        // Define yellow color range
        Scalar lowerYellow = new Scalar(20, 100, 100);
        Scalar upperYellow = new Scalar(30, 255, 255);

        // Threshold to detect yellow
        Core.inRange(hsv, lowerYellow, upperYellow, mask);

        // Morphological operations to reduce noise
        Imgproc.erode(mask, mask, Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(3, 3)));
        Imgproc.dilate(mask, mask, Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(5, 5)));

        // Find contours
        Imgproc.findContours(mask, contours, hierarchy, Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE);

        // Find the largest contour
        MatOfPoint largestContour = null;
        double maxArea = 0;
        for (MatOfPoint contour : contours) {
            double area = Imgproc.contourArea(contour);
            if (area > maxArea) {
                maxArea = area;
                largestContour = contour;
            }
        }

        if (largestContour != null && maxArea > 500) { // Ensure it meets size threshold
            MatOfPoint2f contour2f = new MatOfPoint2f(largestContour.toArray());
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
             xOffset = (rect.center.x - imageCenterX) / imageCenterX * (CAMERA_HORIZONTAL_FOV / 2.0);
             yOffset = (rect.center.y - imageCenterY) / imageCenterY * (CAMERA_VERTICAL_FOV / 2.0);

            // Display angle and offsets
            String angleText = String.format("Angle: %.2f", angle);
            String offsetText = String.format("X Offset: %.2f, Y Offset: %.2f", xOffset, yOffset);
            Imgproc.putText(input, angleText, new Point(rect.center.x + 10, rect.center.y - 10), Imgproc.FONT_HERSHEY_SIMPLEX, 1, new Scalar(255, 255, 255), 2);
            Imgproc.putText(input, offsetText, new Point(rect.center.x + 10, rect.center.y + 10), Imgproc.FONT_HERSHEY_SIMPLEX, 1, new Scalar(255, 255, 255), 2);
            Imgproc.circle(input, new Point(imageCenterX, imageCenterY), 5, new Scalar(255, 0, 0), -1);
            // the circle is the center of the image
        }

        return input;
    }

    public double getPosition() { return angle / 180.0; }
    public double getX() { return xOffset; }
    public double getY() { return yOffset; }
}
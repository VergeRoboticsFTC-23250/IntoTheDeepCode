package org.firstinspires.ftc.teamcode.util.opencv;

import org.opencv.core.*;
import org.opencv.imgproc.Imgproc;
import org.opencv.core.MatOfPoint;
import org.openftc.easyopencv.OpenCvPipeline;

import java.util.ArrayList;
import java.util.List;

public class AngleOffsetPipeline extends OpenCvPipeline {


    private final Scalar lowerBlue = new Scalar(100, 150, 5);
    private final Scalar upperBlue = new Scalar(140, 250, 135);

    private final Scalar lowerRed1 = new Scalar(0, 243, 102);
    private final Scalar upperRed1 = new Scalar(10, 255, 255);
    private final Scalar lowerRed2 = new Scalar(147, 243, 102);
    private final Scalar upperRed2 = new Scalar(180, 255, 255);

    private double horizontalAngleOffset = 0.0;
    private double verticalAngleOffset = 0.0;

    private final double cameraHorizontalFOV = 70.42;  // Degrees
    private final double cameraVerticalFOV = 43.3;


    private final DetectionMode mode;
    public enum DetectionMode {
        BLUE,
        RED
    }

    public AngleOffsetPipeline(DetectionMode mode) {
        this.mode = mode;
    }

    @Override
    public Mat processFrame(Mat input) {
        Rect roi = new Rect(
            new Point(input.cols() * .05, input.rows() * .05),
            new Point(input.cols() * .95, input.rows() * .7)
        );
        Mat hsv = new Mat();
        Mat mask = new Mat();
        Mat maskBlue = new Mat();
        Mat maskRed = new Mat();

        // Crop the frame to the ROI
        Mat roiFrame = input.submat(roi);

        // Convert cropped frame to HSV
        Imgproc.cvtColor(roiFrame, hsv, Imgproc.COLOR_RGB2HSV);

        if (mode == DetectionMode.BLUE) {
            Core.inRange(hsv, lowerBlue, upperBlue, mask);
        } else if (mode == DetectionMode.RED) {
            Mat tempMask1 = new Mat();
            Mat tempMask2 = new Mat();
            Core.inRange(hsv, lowerRed1, upperRed1, tempMask1);
            Core.inRange(hsv, lowerRed2, upperRed2, tempMask2);
            Core.add(tempMask1, tempMask2, mask);
        }

        Mat kernel = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(4, 4));
        Imgproc.dilate(mask, mask, kernel);

        List<MatOfPoint> contours = new ArrayList<>();
        Mat hierarchy = new Mat();
        Imgproc.findContours(mask, contours, hierarchy, Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE);

        // Find the largest contour
        double maxArea = 0.0;
        Rect largestRect = null;
        for (MatOfPoint contour : contours) {
            Rect rect = Imgproc.boundingRect(contour);
            double area = Imgproc.contourArea(contour);
            if (area > maxArea) {
                maxArea = area;
                largestRect = rect;
            }
        }

        // Calculate offsets if a target is found
        if (largestRect != null) {
            // Adjust the rectangle to account for ROI cropping
            largestRect.x += roi.x;
            largestRect.y += roi.y;

            // Center of the object
            double cx = largestRect.x + largestRect.width / 2.0;
            double cy = largestRect.y + largestRect.height / 2.0;

            // Frame center (crosshair)
            double frameCenterX = input.cols() / 2.0;
            double frameCenterY = input.rows() / 2.0;

            // Pixel offsets
            double xOffsetPixels = cx - frameCenterX;
            double yOffsetPixels = cy - frameCenterY;

            // Angular offsets
            horizontalAngleOffset = (xOffsetPixels / input.cols()) * cameraHorizontalFOV;
            verticalAngleOffset = (yOffsetPixels / input.rows()) * cameraVerticalFOV;

            // Draw crosshair
            Imgproc.line(input, new Point(frameCenterX - 20, frameCenterY), new Point(frameCenterX + 20, frameCenterY), new Scalar(255, 255, 255), 2);
            Imgproc.line(input, new Point(frameCenterX, frameCenterY - 20), new Point(frameCenterX, frameCenterY + 20), new Scalar(255, 255, 255), 2);

            // Draw bounding box
            Imgproc.rectangle(input, largestRect, new Scalar(0, 255, 0), 2);

            int textX = (int) (largestRect.x + largestRect.width + 10); // Position the text to the right of the box
            int textY = (int) (largestRect.y + largestRect.height / 2);  // Vertically centered in the box

            String text = String.format("tx: %.2f, ty: %.2f", horizontalAngleOffset, verticalAngleOffset);

            Scalar textColor = new Scalar(255, 255, 255);
            int fontFace = Imgproc.FONT_HERSHEY_SIMPLEX;
            double fontScale = 0.6;
            int thickness = 1;


            Imgproc.putText(input, text, new Point(textX, textY), fontFace, fontScale, textColor, thickness);
        }

        return input;
    }


    public double getHorizontalAngleOffset() {
        return horizontalAngleOffset;
    }

    public double getVerticalAngleOffset() {
        return verticalAngleOffset;
    }
}

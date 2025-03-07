package org.firstinspires.ftc.teamcode.util.opencv;

import com.acmerobotics.dashboard.FtcDashboard;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvCameraRotation;
import org.openftc.easyopencv.OpenCvWebcam;

public class Vision {
    OpenCvWebcam webcam;
    VisionPipeline pipeline;
    Telemetry dashboard;

    public Vision(HardwareMap hardwareMap, VisionPipeline.SampleColor color){
        dashboard = FtcDashboard.getInstance().getTelemetry();
        int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        webcam = OpenCvCameraFactory.getInstance().createWebcam(hardwareMap.get(WebcamName.class, "Webcam"), cameraMonitorViewId);
        pipeline = new VisionPipeline();
        webcam.setPipeline(pipeline);
        pipeline.setColor(color);
        webcam.openCameraDeviceAsync(new OpenCvCamera.AsyncCameraOpenListener()
        {
            @Override
            public void onOpened()
            {
                webcam.startStreaming(1280,720, OpenCvCameraRotation.SENSOR_NATIVE, OpenCvWebcam.StreamFormat.MJPEG);
            }

            @Override
            public void onError(int errorCode) {}
        });
        FtcDashboard.getInstance().startCameraStream(webcam, 30);
    }

    public boolean isSampleVisible(){
        return pipeline.isSampleVisible();
    }

    public double getX(){
        return -pipeline.getX();
    }

    public double getY(){
        return pipeline.getY();
    }

    public double getAngle(){
        return pipeline.getAngle();
    }

    public double getEuclideanDist(){
        return Math.hypot(pipeline.getX(), pipeline.getY());
    }

    public double getErrorAngle(){
        return Math.atan2(pipeline.getX(), pipeline.getY());
    }
}

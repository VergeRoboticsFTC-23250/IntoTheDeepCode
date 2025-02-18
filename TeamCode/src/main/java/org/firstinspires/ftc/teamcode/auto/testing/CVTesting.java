package org.firstinspires.ftc.teamcode.auto.testing;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.teamcode.util.dairy.Paths;
import org.firstinspires.ftc.teamcode.util.dairy.Robot;
import org.firstinspires.ftc.teamcode.util.dairy.features.LoopTimes;
import org.firstinspires.ftc.teamcode.util.dairy.subsystems.Chassis;
import org.firstinspires.ftc.teamcode.util.dairy.subsystems.Intake;
import org.firstinspires.ftc.teamcode.util.dairy.subsystems.IntakeSlides;
import org.firstinspires.ftc.teamcode.util.dairy.subsystems.Outtake;
import org.firstinspires.ftc.teamcode.util.dairy.subsystems.OuttakeSlides;
import org.firstinspires.ftc.teamcode.util.opencv.YellowAnglePipeline;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvCameraRotation;
import org.openftc.easyopencv.OpenCvWebcam;

import dev.frozenmilk.dairy.core.util.features.BulkRead;
import dev.frozenmilk.mercurial.Mercurial;
import dev.frozenmilk.mercurial.commands.groups.Sequential;
import dev.frozenmilk.mercurial.commands.util.Wait;

@Autonomous
@Config
public class CVTesting extends OpMode {

    OpenCvWebcam webcam;
    YellowAnglePipeline pipeline;
    Servo test;
    public static final double SERVO_RANGE = 300.0;

    @Override
    public void init() {
        int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        webcam = OpenCvCameraFactory.getInstance().createWebcam(hardwareMap.get(WebcamName.class, "Webcam"), cameraMonitorViewId);
        pipeline = new YellowAnglePipeline();
        webcam.setPipeline(pipeline);
        webcam.openCameraDeviceAsync(new OpenCvCamera.AsyncCameraOpenListener()
        {
            @Override
            public void onOpened()
            {
                webcam.startStreaming(1280,720, OpenCvCameraRotation.SIDEWAYS_RIGHT, OpenCvWebcam.StreamFormat.MJPEG);
            }

            @Override
            public void onError(int errorCode) {}
        });
        FtcDashboard.getInstance().startCameraStream(webcam, 30);
        test = hardwareMap.get(Servo.class, "test");
    }

    @Override
    public void init_loop() {
        telemetry.addData("pipeline pos", pipeline.getPosition());
        telemetry.addData("pipeline x", pipeline.getX());
        telemetry.addData("pipeline y", pipeline.getY());
        telemetry.addData("servo pos", pipeline.getPosition() * 180.0 / SERVO_RANGE);
        FtcDashboard.getInstance().getTelemetry().addData("pipeline pos", pipeline.getPosition());
        FtcDashboard.getInstance().getTelemetry().addData("pipeline x", pipeline.getX());
        FtcDashboard.getInstance().getTelemetry().addData("pipeline y", pipeline.getY());
        FtcDashboard.getInstance().getTelemetry().addData("servo pos", pipeline.getPosition() * 180.0 / SERVO_RANGE);
        FtcDashboard.getInstance().getTelemetry().update();
        telemetry.update();
        test.setPosition(pipeline.getPosition() * 180.0 / SERVO_RANGE);
    }

    @Override
    public void loop() {
        telemetry.addData("pipeline pos", pipeline.getPosition());
        telemetry.addData("pipeline x", pipeline.getX());
        telemetry.addData("pipeline y", pipeline.getY());
        telemetry.addData("servo pos", pipeline.getPosition() * 180.0 / SERVO_RANGE);
        FtcDashboard.getInstance().getTelemetry().addData("pipeline pos", pipeline.getPosition());
        FtcDashboard.getInstance().getTelemetry().addData("pipeline x", pipeline.getX());
        FtcDashboard.getInstance().getTelemetry().addData("pipeline y", pipeline.getY());
        FtcDashboard.getInstance().getTelemetry().addData("servo pos", pipeline.getPosition() * 180.0 / SERVO_RANGE);
        FtcDashboard.getInstance().getTelemetry().update();
        telemetry.update();
        test.setPosition(pipeline.getPosition() * 180.0 / SERVO_RANGE);
    }

    @Override
    public void start() {
    }
}
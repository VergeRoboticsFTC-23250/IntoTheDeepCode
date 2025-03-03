package org.firstinspires.ftc.teamcode.testing;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import org.firstinspires.ftc.teamcode.util.opencv.Vision;
import org.firstinspires.ftc.teamcode.util.opencv.VisionPipeline;

@TeleOp
public class CVTesting extends OpMode {
    Vision vision;

    @Override
    public void init() {
        vision = new Vision(hardwareMap, VisionPipeline.SampleColor.RED);
    }

    @Override
    public void loop() {
        telemetry.addData("isSampleVisible", vision.isSampleVisible());
        telemetry.addData("angle", vision.getAngle());
        telemetry.addData("dist", vision.getEuclideanDist());
        telemetry.update();
    }

    @Override
    public void start() {

    }
}
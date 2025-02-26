package org.firstinspires.ftc.teamcode.auto.testing;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

@TeleOp
@Config
public class LoopTesting extends OpMode {

    private long lastLoopTime = 0;
    private long loopStartTime = 0;
    private double loopHertz = 0;

    @Override
    public void init() {
        telemetry.addData("Status", "Initialized");
    }

    @Override
    public void init_loop() {
        telemetry.addData("Status", "Init Loop Running");
    }

    @Override
    public void start() {
        loopStartTime = System.nanoTime(); // Initialize timing when the op mode starts
    }

    @Override
    public void loop() {
        long currentTime = System.nanoTime();
        if (lastLoopTime != 0) {
            long loopTime = currentTime - lastLoopTime;
            double loopTimeMs = loopTime / 1e6; // Convert nanoseconds to milliseconds
            loopHertz = 1e9 / loopTime; // Convert nanoseconds to hertz

            telemetry.addData("Loop Time (ms)", loopTimeMs);
            telemetry.addData("Loop Frequency (Hz)", loopHertz);
        }
        lastLoopTime = currentTime; // Update last loop time
        telemetry.update();
    }
}

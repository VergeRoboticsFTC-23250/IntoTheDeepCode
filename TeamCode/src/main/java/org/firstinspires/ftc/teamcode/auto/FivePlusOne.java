package org.firstinspires.ftc.teamcode.auto;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.auto.assets.SpecAuto;

@Autonomous(name = "FivePlusOne", group = "spec-auto")
public class FivePlusOne extends OpMode {
    SpecAuto fiveSpec = new SpecAuto(5, true, true);

    @Override
    public void init() {
        fiveSpec.init();
    }

    @Override
    public void loop() {
        fiveSpec.loop();
    }

    @Override
    public void start() {
        fiveSpec.start();
    }
}

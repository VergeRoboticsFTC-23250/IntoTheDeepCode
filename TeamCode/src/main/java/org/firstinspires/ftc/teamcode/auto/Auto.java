package org.firstinspires.ftc.teamcode.auto;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.util.dairy.subsystems.Chassis;

import dev.frozenmilk.mercurial.Mercurial;

@Mercurial.Attach
@Chassis.Attach
@Autonomous(name = "Auto")
public class Auto extends OpMode {

    @Override
    public void init() {
        // the rest is as normal
        // remember that you can use OpModeLazyCells to init your hardware and similar
    }

    @Override
    public void init_loop() {
        // the rest is as normal
    }

    @Override
    public void start() {
        // the rest is as normal
    }

    @Override
    public void loop() {
        // the rest is as normal
    }

    @Override
    public void stop() {
        // the rest is as normal
    }
}

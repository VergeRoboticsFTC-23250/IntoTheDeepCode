package org.firstinspires.ftc.teamcode.auto;

import com.pedropathing.follower.Follower;
import com.pedropathing.follower.FollowerConstants;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import org.firstinspires.ftc.teamcode.util.pedroPathing.constants.FConstants;
import org.firstinspires.ftc.teamcode.util.pedroPathing.constants.LConstants;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp(name = "barontest")
public class baronTest extends OpMode {
    @Override
    public void init() {
        telemetry.addData("Follower Constants Localizer", FollowerConstants.localizers);
        telemetry.update();
    }
    @Override
    public void start() {
        Follower f = new Follower(hardwareMap, FConstants.class, LConstants.class);
    }
    @Override
    public void loop() {
        telemetry.addData("Follower Constants Localizer", FollowerConstants.localizers);
        telemetry.update();
    }
}
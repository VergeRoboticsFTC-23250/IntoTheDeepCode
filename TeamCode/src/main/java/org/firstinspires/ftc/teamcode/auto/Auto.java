package org.firstinspires.ftc.teamcode.auto;

import com.pedropathing.follower.Follower;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.util.dairy.Paths;
import org.firstinspires.ftc.teamcode.util.dairy.Robot;
import org.firstinspires.ftc.teamcode.util.pedroPathing.constants.FConstants;
import org.firstinspires.ftc.teamcode.util.pedroPathing.constants.LConstants;

import dev.frozenmilk.dairy.core.FeatureRegistrar;

// add feature annotations here
public class Auto extends OpMode {

    Follower follower;

    @Override
    public void init() {
        Robot.init();
        follower = new Follower(hardwareMap, FConstants.class, LConstants.class);
    }

    @Override
    public void loop() {
        follower.update();
    }

    @Override
    public void start() {
        follower.followPath(Paths.plusThreeBlueSpec.get(0));
    }
}
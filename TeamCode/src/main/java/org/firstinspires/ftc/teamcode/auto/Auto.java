package org.firstinspires.ftc.teamcode.auto;

import com.pedropathing.follower.Follower;
import com.pedropathing.localization.Pose;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.util.dairy.Paths;
import org.firstinspires.ftc.teamcode.util.dairy.Robot;
import org.firstinspires.ftc.teamcode.util.pedroPathing.constants.FConstants;
import org.firstinspires.ftc.teamcode.util.pedroPathing.constants.LConstants;

import dev.frozenmilk.dairy.core.FeatureRegistrar;


@Autonomous
public class Auto extends OpMode {

    Follower follower;
    int pathIndex = 0;

    @Override
    public void init() {
        Paths.init();
        follower = new Follower(hardwareMap, FConstants.class, LConstants.class);
        follower.setStartingPose(new Pose(9.000, 65.000, 0));
    }

    public void autoPathUpdate() {
        switch (pathIndex) {
            case 0:
                follower.followPath(Paths.plusThreeBlueSpec.get(0), true);
                pathIndex = 1;
                break;
            case 1:
                if (!follower.isBusy()) {
                    follower.followPath(Paths.plusThreeBlueSpec.get(1), true);
                    pathIndex = 2;
                }
                    break;

            case 2:
                if (!follower.isBusy()) {
                    follower.followPath(Paths.plusThreeBlueSpec.get(2), true);
                    pathIndex = 3;
                }
                    break;

            case 3:
                if (!follower.isBusy()) {
                    follower.followPath(Paths.plusThreeBlueSpec.get(3), true);
                    pathIndex = 4;
                }
                    break;

            case 4:
                if (!follower.isBusy()) {
                    follower.followPath(Paths.plusThreeBlueSpec.get(4), true);
                    pathIndex = 5;
                }
                    break;

            case 5:
                if (!follower.isBusy()) {
                    follower.followPath(Paths.plusThreeBlueSpec.get(5), true);
                    pathIndex = 6;
                }
                    break;
            case 6:
                if (!follower.isBusy()) {
                    follower.followPath(Paths.plusThreeBlueSpec.get(6), true);
                    pathIndex = 7;
                }
                    break;
            case 7:
                if (!follower.isBusy()) {
                    follower.followPath(Paths.plusThreeBlueSpec.get(7), true);
                    pathIndex = 8;
                }
                    break;
            case 8:
                if (!follower.isBusy()) {
                    follower.followPath(Paths.plusThreeBlueSpec.get(8), true);
                    pathIndex = 9;
                }
                    break;
            case 9:
                if (!follower.isBusy()) {
                    follower.followPath(Paths.plusThreeBlueSpec.get(9), true);
                    pathIndex = 10;
                }
                    break;
            case 10:
                if (!follower.isBusy()) {
                    follower.followPath(Paths.plusThreeBlueSpec.get(10), true);
                    pathIndex = 11;
                }
                    break;
            case 11:
                if (!follower.isBusy()) {
                    follower.followPath(Paths.plusThreeBlueSpec.get(11), true);
                }
                    break;
        }
    }

    @Override
    public void loop() {
        follower.update();
        autoPathUpdate();
    }

    @Override
    public void start() {
        follower.followPath(Paths.plusThreeBlueSpec.get(0),true);
    }
}
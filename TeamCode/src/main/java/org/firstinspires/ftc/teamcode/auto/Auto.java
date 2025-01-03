package org.firstinspires.ftc.teamcode.auto;

import com.pedropathing.follower.Follower;
import com.pedropathing.localization.Pose;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.util.dairy.Paths;
import org.firstinspires.ftc.teamcode.util.pedroPathing.constants.FConstants;
import org.firstinspires.ftc.teamcode.util.pedroPathing.constants.LConstants;


@Autonomous
public class Auto extends OpMode {

    Follower follower;
    int pathIndex = 0;

    @Override
    public void init() {
        Paths.init();
        follower = new Follower(hardwareMap, FConstants.class, LConstants.class);
        follower.setStartingPose(new Pose(9, 65.000, 0));
    }

    public void autoPathUpdate() {
        switch (pathIndex) {
            case 0:
                follower.followPath(Paths.plusFourSpec.get(0), true);
                pathIndex = 1;
                break;
            case 1:
                if (!follower.isBusy()) {
                    follower.followPath(Paths.plusFourSpec.get(1), true);
                    pathIndex = 2;
                }
                    break;

            case 2:
                if (!follower.isBusy()) {
                    follower.followPath(Paths.plusFourSpec.get(2), true);
                    pathIndex = 3;
                }
                    break;

            case 3:
                if (!follower.isBusy()) {
                    follower.followPath(Paths.plusFourSpec.get(3), true);
                    pathIndex = 4;
                }
                    break;

            case 4:
                if (!follower.isBusy()) {
                    follower.followPath(Paths.plusFourSpec.get(4), true);
                    pathIndex = 5;
                }
                    break;

            case 5:
                if (!follower.isBusy()) {
                    follower.followPath(Paths.plusFourSpec.get(5), true);
                    pathIndex = 6;
                }
                    break;
            case 6:
                if (!follower.isBusy()) {
                    follower.followPath(Paths.plusFourSpec.get(6), true);
                    pathIndex = 7;
                }
                    break;
            case 7:
                if (!follower.isBusy()) {
                    follower.followPath(Paths.plusFourSpec.get(7), true);
                    pathIndex = 8;
                }
                    break;
            case 8:
                if (!follower.isBusy()) {
                    follower.followPath(Paths.plusFourSpec.get(8), true);
                    pathIndex = 9;
                }
                    break;
            case 9:
                if (!follower.isBusy()) {
                    follower.followPath(Paths.plusFourSpec.get(9), true);
                    pathIndex = 10;
                }
                    break;
            case 10:
                if (!follower.isBusy()) {
                    follower.followPath(Paths.plusFourSpec.get(10), true);
                    pathIndex = 11;
                }
                    break;
            case 11:
                if (!follower.isBusy()) {
                    follower.followPath(Paths.plusFourSpec.get(11), true);
                    pathIndex = 12;
                }
                    break;
            case 12:
                if (!follower.isBusy()) {
                    follower.followPath(Paths.plusFourSpec.get(12), true);
                    pathIndex = 13;
                }
                    break;
            case 13:
                if (!follower.isBusy()) {
                    follower.followPath(Paths.plusFourSpec.get(13), true);
                    pathIndex = 14;
                }
                    break;
        }
    }

    @Override
    public void loop() {
        follower.update();
        autoPathUpdate();
        telemetry.addData("Path Index", pathIndex);
        follower.telemetryDebug(telemetry);
    }

    @Override
    public void start() {
        follower.followPath(Paths.plusFourSpec.get(0), true);
    }
}
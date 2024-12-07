package com.example.meepmeeptesting;

import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.Rotation2d;
import com.acmerobotics.roadrunner.Vector2d;
import com.noahbres.meepmeep.MeepMeep;
import com.noahbres.meepmeep.roadrunner.DefaultBotBuilder;
import com.noahbres.meepmeep.roadrunner.entity.RoadRunnerBotEntity;

public class MeepMeepTesting {
    public static void main(String[] args) {
        MeepMeep meepMeep = new MeepMeep(800);

        RoadRunnerBotEntity myBot = new DefaultBotBuilder(meepMeep)
                // Set bot constraints: maxVel, maxAccel, maxAngVel, maxAngAccel, track width
                .setConstraints(60, 60, Math.toRadians(180), Math.toRadians(180), 15)
                .build();

//        myBot.runAction(myBot.getDrive().actionBuilder(new Pose2d(-12, 61, Math.toRadians(270)))
//                .strafeTo(new Vector2d(-10, 33))
//                .lineToY(40)
//                .strafeToLinearHeading((new Vector2d(-30,38)), Math.toRadians(90))
//                .splineToConstantHeading(new Vector2d(-36, 8), 10.5)
//                .strafeTo(new Vector2d(-45, 14))
//                .strafeTo(new Vector2d(-45, 24))
//                .splineToConstantHeading(new Vector2d(-50, 14), Math.toRadians(180))
//                .strafeTo(new Vector2d(-55, 14))
//                .strafeTo(new Vector2d(-55, 24))
//                .splineToConstantHeading(new Vector2d(-58, 14), Math.toRadians(180))
//                .strafeTo(new Vector2d(-61, 14))
//                .strafeTo(new Vector2d(-61, 24))
//                .splineToLinearHeading(new Pose2d(-47, 61, Math.toRadians(270)), Math.toRadians(90))
//                .strafeToConstantHeading(new Vector2d(-6, 33))
//                .strafeToConstantHeading(new Vector2d(-47,61))
//                .strafeToConstantHeading(new Vector2d(-2, 33))
//                .strafeToConstantHeading(new Vector2d(-47,61))
//                .strafeToConstantHeading(new Vector2d(2, 33))
//                .build());

        meepMeep.setBackground(MeepMeep.Background.FIELD_INTO_THE_DEEP_OFFICIAL)
                .setDarkMode(true)
                .setBackgroundAlpha(0.95f)
                .addEntity(myBot)
                .start();
    }
}
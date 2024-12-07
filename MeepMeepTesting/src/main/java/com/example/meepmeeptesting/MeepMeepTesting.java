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
                .setDimensions(14.5,17)
                .build();

        myBot.runAction(myBot.getDrive().actionBuilder(new Pose2d(13, 60, Math.toRadians(270)))
                .strafeTo(new Vector2d(10, 33))
                        .waitSeconds(.7)
                .lineToY(40)
                .strafeToLinearHeading(new Vector2d(54,47),Math.toRadians(250))
                        .waitSeconds(1)
                .strafeToLinearHeading(new Vector2d(60,58),Math.toRadians(240))
                        .waitSeconds(.7)
                .strafeToLinearHeading(new Vector2d(58,47),Math.toRadians(270))
                        .waitSeconds(1)
                .strafeToLinearHeading(new Vector2d(60,58),Math.toRadians(250))
                        .waitSeconds(.7)
                .strafeToLinearHeading(new Vector2d(53,44),Math.toRadians(315))
                        .waitSeconds(1)
                .strafeToLinearHeading(new Vector2d(62,57),Math.toRadians(250))
                        .waitSeconds(.7)
                .build());

        meepMeep.setBackground(MeepMeep.Background.FIELD_INTO_THE_DEEP_OFFICIAL)
                .setDarkMode(true)
                .setBackgroundAlpha(0.95f)
                .addEntity(myBot)
                .start();
    }
}
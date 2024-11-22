package org.firstinspires.ftc.teamcode.util.ftclib.subsystems;

import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.PoseVelocity2d;
import com.acmerobotics.roadrunner.Vector2d;
import com.arcrobotics.ftclib.command.SubsystemBase;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.util.roadrunner.MecanumDrive;

public class Chassis extends SubsystemBase {
    MecanumDrive drive;

    public Chassis(HardwareMap hMap) {
        drive = new MecanumDrive(hMap, new Pose2d(0,0,0));
    }

    public void drive(double x, double y, double z) {
        drive.setDrivePowers(new PoseVelocity2d(
                new Vector2d(x, y),
                z
        ));
    }
}

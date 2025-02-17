package org.firstinspires.ftc.teamcode.util;

public class Util {
    public static double shortestAngleDistance(double theta1, double theta2) {
        return Math.atan2(Math.sin(theta2 - theta1), Math.cos(theta2 - theta1));
    }
}

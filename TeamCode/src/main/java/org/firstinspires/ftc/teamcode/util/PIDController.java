package org.firstinspires.ftc.teamcode.util;

import com.qualcomm.robotcore.hardware.PIDCoefficients;
import com.qualcomm.robotcore.util.ElapsedTime;

public class PIDController {
    double Kp;
    double Ki;
    double Kd;

    double reference;
    double measured;
    double tolerance;

    double integralSum = 0;

    double lastError = 0;

    ElapsedTime timer;
    double error;

    public PIDController(double Kp, double Ki, double Kd, double reference){
        this.Kp = Kp;
        this.Ki = Ki;
        this.Kd = Kd;
        this.reference = reference;
        timer = new ElapsedTime();
    }
    public PIDController(PIDCoefficients coefficients, double reference){
        this.Kp = coefficients.p;
        this.Ki = coefficients.i;
        this.Kd = coefficients.d;
        this.reference = reference;
        timer = new ElapsedTime();
    }
    public void reset(){
        timer.reset();
    }

    public double getPowerHeading(double measured){
        this.measured = measured;
        error = Util.shortestAngleDistance(reference, measured);

        // rate of change of the error
        double derivative = (error - lastError) / timer.seconds();

        // sum of all error over time
        integralSum = integralSum + (error * timer.seconds());

        double out = (Kp * error) + (Ki * integralSum) + (Kd * derivative);

        lastError = error;

        // reset the timer for next time
        timer.reset();

        return out;
    }
    public double getPower(double measured){
        this.measured = measured;
        error = reference - measured;

        // rate of change of the error
        double derivative = (error - lastError) / timer.seconds();

        // sum of all error over time
        integralSum = integralSum + (error * timer.seconds());

        double out = (Kp * error) + (Ki * integralSum) + (Kd * derivative);

        lastError = error;

        // reset the timer for next time
        timer.reset();

        return out;
    }
    public void setGains(double Kp, double Ki, double Kd){
        this.Kp = Kp;
        this.Ki = Ki;
        this.Kd = Kd;
    }

    public void setReference(double reference) {
        this.reference = reference;
    }

    public boolean isAtReference(){
        return Math.abs(error) < tolerance;
    }
    public void setTolerance(double tolerance){
        this.tolerance = tolerance;
    }

    public double getError(){
        return error;
    }
}

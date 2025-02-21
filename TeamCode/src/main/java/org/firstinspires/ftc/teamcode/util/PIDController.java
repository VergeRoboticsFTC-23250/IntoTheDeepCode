package org.firstinspires.ftc.teamcode.util;

import com.qualcomm.robotcore.hardware.PIDCoefficients;
import com.qualcomm.robotcore.util.ElapsedTime;

public class PIDController {
    private double Kp;
    private double Ki;
    private double Kd;

    private double reference;
    private double measured;
    private double tolerance = 0.05; // Default tolerance

    private double integralSum = 0;
    private double lastError = 0;
    private double maxIntegralSum = 1.0; // Default max integral sum to prevent windup
    private ElapsedTime timer;
    private double error;

    public PIDController(double Kp, double Ki, double Kd, double reference) {
        this.Kp = Kp;
        this.Ki = Ki;
        this.Kd = Kd;
        this.reference = reference;
        timer = new ElapsedTime();
    }
    public PIDController(PIDCoefficients gains, double reference) {
        this.Kp = gains.p;
        this.Ki = gains.i;
        this.Kd = gains.d;
        this.reference = reference;
        timer = new ElapsedTime();
    }

    public void reset() {
        timer.reset();
        integralSum = 0;
        lastError = 0;
    }

    public double getPowerHeading(double measured) {
        this.measured = measured;
        error = Util.shortestAngleDistance(measured, reference);
        return calculateOutput();
    }

    public double getPower(double measured) {
        this.measured = measured;
        error = reference - measured;
        return calculateOutput();
    }

    // Add near other private variables at the top
    private double integralDecayFactor = 0.95; // Default decay factor

    private double derivativeFiltered = 0; // Stores the filtered derivative
    private double derivativeFilterAlpha = 1; // Default smoothing factor (0 = no derivative, 1 = raw derivative)

    private double calculateOutput() {
        double dt = timer.seconds();

        // Derivative (with exponential moving average filter)
        double rawDerivative = dt > 0 ? (error - lastError) / dt : 0;
        derivativeFiltered = (derivativeFilterAlpha * rawDerivative) + ((1 - derivativeFilterAlpha) * derivativeFiltered);

        // Integral with anti-windup and decay
        if (Math.abs(error) < tolerance * 2) {  // Start decay when near target
            integralSum *= integralDecayFactor;
        } else {
            integralSum += error * dt;
        }
        integralSum = Math.max(-maxIntegralSum, Math.min(maxIntegralSum, integralSum));

        // Calculate output
        double out = (Kp * error) + (Ki * integralSum) + (Kd * derivativeFiltered);

        lastError = error;
        timer.reset();

        return out;
    }

    public void setDerivativeFilterAlpha(double alpha) {
        // Ensure alpha is between 0 (no D term) and 1 (raw D term)
        this.derivativeFilterAlpha = Math.max(0, Math.min(1, alpha));
    }

    // Add with other configuration methods
    public void setIntegralDecayFactor(double decayFactor) {
        // Ensure decay factor is between 0 and 1
        this.integralDecayFactor = Math.max(0, Math.min(1, decayFactor));
    }

    // Configuration methods
    public void setMaxIntegralSum(double max) {
        this.maxIntegralSum = Math.abs(max);
    }

    public void setGains(double Kp, double Ki, double Kd) {
        this.Kp = Kp;
        this.Ki = Ki;
        this.Kd = Kd;
        reset(); // Reset when gains change
    }

    // Getter methods for debugging
    public double getProportionalTerm() {
        return Kp * error;
    }

    public double getIntegralTerm() {
        return Ki * integralSum;
    }

    public double getDerivativeTerm() {
        return Kd * (error - lastError) / timer.seconds();
    }

    public void setReference(double reference) {
        this.reference = reference;
    }

    public boolean isAtReference() {
        return Math.abs(error) < tolerance;
    }

    public void setTolerance(double tolerance) {
        this.tolerance = tolerance;
    }

    public double getError() {
        return error;
    }

    // Add getter for integral sum debugging
    public double getIntegralSum() {
        return integralSum;
    }

    public double getReference(){
        return reference;
    }
}
package org.firstinspires.ftc.teamcode.util.ftclib.subsystems;

import com.arcrobotics.ftclib.command.SubsystemBase;
import com.arcrobotics.ftclib.hardware.motors.MotorEx;
import com.arcrobotics.ftclib.hardware.motors.MotorGroup;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class IntakeSlides extends SubsystemBase {
    MotorGroup slides;
    Telemetry telemetry;

    public IntakeSlides(final HardwareMap hMap, final Telemetry telemetry){
        slides = new MotorGroup(
                hMap.get(MotorEx.class, "intakeSR"),
                hMap.get(MotorEx.class, "intakeSL")
        );
    }

    public void logPos(){
        telemetry.addData("Slide R Position", slides.getPositions().get(0));
        telemetry.addData("Slide L Position", slides.getPositions().get(1));
        telemetry.addData("Slide Pos", slides.getCurrentPosition());
        telemetry.update();
    }
}

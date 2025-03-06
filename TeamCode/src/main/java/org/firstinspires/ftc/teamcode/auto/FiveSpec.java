package org.firstinspires.ftc.teamcode.auto;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.auto.assets.SpecAuto;
import org.firstinspires.ftc.teamcode.util.dairy.Robot;
import org.firstinspires.ftc.teamcode.util.dairy.subsystems.Chassis;
import org.firstinspires.ftc.teamcode.util.dairy.subsystems.intake.Differential;
import org.firstinspires.ftc.teamcode.util.dairy.subsystems.intake.IntakeClaw;
import org.firstinspires.ftc.teamcode.util.dairy.subsystems.intake.IntakeDropDown;
import org.firstinspires.ftc.teamcode.util.dairy.subsystems.intake.IntakeSlides;
import org.firstinspires.ftc.teamcode.util.dairy.subsystems.outtake.OuttakeArm;
import org.firstinspires.ftc.teamcode.util.dairy.subsystems.outtake.OuttakeClaw;
import org.firstinspires.ftc.teamcode.util.dairy.subsystems.outtake.OuttakePivot;
import org.firstinspires.ftc.teamcode.util.dairy.subsystems.outtake.OuttakeSlides;

import dev.frozenmilk.dairy.core.util.features.BulkRead;
import dev.frozenmilk.mercurial.Mercurial;

@Mercurial.Attach
@Chassis.Attach
@OuttakeSlides.Attach
@OuttakeClaw.Attach
@OuttakePivot.Attach
@OuttakeArm.Attach
@IntakeDropDown.Attach
@IntakeClaw.Attach
@Differential.Attach
@IntakeSlides.Attach
@BulkRead.Attach
@Autonomous
public class FiveSpec extends OpMode {
    SpecAuto fiveSpec;

    @Override
    public void init() {
        Robot.init(hardwareMap);
        fiveSpec = new SpecAuto(hardwareMap, false, false);
    }

    @Override
    public void loop() {
        fiveSpec.loop();
    }

    @Override
    public void start() {
        fiveSpec.start();
    }
}

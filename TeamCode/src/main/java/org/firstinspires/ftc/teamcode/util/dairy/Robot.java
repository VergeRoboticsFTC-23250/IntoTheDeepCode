package org.firstinspires.ftc.teamcode.util.dairy;

import org.firstinspires.ftc.robotcore.internal.opmode.OpModeMeta;
import org.firstinspires.ftc.teamcode.util.Util.StatePositions;
import org.firstinspires.ftc.teamcode.util.dairy.subsystems.intake.Differential.IntakeWrist;
import org.firstinspires.ftc.teamcode.util.dairy.subsystems.intake.Differential.IntakePivot;
import org.firstinspires.ftc.teamcode.util.dairy.subsystems.intake.IntakeClaw;
import org.firstinspires.ftc.teamcode.util.dairy.subsystems.intake.IntakeDropDown;
import org.firstinspires.ftc.teamcode.util.dairy.subsystems.intake.IntakeSlides;
import org.firstinspires.ftc.teamcode.util.dairy.subsystems.outtake.OuttakeArm;
import org.firstinspires.ftc.teamcode.util.dairy.subsystems.outtake.OuttakeClaw;
import org.firstinspires.ftc.teamcode.util.dairy.subsystems.outtake.OuttakePivot;
import org.firstinspires.ftc.teamcode.util.dairy.subsystems.outtake.OuttakeSlides;
import org.firstinspires.ftc.teamcode.util.opencv.YellowAnglePipeline;
import org.openftc.easyopencv.OpenCvWebcam;

import java.util.Map;

import dev.frozenmilk.dairy.core.FeatureRegistrar;
import dev.frozenmilk.mercurial.commands.Command;
import dev.frozenmilk.mercurial.commands.groups.Parallel;
import dev.frozenmilk.mercurial.commands.groups.Sequential;
import dev.frozenmilk.mercurial.commands.util.IfElse;

public class Robot {

    public static YellowAnglePipeline pipeline;
    public static OpenCvWebcam webcam;
    public enum State {
        INIT,
        HOME,
        INTAKE_GROUND,
        INTAKE_BACK,
        OUTTAKE_BACK,
        OUTTAKE_FRONT,
        BUCKET,
        DROP_SAMPLE,
        PUSH_SAMPLE,
        CAMERA,
    }

    static StatePositions init = new StatePositions(
            OuttakeSlides.init,
            OuttakeArm.init,
            OuttakePivot.init,
            IntakeDropDown.init,
            IntakePivot.init,
            IntakeWrist.init,
            true,
            false,
            false
    );

    static StatePositions home = new StatePositions(
            OuttakeSlides.home,
            OuttakeArm.home,
            OuttakePivot.home,
            IntakeDropDown.home,
            IntakePivot.home,
            IntakeWrist.home,
            false,
            true,
            false
    );

    static StatePositions intakeGround = new StatePositions(
            OuttakeSlides.home,
            OuttakeArm.home,
            OuttakePivot.home,
            IntakeDropDown.intake,
            IntakePivot.intake,
            IntakeWrist.intake,
            true,
            true,
            true
    );

    static StatePositions intakeBack = new StatePositions(
            OuttakeSlides.home,
            OuttakeArm.intakeBack,
            OuttakePivot.intakeBack,
            IntakeDropDown.home,
            IntakePivot.home,
            IntakeWrist.home,
            true,
            true,
            false
    );

    static StatePositions outtakeBack = new StatePositions(
            OuttakeSlides.outtakeBack,
            OuttakeArm.outtakeBack,
            OuttakePivot.outtakeBack,
            IntakeDropDown.home,
            IntakePivot.home,
            IntakeWrist.home,
            true,
            false,
            true
    );

    static StatePositions outtakeFront = new StatePositions(
            OuttakeSlides.outtakeFront,
            OuttakeArm.outtakeFront,
            OuttakePivot.outtakeFront,
            IntakeDropDown.home,
            IntakePivot.home,
            IntakeWrist.home,
            true,
            false,
            true
    );

    static StatePositions bucket = new StatePositions(
            OuttakeSlides.bucket,
            OuttakeArm.bucket,
            OuttakePivot.bucket,
            IntakeDropDown.home,
            IntakePivot.home,
            IntakeWrist.home,
            true,
            false,
            true
    );

    static StatePositions dropSample = new StatePositions(
            OuttakeSlides.home,
            OuttakeArm.dropSamp,
            OuttakePivot.dropSamp,
            IntakeDropDown.home,
            IntakePivot.home,
            IntakeWrist.home,
            true,
            false,
            true
    );

    static StatePositions pushSample = new StatePositions(
            OuttakeSlides.home,
            OuttakeArm.home,
            OuttakePivot.home,
            IntakeDropDown.pushSamp,
            IntakePivot.pushSamp,
            IntakeWrist.pushSamp,
            false,
            true,
            true
    );

    static StatePositions camera = new StatePositions(
            OuttakeSlides.home,
            OuttakeArm.home,
            OuttakePivot.home,
            IntakeDropDown.camera,
            IntakePivot.camera,
            IntakeWrist.camera,
            false,
            true,
            true
    );

    static Map<State, StatePositions> states = Map.of(
            State.INIT, init,
            State.HOME, home,
            State.INTAKE_GROUND, intakeGround,
            State.INTAKE_BACK, intakeBack,
            State.OUTTAKE_BACK, outtakeBack,
            State.OUTTAKE_FRONT, outtakeFront,
            State.BUCKET, bucket,
            State.DROP_SAMPLE, dropSample,
            State.PUSH_SAMPLE, pushSample,
            State.CAMERA, camera
    );

    public static OpModeMeta.Flavor flavor;

    public static void init() {
        flavor = FeatureRegistrar.getActiveOpModeWrapper().getOpModeType();
    }

    public static Command setState(State state) {
        StatePositions s = states.get(state);
        if(s == null){
            return new Sequential();
        }
        return new Parallel(
                OuttakeSlides.runToPosition(s.outtakeSlides),
                OuttakeArm.setPos(s.outtakeArm), //0 is home
                OuttakePivot.setPos(s.outtakePivot), //0 is home
                IntakeDropDown.setPos(s.intakeDropDown), //0 is intake pos
//                IntakePivot.setPos(s.intakePivot),
//                IntakeWrist.setPos(s.intakeWrist),
                new IfElse(
                        () -> s.isIntakeClawOpen,
                        IntakeClaw.open(),
                        IntakeClaw.closeLoose()
                ),

                new IfElse(
                        () -> s.isOuttakeClawOpen,
                        OuttakeClaw.open(),
                        OuttakeClaw.closeLoose()
                )

//                new IfElse(
//                        () -> s.areIntakeSlidesExtended,
//                        IntakeSlides.extend(),
//                        IntakeSlides.retract()
//                )
        );
    }
}

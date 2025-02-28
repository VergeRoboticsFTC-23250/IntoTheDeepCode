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

import java.util.Map;

import dev.frozenmilk.dairy.core.FeatureRegistrar;
import dev.frozenmilk.mercurial.commands.Command;
import dev.frozenmilk.mercurial.commands.Lambda;
import dev.frozenmilk.mercurial.commands.groups.Parallel;
import dev.frozenmilk.mercurial.commands.groups.Sequential;
import dev.frozenmilk.mercurial.commands.util.IfElse;
import dev.frozenmilk.mercurial.commands.util.Wait;

public class Robot {

    private static State currentState = State.INIT;
    private static boolean secondary = false;
    public static void setCurrentState(State st){
        currentState = st;
    }

    public static State getCurrentState(){
        return currentState;
    }

    public enum State {
        INIT,
        HOME,
        INTAKE_GROUND,
        OUTTAKE_GROUND,
        INTAKE_GROUND_SECONDARY,
        INTAKE_BACK,
        OUTTAKE_BACK,
        OUTTAKE_BACK_SECONDARY,
        OUTTAKE_FRONT,
        OUTTAKE_FRONT_SECONDARY,
        BUCKET,
        DROP_SAMPLE,
        PUSH_SAMPLE,
        CAMERA,
        TELEOP_TRANSFER
    }

    static StatePositions camera, pushSample, dropSample, bucket, outtakeFront, outtakeFrontSecondary, outtakeBack, outtakeBackSecondary, intakeBack, intakeGround, intakeGroundSecondary, home, init, teleopTransfer, outtakeGround;

    static Map<State, StatePositions> states;

    public static boolean isAuto;

    public static void init() {
        isAuto = FeatureRegistrar.getActiveOpModeWrapper().getOpModeType() == OpModeMeta.Flavor.AUTONOMOUS;

        home = new StatePositions(
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

        teleopTransfer = new StatePositions(
                OuttakeSlides.home,
                OuttakeArm.teleopTransfer,
                OuttakePivot.teleopTransfer,
                IntakeDropDown.home,
                IntakePivot.home,
                IntakeWrist.home,
                false,
                true,
                false
        );

        init = new StatePositions(
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

        camera = new StatePositions(
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

        pushSample = new StatePositions(
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

        dropSample = new StatePositions(
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

        bucket = new StatePositions(
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

        outtakeFront = new StatePositions(
                OuttakeSlides.outtakeFront,
                OuttakeArm.outtakeFront,
                OuttakePivot.outtakeFront,
                IntakeDropDown.homeSafe,
                IntakePivot.home,
                IntakeWrist.home,
                true,
                false,
                false
        );

        outtakeFrontSecondary = new StatePositions(
                OuttakeSlides.outtakeFront + OuttakeSlides.scoreOffset,
                OuttakeArm.outtakeFront,
                OuttakePivot.outtakeFront,
                IntakeDropDown.homeSafe,
                IntakePivot.home,
                IntakeWrist.home,
                true,
                false,
                false
        );

        outtakeBack = new StatePositions(
                OuttakeSlides.outtakeBack,
                OuttakeArm.home,
                OuttakePivot.homeOuttakeBack,
                IntakeDropDown.homeOuttakeBack,
                IntakePivot.homeOuttakeBack,
                IntakeWrist.home,
                true,
                false,
                false
        );

        outtakeBackSecondary = new StatePositions(
                OuttakeSlides.outtakeBack,
                OuttakeArm.outtakeBack,
                OuttakePivot.outtakeBack,
                IntakeDropDown.homeOuttakeBack,
                IntakePivot.homeOuttakeBack,
                IntakeWrist.home,
                true,
                false,
                false
        );

        intakeBack = new StatePositions(
                OuttakeSlides.home,
                OuttakeArm.intakeBack,
                OuttakePivot.intakeBack,
                IntakeDropDown.homeSafe,
                IntakePivot.home,
                IntakeWrist.home,
                true,
                true,
                false
        );

        intakeGround = new StatePositions(
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

        outtakeGround = new StatePositions(
                OuttakeSlides.home,
                OuttakeArm.home,
                OuttakePivot.home,
                IntakeDropDown.intake,
                IntakePivot.intake,
                IntakeWrist.intake,
                false,
                true,
                true
        );

        intakeGroundSecondary = new StatePositions(
                OuttakeSlides.home,
                OuttakeArm.home,
                OuttakePivot.home,
                IntakeDropDown.intakeGroundSecondary,
                IntakePivot.intakeGroundSecondary,
                IntakeWrist.intake,
                true,
                true,
                true
        );

        states = Map.ofEntries(
                Map.entry(State.INIT, init),
                Map.entry(State.HOME, home),
                Map.entry(State.TELEOP_TRANSFER, teleopTransfer),
                Map.entry(State.INTAKE_GROUND, intakeGround),
                Map.entry(State.INTAKE_GROUND_SECONDARY, intakeGroundSecondary),
                Map.entry(State.INTAKE_BACK, intakeBack),
                Map.entry(State.OUTTAKE_BACK, outtakeBack),
                Map.entry(State.OUTTAKE_BACK_SECONDARY, outtakeBackSecondary),
                Map.entry(State.OUTTAKE_FRONT, outtakeFront),
                Map.entry(State.OUTTAKE_FRONT_SECONDARY, outtakeFrontSecondary),
                Map.entry(State.BUCKET, bucket),
                Map.entry(State.DROP_SAMPLE, dropSample),
                Map.entry(State.PUSH_SAMPLE, pushSample),
                Map.entry(State.CAMERA, camera),
                Map.entry(State.OUTTAKE_GROUND, outtakeGround)
        );
    }

    public static Command setState(State st) {
        StatePositions s = states.get(st);

        return new Parallel(
                OuttakeSlides.runToPosition(s.outtakeSlides),
                OuttakeArm.setPos(s.outtakeArm), //0 is home
                OuttakePivot.setPos(s.outtakePivot), //0 is home
                IntakePivot.setPos(s.intakePivot),
                new IfElse(
                        () -> Robot.getCurrentState() == State.INTAKE_GROUND,
                        new Parallel(),
                        IntakeWrist.setPos(s.intakeWrist)
                ),
                new IfElse(
                        () -> Robot.getCurrentState() == State.INTAKE_GROUND_SECONDARY,
                        IntakeDropDown.setPos(IntakeDropDown.intake),
                        IntakeDropDown.setPos(s.intakeDropDown)
                ),
                new IfElse(
                        () -> s.isIntakeClawOpen,
                        IntakeClaw.open(),
                        IntakeClaw.closeLoose()
                ),

                new IfElse(
                        () -> s.isOuttakeClawOpen,
                        OuttakeClaw.open(),
                        OuttakeClaw.closeLoose()
                ),
                new IfElse(
                        () -> s.areIntakeSlidesExtended,
                        IntakeSlides.extend(),
                        IntakeSlides.retract()
                ),
                new Lambda("setCurrentState").setInit(() -> Robot.setCurrentState(st))
        );
    }

    public static Lambda manipulate(){
        return new Lambda("manipulate")
                .setInit(() -> {
                    if(Robot.getCurrentState() == State.OUTTAKE_FRONT){
                        Robot.setState(State.OUTTAKE_FRONT_SECONDARY).schedule();
                    }else if(Robot.getCurrentState() == State.OUTTAKE_FRONT_SECONDARY){
                        new Sequential(
                                OuttakeClaw.open(),
                                Robot.setState(State.INTAKE_BACK)
                        ).schedule();
                    }else if(Robot.getCurrentState() == State.INTAKE_GROUND){
                        new Sequential(
                                Robot.setState(State.INTAKE_GROUND_SECONDARY),
                                new Wait(0.125),
                                IntakeClaw.closeFirm(),
                                new Wait(0.125),
                                Robot.setState(State.HOME)
                        ).schedule();
                    }else if(Robot.getCurrentState() == State.INTAKE_GROUND_SECONDARY){
                        IntakeClaw.closeFirm().schedule();
                    }else if(Robot.getCurrentState() == State.HOME){
                        Robot.setState(Robot.State.TELEOP_TRANSFER).schedule();
                    }else if(Robot.getCurrentState() == State.TELEOP_TRANSFER){
                        new Parallel(
                                OuttakeClaw.closeFirm(),
                                IntakeClaw.open()
                        ).schedule();
                    }else if(Robot.getCurrentState() == State.OUTTAKE_BACK){
                        Robot.setState(State.OUTTAKE_BACK_SECONDARY).schedule();
                    }else if(Robot.getCurrentState() == State.OUTTAKE_BACK_SECONDARY){
                        OuttakeClaw.open().schedule();
                    }else if(Robot.getCurrentState() == State.INTAKE_BACK){
                        Robot.setState(State.OUTTAKE_FRONT).schedule();
                    }
                });
    }
}

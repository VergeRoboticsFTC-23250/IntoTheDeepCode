package org.firstinspires.ftc.teamcode.util.dairy;

import com.arcrobotics.ftclib.command.WaitCommand;
import com.pedropathing.pathgen.PathBuilder;

import org.firstinspires.ftc.robotcore.internal.opmode.OpModeMeta;
import org.firstinspires.ftc.teamcode.util.dairy.subsystems.IntakeSlides;
import org.firstinspires.ftc.teamcode.util.dairy.subsystems.Outtake;
import org.firstinspires.ftc.teamcode.util.dairy.subsystems.OuttakeSlides;

import java.util.Map;

import dev.frozenmilk.dairy.core.FeatureRegistrar;
import dev.frozenmilk.mercurial.commands.Lambda;
import dev.frozenmilk.mercurial.commands.groups.Parallel;
import dev.frozenmilk.mercurial.commands.groups.Sequential;
import dev.frozenmilk.mercurial.commands.util.StateMachine;
import dev.frozenmilk.mercurial.commands.util.Wait;

public class Robot {

    public static volatile State currentState = State.HOME;
    public enum State {
        HOME,
        INTAKE_SPEC,
        OUTTAKE_SUBMERSIBLE,
        OUTTAKE_SUBMERSIBLE_SCORE,
        OUTTAKE_BUCKET,
        TRANSFER,
        OUTTAKE_SPEC
    }

    public static StateMachine<State> stateMachine;

    public static OpModeMeta.Flavor flavor;

    static PathBuilder plusThreeBlueSpec = new PathBuilder();

    public static void init() {

        StatePositions init = new StatePositions(
                Outtake.armHomePos,
                Outtake.pivotHomePos,
                false,
                OuttakeSlides.minPos
        );
        StatePositions home = new StatePositions(
                Outtake.armHomePos,
                Outtake.pivotHomePos,
                true,
                OuttakeSlides.minPos
        );
        StatePositions intake = new StatePositions(
                Outtake.armSpecPos,
                Outtake.pivotSpecPos,
                true,
                OuttakeSlides.minPos
        );
        StatePositions outtakeSubmersible = new StatePositions(
                Outtake.armSubmersiblePos,
                Outtake.pivotSubmersiblePos,
                false,
                OuttakeSlides.submirsiblePos
        );
        StatePositions outtakeSubmersibleScore = new StatePositions(
                Outtake.armSubmersiblePos,
                Outtake.pivotSubmersiblePos,
                false,
                OuttakeSlides.scoreSubmersiblePos
        );
        StatePositions outtakeBucket = new StatePositions(
                Outtake.armBucketPos,
                Outtake.pivotBucketPos,
                false,
                OuttakeSlides.maxPos
        );
        StatePositions transfer = new StatePositions(
                Outtake.armTransferPos,
                Outtake.pivotTranferPos,
                false,
                OuttakeSlides.safePos
        );

        Map<State, StatePositions> states = Map.of(
                State.HOME, home,
                State.INTAKE_SPEC, intake,
                State.OUTTAKE_SUBMERSIBLE, outtakeSubmersible,
                State.OUTTAKE_SUBMERSIBLE_SCORE, outtakeSubmersibleScore,
                State.OUTTAKE_BUCKET, outtakeBucket,
                State.TRANSFER, transfer
        );

        flavor = FeatureRegistrar.getActiveOpModeWrapper().getOpModeType();

        stateMachine = new StateMachine<>(State.HOME)
                .withState(State.HOME, (state, name) -> Lambda.from(
                        new Sequential(
                                Outtake.openClaw(),
                                Outtake.setArm(home.armPos),
                                Outtake.setPivot(home.pivotPos),
                                OuttakeSlides.runToPosition(OuttakeSlides.minPos)
                        )
                ))
                .withState(State.INTAKE_SPEC, (state, name) -> Lambda.from(
                        new Sequential(
                                Outtake.openClaw(),
                                Outtake.setArm(intake.armPos),
                                Outtake.setPivot(intake.pivotPos),
                                new Wait(0.5),
                                OuttakeSlides.runToPosition(OuttakeSlides.minPos)

                        )
                ))
                .withState(State.OUTTAKE_SUBMERSIBLE, (state, name) -> Lambda.from(
                        new Sequential(
                                Outtake.closeClaw(),
                                OuttakeSlides.runToPosition(outtakeSubmersible.slidePos),
                                Outtake.setPivot(outtakeSubmersible.pivotPos),
                                Outtake.setArm(outtakeSubmersible.armPos)
                        )
                ))
                .withState(State.OUTTAKE_SUBMERSIBLE_SCORE, (state, name) -> Lambda.from(
                        new Sequential(
                                OuttakeSlides.runToPosition(outtakeSubmersibleScore.slidePos)
                        )
                ))
//                .withState(State.OUTTAKE_BUCKET, (state, name) -> Lambda.from(
//                        new Sequential(
//                                Outtake.closeClaw().with(new Wait(0.4)),
//                                OuttakeSlides.runToPosition(outtakeBucket.slidePos).raceWith(new Wait(0.5)),
//                                new Parallel(
//                                        Outtake.setArm(outtakeBucket.armPos).with(new Wait(0.5)),
//                                        Outtake.setPivot(outtakeBucket.pivotPos).with(new Wait(0.5))
//                                )
//                        )
//                ))
                .withState(State.TRANSFER, (state, name) -> Lambda.from(
                        new Sequential(
                                Outtake.setArm(transfer.armPos),
                                Outtake.setPivot(transfer.pivotPos).with(new Wait(.25)),
                                Outtake.closeClaw().with(new Wait(0.25)),
                                OuttakeSlides.runToPosition(transfer.slidePos)
                        )
                ))
                .withState(State.OUTTAKE_SPEC, (state, name) -> Lambda.from(
                        new Sequential(
                                Outtake.closeClaw(),
                                OuttakeSlides.runToPosition(OuttakeSlides.safePos),
                                Outtake.setArm(Outtake.armOuttakeSpec),
                                Outtake.setPivot(Outtake.pivotOuttakeSpec)
                        )
                ));

        Paths.init();
    }

    public static Lambda setState(State state) {
        return new Lambda("set-state")
                .setInit(() -> {
                    if (!stateMachine.getState().equals(State.OUTTAKE_BUCKET) || stateMachine.getState().equals(State.TRANSFER)) {
                        stateMachine.schedule(state);
                        FeatureRegistrar.getActiveOpMode().telemetry.addData("State", state);
                        FeatureRegistrar.getActiveOpMode().telemetry.update();
                    }

                })
                .setFinish(() -> true);
    }

    public static Lambda manipulate() {
        return new Lambda("manipulate")
                .setInit(() -> {
                    if (stateMachine.getState().equals(Robot.State.OUTTAKE_SUBMERSIBLE)) {
                        stateMachine.schedule(State.OUTTAKE_SUBMERSIBLE_SCORE);
                    } else if (stateMachine.getState().equals(State.HOME)) {
                        new Sequential(
                                Robot.setState(State.TRANSFER),
                                Robot.setState(State.OUTTAKE_SPEC)
                        ).schedule();
                    }
                    else {
                        Outtake.toggleThing().schedule();
                    }
                });
    }
}

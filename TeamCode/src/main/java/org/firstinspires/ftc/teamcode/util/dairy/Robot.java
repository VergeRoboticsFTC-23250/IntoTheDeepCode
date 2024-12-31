package org.firstinspires.ftc.teamcode.util.dairy;

import com.pedropathing.pathgen.PathBuilder;

import org.firstinspires.ftc.robotcore.internal.opmode.OpModeMeta;
import org.firstinspires.ftc.teamcode.util.dairy.subsystems.Chassis;
import org.firstinspires.ftc.teamcode.util.dairy.subsystems.IntakeSlides;
import org.firstinspires.ftc.teamcode.util.dairy.subsystems.Outtake;
import org.firstinspires.ftc.teamcode.util.dairy.subsystems.OuttakeSlides;

import java.util.Map;
import java.util.Objects;

import dev.frozenmilk.dairy.core.FeatureRegistrar;
import dev.frozenmilk.mercurial.commands.Lambda;
import dev.frozenmilk.mercurial.commands.groups.Parallel;
import dev.frozenmilk.mercurial.commands.groups.Sequential;
import dev.frozenmilk.mercurial.commands.util.StateMachine;
import dev.frozenmilk.mercurial.commands.util.Wait;

public class Robot {

    public static volatile State currentState = State.INIT;
    public enum State {
        INIT,
        HOME,
        INTAKE,
        OUTTAKE_SUBMERSIBLE,
        OUTTAKE_SUBMERSIBLE_SCORE,
        OUTTAKE_BUCKET,
        TRANSFER
    }

    public static StatePositions init = new StatePositions(
            Outtake.armHomePos,
            Outtake.pivotHomePos,
            false,
            IntakeSlides.minPos
    );
    public static StatePositions home = new StatePositions(
            Outtake.armHomePos,
            Outtake.pivotHomePos,
            true,
            IntakeSlides.minPos
    );
    public static StatePositions intake = new StatePositions(
            Outtake.armSpecPos,
            Outtake.pivotSpecPos,
            true,
            IntakeSlides.minPos
    );
    public static StatePositions outtakeSubmersible = new StatePositions(
            Outtake.armSubmersiblePos,
            Outtake.pivotSubmersiblePos,
            false,
            OuttakeSlides.submirsiblePos
    );
    public static StatePositions outtakeSubmersibleScore = new StatePositions(
            Outtake.armSubmersiblePos,
            Outtake.pivotSubmersiblePos,
            false,
            OuttakeSlides.scoreSubmersiblePos
    );
    public static StatePositions outtakeBucket = new StatePositions(
            Outtake.armBucketPos,
            Outtake.pivotBucketPos,
            false,
            OuttakeSlides.maxPos
    );
    public static StatePositions transfer = new StatePositions(
            Outtake.armTransferPos,
            Outtake.pivotTranferPos,
            false,
            OuttakeSlides.minPos
    );

    public static Map<State, StatePositions> states = Map.of(
            State.INIT, init,
            State.HOME, home,
            State.INTAKE, intake,
            State.OUTTAKE_SUBMERSIBLE, outtakeSubmersible,
            State.OUTTAKE_SUBMERSIBLE_SCORE, outtakeSubmersibleScore,
            State.OUTTAKE_BUCKET, outtakeBucket,
            State.TRANSFER, transfer
    );

    public static StateMachine<State> stateMachine;

    public static OpModeMeta.Flavor flavor;

    static PathBuilder plusThreeBlueSpec = new PathBuilder();

    public static void init() {
        flavor = FeatureRegistrar.getActiveOpModeWrapper().getOpModeType();

        stateMachine = new StateMachine<>(State.INIT)
                .withState(State.INIT, (state, name) -> Lambda.from(
                        new Sequential(
                                Outtake.setArm(Objects.requireNonNull(states.get(state)).armPos).with(new Wait(0.5)),
                                Outtake.setPivot(Objects.requireNonNull(states.get(state)).pivotPos).with(new Wait(0.5)),
                                Outtake.closeClaw().with(new Wait(0.5)),
                                OuttakeSlides.home(),
                                IntakeSlides.home()
                        )
                ))
                .withState(State.HOME, (state, name) -> Lambda.from(
                        new Sequential(
                                Outtake.openClaw().with(new Wait(0.5)),
                                Outtake.setArm(states.get(state).armPos).with(new Wait(0.5)),
                                Outtake.setPivot(states.get(state).pivotPos).with(new Wait(0.5)),
                                OuttakeSlides.home(),
                                IntakeSlides.home()
                        )
                ))
                .withState(State.INTAKE, (state, name) -> Lambda.from(
                        new Sequential(
                                Outtake.setArm(states.get(state).armPos).with(new Wait(0.5)),
                                Outtake.setPivot(states.get(state).pivotPos).with(new Wait(0.5)),
                                Outtake.openClaw().with(new Wait(0.4)),
                                OuttakeSlides.home(),
                                IntakeSlides.home()
                        )
                ))
                .withState(State.OUTTAKE_SUBMERSIBLE, (state, name) -> Lambda.from(
                        new Sequential(
                                Outtake.closeClaw().with(new Wait(0.4)),
                                OuttakeSlides.runToPosition(states.get(state).slidePos).raceWith(new Wait(0.5)),
                                new Parallel(
                                        Outtake.setArm(states.get(state).armPos).with(new Wait(0.5)),
                                        Outtake.setPivot(states.get(state).pivotPos).with(new Wait(0.5))
                                )
                        )

                ))
                .withState(State.OUTTAKE_SUBMERSIBLE_SCORE, (state, name) -> Lambda.from(
                        new Sequential(
                                OuttakeSlides.runToPosition(states.get(state).slidePos).with(new Wait(.8)),
                                Outtake.setArm(states.get(state).armPos).with(new Wait(0.2)),
                                Outtake.setPivot(states.get(state).pivotPos).with(new Wait(0.2)),
                                Outtake.openClaw().with(new Wait(0.4))
                        )
                ))
                .withState(State.OUTTAKE_BUCKET, (state, name) -> Lambda.from(
                        new Sequential(
                                Outtake.closeClaw().with(new Wait(0.4)),
                                OuttakeSlides.runToPosition(states.get(state).slidePos).raceWith(new Wait(0.5)),
                                new Parallel(
                                        Outtake.setArm(states.get(state).armPos).with(new Wait(0.5)),
                                        Outtake.setPivot(states.get(state).pivotPos).with(new Wait(0.5))
                                )
                        )
                ))
                .withState(State.TRANSFER, (state, name) -> Lambda.from(
                        new Sequential(
                                OuttakeSlides.home(),
                                IntakeSlides.home(),
                                Outtake.setArm(states.get(state).armPos).raceWith(new Wait(0.5)),
                                Outtake.setPivot(states.get(state).pivotPos).raceWith(new Wait(0.5)),
                                Outtake.closeClaw().raceWith(new Wait(0.5))
                        )
                ));

        if (flavor == OpModeMeta.Flavor.AUTONOMOUS) {

        }
    }
}

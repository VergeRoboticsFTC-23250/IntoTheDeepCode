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

    public static StateMachine<State> stateMachine;

    public static OpModeMeta.Flavor flavor;

    static PathBuilder plusThreeBlueSpec = new PathBuilder();

    public static void init() {

        StatePositions init = new StatePositions(
                Outtake.armHomePos,
                Outtake.pivotHomePos,
                false,
                IntakeSlides.minPos
        );
        StatePositions home = new StatePositions(
                Outtake.armHomePos,
                Outtake.pivotHomePos,
                true,
                IntakeSlides.minPos
        );
        StatePositions intake = new StatePositions(
                Outtake.armSpecPos,
                Outtake.pivotSpecPos,
                true,
                IntakeSlides.minPos
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
                OuttakeSlides.minPos
        );

        Map<State, StatePositions> states = Map.of(
                State.INIT, init,
                State.HOME, home,
                State.INTAKE, intake,
                State.OUTTAKE_SUBMERSIBLE, outtakeSubmersible,
                State.OUTTAKE_SUBMERSIBLE_SCORE, outtakeSubmersibleScore,
                State.OUTTAKE_BUCKET, outtakeBucket,
                State.TRANSFER, transfer
        );

        flavor = FeatureRegistrar.getActiveOpModeWrapper().getOpModeType();

        stateMachine = new StateMachine<>(State.INIT)
                .withState(State.INIT, (state, name) -> Lambda.from(
                        new Sequential(
                                Outtake.setArm(init.armPos).with(new Wait(0.5)),
                                Outtake.setPivot(init.pivotPos).with(new Wait(0.5)),
                                Outtake.closeClaw().with(new Wait(0.5))
                                //OuttakeSlides.home(),
                                //IntakeSlides.home()
                        )
                ))
                .withState(State.HOME, (state, name) -> Lambda.from(
                        new Sequential(
                                Outtake.openClaw().with(new Wait(0.5)),
                                Outtake.setArm(home.armPos).with(new Wait(0.5)),
                                Outtake.setPivot(home.pivotPos).with(new Wait(0.5))
                                //OuttakeSlides.home(),
                                //IntakeSlides.home()
                        )
                ))
//                .withState(State.INTAKE, (state, name) -> Lambda.from(
//                        new Sequential(
//                                Outtake.setArm(intake.armPos).with(new Wait(0.5)),
//                                Outtake.setPivot(intake.pivotPos).with(new Wait(0.5)),
//                                Outtake.openClaw().with(new Wait(0.4))
//                                //OuttakeSlides.home(),
//                                //IntakeSlides.home()
//                        )
//                ))
                .withState(State.OUTTAKE_SUBMERSIBLE, (state, name) -> Lambda.from(
                        new Sequential(
                                Outtake.closeClaw().with(new Wait(0.4)),
                                OuttakeSlides.runToPosition(outtakeSubmersible.slidePos).raceWith(new Wait(0.5)),
                                new Parallel(
                                        Outtake.setArm(outtakeSubmersible.armPos).with(new Wait(0.5)),
                                        Outtake.setPivot(outtakeSubmersible.pivotPos).with(new Wait(0.5))
                                )
                        )

                ));
//                .withState(State.OUTTAKE_SUBMERSIBLE_SCORE, (state, name) -> Lambda.from(
//                        new Sequential(
//                                OuttakeSlides.runToPosition(outtakeSubmersibleScore.slidePos).with(new Wait(.8)),
//                                Outtake.setArm(outtakeSubmersibleScore.armPos).with(new Wait(0.2)),
//                                Outtake.setPivot(outtakeSubmersibleScore.pivotPos).with(new Wait(0.2)),
//                                Outtake.openClaw().with(new Wait(0.4))
//                        )
//                ))
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
//                .withState(State.TRANSFER, (state, name) -> Lambda.from(
//                        new Sequential(
//                                //OuttakeSlides.home(),
//                                //IntakeSlides.home(),
//                                Outtake.setArm(transfer.armPos).raceWith(new Wait(0.5)),
//                                Outtake.setPivot(transfer.pivotPos).raceWith(new Wait(0.5)),
//                                Outtake.closeClaw().raceWith(new Wait(0.5))
//                        )
//                ));

        if (flavor == OpModeMeta.Flavor.AUTONOMOUS) {

        }
    }

    public static Lambda setState(State state) {
        return new Lambda("set-state")
                .setInit(() -> {
                    stateMachine.setState(state);
                })
                .setFinish(() -> true);
    }
}

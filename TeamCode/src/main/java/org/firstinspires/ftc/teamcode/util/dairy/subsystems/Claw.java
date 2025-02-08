package org.firstinspires.ftc.teamcode.util.dairy.subsystems;

import androidx.annotation.NonNull;

import com.pedropathing.follower.Follower;
import com.pedropathing.follower.FollowerConstants;
import com.pedropathing.localization.Pose;
import com.pedropathing.pathgen.Path;
import com.pedropathing.pathgen.PathChain;
import com.pedropathing.util.Constants;
import com.pedropathing.util.DashboardPoseTracker;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.internal.opmode.OpModeMeta;
import org.firstinspires.ftc.teamcode.util.dairy.Robot;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.concurrent.atomic.AtomicLong;

import dev.frozenmilk.dairy.core.FeatureRegistrar;
import dev.frozenmilk.dairy.core.dependency.Dependency;
import dev.frozenmilk.dairy.core.dependency.annotation.SingleAnnotation;
import dev.frozenmilk.dairy.core.wrapper.Wrapper;
import dev.frozenmilk.mercurial.Mercurial;
import dev.frozenmilk.mercurial.bindings.BoundGamepad;
import dev.frozenmilk.mercurial.commands.Lambda;
import dev.frozenmilk.mercurial.subsystems.Subsystem;
import org.firstinspires.ftc.teamcode.util.pedroPathing.constants.FConstants;
import org.firstinspires.ftc.teamcode.util.pedroPathing.constants.LConstants;
import kotlin.annotation.MustBeDocumented;

public class Claw implements Subsystem {
    public static final Claw INSTANCE = new Claw();
    public static Servo wrist;

    public Claw() {}

    @Retention(RetentionPolicy.RUNTIME) @Target(ElementType.TYPE) @MustBeDocumented
    @Inherited
    public @interface Attach { }

    private Dependency<?> dependency = Subsystem.DEFAULT_DEPENDENCY.and(new SingleAnnotation<>(Attach.class));

    @NonNull
    @Override
    public Dependency<?> getDependency() { return dependency; }

    @Override
    public void setDependency(@NonNull Dependency<?> dependency) { this.dependency = dependency; }

    @Override
    public void preUserInitHook(@NonNull Wrapper opMode) {
        wrist = opMode.getOpMode().hardwareMap.get(Servo.class, "wrist");
    }

    @Override
    public void postUserInitHook(@NonNull Wrapper opMode) {

    }

    @Override
    public void preUserStartHook(@NonNull Wrapper opMode) {
    }

    @Override
    public void postUserLoopHook(@NonNull Wrapper opMode) {

    }

    public static Lambda autoAlign(double angle) {
        return new Lambda("auto-align")
                .setInit(() -> wrist.setPosition(angle * (180.0 / 255.0)));
    }
}
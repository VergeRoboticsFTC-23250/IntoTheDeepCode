package org.firstinspires.ftc.teamcode.util.dairy;

import org.firstinspires.ftc.robotcore.internal.opmode.OpModeMeta;
import org.firstinspires.ftc.teamcode.util.dairy.subsystems.OuttakeSlides;

import dev.frozenmilk.dairy.core.FeatureRegistrar;

public class Robot {

    public static OpModeMeta.Flavor flavor;

    public static void init() {
        flavor = FeatureRegistrar.getActiveOpModeWrapper().getOpModeType();

    }
}

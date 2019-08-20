package com.fuzs.gsds.handler;

import net.minecraftforge.common.ForgeConfigSpec;

@SuppressWarnings("WeakerAccess")
public class ConfigHandler {

    private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static final GeneralConfig GENERAL_CONFIG = new GeneralConfig("general");

    public static class GeneralConfig {

        public final ForgeConfigSpec.BooleanValue bowDrawingAnim;
        public final ForgeConfigSpec.BooleanValue slowBowDrawing;
        public final ForgeConfigSpec.DoubleValue chaseSpeedAmp;
        public final ForgeConfigSpec.DoubleValue maxAttackDistance;

        private GeneralConfig(String name) {

            BUILDER.push(name);

            this.bowDrawingAnim = BUILDER.comment("Skeletons will actually draw their bow back before shooting.", "Only effective when \"Slow bow drawing speed\" is \"false\".").define("Bow drawing animation", true);
            this.slowBowDrawing = BUILDER.comment("Skeletons will NOT shoot faster the closer their target moves to them.").define("Slow bow drawing speed", false);
            this.chaseSpeedAmp = BUILDER.comment("Walking speed amplifier at which the skeleton will chase a target if the distance between the two is too large.").defineInRange("Chasing speed amplifier", 1.25, 0.0, 16.0);
            this.maxAttackDistance = BUILDER.comment("Distance between a skeleton and its target at which the skeleton will shoot. If the distance is larger the skeleton will walk towards the target.").defineInRange("Max attack distance", 15.0, 0.0, 64.0);

            BUILDER.pop();

        }

    }

    public static final ForgeConfigSpec SPEC = BUILDER.build();

}

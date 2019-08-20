package com.fuzs.gsds.handler;

import net.minecraftforge.common.config.Configuration;

import java.io.File;

@SuppressWarnings("WeakerAccess")
public class ConfigHandler {

    public static Configuration config;
    public static String categoryGeneral = Configuration.CATEGORY_GENERAL;

    public static boolean bowDrawingAnim;
    public static boolean slowBowDrawing;
    public static double chaseSpeedAmp;
    public static double maxAttackDistance;

    public static void init(File configFile) {

        if (config == null) {
            config = new Configuration(configFile);
            loadConfiguration();
        }

    }

    public static void loadConfiguration() {

        config.getCategory(categoryGeneral);

        bowDrawingAnim = config.getBoolean("Bow drawing animation", categoryGeneral, true, "Skeletons will actually draw their bow back before shooting. Only effective when \"Slow bow drawing speed\" is \"false\".");
        slowBowDrawing = config.getBoolean("Slow bow drawing speed", categoryGeneral, false, "Skeletons will NOT shoot faster the closer their target moves to them.");
        chaseSpeedAmp = config.getFloat("Chasing speed amplifier", categoryGeneral, 1.25F, 0, 16.0F, "Walking speed amplifier at which the skeleton will chase a target if the distance between the two is too large.");
        maxAttackDistance = config.getFloat("Max attack distance", categoryGeneral, 15.0F, 0, 64.0F, "Distance between a skeleton and its target at which the skeleton will shoot. If the distance is larger the skeleton will walk towards the target.");

        if (config.hasChanged()) {
            config.save();
        }

    }

}

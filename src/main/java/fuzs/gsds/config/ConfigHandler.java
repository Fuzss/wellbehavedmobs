package fuzs.gsds.config;

import java.io.File;

import fuzs.gsds.GoodSkeletonsDontStrafe;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.client.event.ConfigChangedEvent.OnConfigChangedEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@EventBusSubscriber
public class ConfigHandler {
    public static Configuration config;
    public static String categoryGeneral = "general";
    public static boolean lowerIdleBow;
    public static boolean bowDrawingAnim;
    public static boolean slowBowDrawing;
    public static double chaseSpeedAmp;
    public static float maxAttackDistance;

    public static void init(File configFile) {
        if (config == null) {
            config = new Configuration(configFile);
            loadConfiguration();
        }
    }

    private static void loadConfiguration() {
        config.getCategory(categoryGeneral);
        lowerIdleBow = config.getBoolean("Lower bow when idle", categoryGeneral, true, "Skeletons will lower their bow when they aren't focusing a target.");
        bowDrawingAnim = config.getBoolean("Bow drawing animation", categoryGeneral, true, "Skeletons will actually draw their bow back before shooting. Only effective when \"Slow bow drawing speed\" is \"false\".");
        slowBowDrawing = config.getBoolean("Slow bow drawing speed", categoryGeneral, false, "Skeletons will NOT shoot faster the closer their target moves to them.");
        chaseSpeedAmp = (double) config.getFloat("Chasing speed amplifier", categoryGeneral, 1.25F, 0, 16.0F, "Walking speed amplifier at which the skeleton will chase a target if the distance between the two is too large.");
        maxAttackDistance = config.getFloat("Max attack distance", categoryGeneral, 15.0F, 0, 64.0F, "Distance between a skeleton and its target at which the skeleton will shoot. If the distance is larger the skeleton will walk towards the target.");

        if (config.hasChanged()) {
            config.save();
        }
    }

    @SubscribeEvent
    public static void onConfigurationChanged(OnConfigChangedEvent event) {
        if (event.getModID().equalsIgnoreCase(GoodSkeletonsDontStrafe.MODID)) {
            loadConfiguration();
        }
    }
}

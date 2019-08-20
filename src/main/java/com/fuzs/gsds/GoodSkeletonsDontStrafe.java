package com.fuzs.gsds;

import com.fuzs.gsds.handler.ConfigHandler;
import com.fuzs.gsds.handler.SkeletonJoinHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLFingerprintViolationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;

@SuppressWarnings({"WeakerAccess", "unused"})
@Mod(
        modid = GoodSkeletonsDontStrafe.MODID,
        name = GoodSkeletonsDontStrafe.NAME,
        version = GoodSkeletonsDontStrafe.VERSION,
        acceptedMinecraftVersions = GoodSkeletonsDontStrafe.RANGE,
        acceptableRemoteVersions = GoodSkeletonsDontStrafe.REMOTE,
        guiFactory = GoodSkeletonsDontStrafe.GUI,
        dependencies = GoodSkeletonsDontStrafe.DEPENDENCIES,
        certificateFingerprint = GoodSkeletonsDontStrafe.FINGERPRINT
)
public class GoodSkeletonsDontStrafe {

    public static final String MODID = "goodskeletonsdontstrafe";
    public static final String NAME = "Good Skeletons Don't Strafe";
    public static final String VERSION = "@VERSION@";
    public static final String RANGE = "[1.12.2]";
    public static final String REMOTE = "*";
    public static final String GUI = "com.fuzs.gsds.helper.GuiFactory";
    public static final String SLUG = "skeletonsdontstrafe";
    public static final String DEPENDENCIES = "required-after:forge@[14.23.5.2779,)";
    public static final String FINGERPRINT = "@FINGERPRINT@";

    public static final Logger LOGGER = LogManager.getLogger(GoodSkeletonsDontStrafe.NAME);

    @EventHandler
    public void onPreInit(FMLPreInitializationEvent evt) {
        ConfigHandler.init(new File(evt.getModConfigurationDirectory(), SLUG + ".cfg"));
        MinecraftForge.EVENT_BUS.register(this);
        MinecraftForge.EVENT_BUS.register(new SkeletonJoinHandler());
    }

    @EventHandler
    public void onFingerprintViolation(FMLFingerprintViolationEvent evt) {
        LOGGER.warn("Invalid fingerprint detected! The file " + evt.getSource().getName() + " may have been tampered with. This version will NOT be supported by the author!");
    }

    @SubscribeEvent
    public static void onConfigurationChanged(ConfigChangedEvent.OnConfigChangedEvent event) {
        if (event.getModID().equalsIgnoreCase(GoodSkeletonsDontStrafe.MODID)) {
            ConfigHandler.loadConfiguration();
        }
    }

}
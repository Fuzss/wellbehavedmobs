package com.fuzs.gsds;

import com.fuzs.gsds.handler.ConfigHandler;
import com.fuzs.gsds.handler.SkeletonJoinHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@SuppressWarnings({"WeakerAccess", "unused"})
@Mod(GoodSkeletonsDontStrafe.MODID)
public class GoodSkeletonsDontStrafe {

    public static final String MODID = "goodskeletonsdontstrafe";
    public static final String NAME = "Good Skeletons Don't Strafe";
    public static final Logger LOGGER = LogManager.getLogger(GoodSkeletonsDontStrafe.NAME);

    public GoodSkeletonsDontStrafe() {

        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::commonSetup);
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, ConfigHandler.SPEC, MODID + ".toml");

    }

    private void commonSetup(final FMLCommonSetupEvent evt) {

        MinecraftForge.EVENT_BUS.register(new SkeletonJoinHandler());

    }

}
package com.fuzs.wellbehavedmobs;

import com.fuzs.puzzleslib_wbm.PuzzlesLib;
import com.fuzs.puzzleslib_wbm.config.ConfigManager;
import com.fuzs.wellbehavedmobs.common.WellBehavedMobsElements;
import net.minecraftforge.fml.common.Mod;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@SuppressWarnings({"WeakerAccess", "unused"})
@Mod(WellBehavedMobs.MODID)
public class WellBehavedMobs extends PuzzlesLib {

    public static final String MODID = "wellbehavedmobs";
    public static final String NAME = "Well-Behaved Mobs";
    public static final Logger LOGGER = LogManager.getLogger(NAME);

    public WellBehavedMobs() {

        super();
        WellBehavedMobsElements.setup(MODID);
        ConfigManager.get().load();
    }

}
package com.fuzs.gsds.helper;

import java.util.Set;

import com.fuzs.gsds.GoodSkeletonsDontStrafe;
import com.fuzs.gsds.handler.ConfigHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.fml.client.IModGuiFactory;
import net.minecraftforge.fml.client.config.GuiConfig;

@SuppressWarnings("unused")
public class GuiFactory implements IModGuiFactory {

    public void initialize(Minecraft minecraftInstance) {
    }

    public Set<RuntimeOptionCategoryElement> runtimeGuiCategories() {
        return null;
    }

    public boolean hasConfigGui() {
        return true;
    }

    public GuiScreen createConfigGui(GuiScreen parentScreen) {
        return new ConfigGui(parentScreen);
    }

    private static class ConfigGui extends GuiConfig {

        private ConfigGui(GuiScreen parentScreen) {
            super(parentScreen, new ConfigElement(ConfigHandler.config.getCategory(ConfigHandler.categoryGeneral)).getChildElements(),
                    GoodSkeletonsDontStrafe.MODID, false, false,
                    GuiConfig.getAbridgedConfigPath(ConfigHandler.config.toString()));
        }

    }

}

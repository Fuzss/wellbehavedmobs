package com.fuzs.gsds.helper;

import java.util.Set;

import com.fuzs.gsds.GoodSkeletonsDontStrafe;
import com.fuzs.gsds.handler.ConfigHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.fml.client.IModGuiFactory;
import net.minecraftforge.fml.client.config.GuiConfig;

@SuppressWarnings({"deprecation", "unused", "WeakerAccess"})
public class GuiFactory implements IModGuiFactory {

    public void initialize(Minecraft minecraftInstance) {
    }

    @Override
    public Class<? extends GuiScreen> mainConfigGuiClass() {
        return ConfigGui.class;
    }

    public Set<RuntimeOptionCategoryElement> runtimeGuiCategories() {
        return null;
    }

    @Override
    public RuntimeOptionGuiHandler getHandlerFor(RuntimeOptionCategoryElement element) {
        return null;
    }

    public boolean hasConfigGui() {
        return true;
    }

    public GuiScreen createConfigGui(GuiScreen parentScreen) {
        return new ConfigGui(parentScreen);
    }

    public static class ConfigGui extends GuiConfig {

        public ConfigGui(GuiScreen parentScreen) {
            super(parentScreen, new ConfigElement(ConfigHandler.config.getCategory(ConfigHandler.categoryGeneral)).getChildElements(),
                    GoodSkeletonsDontStrafe.MODID, false, false,
                    GuiConfig.getAbridgedConfigPath(ConfigHandler.config.toString()));
        }

    }

}

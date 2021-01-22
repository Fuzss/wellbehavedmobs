package com.fuzs.wellbehavedmobs.mixin;

import com.fuzs.wellbehavedmobs.WellBehavedMobs;
import org.spongepowered.asm.mixin.Mixins;
import org.spongepowered.asm.mixin.connect.IMixinConnector;

@SuppressWarnings("unused")
public class MixinConnector implements IMixinConnector {

    @Override
    public void connect() {

        Mixins.addConfiguration("META-INF/" + WellBehavedMobs.MODID + ".mixins.json");
    }

}

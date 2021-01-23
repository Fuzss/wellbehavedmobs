package com.fuzs.wellbehavedmobs.mixin;

import com.fuzs.wellbehavedmobs.common.WellBehavedMobsElements;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.world.biome.DefaultBiomeFeatures;
import net.minecraft.world.biome.MobSpawnInfo;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Optional;

@SuppressWarnings("unused")
@Mixin(DefaultBiomeFeatures.class)
public class DefaultBiomeFeaturesMixin {

    @Inject(method = "withPassiveMobs", at = @At("TAIL"))
    private static void withPassiveMobs(MobSpawnInfo.Builder builder, CallbackInfo callbackInfo) {

        Optional<Object> optional = WellBehavedMobsElements.getConfigValue(WellBehavedMobsElements.BRAVE_RABBITS, "Rabbits Everywhere");
        if (optional.isPresent() && (Boolean) optional.get()) {

            builder.withSpawner(EntityClassification.CREATURE, new MobSpawnInfo.Spawners(EntityType.RABBIT, 10, 3, 3));
        }
    }

}

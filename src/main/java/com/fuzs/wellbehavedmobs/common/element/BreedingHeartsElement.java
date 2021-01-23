package com.fuzs.wellbehavedmobs.common.element;

import com.fuzs.puzzleslib_wbm.element.AbstractElement;
import com.fuzs.puzzleslib_wbm.element.ISidedElement;
import com.fuzs.wellbehavedmobs.mixin.accessor.IAnimalEntityAccessor;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.event.entity.living.LivingEvent;

public class BreedingHeartsElement extends AbstractElement implements ISidedElement.Common {

    @Override
    public String getDescription() {

        return "Heart particles appear around animals in love instead of only once when they enter love mode.";
    }

    @Override
    public void setupCommon() {
        
        this.addListener(this::onLivingUpdate);
    }

    private void onLivingUpdate(final LivingEvent.LivingUpdateEvent evt) {

        // call correct method for sending spawned particles to the client
        if (evt.getEntityLiving() instanceof AnimalEntity && !evt.getEntityLiving().world.isRemote) {

            AnimalEntity animal = (AnimalEntity) evt.getEntityLiving();
            int inLove = ((IAnimalEntityAccessor) animal).getInLove();
            if (inLove > 0 && inLove % 10 == 0) {

                double posX = animal.getRNG().nextGaussian() * 0.02;
                double posY = animal.getRNG().nextGaussian() * 0.02;
                double posZ = animal.getRNG().nextGaussian() * 0.02;
                ((ServerWorld) animal.world).spawnParticle(ParticleTypes.HEART, animal.getPosXRandom(1.0),
                        animal.getPosYRandom() + 0.5, animal.getPosZRandom(1.0), 1, posX, posY, posZ, 0.0);
            }
        }
    }
    
}

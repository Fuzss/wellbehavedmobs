package com.fuzs.gsds.handler;

import com.fuzs.gsds.ai.RangedEasyBowAttackGoal;
import net.minecraft.entity.ai.goal.RangedBowAttackGoal;
import net.minecraft.entity.monster.AbstractSkeletonEntity;
import net.minecraft.item.BowItem;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class SkeletonJoinHandler {

    @SuppressWarnings("unused")
    @SubscribeEvent
    public void onEntityJoinWorld(EntityJoinWorldEvent evt) {

        if (evt.getEntity() instanceof AbstractSkeletonEntity) {

            AbstractSkeletonEntity skeleton = (AbstractSkeletonEntity) evt.getEntity();
            ItemStack stack = skeleton.getHeldItemMainhand();
            RangedEasyBowAttackGoal<AbstractSkeletonEntity> aiarroweasyattack = new RangedEasyBowAttackGoal<>(skeleton, ConfigBuildHandler.GENERAL_CONFIG.chaseSpeedAmp.get(),
                    20, 60, ConfigBuildHandler.GENERAL_CONFIG.maxAttackDistance.get().floatValue());
            skeleton.aiArrowAttack = aiarroweasyattack;

            if (stack.getItem() instanceof BowItem) {
                skeleton.goalSelector.goals.stream().map(it -> it.inner).filter(it -> it instanceof RangedBowAttackGoal)
                        .findFirst().ifPresent(bowGoal -> {
                    skeleton.goalSelector.removeGoal(bowGoal);
                    skeleton.goalSelector.addGoal(4, aiarroweasyattack);
                });
            }

        }

    }

}

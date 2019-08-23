package com.fuzs.gsds.handler;

import com.fuzs.gsds.ai.RangedEasyBowAttackGoal;
import com.fuzs.gsds.helper.ReflectionHelper;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.ai.goal.PrioritizedGoal;
import net.minecraft.entity.ai.goal.RangedBowAttackGoal;
import net.minecraft.entity.monster.AbstractSkeletonEntity;
import net.minecraft.item.BowItem;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.Set;

public class SkeletonJoinHandler {

    @SuppressWarnings("unused")
    @SubscribeEvent
    public void onJoinWorld(EntityJoinWorldEvent evt) {

        if (evt.getEntity() instanceof AbstractSkeletonEntity) {

            AbstractSkeletonEntity abstractskeleton = (AbstractSkeletonEntity) evt.getEntity();
            ItemStack itemstack = abstractskeleton.getHeldItemMainhand();

            RangedEasyBowAttackGoal<AbstractSkeletonEntity> aiarroweasyattack = new RangedEasyBowAttackGoal<>(abstractskeleton,
                    ConfigHandler.GENERAL_CONFIG.chaseSpeedAmp.get(), 20, 60, ConfigHandler.GENERAL_CONFIG.maxAttackDistance.get().floatValue());

            if (itemstack.getItem() instanceof BowItem) {

                Set<PrioritizedGoal> goals = ReflectionHelper.getGoals(abstractskeleton.goalSelector);
                assert goals != null;

                Goal aiarrowattack = null;

                for (PrioritizedGoal entityaitasks$entityaitaskentry : goals) {

                    Goal entityaibase = ReflectionHelper.getInnerGoal(entityaitasks$entityaitaskentry);

                    if (entityaibase instanceof RangedBowAttackGoal) {

                        aiarrowattack = entityaibase;
                        break;

                    }

                }

                if (aiarrowattack != null) {

                    abstractskeleton.goalSelector.removeGoal(aiarrowattack);
                    abstractskeleton.goalSelector.addGoal(4, aiarroweasyattack);

                }

            }

            ReflectionHelper.setAiArrowAttack(abstractskeleton, aiarroweasyattack);

        }

    }

}

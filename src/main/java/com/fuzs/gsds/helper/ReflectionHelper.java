package com.fuzs.gsds.helper;

import com.fuzs.gsds.GoodSkeletonsDontStrafe;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.ai.goal.GoalSelector;
import net.minecraft.entity.ai.goal.PrioritizedGoal;
import net.minecraft.entity.ai.goal.RangedBowAttackGoal;
import net.minecraft.entity.monster.AbstractSkeletonEntity;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;

import java.util.Set;

public class ReflectionHelper {

    private static final String ABSTRACTSKELETONENTITY_AIRARROWATTACK = "field_85037_d";
    private static final String GOALSELECTOR_GOALS = "field_220892_d";
    private static final String PRIORITIZEDGOAL_INNER = "field_220774_a";

    public static void setAiArrowAttack(AbstractSkeletonEntity instance, RangedBowAttackGoal aitask) {

        try {

            ObfuscationReflectionHelper.setPrivateValue(AbstractSkeletonEntity.class, instance, aitask, ABSTRACTSKELETONENTITY_AIRARROWATTACK);

        } catch (Exception e) {

            GoodSkeletonsDontStrafe.LOGGER.error("setAiArrowAttack() failed", e);

        }

    }

    public static Set<PrioritizedGoal> getGoals(GoalSelector instance) {

        try {

            return ObfuscationReflectionHelper.getPrivateValue(GoalSelector.class, instance, GOALSELECTOR_GOALS);

        } catch (Exception e) {

            GoodSkeletonsDontStrafe.LOGGER.error("getGoals() failed", e);

        }

        return null;

    }

    public static Goal getInnerGoal(PrioritizedGoal instance) {

        try {

            return ObfuscationReflectionHelper.getPrivateValue(PrioritizedGoal.class, instance, PRIORITIZEDGOAL_INNER);

        } catch (Exception e) {

            GoodSkeletonsDontStrafe.LOGGER.error("getInnerGoal() failed", e);

        }

        return null;

    }

}

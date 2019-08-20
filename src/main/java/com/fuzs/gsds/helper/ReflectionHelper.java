package com.fuzs.gsds.helper;

import com.fuzs.gsds.GoodSkeletonsDontStrafe;
import net.minecraft.entity.ai.EntityAIAttackRangedBow;
import net.minecraft.entity.monster.AbstractSkeleton;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;

public class ReflectionHelper {

    private static final String ABSTRACTSKELETONENTITY_AIRARROWATTACK = "field_85037_d";

    public static void setAiArrowAttack(AbstractSkeleton instance, EntityAIAttackRangedBow aitask) {

        try {

            ObfuscationReflectionHelper.setPrivateValue(AbstractSkeleton.class, instance, aitask, ABSTRACTSKELETONENTITY_AIRARROWATTACK);

        } catch (Exception e) {

            GoodSkeletonsDontStrafe.LOGGER.error("setAiArrowAttack() failed", e);

        }

    }

}

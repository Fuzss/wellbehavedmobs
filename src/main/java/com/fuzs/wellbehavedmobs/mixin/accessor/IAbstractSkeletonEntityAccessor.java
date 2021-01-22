package com.fuzs.wellbehavedmobs.mixin.accessor;

import net.minecraft.entity.ai.goal.RangedBowAttackGoal;
import net.minecraft.entity.monster.AbstractSkeletonEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(AbstractSkeletonEntity.class)
public interface IAbstractSkeletonEntityAccessor {

    @Accessor
    RangedBowAttackGoal<AbstractSkeletonEntity> getAiArrowAttack();

    @Accessor
    void setAiArrowAttack(RangedBowAttackGoal<AbstractSkeletonEntity> aiArrowAttack);

}

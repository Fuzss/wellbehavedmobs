package com.fuzs.wellbehavedmobs.mixin.accessor;

import net.minecraft.entity.ai.goal.AvoidEntityGoal;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(AvoidEntityGoal.class)
public interface IAvoidEntityGoalAccessor {

    @Accessor
    Class<?> getClassToAvoid();

}

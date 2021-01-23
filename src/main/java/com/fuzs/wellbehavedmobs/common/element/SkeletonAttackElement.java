package com.fuzs.wellbehavedmobs.common.element;

import com.fuzs.puzzleslib_wbm.element.AbstractElement;
import com.fuzs.puzzleslib_wbm.element.ISidedElement;
import com.fuzs.wellbehavedmobs.common.ai.RangedBowEasyAttackGoal;
import com.fuzs.wellbehavedmobs.mixin.accessor.IAbstractSkeletonEntityAccessor;
import net.minecraft.entity.monster.AbstractSkeletonEntity;
import net.minecraft.item.BowItem;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;

public class SkeletonAttackElement extends AbstractElement implements ISidedElement.Common {

    @Override
    public String getDescription() {

        return "Remove the very annoying and buggy strafing behavior from skeletons when attacking.";
    }

    @Override
    public void setupCommon() {
        
        this.addListener(this::onEntityJoinWorld);
    }

    @Override
    public void setupCommonConfig(ForgeConfigSpec.Builder builder) {

        addToConfig(builder.comment("Skeletons will shoot faster the closer their target moves to them.").define("Quick Bow Drawing", true), v -> {});
    }

    private void onEntityJoinWorld(final EntityJoinWorldEvent evt) {

        if (evt.getEntity() instanceof AbstractSkeletonEntity) {

            AbstractSkeletonEntity skeleton = (AbstractSkeletonEntity) evt.getEntity();
            ItemStack stack = skeleton.getHeldItemMainhand();
            RangedBowEasyAttackGoal<AbstractSkeletonEntity> aiArrowAttack = new RangedBowEasyAttackGoal<>(skeleton, 1.0, 20, 60, 15.0F);
            if (stack.getItem() instanceof BowItem) {

                skeleton.goalSelector.removeGoal(((IAbstractSkeletonEntityAccessor) skeleton).getAiArrowAttack());
                skeleton.goalSelector.addGoal(4, aiArrowAttack);
            }

            ((IAbstractSkeletonEntityAccessor) skeleton).setAiArrowAttack(aiArrowAttack);
        }
    }
    
}

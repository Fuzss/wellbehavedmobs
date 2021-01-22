package com.fuzs.wellbehavedmobs.common.element;

import com.fuzs.puzzleslib.element.AbstractElement;
import com.fuzs.puzzleslib.element.ISidedElement;
import com.fuzs.wellbehavedmobs.common.ai.RangedEasyBowAttackGoal;
import com.fuzs.wellbehavedmobs.mixin.accessor.IAbstractSkeletonEntityAccessor;
import net.minecraft.entity.monster.AbstractSkeletonEntity;
import net.minecraft.item.BowItem;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;

public class SkeletonAttackElement extends AbstractElement implements ISidedElement.Common {

    private boolean bowDrawingAnimation;
    private boolean quickBowDrawing;
    private double chaseSpeedAmplifier;
    private double maxAttackDistance;

    @Override
    public boolean getDefaultState() {

        return true;
    }

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

        addToConfig(builder.comment("Skeletons will draw their bows back before shooting.", "Only effective when \"Quick Bow Drawing\" is enabled.").define("Bow Drawing Animation", true), v -> this.bowDrawingAnimation = v);
        addToConfig(builder.comment("Skeletons will shoot faster the closer their target moves to them.").define("Quick Bow Drawing", true), v -> this.quickBowDrawing = v);
        addToConfig(builder.comment("Walking speed amplifier at which the skeleton will chase a target if the distance between the two is too large.").defineInRange("Chasing Speed Amplifier", 1.25, 0.0, 16.0), v -> this.chaseSpeedAmplifier = v);
        addToConfig(builder.comment("Distance between a skeleton and its target at which the skeleton will shoot. If the distance is larger the skeleton will move towards the target.").defineInRange("Max Attack Distance", 15.0, 0.0, 64.0), v -> this.maxAttackDistance = v);
    }

    private void onEntityJoinWorld(final EntityJoinWorldEvent evt) {

        if (evt.getEntity() instanceof AbstractSkeletonEntity) {

            AbstractSkeletonEntity skeleton = (AbstractSkeletonEntity) evt.getEntity();
            ItemStack stack = skeleton.getHeldItemMainhand();
            RangedEasyBowAttackGoal<AbstractSkeletonEntity> aiarroweasyattack = new RangedEasyBowAttackGoal<>(skeleton, this.chaseSpeedAmplifier, 20, 60, (float) this.maxAttackDistance);
            if (stack.getItem() instanceof BowItem) {

                skeleton.goalSelector.removeGoal(((IAbstractSkeletonEntityAccessor) skeleton).getAiArrowAttack());
                skeleton.goalSelector.addGoal(4, aiarroweasyattack);
            }

            ((IAbstractSkeletonEntityAccessor) skeleton).setAiArrowAttack(aiarroweasyattack);
        }
    }
    
}

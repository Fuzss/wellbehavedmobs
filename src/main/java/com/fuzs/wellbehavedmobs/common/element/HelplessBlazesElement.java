package com.fuzs.wellbehavedmobs.common.element;

import com.fuzs.puzzleslib_wbm.element.AbstractElement;
import com.fuzs.puzzleslib_wbm.element.ISidedElement;
import com.fuzs.wellbehavedmobs.mixin.accessor.IGoalSelectorAccessor;
import com.fuzs.wellbehavedmobs.mixin.accessor.IHurtByTargetGoalAccessor;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.goal.HurtByTargetGoal;
import net.minecraft.entity.ai.goal.PrioritizedGoal;
import net.minecraft.entity.monster.BlazeEntity;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;

public class HelplessBlazesElement extends AbstractElement implements ISidedElement.Common {

    private boolean noCallForHelp;
    private boolean decreaseFollowRange;

    @Override
    public String getDescription() {

        return "Blazes don't call their buddies for help when attacking anymore and have a decreased follow range.";
    }

    @Override
    public void setupCommon() {

        this.addListener(this::onEntityJoinWorld);
    }

    @Override
    public void setupCommonConfig(ForgeConfigSpec.Builder builder) {

        addToConfig(builder.comment("Other blazes aren't alerted when a blaze is fighting a player.").define("No Call For Help", true), v -> this.noCallForHelp = v);
        addToConfig(builder.comment("Decrease follow range to 32 blocks from 48.").define("Decrease Follow Range", true), v -> this.decreaseFollowRange = v);
    }

    private void onEntityJoinWorld(final EntityJoinWorldEvent evt) {

        if (evt.getEntity() instanceof BlazeEntity) {

            if (this.noCallForHelp) {

                this.disableCallForHelp((BlazeEntity) evt.getEntity());
            }

            if (this.decreaseFollowRange) {

                ((LivingEntity) evt.getEntity()).getAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(32.0);
            }
        }
    }

    private void disableCallForHelp(BlazeEntity blazeEntity) {

        ((IGoalSelectorAccessor) blazeEntity.goalSelector).getGoals().stream()
                .map(PrioritizedGoal::getGoal)
                .filter(goal -> goal instanceof HurtByTargetGoal)
                .findFirst()
                .map(goal -> ((IHurtByTargetGoalAccessor) goal))
                .ifPresent(goal -> goal.setEntityCallsForHelp(false));
    }
    
}

package com.fuzs.wellbehavedmobs.common.element;

import com.fuzs.puzzleslib.element.AbstractElement;
import com.fuzs.puzzleslib.element.ISidedElement;
import com.fuzs.wellbehavedmobs.mixin.accessor.IGoalSelectorAccessor;
import com.fuzs.wellbehavedmobs.mixin.accessor.IHurtByTargetGoalAccessor;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.attributes.GlobalEntityTypeAttributes;
import net.minecraft.entity.ai.goal.HurtByTargetGoal;
import net.minecraft.entity.ai.goal.PrioritizedGoal;
import net.minecraft.entity.monster.BlazeEntity;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;

public class HelplessBlazeElement extends AbstractElement implements ISidedElement.Common {

    private boolean noCallForHelp;

    @Override
    public boolean getDefaultState() {

        return true;
    }

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
        addToConfig(builder.comment("Decrease follow range to 32 blocks from 48.").define("Decrease Follow Range", true), this::setFollowRangeAttribute);
    }

    @Override
    public void onDisable() {

        this.setFollowRangeAttribute(false);
    }

    private void onEntityJoinWorld(final EntityJoinWorldEvent evt) {

        if (this.noCallForHelp && evt.getEntity() instanceof BlazeEntity) {

            this.disableCallForHelp((BlazeEntity) evt.getEntity());
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

    private void setFollowRangeAttribute(boolean decrease) {

        GlobalEntityTypeAttributes.put(EntityType.BLAZE, decrease ? BlazeEntity.registerAttributes()
                .createMutableAttribute(Attributes.FOLLOW_RANGE, 32.0).create() : BlazeEntity.registerAttributes().create());
    }
    
}

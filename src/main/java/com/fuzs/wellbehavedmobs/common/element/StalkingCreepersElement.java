package com.fuzs.wellbehavedmobs.common.element;

import com.fuzs.puzzleslib_wbm.element.AbstractElement;
import com.fuzs.puzzleslib_wbm.element.ISidedElement;
import com.fuzs.wellbehavedmobs.common.ai.StalkingCreeperSwellGoal;
import com.fuzs.wellbehavedmobs.mixin.accessor.IGoalSelectorAccessor;
import net.minecraft.entity.ai.goal.CreeperSwellGoal;
import net.minecraft.entity.ai.goal.PrioritizedGoal;
import net.minecraft.entity.monster.CreeperEntity;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;

public class StalkingCreepersElement extends AbstractElement implements ISidedElement.Common {

    @Override
    public boolean getDefaultState() {

        return false;
    }

    @Override
    public String getDescription() {

        return "Instead of standing still, creepers will stay close to their target and dodge attacks while exploding.";
    }

    @Override
    public void setupCommon() {
        
        this.addListener(this::onEntityJoinWorld);
    }

    private void onEntityJoinWorld(final EntityJoinWorldEvent evt) {

        if (evt.getEntity() instanceof CreeperEntity) {

            CreeperEntity creeperEntity = (CreeperEntity) evt.getEntity();
            ((IGoalSelectorAccessor) creeperEntity.goalSelector).getGoals().stream().map(PrioritizedGoal::getGoal).filter(goal -> goal instanceof CreeperSwellGoal).findFirst().ifPresent(creeperEntity.goalSelector::removeGoal);
            creeperEntity.goalSelector.addGoal(2, new StalkingCreeperSwellGoal(creeperEntity));
        }
    }
    
}

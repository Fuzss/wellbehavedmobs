package com.fuzs.wellbehavedmobs.common.element;

import com.fuzs.puzzleslib.element.AbstractElement;
import com.fuzs.puzzleslib.element.ISidedElement;
import com.fuzs.wellbehavedmobs.mixin.accessor.IAvoidEntityGoalAccessor;
import com.fuzs.wellbehavedmobs.mixin.accessor.IGoalSelectorAccessor;
import net.minecraft.block.Blocks;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.passive.RabbitEntity;
import net.minecraft.entity.passive.WolfEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.Ingredient;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;

import java.util.Set;
import java.util.stream.Collectors;

public class BraveRabbitElement extends AbstractElement implements ISidedElement.Common {

    @Override
    public boolean getDefaultState() {

        return true;
    }

    @Override
    public String getDescription() {

        return "Change rabbit ai for them to no longer be scared as easily. Also add some basic animal behaviour (such as kits following their parents) which for some reason is missing by default.";
    }

    @Override
    public void setupCommon() {

        this.addListener(this::onEntityJoinWorld);
    }

    private void onEntityJoinWorld(final EntityJoinWorldEvent evt) {

        if (evt.getEntity() instanceof RabbitEntity) {

            RabbitEntity rabbitEntity = ((RabbitEntity) evt.getEntity());
            this.clearOldGoals(rabbitEntity);
            this.registerNewGoals(rabbitEntity);
        }
    }

    private void clearOldGoals(RabbitEntity rabbitEntity) {

        if (rabbitEntity != null) {

            Set<Goal> goalsToRemove = this.getGoalsToRemove(((IGoalSelectorAccessor) rabbitEntity.goalSelector).getGoals(), rabbitEntity.getRabbitType() != 99);
            goalsToRemove.forEach(rabbitEntity.goalSelector::removeGoal);
        }
    }

    private Set<Goal> getGoalsToRemove(Set<PrioritizedGoal> goals, boolean isNormalRabbit) {

        return goals.stream()
                .map(PrioritizedGoal::getGoal)
                .filter(goal -> !(goal instanceof MoveToBlockGoal || goal instanceof MeleeAttackGoal || goal instanceof PanicGoal || goal instanceof AvoidEntityGoal && ((IAvoidEntityGoalAccessor) goal).getClassToAvoid() != WolfEntity.class && isNormalRabbit))
                .collect(Collectors.toSet());
    }

    private void registerNewGoals(RabbitEntity rabbitEntity) {

        if (rabbitEntity != null) {

            rabbitEntity.goalSelector.addGoal(0, new SwimGoal(rabbitEntity));
            rabbitEntity.goalSelector.addGoal(2, new BreedGoal(rabbitEntity, 1.1));
            Ingredient breedingItems = Ingredient.fromItems(Items.CARROT, Items.GOLDEN_CARROT, Blocks.DANDELION);
            rabbitEntity.goalSelector.addGoal(3, new TemptGoal(rabbitEntity, 1.33, breedingItems, false));
            rabbitEntity.goalSelector.addGoal(6, new FollowParentGoal(rabbitEntity, 1.2));
            rabbitEntity.goalSelector.addGoal(7, new WaterAvoidingRandomWalkingGoal(rabbitEntity, 1.1));
            rabbitEntity.goalSelector.addGoal(8, new LookAtGoal(rabbitEntity, PlayerEntity.class, 6.0F));
            rabbitEntity.goalSelector.addGoal(9, new LookRandomlyGoal(rabbitEntity));
        }
    }
    
}

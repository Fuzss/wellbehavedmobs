package com.fuzs.wellbehavedmobs.common.element;

import com.fuzs.puzzleslib.element.AbstractElement;
import com.fuzs.puzzleslib.element.ISidedElement;
import net.minecraft.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.entity.passive.SheepEntity;
import net.minecraft.entity.passive.WolfEntity;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;

public class SmartSheepElement extends AbstractElement implements ISidedElement.Common {

    @Override
    public boolean getDefaultState() {

        return true;
    }

    @Override
    public String getDescription() {

        return "Sheep flee from wolves instead of letting themselves be eaten, because why would they do that.";
    }

    @Override
    public void setupCommon() {
        
        this.addListener(this::onEntityJoinWorld);
    }

    private void onEntityJoinWorld(final EntityJoinWorldEvent evt) {

        if (evt.getEntity() instanceof SheepEntity) {

            SheepEntity sheepEntity = (SheepEntity) evt.getEntity();
            sheepEntity.goalSelector.addGoal(4, new AvoidEntityGoal<>(sheepEntity, WolfEntity.class, 10.0F, 1.25, 1.25));
        }
    }
    
}

package com.fuzs.wellbehavedmobs.common.element;

import com.fuzs.puzzleslib_wbm.element.AbstractElement;
import com.fuzs.puzzleslib_wbm.element.ISidedElement;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.monster.EndermanEntity;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingSetAttackTargetEvent;

import java.util.Optional;
import java.util.UUID;

public class ChilledEndermenElement extends AbstractElement implements ISidedElement.Common {

    private boolean chilledAttackSpeed;
    private boolean decreaseFollowRange;

    @Override
    public String getDescription() {

        return "Endermen aren't lightning fast and can't sense you from the other end of the world anymore.";
    }

    @Override
    public void setupCommon() {

        this.addListener(this::onEntityJoinWorld);
        this.addListener(this::onLivingSetAttackTarget);
    }

    @Override
    public void setupCommonConfig(ForgeConfigSpec.Builder builder) {

        addToConfig(builder.comment("Enderman don't run even faster when attacking.").define("Chilled Attack Speed", true), v -> this.chilledAttackSpeed = v);
        addToConfig(builder.comment("Decrease follow range to 32 blocks from a ridiculously high 64 block count.").define("Decrease Follow Range", false), v -> this.decreaseFollowRange = v);
    }

    private void onEntityJoinWorld(final EntityJoinWorldEvent evt) {

        if (this.decreaseFollowRange && evt.getEntity() instanceof EndermanEntity) {

            ((LivingEntity) evt.getEntity()).getAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(32.0);
        }
    }

    private void onLivingSetAttackTarget(final LivingSetAttackTargetEvent evt) {

        if (this.chilledAttackSpeed && evt.getEntity() instanceof EndermanEntity) {

            // id for enderman attack boost
            final UUID attackingSpeedBoostId = UUID.fromString("020E0DFB-87AE-4653-9556-831010E291A0");
            IAttributeInstance iattributeinstance = ((EndermanEntity) evt.getEntity()).getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED);
            Optional.ofNullable(iattributeinstance.getModifier(attackingSpeedBoostId)).ifPresent(iattributeinstance::removeModifier);
        }
    }
    
}

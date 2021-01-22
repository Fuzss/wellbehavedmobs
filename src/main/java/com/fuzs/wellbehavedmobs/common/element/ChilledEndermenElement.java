package com.fuzs.wellbehavedmobs.common.element;

import com.fuzs.puzzleslib_wbm.element.AbstractElement;
import com.fuzs.puzzleslib_wbm.element.ISidedElement;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.attributes.GlobalEntityTypeAttributes;
import net.minecraft.entity.ai.attributes.ModifiableAttributeInstance;
import net.minecraft.entity.monster.EndermanEntity;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.event.entity.living.LivingSetAttackTargetEvent;

import java.util.Optional;
import java.util.UUID;

public class ChilledEndermenElement extends AbstractElement implements ISidedElement.Common {

    private boolean chilledAttackSpeed;

    @Override
    public boolean getDefaultState() {

        return true;
    }

    @Override
    public String getDescription() {

        return "Endermen aren't lightning fast and can't sense you from the other end of the world anymore.";
    }

    @Override
    public void setupCommon() {

        this.addListener(this::onLivingSetAttackTarget);
    }

    @Override
    public void setupCommonConfig(ForgeConfigSpec.Builder builder) {

        addToConfig(builder.comment("Enderman don't run even faster when attacking.").define("Chilled Attack Speed", true), v -> this.chilledAttackSpeed = v);
        addToConfig(builder.comment("Decrease follow range to 32 blocks from a ridiculously high 64 block count.").define("Decrease Follow Range", false), this::setFollowRangeAttribute);
    }

    @Override
    public void onDisable() {

        this.setFollowRangeAttribute(false);
    }

    private void onLivingSetAttackTarget(final LivingSetAttackTargetEvent evt) {

        if (this.chilledAttackSpeed && evt.getEntity() instanceof EndermanEntity) {

            // id for enderman attack boost
            final UUID attackingSpeedBoostId = UUID.fromString("020E0DFB-87AE-4653-9556-831010E291A0");
            ModifiableAttributeInstance attribute = ((EndermanEntity) evt.getEntity()).getAttribute(Attributes.MOVEMENT_SPEED);
            if (attribute != null) {

                Optional.ofNullable(attribute.getModifier(attackingSpeedBoostId)).ifPresent(attribute::removeModifier);
            }
        }
    }

    private void setFollowRangeAttribute(boolean decrease) {

        GlobalEntityTypeAttributes.put(EntityType.ENDERMAN, decrease ? EndermanEntity.func_234287_m_()
                .createMutableAttribute(Attributes.FOLLOW_RANGE, 32.0).create() : EndermanEntity.func_234287_m_().create());
    }
    
}

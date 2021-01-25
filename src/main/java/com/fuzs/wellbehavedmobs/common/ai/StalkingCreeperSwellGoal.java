package com.fuzs.wellbehavedmobs.common.ai;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.CreeperSwellGoal;
import net.minecraft.entity.monster.CreeperEntity;
import net.minecraft.util.math.MathHelper;

public class StalkingCreeperSwellGoal extends CreeperSwellGoal {

    private final CreeperEntity swellingCreeper;
    private LivingEntity creeperAttackTarget;

    public StalkingCreeperSwellGoal(CreeperEntity entitycreeperIn) {

        super(entitycreeperIn);
        this.swellingCreeper = entitycreeperIn;
    }

    /**
     * Execute a one shot task or start executing a continuous task
     */
    @Override
    public void startExecuting() {

        super.startExecuting();
        this.creeperAttackTarget = this.swellingCreeper.getAttackTarget();
    }

    /**
     * Keep ticking a continuous task that has already been started
     */
    @Override
    public void tick() {

        super.tick();
        if (this.swellingCreeper.getCreeperState() == 1) {

            float prevRotationYaw = this.swellingCreeper.rotationYaw;
            this.swellingCreeper.faceEntity(this.creeperAttackTarget, 30.0F, 30.0F);
            float strafeAmount = (prevRotationYaw - this.swellingCreeper.rotationYaw + 90.0F) * (float) Math.PI / 180.0F;
            this.swellingCreeper.getMoveHelper().strafe(MathHelper.cos(strafeAmount) * 0.7F, -MathHelper.sin(strafeAmount) * 0.7F);
        }
    }

}

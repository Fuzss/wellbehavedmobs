package com.fuzs.gsds.ai;

import com.fuzs.gsds.handler.ConfigHandler;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIAttackRangedBow;
import net.minecraft.entity.monster.AbstractSkeleton;
import net.minecraft.item.ItemBow;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.MathHelper;

public class EntityAIAttackRangedEasyBow extends EntityAIAttackRangedBow {

    // The entity (as a RangedAttackMob) the AI instance has been applied to.
    private final AbstractSkeleton entity;
    // A decrementing tick that spawns a ranged attack once this value reaches 0. It is then set back to the maxattackTime.
    private int attackTime;
    private final double moveSpeedAmp;
    private int seeTime;
    private int attackCooldown;
    // The maximum time the AI has to wait before performing another ranged attack.
    private int maxattackTime;
    private float field_96562_i;
    private float maxAttackDistance;

    public EntityAIAttackRangedEasyBow(AbstractSkeleton attacker, double movespeed, int p_i1650_4_, int maxattackTime, float maxAttackDistanceIn) {
        super(attacker, movespeed, p_i1650_4_, maxAttackDistanceIn);
        this.attackTime = -1;
        this.entity = attacker;
        this.moveSpeedAmp = movespeed;
        this.attackCooldown = p_i1650_4_;
        this.maxattackTime = maxattackTime;
        this.field_96562_i = maxAttackDistanceIn;
        this.maxAttackDistance = maxAttackDistanceIn * maxAttackDistanceIn;
        this.setMutexBits(3);
    }

    /**
     * Reset the task's internal state. Called when this task is interrupted by another one
     */
    @Override
    public void resetTask() {
        super.resetTask();
        this.seeTime = 0;
        this.attackTime = -1;
    }

    /**
     * Updates the task
     */
    @Override
    public void updateTask() {

        EntityLivingBase entitylivingbase = this.entity.getAttackTarget();
        
        if (entitylivingbase == null) {
            return;
        }
        
        double d0 = this.entity.getDistanceSq(entitylivingbase.posX, entitylivingbase.getEntityBoundingBox().minY, entitylivingbase.posZ);
        boolean flag = this.entity.getEntitySenses().canSee(entitylivingbase);

        if (flag != this.seeTime > 0) {
            this.seeTime = 0;
        }

        if (flag) {
            ++this.seeTime;
        } else {
            --this.seeTime;
        }

        if (d0 <= (double) this.maxAttackDistance && this.seeTime >= 20) {
            this.entity.getNavigator().clearPathEntity();
        } else {
            this.entity.getNavigator().tryMoveToEntityLiving(entitylivingbase, this.moveSpeedAmp);
        }

        this.entity.getLookHelper().setLookPositionWithEntity(entitylivingbase, 30.0F, 30.0F);

        if (ConfigHandler.slowBowDrawing) {

            if (this.entity.isHandActive()) {

                if (!flag && this.seeTime < -60) {

                    this.entity.resetActiveHand();

                } else if (flag) {

                    int i = this.entity.getItemInUseMaxCount();

                    if (i >= 20) {
                        this.entity.resetActiveHand();
                        this.entity.attackEntityWithRangedAttack(entitylivingbase, ItemBow.getArrowVelocity(i));
                        this.attackTime = this.attackCooldown;
                    }

                }

            } else if (--this.attackTime <= 0 && this.seeTime >= -60) {

                this.entity.setActiveHand(EnumHand.MAIN_HAND);

            }

        } else {

            if (--this.attackTime == 0) {

                if (d0 > (double) this.maxAttackDistance || !flag && this.seeTime < -60) {

                    this.entity.resetActiveHand();

                } else if (flag) {

                    float f = MathHelper.sqrt(d0) / this.field_96562_i;
                    float lvt_5_1_ = MathHelper.clamp(f, 0.1F, 1.0F);
                    this.entity.attackEntityWithRangedAttack(entitylivingbase, lvt_5_1_);
                    this.attackTime = MathHelper.floor(f * (float) (this.maxattackTime - this.attackCooldown) + (float) this.attackCooldown);
                    this.entity.resetActiveHand();

                }

            } else if (this.attackTime < 0) {

                float f2 = MathHelper.sqrt(d0) / this.field_96562_i;
                this.attackTime = MathHelper.floor(f2 * (float) (this.maxattackTime - this.attackCooldown) + (float) this.attackCooldown);

            } else if (!this.entity.isHandActive() && this.attackTime <= 20 && ConfigHandler.bowDrawingAnim) {

                this.entity.setActiveHand(EnumHand.MAIN_HAND);

            }

        }

    }

}

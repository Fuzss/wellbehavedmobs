package com.fuzs.wellbehavedmobs.common.ai;

import net.minecraft.entity.IRangedAttackMob;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.ai.goal.RangedBowAttackGoal;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.item.BowItem;
import net.minecraft.util.Hand;
import net.minecraft.util.math.MathHelper;

import java.util.EnumSet;

public class RangedEasyBowAttackGoal<T extends MonsterEntity & IRangedAttackMob> extends RangedBowAttackGoal<T> {

    // The entity (as a RangedAttackMob) the AI instance has been applied to.
    private final T entity;
    // A decrementing tick that spawns a ranged attack once this value reaches 0. It is then set back to the maxattackTime.
    private int attackTime;
    private final double moveSpeedAmp;
    private int seeTime;
    private int attackCooldown;
    // The maximum time the AI has to wait before performing another ranged attack.
    private int maxAttackTime;
    private float field_96562_i;
    private float maxAttackDistance;

    public RangedEasyBowAttackGoal(T attacker, double movespeed, int p_i1650_4_, int maxAttackTime, float maxAttackDistanceIn) {
        super(attacker, movespeed, p_i1650_4_, maxAttackDistanceIn);
        this.attackTime = -1;
        this.entity = attacker;
        this.moveSpeedAmp = movespeed;
        this.attackCooldown = p_i1650_4_;
        this.maxAttackTime = maxAttackTime;
        this.field_96562_i = maxAttackDistanceIn;
        this.maxAttackDistance = maxAttackDistanceIn * maxAttackDistanceIn;
        this.setMutexFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
    }

    @Override
    public void setAttackCooldown(int cooldown) {
        this.attackCooldown = cooldown;
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
    public void tick() {

        LivingEntity livingentity = this.entity.getAttackTarget();
        
        if (livingentity == null) {
            return;
        }
        
        double d0 = this.entity.getDistanceSq(livingentity.func_226277_ct_(), livingentity.func_226278_cu_(), livingentity.func_226281_cx_());
        boolean flag = this.entity.getEntitySenses().canSee(livingentity);

        if (flag != this.seeTime > 0) {
            this.seeTime = 0;
        }

        if (flag) {
            ++this.seeTime;
        } else {
            --this.seeTime;
        }

        if (d0 <= (double) this.maxAttackDistance && this.seeTime >= 20) {
            this.entity.getNavigator().clearPath();
        } else {
            this.entity.getNavigator().tryMoveToEntityLiving(livingentity, this.moveSpeedAmp);
        }

        this.entity.getLookController().setLookPositionWithEntity(livingentity, 30.0F, 30.0F);

        if (ConfigBuildHandler.GENERAL_CONFIG.slowBowDrawing.get()) {

            if (this.entity.isHandActive()) {

                if (!flag && this.seeTime < -60) {
                    this.entity.resetActiveHand();
                } else if (flag) {
                    int i = this.entity.getItemInUseMaxCount();
                    if (i >= 20) {
                        this.entity.resetActiveHand();
                        this.entity.attackEntityWithRangedAttack(livingentity, BowItem.getArrowVelocity(i));
                        this.attackTime = this.attackCooldown;
                    }
                }

            } else if (--this.attackTime <= 0 && this.seeTime >= -60) {

                this.entity.setActiveHand(Hand.MAIN_HAND);

            }

        } else {

            if (--this.attackTime == 0) {

                if (d0 > (double) this.maxAttackDistance || !flag && this.seeTime < -60) {
                    this.entity.resetActiveHand();
                } else if (flag) {
                    float f = MathHelper.sqrt(d0) / this.field_96562_i;
                    float lvt_5_1_ = MathHelper.clamp(f, 0.1F, 1.0F);
                    this.entity.attackEntityWithRangedAttack(livingentity, lvt_5_1_);
                    this.attackTime = MathHelper.floor(f * (float) (this.maxAttackTime - this.attackCooldown) + (float) this.attackCooldown);
                    this.entity.resetActiveHand();
                }

            } else if (this.attackTime < 0) {

                float f2 = MathHelper.sqrt(d0) / this.field_96562_i;
                this.attackTime = MathHelper.floor(f2 * (float) (this.maxAttackTime - this.attackCooldown) + (float) this.attackCooldown);

            } else if (!this.entity.isHandActive() && this.attackTime <= 20 && ConfigBuildHandler.GENERAL_CONFIG.bowDrawingAnim.get()) {

                this.entity.setActiveHand(Hand.MAIN_HAND);

            }

        }

    }

}

package com.fuzs.gsds.ai;

import com.fuzs.gsds.handler.ConfigHandler;
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
    // The maximum time the AI has to wait before peforming another ranged attack.
    private int maxattackTime;
    private float field_96562_i;
    private float maxAttackDistance;

    public RangedEasyBowAttackGoal(T attacker, double movespeed, int p_i1650_4_, int maxattackTime, float maxAttackDistanceIn) {
        super(attacker, movespeed, p_i1650_4_, maxAttackDistanceIn);
        this.attackTime = -1;
        this.entity = attacker;
        this.moveSpeedAmp = movespeed;
        this.attackCooldown = p_i1650_4_;
        this.maxattackTime = maxattackTime;
        this.field_96562_i = maxAttackDistanceIn;
        this.maxAttackDistance = maxAttackDistanceIn * maxAttackDistanceIn;
        this.setMutexFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
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

        LivingEntity entitylivingbase = this.entity.getAttackTarget();
        
        if (entitylivingbase == null) {
            return;
        }
        
        double d0 = this.entity.getDistanceSq(entitylivingbase.posX, entitylivingbase.getBoundingBox().minY, entitylivingbase.posZ);
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
            this.entity.getNavigator().clearPath();
        } else {
            this.entity.getNavigator().tryMoveToEntityLiving(entitylivingbase, this.moveSpeedAmp);
        }

        this.entity.getLookController().setLookPositionWithEntity(entitylivingbase, 30.0F, 30.0F);

        if (ConfigHandler.GENERAL_CONFIG.slowBowDrawing.get()) {

            if (this.entity.isHandActive()) {

                if (!flag && this.seeTime < -60) {

                    this.entity.resetActiveHand();

                } else if (flag) {

                    int i = this.entity.getItemInUseMaxCount();

                    if (i >= 20) {
                        this.entity.resetActiveHand();
                        this.entity.attackEntityWithRangedAttack(entitylivingbase, BowItem.getArrowVelocity(i));
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
                    this.entity.attackEntityWithRangedAttack(entitylivingbase, lvt_5_1_);
                    this.attackTime = MathHelper.floor(f * (float) (this.maxattackTime - this.attackCooldown) + (float) this.attackCooldown);
                    this.entity.resetActiveHand();

                }

            } else if (this.attackTime < 0) {

                float f2 = MathHelper.sqrt(d0) / this.field_96562_i;
                this.attackTime = MathHelper.floor(f2 * (float) (this.maxattackTime - this.attackCooldown) + (float) this.attackCooldown);

            } else if (!this.entity.isHandActive() && this.attackTime <= 20 && ConfigHandler.GENERAL_CONFIG.bowDrawingAnim.get()) {

                this.entity.setActiveHand(Hand.MAIN_HAND);

            }

        }

    }

}

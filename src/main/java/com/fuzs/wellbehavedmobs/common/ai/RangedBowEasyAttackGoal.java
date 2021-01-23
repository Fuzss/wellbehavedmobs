package com.fuzs.wellbehavedmobs.common.ai;

import com.fuzs.wellbehavedmobs.common.WellBehavedMobsElements;
import net.minecraft.entity.IRangedAttackMob;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.RangedBowAttackGoal;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.projectile.ProjectileHelper;
import net.minecraft.item.BowItem;
import net.minecraft.item.Items;
import net.minecraft.util.math.MathHelper;

import java.util.Optional;

public class RangedBowEasyAttackGoal<T extends MonsterEntity & IRangedAttackMob> extends RangedBowAttackGoal<T> {

    private final T entity;
    private final double moveSpeedAmplifier;
    private int attackCooldown;
    private final int maxAttackTime;
    private final float maxAttackDistance;
    private int attackTime = -1;
    private int seeTime;

    public RangedBowEasyAttackGoal(T mob, double chaseTargetSpeed, int attackCooldown, int maxAttackTime, float maxAttackDistance) {

        super(mob, chaseTargetSpeed, attackCooldown, maxAttackDistance);
        this.entity = mob;
        this.moveSpeedAmplifier = chaseTargetSpeed;
        this.attackCooldown = attackCooldown;
        this.maxAttackTime = maxAttackTime;
        this.maxAttackDistance = maxAttackDistance * maxAttackDistance;
    }

    @Override
    public void setAttackCooldown(int attackCooldownIn) {

        this.attackCooldown = attackCooldownIn / 2;
    }

    @Override
    public void resetTask() {

        this.entity.setAggroed(false);
        this.seeTime = 0;
        this.attackTime = -1;
        this.entity.resetActiveHand();
    }

    @Override
    public void tick() {

        LivingEntity livingentity = this.entity.getAttackTarget();
        if (livingentity != null) {

            double distanceToTarget = this.entity.getDistanceSq(livingentity.getPosX(), livingentity.getPosY(), livingentity.getPosZ());
            boolean canSeeTarget = this.entity.getEntitySenses().canSee(livingentity);

            if (canSeeTarget != this.seeTime > 0) {

                this.seeTime = 0;
            }

            if (canSeeTarget) {

                ++this.seeTime;
            } else {

                --this.seeTime;
            }

            if (distanceToTarget <= (double) this.maxAttackDistance && this.seeTime >= 20) {

                this.entity.getNavigator().clearPath();
            } else {

                this.entity.getNavigator().tryMoveToEntityLiving(livingentity, this.moveSpeedAmplifier);
            }

            this.entity.getLookController().setLookPositionWithEntity(livingentity, 30.0F, 30.0F);

            if (this.entity.isHandActive()) {

                if (!canSeeTarget && this.seeTime < -this.maxAttackTime) {

                    this.entity.resetActiveHand();
                } else if (canSeeTarget) {

                    int useCount = this.entity.getItemInUseMaxCount();
                    if (useCount >= 20) {

                        this.entity.resetActiveHand();
                        double distanceVelocity = Math.sqrt(distanceToTarget) / Math.sqrt(this.maxAttackDistance);
                        this.entity.attackEntityWithRangedAttack(livingentity, MathHelper.clamp((float) distanceVelocity, 0.1F, 1.0F) * BowItem.getArrowVelocity(useCount));
                        Optional<Object> optional = WellBehavedMobsElements.getConfigValue(WellBehavedMobsElements.SKELETON_ATTACK, "Quick Bow Drawing");
                        this.attackTime = optional.isPresent() && !((Boolean) optional.get()) ? this.attackCooldown : MathHelper.floor(distanceVelocity * (this.maxAttackTime - this.attackCooldown) + this.attackCooldown);
                    }
                }
            } else if (--this.attackTime <= 0 && this.seeTime >= -this.maxAttackTime) {

                this.entity.setActiveHand(ProjectileHelper.getHandWith(this.entity, Items.BOW));
            }
        }
    }

}

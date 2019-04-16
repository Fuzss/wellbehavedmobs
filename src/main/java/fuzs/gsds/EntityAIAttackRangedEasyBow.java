package fuzs.gsds;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IRangedAttackMob;
import net.minecraft.entity.ai.EntityAIAttackRangedBow;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.MathHelper;

public class EntityAIAttackRangedEasyBow<T extends EntityMob & IRangedAttackMob> extends EntityAIAttackRangedBow
{
    /**
     * The entity (as a RangedAttackMob) the AI instance has been applied to.
     */
    private final T entity;

    /**
     * A decrementing tick that spawns a ranged attack once this value reaches 0. It is then set back to the
     * maxattackTime.
     */
    private int attackTime;
    private final double moveSpeedAmp;
    private int seeTime;
    private int field_96561_g;

    /**
     * The maximum time the AI has to wait before peforming another ranged attack.
     */
    private int maxattackTime;
    private float field_96562_i;
    private float maxAttackDistance;

    EntityAIAttackRangedEasyBow(T attacker, double movespeed, int p_i1650_4_, int maxattackTime, float maxAttackDistanceIn)
    {
        super(attacker, movespeed, p_i1650_4_, maxAttackDistanceIn);
        this.attackTime = -1;
        this.entity = attacker;
        this.moveSpeedAmp = movespeed;
        this.field_96561_g = p_i1650_4_;
        this.maxattackTime = maxattackTime;
        this.field_96562_i = maxAttackDistanceIn;
        this.maxAttackDistance = maxAttackDistanceIn * maxAttackDistanceIn;
        this.setMutexBits(3);
    }

    /**
     * Updates the task
     */
    @Override
    public void updateTask()
    {
        EntityLivingBase entitylivingbase = this.entity.getAttackTarget();
        
        if (entitylivingbase == null) {
            return;
        }
        
        double d0 = this.entity.getDistanceSq(entitylivingbase.posX, entitylivingbase.getEntityBoundingBox().minY, entitylivingbase.posZ);
        boolean flag = this.entity.getEntitySenses().canSee(entitylivingbase);

        if (flag)
        {
            ++this.seeTime;
        }
        else
        {
            this.seeTime = 0;
        }

        if (d0 <= (double)this.maxAttackDistance && this.seeTime >= 20)
        {
            this.entity.getNavigator().clearPath();
        }
        else
        {
            this.entity.getNavigator().tryMoveToEntityLiving(entitylivingbase, this.moveSpeedAmp);
        }

        this.entity.getLookHelper().setLookPositionWithEntity(entitylivingbase, 30.0F, 30.0F);

        if (--this.attackTime == 0)
        {
            if (d0 > (double)this.maxAttackDistance || !flag)
            {
                this.entity.resetActiveHand();
                return;
            }

            float f = MathHelper.sqrt(d0) / this.field_96562_i;
            float lvt_5_1_ = MathHelper.clamp(f, 0.1F, 1.0F);
            this.entity.attackEntityWithRangedAttack(entitylivingbase, lvt_5_1_);
            this.attackTime = MathHelper.floor(f * (float)(this.maxattackTime - this.field_96561_g) + (float)this.field_96561_g);
            this.entity.resetActiveHand();
        }
        else if (this.attackTime < 0)
        {
            float f2 = MathHelper.sqrt(d0) / this.field_96562_i;
            this.attackTime = MathHelper.floor(f2 * (float)(this.maxattackTime - this.field_96561_g) + (float)this.field_96561_g);
        }
        else if (!this.entity.isHandActive() && this.attackTime <= 20)
        {
            this.entity.setActiveHand(EnumHand.MAIN_HAND);
        }
    }
}

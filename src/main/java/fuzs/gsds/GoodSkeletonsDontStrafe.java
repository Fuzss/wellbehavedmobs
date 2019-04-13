package fuzs.gsds;

import net.minecraft.entity.ai.EntityAIAttackRangedBow;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.EntityAITasks;
import net.minecraft.entity.monster.*;
import net.minecraft.item.ItemStack;
import net.minecraft.world.EnumDifficulty;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod(modid = GoodSkeletonsDontStrafe.MODID, name = GoodSkeletonsDontStrafe.NAME, version = GoodSkeletonsDontStrafe.VERSION, acceptedMinecraftVersions = GoodSkeletonsDontStrafe.AVERSIONS, acceptableRemoteVersions = "*")
public class GoodSkeletonsDontStrafe {

    public static final String MODID = "gsds";
    public static final String NAME = "Good Skeletons Don't Strafe";
    public static final String VERSION = "1.0.0";
    public static final String AVERSIONS = "[1.12,1.12.2]";

    @EventHandler
    public void preinit(FMLPreInitializationEvent event) {
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void switchRangedAttackAI(LivingEvent.LivingUpdateEvent evt) {
        if (evt.getEntity() instanceof AbstractSkeleton) {

            AbstractSkeleton abstractskeleton = (AbstractSkeleton) evt.getEntity();
            ItemStack itemstack = abstractskeleton.getHeldItemMainhand();

            if (itemstack.getItem() instanceof net.minecraft.item.ItemBow) {

                for (EntityAITasks.EntityAITaskEntry entityaitasks$entityaitaskentry : abstractskeleton.tasks.taskEntries) {
                    EntityAIBase entityaibase = entityaitasks$entityaitaskentry.action;

                    if (entityaibase instanceof EntityAIAttackRangedBow && entityaitasks$entityaitaskentry.using) {
                        abstractskeleton.tasks.removeTask(entityaibase);

                        boolean flag = abstractskeleton.getEntityWorld().getDifficulty() == EnumDifficulty.HARD;
                        EntityAIAttackRangedEasyBow<AbstractSkeleton> aiarroweasyattack = new EntityAIAttackRangedEasyBow<AbstractSkeleton>(abstractskeleton, 1.0D, flag ? 40 : 20, 15.0F);
                        abstractskeleton.tasks.addTask(4, aiarroweasyattack);
                    }

                }
            }
        }
    }

}

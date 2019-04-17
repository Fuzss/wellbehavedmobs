package fuzs.gsds;

import fuzs.gsds.config.ConfigHandler;
import net.minecraft.entity.ai.EntityAIAttackRangedBow;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.EntityAITasks;
import net.minecraft.entity.monster.*;
import net.minecraft.item.ItemStack;
import net.minecraft.world.EnumDifficulty;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.io.File;

@Mod(modid = GoodSkeletonsDontStrafe.MODID, name = GoodSkeletonsDontStrafe.NAME, version = GoodSkeletonsDontStrafe.VERSION, acceptedMinecraftVersions = GoodSkeletonsDontStrafe.RANGE, acceptableRemoteVersions = "*", guiFactory = GoodSkeletonsDontStrafe.GUI)
public class GoodSkeletonsDontStrafe implements PrivateAccessor {

    public static final String MODID = "gsds";
    public static final String NAME = "Good Skeletons Don't Strafe";
    public static final String VERSION = "1.0.2";
    public static final String RANGE = "[1.12,1.12.2]";
    public static final String GUI = "fuzs.gsds.config.GuiFactory";
    public static final String SLUG = "skeletonsdontstrafe";

    @EventHandler
    public void preinit(FMLPreInitializationEvent event) {
        ConfigHandler.init(new File(event.getModConfigurationDirectory(), SLUG + ".cfg"));
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void onJoinWorld(EntityJoinWorldEvent evt) {
        if (evt.getEntity() instanceof AbstractSkeleton) {
            AbstractSkeleton abstractskeleton = (AbstractSkeleton) evt.getEntity();
            boolean flag = abstractskeleton.getEntityWorld().getDifficulty() != EnumDifficulty.HARD && ConfigHandler.slowBowDrawing;
            EntityAIAttackRangedEasyBow<AbstractSkeleton> aiarroweasyattack = new EntityAIAttackRangedEasyBow<AbstractSkeleton>(abstractskeleton, ConfigHandler.chaseSpeedAmp, flag ? 40 : 20, 60, ConfigHandler.maxAttackDistance);
            ItemStack itemstack = abstractskeleton.getHeldItemMainhand();

            if (itemstack.getItem() instanceof net.minecraft.item.ItemBow) {

                for (EntityAITasks.EntityAITaskEntry entityaitasks$entityaitaskentry : abstractskeleton.tasks.taskEntries) {
                    EntityAIBase entityaibase = entityaitasks$entityaitaskentry.action;

                    if (entityaibase instanceof EntityAIAttackRangedBow) {
                        abstractskeleton.tasks.removeTask(entityaibase);
                        abstractskeleton.tasks.addTask(4, aiarroweasyattack);
                    }

                }
            }

            setAiArrowAttack(abstractskeleton, aiarroweasyattack);

            if (!ConfigHandler.lowerIdleBow && (abstractskeleton instanceof EntitySkeleton || abstractskeleton instanceof EntityStray)) {
                abstractskeleton.setSwingingArms(true);
            }

        }
    }

}
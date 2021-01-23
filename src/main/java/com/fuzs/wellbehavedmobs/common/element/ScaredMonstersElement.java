package com.fuzs.wellbehavedmobs.common.element;

import com.fuzs.puzzleslib_wbm.config.ConfigManager;
import com.fuzs.puzzleslib_wbm.config.deserialize.EntryCollectionBuilder;
import com.fuzs.puzzleslib_wbm.element.AbstractElement;
import com.fuzs.puzzleslib_wbm.element.ISidedElement;
import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.entity.monster.CreeperEntity;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.List;
import java.util.Set;

public class ScaredMonstersElement extends AbstractElement implements ISidedElement.Common {

    private Set<EntityType<?>> scaredMonsters;

    @Override
    public String getDescription() {

        return "Various monsters run from creepers about to explode.";
    }

    @Override
    public void setupCommon() {
        
        this.addListener(this::onEntityJoinWorld);
    }

    @Override
    public void setupCommonConfig(ForgeConfigSpec.Builder builder) {

        addToConfig(builder.comment("Mobs that will run away from creepers about to explode.", EntryCollectionBuilder.CONFIG_STRING).define("Scared Monster List", this.getMonsterList()), v -> this.scaredMonsters = v, v -> new EntryCollectionBuilder<>(ForgeRegistries.ENTITIES).buildEntrySet(v, type -> type.getClassification() != EntityClassification.MISC, "Not a living entity"));
    }

    private List<String> getMonsterList() {

        return ConfigManager.get().getKeyList(EntityType.CREEPER, EntityType.SKELETON, EntityType.SPIDER, EntityType.CAVE_SPIDER, EntityType.WITCH, EntityType.ZOMBIE, EntityType.ZOMBIE_VILLAGER, EntityType.ZOMBIE_PIGMAN, EntityType.HUSK, EntityType.STRAY, EntityType.WITHER_SKELETON, EntityType.DROWNED);
    }

    private void onEntityJoinWorld(final EntityJoinWorldEvent evt) {

        if (evt.getEntity() instanceof CreatureEntity && this.scaredMonsters.contains(evt.getEntity().getType())) {

            CreatureEntity creatureEntity = (CreatureEntity) evt.getEntity();
            creatureEntity.goalSelector.addGoal(2, new AvoidEntityGoal<>(creatureEntity, CreeperEntity.class, 4.0F, 1.0, 2.0, this::isAboutToExplode));
        }
    }

    private boolean isAboutToExplode(LivingEntity entity) {

        // also check isIgnited in case of flint and steel was used
        return entity instanceof CreeperEntity && (((CreeperEntity) entity).getCreeperState() > 0 || ((CreeperEntity) entity).hasIgnited());
    }
    
}

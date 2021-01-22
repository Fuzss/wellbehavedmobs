package com.fuzs.wellbehavedmobs.common;

import com.fuzs.puzzleslib.element.AbstractElement;
import com.fuzs.puzzleslib.element.ElementRegistry;
import com.fuzs.wellbehavedmobs.common.element.*;

public class WellBehavedMobsElements extends ElementRegistry {

    public static final AbstractElement SKELETON_ATTACK = register("skeleton_attack", new SkeletonAttackElement());
    public static final AbstractElement SMART_SHEEP = register("smart_sheep", new SmartSheepElement());
    public static final AbstractElement SCARED_MONSTER = register("scared_monster", new ScaredMonsterElement());
    public static final AbstractElement CHILLED_ENDERMAN = register("chilled_enderman", new ChilledEndermanElement());
    public static final AbstractElement HELPLESS_BLAZES = register("helpless_blazes", new HelplessBlazeElement());
    public static final AbstractElement BRAVE_RABBIT = register("brave_rabbit", new BraveRabbitElement());

    /**
     * create overload so this class and its elements are loaded
     */
    public static void setup() {

        ElementRegistry.setup();
    }

}

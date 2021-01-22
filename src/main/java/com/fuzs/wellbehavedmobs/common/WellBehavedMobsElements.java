package com.fuzs.wellbehavedmobs.common;

import com.fuzs.puzzleslib_wbm.element.AbstractElement;
import com.fuzs.puzzleslib_wbm.element.ElementRegistry;
import com.fuzs.wellbehavedmobs.common.element.*;

public class WellBehavedMobsElements extends ElementRegistry {

    public static final AbstractElement SKELETON_ATTACK = register("skeleton_attack", new SkeletonAttackElement());
    public static final AbstractElement SMART_SHEEP = register("smart_sheep", new SmartSheepElement());
    public static final AbstractElement SCARED_MONSTERS = register("scared_monsters", new ScaredMonstersElement());
    public static final AbstractElement CHILLED_ENDERMEN = register("chilled_endermen", new ChilledEndermenElement());
    public static final AbstractElement HELPLESS_BLAZES = register("helpless_blazes", new HelplessBlazesElement());
    public static final AbstractElement BRAVE_RABBITS = register("brave_rabbits", new BraveRabbitsElement());

    /**
     * create overload so this class and its elements are loaded
     */
    public static void setup() {

        ElementRegistry.setup();
    }

}

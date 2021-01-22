package com.fuzs.puzzleslib.element;

import com.fuzs.puzzleslib.config.ConfigManager;
import com.google.common.collect.Maps;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * default registry for elements
 */
public abstract class ElementRegistry {

    /**
     * storage for elements of all mods for performing actions on all of them
     */
    private static final Map<ResourceLocation, AbstractElement> ELEMENTS = Maps.newHashMap();

    /**
     * register an element to the namespace of the active mod container
     * @param key identifier for this element
     * @param element element to be registered
     * @return <code>element</code>
     */
    public static AbstractElement register(String key, AbstractElement element) {

        return register(getActiveNamespace(), key, element);
    }

    /**
     * register an element, overload this to set mod namespace
     * every element must be sided, meaning must somehow implement {@link ISidedElement}
     * @param namespace namespace of registering mod
     * @param key identifier for this element
     * @param element element to be registered
     * @return <code>element</code>
     */
    protected static AbstractElement register(String namespace, String key, AbstractElement element) {

        if (element instanceof ISidedElement) {

            ResourceLocation location = new ResourceLocation(namespace, key);
            ELEMENTS.put(location, element);
            element.setRegistryName(location);
            return element;
        }

        throw new RuntimeException("Unable to register element: " + "Invalid element, no instance of ISidedElement");
    }

    /**
     * get an element from another mod which uses this registry
     * @param namespace namespace of owning mod
     * @param key key for element to get
     * @return optional element
     */
    public static Optional<AbstractElement> get(String namespace, String key) {

        return Optional.ofNullable(ELEMENTS.get(new ResourceLocation(namespace, key)));
    }

    /**
     * @param namespace namespace of owning mod
     * @param key key for element to get
     * @param path path for config value
     * @return the config value
     */
    @SuppressWarnings("OptionalIsPresent")
    public static Optional<Object> getConfigValue(String namespace, String key, String... path) {

        Optional<AbstractElement> element = get(namespace, key);
        if (element.isPresent()) {

            return getConfigValue(element.get(), path);
        }

        return Optional.empty();
    }

    /**
     * cast an element to its class type to make unique methods accessible
     * @param element element to get
     * @param <T> return type
     * @return <code>element</code> casted as <code>T</code>
     */
    @SuppressWarnings("unchecked")
    public static <T extends AbstractElement> T getAs(AbstractElement element) {

        return (T) element;
    }

    /**
     * @param element element to get value from
     * @param path path for config value
     * @return the config value
     */
    public static Optional<Object> getConfigValue(AbstractElement element, String... path) {

        if (element.isEnabled()) {

            String fullPath = Stream.concat(Stream.of(element.getRegistryName()), Stream.of(path)).collect(Collectors.joining("."));
            return Optional.of(ConfigManager.get().getValueFromPath(fullPath));
        }

        return Optional.empty();
    }

    /**
     * generate general config section for controlling elements, setup individual config sections and collect events to be registered in {@link #load}
     */
    protected static void setup() {

        Map<ResourceLocation, AbstractElement> elements = getOwnElements();
        ConfigManager.builder().create("general", builder -> elements.values().forEach(element -> element.setupGeneralConfig(builder)), getSide(elements.values()));
        elements.values().forEach(AbstractElement::setup);
    }

    /**
     * @return elements for active mod container as set
     */
    private static Map<ResourceLocation, AbstractElement> getOwnElements() {

        return ELEMENTS.entrySet().stream()
                .filter(entry -> entry.getKey().getNamespace().equals(getActiveNamespace()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    /**
     * execute on common load and register events
     * loads all elements, no matter which mod they're from
     */
    public static void load() {

        ELEMENTS.values().forEach(AbstractElement::load);
        ELEMENTS.values().stream()
                .filter(element -> element instanceof ISidedElement.Common)
                .map(element -> (ISidedElement.Common) element)
                .forEach(ISidedElement.Common::loadCommon);
    }

    /**
     * execute on client load
     * loads all elements, no matter which mod they're from
     */
    public static void loadClient() {

        ELEMENTS.values().stream()
                .filter(element -> element instanceof ISidedElement.Client)
                .map(element -> (ISidedElement.Client) element)
                .forEach(ISidedElement.Client::loadClient);
    }

    /**
     * execute on server load
     * loads all elements, no matter which mod they're from
     */
    public static void loadServer() {

        ELEMENTS.values().stream()
                .filter(element -> element instanceof ISidedElement.Server)
                .map(element -> (ISidedElement.Server) element)
                .forEach(ISidedElement.Server::loadServer);
    }

    /**
     * finds the main side this mod is running on, usually {@link net.minecraftforge.fml.config.ModConfig.Type#COMMON}
     * @return main side
     */
    private static ModConfig.Type getSide(Collection<AbstractElement> elements) {

        if (!elements.isEmpty()) {

            if (elements.stream().anyMatch(element -> element instanceof ISidedElement.Common)) {

                return ModConfig.Type.COMMON;
            } else if (elements.stream().allMatch(element -> element instanceof ISidedElement.Client)) {

                return ModConfig.Type.CLIENT;
            } else if (elements.stream().allMatch(element -> element instanceof ISidedElement.Server)) {

                return ModConfig.Type.SERVER;
            }
        }

        throw new RuntimeException("Mod has no elements");
    }

    /**
     * get active modid so entries can still be associated with the mod
     * @return active modid
     */
    private static String getActiveNamespace() {

        return ModLoadingContext.get().getActiveNamespace();
    }

}

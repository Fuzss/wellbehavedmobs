package com.fuzs.puzzleslib_wbm.element;

import com.fuzs.puzzleslib_wbm.config.ConfigManager;
import com.fuzs.puzzleslib_wbm.config.deserialize.EntryCollectionBuilder;
import com.google.common.collect.Lists;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.Cancelable;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.IForgeRegistryEntry;
import org.apache.commons.lang3.StringUtils;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * all features a mod adds are structured into elements which are then registered, this is an abstract version
 */
@SuppressWarnings("unused")
public abstract class AbstractElement implements IConfigurableElement {

    /**
     * all events registered by this element
     */
    private final List<EventStorage<? extends Event>> events = Lists.newArrayList();
    /**
     * name of this element
     */
    private ResourceLocation registryName;
    /**
     * is this element enabled (are events registered)
     */
    private boolean enabled;
    /**
     * handler for client operations
     */
    @Nullable
    private ISidedElement.Client clientPerformer;
    /**
     * handler for server operations
     */
    @Nullable
    private ISidedElement.Server serverPerformer;

    public final void setRegistryName(ResourceLocation location) {

        if (this.registryName != null) {

            throw new RuntimeException("Unable to set registry name for element: " + "Name already set");
        }

        this.registryName = location;
    }

    /**
     * @return name set in elements registry
     */
    public final String getRegistryName() {

        if (this.registryName == null) {

            throw new RuntimeException("Unable to get registry name for element: " + "Name not set");
        }

        return this.registryName.getPath();
    }

    @Override
    public boolean getDefaultState() {

        return true;
    }

    @Override
    public final String getDisplayName() {

        return Stream.of(this.getRegistryName().split("_")).map(StringUtils::capitalize).collect(Collectors.joining(" "));
    }

    @Override
    public final void setupGeneralConfig(ForgeConfigSpec.Builder builder) {

        addToConfig(builder.comment(this.getDescription()).define(this.getDisplayName(), this.getDefaultState()), this::setEnabled);
    }

    @Override
    public void unload() {

    }

    /**
     * build element config and get event listeners
     */
    public final void setup() {

        this.setupPerformers();
        this.setupConfig(this.getRegistryName());
        this.setup(ISidedElement.Common::setupCommon, ISidedElement.Client::setupClient, ISidedElement.Server::setupServer);
    }

    /**
     * create performer for physical side
     */
    private void setupPerformers() {

        if (FMLEnvironment.dist.isDedicatedServer()) {

            Optional.ofNullable(this.createServerPerformer()).map(performer -> performer.apply(this)).ifPresent(performer -> {

                if (performer instanceof ISidedElement.Server) {

                    this.serverPerformer = (ISidedElement.Server) performer;
                } else {

                    throw new RuntimeException("Performer does not implement ISidedElement.Server");
                }
            });
        } else {

            Optional.ofNullable(this.createClientPerformer()).map(performer -> performer.apply(this)).ifPresent(performer -> {

                if (performer instanceof ISidedElement.Client) {

                    this.clientPerformer = (ISidedElement.Client) performer;
                } else {

                    throw new RuntimeException("Performer does not implement ISidedElement.Client");
                }
            });
        }
    }

    /**
     * @param commonSetup consumer if implements {@link com.fuzs.puzzleslib_wbm.element.ISidedElement.Common}
     * @param clientSetup consumer if implements {@link com.fuzs.puzzleslib_wbm.element.ISidedElement.Client}
     * @param serverSetup consumer if implements {@link com.fuzs.puzzleslib_wbm.element.ISidedElement.Server}
     */
    private void setup(Consumer<ISidedElement.Common> commonSetup, Consumer<ISidedElement.Client> clientSetup, Consumer<ISidedElement.Server> serverSetup) {

        if (this instanceof ISidedElement.Common) {

            commonSetup.accept(((ISidedElement.Common) this));
        }

        if (this instanceof ISidedElement.Client) {

            clientSetup.accept(((ISidedElement.Client) this));
        }

        if (this instanceof ISidedElement.Server) {

            serverSetup.accept(((ISidedElement.Server) this));
        }
    }

    /**
     * setup config for all sides
     * @param elementId id of this element for config section
     */
    private void setupConfig(String elementId) {

        Consumer<ISidedElement.Common> commonSetup = element -> ConfigManager.builder().create(elementId, element::setupCommonConfig, ModConfig.Type.COMMON);
        Consumer<ISidedElement.Client> clientSetup = element -> ConfigManager.builder().create(elementId, element::setupClientConfig, ModConfig.Type.CLIENT);
        Consumer<ISidedElement.Server> serverSetup = element -> ConfigManager.builder().create(elementId, element::setupServerConfig, ModConfig.Type.SERVER);
        this.setup(commonSetup, clientSetup, serverSetup);
    }

    /**
     * register Forge events from internal storage
     */
    public final void load() {

        this.reload(true);
    }

    /**
     * update status of all stored events
     * @param isInit is this method called during initial setup
     */
    private void reload(boolean isInit) {

        if (this.isEnabled() || this.isAlwaysEnabled()) {

            this.events.forEach(EventStorage::register);
        } else if (!isInit) {

            // nothing to unregister during initial setup
            this.events.forEach(EventStorage::unregister);
            this.unload();
        }
    }

    @Override
    public final boolean isEnabled() {

        return this.enabled;
    }

    /**
     * are the events from this mod always active
     * @return is always enabled
     */
    protected boolean isAlwaysEnabled() {

        return false;
    }

    /**
     * set {@link #enabled} state, reload when changed
     * @param enabled enabled
     */
    private void setEnabled(boolean enabled) {

        if (enabled != this.enabled) {

            this.enabled = enabled;
            this.reload(false);
        }
    }

    /**
     * @return handler for client side
     */
    @Nullable
    protected Function<AbstractElement, ISidedElement.Abstract<?>> createClientPerformer() {

        return null;
    }

    /**
     * @return handler for server side
     */
    @Nullable
    protected Function<AbstractElement, ISidedElement.Abstract<?>> createServerPerformer() {

        return null;
    }

    /**
     * @param consumer action to perform on {@link #clientPerformer}
     */
    protected final void performForClient(Consumer<ISidedElement.Client> consumer) {

        if (this.clientPerformer != null) {

            consumer.accept(this.clientPerformer);
        }
    }

    /**
     * @param consumer action to perform on {@link #serverPerformer}
     */
    protected final void performForServer(Consumer<ISidedElement.Server> consumer) {

        if (this.serverPerformer != null) {

            consumer.accept(this.serverPerformer);
        }
    }

    /**
     * @param entry config entry to add
     * @param action consumer for updating value when changed
     * @param <S> type of config value
     * @param <T> field type
     */
    public static <S extends ForgeConfigSpec.ConfigValue<T>, T> void addToConfig(S entry, Consumer<T> action) {

        ConfigManager.get().registerEntry(entry, action);
    }

    /**
     * @param entry config entry to add
     * @param action consumer for updating value when changed
     * @param transformer transformation to apply when returning value
     * @param <S> type of config value
     * @param <T> field type
     * @param <R> final return type of config entry
     */
    public static <S extends ForgeConfigSpec.ConfigValue<T>, T, R> void addToConfig(S entry, Consumer<R> action, Function<T, R> transformer) {

        ConfigManager.get().registerEntry(entry, action, transformer);
    }

    /**
     * deserialize string <code>data</code> into entries of a <code>registry</code>
     * @param data data as string list as provided by Forge config
     * @param registry registry to get entries from
     * @param <T> type of registry
     * @return deserialized data as set
     */
    public static <T extends IForgeRegistryEntry<T>> Set<T> deserializeToSet(List<String> data, IForgeRegistry<T> registry) {

        return new EntryCollectionBuilder<>(registry).buildEntrySet(data);
    }

    /**
     * deserialize string <code>data</code> into entries of a <code>registry</code>
     * @param data data as string list as provided by Forge config
     * @param registry registry to get entries from
     * @param <T> type of registry
     * @return deserialized data as map
     */
    public static <T extends IForgeRegistryEntry<T>> Map<T, double[]> deserializeToMap(List<String> data, IForgeRegistry<T> registry) {

        return new EntryCollectionBuilder<>(registry).buildEntryMap(data);
    }

    /**
     * Add a consumer listener with {@link EventPriority} set to {@link EventPriority#NORMAL}
     * @param consumer Callback to invoke when a matching event is received
     * @param <T> The {@link Event} subclass to listen for
     */
    public final <T extends Event> void addListener(Consumer<T> consumer) {

        this.addListener(consumer, EventPriority.NORMAL);
    }

    /**
     * Add a consumer listener with {@link EventPriority} set to {@link EventPriority#NORMAL}
     * @param consumer Callback to invoke when a matching event is received
     * @param receiveCancelled Indicate if this listener should receive events that have been {@link Cancelable} cancelled
     * @param <T> The {@link Event} subclass to listen for
     */
    public final <T extends Event> void addListener(Consumer<T> consumer, boolean receiveCancelled) {

        this.addListener(consumer, EventPriority.NORMAL, receiveCancelled);
    }

    /**
     * Add a consumer listener with the specified {@link EventPriority}
     * @param consumer Callback to invoke when a matching event is received
     * @param priority {@link EventPriority} for this listener
     * @param <T> The {@link Event} subclass to listen for
     */
    public final <T extends Event> void addListener(Consumer<T> consumer, EventPriority priority) {

        this.addListener(consumer, priority, false);
    }

    /**
     * Add a consumer listener with the specified {@link EventPriority}
     * @param consumer Callback to invoke when a matching event is received
     * @param priority {@link EventPriority} for this listener
     * @param receiveCancelled Indicate if this listener should receive events that have been {@link Cancelable} cancelled
     * @param <T> The {@link Event} subclass to listen for
     */
    public final <T extends Event> void addListener(Consumer<T> consumer, EventPriority priority, boolean receiveCancelled) {

        this.events.add(new EventStorage<>(consumer, priority, receiveCancelled));
    }

    /**
     * storage for {@link net.minecraftforge.eventbus.api.Event} so we can register and unregister them as needed
     * @param <T> type of event
     */
    private static class EventStorage<T extends Event> {

        /**
         * Callback to invoke when a matching event is received
         */
        private final Consumer<T> event;
        /**
         * {@link EventPriority} for this listener
         */
        private final EventPriority priority;
        /**
         * Indicate if this listener should receive events that have been {@link Cancelable} cancelled
         */
        private final boolean receiveCancelled;
        /**
         * has been registered or unregistered
         */
        private boolean active;

        /**
         * create new storage object with the same arguments as when calling {@link net.minecraftforge.eventbus.api.IEventBus#addListener}
         * @param priority {@link EventPriority} for this listener
         * @param receiveCancelled Indicate if this listener should receive events that have been {@link Cancelable} cancelled
         * @param consumer Callback to invoke when a matching event is received
         */
        EventStorage(Consumer<T> consumer, EventPriority priority, boolean receiveCancelled) {

            this.event = consumer;
            this.priority = priority;
            this.receiveCancelled = receiveCancelled;
        }

        /**
         * check if storage object can be registered and do so if possible
         */
        void register() {

            if (this.isActive(true)) {

                MinecraftForge.EVENT_BUS.addListener(this.priority, this.receiveCancelled, this.event);
            }
        }

        /**
         * check if storage object can be unregistered and do so if possible
         */
        void unregister() {

            if (this.isActive(false)) {

                MinecraftForge.EVENT_BUS.unregister(this.event);
            }
        }

        /**
         * verify with {@link #active} if registering action can be performed
         * @param newState new active state after registering or unregistering
         * @return is an action permitted
         */
        private boolean isActive(boolean newState) {

            if (this.active != newState) {

                this.active = newState;
                return true;
            }

            return false;
        }
    }

}

package com.fuzs.puzzleslib_wbm.element;

import net.minecraftforge.common.ForgeConfigSpec;

/**
 * don't implement this directly, only sub-interfaces, multiple versions can be implemented
 */
public interface ISidedElement {

    /**
     * implement this for elements with common capabilities
     */
    interface Common extends ISidedElement {

        /**
         * register common events
         */
        default void setupCommon() {

        }

        /**
         * setup for {@link net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent}
         */
        default void loadCommon() {

        }

        /**
         * build common config
         * @param builder builder for common config
         */
        default void setupCommonConfig(ForgeConfigSpec.Builder builder) {

        }

    }

    /**
     * implement this for elements with client-side capabilities
     */
    interface Client extends ISidedElement {

        /**
         * register client events
         */
        default void setupClient() {

        }

        /**
         * setup for {@link net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent}
         */
        default void loadClient() {

        }

        /**
         * build client config
         * @param builder builder for client config
         */
        default void setupClientConfig(ForgeConfigSpec.Builder builder) {

        }

    }

    /**
     * implement this for elements with server-side capabilities
     */
    interface Server extends ISidedElement {

        /**
         * register server events
         */
        default void setupServer() {

        }

        /**
         * setup for {@link net.minecraftforge.fml.event.lifecycle.FMLDedicatedServerSetupEvent}
         */
        default void loadServer() {

        }

        /**
         * build server config
         * @param builder builder for server config
         */
        default void setupServerConfig(ForgeConfigSpec.Builder builder) {

        }

    }

    /**
     * abstract template for sided elements complementing a common element
     */
    class Abstract<T extends AbstractElement & ISidedElement.Common> {

        /**
         * common element this belongs to
         */
        private final T parent;

        /**
         * create new with parent
         * @param parent parent
         */
        public Abstract(T parent) {

            this.parent = parent;
        }

        /**
         * @return common parent for this
         */
        public T getParent() {

            return this.parent;
        }

    }

}

package com.almostreliable.kubeio;

import com.almostreliable.kubeio.enderio.CustomConduitEntry;
import com.almostreliable.kubeio.kube.KubePlugin;
import com.almostreliable.kubeio.kube.event.ConduitRegistryEvent;
import com.enderio.base.common.init.EIOCreativeTabs;
import com.mojang.logging.LogUtils;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.RegisterEvent;
import org.slf4j.Logger;

@SuppressWarnings("UtilityClassWithPublicConstructor")
@Mod(KubeIOConstants.MOD_ID)
public final class ModInitializer {

    private static final Logger LOGGER = LogUtils.getLogger();

    public ModInitializer() {
        LOGGER.info("Loading EnderIO integration for KubeJS");
        var modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        modEventBus.addListener(ModInitializer::onRegistration);
        modEventBus.addListener(ModInitializer::onTabContents);
    }

    public static ResourceLocation getRl(String path) {
        return new ResourceLocation(KubeIOConstants.MOD_ID, path);
    }

    private static void onRegistration(RegisterEvent event) {
        if (event.getRegistryKey().equals(Registries.ITEM)) {
            KubePlugin.Events.CONDUIT_REGISTRY.post(new ConduitRegistryEvent());
        }
    }

    private static void onTabContents(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey() != EIOCreativeTabs.CONDUITS) return;

        for (CustomConduitEntry conduit : ConduitRegistryEvent.CONDUITS) {
            event.accept(conduit.item());
        }
    }
}

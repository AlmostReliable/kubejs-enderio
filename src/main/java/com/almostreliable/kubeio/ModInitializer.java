package com.almostreliable.kubeio;

import com.almostreliable.kubeio.core.EIOIntegration;
import com.almostreliable.kubeio.util.SmeltingFilterSynchronizer;
import com.enderio.api.integration.IntegrationManager;
import com.mojang.logging.LogUtils;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegisterEvent;
import org.slf4j.Logger;

@SuppressWarnings("UtilityClassWithPublicConstructor")
@Mod(KubeIOConstants.MOD_ID)
public final class ModInitializer {

    private static final Logger LOGGER = LogUtils.getLogger();

    public ModInitializer() {
        LOGGER.info("Loading EnderIO integration for KubeJS");
        FMLJavaModLoadingContext.get().getModEventBus().addListener(ModInitializer::onRegistration);
        IntegrationManager.addIntegration(new EIOIntegration());
    }

    public static ResourceLocation getRl(String path) {
        return new ResourceLocation(KubeIOConstants.MOD_ID, path);
    }

    private static void onRegistration(RegisterEvent event) {
        if (event.getRegistryKey().equals(Registries.RECIPE_TYPE)) {
            ForgeRegistries.RECIPE_TYPES.register(SmeltingFilterSynchronizer.ID, SmeltingFilterSynchronizer.TYPE);
        }
        if (event.getRegistryKey().equals(Registries.RECIPE_SERIALIZER)) {
            ForgeRegistries.RECIPE_SERIALIZERS.register(
                SmeltingFilterSynchronizer.ID,
                SmeltingFilterSynchronizer.SERIALIZER
            );
        }
    }
}

package com.almostreliable.kubeio;

import com.almostreliable.kubeio.recipe.AlloySmelterKubeRecipe;
import com.almostreliable.kubejs_enderio.ModConstants;

import net.neoforged.bus.api.EventPriority;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.NeoForge;

import com.mojang.logging.LogUtils;
import org.slf4j.Logger;

@Mod(ModConstants.MOD_ID)
public final class ModInitializer {

    private static final Logger LOGGER = LogUtils.getLogger();

    public ModInitializer() {
        LOGGER.info("Loading EnderIO integration for KubeJS");
        NeoForge.EVENT_BUS.addListener(EventPriority.LOWEST, AlloySmelterKubeRecipe::injectSmeltingRecipes); // after KubeJS
    }
}

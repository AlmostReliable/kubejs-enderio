package com.almostreliable.kubeio;

import com.almostreliable.kubejs_enderio.ModConstants;

import net.neoforged.fml.common.Mod;

import com.mojang.logging.LogUtils;
import org.slf4j.Logger;

@Mod(ModConstants.MOD_ID)
public final class ModInitializer {

    private static final Logger LOGGER = LogUtils.getLogger();

    public ModInitializer() {
        LOGGER.info("Loading EnderIO integration for KubeJS");
    }
}

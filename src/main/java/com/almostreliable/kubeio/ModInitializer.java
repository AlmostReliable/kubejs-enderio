package com.almostreliable.kubeio;

import com.almostreliable.kubeio.event.GrindingBallEvent;
import com.almostreliable.kubejs_enderio.ModConstants;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.event.ModifyDefaultComponentsEvent;

import com.enderio.enderio.api.EnderIODataComponents;
import com.mojang.logging.LogUtils;
import dev.latvian.mods.kubejs.script.ScriptType;
import org.slf4j.Logger;

@Mod(ModConstants.MOD_ID)
public final class ModInitializer {

    private static final Logger LOGGER = LogUtils.getLogger();

    public ModInitializer(IEventBus eventBus) {
        LOGGER.info("Loading EnderIO integration for KubeJS");
        eventBus.addListener(ModInitializer::onModifyComponents);
    }

    private static void onModifyComponents(ModifyDefaultComponentsEvent event) {
        if (KubePlugin.Events.GRINDING_BALLS.hasListeners()) {
            KubePlugin.Events.GRINDING_BALLS.post(ScriptType.STARTUP, new GrindingBallEvent());
            for (var grindingBallData : GrindingBallEvent.GRINDING_BALLS.entrySet()) {
                var item = grindingBallData.getKey();
                var data = grindingBallData.getValue();
                event.modify(
                    item, builder -> builder.set(EnderIODataComponents.GRINDING_BALL, data)
                );
            }
        }
    }
}

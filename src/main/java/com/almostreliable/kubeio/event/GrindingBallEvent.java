package com.almostreliable.kubeio.event;

import net.minecraft.world.item.Item;

import com.enderio.enderio.api.components.GrindingBallData;
import dev.latvian.mods.kubejs.event.KubeEvent;
import dev.latvian.mods.rhino.util.HideFromJS;

import java.util.HashMap;
import java.util.Map;

public class GrindingBallEvent implements KubeEvent {

    @HideFromJS
    public static final Map<Item, GrindingBallData> GRINDING_BALLS = new HashMap<>();

    public void add(Item item, float outputMultiplier, float bonusMultiplier, float powerUse, int durability) {
        var data = new GrindingBallData(outputMultiplier, bonusMultiplier, powerUse, durability);
        GRINDING_BALLS.put(item, data);
    }

    @SuppressWarnings("StaticMethodOnlyUsedInOneClass")
    @HideFromJS
    public static void clear() {
        GRINDING_BALLS.clear();
    }
}

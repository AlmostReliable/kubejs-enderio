package com.almostreliable.kubeio.event;

import net.minecraft.world.item.Item;

import com.enderio.enderio.api.components.GrindingBallData;
import dev.latvian.mods.kubejs.item.ItemPredicate;
import dev.latvian.mods.kubejs.script.data.VirtualDataMapFile;
import dev.latvian.mods.rhino.util.HideFromJS;

public class GrindingBallModificationEvent extends SimpleDataMapEvent<GrindingBallData> {

    @HideFromJS
    public GrindingBallModificationEvent(VirtualDataMapFile<Item, GrindingBallData> dataMap) {
        super(dataMap);
    }

    public void add(ItemPredicate item, float outputMultiplier, float bonusMultiplier, float powerUse, int durability) {
        var data = new GrindingBallData(outputMultiplier, bonusMultiplier, powerUse, durability);
        add(item, data);
    }
}

package com.almostreliable.kubeio.event;

import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

import dev.latvian.mods.kubejs.item.ItemPredicate;
import dev.latvian.mods.kubejs.script.data.VirtualDataMapFile;
import dev.latvian.mods.rhino.util.HideFromJS;

import java.util.Map;

public class VatReagentModificationEvent extends SimpleDataMapEvent<Map<TagKey<Item>, Double>> {

    @HideFromJS
    public VatReagentModificationEvent(VirtualDataMapFile<Item, Map<TagKey<Item>, Double>> dataMap) {
        super(dataMap);
    }

    public void add(ItemPredicate item, TagKey<Item> tag, double modifier) {
        var data = Map.of(tag, modifier);
        add(item, data);
    }
}

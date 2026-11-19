package com.almostreliable.kubeio.event;

import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;

import com.enderio.enderio.foundation.tag.EIOTags;
import dev.latvian.mods.kubejs.event.KubeEvent;
import dev.latvian.mods.kubejs.script.data.VirtualDataMapFile;
import dev.latvian.mods.rhino.util.HideFromJS;

import java.util.Map;

public class RangeExtenderModificationEvent implements KubeEvent {

    private final VirtualDataMapFile<Block, Map<TagKey<Block>, Integer>> dataMap;

    @HideFromJS
    public RangeExtenderModificationEvent(VirtualDataMapFile<Block, Map<TagKey<Block>, Integer>> dataMap) {
        this.dataMap = dataMap;
    }

    // the wireless charger only reads the value stored for the range extender tag
    public void add(Block block, int range) {
        dataMap.add(block, Map.of(EIOTags.Blocks.RANGE_EXTENDER, range));
    }

    public void remove(Block block) {
        dataMap.remove(block);
    }

    public void clear() {
        dataMap.clear();
    }
}

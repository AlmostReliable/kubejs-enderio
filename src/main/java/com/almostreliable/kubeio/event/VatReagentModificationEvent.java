package com.almostreliable.kubeio.event;

import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Ingredient;

import com.mojang.datafixers.util.Either;
import dev.latvian.mods.kubejs.event.KubeEvent;
import dev.latvian.mods.kubejs.item.ItemPredicate;
import dev.latvian.mods.kubejs.script.data.VirtualDataMapFile;

import java.util.Map;
import java.util.stream.Stream;

public record VatReagentModificationEvent(
    VirtualDataMapFile<Item, Map<TagKey<Item>, Double>> dataMap
) implements KubeEvent {

    public void add(ItemPredicate item, TagKey<Item> tag, double modifier) {
        dissolve(item)
            .ifLeft(t -> dataMap.addTag(t, Map.of(tag, modifier)))
            .ifRight(items -> items.forEach(i -> dataMap.add(i, Map.of(tag, modifier))));
    }

    public void remove(ItemPredicate item) {
        dissolve(item)
            .ifLeft(dataMap::removeTag)
            .ifRight(s -> s.forEach(dataMap::remove));
    }

    public void clear() {
        dataMap.clear();
    }

    private static Either<TagKey<Item>, Stream<Item>> dissolve(ItemPredicate filter) {
        var tag = filter instanceof Ingredient ingredient ? ingredient.kjs$getTagKey() : null;
        if (tag != null) return Either.left(tag);
        return Either.right(filter.kjs$getItemStream());
    }
}

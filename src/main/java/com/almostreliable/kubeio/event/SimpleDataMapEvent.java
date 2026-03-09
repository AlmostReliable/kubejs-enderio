package com.almostreliable.kubeio.event;

import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Ingredient;

import com.mojang.datafixers.util.Either;
import dev.latvian.mods.kubejs.event.KubeEvent;
import dev.latvian.mods.kubejs.item.ItemPredicate;
import dev.latvian.mods.kubejs.script.data.VirtualDataMapFile;

import java.util.stream.Stream;

public class SimpleDataMapEvent<T> implements KubeEvent {

    private final VirtualDataMapFile<Item, T> dataMap;

    SimpleDataMapEvent(VirtualDataMapFile<Item, T> dataMap) {
        this.dataMap = dataMap;
    }

    void add(ItemPredicate item, T data) {
        dissolve(item)
            .ifLeft(t -> dataMap.addTag(t, data))
            .ifRight(stream -> stream.forEach(i -> dataMap.add(i, data)));
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

package com.almostreliable.kubeio.event;

import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

import dev.latvian.mods.kubejs.event.KubeEvent;
import dev.latvian.mods.kubejs.item.ItemPredicate;
import dev.latvian.mods.kubejs.script.data.VirtualDataMapFile;
import dev.latvian.mods.rhino.Context;
import org.jspecify.annotations.Nullable;

@SuppressWarnings("unused")
public class SimpleDataMapEvent<T> implements KubeEvent {

    private final VirtualDataMapFile<Item, T> dataMap;

    SimpleDataMapEvent(VirtualDataMapFile<Item, T> dataMap) {
        this.dataMap = dataMap;
    }

    void add(Context cx, Object item, T data) {
        var tag = parseTag(item);
        if (tag != null) {
            dataMap.addTag(tag, data);
            return;
        }

        var holders = getHolders(cx, item);
        dataMap.add(holders, data);
    }

    public void remove(Context cx, Object item) {
        var tag = parseTag(item);
        if (tag != null) {
            dataMap.removeTag(tag);
            return;
        }

        var holders = getHolders(cx, item);
        dataMap.remove(holders);
    }

    public void clear() {
        dataMap.clear();
    }

    // tags aren't loaded yet, vanilla timing is awful :)
    @Nullable
    private static TagKey<Item> parseTag(Object item) {
        if (!(item instanceof CharSequence charSequence) || charSequence.isEmpty() || charSequence.charAt(0) != '#') return null;

        var tagId = Identifier.tryParse(charSequence.subSequence(1, charSequence.length()).toString());
        return tagId == null ? null : TagKey.create(Registries.ITEM, tagId);
    }

    // components aren't bound yet, can't create stacks
    private static HolderSet<Item> getHolders(Context cx, Object item) {
        var ingredient = ItemPredicate.wrap(cx, item).kjs$asIngredient();
        var customIngredient = ingredient.getCustomIngredient();
        if (customIngredient != null) return HolderSet.direct(customIngredient.items().toList());
        return ingredient.getValues();
    }
}

package com.almostreliable.kubeio.binding;

import com.almostreliable.kubeio.mixin.SagMillOutputItemMixin;
import com.enderio.machines.common.recipe.SagMillingRecipe;
import dev.latvian.mods.rhino.util.RemapPrefixForJS;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

/**
 * Implemented on {@link SagMillOutputItemMixin} to add utility methods.
 * The actual binding exposes the {@link SagMillingRecipe.OutputItem}.
 */
@RemapPrefixForJS("kubeio$")
public interface SagMillOutputItem {

    static SagMillingRecipe.OutputItem kubeio$of(ItemStack item) {
        return kubeio$of(item, 1);
    }

    static SagMillingRecipe.OutputItem kubeio$of(ItemStack item, float chance) {
        return SagMillingRecipe.OutputItem.of(item, chance, false);
    }

    static SagMillingRecipe.OutputItem kubeio$ofTag(TagKey<Item> tag, int count) {
        return kubeio$ofTag(tag, count, 1);
    }

    static SagMillingRecipe.OutputItem kubeio$ofTag(TagKey<Item> tag, int count, float chance) {
        return SagMillingRecipe.OutputItem.of(tag, count, chance, false);
    }
}

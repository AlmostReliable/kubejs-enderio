package com.almostreliable.kubeio.binding;

import com.almostreliable.kubeio.mixin.FireCraftingResultMixin;
import com.enderio.base.common.recipe.FireCraftingRecipe;
import dev.latvian.mods.rhino.util.RemapPrefixForJS;
import net.minecraft.world.item.ItemStack;

/**
 * Implemented on {@link FireCraftingResultMixin} to add utility methods.
 * The actual binding exposes the {@link FireCraftingRecipe.Result}.
 */
@RemapPrefixForJS("kubeio$")
public interface FireCraftingResult {

    static FireCraftingRecipe.Result kubeio$of(ItemStack item) {
        return kubeio$of(item, item.getCount());
    }

    static FireCraftingRecipe.Result kubeio$of(ItemStack item, int count) {
        return kubeio$of(item, count, count);
    }

    static FireCraftingRecipe.Result kubeio$of(ItemStack item, int minCount, int maxCount) {
        return kubeio$of(item, minCount, maxCount, 1);
    }

    static FireCraftingRecipe.Result kubeio$of(ItemStack item, int minCount, int maxCount, float chance) {
        return new FireCraftingRecipe.Result(item, minCount, maxCount, chance);
    }
}

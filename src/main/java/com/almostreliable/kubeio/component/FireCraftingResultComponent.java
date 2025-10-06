package com.almostreliable.kubeio.component;

import com.almostreliable.kubeio.binding.FireCraftingResult;
import com.enderio.base.common.recipe.FireCraftingRecipe;
import com.mojang.serialization.Codec;
import dev.latvian.mods.kubejs.item.ItemStackJS;
import dev.latvian.mods.kubejs.recipe.KubeRecipe;
import dev.latvian.mods.kubejs.recipe.component.RecipeComponent;
import dev.latvian.mods.kubejs.script.KubeJSContext;
import dev.latvian.mods.kubejs.util.RegistryAccessContainer;
import dev.latvian.mods.rhino.Context;
import dev.latvian.mods.rhino.type.TypeInfo;
import net.minecraft.world.item.ItemStack;

public record FireCraftingResultComponent() implements RecipeComponent<FireCraftingRecipe.Result> {

    public static final RecipeComponent<FireCraftingRecipe.Result> INSTANCE = new FireCraftingResultComponent();

    @Override
    public Codec<FireCraftingRecipe.Result> codec() {
        return FireCraftingRecipe.Result.CODEC;
    }

    @Override
    public TypeInfo typeInfo() {
        return TypeInfo.of(FireCraftingRecipe.Result.class).or(ItemStackJS.TYPE_INFO);
    }

    @Override
    public FireCraftingRecipe.Result wrap(Context cx, KubeRecipe recipe, Object from) {
        if (from instanceof FireCraftingRecipe.Result o) {
            return o;
        }

        RegistryAccessContainer registryAccess = ((KubeJSContext) cx).getRegistries();
        ItemStack itemStack = ItemStackJS.wrap(registryAccess, from);
        if (itemStack.isEmpty()) {
            throw new IllegalArgumentException("empty sag mill output: " + from);
        }

        return FireCraftingResult.kubeio$of(itemStack);
    }

    @Override
    public String toString() {
        return "enderio:fire_crafting_result";
    }
}

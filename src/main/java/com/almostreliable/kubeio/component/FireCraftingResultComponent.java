package com.almostreliable.kubeio.component;

import com.almostreliable.kubeio.binding.FireCraftingResult;

import com.enderio.enderio.EnderIO;
import com.enderio.enderio.content.fire_crafting.FireCraftingRecipe.Result;
import com.mojang.serialization.Codec;
import dev.latvian.mods.kubejs.error.InvalidRecipeComponentValueException;
import dev.latvian.mods.kubejs.plugin.builtin.wrapper.ItemWrapper;
import dev.latvian.mods.kubejs.recipe.RecipeScriptContext;
import dev.latvian.mods.kubejs.recipe.component.RecipeComponent;
import dev.latvian.mods.kubejs.recipe.component.RecipeComponentType;
import dev.latvian.mods.rhino.type.TypeInfo;
import org.jspecify.annotations.Nullable;

import net.minecraft.resources.ResourceKey;

public record FireCraftingResultComponent(ResourceKey<RecipeComponentType<?>> type) implements RecipeComponent<Result> {

    public static final FireCraftingResultComponent INSTANCE = new FireCraftingResultComponent(
        RecipeComponentType.key(EnderIO.id("fire_crafting_result"))
    );

    @Override
    public Codec<Result> codec() {
        return Result.CODEC;
    }

    @Override
    public TypeInfo typeInfo() {
        return TypeInfo.of(Result.class).or(ItemWrapper.TYPE_INFO);
    }

    @Override
    public Result wrap(RecipeScriptContext cx, @Nullable Object from) {
        if (from instanceof Result o) {
            return o;
        }

        var itemStack = ItemWrapper.wrap(cx.cx(), from);
        if (itemStack.isEmpty()) {
            var message = "fire crafting result must not be empty";
            throw new InvalidRecipeComponentValueException(message, this, from).source(cx.recipe().sourceLine);
        }

        return FireCraftingResult.kubeio$of(itemStack);
    }
}

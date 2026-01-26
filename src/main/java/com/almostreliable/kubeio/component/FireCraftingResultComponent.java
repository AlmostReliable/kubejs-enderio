package com.almostreliable.kubeio.component;

import com.almostreliable.kubeio.binding.FireCraftingResult;
import com.enderio.enderio.EnderIO;
import com.enderio.enderio.content.fire_crafting.FireCraftingRecipe.Result;
import com.mojang.serialization.Codec;
import dev.latvian.mods.kubejs.plugin.builtin.wrapper.ItemWrapper;
import dev.latvian.mods.kubejs.recipe.RecipeScriptContext;
import dev.latvian.mods.kubejs.recipe.component.RecipeComponent;
import dev.latvian.mods.kubejs.recipe.component.RecipeComponentType;
import dev.latvian.mods.rhino.type.TypeInfo;
import net.minecraft.world.item.ItemStack;

public record FireCraftingResultComponent(RecipeComponentType<?> type) implements RecipeComponent<Result> {

    public static final RecipeComponentType<Result> TYPE = RecipeComponentType.unit(
        EnderIO.rl("fire_crafting_result"),
        FireCraftingResultComponent::new
    );
    private static final Result EMPTY = FireCraftingResult.kubeio$of(ItemStack.EMPTY);

    @Override
    public Codec<Result> codec() {
        return Result.CODEC;
    }

    @Override
    public TypeInfo typeInfo() {
        return TypeInfo.of(Result.class).or(ItemWrapper.TYPE_INFO);
    }

    @Override
    public Result wrap(RecipeScriptContext cx, Object from) {
        if (from instanceof Result o) {
            return o;
        }

        ItemStack itemStack = ItemWrapper.wrap(cx.cx(), from);
        return itemStack.isEmpty() ? EMPTY : FireCraftingResult.kubeio$of(itemStack);
    }

    @Override
    public boolean isEmpty(Result value) {
        return value == EMPTY || value.result().isEmpty();
    }
}

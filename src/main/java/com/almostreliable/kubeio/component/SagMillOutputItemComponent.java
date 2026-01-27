package com.almostreliable.kubeio.component;

import com.almostreliable.kubeio.binding.SagMillOutputItem;
import com.almostreliable.kubeio.mixin.IngredientAccessor;
import com.almostreliable.kubeio.mixin.TagValueAccessor;
import com.enderio.enderio.EnderIO;
import com.enderio.enderio.content.machines.sag_mill.SagMillingRecipe;
import com.enderio.enderio.content.machines.sag_mill.SagMillingRecipe.OutputItem;
import com.mojang.serialization.Codec;
import dev.latvian.mods.kubejs.error.InvalidRecipeComponentValueException;
import dev.latvian.mods.kubejs.plugin.builtin.wrapper.IngredientWrapper;
import dev.latvian.mods.kubejs.plugin.builtin.wrapper.ItemWrapper;
import dev.latvian.mods.kubejs.plugin.builtin.wrapper.SizedIngredientWrapper;
import dev.latvian.mods.kubejs.recipe.RecipeScriptContext;
import dev.latvian.mods.kubejs.recipe.component.RecipeComponent;
import dev.latvian.mods.kubejs.recipe.component.RecipeComponentType;
import dev.latvian.mods.rhino.type.TypeInfo;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.neoforged.neoforge.common.crafting.SizedIngredient;

public record SagMillOutputItemComponent(RecipeComponentType<?> type) implements RecipeComponent<OutputItem> {

    public static final RecipeComponentType<OutputItem> TYPE = RecipeComponentType.unit(
        EnderIO.rl("sag_mill_output"),
        SagMillOutputItemComponent::new
    );
    private static final OutputItem EMPTY = SagMillOutputItem.kubeio$of(ItemStack.EMPTY);

    @Override
    public Codec<OutputItem> codec() {
        return SagMillingRecipe.OutputItem.CODEC;
    }

    @Override
    public TypeInfo typeInfo() {
        return TypeInfo.of(OutputItem.class)
            .or(SizedIngredientWrapper.TYPE_INFO)
            .or(IngredientWrapper.TYPE_INFO)
            .or(ItemWrapper.TYPE_INFO);
    }

    @Override
    public OutputItem wrap(RecipeScriptContext cx, Object from) {
        if (from instanceof OutputItem o) {
            return o;
        }

        SizedIngredient sizedIngredient = SizedIngredientWrapper.wrap(cx.cx(), from);
        var ingredientValues = ((IngredientAccessor) (Object) sizedIngredient.ingredient()).kubeio$getValues();
        if (ingredientValues.length == 0) {
            return EMPTY;
        }
        if (ingredientValues.length > 1) {
            throw new InvalidRecipeComponentValueException(
                "compound ingredients not supported in sag mill output",
                this,
                from
            ).source(cx.recipe().sourceLine);
        }

        var ingredientValue = ingredientValues[0];
        if (ingredientValue instanceof Ingredient.TagValue tagValue) {
            var tag = ((TagValueAccessor) (Object) tagValue).kubeio$getTag();
            return SagMillOutputItem.kubeio$ofTag(tag, sizedIngredient.count());
        }

        if (ingredientValue instanceof Ingredient.ItemValue itemValue) {
            var items = itemValue.getItems();
            if (items.isEmpty()) {
                return EMPTY;
            }
            if (items.size() > 1) {
                throw new InvalidRecipeComponentValueException(
                    "compound ingredients not supported in sag mill output",
                    this,
                    from
                ).source(cx.recipe().sourceLine);
            }

            ItemStack itemStack = new ItemStack(items.iterator().next().getItem(), sizedIngredient.count());
            return SagMillOutputItem.kubeio$of(itemStack);
        }

        ItemStack itemStack = ItemWrapper.wrap(cx.cx(), from);
        return itemStack.isEmpty() ? EMPTY : SagMillOutputItem.kubeio$of(itemStack);
    }

    @Override
    public boolean isEmpty(OutputItem value) {
        return value == EMPTY || !value.isPresent();
    }
}

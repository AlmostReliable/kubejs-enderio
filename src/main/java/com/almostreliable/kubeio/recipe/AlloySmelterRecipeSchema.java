package com.almostreliable.kubeio.recipe;

import com.almostreliable.kubeio.CommonRecipeKeys;
import com.enderio.machines.common.recipe.AlloySmeltingRecipe;
import com.enderio.machines.data.recipes.AlloyRecipeProvider;
import dev.latvian.mods.kubejs.item.OutputItem;
import dev.latvian.mods.kubejs.recipe.RecipeKey;
import dev.latvian.mods.kubejs.recipe.component.ItemComponents;
import dev.latvian.mods.kubejs.recipe.component.NumberComponent;
import dev.latvian.mods.kubejs.recipe.schema.RecipeSchema;

/**
 * See {@link AlloySmeltingRecipe.Serializer} and {@link AlloyRecipeProvider}.
 */
public interface AlloySmelterRecipeSchema extends CommonRecipeKeys {

    RecipeKey<OutputItem> RESULT = ItemComponents.OUTPUT.key("result").noBuilders();
    RecipeKey<Float> EXPERIENCE = NumberComponent.FLOAT.key("experience").optional(0f).alwaysWrite();

    RecipeSchema SCHEMA = new RecipeSchema(
        RESULT,
        MULTI_COUNTED_INPUT,
        ENERGY,
        EXPERIENCE
    );
}
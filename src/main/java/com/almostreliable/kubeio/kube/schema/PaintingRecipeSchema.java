package com.almostreliable.kubeio.kube.schema;

import com.almostreliable.kubeio.kube.recipe.CommonRecipeKeys;
import com.enderio.machines.common.recipe.PaintingRecipe;
import com.enderio.machines.data.recipes.PaintingRecipeProvider;
import dev.latvian.mods.kubejs.recipe.schema.RecipeSchema;

/**
 * See {@link PaintingRecipe.Serializer} and {@link PaintingRecipeProvider}.
 */
public interface PaintingRecipeSchema extends CommonRecipeKeys {

    RecipeSchema SCHEMA = new RecipeSchema(
        OUTPUT_STACK,
        SINGLE_INPUT
    );
}

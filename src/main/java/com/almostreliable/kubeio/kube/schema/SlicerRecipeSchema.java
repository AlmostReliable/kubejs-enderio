package com.almostreliable.kubeio.kube.schema;

import com.almostreliable.kubeio.kube.recipe.CommonRecipeKeys;
import com.enderio.machines.common.recipe.SlicingRecipe;
import com.enderio.machines.data.recipes.SlicingRecipeProvider;
import dev.latvian.mods.kubejs.recipe.RecipeExceptionJS;
import dev.latvian.mods.kubejs.recipe.RecipeJS;
import dev.latvian.mods.kubejs.recipe.schema.RecipeSchema;

/**
 * See {@link SlicingRecipe.Serializer} and {@link SlicingRecipeProvider}.
 */
public interface SlicerRecipeSchema extends CommonRecipeKeys {

    RecipeSchema SCHEMA = new RecipeSchema(
        SlicerRecipeJS.class,
        SlicerRecipeJS::new,
        OUTPUT_STACK,
        MULTI_INPUT,
        ENERGY
    );

    class SlicerRecipeJS extends RecipeJS {

        @Override
        public void afterLoaded() {
            super.afterLoaded();
            var inputs = getValue(MULTI_INPUT);
            if (inputs.length != 6) {
                throw new RecipeExceptionJS("Slicer recipe must have 6 inputs").error();
            }
        }
    }
}

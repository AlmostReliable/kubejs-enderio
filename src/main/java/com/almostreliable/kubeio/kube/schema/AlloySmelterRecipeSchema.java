package com.almostreliable.kubeio.kube.schema;

import com.almostreliable.kubeio.kube.KubePlugin;
import com.almostreliable.kubeio.kube.recipe.CommonRecipeKeys;
import com.almostreliable.kubeio.kube.recipe.RecipeComponents;
import com.enderio.core.common.recipes.CountedIngredient;
import com.enderio.machines.common.recipe.AlloySmeltingRecipe;
import com.enderio.machines.data.recipes.AlloyRecipeProvider;
import dev.latvian.mods.kubejs.recipe.RecipeJS;
import dev.latvian.mods.kubejs.recipe.RecipeKey;
import dev.latvian.mods.kubejs.recipe.component.BooleanComponent;
import dev.latvian.mods.kubejs.recipe.component.NumberComponent;
import dev.latvian.mods.kubejs.recipe.schema.RecipeSchema;

/**
 * See {@link AlloySmeltingRecipe.Serializer} and {@link AlloyRecipeProvider}.
 */
public interface AlloySmelterRecipeSchema extends CommonRecipeKeys {

    RecipeKey<CountedIngredient[]> MULTI_COUNTED_INPUT = RecipeComponents.COUNTED_INGREDIENT_ARRAY.key("inputs")
        .noBuilders();
    RecipeKey<Float> EXPERIENCE = NumberComponent.FLOAT.key("experience").optional(0f).alwaysWrite();
    RecipeKey<Boolean> IS_SMELTING = BooleanComponent.BOOLEAN.key("is_smelting").optional(false).noBuilders();

    RecipeSchema SCHEMA = new RecipeSchema(
        AlloySmelterRecipeJS.class,
        AlloySmelterRecipeJS::new,
        RESULT_STACK,
        MULTI_COUNTED_INPUT,
        ENERGY,
        EXPERIENCE,
        IS_SMELTING
    );

    class AlloySmelterRecipeJS extends RecipeJS {

        public AlloySmelterRecipeJS smelting() {
            setValue(IS_SMELTING, true);
            KubePlugin.SMELTING_RECIPES.add(getOrCreateId());
            return this;
        }
    }
}

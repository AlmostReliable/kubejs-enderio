package com.almostreliable.kubeio.kube.schema;

import com.almostreliable.kubeio.kube.recipe.CommonRecipeKeys;
import com.enderio.machines.common.recipe.TankRecipe;
import com.enderio.machines.data.recipes.TankRecipeProvider;
import dev.latvian.mods.kubejs.fluid.InputFluid;
import dev.latvian.mods.kubejs.recipe.RecipeJS;
import dev.latvian.mods.kubejs.recipe.RecipeKey;
import dev.latvian.mods.kubejs.recipe.component.BooleanComponent;
import dev.latvian.mods.kubejs.recipe.component.FluidComponents;
import dev.latvian.mods.kubejs.recipe.schema.RecipeSchema;

/**
 * See {@link TankRecipe.Serializer} and {@link TankRecipeProvider}.
 */
public interface TankRecipeSchema extends CommonRecipeKeys {

    RecipeKey<InputFluid> FLUID = FluidComponents.INPUT.key("fluid").noBuilders();
    RecipeKey<Boolean> IS_EMPTYING = BooleanComponent.BOOLEAN.key("is_emptying")
        .optional(false)
        .alwaysWrite()
        .noBuilders();

    RecipeSchema SCHEMA = new RecipeSchema(
        TankRecipeJS.class,
        TankRecipeJS::new,
        OUTPUT_STACK,
        SINGLE_INPUT,
        FLUID,
        IS_EMPTYING
    );

    class TankRecipeJS extends RecipeJS {

        public TankRecipeJS emptying() {
            setValue(IS_EMPTYING, true);
            return this;
        }
    }
}

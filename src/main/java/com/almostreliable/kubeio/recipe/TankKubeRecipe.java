package com.almostreliable.kubeio.recipe;

import com.almostreliable.kubeio.schema.TankRecipeSchema;
import com.enderio.enderio.content.storage.fluid_tank.TankRecipe;
import com.enderio.enderio.init.EIORecipes;
import dev.latvian.mods.kubejs.recipe.KubeRecipe;
import dev.latvian.mods.kubejs.recipe.schema.KubeRecipeFactory;

/**
 * See {@link TankRecipeSchema}.
 */
public class TankKubeRecipe extends KubeRecipe {

    public static final KubeRecipeFactory FACTORY = new KubeRecipeFactory(
        EIORecipes.TANK.type().getId(),
        TankKubeRecipe.class,
        TankKubeRecipe::new
    );

    public TankKubeRecipe emptying() {
        setValue(TankRecipeSchema.MODE, TankRecipe.Mode.EMPTY);
        return this;
    }
}

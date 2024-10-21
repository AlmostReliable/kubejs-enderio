package com.almostreliable.kubeio.recipe;

import com.almostreliable.kubeio.schema.TankRecipeSchema;
import com.enderio.machines.common.init.MachineRecipes;
import com.enderio.machines.common.recipe.TankRecipe;
import dev.latvian.mods.kubejs.recipe.KubeRecipe;
import dev.latvian.mods.kubejs.recipe.schema.KubeRecipeFactory;

/**
 * See {@link TankRecipeSchema}.
 */
public class TankKubeRecipe extends KubeRecipe {

    public static final KubeRecipeFactory FACTORY = new KubeRecipeFactory(
        MachineRecipes.TANK.type().getId(),
        TankKubeRecipe.class,
        TankKubeRecipe::new
    );

    public TankKubeRecipe emptying() {
        setValue(TankRecipeSchema.MODE, TankRecipe.Mode.EMPTY);
        return this;
    }
}

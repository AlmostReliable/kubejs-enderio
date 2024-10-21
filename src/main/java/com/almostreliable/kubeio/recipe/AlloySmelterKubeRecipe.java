package com.almostreliable.kubeio.recipe;

import com.almostreliable.kubeio.KubePlugin;
import com.almostreliable.kubeio.schema.AlloySmelterRecipeSchema;
import com.enderio.machines.common.init.MachineRecipes;
import dev.latvian.mods.kubejs.recipe.KubeRecipe;
import dev.latvian.mods.kubejs.recipe.schema.KubeRecipeFactory;

/**
 * See {@link AlloySmelterRecipeSchema}.
 */
public class AlloySmelterKubeRecipe extends KubeRecipe {

    public static final KubeRecipeFactory FACTORY = new KubeRecipeFactory(
        MachineRecipes.ALLOY_SMELTING.type().getId(),
        AlloySmelterKubeRecipe.class,
        AlloySmelterKubeRecipe::new
    );

    public AlloySmelterKubeRecipe smelting() {
        setValue(AlloySmelterRecipeSchema.IS_SMELTING, true);
        KubePlugin.SMELTING_RECIPES.add(getOrCreateId());
        return this;
    }
}

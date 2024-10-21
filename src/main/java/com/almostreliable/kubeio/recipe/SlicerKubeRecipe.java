package com.almostreliable.kubeio.recipe;

import com.almostreliable.kubeio.schema.SlicerRecipeSchema;
import com.enderio.machines.common.init.MachineRecipes;
import dev.latvian.mods.kubejs.recipe.KubeRecipe;
import dev.latvian.mods.kubejs.recipe.schema.KubeRecipeFactory;

/**
 * See {@link SlicerRecipeSchema}.
 */
public class SlicerKubeRecipe extends KubeRecipe {

    public static final KubeRecipeFactory FACTORY = new KubeRecipeFactory(
        MachineRecipes.SLICING.type().getId(),
        SlicerKubeRecipe.class,
        SlicerKubeRecipe::new
    );

    @Override
    public void afterLoaded() {
        super.afterLoaded();
        var inputs = getValue(SlicerRecipeSchema.INPUTS);
        if (inputs != null && inputs.size() != 6) {
            throw new IllegalArgumentException("slicer recipe must have exactly 6 inputs");
        }
    }
}

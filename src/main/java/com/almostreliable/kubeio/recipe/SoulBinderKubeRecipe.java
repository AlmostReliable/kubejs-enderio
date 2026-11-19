package com.almostreliable.kubeio.recipe;

import com.almostreliable.kubeio.schema.SoulBinderRecipeSchema;
import com.enderio.enderio.init.EIORecipes;
import dev.latvian.mods.kubejs.recipe.KubeRecipe;
import dev.latvian.mods.kubejs.recipe.schema.KubeRecipeFactory;

/**
 * See {@link SoulBinderRecipeSchema}.
 */
public class SoulBinderKubeRecipe extends KubeRecipe {

    public static final KubeRecipeFactory FACTORY = new KubeRecipeFactory(
        EIORecipes.SOUL_BINDING.type().getId(),
        SoulBinderKubeRecipe.class,
        SoulBinderKubeRecipe::new
    );

    public SoulBinderKubeRecipe copyInputComponents() {
        setValue(SoulBinderRecipeSchema.COPY_INPUT_COMPONENTS, true);
        return this;
    }
}

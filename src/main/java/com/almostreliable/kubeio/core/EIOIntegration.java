package com.almostreliable.kubeio.core;

import com.enderio.api.integration.Integration;
import net.minecraft.world.item.crafting.SmeltingRecipe;

public class EIOIntegration implements Integration {

    @Override
    public boolean acceptSmeltingRecipe(SmeltingRecipe recipe) {
        return !RecipesBinding.FILTERED_SMELTING_RECIPES.contains(recipe.getId());
    }
}

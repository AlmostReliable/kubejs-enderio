package com.almostreliable.kubeio.kube.recipe;

import com.enderio.base.common.init.EIORecipes;
import com.enderio.machines.common.init.MachineRecipes;
import dev.latvian.mods.kubejs.item.InputItem;
import dev.latvian.mods.kubejs.item.OutputItem;
import dev.latvian.mods.kubejs.recipe.RecipeKey;
import dev.latvian.mods.kubejs.recipe.component.ItemComponents;
import dev.latvian.mods.kubejs.recipe.component.NumberComponent;

/**
 * Commonly used RecipeKeys from {@link EIORecipes} and {@link MachineRecipes}.
 */
@SuppressWarnings("StaticMethodOnlyUsedInOneClass")
public interface CommonRecipeKeys {

    RecipeKey<InputItem> SINGLE_INPUT = ItemComponents.INPUT.key("input").noBuilders();
    RecipeKey<InputItem[]> MULTI_INPUT = ItemComponents.INPUT_ARRAY.key("inputs").noBuilders();
    RecipeKey<OutputItem> RESULT_STACK = ItemComponents.OUTPUT.key("result").noBuilders();
    RecipeKey<OutputItem> OUTPUT_STACK = ItemComponents.OUTPUT.key("output").noBuilders();
    RecipeKey<Integer> ENERGY = NumberComponent.INT.key("energy").optional(2_000).alwaysWrite();
}

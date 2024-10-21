package com.almostreliable.kubeio.schema;

import com.almostreliable.kubeio.recipe.SlicerKubeRecipe;
import com.enderio.machines.common.recipe.SlicingRecipe;
import com.enderio.machines.data.recipes.SlicingRecipeProvider;
import dev.latvian.mods.kubejs.recipe.RecipeKey;
import dev.latvian.mods.kubejs.recipe.component.ComponentRole;
import dev.latvian.mods.kubejs.recipe.component.IngredientComponent;
import dev.latvian.mods.kubejs.recipe.component.ItemStackComponent;
import dev.latvian.mods.kubejs.recipe.component.NumberComponent;
import dev.latvian.mods.kubejs.recipe.schema.RecipeSchema;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.List;

/**
 * See {@link SlicingRecipe.Serializer} and {@link SlicingRecipeProvider}.
 */
public interface SlicerRecipeSchema {

    RecipeKey<ItemStack> OUTPUT = ItemStackComponent.STRICT_ITEM_STACK
        .key("output", ComponentRole.OUTPUT)
        .noFunctions();
    RecipeKey<List<Ingredient>> INPUTS = IngredientComponent.INGREDIENT
        .asList()
        .key("inputs", ComponentRole.INPUT)
        .noFunctions();
    RecipeKey<Integer> ENERGY = NumberComponent.INT
        .key("energy", ComponentRole.OTHER)
        .optional(2_000)
        .alwaysWrite();

    RecipeSchema SCHEMA = new RecipeSchema(OUTPUT, INPUTS, ENERGY)
        .factory(SlicerKubeRecipe.FACTORY);
}

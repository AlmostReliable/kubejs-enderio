package com.almostreliable.kubeio.schema;

import com.enderio.machines.common.blocks.painting.PaintingRecipe;
import com.enderio.machines.data.recipes.PaintingRecipeProvider;
import dev.latvian.mods.kubejs.recipe.RecipeKey;
import dev.latvian.mods.kubejs.recipe.component.ComponentRole;
import dev.latvian.mods.kubejs.recipe.component.IngredientComponent;
import dev.latvian.mods.kubejs.recipe.component.ItemStackComponent;
import dev.latvian.mods.kubejs.recipe.schema.RecipeSchema;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;

/**
 * See {@link PaintingRecipe.Serializer} and {@link PaintingRecipeProvider}.
 */
public interface PaintingRecipeSchema {

    RecipeKey<ItemStack> OUTPUT = ItemStackComponent.STRICT_ITEM_STACK
        .key("output", ComponentRole.OUTPUT)
        .noFunctions();
    RecipeKey<Ingredient> INPUT = IngredientComponent.INGREDIENT
        .key("input", ComponentRole.INPUT)
        .noFunctions();

    RecipeSchema SCHEMA = new RecipeSchema(OUTPUT, INPUT);
}

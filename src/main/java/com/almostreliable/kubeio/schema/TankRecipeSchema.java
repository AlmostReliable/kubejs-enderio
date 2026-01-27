package com.almostreliable.kubeio.schema;

import com.almostreliable.kubeio.component.SimpleComponents;
import com.almostreliable.kubeio.recipe.TankKubeRecipe;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.neoforged.neoforge.fluids.crafting.SizedFluidIngredient;

import com.enderio.enderio.content.storage.fluid_tank.TankRecipe;
import dev.latvian.mods.kubejs.recipe.RecipeKey;
import dev.latvian.mods.kubejs.recipe.component.ComponentRole;
import dev.latvian.mods.kubejs.recipe.component.IngredientComponent;
import dev.latvian.mods.kubejs.recipe.component.ItemStackComponent;
import dev.latvian.mods.kubejs.recipe.component.SizedFluidIngredientComponent;
import dev.latvian.mods.kubejs.recipe.schema.RecipeSchema;

/**
 * See {@link TankRecipe.Serializer}.
 */
public interface TankRecipeSchema {

    RecipeKey<ItemStack> OUTPUT = ItemStackComponent.ITEM_STACK
        .key("output", ComponentRole.OUTPUT)
        .noFunctions();
    RecipeKey<Ingredient> INPUT = IngredientComponent.INGREDIENT
        .key("input", ComponentRole.INPUT)
        .noFunctions();
    RecipeKey<SizedFluidIngredient> FLUID = SizedFluidIngredientComponent.FLAT
        .key("fluid", ComponentRole.OTHER)
        .noFunctions();
    RecipeKey<TankRecipe.Mode> MODE = SimpleComponents.TANK_MODE
        .key("mode", ComponentRole.OTHER)
        .optional(TankRecipe.Mode.FILL)
        .alwaysWrite()
        .noFunctions();

    RecipeSchema SCHEMA = new RecipeSchema(OUTPUT, INPUT, FLUID, MODE)
        .factory(TankKubeRecipe.FACTORY);
}

package com.almostreliable.kubeio.schema;

import com.almostreliable.kubeio.component.SimpleComponents;
import com.almostreliable.kubeio.recipe.TankKubeRecipe;
import com.enderio.machines.common.blocks.fluid_tank.TankRecipe;
import com.enderio.machines.data.recipes.TankRecipeProvider;
import dev.latvian.mods.kubejs.recipe.RecipeKey;
import dev.latvian.mods.kubejs.recipe.component.ComponentRole;
import dev.latvian.mods.kubejs.recipe.component.FluidStackComponent;
import dev.latvian.mods.kubejs.recipe.component.IngredientComponent;
import dev.latvian.mods.kubejs.recipe.component.ItemStackComponent;
import dev.latvian.mods.kubejs.recipe.schema.RecipeSchema;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.neoforged.neoforge.fluids.FluidStack;

/**
 * See {@link TankRecipe.Serializer} and {@link TankRecipeProvider}.
 */
public interface TankRecipeSchema {

    RecipeKey<ItemStack> OUTPUT = ItemStackComponent.ITEM_STACK
        .key("output", ComponentRole.OUTPUT)
        .noFunctions();
    RecipeKey<Ingredient> INPUT = IngredientComponent.INGREDIENT
        .key("input", ComponentRole.INPUT)
        .noFunctions();
    RecipeKey<FluidStack> FLUID = FluidStackComponent.FLUID_STACK
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

package com.almostreliable.kubeio.schema;

import com.almostreliable.kubeio.component.SimpleComponents;
import com.almostreliable.kubeio.recipe.TankKubeRecipe;
import com.enderio.enderio.content.storage.fluid_tank.TankRecipe;
import dev.latvian.mods.kubejs.recipe.RecipeKey;
import dev.latvian.mods.kubejs.recipe.component.*;
import dev.latvian.mods.kubejs.recipe.schema.RecipeSchema;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.crafting.FluidIngredient;

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
    RecipeKey<FluidIngredient> FLUID = FluidIngredientComponent.FLUID_INGREDIENT
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

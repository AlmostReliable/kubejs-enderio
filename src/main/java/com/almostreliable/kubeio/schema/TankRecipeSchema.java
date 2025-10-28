package com.almostreliable.kubeio.schema;

import com.almostreliable.kubeio.recipe.TankKubeRecipe;
import com.enderio.base.api.EnderIO;
import com.enderio.machines.common.blocks.fluid_tank.TankRecipe;
import com.enderio.machines.data.recipes.TankRecipeProvider;
import dev.latvian.mods.kubejs.recipe.RecipeKey;
import dev.latvian.mods.kubejs.recipe.component.*;
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
    RecipeKey<TankRecipe.Mode> MODE = EnumComponent.of(
            EnderIO.loc("tank_mode"),
            TankRecipe.Mode.class,
            TankRecipe.Mode.CODEC
        )
        .key("mode", ComponentRole.OTHER)
        .optional(TankRecipe.Mode.FILL)
        .alwaysWrite()
        .noFunctions();

    RecipeSchema SCHEMA = new RecipeSchema(OUTPUT, INPUT, FLUID, MODE)
        .factory(TankKubeRecipe.FACTORY);
}

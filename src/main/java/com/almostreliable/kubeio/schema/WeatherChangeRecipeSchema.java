package com.almostreliable.kubeio.schema;

import com.almostreliable.kubeio.component.SimpleComponents;

import net.neoforged.neoforge.fluids.FluidStack;

import com.enderio.enderio.content.machines.obelisks.weather.WeatherChangeRecipe;
import dev.latvian.mods.kubejs.recipe.RecipeKey;
import dev.latvian.mods.kubejs.recipe.component.ComponentRole;
import dev.latvian.mods.kubejs.recipe.component.FluidStackComponent;
import dev.latvian.mods.kubejs.recipe.schema.RecipeSchema;

/**
 * See {@link WeatherChangeRecipe.Serializer}.
 */
public interface WeatherChangeRecipeSchema {

    RecipeKey<FluidStack> FLUID = FluidStackComponent.FLUID_STACK
        .key("fluid", ComponentRole.INPUT)
        .noFunctions();
    RecipeKey<WeatherChangeRecipe.WeatherMode> MODE = SimpleComponents.WEATHER_MODE
        .key("mode", ComponentRole.OTHER)
        .noFunctions();

    RecipeSchema SCHEMA = new RecipeSchema(FLUID, MODE);
}

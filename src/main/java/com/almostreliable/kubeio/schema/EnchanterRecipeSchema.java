package com.almostreliable.kubeio.schema;

import com.enderio.machines.common.blocks.enchanter.EnchanterRecipe;
import com.enderio.machines.data.recipes.EnchanterRecipeProvider;
import dev.latvian.mods.kubejs.recipe.RecipeKey;
import dev.latvian.mods.kubejs.recipe.component.ComponentRole;
import dev.latvian.mods.kubejs.recipe.component.NumberComponent;
import dev.latvian.mods.kubejs.recipe.component.RegistryComponent;
import dev.latvian.mods.kubejs.recipe.component.SizedIngredientComponent;
import dev.latvian.mods.kubejs.recipe.schema.RecipeSchema;
import dev.latvian.mods.kubejs.util.RegistryAccessContainer;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.enchantment.Enchantment;
import net.neoforged.neoforge.common.crafting.SizedIngredient;

import java.util.List;

/**
 * See {@link EnchanterRecipe.Serializer} and {@link EnchanterRecipeProvider}.
 */
public interface EnchanterRecipeSchema {

    RecipeKey<Holder<Enchantment>> ENCHANTMENT = new RegistryComponent<Enchantment>(
        RegistryAccessContainer.BUILTIN,
        Registries.ENCHANTMENT
    )
        .key("enchantment", ComponentRole.OTHER)
        .noFunctions();
    RecipeKey<SizedIngredient> INPUT = SizedIngredientComponent.FLAT
        .key("input", ComponentRole.INPUT)
        .noFunctions();
    RecipeKey<Integer> COST_MULTIPLIER = NumberComponent.INT
        .key("cost_multiplier", ComponentRole.OTHER)
        .functionNames(List.of("costMultiplier"))
        .optional(1)
        .alwaysWrite();

    RecipeSchema SCHEMA = new RecipeSchema(ENCHANTMENT, INPUT, COST_MULTIPLIER);
}

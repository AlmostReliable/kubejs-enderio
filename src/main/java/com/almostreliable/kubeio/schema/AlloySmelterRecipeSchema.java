package com.almostreliable.kubeio.schema;

import com.almostreliable.kubeio.recipe.AlloySmelterKubeRecipe;
import com.enderio.machines.common.recipe.AlloySmeltingRecipe;
import com.enderio.machines.data.recipes.AlloyRecipeProvider;
import dev.latvian.mods.kubejs.recipe.RecipeKey;
import dev.latvian.mods.kubejs.recipe.component.*;
import dev.latvian.mods.kubejs.recipe.schema.RecipeSchema;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.common.crafting.SizedIngredient;

import java.util.List;

/**
 * See {@link AlloySmeltingRecipe.Serializer} and {@link AlloyRecipeProvider}.
 */
public interface AlloySmelterRecipeSchema {

    RecipeKey<List<SizedIngredient>> INPUTS = SizedIngredientComponent.FLAT
        .asList()
        .key("inputs", ComponentRole.INPUT)
        .noFunctions();
    RecipeKey<ItemStack> OUTPUT = ItemStackComponent.STRICT_ITEM_STACK
        .key("output", ComponentRole.OUTPUT)
        .noFunctions();
    RecipeKey<Integer> ENERGY = NumberComponent.INT
        .key("energy", ComponentRole.OTHER)
        .optional(2_000)
        .alwaysWrite();
    RecipeKey<Float> EXPERIENCE = NumberComponent.FLOAT
        .key("experience", ComponentRole.OTHER)
        .optional(0f)
        .alwaysWrite();
    RecipeKey<Boolean> IS_SMELTING = BooleanComponent.BOOLEAN
        .key("is_smelting", ComponentRole.OTHER)
        .optional(false)
        .noFunctions();

    RecipeSchema SCHEMA = new RecipeSchema(OUTPUT, INPUTS, ENERGY, EXPERIENCE, IS_SMELTING)
        .factory(AlloySmelterKubeRecipe.FACTORY);
}

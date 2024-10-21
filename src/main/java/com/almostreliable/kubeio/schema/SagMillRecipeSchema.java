package com.almostreliable.kubeio.schema;

import com.almostreliable.kubeio.component.SagMillOutputItemComponent;
import com.enderio.machines.common.recipe.SagMillingRecipe;
import com.enderio.machines.data.recipes.SagMillRecipeProvider;
import dev.latvian.mods.kubejs.recipe.RecipeKey;
import dev.latvian.mods.kubejs.recipe.component.ComponentRole;
import dev.latvian.mods.kubejs.recipe.component.EnumComponent;
import dev.latvian.mods.kubejs.recipe.component.IngredientComponent;
import dev.latvian.mods.kubejs.recipe.component.NumberComponent;
import dev.latvian.mods.kubejs.recipe.schema.RecipeSchema;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.List;

/**
 * See {@link SagMillingRecipe.Serializer} and {@link SagMillRecipeProvider}.
 */
public interface SagMillRecipeSchema {

    RecipeKey<List<SagMillingRecipe.OutputItem>> OUTPUTS = SagMillOutputItemComponent.OUTPUT_ITEM
        .asList()
        .key("outputs", ComponentRole.OUTPUT)
        .noFunctions();
    RecipeKey<Ingredient> INPUT = IngredientComponent.NON_EMPTY_INGREDIENT
        .key("input", ComponentRole.INPUT)
        .noFunctions();
    RecipeKey<Integer> ENERGY = NumberComponent.INT
        .key("energy", ComponentRole.INPUT)
        .optional(2_000)
        .alwaysWrite();
    RecipeKey<SagMillingRecipe.BonusType> BONUS = EnumComponent.of(
            "enderio:bonus_type",
            SagMillingRecipe.BonusType.class,
            SagMillingRecipe.BonusType.CODEC
        )
        .key("bonus", ComponentRole.OTHER)
        .optional(SagMillingRecipe.BonusType.MULTIPLY_OUTPUT);

    RecipeSchema SCHEMA = new RecipeSchema(OUTPUTS, INPUT, ENERGY, BONUS);
}

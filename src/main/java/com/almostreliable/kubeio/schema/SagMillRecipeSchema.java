package com.almostreliable.kubeio.schema;

import com.almostreliable.kubeio.component.SagMillOutputItemComponent;
import com.almostreliable.kubeio.component.SimpleComponents;
import com.enderio.enderio.content.machines.sag_mill.SagMillingRecipe;
import dev.latvian.mods.kubejs.recipe.RecipeKey;
import dev.latvian.mods.kubejs.recipe.component.ComponentRole;
import dev.latvian.mods.kubejs.recipe.component.IngredientComponent;
import dev.latvian.mods.kubejs.recipe.component.NumberComponent;
import dev.latvian.mods.kubejs.recipe.schema.RecipeSchema;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.List;

/**
 * See {@link SagMillingRecipe.Serializer}.
 */
public interface SagMillRecipeSchema {

    RecipeKey<List<SagMillingRecipe.OutputItem>> OUTPUTS = SagMillOutputItemComponent.TYPE
        .instance()
        .asList()
        .key("outputs", ComponentRole.OUTPUT)
        .noFunctions();
    RecipeKey<Ingredient> INPUT = IngredientComponent.INGREDIENT
        .key("input", ComponentRole.INPUT)
        .noFunctions();
    RecipeKey<Integer> ENERGY = NumberComponent.INT
        .key("energy", ComponentRole.INPUT)
        .optional(2_000)
        .alwaysWrite();
    RecipeKey<SagMillingRecipe.BonusType> BONUS = SimpleComponents.BOUS_TYPE
        .key("bonus", ComponentRole.OTHER)
        .optional(SagMillingRecipe.BonusType.MULTIPLY_OUTPUT);

    RecipeSchema SCHEMA = new RecipeSchema(OUTPUTS, INPUT, ENERGY, BONUS);
}

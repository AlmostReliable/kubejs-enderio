package com.almostreliable.kubeio.schema;

import com.enderio.machines.common.blocks.vat.FermentingRecipe;
import com.enderio.machines.data.recipes.FermentingRecipeProvider;
import dev.latvian.mods.kubejs.recipe.RecipeKey;
import dev.latvian.mods.kubejs.recipe.component.*;
import dev.latvian.mods.kubejs.recipe.schema.RecipeSchema;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.crafting.SizedFluidIngredient;

/**
 * See {@link FermentingRecipe.Serializer} and {@link FermentingRecipeProvider}.
 */
public interface VatRecipeSchema {

    RecipeKey<FluidStack> OUTPUT = FluidStackComponent.FLUID_STACK
        .key("output", ComponentRole.OUTPUT)
        .noFunctions();
    RecipeKey<SizedFluidIngredient> INPUT = SizedFluidIngredientComponent.FLAT
        .key("input", ComponentRole.INPUT)
        .noFunctions();
    RecipeKey<TagKey<Item>> LEFT_REAGENT = TagKeyComponent.ITEM
        .key("left_reagent", ComponentRole.INPUT)
        .noFunctions();
    RecipeKey<TagKey<Item>> RIGHT_REAGENT = TagKeyComponent.ITEM
        .key("right_reagent", ComponentRole.INPUT)
        .noFunctions();
    RecipeKey<Integer> TICKS = NumberComponent.INT
        .key("ticks", ComponentRole.OTHER)
        .optional(60)
        .alwaysWrite();

    RecipeSchema SCHEMA = new RecipeSchema(OUTPUT, INPUT, LEFT_REAGENT, RIGHT_REAGENT, TICKS);
}

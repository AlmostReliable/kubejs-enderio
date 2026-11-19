package com.almostreliable.kubeio.schema;

import com.almostreliable.kubeio.component.EntityTypeIdComponent;
import com.almostreliable.kubeio.component.SimpleComponents;
import com.almostreliable.kubeio.component.SoulDataComponent;
import com.almostreliable.kubeio.recipe.SoulBinderKubeRecipe;

import com.enderio.enderio.content.machines.soul_binder.SoulBindingRecipe;
import dev.latvian.mods.kubejs.recipe.RecipeKey;
import dev.latvian.mods.kubejs.recipe.component.*;
import dev.latvian.mods.kubejs.recipe.schema.RecipeSchema;

import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.List;

/**
 * See {@link SoulBindingRecipe.Serializer}.
 */
public interface SoulBinderRecipeSchema {

    RecipeKey<ItemStack> OUTPUT = ItemStackComponent.ITEM_STACK
        .key("output", ComponentRole.OUTPUT)
        .noFunctions();
    RecipeKey<Ingredient> INPUT = IngredientComponent.INGREDIENT
        .key("input", ComponentRole.INPUT)
        .noFunctions();
    RecipeKey<Integer> ENERGY = NumberComponent.INT
        .key("energy", ComponentRole.OTHER)
        .optional(2_000)
        .alwaysWrite();
    RecipeKey<Integer> EXPERIENCE = NumberComponent.INT
        .key("experience", ComponentRole.OTHER)
        .optional(5)
        .alwaysWrite();
    RecipeKey<Identifier> ENTITY_TYPE = EntityTypeIdComponent.INSTANCE
        .key("entity_type", ComponentRole.OTHER)
        .functionNames(List.of("entityType"))
        .defaultOptional()
        .exclude();
    RecipeKey<MobCategory> MOB_CATEGORY = SimpleComponents.MOB_CATEGORY
        .key("mob_category", ComponentRole.OTHER)
        .functionNames(List.of("mobCategory"))
        .defaultOptional()
        .exclude();
    RecipeKey<String> SOUL_DATA = SoulDataComponent.INSTANCE
        .key("soul_data", ComponentRole.OTHER)
        .functionNames(List.of("soulData"))
        .defaultOptional()
        .exclude();
    RecipeKey<Boolean> COPY_INPUT_COMPONENTS = BooleanComponent.BOOLEAN
        .key("copyInputComponents", ComponentRole.OTHER)
        .optional(false)
        .noFunctions()
        .exclude();

    RecipeSchema SCHEMA = new RecipeSchema(
        OUTPUT,
        INPUT,
        ENERGY,
        EXPERIENCE,
        ENTITY_TYPE,
        MOB_CATEGORY,
        SOUL_DATA,
        COPY_INPUT_COMPONENTS
    ).factory(SoulBinderKubeRecipe.FACTORY);
}

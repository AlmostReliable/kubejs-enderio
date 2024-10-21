package com.almostreliable.kubeio.schema;

import com.enderio.machines.common.recipe.SoulBindingRecipe;
import com.enderio.machines.data.recipes.SoulBindingRecipeProvider;
import dev.latvian.mods.kubejs.recipe.RecipeKey;
import dev.latvian.mods.kubejs.recipe.component.*;
import dev.latvian.mods.kubejs.recipe.schema.RecipeSchema;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.List;

/**
 * See {@link SoulBindingRecipe.Serializer} and {@link SoulBindingRecipeProvider}.
 */
public interface SoulBinderRecipeSchema {

    RecipeKey<ItemStack> OUTPUT = ItemStackComponent.STRICT_ITEM_STACK
        .key("output", ComponentRole.OUTPUT)
        .noFunctions();
    RecipeKey<Ingredient> INPUT = IngredientComponent.NON_EMPTY_INGREDIENT
        .key("input", ComponentRole.INPUT)
        .noFunctions();
    RecipeKey<Integer> ENERGY = NumberComponent.INT
        .key("energy", ComponentRole.OTHER)
        .optional(2_000)
        .alwaysWrite();
    RecipeKey<Float> EXPERIENCE = NumberComponent.FLOAT
        .key("experience", ComponentRole.OTHER)
        .optional(5f)
        .alwaysWrite();
    RecipeKey<TagKey<EntityType<?>>> ENTITY_TYPE = TagKeyComponent.ENTITY_TYPE
        .key("entity_type", ComponentRole.OTHER)
        .functionNames(List.of("entityType"))
        .defaultOptional()
        .exclude();
    RecipeKey<MobCategory> MOB_CATEGORY = EnumComponent.of("mob_category", MobCategory.class, MobCategory.CODEC)
        .key("mob_category", ComponentRole.OTHER)
        .functionNames(List.of("mobCategory"))
        .defaultOptional()
        .exclude();
    RecipeKey<String> SOUL_DATA = StringComponent.NON_BLANK
        .key("soul_data", ComponentRole.OTHER)
        .functionNames(List.of("soulData"))
        .defaultOptional()
        .exclude();

    RecipeSchema SCHEMA = new RecipeSchema(OUTPUT, INPUT, ENERGY, EXPERIENCE, ENTITY_TYPE, MOB_CATEGORY, SOUL_DATA);
}

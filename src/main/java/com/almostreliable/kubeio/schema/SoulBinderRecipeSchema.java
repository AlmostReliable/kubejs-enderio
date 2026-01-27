package com.almostreliable.kubeio.schema;

import com.almostreliable.kubeio.component.SimpleComponents;
import com.enderio.enderio.content.machines.soul_binder.SoulBindingRecipe;
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
    RecipeKey<Float> EXPERIENCE = NumberComponent.FLOAT
        .key("experience", ComponentRole.OTHER)
        .optional(5f)
        .alwaysWrite();
    RecipeKey<TagKey<EntityType<?>>> ENTITY_TYPE = TagKeyComponent.ENTITY_TYPE
        .key("entity_type", ComponentRole.OTHER)
        .functionNames(List.of("entityType"))
        .defaultOptional()
        .exclude();
    RecipeKey<MobCategory> MOB_CATEGORY = SimpleComponents.MOB_CATEGORY
        .key("mob_category", ComponentRole.OTHER)
        .functionNames(List.of("mobCategory"))
        .defaultOptional()
        .exclude();
    RecipeKey<String> SOUL_DATA = StringComponent.STRING
        .key("soul_data", ComponentRole.OTHER)
        .functionNames(List.of("soulData"))
        .defaultOptional()
        .exclude();

    RecipeSchema SCHEMA = new RecipeSchema(OUTPUT, INPUT, ENERGY, EXPERIENCE, ENTITY_TYPE, MOB_CATEGORY, SOUL_DATA);
}

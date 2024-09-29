package com.almostreliable.kubeio.kube.schema;

import com.almostreliable.kubeio.kube.recipe.CommonRecipeKeys;
import com.enderio.machines.common.recipe.SoulBindingRecipe;
import com.enderio.machines.data.recipes.SoulBindingRecipeProvider;
import dev.latvian.mods.kubejs.recipe.RecipeKey;
import dev.latvian.mods.kubejs.recipe.component.EnumComponent;
import dev.latvian.mods.kubejs.recipe.component.NumberComponent;
import dev.latvian.mods.kubejs.recipe.component.StringComponent;
import dev.latvian.mods.kubejs.recipe.component.TagKeyComponent;
import dev.latvian.mods.kubejs.recipe.schema.RecipeSchema;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;

/**
 * See {@link SoulBindingRecipe.Serializer} and {@link SoulBindingRecipeProvider}.
 */
public interface SoulBinderRecipeSchema extends CommonRecipeKeys {

    RecipeKey<Integer> EXP = NumberComponent.INT.key("exp").optional(1).alwaysWrite();
    RecipeKey<TagKey<EntityType<?>>> ENTITY_TYPE = TagKeyComponent.ENTITY_TYPE.key("entity_type")
        .preferred("entityType")
        .defaultOptional();
    RecipeKey<MobCategory> MOB_CATEGORY = new EnumComponent<>(MobCategory.class).key("mob_category")
        .preferred("mobCategory")
        .defaultOptional();
    RecipeKey<String> SOUL_DATA = StringComponent.NON_BLANK.key("soul_data")
        .preferred("soulData")
        .defaultOptional();

    RecipeSchema SCHEMA = new RecipeSchema(
        OUTPUT_STACK,
        SINGLE_INPUT,
        ENERGY,
        EXP,
        ENTITY_TYPE,
        MOB_CATEGORY,
        SOUL_DATA
    );
}

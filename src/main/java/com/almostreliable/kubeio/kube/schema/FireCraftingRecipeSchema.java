package com.almostreliable.kubeio.kube.schema;

import com.almostreliable.kubeio.kube.recipe.RecipeComponents;
import com.enderio.base.common.recipe.FireCraftingRecipe;
import com.enderio.base.data.recipe.FireCraftingRecipeProvider;
import com.mojang.datafixers.util.Either;
import dev.latvian.mods.kubejs.recipe.RecipeJS;
import dev.latvian.mods.kubejs.recipe.RecipeKey;
import dev.latvian.mods.kubejs.recipe.component.NumberComponent;
import dev.latvian.mods.kubejs.recipe.component.StringComponent;
import dev.latvian.mods.kubejs.recipe.schema.RecipeSchema;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import org.apache.commons.lang3.ArrayUtils;

/**
 * See {@link FireCraftingRecipe.Serializer} and {@link FireCraftingRecipeProvider}.
 */
public interface FireCraftingRecipeSchema {

    RecipeKey<Either<Block, TagKey<Block>>[]> BASE_BLOCKS = RecipeComponents.BLOCK_OR_TAG_ARRAY.key("base_blocks")
        .noBuilders();
    RecipeKey<String> LOOT_TABLE = StringComponent.ID.key("loot_table").noBuilders();
    RecipeKey<Integer> MAX_ITEM_DROPS = NumberComponent.INT.key("max_item_drops")
        .preferred("maxItemDrops")
        .optional(1_000)
        .alwaysWrite();
    RecipeKey<String[]> DIMENSIONS = StringComponent.ID.asArray().key("dimensions")
        .optional(ArrayUtils.toArray("minecraft:overworld"))
        .alwaysWrite();

    RecipeSchema SCHEMA = new RecipeSchema(
        FireCraftingRecipeJS.class,
        FireCraftingRecipeJS::new,
        BASE_BLOCKS,
        LOOT_TABLE,
        MAX_ITEM_DROPS,
        DIMENSIONS
    );

    class FireCraftingRecipeJS extends RecipeJS {

        public FireCraftingRecipeJS dimension(ResourceLocation dimension) {
            setValue(DIMENSIONS, ArrayUtils.toArray(dimension.toString()));
            return this;
        }
    }
}

package com.almostreliable.kubeio.schema;

import com.almostreliable.kubeio.component.ResourceKeyComponent;
import com.almostreliable.kubeio.recipe.FireCraftingKubeRecipe;
import com.enderio.base.common.recipe.FireCraftingRecipe;
import com.enderio.base.data.recipe.FireCraftingRecipeProvider;
import dev.latvian.mods.kubejs.recipe.RecipeKey;
import dev.latvian.mods.kubejs.recipe.component.BlockComponent;
import dev.latvian.mods.kubejs.recipe.component.ComponentRole;
import dev.latvian.mods.kubejs.recipe.component.NumberComponent;
import dev.latvian.mods.kubejs.recipe.component.TagKeyComponent;
import dev.latvian.mods.kubejs.recipe.schema.RecipeSchema;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.storage.loot.LootTable;

import java.util.List;

/**
 * See {@link FireCraftingRecipe.Serializer} and {@link FireCraftingRecipeProvider}.
 */
public interface FireCraftingRecipeSchema {

    RecipeKey<ResourceKey<LootTable>> LOOT_TABLE = ResourceKeyComponent.LOOT_TABLE
        .key("loot_table", ComponentRole.OTHER)
        .noFunctions();
    RecipeKey<Block> BLOCK_AFTER_BURNING = BlockComponent.BLOCK
        .key("block_after_burning", ComponentRole.OUTPUT)
        .functionNames(List.of("blockAfterBurning"))
        .optional(Blocks.AIR)
        .allowEmpty()
        .exclude();
    RecipeKey<List<Block>> BASE_BLOCKS = BlockComponent.BLOCK
        .asList()
        .key("base_blocks", ComponentRole.INPUT)
        .defaultOptional()
        .allowEmpty()
        .noFunctions()
        .exclude();
    RecipeKey<List<TagKey<Block>>> BASE_TAGS = TagKeyComponent.BLOCK
        .asList()
        .key("base_tags", ComponentRole.INPUT)
        .defaultOptional()
        .allowEmpty()
        .noFunctions()
        .exclude();
    RecipeKey<List<ResourceKey<Level>>> DIMENSIONS = ResourceKeyComponent.DIMENSION
        .asList()
        .key("dimensions", ComponentRole.OTHER)
        .optional(List.of(Level.OVERWORLD))
        .alwaysWrite()
        .exclude();
    RecipeKey<Integer> MAX_ITEM_DROPS = NumberComponent.INT
        .key("max_item_drops", ComponentRole.OTHER)
        .functionNames(List.of("maxItemDrops", "limitDrops"))
        .optional(1_000)
        .alwaysWrite()
        .exclude();

    RecipeSchema SCHEMA = new RecipeSchema(
        LOOT_TABLE,
        BLOCK_AFTER_BURNING,
        BASE_BLOCKS,
        BASE_TAGS,
        DIMENSIONS,
        MAX_ITEM_DROPS
    ).factory(FireCraftingKubeRecipe.FACTORY);
}

package com.almostreliable.kubeio.recipe;

import com.almostreliable.kubeio.schema.FireCraftingRecipeSchema;
import com.enderio.base.common.init.EIORecipes;
import dev.latvian.mods.kubejs.recipe.KubeRecipe;
import dev.latvian.mods.kubejs.recipe.schema.KubeRecipeFactory;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;

import java.util.ArrayList;

/**
 * See {@link FireCraftingRecipeSchema}.
 */
public class FireCraftingKubeRecipe extends KubeRecipe {

    public static final KubeRecipeFactory FACTORY = new KubeRecipeFactory(
        EIORecipes.FIRE_CRAFTING.type().getId(),
        FireCraftingKubeRecipe.class,
        FireCraftingKubeRecipe::new
    );

    public FireCraftingKubeRecipe block(Block block) {
        var value = getValue(FireCraftingRecipeSchema.BASE_BLOCKS);
        if (value == null) value = new ArrayList<>();
        value.add(block);
        setValue(FireCraftingRecipeSchema.BASE_BLOCKS, value);

        return this;
    }

    public FireCraftingKubeRecipe blockTag(TagKey<Block> block) {
        var value = getValue(FireCraftingRecipeSchema.BASE_TAGS);
        if (value == null) value = new ArrayList<>();
        value.add(block);
        setValue(FireCraftingRecipeSchema.BASE_TAGS, value);

        return this;
    }

    @Override
    public void serialize() {
        // TODO: find a better approach to validate the recipe, exception is only printed to latest.log

        var baseBlocks = getValue(FireCraftingRecipeSchema.BASE_BLOCKS);
        var baseTags = getValue(FireCraftingRecipeSchema.BASE_TAGS);

        int count = 0;
        if (baseBlocks != null) count += baseBlocks.size();
        if (baseTags != null) count += baseTags.size();

        if (count == 0) {
            throw new IllegalArgumentException("fire crafting recipe must have at least one base block or tag");
        }

        super.serialize();
    }
}

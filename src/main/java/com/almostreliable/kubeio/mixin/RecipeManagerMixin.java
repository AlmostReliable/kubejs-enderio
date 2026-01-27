package com.almostreliable.kubeio.mixin;

import com.almostreliable.kubeio.KubePlugin;
import com.enderio.enderio.content.machines.alloy.AlloySmeltingRecipe;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMultimap;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.*;
import net.neoforged.neoforge.common.conditions.WithConditions;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(RecipeManager.class)
public class RecipeManagerMixin {

    @Inject(method = "lambda$apply$0", at = @At("HEAD"))
    private static void kubeio$injectSmeltingRecipes(
        ResourceLocation id, ImmutableMultimap.Builder<RecipeType<?>, RecipeHolder<?>> byTypeBuilder,
        ImmutableMap.Builder<ResourceLocation, RecipeHolder<?>> byNameBuilder, WithConditions<Recipe<?>> condRecipe,
        CallbackInfo ci
    ) {
        if (!KubePlugin.SMELTING_RECIPES.contains(id)) return;
        KubePlugin.SMELTING_RECIPES.remove(id);

        var recipe = condRecipe.carrier();
        if (!(recipe instanceof AlloySmeltingRecipe alloyRecipe)) return;

        var inputs = alloyRecipe.inputs();
        if (inputs.size() != 1 || inputs.getFirst().count() != 1) return;

        var input = inputs.getFirst().ingredient();
        var output = alloyRecipe.output();
        float experience = alloyRecipe.experience();
        var inheritedId = ResourceLocation.tryParse(id + "_inherited");
        if (inheritedId == null) return;

        var holder = new RecipeHolder<>(
            inheritedId,
            new SmeltingRecipe("", CookingBookCategory.MISC, input, output, experience, 200)
        );
        byTypeBuilder.put(RecipeType.SMELTING, holder);
        byNameBuilder.put(inheritedId, holder);
    }
}

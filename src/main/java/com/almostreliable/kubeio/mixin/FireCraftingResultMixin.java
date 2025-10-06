package com.almostreliable.kubeio.mixin;

import com.almostreliable.kubeio.binding.FireCraftingResult;
import com.enderio.base.common.recipe.FireCraftingRecipe;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(FireCraftingRecipe.Result.class)
public interface FireCraftingResultMixin extends FireCraftingResult {}

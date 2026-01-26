package com.almostreliable.kubeio.mixin;

import com.almostreliable.kubeio.binding.FireCraftingResult;
import com.enderio.enderio.content.fire_crafting.FireCraftingRecipe;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(FireCraftingRecipe.Result.class)
public interface FireCraftingResultMixin extends FireCraftingResult {}

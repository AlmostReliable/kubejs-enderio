package com.almostreliable.kubeio.mixin;

import com.enderio.machines.common.recipe.AlloySmeltingRecipe;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.common.crafting.SizedIngredient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.List;

@Mixin(AlloySmeltingRecipe.class)
public interface AlloySmeltingRecipeAccessor {

    @Accessor(remap = false)
    List<SizedIngredient> getInputs();

    @Accessor(remap = false)
    ItemStack getOutput();

    @Accessor(remap = false)
    float getExperience();
}

package com.almostreliable.kubeio.mixin;

import com.almostreliable.kubeio.binding.SagMillOutputItem;
import com.enderio.machines.common.recipe.SagMillingRecipe;
import dev.latvian.mods.rhino.util.HideFromJS;
import dev.latvian.mods.rhino.util.RemapForJS;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.throwables.MixinError;

@Mixin(SagMillingRecipe.OutputItem.class)
public abstract class SagMillOutputItemMixin implements SagMillOutputItem {

    @Shadow
    public static SagMillingRecipe.OutputItem of(ItemStack item, float chance, boolean optional) {
        throw new MixinError();
    }

    @HideFromJS
    @Shadow
    public static SagMillingRecipe.OutputItem of(Item item, int count, float chance, boolean optional) {
        throw new MixinError();
    }

    @RemapForJS("ofTag")
    @Shadow
    public static SagMillingRecipe.OutputItem of(TagKey<Item> tag, int count, float chance, boolean optional) {
        throw new MixinError();
    }
}

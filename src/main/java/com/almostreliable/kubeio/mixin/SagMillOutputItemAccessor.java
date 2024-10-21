package com.almostreliable.kubeio.mixin;

import com.enderio.machines.common.recipe.SagMillingRecipe;
import com.mojang.serialization.Codec;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.throwables.MixinError;

@Mixin(SagMillingRecipe.OutputItem.class)
public interface SagMillOutputItemAccessor {

    @Accessor(value = "CODEC", remap = false)
    static Codec<SagMillingRecipe.OutputItem> getCodec() {
        throw new MixinError();
    }
}

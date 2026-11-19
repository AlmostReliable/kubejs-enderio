package com.almostreliable.kubeio.component;

import com.enderio.enderio.EnderIO;
import com.enderio.enderio.foundation.souldata.SoulDataReloadListener;
import com.mojang.serialization.Codec;
import dev.latvian.mods.kubejs.error.InvalidRecipeComponentValueException;
import dev.latvian.mods.kubejs.recipe.RecipeScriptContext;
import dev.latvian.mods.kubejs.recipe.component.RecipeComponent;
import dev.latvian.mods.kubejs.recipe.component.RecipeComponentType;
import dev.latvian.mods.rhino.type.TypeInfo;

public record SoulDataComponent(RecipeComponentType<?> type) implements RecipeComponent<String> {

    public static final RecipeComponentType<String> TYPE = RecipeComponentType.unit(EnderIO.rl("soul_data"), SoulDataComponent::new);

    @Override
    public Codec<String> codec() {
        return Codec.STRING;
    }

    @Override
    public TypeInfo typeInfo() {
        return TypeInfo.STRING;
    }

    @Override
    public String wrap(RecipeScriptContext cx, Object from) {
        var soulData = RecipeComponent.super.wrap(cx, from);
        // noinspection ConstantValue
        if (SoulDataReloadListener.fromString(soulData) == null) {
            var message = "unknown soul data type: " + soulData;
            throw new InvalidRecipeComponentValueException(message, this, from).source(cx.recipe().sourceLine);
        }

        return soulData;
    }
}

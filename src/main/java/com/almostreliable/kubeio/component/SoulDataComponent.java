package com.almostreliable.kubeio.component;

import net.minecraft.resources.ResourceKey;

import com.enderio.enderio.EnderIO;
import com.enderio.enderio.foundation.souldata.SoulDataReloadListener;
import com.mojang.serialization.Codec;
import dev.latvian.mods.kubejs.error.InvalidRecipeComponentValueException;
import dev.latvian.mods.kubejs.recipe.RecipeScriptContext;
import dev.latvian.mods.kubejs.recipe.component.RecipeComponent;
import dev.latvian.mods.kubejs.recipe.component.RecipeComponentType;
import dev.latvian.mods.rhino.type.TypeInfo;
import org.jspecify.annotations.Nullable;

public record SoulDataComponent(ResourceKey<RecipeComponentType<?>> type) implements RecipeComponent<String> {

    public static final SoulDataComponent INSTANCE = new SoulDataComponent(RecipeComponentType.key(EnderIO.id("soul_data")));

    @Override
    public Codec<String> codec() {
        return Codec.STRING;
    }

    @Override
    public TypeInfo typeInfo() {
        return TypeInfo.STRING;
    }

    @Override
    public String wrap(RecipeScriptContext cx, @Nullable Object from) {
        var soulData = RecipeComponent.super.wrap(cx, from);
        // noinspection ConstantValue
        if (SoulDataReloadListener.fromString(soulData) == null) {
            var message = "unknown soul data type: " + soulData;
            throw new InvalidRecipeComponentValueException(message, this, from).source(cx.recipe().sourceLine);
        }

        return soulData;
    }
}

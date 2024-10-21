package com.almostreliable.kubeio.component;

import com.mojang.serialization.Codec;
import dev.latvian.mods.kubejs.recipe.KubeRecipe;
import dev.latvian.mods.kubejs.recipe.component.RecipeComponent;
import dev.latvian.mods.rhino.Context;
import dev.latvian.mods.rhino.type.TypeInfo;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.loot.LootTable;

public record ResourceKeyComponent<T>(Class<T> clazz, ResourceKey<Registry<T>> key) implements RecipeComponent<ResourceKey<T>> {

    public static final RecipeComponent<ResourceKey<Level>> DIMENSION = new ResourceKeyComponent<>(
        Level.class,
        Registries.DIMENSION
    );
    public static final RecipeComponent<ResourceKey<LootTable>> LOOT_TABLE = new ResourceKeyComponent<>(
        LootTable.class,
        Registries.LOOT_TABLE
    );

    @Override
    public Codec<ResourceKey<T>> codec() {
        return ResourceKey.codec(key);
    }

    @Override
    public TypeInfo typeInfo() {
        return TypeInfo.of(ResourceKey.class).withParams(TypeInfo.of(clazz));
    }

    @SuppressWarnings("unchecked")
    @Override
    public ResourceKey<T> wrap(Context cx, KubeRecipe recipe, Object from) {
        return (ResourceKey<T>) cx.jsToJava(from, typeInfo());
    }

    @Override
    public String toString() {
        return "enderio:resource_key<" + clazz.getSimpleName().toLowerCase() + ">";
    }
}

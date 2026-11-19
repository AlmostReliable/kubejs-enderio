package com.almostreliable.kubeio.component;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.EntityType;

import com.enderio.enderio.EnderIO;
import com.mojang.serialization.Codec;
import dev.latvian.mods.kubejs.error.InvalidRecipeComponentValueException;
import dev.latvian.mods.kubejs.recipe.RecipeScriptContext;
import dev.latvian.mods.kubejs.recipe.component.RecipeComponent;
import dev.latvian.mods.kubejs.recipe.component.RecipeComponentType;
import dev.latvian.mods.rhino.type.TypeInfo;
import org.jspecify.annotations.Nullable;

public record EntityTypeIdComponent(ResourceKey<RecipeComponentType<?>> type) implements RecipeComponent<Identifier> {

    public static final EntityTypeIdComponent INSTANCE = new EntityTypeIdComponent(RecipeComponentType.key(EnderIO.id("entity_type_id")));

    @Override
    public Codec<Identifier> codec() {
        return Identifier.CODEC;
    }

    @Override
    public TypeInfo typeInfo() {
        return TypeInfo.of(Identifier.class);
    }

    @Override
    public Identifier wrap(RecipeScriptContext cx, @Nullable Object from) {
        if (from instanceof EntityType<?> entityType) {
            return BuiltInRegistries.ENTITY_TYPE.getKey(entityType);
        }

        var id = RecipeComponent.super.wrap(cx, from);
        if (!BuiltInRegistries.ENTITY_TYPE.containsKey(id)) {
            var message = "unknown entity type: " + id;
            throw new InvalidRecipeComponentValueException(message, this, from).source(cx.recipe().sourceLine);
        }

        return id;
    }
}

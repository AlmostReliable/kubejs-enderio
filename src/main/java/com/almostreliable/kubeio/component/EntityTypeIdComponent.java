package com.almostreliable.kubeio.component;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;

import com.enderio.enderio.EnderIO;
import com.mojang.serialization.Codec;
import dev.latvian.mods.kubejs.error.InvalidRecipeComponentValueException;
import dev.latvian.mods.kubejs.recipe.RecipeScriptContext;
import dev.latvian.mods.kubejs.recipe.component.RecipeComponent;
import dev.latvian.mods.kubejs.recipe.component.RecipeComponentType;
import dev.latvian.mods.rhino.type.TypeInfo;

public record EntityTypeIdComponent(RecipeComponentType<?> type) implements RecipeComponent<ResourceLocation> {

    public static final RecipeComponentType<ResourceLocation> TYPE = RecipeComponentType.unit(
        EnderIO.rl("entity_type_id"),
        EntityTypeIdComponent::new
    );

    @Override
    public Codec<ResourceLocation> codec() {
        return ResourceLocation.CODEC;
    }

    @Override
    public TypeInfo typeInfo() {
        return TypeInfo.of(ResourceLocation.class);
    }

    @Override
    public ResourceLocation wrap(RecipeScriptContext cx, Object from) {
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

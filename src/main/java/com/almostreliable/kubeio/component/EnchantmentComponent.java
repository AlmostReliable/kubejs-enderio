package com.almostreliable.kubeio.component;

import com.enderio.enderio.EnderIO;
import com.mojang.serialization.Codec;
import dev.latvian.mods.kubejs.recipe.component.RecipeComponent;
import dev.latvian.mods.kubejs.recipe.component.RecipeComponentType;
import dev.latvian.mods.rhino.type.TypeInfo;

import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.enchantment.Enchantment;

public record EnchantmentComponent(ResourceKey<RecipeComponentType<?>> type) implements RecipeComponent<Holder<Enchantment>> {

    public static final EnchantmentComponent INSTANCE = new EnchantmentComponent(RecipeComponentType.key(EnderIO.id("enchantment")));

    @Override
    public Codec<Holder<Enchantment>> codec() {
        return Enchantment.CODEC;
    }

    @Override
    public TypeInfo typeInfo() {
        return TypeInfo.of(Holder.class).withParams(TypeInfo.of(Enchantment.class));
    }
}

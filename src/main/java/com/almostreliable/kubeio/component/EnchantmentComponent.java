package com.almostreliable.kubeio.component;

import com.enderio.base.api.EnderIO;
import com.mojang.serialization.Codec;
import dev.latvian.mods.kubejs.recipe.RecipeScriptContext;
import dev.latvian.mods.kubejs.recipe.component.RecipeComponent;
import dev.latvian.mods.kubejs.recipe.component.RecipeComponentType;
import dev.latvian.mods.rhino.type.TypeInfo;
import net.minecraft.core.Holder;
import net.minecraft.world.item.enchantment.Enchantment;

public record EnchantmentComponent(RecipeComponentType<?> type) implements RecipeComponent<Holder<Enchantment>> {

    public static final RecipeComponentType<Holder<Enchantment>> TYPE = RecipeComponentType.unit(
        EnderIO.loc("enchantment"),
        EnchantmentComponent::new
    );

    @Override
    public Codec<Holder<Enchantment>> codec() {
        return Enchantment.CODEC;
    }

    @Override
    public TypeInfo typeInfo() {
        return TypeInfo.of(Holder.class).withParams(TypeInfo.of(Enchantment.class));
    }

    @SuppressWarnings("unchecked")
    @Override
    public Holder<Enchantment> wrap(RecipeScriptContext cx, Object from) {
        return (Holder<Enchantment>) cx.cx().jsToJava(from, typeInfo());
    }
}


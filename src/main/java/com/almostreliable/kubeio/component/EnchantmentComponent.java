package com.almostreliable.kubeio.component;

import com.mojang.serialization.Codec;
import dev.latvian.mods.kubejs.recipe.KubeRecipe;
import dev.latvian.mods.kubejs.recipe.component.RecipeComponent;
import dev.latvian.mods.rhino.Context;
import dev.latvian.mods.rhino.type.TypeInfo;
import net.minecraft.core.Holder;
import net.minecraft.world.item.enchantment.Enchantment;

public record EnchantmentComponent() implements RecipeComponent<Holder<Enchantment>> {

    public static final RecipeComponent<Holder<Enchantment>> ENCHANTMENT = new EnchantmentComponent();

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
    public Holder<Enchantment> wrap(Context cx, KubeRecipe recipe, Object from) {
        return (Holder<Enchantment>) cx.jsToJava(from, typeInfo());
    }

    @Override
    public String toString() {
        return "enderio:enchantment";
    }
}

package com.almostreliable.kubeio.component;

import com.almostreliable.kubeio.binding.SagMillOutputItem;
import com.almostreliable.kubeio.mixin.IngredientAccessor;
import com.almostreliable.kubeio.mixin.SagMillOutputItemAccessor;
import com.almostreliable.kubeio.mixin.TagValueAccessor;
import com.enderio.machines.common.recipe.SagMillingRecipe;
import com.mojang.serialization.Codec;
import dev.latvian.mods.kubejs.bindings.SizedIngredientWrapper;
import dev.latvian.mods.kubejs.item.ItemStackJS;
import dev.latvian.mods.kubejs.recipe.KubeRecipe;
import dev.latvian.mods.kubejs.recipe.component.RecipeComponent;
import dev.latvian.mods.kubejs.script.KubeJSContext;
import dev.latvian.mods.kubejs.util.RegistryAccessContainer;
import dev.latvian.mods.rhino.Context;
import dev.latvian.mods.rhino.type.TypeInfo;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.neoforged.neoforge.common.crafting.SizedIngredient;

public record SagMillOutputItemComponent() implements RecipeComponent<SagMillingRecipe.OutputItem> {

    public static final RecipeComponent<SagMillingRecipe.OutputItem> OUTPUT_ITEM = new SagMillOutputItemComponent();

    @Override
    public Codec<SagMillingRecipe.OutputItem> codec() {
        return SagMillOutputItemAccessor.getCodec();
    }

    @Override
    public TypeInfo typeInfo() {
        return TypeInfo.of(SagMillingRecipe.OutputItem.class).or(ItemStackJS.TYPE_INFO);
    }

    @Override
    public SagMillingRecipe.OutputItem wrap(Context cx, KubeRecipe recipe, Object from) {
        if (from instanceof SagMillingRecipe.OutputItem o) {
            return o;
        }

        RegistryAccessContainer registryAccess = ((KubeJSContext) cx).getRegistries();

        SizedIngredient sizedIngredient = SizedIngredientWrapper.wrap(registryAccess, from);
        var ingredientValues = ((IngredientAccessor) (Object) sizedIngredient.ingredient()).kubeio$getValues();
        if (ingredientValues.length > 1) {
            throw new IllegalArgumentException("compound ingredients not supported in sag mill output: " + from);
        }

        var ingredientValue = ingredientValues[0];
        if (ingredientValue instanceof Ingredient.TagValue tagValue) {
            var tag = ((TagValueAccessor) (Object) tagValue).kubeio$getTag();
            return SagMillOutputItem.kubeio$ofTag(tag, sizedIngredient.count());
        }

        if (ingredientValue instanceof Ingredient.ItemValue itemValue) {
            var items = itemValue.getItems();
            if (items.size() > 1) {
                throw new IllegalArgumentException("compound ingredients not supported in sag mill output: " + from);
            }

            ItemStack itemStack = new ItemStack(items.iterator().next().getItem(), sizedIngredient.count());
            return SagMillOutputItem.kubeio$of(itemStack);
        }

        ItemStack itemStack = ItemStackJS.wrap(registryAccess, from);
        if (itemStack.isEmpty()) {
            throw new IllegalArgumentException("empty sag mill output: " + from);
        }

        return SagMillOutputItem.kubeio$of(itemStack);
    }

    @Override
    public String toString() {
        return "enderio:sag_mill_output";
    }
}

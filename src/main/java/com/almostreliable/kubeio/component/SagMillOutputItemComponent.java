package com.almostreliable.kubeio.component;

import com.almostreliable.kubeio.binding.SagMillOutputItem;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;

import com.enderio.enderio.EnderIO;
import com.enderio.enderio.content.machines.sag_mill.SagMillingRecipe;
import com.enderio.enderio.content.machines.sag_mill.SagMillingRecipe.OutputItem;
import com.mojang.serialization.Codec;
import dev.latvian.mods.kubejs.error.InvalidRecipeComponentValueException;
import dev.latvian.mods.kubejs.plugin.builtin.wrapper.IngredientWrapper;
import dev.latvian.mods.kubejs.plugin.builtin.wrapper.ItemWrapper;
import dev.latvian.mods.kubejs.plugin.builtin.wrapper.SizedIngredientWrapper;
import dev.latvian.mods.kubejs.recipe.RecipeScriptContext;
import dev.latvian.mods.kubejs.recipe.component.RecipeComponent;
import dev.latvian.mods.kubejs.recipe.component.RecipeComponentType;
import dev.latvian.mods.rhino.type.TypeInfo;
import org.jspecify.annotations.Nullable;

import java.util.regex.Pattern;

public record SagMillOutputItemComponent(ResourceKey<RecipeComponentType<?>> type) implements RecipeComponent<OutputItem> {

    public static final SagMillOutputItemComponent INSTANCE = new SagMillOutputItemComponent(
        RecipeComponentType.key(EnderIO.id("sag_mill_output"))
    );
    private static final Pattern SIZED_TAG_PATTERN = Pattern.compile("(?:(\\d+)\\s*x\\s+)?#(\\S+)");

    @Override
    public Codec<OutputItem> codec() {
        return SagMillingRecipe.OutputItem.CODEC;
    }

    @Override
    public TypeInfo typeInfo() {
        return TypeInfo.of(OutputItem.class)
            .or(SizedIngredientWrapper.TYPE_INFO)
            .or(IngredientWrapper.TYPE_INFO)
            .or(ItemWrapper.TYPE_INFO);
    }

    @Override
    public OutputItem wrap(RecipeScriptContext cx, @Nullable Object from) {
        if (from instanceof OutputItem o) {
            return o;
        }

        // sized ingredient wrapper drops item components, try stack wrapping first
        if (from instanceof ItemStack itemStack) {
            return wrapItemStack(cx, itemStack, from);
        }

        // sized ingredient wrapper rejects unknown tags, tag outputs are optional in the sag mill, try parsing tag first
        if (from instanceof CharSequence charSequence) {
            var tagOutput = parseTagOutput(charSequence);
            if (tagOutput != null) return tagOutput;
        }

        var sizedIngredient = SizedIngredientWrapper.wrap(cx.cx(), from);
        var ingredient = sizedIngredient.ingredient();
        if (ingredient.isCustom()) {
            var itemStack = ItemWrapper.wrap(cx.cx(), from);
            return wrapItemStack(cx, itemStack, from);
        }

        var values = ingredient.getValues();
        var tag = values.unwrapKey();
        if (tag.isPresent()) {
            return SagMillOutputItem.kubeio$ofTag(tag.get(), sizedIngredient.count());
        }

        if (values.size() != 1) {
            throw new InvalidRecipeComponentValueException(
                "compound ingredients not supported in sag mill output",
                this,
                from
            ).source(cx.recipe().sourceLine);
        }

        var template = new ItemStackTemplate(values.get(0), sizedIngredient.count());
        return OutputItem.of(template, 1, false);
    }

    @Nullable
    private static OutputItem parseTagOutput(CharSequence charSequence) {
        var matcher = SIZED_TAG_PATTERN.matcher(charSequence);
        if (!matcher.matches()) return null;

        var tagId = Identifier.tryParse(matcher.group(2));
        if (tagId == null) return null;

        var countGroup = matcher.group(1);
        var count = countGroup == null ? 1 : Integer.parseInt(countGroup);
        var tag = TagKey.create(Registries.ITEM, tagId);

        return SagMillOutputItem.kubeio$ofTag(tag, count);
    }

    private OutputItem wrapItemStack(RecipeScriptContext cx, ItemStack itemStack, Object from) {
        if (itemStack.isEmpty()) {
            throw new InvalidRecipeComponentValueException("sag mill output must not be empty", this, from).source(cx.recipe().sourceLine);
        }

        return SagMillOutputItem.kubeio$of(itemStack);
    }
}

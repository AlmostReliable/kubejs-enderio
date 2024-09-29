package com.almostreliable.kubeio.kube.recipe;

import com.almostreliable.kubeio.kube.KubePlugin;
import com.almostreliable.kubeio.mixin.accessor.IngredientAccessor;
import com.almostreliable.kubeio.mixin.accessor.TagValueAccessor;
import com.enderio.core.common.recipes.CountedIngredient;
import com.enderio.machines.common.recipe.SagMillingRecipe;
import com.google.common.base.Preconditions;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.mojang.datafixers.util.Either;
import dev.latvian.mods.kubejs.block.predicate.BlockIDPredicate;
import dev.latvian.mods.kubejs.item.InputItem;
import dev.latvian.mods.kubejs.item.OutputItem;
import dev.latvian.mods.kubejs.recipe.RecipeJS;
import dev.latvian.mods.kubejs.recipe.ReplacementMatch;
import dev.latvian.mods.kubejs.recipe.component.RecipeComponent;
import dev.latvian.mods.kubejs.util.UtilsJS;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.util.GsonHelper;
import net.minecraft.util.Mth;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.registries.ForgeRegistries;

@SuppressWarnings("StaticMethodOnlyUsedInOneClass")
public interface RecipeComponents {

    RecipeComponent<CountedIngredient> COUNTED_INGREDIENT = new RecipeComponent<>() {

        @Override
        public Class<?> componentClass() {
            return CountedIngredient.class;
        }

        @Override
        public String componentType() {
            return "counted_ingredient";
        }

        @Override
        public JsonElement write(RecipeJS recipe, CountedIngredient value) {
            return value.toJson();
        }

        @Override
        public CountedIngredient read(RecipeJS recipe, Object from) {
            return KubePlugin.wrapCountedIngredient(from);
        }

        @Override
        public boolean isInput(RecipeJS recipe, CountedIngredient value, ReplacementMatch match) {
            return true;
        }
    };

    RecipeComponent<CountedIngredient[]> COUNTED_INGREDIENT_ARRAY = COUNTED_INGREDIENT.asArray();

    RecipeComponent<SagMillingRecipe.OutputItem> OUTPUT_ITEM = new RecipeComponent<>() {

        @Override
        public Class<?> componentClass() {
            return SagMillingRecipe.OutputItem.class;
        }

        @Override
        public String componentType() {
            return "output_item";
        }

        @Override
        public JsonElement write(RecipeJS recipe, SagMillingRecipe.OutputItem value) {
            return value.toJson();
        }

        @SuppressWarnings("CastToIncompatibleInterface")
        @Override
        public SagMillingRecipe.OutputItem read(RecipeJS recipe, Object from) {
            if (from instanceof SagMillingRecipe.OutputItem outputItem) {
                return outputItem;
            }

            if (from instanceof Ingredient || from instanceof InputItem || from instanceof String) {
                InputItem inputItem = InputItem.of(from);
                int count = inputItem.count;

                var ingredientValues = ((IngredientAccessor) inputItem.ingredient).kubeio$getValues();
                if (ingredientValues.length > 1) {
                    throw new IllegalArgumentException("Input item has more than one value");
                }
                var ingredientValue = ingredientValues[0];

                if (ingredientValue instanceof Ingredient.TagValue tagValue) {
                    var tag = ((TagValueAccessor) tagValue).kubeio$getTag();
                    return SagMillingRecipe.OutputItem.of(tag, count, 1f, false);
                }

                if (ingredientValue instanceof Ingredient.ItemValue itemValue) {
                    var items = itemValue.getItems();
                    if (items.size() > 1) {
                        throw new IllegalArgumentException("Input item has more than one item");
                    }
                    return SagMillingRecipe.OutputItem.of(items.iterator().next().getItem(), count, 1f, false);
                }
            }

            if (from instanceof JsonObject jsonObject) {
                int count = GsonHelper.getAsInt(jsonObject, "count", 1);
                float chance = GsonHelper.getAsFloat(jsonObject, "chance", 1.0f);
                boolean optional = GsonHelper.getAsBoolean(jsonObject, "optional", false);

                if (jsonObject.has("tag")) {
                    ResourceLocation id = new ResourceLocation(jsonObject.get("tag").getAsString());
                    TagKey<Item> tag = ItemTags.create(id);

                    return SagMillingRecipe.OutputItem.of(tag, count, chance, optional);
                }

                if (jsonObject.has("item")) {
                    ResourceLocation id = new ResourceLocation(jsonObject.get("item").getAsString());
                    Item item = ForgeRegistries.ITEMS.getValue(id);

                    if (!optional && item == null) {
                        throw new IllegalArgumentException("Recipe is missing a required output item");
                    }

                    return SagMillingRecipe.OutputItem.of(item, count, chance, optional);
                }

                throw new IllegalArgumentException("Invalid output item: " + jsonObject);
            }

            OutputItem outputItem = OutputItem.of(from);
            float chance = Double.isNaN(outputItem.getChance()) ? 1.0f : (float) outputItem.getChance();
            chance = Mth.clamp(chance, 0.0f, 1.0f);

            return SagMillingRecipe.OutputItem.of(outputItem.item.getItem(), outputItem.getCount(), chance, false);
        }

        @Override
        public boolean isOutput(RecipeJS recipe, SagMillingRecipe.OutputItem value, ReplacementMatch match) {
            return true;
        }
    };

    RecipeComponent<SagMillingRecipe.OutputItem[]> OUTPUT_ITEM_ARRAY = OUTPUT_ITEM.asArray();

    RecipeComponent<Item> ITEM = new RecipeComponent<>() {

        @Override
        public Class<?> componentClass() {
            return Item.class;
        }

        @Override
        public String componentType() {
            return "item";
        }

        @Override
        public JsonElement write(RecipeJS recipe, Item value) {
            return new JsonPrimitive(Preconditions.checkNotNull(ForgeRegistries.ITEMS.getKey(value)).toString());
        }

        @Override
        public Item read(RecipeJS recipe, Object from) {
            OutputItem outputItem = OutputItem.of(from);
            return outputItem.item.getItem();
        }

        @Override
        public boolean isOutput(RecipeJS recipe, Item value, ReplacementMatch match) {
            return true;
        }
    };

    RecipeComponent<Either<Block, TagKey<Block>>> BLOCK_OR_TAG = new RecipeComponent<>() {

        @Override
        public Class<?> componentClass() {
            return Either.class;
        }

        @Override
        public String componentType() {
            return "block_or_tag";
        }

        @Override
        public JsonElement write(RecipeJS recipe, Either<Block, TagKey<Block>> value) {
            JsonObject jsonObject = new JsonObject();

            value.left().ifPresent(block -> {
                ResourceLocation blockId = ForgeRegistries.BLOCKS.getKey(block);
                jsonObject.addProperty("block", Preconditions.checkNotNull(blockId).toString());
            });
            value.right().ifPresent(tag -> jsonObject.addProperty("tag", tag.location().toString()));

            return jsonObject;
        }

        @Override
        public Either<Block, TagKey<Block>> read(RecipeJS recipe, Object from) {
            if (from instanceof BlockIDPredicate blockPredicate) {
                return read(recipe, blockPredicate.getBlockState());
            }

            if (from instanceof BlockState blockState) {
                return read(recipe, blockState.getBlock());
            }

            if (from instanceof Block block) {
                return Either.left(block);
            }

            if (from instanceof JsonObject jsonObject) {
                if (jsonObject.has("block")) {
                    String id = jsonObject.get("block").getAsString();
                    Block block = ForgeRegistries.BLOCKS.getValue(new ResourceLocation(id));
                    if (block == null) {
                        throw new IllegalArgumentException("Unknown block: " + id);
                    }
                    return Either.left(block);
                }

                if (jsonObject.has("tag")) {
                    String id = jsonObject.get("tag").getAsString();
                    TagKey<Block> tag = BlockTags.create(new ResourceLocation(id));
                    return Either.right(tag);
                }

                throw new IllegalArgumentException("Invalid block or block tag: " + jsonObject);
            }

            if (from instanceof String id) {
                if (id.startsWith("#")) {
                    TagKey<Block> tag = BlockTags.create(new ResourceLocation(id.substring(1)));
                    return Either.right(tag);
                }

                BlockState state = UtilsJS.parseBlockState(id);
                if (state == null || state.isAir()) {
                    throw new IllegalArgumentException("Unknown block: " + id);
                }
                return Either.left(state.getBlock());
            }

            throw new IllegalArgumentException("Invalid block or block tag: " + from);
        }
    };

    RecipeComponent<Either<Block, TagKey<Block>>[]> BLOCK_OR_TAG_ARRAY = BLOCK_OR_TAG.asArray();

    RecipeComponent<Enchantment> ENCHANTMENT = new RecipeComponent<>() {

        @Override
        public Class<?> componentClass() {
            return Enchantment.class;
        }

        @Override
        public String componentType() {
            return "enchantment";
        }

        @Override
        public JsonElement write(RecipeJS recipe, Enchantment value) {
            ResourceLocation enchantmentId = ForgeRegistries.ENCHANTMENTS.getKey(value);
            return new JsonPrimitive(Preconditions.checkNotNull(enchantmentId).toString());
        }

        @Override
        public Enchantment read(RecipeJS recipe, Object from) {
            if (from instanceof Enchantment enchantment) {
                return enchantment;
            }

            if (from instanceof JsonPrimitive primitive) {
                return read(recipe, primitive.getAsString());
            }

            if (from instanceof String enchantmentId) {
                Enchantment enchantment = ForgeRegistries.ENCHANTMENTS.getValue(new ResourceLocation(enchantmentId));
                if (enchantment == null) {
                    throw new IllegalArgumentException("Unknown enchantment: " + enchantmentId);
                }
                return enchantment;
            }

            throw new IllegalArgumentException("Invalid enchantment: " + from);
        }
    };
}

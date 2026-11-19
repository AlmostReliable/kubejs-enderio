package com.almostreliable.kubeio.recipe;

import com.almostreliable.kubeio.schema.AlloySmelterRecipeSchema;

import net.minecraft.resources.Identifier;
import net.minecraft.world.item.crafting.Recipe;
import net.neoforged.neoforge.event.ModifyRecipeJsonsEvent;

import com.enderio.enderio.content.machines.alloy.AlloySmeltingRecipe;
import com.enderio.enderio.init.EIORecipeTypes;
import com.google.gson.JsonObject;
import dev.latvian.mods.kubejs.recipe.KubeRecipe;
import dev.latvian.mods.kubejs.recipe.schema.KubeRecipeFactory;
import dev.latvian.mods.rhino.util.HideFromJS;

import java.util.HashSet;
import java.util.Set;

/**
 * See {@link AlloySmelterRecipeSchema}.
 */
public class AlloySmelterKubeRecipe extends KubeRecipe {

    public static final KubeRecipeFactory FACTORY = new KubeRecipeFactory(
        EIORecipeTypes.ALLOY_SMELTING.getId(),
        AlloySmelterKubeRecipe.class,
        AlloySmelterKubeRecipe::new
    );
    private static final Set<Identifier> SMELTING_RECIPES = new HashSet<>();

    public AlloySmelterKubeRecipe smelting() {
        setValue(AlloySmelterRecipeSchema.IS_SMELTING, true);
        SMELTING_RECIPES.add(getOrCreateId());
        return this;
    }

    @HideFromJS
    public static void injectSmeltingRecipes(ModifyRecipeJsonsEvent event) {
        var ops = event.getOps();
        var recipeJsons = event.getRecipeJsons();

        for (var id : SMELTING_RECIPES) {
            var json = recipeJsons.get(id);
            if (json == null) continue;

            var recipe = Recipe.CODEC.parse(ops, json).result().orElse(null);
            if (!(recipe instanceof AlloySmeltingRecipe alloyRecipe)) continue;

            var inputs = alloyRecipe.inputs();
            if (inputs.size() != 1 || inputs.getFirst().count() != 1) continue;

            var alloyJson = json.getAsJsonObject();
            var inputJson = alloyJson.getAsJsonArray("inputs").get(0).getAsJsonObject();

            var smeltingJson = new JsonObject();
            smeltingJson.addProperty("type", "minecraft:smelting");
            smeltingJson.add("ingredient", inputJson.get("ingredient"));
            smeltingJson.add("result", alloyJson.get("output"));
            smeltingJson.addProperty("experience", alloyRecipe.experience());
            smeltingJson.addProperty("cookingtime", 200);

            recipeJsons.put(id.withSuffix("_inherited"), smeltingJson);
        }

        SMELTING_RECIPES.clear();
    }
}

package com.almostreliable.kubeio.kube;

import com.almostreliable.kubeio.enderio.conduit.CustomConduitEntry;
import com.almostreliable.kubeio.kube.event.ConduitRegistryEvent;
import com.almostreliable.kubeio.kube.schema.*;
import com.enderio.EnderIO;
import com.enderio.base.common.init.EIORecipes;
import com.enderio.core.common.recipes.CountedIngredient;
import com.enderio.core.common.recipes.RecipeTypeSerializerPair;
import com.enderio.machines.common.init.MachineRecipes;
import com.enderio.machines.common.recipe.SagMillingRecipe;
import com.google.gson.JsonObject;
import dev.latvian.mods.kubejs.KubeJSPlugin;
import dev.latvian.mods.kubejs.client.LangEventJS;
import dev.latvian.mods.kubejs.event.EventGroup;
import dev.latvian.mods.kubejs.event.EventHandler;
import dev.latvian.mods.kubejs.generator.AssetJsonGenerator;
import dev.latvian.mods.kubejs.item.InputItem;
import dev.latvian.mods.kubejs.recipe.schema.RecipeNamespace;
import dev.latvian.mods.kubejs.recipe.schema.RecipeSchema;
import dev.latvian.mods.kubejs.recipe.schema.RegisterRecipeSchemasEvent;
import dev.latvian.mods.kubejs.script.BindingsEvent;
import dev.latvian.mods.kubejs.script.ScriptType;
import dev.latvian.mods.kubejs.util.MapJS;
import dev.latvian.mods.rhino.util.wrap.TypeWrappers;
import net.minecraft.world.entity.MobCategory;

import java.util.Map;

public class KubePlugin extends KubeJSPlugin {

    @Override
    public void registerEvents() {
        Events.GROUP.register();
    }

    @Override
    public void registerBindings(BindingsEvent event) {
        if (event.getType().isServer()) {
            event.add("MobCategory", MobCategory.class);
            event.add("EnderIOBonusType", SagMillingRecipe.BonusType.class);
        }
    }

    @Override
    public void registerTypeWrappers(ScriptType type, TypeWrappers typeWrappers) {
        typeWrappers.register(CountedIngredient.class, (cx, o) -> wrapCountedIngredient(o));
    }

    @Override
    public void generateAssetJsons(AssetJsonGenerator generator) {
        for (CustomConduitEntry conduit : ConduitRegistryEvent.CONDUITS) {
            generator.itemModel(EnderIO.loc(conduit.id()), modelGenerator -> {
                modelGenerator.parent(EnderIO.loc("item/conduit").toString());
                modelGenerator.texture("0", EnderIO.loc("block/conduit/" + conduit.id()).toString());
            });
        }
    }

    @Override
    public void generateLang(LangEventJS event) {
        for (CustomConduitEntry conduit : ConduitRegistryEvent.CONDUITS) {
            event.add("item." + EnderIO.MODID + "." + conduit.id(), conduit.name());
        }
    }

    @Override
    public void registerRecipeSchemas(RegisterRecipeSchemasEvent event) {
        Map<RecipeTypeSerializerPair<?, ?>, RecipeSchema> basicRecipeSchemas = Map.of(
            EIORecipes.FIRE_CRAFTING, FireCraftingRecipeSchema.SCHEMA,
            EIORecipes.GRINDING_BALL, GrindingBallRecipeSchema.SCHEMA
        );

        Map<RecipeTypeSerializerPair<?, ?>, RecipeSchema> machineRecipeSchemas = Map.of(
            MachineRecipes.ALLOY_SMELTING, AlloySmelterRecipeSchema.SCHEMA,
            MachineRecipes.ENCHANTING, EnchanterRecipeSchema.SCHEMA,
            MachineRecipes.PAINTING, PaintingRecipeSchema.SCHEMA,
            MachineRecipes.SAG_MILLING, SagMillRecipeSchema.SCHEMA,
            MachineRecipes.SLICING, SlicerRecipeSchema.SCHEMA,
            MachineRecipes.SOUL_BINDING, SoulBinderRecipeSchema.SCHEMA,
            MachineRecipes.TANK, TankRecipeSchema.SCHEMA
        );

        RecipeNamespace namespace = event.namespace(EnderIO.MODID);

        for (var schemaEntry : basicRecipeSchemas.entrySet()) {
            registerRecipeSchema(namespace, schemaEntry);
        }
        for (var schemaEntry : machineRecipeSchemas.entrySet()) {
            registerRecipeSchema(namespace, schemaEntry);
        }
    }

    private void registerRecipeSchema(
        RecipeNamespace namespace, Map.Entry<RecipeTypeSerializerPair<?, ?>, RecipeSchema> schemaEntry
    ) {
        String id = schemaEntry.getKey().type().getId().getPath();
        namespace.register(id, schemaEntry.getValue());
    }

    public static CountedIngredient wrapCountedIngredient(Object o) {
        if (o instanceof CountedIngredient countedIngredient) {
            return countedIngredient;
        }

        if (o instanceof JsonObject jsonObject) {
            return CountedIngredient.fromJson(jsonObject);
        }

        if (o instanceof Map<?, ?> map && map.containsKey("ingredient") && map.containsKey("count")) {
            JsonObject jsonObject = MapJS.json(map);
            if (jsonObject == null) {
                throw new IllegalArgumentException("Invalid counted ingredient: " + map);
            }
            return wrapCountedIngredient(jsonObject);
        }

        InputItem inputItem = InputItem.of(o);
        return CountedIngredient.of(inputItem.count, inputItem.ingredient);
    }

    public interface Events {
        EventGroup GROUP = EventGroup.of("EnderIOEvents");
        EventHandler CONDUIT_REGISTRY = GROUP.startup("conduits", () -> ConduitRegistryEvent.class);
    }
}

package com.almostreliable.kubeio;

import com.almostreliable.kubeio.binding.DataComponents;
import com.almostreliable.kubeio.component.EnchantmentComponent;
import com.almostreliable.kubeio.component.FireCraftingResultComponent;
import com.almostreliable.kubeio.component.SagMillOutputItemComponent;
import com.almostreliable.kubeio.component.SimpleComponents;
import com.almostreliable.kubeio.event.ConduitRegistryEvent;
import com.almostreliable.kubeio.event.GrindingBallEvent;
import com.almostreliable.kubeio.event.VatReagentModificationEvent;
import com.almostreliable.kubeio.recipe.AlloySmelterKubeRecipe;
import com.almostreliable.kubeio.recipe.FireCraftingKubeRecipe;
import com.almostreliable.kubeio.recipe.TankKubeRecipe;
import com.almostreliable.kubeio.schema.AlloySmelterRecipeSchema;
import com.almostreliable.kubeio.schema.EnchanterRecipeSchema;
import com.almostreliable.kubeio.schema.FireCraftingRecipeSchema;
import com.almostreliable.kubeio.schema.PaintingRecipeSchema;
import com.almostreliable.kubeio.schema.SagMillRecipeSchema;
import com.almostreliable.kubeio.schema.SlicerRecipeSchema;
import com.almostreliable.kubeio.schema.SoulBinderRecipeSchema;
import com.almostreliable.kubeio.schema.TankRecipeSchema;
import com.almostreliable.kubeio.schema.VatRecipeSchema;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.MobCategory;

import com.enderio.core.common.recipes.RecipeTypeSerializerPair;
import com.enderio.enderio.EnderIO;
import com.enderio.enderio.content.fire_crafting.FireCraftingRecipe;
import com.enderio.enderio.content.machines.sag_mill.SagMillingRecipe;
import com.enderio.enderio.content.storage.fluid_tank.TankRecipe;
import com.enderio.enderio.foundation.datamap.VatReagent;
import com.enderio.enderio.foundation.tag.EIOTags;
import com.enderio.enderio.init.EIORecipes;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import dev.latvian.mods.kubejs.event.EventGroup;
import dev.latvian.mods.kubejs.event.EventGroupRegistry;
import dev.latvian.mods.kubejs.event.EventHandler;
import dev.latvian.mods.kubejs.generator.KubeDataGenerator;
import dev.latvian.mods.kubejs.plugin.KubeJSPlugin;
import dev.latvian.mods.kubejs.recipe.component.RecipeComponentTypeRegistry;
import dev.latvian.mods.kubejs.recipe.schema.RecipeFactoryRegistry;
import dev.latvian.mods.kubejs.recipe.schema.RecipeNamespace;
import dev.latvian.mods.kubejs.recipe.schema.RecipeSchema;
import dev.latvian.mods.kubejs.recipe.schema.RecipeSchemaRegistry;
import dev.latvian.mods.kubejs.script.BindingRegistry;
import dev.latvian.mods.kubejs.script.ScriptType;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class KubePlugin implements KubeJSPlugin {

    public static final Set<ResourceLocation> SMELTING_RECIPES = new HashSet<>();

    @Override
    public void registerEvents(EventGroupRegistry registry) {
        registry.register(Events.GROUP);
    }

    @Override
    public void registerBindings(BindingRegistry registry) {
        if (registry.type().isServer()) {
            registry.add("FireCraftingResult", FireCraftingRecipe.Result.class);
            registry.add("MobCategory", MobCategory.class);
            registry.add("SagMillBonus", SagMillingRecipe.BonusType.class);
            registry.add("SagMillOutput", SagMillingRecipe.OutputItem.class);
            registry.add("TankMode", TankRecipe.Mode.class);
        }
        if (registry.type().isStartup()) {
            registry.add("EnderIOComponents", DataComponents.class);
        }
    }

    @Override
    public void registerRecipeComponents(RecipeComponentTypeRegistry registry) {
        registry.register(SimpleComponents.BOUS_TYPE);
        registry.register(SimpleComponents.MOB_CATEGORY);
        registry.register(SimpleComponents.TANK_MODE);
        registry.register(EnchantmentComponent.TYPE);
        registry.register(FireCraftingResultComponent.TYPE);
        registry.register(SagMillOutputItemComponent.TYPE);
    }

    @Override
    public void registerRecipeFactories(RecipeFactoryRegistry registry) {
        registry.register(AlloySmelterKubeRecipe.FACTORY);
        registry.register(FireCraftingKubeRecipe.FACTORY);
        registry.register(TankKubeRecipe.FACTORY);
    }

    @Override
    public void registerRecipeSchemas(RecipeSchemaRegistry registry) {
        Map<RecipeTypeSerializerPair<?, ?>, RecipeSchema> basicRecipeSchemas = Map.of(
            EIORecipes.FIRE_CRAFTING, FireCraftingRecipeSchema.SCHEMA
            // EIORecipes.GRINDING_BALL, GrindingBallRecipeSchema.SCHEMA TODO: migrate to data component system
        );

        Map<RecipeTypeSerializerPair<?, ?>, RecipeSchema> machineRecipeSchemas = Map.of(
            EIORecipes.ALLOY_SMELTING, AlloySmelterRecipeSchema.SCHEMA,
            EIORecipes.ENCHANTING, EnchanterRecipeSchema.SCHEMA,
            EIORecipes.PAINTING, PaintingRecipeSchema.SCHEMA,
            EIORecipes.SAG_MILLING, SagMillRecipeSchema.SCHEMA,
            EIORecipes.SLICING, SlicerRecipeSchema.SCHEMA,
            EIORecipes.SOUL_BINDING, SoulBinderRecipeSchema.SCHEMA,
            EIORecipes.TANK, TankRecipeSchema.SCHEMA,
            EIORecipes.VAT_FERMENTING, VatRecipeSchema.SCHEMA
        );

        RecipeNamespace namespace = registry.namespace(EnderIO.MOD_ID);

        for (var schemaEntry : basicRecipeSchemas.entrySet()) {
            registerRecipeSchema(namespace, schemaEntry);
        }
        for (var schemaEntry : machineRecipeSchemas.entrySet()) {
            registerRecipeSchema(namespace, schemaEntry);
        }
    }

    @Override
    public void generateData(KubeDataGenerator generator) {
        if (Events.CONDUIT_REGISTRY.hasListeners()) {
            Events.CONDUIT_REGISTRY.post(ScriptType.SERVER, new ConduitRegistryEvent());
            ConduitRegistryEvent.CUSTOM_CONDUITS.forEach(generator::json);
            ConduitRegistryEvent.clear();
        }

        if (Events.GRINDING_BALLS.hasListeners()) {
            JsonArray array = new JsonArray();
            for (var item : GrindingBallEvent.GRINDING_BALLS.keySet()) {
                ResourceLocation itemId = item.kjs$getIdLocation();
                array.add(itemId.toString());
            }
            JsonObject json = new JsonObject();
            json.add("values", array);

            ResourceLocation grindingBallTag = EIOTags.Items.GRINDING_BALLS.location();
            generator.json(EnderIO.rl("tags/item/" + grindingBallTag.getPath()), json);
            GrindingBallEvent.clear();
        }

        if (Events.VAT_REAGENTS.hasListeners()) {
            generator.dataMap(
                VatReagent.DATA_MAP,
                map -> Events.VAT_REAGENTS.post(ScriptType.SERVER, new VatReagentModificationEvent(map))
            );
        }
    }

    private void registerRecipeSchema(
        RecipeNamespace namespace, Map.Entry<RecipeTypeSerializerPair<?, ?>, RecipeSchema> schemaEntry
    ) {
        String id = schemaEntry.getKey().type().getId().getPath();
        namespace.register(id, schemaEntry.getValue());
    }

    public interface Events {

        EventGroup GROUP = EventGroup.of("EnderIOEvents");

        // startup
        EventHandler GRINDING_BALLS = GROUP.startup("grindingBalls", () -> GrindingBallEvent.class);

        // server
        EventHandler CONDUIT_REGISTRY = GROUP.server("conduits", () -> ConduitRegistryEvent.class);
        EventHandler VAT_REAGENTS = GROUP.server("vatReagents", () -> VatReagentModificationEvent.class);
    }
}

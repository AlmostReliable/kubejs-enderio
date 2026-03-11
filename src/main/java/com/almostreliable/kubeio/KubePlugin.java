package com.almostreliable.kubeio;

import com.almostreliable.kubeio.builder.CapacitorBuilder;
import com.almostreliable.kubeio.component.EnchantmentComponent;
import com.almostreliable.kubeio.component.FireCraftingResultComponent;
import com.almostreliable.kubeio.component.SagMillOutputItemComponent;
import com.almostreliable.kubeio.component.SimpleComponents;
import com.almostreliable.kubeio.event.ConduitRegistrationEvent;
import com.almostreliable.kubeio.event.GrindingBallModificationEvent;
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

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.MobCategory;

import com.enderio.core.common.recipes.RecipeTypeSerializerPair;
import com.enderio.enderio.EnderIO;
import com.enderio.enderio.api.capacitor.CapacitorModifier;
import com.enderio.enderio.api.components.GrindingBallData;
import com.enderio.enderio.content.fire_crafting.FireCraftingRecipe;
import com.enderio.enderio.content.machines.sag_mill.SagMillingRecipe;
import com.enderio.enderio.content.storage.fluid_tank.TankRecipe;
import com.enderio.enderio.foundation.datamap.VatReagent;
import com.enderio.enderio.init.EIORecipes;
import dev.latvian.mods.kubejs.event.EventGroup;
import dev.latvian.mods.kubejs.event.EventGroupRegistry;
import dev.latvian.mods.kubejs.event.EventHandler;
import dev.latvian.mods.kubejs.generator.KubeDataGenerator;
import dev.latvian.mods.kubejs.plugin.KubeJSPlugin;
import dev.latvian.mods.kubejs.recipe.component.RecipeComponentTypeRegistry;
import dev.latvian.mods.kubejs.recipe.schema.RecipeFactoryRegistry;
import dev.latvian.mods.kubejs.recipe.schema.RecipeSchema;
import dev.latvian.mods.kubejs.recipe.schema.RecipeSchemaRegistry;
import dev.latvian.mods.kubejs.registry.BuilderTypeRegistry;
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
    public void registerBuilderTypes(BuilderTypeRegistry registry) {
        registry.of(
            Registries.ITEM,
            reg -> reg.add(EnderIO.rl("capacitor"), CapacitorBuilder.class, CapacitorBuilder::new)
        );
    }

    @Override
    public void registerBindings(BindingRegistry registry) {
        if (registry.type().isStartup()) {
            registry.add("CapacitorModifier", CapacitorModifier.class);
        }
        if (registry.type().isServer()) {
            registry.add("FireCraftingResult", FireCraftingRecipe.Result.class);
            registry.add("MobCategory", MobCategory.class);
            registry.add("SagMillBonus", SagMillingRecipe.BonusType.class);
            registry.add("SagMillOutput", SagMillingRecipe.OutputItem.class);
            registry.add("TankMode", TankRecipe.Mode.class);
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
        Map<RecipeTypeSerializerPair<?, ?>, RecipeSchema> recipeSchemas = Map.of(
            EIORecipes.ALLOY_SMELTING, AlloySmelterRecipeSchema.SCHEMA,
            EIORecipes.ENCHANTING, EnchanterRecipeSchema.SCHEMA,
            EIORecipes.FIRE_CRAFTING, FireCraftingRecipeSchema.SCHEMA,
            EIORecipes.PAINTING, PaintingRecipeSchema.SCHEMA,
            EIORecipes.SAG_MILLING, SagMillRecipeSchema.SCHEMA,
            EIORecipes.SLICING, SlicerRecipeSchema.SCHEMA,
            EIORecipes.SOUL_BINDING, SoulBinderRecipeSchema.SCHEMA,
            EIORecipes.TANK, TankRecipeSchema.SCHEMA,
            EIORecipes.VAT_FERMENTING, VatRecipeSchema.SCHEMA
        );

        for (var entry : recipeSchemas.entrySet()) {
            var recipeType = entry.getKey();
            var schema = entry.getValue();
            var id = recipeType.type().getId();
            registry.register(id, schema);
        }
    }

    @Override
    public void generateData(KubeDataGenerator generator) {
        if (Events.CONDUIT_REGISTRY.hasListeners()) {
            Events.CONDUIT_REGISTRY.post(ScriptType.SERVER, new ConduitRegistrationEvent());
            ConduitRegistrationEvent.CUSTOM_CONDUITS.forEach(generator::json);
            ConduitRegistrationEvent.clear();
        }

        if (Events.GRINDING_BALLS.hasListeners()) {
            generator.dataMap(
                GrindingBallData.DATA_MAP_TYPE,
                map -> Events.GRINDING_BALLS.post(ScriptType.SERVER, new GrindingBallModificationEvent(map))
            );
        }

        if (Events.VAT_REAGENTS.hasListeners()) {
            generator.dataMap(
                VatReagent.DATA_MAP,
                map -> Events.VAT_REAGENTS.post(ScriptType.SERVER, new VatReagentModificationEvent(map))
            );
        }
    }

    public interface Events {

        EventGroup GROUP = EventGroup.of("EnderIOEvents");

        EventHandler CONDUIT_REGISTRY = GROUP.server("conduits", () -> ConduitRegistrationEvent.class);
        EventHandler GRINDING_BALLS = GROUP.server("grindingBalls", () -> GrindingBallModificationEvent.class);
        EventHandler VAT_REAGENTS = GROUP.server("vatReagents", () -> VatReagentModificationEvent.class);
    }
}

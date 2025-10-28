package com.almostreliable.kubeio;

import com.almostreliable.kubeio.binding.DataComponents;
import com.almostreliable.kubeio.component.FireCraftingResultComponent;
import com.almostreliable.kubeio.component.ResourceKeyComponent;
import com.almostreliable.kubeio.component.SagMillOutputItemComponent;
import com.almostreliable.kubeio.event.ConduitRegistryEvent;
import com.almostreliable.kubeio.recipe.AlloySmelterKubeRecipe;
import com.almostreliable.kubeio.recipe.FireCraftingKubeRecipe;
import com.almostreliable.kubeio.recipe.TankKubeRecipe;
import com.almostreliable.kubeio.schema.*;
import com.enderio.base.api.EnderIO;
import com.enderio.base.common.init.EIORecipes;
import com.enderio.base.common.recipe.FireCraftingRecipe;
import com.enderio.core.common.recipes.RecipeTypeSerializerPair;
import com.enderio.machines.common.blocks.alloy.AlloySmeltingRecipe;
import com.enderio.machines.common.blocks.fluid_tank.TankRecipe;
import com.enderio.machines.common.blocks.sag_mill.SagMillingRecipe;
import com.enderio.machines.common.init.MachineRecipes;
import dev.latvian.mods.kubejs.core.RecipeManagerKJS;
import dev.latvian.mods.kubejs.event.EventGroup;
import dev.latvian.mods.kubejs.event.EventGroupRegistry;
import dev.latvian.mods.kubejs.event.EventHandler;
import dev.latvian.mods.kubejs.generator.KubeDataGenerator;
import dev.latvian.mods.kubejs.plugin.KubeJSPlugin;
import dev.latvian.mods.kubejs.recipe.RecipesKubeEvent;
import dev.latvian.mods.kubejs.recipe.schema.*;
import dev.latvian.mods.kubejs.script.BindingRegistry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CookingBookCategory;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.SmeltingRecipe;

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
    public void registerRecipeComponents(RecipeComponentFactoryRegistry registry) {
        registry.register(FireCraftingResultComponent.INSTANCE);
        registry.register(ResourceKeyComponent.DIMENSION);
        registry.register(ResourceKeyComponent.LOOT_TABLE);
        registry.register(SagMillOutputItemComponent.INSTANCE);
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
            MachineRecipes.ALLOY_SMELTING, AlloySmelterRecipeSchema.SCHEMA,
            MachineRecipes.ENCHANTING, EnchanterRecipeSchema.SCHEMA,
            MachineRecipes.PAINTING, PaintingRecipeSchema.SCHEMA,
            MachineRecipes.SAG_MILLING, SagMillRecipeSchema.SCHEMA,
            MachineRecipes.SLICING, SlicerRecipeSchema.SCHEMA,
            MachineRecipes.SOUL_BINDING, SoulBinderRecipeSchema.SCHEMA,
            MachineRecipes.TANK, TankRecipeSchema.SCHEMA,
            MachineRecipes.VAT_FERMENTING, VatRecipeSchema.SCHEMA
        );

        RecipeNamespace namespace = registry.namespace(EnderIO.NAMESPACE);

        for (var schemaEntry : basicRecipeSchemas.entrySet()) {
            registerRecipeSchema(namespace, schemaEntry);
        }
        for (var schemaEntry : machineRecipeSchemas.entrySet()) {
            registerRecipeSchema(namespace, schemaEntry);
        }
    }

    @Override
    public void generateData(KubeDataGenerator generator) {
        Events.CONDUIT_REGISTRY.post(new ConduitRegistryEvent());
        ConduitRegistryEvent.CUSTOM_CONDUITS.forEach(generator::json);
        ConduitRegistryEvent.clear();
    }

    @SuppressWarnings("removal") // TODO: use dynamic recipes
    @Override
    public void injectRuntimeRecipes(
        RecipesKubeEvent event, RecipeManagerKJS manager, Map<ResourceLocation, RecipeHolder<?>> recipesByName
    ) {
        for (ResourceLocation recipeId : SMELTING_RECIPES) {
            var recipe = recipesByName.get(recipeId).value();
            if (!(recipe instanceof AlloySmeltingRecipe alloyRecipe)) {
                continue;
            }

            var inputs = alloyRecipe.inputs();
            if (inputs.size() != 1 || inputs.getFirst().count() != 1) continue;

            Ingredient input = inputs.getFirst().ingredient();
            ItemStack output = alloyRecipe.output();
            float experience = alloyRecipe.experience();
            ResourceLocation id = ResourceLocation.tryParse(recipeId.toString() + "_inherited");
            if (id == null) continue;

            var holder = new RecipeHolder<>(
                id,
                new SmeltingRecipe("", CookingBookCategory.MISC, input, output, experience, 200)
            );
            recipesByName.put(id, holder);
        }

        SMELTING_RECIPES.clear();
    }

    private void registerRecipeSchema(
        RecipeNamespace namespace, Map.Entry<RecipeTypeSerializerPair<?, ?>, RecipeSchema> schemaEntry
    ) {
        String id = schemaEntry.getKey().type().getId().getPath();
        namespace.register(id, schemaEntry.getValue());
    }

    public interface Events {
        EventGroup GROUP = EventGroup.of("EnderIOEvents");
        EventHandler CONDUIT_REGISTRY = GROUP.server("conduits", () -> ConduitRegistryEvent.class);
    }
}

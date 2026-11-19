package com.almostreliable.kubeio.component;

import com.enderio.enderio.EnderIO;
import com.enderio.enderio.content.machines.obelisks.weather.WeatherChangeRecipe;
import com.enderio.enderio.content.machines.sag_mill.SagMillingRecipe;
import com.enderio.enderio.content.storage.fluid_tank.TankRecipe;
import dev.latvian.mods.kubejs.recipe.component.EnumComponent;
import dev.latvian.mods.kubejs.recipe.component.RecipeComponentType;

import net.minecraft.world.entity.MobCategory;

public interface SimpleComponents {

    EnumComponent<SagMillingRecipe.BonusType> BONUS_TYPE = EnumComponent.create(
        RecipeComponentType.key(EnderIO.id("bonus_type")),
        SagMillingRecipe.BonusType.class,
        SagMillingRecipe.BonusType.CODEC
    );
    EnumComponent<MobCategory> MOB_CATEGORY = EnumComponent.create(
        RecipeComponentType.key(EnderIO.id("mob_category")),
        MobCategory.class,
        MobCategory.CODEC
    );
    EnumComponent<TankRecipe.Mode> TANK_MODE = EnumComponent.create(
        RecipeComponentType.key(EnderIO.id("tank_mode")),
        TankRecipe.Mode.class,
        TankRecipe.Mode.CODEC
    );
    EnumComponent<WeatherChangeRecipe.WeatherMode> WEATHER_MODE = EnumComponent.create(
        RecipeComponentType.key(EnderIO.id("weather_mode")),
        WeatherChangeRecipe.WeatherMode.class,
        WeatherChangeRecipe.WeatherMode.CODEC
    );
}

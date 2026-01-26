package com.almostreliable.kubeio.component;

import com.enderio.enderio.EnderIO;
import com.enderio.enderio.content.machines.sag_mill.SagMillingRecipe;
import com.enderio.enderio.content.storage.fluid_tank.TankRecipe;
import dev.latvian.mods.kubejs.recipe.component.EnumComponent;
import dev.latvian.mods.kubejs.recipe.component.RecipeComponentType;
import net.minecraft.world.entity.MobCategory;

public interface SimpleComponents {

    RecipeComponentType<SagMillingRecipe.BonusType> BOUS_TYPE = EnumComponent.of(
        EnderIO.rl("bonus_type"),
        SagMillingRecipe.BonusType.class,
        SagMillingRecipe.BonusType.CODEC
    );
    RecipeComponentType<MobCategory> MOB_CATEGORY = EnumComponent.of(
        EnderIO.rl("mob_category"),
        MobCategory.class,
        MobCategory.CODEC
    );
    RecipeComponentType<TankRecipe.Mode> TANK_MODE = EnumComponent.of(
        EnderIO.rl("tank_mode"),
        TankRecipe.Mode.class,
        TankRecipe.Mode.CODEC
    );
}

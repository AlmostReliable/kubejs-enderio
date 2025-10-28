package com.almostreliable.kubeio.component;

import com.enderio.base.api.EnderIO;
import com.enderio.machines.common.blocks.fluid_tank.TankRecipe;
import com.enderio.machines.common.blocks.sag_mill.SagMillingRecipe;
import dev.latvian.mods.kubejs.recipe.component.EnumComponent;
import dev.latvian.mods.kubejs.recipe.component.RecipeComponentType;
import net.minecraft.world.entity.MobCategory;

public interface SimpleComponents {

    RecipeComponentType<SagMillingRecipe.BonusType> BOUS_TYPE = EnumComponent.of(
        EnderIO.loc("bonus_type"),
        SagMillingRecipe.BonusType.class,
        SagMillingRecipe.BonusType.CODEC
    );
    RecipeComponentType<MobCategory> MOB_CATEGORY = EnumComponent.of(
        EnderIO.loc("mob_category"),
        MobCategory.class,
        MobCategory.CODEC
    );
    RecipeComponentType<TankRecipe.Mode> TANK_MODE = EnumComponent.of(
        EnderIO.loc("tank_mode"),
        TankRecipe.Mode.class,
        TankRecipe.Mode.CODEC
    );
}

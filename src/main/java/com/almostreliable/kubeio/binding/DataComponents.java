package com.almostreliable.kubeio.binding;

import com.enderio.enderio.api.EnderIODataComponents;
import com.enderio.enderio.api.components.GrindingBallData;
import net.minecraft.core.component.DataComponentType;

import java.util.function.Supplier;

public interface DataComponents {
    Supplier<DataComponentType<GrindingBallData>> GRINDING_BALL = () -> EnderIODataComponents.GRINDING_BALL;
}

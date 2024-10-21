package com.almostreliable.kubeio.binding;

import com.enderio.base.api.grindingball.GrindingBallData;
import com.enderio.base.common.init.EIODataComponents;
import net.minecraft.core.component.DataComponentType;

import java.util.function.Supplier;

public interface DataComponents {
    Supplier<DataComponentType<GrindingBallData>> GRINDING_BALL = EIODataComponents.GRINDING_BALL;
}

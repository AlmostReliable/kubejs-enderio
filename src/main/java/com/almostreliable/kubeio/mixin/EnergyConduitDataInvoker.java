package com.almostreliable.kubeio.mixin;

import com.enderio.conduits.common.conduit.type.energy.EnergyConduitData;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.IEnergyStorage;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(EnergyConduitData.class)
public interface EnergyConduitDataInvoker {

    @Invoker(remap = false)
    LazyOptional<IEnergyStorage> callGetSelfCap();
}

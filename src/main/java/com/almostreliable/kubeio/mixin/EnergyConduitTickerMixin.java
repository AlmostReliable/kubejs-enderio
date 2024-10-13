package com.almostreliable.kubeio.mixin;

import com.almostreliable.kubeio.enderio.CustomEnergyConduitTicker;
import com.enderio.api.conduit.ColoredRedstoneProvider;
import com.enderio.api.conduit.ConduitData;
import com.enderio.api.conduit.ConduitGraph;
import com.enderio.api.conduit.ConduitType;
import com.enderio.api.conduit.ticker.CapabilityAwareConduitTicker;
import com.enderio.api.conduit.ticker.IOAwareConduitTicker;
import com.enderio.api.misc.ColorControl;
import net.minecraft.server.level.ServerLevel;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.*;

@Mixin(CapabilityAwareConduitTicker.class)
public abstract class EnergyConduitTickerMixin<TData extends ConduitData<TData>, TCap> {

    @SuppressWarnings({"InstanceofThis", "ConstantValue"})
    @Inject(method = "tickColoredGraph", at = @At(value = "HEAD", remap = false), cancellable = true, remap = false)
    private void kubeio$tickColoredGraph(
        ServerLevel level, ConduitType<TData> type, List<IOAwareConduitTicker.Connection<TData>> inserts,
        List<IOAwareConduitTicker.Connection<TData>> extracts, ColorControl color, ConduitGraph<TData> graph,
        ColoredRedstoneProvider coloredRedstoneProvider, CallbackInfo ci
    ) {
        if (!((Object) this instanceof CustomEnergyConduitTicker ticker)) return;
        ticker.customTickEnergyGraph(level, inserts, extracts);
        ci.cancel();
    }
}

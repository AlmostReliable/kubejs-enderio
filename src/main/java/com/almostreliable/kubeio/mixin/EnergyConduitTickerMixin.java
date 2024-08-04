package com.almostreliable.kubeio.mixin;

import com.almostreliable.kubeio.enderio.conduit.CustomEnergyConduitTicker;
import com.enderio.conduits.common.conduit.type.energy.EnergyConduitTicker;
import net.minecraftforge.energy.IEnergyStorage;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(EnergyConduitTicker.class)
public class EnergyConduitTickerMixin {

    @SuppressWarnings("InstanceofThis")
    @Redirect(method = "extractEnergy", at = @At(value = "INVOKE", target = "Lnet/minecraftforge/energy/IEnergyStorage;extractEnergy(IZ)I", ordinal = 0), remap = false)
    private int kubeio$extractEnergy(IEnergyStorage instance, int amount, boolean simulate) {
        if ((Object) this instanceof CustomEnergyConduitTicker ticker) {
            return instance.extractEnergy(ticker.getTransferRate(), true);
        }

        return instance.extractEnergy(amount, simulate);
    }
}

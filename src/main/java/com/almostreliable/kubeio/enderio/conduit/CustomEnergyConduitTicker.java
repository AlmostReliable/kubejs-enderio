package com.almostreliable.kubeio.enderio.conduit;

import com.enderio.conduits.common.conduit.type.energy.EnergyConduitTicker;

public class CustomEnergyConduitTicker extends EnergyConduitTicker {

    private final int transferRate;

    CustomEnergyConduitTicker(int transferRate) {
        this.transferRate = transferRate;
    }

    public int getTransferRate() {
        return transferRate;
    }
}

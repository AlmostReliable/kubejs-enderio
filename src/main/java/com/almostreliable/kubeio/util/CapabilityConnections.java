package com.almostreliable.kubeio.util;

import net.minecraftforge.energy.IEnergyStorage;

public class CapabilityConnections
{
    public final IEnergyStorage capability;
    private int count = 0;

    public CapabilityConnections(IEnergyStorage capability) {
        this.capability = capability;
    }

    public int getCount() {
        return count;
    }

    public void increment() {
        count++;
    }
}

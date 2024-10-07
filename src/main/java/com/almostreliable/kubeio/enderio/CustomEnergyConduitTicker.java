package com.almostreliable.kubeio.enderio;

import com.enderio.api.conduit.ColoredRedstoneProvider;
import com.enderio.api.conduit.ConduitGraph;
import com.enderio.api.conduit.ConduitType;
import com.enderio.api.conduit.ticker.CapabilityAwareConduitTicker;
import com.enderio.conduits.common.conduit.type.energy.EnergyConduitData;
import com.enderio.conduits.common.conduit.type.energy.EnergyConduitTicker;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Tuple;
import net.minecraftforge.energy.IEnergyStorage;

import java.util.Comparator;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class CustomEnergyConduitTicker extends EnergyConduitTicker {

    private final int transferRate;

    CustomEnergyConduitTicker(int transferRate) {
        this.transferRate = transferRate;
    }

    @Override
    public void tickCapabilityGraph(
        ServerLevel level, ConduitType<EnergyConduitData> type,
        List<CapabilityAwareConduitTicker<EnergyConduitData, IEnergyStorage>.CapabilityConnection> inserts,
        List<CapabilityAwareConduitTicker<EnergyConduitData, IEnergyStorage>.CapabilityConnection> extracts,
        ConduitGraph<EnergyConduitData> graph, ColoredRedstoneProvider coloredRedstoneProvider
    ) {
        AtomicReference<Integer> maxInsertAmount = new AtomicReference<>(0);
        List<Tuple<CapabilityAwareConduitTicker<EnergyConduitData, IEnergyStorage>.CapabilityConnection, Integer>> insertsWithAmounts = inserts.stream()
            .map(capConn -> {
                int insertAmount = capConn.capability.receiveEnergy(transferRate, true);
                maxInsertAmount.updateAndGet(v -> v + insertAmount);
                return new Tuple<>(capConn, insertAmount);
            })
            .filter(t -> t.getB() > 0)
            .sorted(Comparator.comparingInt(Tuple::getB))
            .toList();

        AtomicReference<Integer> maxExtractAmount = new AtomicReference<>(0);
        List<Tuple<CapabilityAwareConduitTicker<EnergyConduitData, IEnergyStorage>.CapabilityConnection, Integer>> extractsWithAmounts = extracts.stream()
            .map(capConn -> {
                int extractAmount = capConn.capability.extractEnergy(transferRate, true);
                maxExtractAmount.updateAndGet(v -> v + extractAmount);
                return new Tuple<>(capConn, extractAmount);
            })
            .filter(t -> t.getB() > 0)
            .sorted(Comparator.comparingInt(Tuple::getB))
            .toList();

        if(maxInsertAmount.get() < maxExtractAmount.get()) {
            insertsWithAmounts.forEach(t -> t.getA().capability.receiveEnergy(transferRate, false));
            int leftToExtract = maxInsertAmount.get();
            for(int i = 0; i < extractsWithAmounts.size(); i++) {
                int toExtract = (int) Math.ceil(leftToExtract / (double)(extractsWithAmounts.size() - i));
                leftToExtract -= extractsWithAmounts.get(i).getA().capability.extractEnergy(toExtract, false);
                if(leftToExtract <= 0) break;
            }
        } else if (maxInsertAmount.get() > maxExtractAmount.get()) {
            extractsWithAmounts.forEach(t -> t.getA().capability.extractEnergy(transferRate, false));
            int leftToInsert = maxExtractAmount.get();
            for(int i = 0; i < insertsWithAmounts.size(); i++) {
                int toInsert = (int) Math.ceil(leftToInsert / (double)(insertsWithAmounts.size() - i));
                leftToInsert -= insertsWithAmounts.get(i).getA().capability.receiveEnergy(toInsert, false);
                if(leftToInsert <= 0) break;
            }
        } else {
            insertsWithAmounts.forEach(t -> t.getA().capability.receiveEnergy(transferRate, false));
            extractsWithAmounts.forEach(t -> t.getA().capability.extractEnergy(transferRate, false));
        }
    }
}

package com.almostreliable.kubeio.enderio;

import com.enderio.api.conduit.ConduitData;
import com.enderio.api.conduit.ticker.IOAwareConduitTicker;
import com.enderio.conduits.common.conduit.type.energy.EnergyConduitTicker;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Tuple;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.energy.IEnergyStorage;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.BiFunction;

public class CustomEnergyConduitTicker extends EnergyConduitTicker {

    private final int transferRate;

    CustomEnergyConduitTicker(int transferRate) {
        this.transferRate = transferRate;
    }

    public <TData extends ConduitData<TData>> void customTickEnergyGraph(
        ServerLevel level,
        List<Connection<TData>> inserts,
        List<IOAwareConduitTicker.Connection<TData>> extracts
    ) {
        Map<BlockEntity, BlockCapabilityConnections> capabilitiesByBlocks = getCapabilitiesByBlocks(
            level,
            inserts,
            extracts
        );
        ExtractInsertData extractInsertData = transformCapabilitiesByBlocks(capabilitiesByBlocks);

        // If this is true, no energy transfer is necessary
        if ((extractInsertData.inserts.isEmpty() && extractInsertData.extracts.isEmpty()) ||
            (extractInsertData.inserts.isEmpty() && extractInsertData.insertAndExtracts.isEmpty()) ||
            (extractInsertData.extracts.isEmpty() && extractInsertData.insertAndExtracts.isEmpty())) {
            return;
        }

        OrderedCapabilitiesWithMaxTransfer sortedInserts = sortTransfers(
            extractInsertData.inserts,
            (energyStorage, amount) -> energyStorage.receiveEnergy(amount, true)
        );
        OrderedCapabilitiesWithMaxTransfer sortedExtracts = sortTransfers(
            extractInsertData.extracts,
            (energyStorage, amount) -> energyStorage.extractEnergy(amount, true)
        );

        if (sortedInserts.maxTransferAmount < sortedExtracts.maxTransferAmount) {
            handleExcessPower(sortedInserts, sortedExtracts, extractInsertData);
        } else if (sortedInserts.maxTransferAmount > sortedExtracts.maxTransferAmount) {
            handlePowerDeficit(sortedInserts, sortedExtracts, extractInsertData);
        } else {
            for(var insert : sortedInserts.caps) insert.receiveEnergy(transferRate, false);
            for(var extract : sortedExtracts.caps) extract.extractEnergy(transferRate, false);
        }
    }

    private void handleExcessPower(
        OrderedCapabilitiesWithMaxTransfer sortedInserts,
        OrderedCapabilitiesWithMaxTransfer sortedExtracts,
        ExtractInsertData extractInsertData
    ) {
        int excessPower = sortedExtracts.maxTransferAmount - sortedInserts.maxTransferAmount;
        OrderedCapabilitiesWithMaxTransfer sortedExtraInserts = sortTransfers(
            extractInsertData.insertAndExtracts.stream().map(Tuple::getA).toList(),
            (energyStorage, amount) -> energyStorage.receiveEnergy(amount, true)
        );
        for(var insert : sortedInserts.caps) insert.receiveEnergy(transferRate, false);
        if (excessPower > sortedExtraInserts.maxTransferAmount) {
            for(var insert : sortedExtraInserts.caps) insert.receiveEnergy(transferRate, false);

            int leftToExtract = sortedInserts.maxTransferAmount + sortedExtraInserts.maxTransferAmount;
            extractBalanced(sortedExtracts, leftToExtract);
        } else {
            for(var extract : sortedExtracts.caps) extract.extractEnergy(transferRate, false);

            insertBalanced(sortedExtraInserts, excessPower);
        }
    }

    private void handlePowerDeficit(
        OrderedCapabilitiesWithMaxTransfer sortedInserts, OrderedCapabilitiesWithMaxTransfer sortedExtracts,
        ExtractInsertData extractInsertData
    ) {
        int powerDeficit = sortedInserts.maxTransferAmount - sortedExtracts.maxTransferAmount;
        OrderedCapabilitiesWithMaxTransfer sortedExtraExtracts = sortTransfers(
            extractInsertData.insertAndExtracts.stream().map(Tuple::getB).toList(),
            (energyStorage, amount) -> energyStorage.extractEnergy(amount, true)
        );
        for(var extract : sortedExtracts.caps) extract.extractEnergy(transferRate, false);
        if (powerDeficit > sortedExtraExtracts.maxTransferAmount) {
            for(var extract : sortedExtraExtracts.caps) extract.extractEnergy(transferRate, false);

            int leftToInsert = sortedExtracts.maxTransferAmount + sortedExtraExtracts.maxTransferAmount;
            insertBalanced(sortedInserts, leftToInsert);
        } else {
            for(var insert : sortedInserts.caps) insert.receiveEnergy(transferRate, false);

            extractBalanced(sortedExtraExtracts, powerDeficit);
        }
    }

    private void insertBalanced(OrderedCapabilitiesWithMaxTransfer sortedExtraInserts, int leftToInsert) {
        for (int i = 0; i < sortedExtraInserts.caps.size(); i++) {
            int toInsert = (int) Math.ceil(leftToInsert / (double) (sortedExtraInserts.caps.size() - i));
            leftToInsert -= sortedExtraInserts.caps.get(i).receiveEnergy(toInsert, false);
            if (leftToInsert <= 0) break;
        }
    }

    private void extractBalanced(OrderedCapabilitiesWithMaxTransfer sortedExtracts, int leftToExtract) {
        for (int i = 0; i < sortedExtracts.caps.size(); i++) {
            int toExtract = (int) Math.ceil(leftToExtract / (double) (sortedExtracts.caps.size() - i));
            leftToExtract -= sortedExtracts.caps.get(i).extractEnergy(toExtract, false);
            if (leftToExtract <= 0) break;
        }
    }

    private <TData extends ConduitData<TData>> Map<BlockEntity, BlockCapabilityConnections> getCapabilitiesByBlocks(
        ServerLevel level,
        List<Connection<TData>> inserts,
        List<IOAwareConduitTicker.Connection<TData>> extracts
    ) {
        Map<BlockEntity, BlockCapabilityConnections> capabilitiesByBlocks = new IdentityHashMap<>(
            inserts.size() + extracts.size());

        for (IOAwareConduitTicker.Connection<TData> insert : inserts) {
            BlockEntity be = level.getBlockEntity(insert.move());
            if (be == null) continue;
            IEnergyStorage capability = be.getCapability(ForgeCapabilities.ENERGY, insert.dir().getOpposite())
                .resolve()
                .orElse(null);
            if (capability == null) continue;
            BlockCapabilityConnections blockCapabilityConnections = capabilitiesByBlocks.computeIfAbsent(
                be,
                k -> new BlockCapabilityConnections(new CapabilityConnections(capability), null)
            );
            Objects.requireNonNull(blockCapabilityConnections.getInsert()).increment();
        }

        if (!capabilitiesByBlocks.isEmpty()) {
            for (IOAwareConduitTicker.Connection<TData> extract : extracts) {
                BlockEntity be = level.getBlockEntity(extract.move());
                if (be == null) continue;
                IEnergyStorage capability = be.getCapability(ForgeCapabilities.ENERGY, extract.dir().getOpposite())
                    .resolve()
                    .orElse(null);
                if (capability == null) continue;
                BlockCapabilityConnections blockCapabilityConnections = capabilitiesByBlocks.computeIfAbsent(
                    be,
                    k -> new BlockCapabilityConnections(null, new CapabilityConnections(capability))
                );
                if (blockCapabilityConnections.getExtract() == null) blockCapabilityConnections.setExtract(new CapabilityConnections(capability));
                blockCapabilityConnections.getExtract().increment();
            }
        }

        return capabilitiesByBlocks;
    }

    private ExtractInsertData transformCapabilitiesByBlocks(
        Map<BlockEntity, BlockCapabilityConnections> capabilitiesByBlock
    ) {
        List<CapabilityConnections> inserts = new ArrayList<>();
        List<CapabilityConnections> extracts = new ArrayList<>();
        List<Tuple<CapabilityConnections, CapabilityConnections>> insertAndExtracts = new ArrayList<>();

        // I only use one capability for all energy transfer as I need to check if all the energy fits in the machine from all connected sides
        for (var blockCapabilities : capabilitiesByBlock.values()) {
            if (blockCapabilities.getInsert() == null) {
                extracts.add(blockCapabilities.getExtract());
            } else if (blockCapabilities.getExtract() == null) {
                inserts.add(blockCapabilities.getInsert());
            } else {
                insertAndExtracts.add(new Tuple<>(blockCapabilities.getInsert(), blockCapabilities.getExtract()));
            }
        }

        return new ExtractInsertData(inserts, extracts, insertAndExtracts);
    }

    private OrderedCapabilitiesWithMaxTransfer sortTransfers(
        List<CapabilityConnections> capabilityConnectionsList, BiFunction<IEnergyStorage, Integer, Integer> getTransfer
    ) {
        int maxTransferAmount = 0;
        List<Tuple<IEnergyStorage, Integer>> capabilitiesWithInsertAmounts = new ArrayList<>();
        for (var capabilityConnections : capabilityConnectionsList) {
            int transferAmount = getTransfer.apply(
                capabilityConnections.capability,
                transferRate * capabilityConnections.getCount()
            );
            if (transferAmount == 0) continue;
            maxTransferAmount += transferAmount;
            capabilitiesWithInsertAmounts.addAll(split(
                capabilityConnections.capability,
                capabilityConnections.getCount(),
                transferAmount
            ));
        }
        capabilitiesWithInsertAmounts.sort(Comparator.comparingInt(Tuple::getB));

        List<IEnergyStorage> sortedInserts = new ArrayList<>();
        for (var capabilityWithInsertAmount : capabilitiesWithInsertAmounts) {
            sortedInserts.add(capabilityWithInsertAmount.getA());
        }

        return new OrderedCapabilitiesWithMaxTransfer(sortedInserts, maxTransferAmount);
    }

    private List<Tuple<IEnergyStorage, Integer>> split(IEnergyStorage storage, int partCount, int amountToSplit) {
        int part = (int) Math.ceil((double) amountToSplit / partCount);
        List<Tuple<IEnergyStorage, Integer>> result = new ArrayList<>(partCount);
        for (int i = 0; i < amountToSplit / part; i++) {
            result.add(new Tuple<>(storage, part));
        }
        if (amountToSplit % part != 0) result.add(new Tuple<>(storage, amountToSplit % part));
        return result;
    }

    private record OrderedCapabilitiesWithMaxTransfer(List<IEnergyStorage> caps, int maxTransferAmount) {}

    private record ExtractInsertData(List<CapabilityConnections> inserts,
                                     List<CapabilityConnections> extracts,
                                     List<Tuple<CapabilityConnections, CapabilityConnections>> insertAndExtracts) {}

    private static class BlockCapabilityConnections {
        private @Nullable CapabilityConnections insertCapabilityConnections;
        private @Nullable CapabilityConnections extractCapabilityConnections;

        public BlockCapabilityConnections(@Nullable CapabilityConnections insertCapabilityConnections, @Nullable CapabilityConnections extractCapabilityConnections) {
            this.insertCapabilityConnections = insertCapabilityConnections;
            this.extractCapabilityConnections = extractCapabilityConnections;
        }

        public @Nullable CapabilityConnections getInsert() {
            return insertCapabilityConnections;
        }

        public @Nullable CapabilityConnections getExtract() {
            return extractCapabilityConnections;
        }

        public void setInsert(@Nullable CapabilityConnections insertCapabilityConnections) {
            this.insertCapabilityConnections = insertCapabilityConnections;
        }

        public void setExtract(@Nullable CapabilityConnections extractCapabilityConnections) {
            this.extractCapabilityConnections = extractCapabilityConnections;
        }
    }

    private static class CapabilityConnections {
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
}

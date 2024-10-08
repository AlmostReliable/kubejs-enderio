package com.almostreliable.kubeio.mixin;

import com.almostreliable.kubeio.enderio.CustomEnergyConduitTicker;
import com.almostreliable.kubeio.util.CapabilityConnections;
import com.enderio.api.conduit.ColoredRedstoneProvider;
import com.enderio.api.conduit.ConduitData;
import com.enderio.api.conduit.ConduitGraph;
import com.enderio.api.conduit.ConduitType;
import com.enderio.api.conduit.ticker.CapabilityAwareConduitTicker;
import com.enderio.api.conduit.ticker.IOAwareConduitTicker;
import com.enderio.api.misc.ColorControl;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Tuple;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.energy.IEnergyStorage;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Stream;

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
        Map<BlockEntity, Tuple<CapabilityConnections, CapabilityConnections>> capabilitiesByBlock = new IdentityHashMap<>(
            inserts.size() + extracts.size());

        for (IOAwareConduitTicker.Connection<TData> insert : inserts) {
            BlockEntity be = level.getBlockEntity(insert.move());
            if (be == null) continue;
            IEnergyStorage capability = be.getCapability(ForgeCapabilities.ENERGY, insert.dir().getOpposite())
                .resolve()
                .orElse(null);
            if (capability == null || !capability.canReceive()) continue;
            Tuple<CapabilityConnections, CapabilityConnections> capabilityTuple = capabilitiesByBlock.computeIfAbsent(
                be,
                k -> new Tuple<>(new CapabilityConnections(capability), null)
            );
            capabilityTuple.getA().increment();
        }

        if (!capabilitiesByBlock.isEmpty()) {
            for (IOAwareConduitTicker.Connection<TData> extract : extracts) {
                BlockEntity be = level.getBlockEntity(extract.move());
                if (be == null) continue;
                IEnergyStorage capability = be.getCapability(ForgeCapabilities.ENERGY, extract.dir().getOpposite())
                    .resolve()
                    .orElse(null);
                if (capability == null || !capability.canExtract()) continue;
                Tuple<CapabilityConnections, CapabilityConnections> capabilityTuple = capabilitiesByBlock.computeIfAbsent(
                    be,
                    k -> new Tuple<>(null, new CapabilityConnections(capability))
                );
                if (capabilityTuple.getB() == null) capabilityTuple.setB(new CapabilityConnections(capability));
                capabilityTuple.getB().increment();
            }
        }

        List<CapabilityConnections> energyInserts = new ArrayList<>();
        List<CapabilityConnections> energyExtracts = new ArrayList<>();
        List<Tuple<CapabilityConnections, CapabilityConnections>> energyInsertAndExtracts = new ArrayList<>();

        // I only use one capability for all energy transfer as I need to check if all the energy fits in the machine from all connected sides
        for (var tuple : capabilitiesByBlock.values()) {
            if (tuple.getA() == null) {
                energyExtracts.add(tuple.getB());
            } else if (tuple.getB() == null) {
                energyInserts.add(tuple.getA());
            } else {
                energyInsertAndExtracts.add(tuple);
            }
        }

        if ((!energyInserts.isEmpty() && !energyExtracts.isEmpty()) || (!energyInserts.isEmpty() && !energyInsertAndExtracts.isEmpty()) || (!energyExtracts.isEmpty() && !energyInsertAndExtracts.isEmpty())) {
            tickEnergyGraph(ticker, energyInserts, energyExtracts, energyInsertAndExtracts);
        }

        ci.cancel();
    }

    private void tickEnergyGraph(
        CustomEnergyConduitTicker ticker, List<CapabilityConnections> inserts, List<CapabilityConnections> extracts,
        List<Tuple<CapabilityConnections, CapabilityConnections>> insertAndExtracts
    ) {
        int transferRate = ticker.getTransferRate();

        AtomicReference<Integer> maxInsertAmount = new AtomicReference<>(0);
        List<IEnergyStorage> sortedInserts = inserts.stream()
            .flatMap(capabilityConnections -> {
                int insertAmount = capabilityConnections.capability.receiveEnergy(
                    transferRate * capabilityConnections.getCount(),
                    true
                );
                maxInsertAmount.updateAndGet(v -> v + insertAmount);
                return split(capabilityConnections.capability, capabilityConnections.getCount(), insertAmount).stream();
            })
            .filter(t -> t.getB() > 0)
            .sorted(Comparator.comparingInt(Tuple::getB))
            .map(Tuple::getA)
            .toList();

        AtomicReference<Integer> maxExtractAmount = new AtomicReference<>(0);
        List<IEnergyStorage> sortedExtracts = extracts.stream()
            .flatMap(capabilityConnections -> {
                int extractAmount = capabilityConnections.capability.extractEnergy(
                    transferRate * capabilityConnections.getCount(),
                    true
                );
                maxExtractAmount.updateAndGet(v -> v + extractAmount);
                return split(
                    capabilityConnections.capability,
                    capabilityConnections.getCount(),
                    extractAmount
                ).stream();
            })
            .filter(t -> t.getB() > 0)
            .sorted(Comparator.comparingInt(Tuple::getB))
            .map(Tuple::getA)
            .toList();

        if (maxInsertAmount.get() < maxExtractAmount.get()) {
            int excessPower = maxExtractAmount.get() - maxInsertAmount.get();

            AtomicReference<Integer> maxExtraInsertAmount = new AtomicReference<>(0);
            List<IEnergyStorage> extraSortedInserts = insertAndExtracts.stream()
                .map(Tuple::getA)
                .flatMap(capabilityConnections -> {
                    int insertAmount = capabilityConnections.capability.receiveEnergy(
                        transferRate * capabilityConnections.getCount(),
                        true
                    );
                    if (insertAmount == 0) return Stream.empty();
                    maxExtraInsertAmount.updateAndGet(v -> v + insertAmount);
                    return split(
                        capabilityConnections.capability,
                        capabilityConnections.getCount(),
                        insertAmount
                    ).stream();
                })
                .filter(t -> t.getB() > 0)
                .sorted(Comparator.comparingInt(Tuple::getB))
                .map(Tuple::getA)
                .toList();

            sortedInserts.forEach(cap -> cap.receiveEnergy(transferRate, false));

            if (excessPower > maxExtraInsertAmount.get()) {
                extraSortedInserts.forEach(cap -> cap.receiveEnergy(transferRate, false));

                int leftToExtract = maxInsertAmount.get() + maxExtraInsertAmount.get();
                for (int i = 0; i < sortedExtracts.size(); i++) {
                    int toExtract = (int) Math.ceil(leftToExtract / (double) (sortedExtracts.size() - i));
                    leftToExtract -= sortedExtracts.get(i).extractEnergy(toExtract, false);
                    if (leftToExtract <= 0) break;
                }
            } else {
                sortedExtracts.forEach(cap -> cap.extractEnergy(transferRate, false));

                int leftToInsert = excessPower;
                for (int i = 0; i < extraSortedInserts.size(); i++) {
                    int toInsert = (int) Math.ceil(leftToInsert / (double) (extraSortedInserts.size() - i));
                    leftToInsert -= extraSortedInserts.get(i).receiveEnergy(toInsert, false);
                    if (leftToInsert <= 0) break;
                }
            }
        } else if (maxInsertAmount.get() > maxExtractAmount.get()) {
            int powerDeficit = maxInsertAmount.get() - maxExtractAmount.get();

            AtomicReference<Integer> maxExtraExtractAmount = new AtomicReference<>(0);
            List<IEnergyStorage> extraSortedExtracts = insertAndExtracts.stream()
                .map(Tuple::getB)
                .flatMap(capabilityConnections -> {
                    int extractAmount = capabilityConnections.capability.extractEnergy(
                        transferRate * capabilityConnections.getCount(),
                        true
                    );
                    if (extractAmount == 0) return Stream.empty();
                    maxExtraExtractAmount.updateAndGet(v -> v + extractAmount);
                    return split(
                        capabilityConnections.capability,
                        capabilityConnections.getCount(),
                        extractAmount
                    ).stream();
                })
                .filter(t -> t.getB() > 0)
                .sorted(Comparator.comparingInt(Tuple::getB))
                .map(Tuple::getA)
                .toList();

            sortedExtracts.forEach(cap -> cap.extractEnergy(transferRate, false));

            if (powerDeficit > maxExtraExtractAmount.get()) {
                extraSortedExtracts.forEach(cap -> cap.extractEnergy(transferRate, false));

                int leftToInsert = maxExtractAmount.get() + maxExtraExtractAmount.get();
                for (int i = 0; i < sortedInserts.size(); i++) {
                    int toInsert = (int) Math.ceil(leftToInsert / (double) (sortedInserts.size() - i));
                    leftToInsert -= sortedInserts.get(i).receiveEnergy(toInsert, false);
                    if (leftToInsert <= 0) break;
                }
            } else {
                sortedInserts.forEach(cap -> cap.receiveEnergy(transferRate, false));

                int leftToExtract = powerDeficit;
                for (int i = 0; i < extraSortedExtracts.size(); i++) {
                    int toExtract = (int) Math.ceil(leftToExtract / (double) (extraSortedExtracts.size() - i));
                    leftToExtract -= extraSortedExtracts.get(i).extractEnergy(toExtract, false);
                    if (leftToExtract <= 0) break;
                }
            }
        } else {
            sortedInserts.forEach(cap -> cap.receiveEnergy(transferRate, false));
            sortedExtracts.forEach(cap -> cap.extractEnergy(transferRate, false));
        }
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
}

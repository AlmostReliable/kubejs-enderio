package com.almostreliable.kubeio.event;

import com.enderio.EnderIOBase;
import com.enderio.conduits.api.Conduit;
import com.enderio.conduits.common.conduit.type.energy.EnergyConduit;
import com.enderio.conduits.common.conduit.type.fluid.FluidConduit;
import com.enderio.modconduits.mods.appeng.MEConduit;
import com.enderio.modconduits.mods.mekanism.ChemicalConduit;
import com.google.common.base.Preconditions;
import com.google.gson.JsonElement;
import com.mojang.serialization.JsonOps;
import dev.latvian.mods.kubejs.event.KubeEvent;
import dev.latvian.mods.rhino.util.HideFromJS;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.fml.ModList;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.BiFunction;

public class ConduitRegistryEvent implements KubeEvent {

    @HideFromJS
    public static final Map<ResourceLocation, JsonElement> CUSTOM_CONDUITS = new HashMap<>();

    public void registerEnergyConduit(String id, Component name, int transferRate) {
        CustomConduit.of(id, name).bindInstance((n, tex) -> new EnergyConduit(tex, n, transferRate));
    }

    public void registerFluidConduit(String id, Component name, int transferRate, boolean multiFluid) {
        CustomConduit.of(id, name).bindInstance((n, tex) -> new FluidConduit(tex, n, transferRate, multiFluid));
    }

    public void registerChemicalConduit(String id, Component name, int transferRate, boolean multiChemical) {
        Preconditions.checkArgument(
            ModList.get().isLoaded("mekanism"),
            "mekanism must be loaded to use chemical conduits"
        );
        CustomConduit.of(id, name).bindInstance((n, tex) -> new ChemicalConduit(tex, n, transferRate, multiChemical));
    }

    public void registerMeConduit(String id, Component name, boolean dense) {
        Preconditions.checkArgument(
            ModList.get().isLoaded("ae2"),
            "applied energistics 2 must be loaded to use me conduits"
        );
        CustomConduit.of(id, name).bindInstance((n, tex) -> new MEConduit(tex, n, dense));
    }

    @SuppressWarnings("StaticMethodOnlyUsedInOneClass")
    @HideFromJS
    public static void clear() {
        CUSTOM_CONDUITS.clear();
        CustomConduit.CONDUIT_IDS.clear();
    }

    private record CustomConduit(String id, Component name) {

        private static final Set<String> CONDUIT_IDS = new HashSet<>();

        private static CustomConduit of(String id, Component name) {
            Preconditions.checkArgument(!id.contains(":"), "id must not contain a colon (:)");
            Preconditions.checkArgument(!id.contains(" "), "id must not contain a space");
            Preconditions.checkArgument(!CONDUIT_IDS.contains(id), "id must be unique");
            return new CustomConduit(id, name);
        }

        private void bindInstance(BiFunction<Component, ResourceLocation, Conduit<?>> factory) {
            var conduit = factory.apply(name, getTexturePath());
            JsonElement conduitJson = Conduit.DIRECT_CODEC.encodeStart(JsonOps.INSTANCE, conduit).getOrThrow();
            CUSTOM_CONDUITS.put(EnderIOBase.loc("enderio/conduit/" + id), conduitJson);
            CONDUIT_IDS.add(id);
        }

        private ResourceLocation getTexturePath() {
            return EnderIOBase.loc("block/conduit/" + id);
        }
    }
}

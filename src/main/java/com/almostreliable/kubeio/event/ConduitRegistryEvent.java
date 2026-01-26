package com.almostreliable.kubeio.event;

import appeng.api.util.AEColor;
import com.enderio.enderio.EnderIO;
import com.enderio.enderio.api.conduits.Conduit;
import com.enderio.enderio.content.conduits.type.energy.EnergyConduit;
import com.enderio.enderio.content.conduits.type.fluid.FluidConduit;
import com.enderio.modded_conduits.common.modules.appeng.MEConduit;
import com.enderio.modded_conduits.common.modules.mekanism.chemical.ChemicalConduit;
import com.google.common.base.Preconditions;
import com.google.gson.JsonElement;
import com.mojang.serialization.JsonOps;
import dev.latvian.mods.kubejs.event.KubeEvent;
import dev.latvian.mods.rhino.util.HideFromJS;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.fml.ModList;

import java.util.*;
import java.util.function.BiFunction;

public class ConduitRegistryEvent implements KubeEvent {

    @HideFromJS
    public static final Map<ResourceLocation, JsonElement> CUSTOM_CONDUITS = new HashMap<>();

    public void registerEnergyConduit(String id, Component name, int transferRate) {
        CustomConduit.of(id, name).bindInstance((n, tex) -> new EnergyConduit(tex, n, transferRate));
    }

    public void registerFluidConduit(
        String id, Component name, int transferRate, boolean multiFluid, boolean supportPriority
    ) {
        CustomConduit.of(id, name)
            .bindInstance((n, tex) -> new FluidConduit(tex, n, transferRate, multiFluid, supportPriority));
    }

    public void registerChemicalConduit(String id, Component name, int transferRate, boolean multiChemical) {
        Preconditions.checkArgument(
            ModList.get().isLoaded("mekanism"),
            "mekanism must be loaded to use chemical conduits"
        );
        CustomConduit.of(id, name).bindInstance((n, tex) -> new ChemicalConduit(tex, n, transferRate, multiChemical));
    }

    public void registerMeConduit(String id, Component name, String color, boolean dense) {
        Preconditions.checkArgument(
            Ae2Integration.isLoaded(),
            "applied energistics 2 must be loaded to use me conduits"
        );

        var conduitFactory = Ae2Integration.createFactory(color, dense);
        CustomConduit.of(id, name).bindInstance(conduitFactory);
    }

    @SuppressWarnings("StaticMethodOnlyUsedInOneClass")
    @HideFromJS
    public static void clear() {
        CUSTOM_CONDUITS.clear();
        CustomConduit.CONDUIT_IDS.clear();
    }

    @SuppressWarnings("UnstableApiUsage")
    private record CustomConduit(String id, Component name) {

        private static final Set<String> CONDUIT_IDS = new HashSet<>();

        private static CustomConduit of(String id, Component name) {
            Preconditions.checkArgument(!id.contains(":"), "id must not contain a colon (:)");
            Preconditions.checkArgument(!id.contains(" "), "id must not contain a space");
            Preconditions.checkArgument(!CONDUIT_IDS.contains(id), "id must be unique");
            return new CustomConduit(id, name);
        }

        private void bindInstance(BiFunction<Component, ResourceLocation, Conduit<?, ?>> factory) {
            var conduit = factory.apply(name, getTexturePath());
            JsonElement conduitJson = Conduit.DIRECT_CODEC.encodeStart(JsonOps.INSTANCE, conduit).getOrThrow();
            CUSTOM_CONDUITS.put(EnderIO.rl("enderio/conduit/" + id), conduitJson);
            CONDUIT_IDS.add(id);
        }

        private ResourceLocation getTexturePath() {
            return EnderIO.rl("block/conduit/" + id);
        }
    }

    @SuppressWarnings("UnstableApiUsage")
    private static final class Ae2Integration {

        private static BiFunction<Component, ResourceLocation, Conduit<?, ?>> createFactory(
            String color, boolean dense
        ) {
            AEColor aeColor = AEColor.valueOf(color.toUpperCase(Locale.ROOT));
            return (n, t) -> new MEConduit(t, n, aeColor, dense);
        }

        private static boolean isLoaded() {
            return ModList.get().isLoaded("ae2");
        }
    }
}

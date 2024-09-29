package com.almostreliable.kubeio.kube.event;

import com.almostreliable.kubeio.enderio.CustomConduitEntry;
import com.almostreliable.kubeio.enderio.CustomEnergyConduitType;
import com.enderio.EnderIO;
import com.enderio.api.conduit.ConduitItemFactory;
import com.enderio.conduits.common.init.EIOConduitTypes;
import com.google.common.base.Preconditions;
import dev.latvian.mods.kubejs.event.EventJS;
import dev.latvian.mods.rhino.util.HideFromJS;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.HashSet;
import java.util.Set;

public class ConduitRegistryEvent extends EventJS {

    @HideFromJS
    public static final Set<CustomConduitEntry> CONDUITS = new HashSet<>();

    @SuppressWarnings("unused")
    public void registerEnergyConduit(String id, String name, int transferRate) {
        Preconditions.checkArgument(!id.contains(":"), "id must not contain a colon (:)");
        Preconditions.checkArgument(!id.contains(" "), "id must not contain a space");
        Preconditions.checkArgument(
            CONDUITS.stream().noneMatch(conduit -> conduit.id().equals(id)),
            "id must be unique"
        );

        var type = EIOConduitTypes.CONDUIT_TYPES.register(id, () -> new CustomEnergyConduitType(
            EnderIO.loc(id),
            transferRate
        ));

        Item item = ConduitItemFactory.build(type, new Item.Properties());
        ForgeRegistries.ITEMS.register(EnderIO.loc(id), item);

        CONDUITS.add(new CustomConduitEntry(id, name, item));
    }
}

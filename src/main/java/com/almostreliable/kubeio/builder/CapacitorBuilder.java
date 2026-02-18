package com.almostreliable.kubeio.builder;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import com.enderio.enderio.api.capacitor.CapacitorData;
import com.enderio.enderio.api.capacitor.CapacitorModifier;
import com.enderio.enderio.content.capacitors.LootCapacitorItem;
import com.enderio.enderio.init.EIODataComponents;
import dev.latvian.mods.kubejs.item.ItemBuilder;

import java.util.EnumMap;

public class CapacitorBuilder extends ItemBuilder {

    private final EnumMap<CapacitorModifier, Float> modifiers = new EnumMap<>(CapacitorModifier.class);
    private float modifierBaseValue = 1;
    private boolean visibleInCreativeTab = true;

    public CapacitorBuilder(ResourceLocation id) {
        super(id);
        maxStackSize(64);
    }

    public CapacitorBuilder baseValue(float base) {
        this.modifierBaseValue = base;
        return this;
    }

    public CapacitorBuilder modifierValue(CapacitorModifier modifier, float value) {
        modifiers.put(modifier, value);
        return this;
    }

    public CapacitorBuilder hideFromCreativeTab() {
        this.visibleInCreativeTab = false;
        return this;
    }

    @Override
    public Item createObject() {
        var capacitorData = modifiers.isEmpty() ?
            CapacitorData.simple(modifierBaseValue) :
            new CapacitorData(modifierBaseValue, modifiers);
        component(EIODataComponents.CAPACITOR_DATA, capacitorData);

        return new CustomCapacitor(this, capacitorData);
    }

    public static final class CustomCapacitor extends LootCapacitorItem {

        private final CapacitorBuilder builder;
        private final CapacitorData data;

        private CustomCapacitor(CapacitorBuilder builder, CapacitorData data) {
            super(builder.createItemProperties());
            this.builder = builder;
            this.data = data;
        }

        @Override
        public boolean shouldAddDefaultItem() {
            return builder.visibleInCreativeTab;
        }

        @Override
        public Component getName(ItemStack stack) {
            return builder.displayName != null ? builder.displayName : super.getName(stack);
        }
    }
}

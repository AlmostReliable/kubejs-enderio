package com.almostreliable.kubeio.event;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;

import com.enderio.enderio.content.machines.powered_spawner.MobSpawnMode;
import com.enderio.enderio.foundation.souldata.EngineSoul;
import com.enderio.enderio.foundation.souldata.FarmSoul;
import com.enderio.enderio.foundation.souldata.SolarSoul;
import com.enderio.enderio.foundation.souldata.SoulData;
import com.enderio.enderio.foundation.souldata.SoulDataReloadListener;
import com.enderio.enderio.foundation.souldata.SpawnerSoul;
import dev.latvian.mods.kubejs.error.KubeRuntimeException;
import dev.latvian.mods.kubejs.event.KubeEvent;
import dev.latvian.mods.kubejs.script.SourceLine;
import dev.latvian.mods.rhino.Context;
import dev.latvian.mods.rhino.util.HideFromJS;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

@SuppressWarnings({"WeakerAccess", "unused"})
public class SoulDataModificationEvent implements KubeEvent {

    private static final Map<SoulDataReloadListener<?>, SoulDataChanges<?>> CHANGES_BY_LISTENER = new HashMap<>();

    public void spawner(Context cx, ResourceLocation entity, int power, MobSpawnMode spawnMode) {
        validateEntity(cx, entity);
        var soulData = new SpawnerSoul.SoulData(entity, power, spawnMode);
        getChanges(SpawnerSoul.SPAWNER).add(soulData);
    }

    public void spawner(Context cx, ResourceLocation entity, int power) {
        spawner(cx, entity, power, MobSpawnMode.NEW);
    }

    public void engine(Context cx, ResourceLocation entity, String fluid, int powerPerMb, int ticksPerMb) {
        validateEntity(cx, entity);
        validateFluid(cx, fluid);
        var soulData = new EngineSoul.SoulData(entity, fluid, powerPerMb, ticksPerMb);
        getChanges(EngineSoul.ENGINE).add(soulData);
    }

    public void farm(Context cx, ResourceLocation entity, float bonemeal, int seeds, float power) {
        validateEntity(cx, entity);
        var soulData = new FarmSoul.SoulData(entity, bonemeal, seeds, power);
        getChanges(FarmSoul.FARM).add(soulData);
    }

    public void solar(Context cx, ResourceLocation entity, boolean daytime, boolean nighttime, ResourceLocation dimension) {
        validateEntity(cx, entity);
        var dimensionKey = ResourceKey.create(Registries.DIMENSION, dimension);
        var level = Optional.of(dimensionKey);
        var soulData = new SolarSoul.SoulData(entity, daytime, nighttime, level);
        getChanges(SolarSoul.SOLAR).add(soulData);
    }

    public void solar(Context cx, ResourceLocation entity, boolean daytime, boolean nighttime) {
        validateEntity(cx, entity);
        var soulData = new SolarSoul.SoulData(entity, daytime, nighttime, Optional.empty());
        getChanges(SolarSoul.SOLAR).add(soulData);
    }

    public void remove(Context cx, String type, ResourceLocation entity) {
        var listener = getListener(cx, type);
        getChanges(listener).remove(entity);
    }

    public void removeAll(Context cx, String type) {
        var listener = getListener(cx, type);
        getChanges(listener).clear();
    }

    @HideFromJS
    public static void reset() {
        CHANGES_BY_LISTENER.clear();
    }

    @HideFromJS
    public static <T extends SoulData> void applyChanges(SoulDataReloadListener<T> listener) {
        @SuppressWarnings("unchecked")
        var changes = (SoulDataChanges<T>) CHANGES_BY_LISTENER.get(listener);
        if (changes == null) return;

        changes.applyTo(listener.map);
    }

    @SuppressWarnings("unchecked")
    private static <T extends SoulData> SoulDataChanges<T> getChanges(SoulDataReloadListener<T> listener) {
        return (SoulDataChanges<T>) CHANGES_BY_LISTENER.computeIfAbsent(listener, l -> new SoulDataChanges<>());
    }

    private static SoulDataReloadListener<?> getListener(Context cx, String type) {
        var listener = SoulDataReloadListener.fromString(type);
        // noinspection ConstantValue
        if (listener == null) {
            throw error(cx, "unknown soul data type: " + type);
        }

        return listener;
    }

    private static void validateEntity(Context cx, ResourceLocation entity) {
        if (BuiltInRegistries.ENTITY_TYPE.containsKey(entity)) return;
        throw error(cx, "unknown entity type: " + entity);
    }

    private static void validateFluid(Context cx, String fluid) {
        var isTag = fluid.startsWith("#");
        var idString = isTag ? fluid.substring(1) : fluid;
        var id = ResourceLocation.tryParse(idString);
        if (id == null) {
            throw error(cx, "invalid fluid: " + fluid);
        }

        if (isTag || BuiltInRegistries.FLUID.containsKey(id)) return;
        throw error(cx, "unknown fluid: " + fluid);
    }

    private static KubeRuntimeException error(Context cx, String message) {
        return new KubeRuntimeException(message).source(SourceLine.of(cx));
    }

    private static final class SoulDataChanges<T extends SoulData> {

        private final Map<ResourceLocation, T> additionsByEntity = new HashMap<>();
        private final Set<ResourceLocation> removedEntities = new HashSet<>();

        private boolean isCleared;

        private void add(T soulData) {
            var entity = soulData.getKey();
            removedEntities.remove(entity);
            additionsByEntity.put(entity, soulData);
        }

        private void remove(ResourceLocation entity) {
            additionsByEntity.remove(entity);
            removedEntities.add(entity);
        }

        private void clear() {
            isCleared = true;
            additionsByEntity.clear();
            removedEntities.clear();
        }

        private void applyTo(Map<ResourceLocation, T> soulDataByEntity) {
            if (isCleared) {
                soulDataByEntity.clear();
            }

            soulDataByEntity.keySet().removeAll(removedEntities);
            soulDataByEntity.putAll(additionsByEntity);
        }
    }
}

package com.almostreliable.kubeio.mixin;

import com.almostreliable.kubeio.event.SoulDataModificationEvent;

import com.enderio.enderio.foundation.souldata.SoulDataReloadListener;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SoulDataReloadListener.class)
public class SoulDataReloadListenerMixin {

    @Inject(
        method = "apply(Ljava/util/Map;Lnet/minecraft/server/packs/resources/ResourceManager;Lnet/minecraft/util/profiling/ProfilerFiller;)V",
        at = @At("TAIL")
    )
    private void kubeio$applyScriptChanges(CallbackInfo ci) {
        SoulDataModificationEvent.applyChanges((SoulDataReloadListener<?>) (Object) this);
    }
}

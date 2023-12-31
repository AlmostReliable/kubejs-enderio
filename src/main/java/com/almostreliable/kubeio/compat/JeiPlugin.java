package com.almostreliable.kubeio.compat;

import com.almostreliable.kubeio.ModInitializer;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.runtime.IJeiRuntime;
import net.minecraft.resources.ResourceLocation;

@mezz.jei.api.JeiPlugin
public class JeiPlugin implements IModPlugin {

    static IJeiRuntime RUNTIME;

    @Override
    public ResourceLocation getPluginUid() {
        return ModInitializer.getRl("jei");
    }

    @Override
    public void onRuntimeAvailable(IJeiRuntime jeiRuntime) {
        RUNTIME = jeiRuntime;
        JeiAdapter.Adapter.applyRecipeFilters();
    }
}

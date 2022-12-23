package com.robocraft999.createplus.registry;

import com.robocraft999.createplus.CreatePlus;
import com.robocraft999.createplus.item.goggle.module.GoggleModule;
import mekanism.api.MekanismAPI;
import mekanism.api.gear.ModuleData;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class CPModules {

    public static final DeferredRegister<ModuleData<?>> MODULES = DeferredRegister.create(MekanismAPI.moduleRegistryName(), CreatePlus.MODID);

    public static final RegistryObject<ModuleData<?>> GOGGLE_MODULE = MODULES.register("goggle_unit",
            () -> new ModuleData<>(ModuleData.ModuleDataBuilder.custom(GoggleModule::new, CPItems.goggle_unit::get))
    );


    public static void register(IEventBus bus) {
        MODULES.register(bus);
    }
}

package com.robocraft999.createplus;

import com.robocraft999.createplus.data.RecipeDataProvider;
import com.robocraft999.createplus.item.goggle.GoggleArmorLayer;
import com.robocraft999.createplus.lang.CPLang;
import com.robocraft999.createplus.registry.*;
import com.simibubi.create.foundation.data.CreateRegistrate;
import mekanism.api.MekanismIMC;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig.Type;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import top.theillusivec4.curios.api.SlotTypeMessage;

@Mod(CreatePlus.MODID)
public class CreatePlus {
	
	public static CreatePlus INSTANCE;
	public static final String MODID = "createplus";
	public static final Logger logger = LogManager.getLogger(MODID);
	public static final CreateRegistrate REGISTRATE = CreateRegistrate.create(MODID);
	
	public CreatePlus() {
		INSTANCE = this;

		IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        REGISTRATE.registerEventListeners(modEventBus);

		CPBlocks.register();
		CPTileEntities.register();
		CPItems.register();

		CPRecipeTypes.register(modEventBus);

        if(ModCompat.MEKANISM.isLoaded())
			CPModules.register(modEventBus);

		ModLoadingContext.get().registerConfig(Type.COMMON, CPConfig.commonSpec);
		ModLoadingContext.get().registerConfig(Type.CLIENT, CPConfig.clientSpec);

		modEventBus.addListener(this::setup);
		modEventBus.addListener(this::clientRegistries);
		modEventBus.addListener(this::gatherData);

		CPLang.addRestOfLang();

		DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> modEventBus.addListener(this::onLayerRegister));
		
		MinecraftForge.EVENT_BUS.register(this);
	}
	
	private void setup(final FMLCommonSetupEvent event) {
		logger.info("Create Plus Setup");
	}
	
	
	private void clientRegistries(final FMLClientSetupEvent event) {
		logger.info("Create Plus Client Setup");
	}

	private void onLayerRegister(final EntityRenderersEvent.RegisterLayerDefinitions event) {
		event.registerLayerDefinition(GoggleArmorLayer.LAYER, () -> LayerDefinition.create(GoggleArmorLayer.mesh(), 1, 1));
	}

	private void gatherData(GatherDataEvent event){
		logger.info("gathering data");
		event.getGenerator().addProvider(true, new RecipeDataProvider(event.getGenerator()));
	}
	
	
	@Mod.EventBusSubscriber(modid = MODID, bus=Mod.EventBusSubscriber.Bus.MOD)
	public static class RegistryEvents {

		@SuppressWarnings("unused")
		@SubscribeEvent
		public static void enqueueIMC(final InterModEnqueueEvent event) {
			if(ModCompat.CURIOS.isLoaded()) {
				if(CPConfig.COMMON.useCustomCurioBacktankSlot.get()){
					InterModComms.sendTo(MODID, "curios", SlotTypeMessage.REGISTER_TYPE,
							() -> new SlotTypeMessage.Builder("createplus.backtank_slot").size(1).icon(new ResourceLocation(MODID, "item/backtank_slot_icon")).build());
				}

			}
			if(ModCompat.MEKANISM.isLoaded()){
				MekanismIMC.addMekaSuitHelmetModules(CPModules.GOGGLE_MODULE.get());
			}
		}
	}
}

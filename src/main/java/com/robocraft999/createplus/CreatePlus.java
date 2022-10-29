package com.robocraft999.createplus;

import com.robocraft999.createplus.data.RecipeDataProvider;
import com.robocraft999.createplus.item.goggle.GoggleArmorLayer;
import com.robocraft999.createplus.lists.ModCompat;
import com.robocraft999.createplus.lists.ModuleList;
import mekanism.api.MekanismIMC;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.RegisterColorHandlersEvent;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig.Type;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.robocraft999.createplus.lists.ItemList;
import com.robocraft999.createplus.lists.RecipeTypeList;

import net.minecraft.world.item.DyeableLeatherItem;
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
	private static final Logger logger = LogManager.getLogger(MODID);
	
	public CreatePlus() {
		INSTANCE = this;

		IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

		ItemList.register(modEventBus);
		RecipeTypeList.register(modEventBus);

        if(ModCompat.MEKANISM.isLoaded())
			ModuleList.register(modEventBus);

		ModLoadingContext.get().registerConfig(Type.COMMON, CPConfig.commonSpec);
		ModLoadingContext.get().registerConfig(Type.CLIENT, CPConfig.clientSpec);

		modEventBus.addListener(this::setup);
		modEventBus.addListener(this::clientRegistries);
		modEventBus.addListener(this::gatherData);
		
		DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> modEventBus.addListener(this::registerItemColors));
		DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> modEventBus.addListener(this::onLayerRegister));
		
		MinecraftForge.EVENT_BUS.register(this);
	}
	
	private void setup(final FMLCommonSetupEvent event) {
		logger.info("Create Plus Setup");
	}
	
	
	private void clientRegistries(final FMLClientSetupEvent event) {
		logger.info("Create Plus Client Setup");
	}
	
	private void registerItemColors(final RegisterColorHandlersEvent.Item event){
	    event.getItemColors().register((stack, color) -> color > 0 ? -1 : ((DyeableLeatherItem)stack.getItem()).getColor(stack), ItemList.goggle_leather_helmet.get());
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
				MekanismIMC.addMekaSuitHelmetModules(ModuleList.GOGGLE_MODULE.get());
			}
		}
	}
}

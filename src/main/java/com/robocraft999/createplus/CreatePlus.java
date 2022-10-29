package com.robocraft999.createplus;

import com.robocraft999.createplus.data.RecipeDataProvider;
import com.robocraft999.createplus.lists.ModCompat;
import com.robocraft999.createplus.lists.ModuleList;
import mekanism.api.MekanismIMC;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.world.item.Items;
import net.minecraftforge.client.event.RegisterColorHandlersEvent;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.robocraft999.createplus.lists.ItemList;
import com.robocraft999.createplus.lists.RecipeTypeList;

import net.minecraft.world.item.DyeableLeatherItem;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.crafting.conditions.ModLoadedCondition;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

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
		
		modEventBus.addListener(this::setup);
		modEventBus.addListener(this::clientRegistries);
		modEventBus.addListener(this::gatherData);
		
		DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> modEventBus.addListener(this::registerItemColors));
		
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
				/*InterModComms.sendTo(MODID, "curios", SlotTypeMessage.REGISTER_TYPE,
						() -> new SlotTypeMessage.Builder("createplus.backtank_slot").size(1).icon(new ResourceLocation(MODID, "item/goggle_slot_icon")).build());*/

			}
			if(ModCompat.MEKANISM.isLoaded()){
				MekanismIMC.addMekaSuitHelmetModules(ModuleList.GOGGLE_MODULE.get());
			}
		}
	}
}

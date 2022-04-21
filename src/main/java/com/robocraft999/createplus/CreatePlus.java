package com.robocraft999.createplus;

import net.minecraftforge.client.gui.OverlayRegistry;
import net.minecraftforge.eventbus.api.IEventBus;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.robocraft999.createplus.lists.ItemList;
import com.robocraft999.createplus.lists.RecipeTypeList;
import com.simibubi.create.content.contraptions.goggles.GoggleOverlayRenderer;

import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.item.DyeableLeatherItem;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ColorHandlerEvent;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.crafting.conditions.ModLoadedCondition;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import top.theillusivec4.curios.api.SlotTypeMessage;

@Mod("createplus")
public class CreatePlus {
	
	public static CreatePlus INSTANCE;
	public static final String MODID = "createplus";
	private static final Logger logger = LogManager.getLogger(MODID);
	
	public CreatePlus() {
		INSTANCE = this;

		IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

		ItemList.register(modEventBus);
		
		modEventBus.addListener(this::setup);
		modEventBus.addListener(this::clientRegistries);
		
		DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> modEventBus.addListener(this::registerItemColors));

		//MinecraftForge.EVENT_BUS.register(new ClientEvents());
		modEventBus.addGenericListener(RecipeSerializer.class, RecipeTypeList::register);
		
		MinecraftForge.EVENT_BUS.register(this);
	}
	
	private void setup(final FMLCommonSetupEvent event) {
		logger.info("Create Plus Setup");
	}
	
	
	private void clientRegistries(final FMLClientSetupEvent event) {
		//OverlayRegistry.registerOverlayAbove(ForgeIngameGui.HOTBAR_ELEMENT, "Create's Goggle Information", GoggleOverlayRenderer.OVERLAY);
        OverlayRegistry.enableOverlay(GoggleOverlayRenderer.OVERLAY, false);
        OverlayRegistry.registerOverlayAbove(GoggleOverlayRenderer.OVERLAY, "Createplus's Goggle Information", com.robocraft999.createplus.item.goggle.GoggleOverlayRenderer.OVERLAY);
		logger.info("Create Plus Client Setup");
	}
	
	private void registerItemColors(final ColorHandlerEvent.Item event){
	    event.getItemColors().register((stack, color) -> color > 0 ? -1 : ((DyeableLeatherItem)stack.getItem()).getColor(stack), ItemList.goggle_leather_helmet.get());
	}
	
	
	@Mod.EventBusSubscriber(modid = MODID, bus=Mod.EventBusSubscriber.Bus.MOD)
	public static class RegistryEvents {

		@SuppressWarnings("unused")
		@SubscribeEvent
		public static void enqueueIMC(final InterModEnqueueEvent event) {
			ModLoadedCondition curios_loaded = new ModLoadedCondition("curios");
			if(curios_loaded.test())
				/*InterModComms.sendTo(MODID, "curios", SlotTypeMessage.REGISTER_TYPE,
						() -> new SlotTypeMessage.Builder("createplus.backtank_slot").size(1).icon(new ResourceLocation(MODID, "item/goggle_slot_icon")).build());*/
				
				InterModComms.sendTo(MODID, "curios", SlotTypeMessage.REGISTER_TYPE, 
				() -> new SlotTypeMessage.Builder("createplus.goggle_slot").size(1).icon(new ResourceLocation(MODID, "item/goggle_slot_icon")).build());
		}
	}
	
	@Mod.EventBusSubscriber(modid = MODID, value = Dist.CLIENT, bus = Bus.MOD)
	  public static class ClientProxy {

		@SuppressWarnings("unused")
	    @SubscribeEvent
	    public static void stitchTextures(TextureStitchEvent.Pre evt) {


	      if (evt.getAtlas().location() == InventoryMenu.BLOCK_ATLAS) {
	        evt.addSprite(new ResourceLocation(MODID, "item/goggle_slot_icon"));
	      }
	    }
	}
}

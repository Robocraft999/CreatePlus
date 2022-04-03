package com.robocraft999.createplus;

import java.util.function.Supplier;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.robocraft999.createplus.item.goggle.DivingGoggleArmor;
import com.robocraft999.createplus.item.goggle.DyableGoggleArmor;
import com.robocraft999.createplus.item.goggle.GoggleArmor;
import com.robocraft999.createplus.lists.ArmorMaterialList;
import com.robocraft999.createplus.lists.ItemList;
import com.robocraft999.createplus.lists.RecipeTypeList;
import com.simibubi.create.content.contraptions.goggles.GoggleOverlayRenderer;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.item.DyeableLeatherItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ColorHandlerEvent;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.crafting.conditions.ModLoadedCondition;
import net.minecraftforge.event.RegistryEvent;
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
		
		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::clientRegistries);
		
		DistExecutor.safeRunWhenOn(Dist.CLIENT, () -> () -> FMLJavaModLoadingContext.get().getModEventBus().addListener(this::registerItemColors));
		
		FMLJavaModLoadingContext.get().getModEventBus().addGenericListener(RecipeSerializer.class, RecipeTypeList::register);
		
		MinecraftForge.EVENT_BUS.register(this);
	}
	
	private void setup(final FMLCommonSetupEvent event) {
		logger.info("Create Plus Setup");
	}
	
	
	private void clientRegistries(final FMLClientSetupEvent event) {
		logger.info("Create Plus Client Setup");
	}
	
	private void registerItemColors(final ColorHandlerEvent.Item event){
	    event.getItemColors().register((stack, color) -> color > 0 ? -1 : ((DyeableLeatherItem)stack.getItem()).getColor(stack), ItemList.goggle_leather_helmet);
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

		@SuppressWarnings("unused")
		@SubscribeEvent
		public static void registerItems(final RegistryEvent.Register<Item> event) {
			event.getRegistry().registerAll(
					ItemList.goggle_chainmail_helmet = new GoggleArmor(ArmorMaterialList.GOGGLE_CHAIN, new Item.Properties().tab(CreativeModeTab.TAB_COMBAT)).setRegistryName(location("goggle_chainmail_helmet")),
					ItemList.goggle_diamond_helmet = new GoggleArmor(ArmorMaterialList.GOGGLE_DIAMOND, new Item.Properties().tab(CreativeModeTab.TAB_COMBAT)).setRegistryName(location("goggle_diamond_helmet")),
					ItemList.goggle_golden_helmet = new GoggleArmor(ArmorMaterialList.GOGGLE_GOLD, new Item.Properties().tab(CreativeModeTab.TAB_COMBAT)).setRegistryName(location("goggle_golden_helmet")),
					ItemList.goggle_iron_helmet = new GoggleArmor(ArmorMaterialList.GOGGLE_IRON, new Item.Properties().tab(CreativeModeTab.TAB_COMBAT)).setRegistryName(location("goggle_iron_helmet")),
					ItemList.goggle_leather_helmet = new DyableGoggleArmor(ArmorMaterialList.GOGGLE_LEATHER, new Item.Properties().tab(CreativeModeTab.TAB_COMBAT)).setRegistryName(location("goggle_leather_helmet")),
					ItemList.goggle_turtle_helmet = new GoggleArmor(ArmorMaterialList.GOGGLE_TURTLE, new Item.Properties().tab(CreativeModeTab.TAB_COMBAT)).setRegistryName(location("goggle_turtle_helmet")),
					ItemList.goggle_netherite_helmet = new GoggleArmor(ArmorMaterialList.GOGGLE_NETHERITE, new Item.Properties().tab(CreativeModeTab.TAB_COMBAT).fireResistant()).setRegistryName(location("goggle_netherite_helmet")),
					ItemList.goggle_diving_helmet = new DivingGoggleArmor(ArmorMaterialList.GOGGLE_DIVING, new Item.Properties().tab(CreativeModeTab.TAB_COMBAT)).setRegistryName(location("goggle_diving_helmet"))
			);
			ItemList.addGogglesToList();
			
			GoggleOverlayRenderer.registerCustomGoggleCondition(com.robocraft999.createplus.item.goggle.GoggleOverlayRenderer.customGogglePredicate);
			
			logger.info("Items registered");
		}

		@SuppressWarnings("unused")
		@SubscribeEvent
		public static void registerBlocks(final RegistryEvent.Register<Block> event) {
			event.getRegistry().registerAll(
					
			);
		}

		@SuppressWarnings("unused")
		@SubscribeEvent
		public static void registerContainer(final RegistryEvent.Register<MenuType<?>> event) {
			event.getRegistry().registerAll(
					
			);
		}
		
		private static ResourceLocation location(String name) {
			return new ResourceLocation(MODID,name);
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

package com.robocraft999.createplus.lists;

import com.robocraft999.createplus.CreatePlus;
import com.robocraft999.createplus.item.goggle.DivingGoggleArmor;
import com.robocraft999.createplus.item.goggle.DyableGoggleArmor;
import com.robocraft999.createplus.item.goggle.GoggleArmor;

import mekanism.api.MekanismAPI;
import mekanism.api.providers.IModuleDataProvider;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraftforge.common.crafting.conditions.ModLoadedCondition;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ItemList {

	public static final DeferredRegister<Item> ITEM_REGISTER = DeferredRegister.create(ForgeRegistries.ITEMS, CreatePlus.MODID);

	public static final RegistryObject<Item>
		goggle_chainmail_helmet = ITEM_REGISTER.register("goggle_chainmail_helmet", () -> new GoggleArmor(ArmorMaterialList.GOGGLE_CHAIN, new Item.Properties().tab(CreativeModeTab.TAB_COMBAT))),
		goggle_diamond_helmet = ITEM_REGISTER.register("goggle_diamond_helmet", () -> new GoggleArmor(ArmorMaterialList.GOGGLE_DIAMOND, new Item.Properties().tab(CreativeModeTab.TAB_COMBAT))),
		goggle_golden_helmet = ITEM_REGISTER.register("goggle_golden_helmet", () -> new GoggleArmor(ArmorMaterialList.GOGGLE_GOLD, new Item.Properties().tab(CreativeModeTab.TAB_COMBAT))),
		goggle_iron_helmet = ITEM_REGISTER.register("goggle_iron_helmet", () -> new GoggleArmor(ArmorMaterialList.GOGGLE_IRON, new Item.Properties().tab(CreativeModeTab.TAB_COMBAT))),
		goggle_leather_helmet = ITEM_REGISTER.register("goggle_leather_helmet", () -> new DyableGoggleArmor(ArmorMaterialList.GOGGLE_LEATHER, new Item.Properties().tab(CreativeModeTab.TAB_COMBAT))),
		goggle_turtle_helmet = ITEM_REGISTER.register("goggle_turtle_helmet", () -> new GoggleArmor(ArmorMaterialList.GOGGLE_TURTLE, new Item.Properties().tab(CreativeModeTab.TAB_COMBAT))),
		goggle_netherite_helmet = ITEM_REGISTER.register("goggle_netherite_helmet", () -> new GoggleArmor(ArmorMaterialList.GOGGLE_NETHERITE, new Item.Properties().tab(CreativeModeTab.TAB_COMBAT).fireResistant())),
		goggle_diving_helmet = ITEM_REGISTER.register("goggle_diving_helmet", () -> new DivingGoggleArmor(ArmorMaterialList.GOGGLE_DIVING, new Item.Properties().tab(CreativeModeTab.TAB_COMBAT)));

	public static final RegistryObject<Item>
		goggle_unit = registerGoggleModule(new Item.Properties().tab(CreativeModeTab.TAB_SEARCH));

	public static void register(IEventBus bus) {
		ITEM_REGISTER.register(bus);
	}

	private static RegistryObject<Item> registerGoggleModule(Item.Properties properties){
		if (ModCompat.MEKANISM.isLoaded()) {
			return ITEM_REGISTER.register("module_goggle_unit", () -> MekanismAPI.getModuleHelper().createModuleItem((IModuleDataProvider) ModuleList.GOGGLE_MODULE::get, properties));
		}
		return null;
	}
}

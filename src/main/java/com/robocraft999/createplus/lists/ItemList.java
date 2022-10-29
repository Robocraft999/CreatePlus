package com.robocraft999.createplus.lists;

import com.robocraft999.createplus.CreatePlus;
import com.robocraft999.createplus.item.backtank.BacktankArmor;
import com.robocraft999.createplus.item.goggle.DivingGoggleArmor;
import com.robocraft999.createplus.item.goggle.DyableGoggleArmor;
import com.robocraft999.createplus.item.goggle.GoggleArmor;

import com.simibubi.create.content.curiosities.armor.AllArmorMaterials;
import mekanism.api.MekanismAPI;
import mekanism.api.providers.IModuleDataProvider;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ArmorMaterials;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public class ItemList {

	public static final DeferredRegister<Item> ITEM_REGISTER = DeferredRegister.create(ForgeRegistries.ITEMS, CreatePlus.MODID);

	public static final RegistryObject<Item>
		goggle_chainmail_helmet = goggleHelmet("goggle_chainmail_helmet", ArmorMaterials.CHAIN),
		goggle_diamond_helmet = goggleHelmet("goggle_diamond_helmet", ArmorMaterials.DIAMOND),
		goggle_golden_helmet = goggleHelmet("goggle_golden_helmet", ArmorMaterials.GOLD),
		goggle_iron_helmet = goggleHelmet("goggle_iron_helmet", ArmorMaterials.IRON),
		goggle_leather_helmet = customGoggleHelmet("goggle_leather_helmet", () -> new DyableGoggleArmor(ArmorMaterials.LEATHER, new Item.Properties().tab(CreativeModeTab.TAB_COMBAT))),
		goggle_turtle_helmet = goggleHelmet("goggle_turtle_helmet", ArmorMaterials.TURTLE),
		goggle_netherite_helmet = goggleHelmet("goggle_netherite_helmet", ArmorMaterials.NETHERITE),
		goggle_diving_helmet = customGoggleHelmet("goggle_diving_helmet", () -> new DivingGoggleArmor(AllArmorMaterials.COPPER, new Item.Properties().tab(CreativeModeTab.TAB_COMBAT))),
		backtank_chainmail = ITEM_REGISTER.register("backtank_chainmail", () -> new BacktankArmor(ArmorMaterials.CHAIN, new Item.Properties().tab(CreativeModeTab.TAB_COMBAT)));

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

	private static RegistryObject<Item> goggleHelmet(String name, ArmorMaterial material){
		return ITEM_REGISTER.register(name, () -> new GoggleArmor(material, new Item.Properties().tab(CreativeModeTab.TAB_COMBAT)));
	}

	private static RegistryObject<Item> customGoggleHelmet(String name, Supplier<? extends GoggleArmor> item){
		return ITEM_REGISTER.register(name, item);
	}
}

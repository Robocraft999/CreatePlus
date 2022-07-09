package com.robocraft999.createplus.lists;

import java.util.ArrayList;

import com.robocraft999.createplus.CreatePlus;
import com.robocraft999.createplus.item.goggle.DivingGoggleArmor;
import com.robocraft999.createplus.item.goggle.DyableGoggleArmor;
import com.robocraft999.createplus.item.goggle.GoggleArmor;
import com.simibubi.create.AllItems;

import com.simibubi.create.content.curiosities.armor.AllArmorMaterials;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorMaterials;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.crafting.conditions.ModLoadedCondition;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import top.theillusivec4.curios.api.CuriosApi;

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



	public static boolean isGoggleHelmet(Player player) {
		ItemStack headSlot = player.getItemBySlot(EquipmentSlot.HEAD);

		for(RegistryObject<Item> regItem : ItemList.ITEM_REGISTER.getEntries()){
			if(regItem.get().equals(headSlot.getItem()))return true;
		}

		ModLoadedCondition curiosloaded = new ModLoadedCondition("curios");
		if(curiosloaded.test()) {
			LazyOptional<IItemHandlerModifiable> test = CuriosApi.getCuriosHelper().getEquippedCurios(player);
			IItemHandlerModifiable test2 = test.orElse(null);
			for(int i = 0; i < test2.getSlots();i++) {
				ItemStack curiosSlot = test2.getStackInSlot(i);
				if(curiosSlot.getItem() == AllItems.GOGGLES.get())return true;
			}
		}
		/*
		ModLoadedCondition mekloaded = new ModLoadedCondition("mekanism");
		if(mekloaded.test()) {
			if(headSlot.getItem() == MekanismItems.MEKASUIT_HELMET.asItem()) {
				ItemMekaSuitArmor helmet = (ItemMekaSuitArmor)headSlot.getItem();
				if(helmet.hasModule(headSlot, Modules.VISION_ENHANCEMENT_UNIT)) {//TODO add own Module
					if(helmet.isModuleEnabled(headSlot, Modules.VISION_ENHANCEMENT_UNIT)) {//TODO add own Module
						wearingGoggles = true;
					}
				}
			}
		}*/

		return false;
	}

	public static void register(IEventBus bus) {
		ITEM_REGISTER.register(bus);
	}
	
}

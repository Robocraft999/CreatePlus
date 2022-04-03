package com.robocraft999.createplus.lists;

import java.util.ArrayList;

import com.simibubi.create.AllItems;

import net.minecraft.client.Minecraft;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.crafting.conditions.ModLoadedCondition;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandlerModifiable;
import top.theillusivec4.curios.api.CuriosApi;

public class ItemList {
	
	public static ArrayList<Item> GOGGLES = new ArrayList<>();
	
	public static Item goggle_leather_helmet;
	public static Item goggle_chainmail_helmet;
	public static Item goggle_iron_helmet;
	public static Item goggle_diamond_helmet;
	public static Item goggle_golden_helmet;
	public static Item goggle_turtle_helmet;
	public static Item goggle_netherite_helmet;
	public static Item goggle_diving_helmet;
	
	public static void addGogglesToList() {
		GOGGLES.add(goggle_leather_helmet);
		GOGGLES.add(goggle_chainmail_helmet);
		GOGGLES.add(goggle_iron_helmet);
		GOGGLES.add(goggle_diamond_helmet);
		GOGGLES.add(goggle_golden_helmet);
		GOGGLES.add(goggle_turtle_helmet);
		GOGGLES.add(goggle_netherite_helmet);
		GOGGLES.add(goggle_diving_helmet);
	}
	
	public static boolean isGoggleHelmet(ItemStack headSlot) {
		Minecraft mc = Minecraft.getInstance();

		boolean wearingGoggles = GOGGLES.contains(headSlot.getItem());
		
		ModLoadedCondition curiosloaded = new ModLoadedCondition("curios");
		if(curiosloaded.test()) {
			LazyOptional<IItemHandlerModifiable> test = CuriosApi.getCuriosHelper().getEquippedCurios(mc.player);
			IItemHandlerModifiable test2 = test.orElse(null);
			for(int i = 0; i < test2.getSlots();i++) {
				ItemStack curiosSlot = test2.getStackInSlot(i);
				if(curiosSlot.getItem() == AllItems.GOGGLES.get())wearingGoggles = true;
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
		
		return wearingGoggles;
	}
	
}

package com.robocraft999.createplus.item.goggle;

import net.minecraft.item.IArmorMaterial;
import net.minecraft.item.IDyeableArmorItem;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.event.ColorHandlerEvent;

public class DyableGoggleArmor extends GoggleArmor implements IDyeableArmorItem{

	public DyableGoggleArmor(IArmorMaterial material, Properties properties) {
		super(material, properties);
	}
	
	@Override
	public void setColor(ItemStack stack, int color) {
		IDyeableArmorItem.super.setColor(stack, color);
		System.out.println("color set");
		System.out.println(getColor(stack));
	}
	
}

package com.robocraft999.createplus.item.goggle;

import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.DyeableLeatherItem;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nonnull;

public class DyableGoggleArmor extends GoggleArmor implements DyeableLeatherItem{

	public DyableGoggleArmor(ArmorMaterial material, Properties properties) {
		super(material, properties);
	}
	
	@Override
	public void setColor(@Nonnull ItemStack stack, int color) {
		DyeableLeatherItem.super.setColor(stack, color);
	}
	
}

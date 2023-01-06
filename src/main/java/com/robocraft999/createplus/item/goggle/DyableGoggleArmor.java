package com.robocraft999.createplus.item.goggle;

import net.minecraft.client.color.item.ItemColor;
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

	public static class DyableGoggleArmorColor implements ItemColor{
		@Override
		public int getColor(ItemStack stack, int layer) {
			return layer > 0 ? -1 : ((DyeableLeatherItem)stack.getItem()).getColor(stack);
		}
	}
	
}

package com.robocraft999.createplus.item.backtank;

import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.DyeableLeatherItem;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nonnull;

public class DyableBacktankArmor extends BacktankArmor implements DyeableLeatherItem {
    public DyableBacktankArmor(ArmorMaterial material, String id, Properties properties) {
        super(material, id, properties);
    }

    @Override
    public void setColor(@Nonnull ItemStack stack, int color) {
        DyeableLeatherItem.super.setColor(stack, color);
    }
}

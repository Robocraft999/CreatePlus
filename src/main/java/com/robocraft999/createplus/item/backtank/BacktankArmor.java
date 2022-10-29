package com.robocraft999.createplus.item.backtank;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;

public class BacktankArmor extends ArmorItem {
    public BacktankArmor(ArmorMaterial material, Properties properties) {
        super(material, EquipmentSlot.CHEST, properties);
    }

    public static boolean isWornBy(LivingEntity entity){
        return entity.getItemBySlot(EquipmentSlot.CHEST).getItem() instanceof BacktankArmor;
    }
}

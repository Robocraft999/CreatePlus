package com.robocraft999.createplus.item.goggle;

import javax.annotation.Nonnull;

import com.robocraft999.createplus.CreatePlus;

import com.robocraft999.createplus.registry.ModCompat;
import com.robocraft999.createplus.registry.CPModules;
import mekanism.api.MekanismAPI;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.level.Level;

import java.util.List;

public class GoggleArmor extends ArmorItem{

	private static final String TAG_NAME = "goggle";
	public GoggleArmor(ArmorMaterial material, Properties properties) {
		super(material, EquipmentSlot.HEAD, properties);
		DispenserBlock.registerBehavior(this, ArmorItem.DISPENSE_ITEM_BEHAVIOR);
	}

	@Override
	public EquipmentSlot getEquipmentSlot(ItemStack stack) {
		return EquipmentSlot.HEAD;
	}

	@Nonnull
	public InteractionResultHolder<ItemStack> use(@Nonnull Level worldIn, Player playerIn, @Nonnull InteractionHand handIn) {
		ItemStack itemstack = playerIn.getItemInHand(handIn);
		EquipmentSlot equipmentslottype = Mob.getEquipmentSlotForItem(itemstack);
		ItemStack slot = playerIn.getItemBySlot(equipmentslottype);
		if (slot.isEmpty()) {
			playerIn.setItemSlot(equipmentslottype, itemstack.copy());
			itemstack.setCount(0);
			return new InteractionResultHolder<>(InteractionResult.SUCCESS, itemstack);
		} else {
			return new InteractionResultHolder<>(InteractionResult.FAIL, itemstack);
		}
	}

	@Override
  	public boolean makesPiglinsNeutral(@Nonnull ItemStack stack, @Nonnull LivingEntity wearer) {
		return getMaterial() == ArmorMaterials.GOLD;
	}

	public static boolean isGoggleHelmet(Player player) {
		ItemStack headSlot = player.getItemBySlot(EquipmentSlot.HEAD);

		List<TagKey<Item>> tags = headSlot.getTags().filter(tag -> tag.location().getNamespace().equals(CreatePlus.MODID)).toList();
		for(TagKey<Item> tag : tags){
			if(tag.location().getPath().equals(TAG_NAME)){
				return true;
			}
		}

		if(ModCompat.MEKANISM.isLoaded()) {
			return MekanismAPI.getModuleHelper().isEnabled(headSlot, CPModules.GOGGLE_MODULE.get());
		}

		return false;
	}

	public static boolean isWornBy(LivingEntity entity){
		return entity.getItemBySlot(EquipmentSlot.HEAD).getItem() instanceof GoggleArmor;
	}
}

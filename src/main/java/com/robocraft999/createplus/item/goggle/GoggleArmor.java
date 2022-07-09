package com.robocraft999.createplus.item.goggle;

import javax.annotation.Nonnull;

import com.robocraft999.createplus.lists.ArmorMaterialList;

import com.robocraft999.createplus.lists.ItemList;
import com.simibubi.create.AllItems;
import com.simibubi.create.content.contraptions.goggles.GogglesItem;
import net.minecraft.client.Minecraft;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.crafting.conditions.ModLoadedCondition;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.registries.RegistryObject;
import top.theillusivec4.curios.api.CuriosApi;

public class GoggleArmor extends ArmorItem{

	public GoggleArmor(ArmorMaterial material, Properties properties) {
		super(material, EquipmentSlot.HEAD, properties);
		DispenserBlock.registerBehavior(this, ArmorItem.DISPENSE_ITEM_BEHAVIOR);
		GogglesItem.addIsWearingPredicate(this::isGoggleHelmet);
	}

	@Override
	public EquipmentSlot getEquipmentSlot(ItemStack stack) {
		return EquipmentSlot.HEAD;
	}

	@Nonnull
	public InteractionResultHolder<ItemStack> use(@Nonnull Level worldIn, Player playerIn, @Nonnull InteractionHand handIn) {
		ItemStack itemstack = playerIn.getItemInHand(handIn);
		EquipmentSlot equipmentslottype = Mob.getEquipmentSlotForItem(itemstack);
		ItemStack itemstack1 = playerIn.getItemBySlot(equipmentslottype);
		if (itemstack1.isEmpty()) {
			playerIn.setItemSlot(equipmentslottype, itemstack.copy());
			itemstack.setCount(0);
			return new InteractionResultHolder<>(InteractionResult.SUCCESS, itemstack);
		} else {
			return new InteractionResultHolder<>(InteractionResult.FAIL, itemstack);
		}
	}

	@Override
  	public boolean makesPiglinsNeutral(@Nonnull ItemStack stack, @Nonnull LivingEntity wearer) {
		return getMaterial() == ArmorMaterialList.GOGGLE_GOLD;
	}

	public boolean isGoggleHelmet(Player player) {
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
}

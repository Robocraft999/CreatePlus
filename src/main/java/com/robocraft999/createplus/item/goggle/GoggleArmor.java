package com.robocraft999.createplus.item.goggle;

import javax.annotation.Nonnull;

import com.robocraft999.createplus.CreatePlus;
import com.robocraft999.createplus.lists.ArmorMaterialList;

import com.robocraft999.createplus.lists.ItemList;
import com.robocraft999.createplus.lists.ModuleList;
import com.simibubi.create.AllItems;
import com.simibubi.create.content.contraptions.goggles.GogglesItem;
import mekanism.api.MekanismAPI;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.inventory.Slot;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class GoggleArmor extends ArmorItem{

	private final String TAG_NAME = "goggle";
	public GoggleArmor(ArmorMaterial material, Properties properties) {
		super(material, EquipmentSlot.HEAD, properties);
		DispenserBlock.registerBehavior(this, ArmorItem.DISPENSE_ITEM_BEHAVIOR);
		GogglesItem.addIsWearingPredicate(this::isGoggleHelmet); //TODO move somewhere where its not called by each new instance
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
		return getMaterial() == ArmorMaterialList.GOGGLE_GOLD;
	}

	public boolean isGoggleHelmet(Player player) {
		ItemStack headSlot = player.getItemBySlot(EquipmentSlot.HEAD);

		List<TagKey<Item>> tags = headSlot.getTags().filter(tag -> tag.location().getNamespace().equals(CreatePlus.MODID)).toList();
		for(TagKey<Item> tag : tags){
			if(tag.location().getPath().equals(TAG_NAME)){
				return true;
			}
		}

		ModLoadedCondition mekloaded = new ModLoadedCondition("mekanism");
		if(mekloaded.test()) {
			return MekanismAPI.getModuleHelper().isEnabled(headSlot, ModuleList.GOGGLE_MODULE.get());
		}

		return false;
	}
}

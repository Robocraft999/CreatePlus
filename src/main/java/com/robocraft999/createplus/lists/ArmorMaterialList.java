package com.robocraft999.createplus.lists;

import com.robocraft999.createplus.CreatePlus;

import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.IArmorMaterial;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;

import com.simibubi.create.AllItems;

public enum ArmorMaterialList implements IArmorMaterial{

	
	GOGGLE_CHAIN("goggle_chainmail", 15, new int[]{1, 4, 5, 2}, 12, Items.IRON_INGOT,"item.armor.equip_chain", 0.0F, 0.0F),
	GOGGLE_DIAMOND("goggle_diamond", 33, new int[]{3, 6, 8, 3}, 10, Items.DIAMOND,"item.armor.equip_diamond", 2.0F, 0.0F),
	GOGGLE_GOLD("goggle_gold", 7, new int[]{1, 3, 5, 2}, 25, Items.GOLD_INGOT,"item.armor.equip_gold", 0.0F, 0.0F),
	GOGGLE_IRON("goggle_iron", 15, new int[]{2, 5, 6, 2}, 9, Items.IRON_INGOT,"item.armor.equip_iron", 0.0F, 0.0F),
	GOGGLE_LEATHER("goggle_leather", 5, new int[]{1, 2, 3, 1}, 15, Items.LEATHER,"item.armor.equip_leather", 0.0F, 0.0F),
	GOGGLE_TURTLE("goggle_turtle", 25, new int[]{2, 5, 6, 2}, 9, Items.SCUTE,"item.armor.equip_turtle", 0.0F, 0.0F),
	GOGGLE_NETHERITE("goggle_netherite",37, new int[]{3, 6, 8, 3}, 15, Items.NETHERITE_INGOT, "item.armor.equip_netherite",3.0F, 0.1F),
	GOGGLE_DIVING("goggle_diving", 7, new int[]{1, 3, 4, 2}, 25, AllItems.COPPER_INGOT.get(), "item.armor.equip_gold", 0.0F, 0.0F);
	
	private final int[] max_damage_array = new int[]{13,15,16,11};
	private String name, equipSound;
	private int durability, enchantability;
	private Item repairItem;
	private int[] damageReductionAmount;
	private float toughness;
	private float knockbackResistance;
	
	private ArmorMaterialList(String name, int durability, int[] damageReductionAmount, int enchantability, Item repairItem, String equipSound, float toughness, float knockbackResistance) {
		this.name = name;
		this.durability = durability;
		this.damageReductionAmount = damageReductionAmount;
		this.enchantability = enchantability;
		this.repairItem = repairItem;
		this.equipSound = equipSound;
		this.toughness = toughness;
		this.knockbackResistance = knockbackResistance;
	}

	@Override
	public int getDurabilityForSlot(EquipmentSlotType slotIn) {
		return this.max_damage_array[slotIn.getIndex()] * this.durability;
	}

	@Override
	public int getDefenseForSlot(EquipmentSlotType slotIn) {
		return this.damageReductionAmount[slotIn.getIndex()];
	}

	@Override
	public int getEnchantmentValue() {
		return this.enchantability;
	}

	@Override
	public SoundEvent getEquipSound() {
		return new SoundEvent(new ResourceLocation(this.equipSound));
	}

	@Override
	public Ingredient getRepairIngredient() {
		return Ingredient.of(this.repairItem);
	}

	@Override
	public String getName() {
		return CreatePlus.MODID + ":" + this.name;
	}

	@Override
	public float getToughness() {
		return this.toughness;
	}

	@Override
	public float getKnockbackResistance() {
		return this.knockbackResistance;
	}
}

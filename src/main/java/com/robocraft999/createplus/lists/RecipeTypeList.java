package com.robocraft999.createplus.lists;

import java.util.function.Supplier;

import com.robocraft999.createplus.CreatePlus;
import com.robocraft999.createplus.item.crafting.GHelmetCrafingRecipe;
import com.simibubi.create.foundation.utility.Lang;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.ShapedRecipe;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraftforge.event.RegistryEvent;

public enum RecipeTypeList {

	HELMET_CRAFTING(GHelmetCrafingRecipe.Serializer::new, IRecipeType.CRAFTING);
	
	public IRecipeSerializer<?> serializer;
	public Supplier<IRecipeSerializer<?>> supplier;
	public IRecipeType<? extends IRecipe<? extends IInventory>> type;

	RecipeTypeList(Supplier<IRecipeSerializer<?>> supplier,IRecipeType<? extends IRecipe<? extends IInventory>> existingType) {
		this.supplier = supplier;
		this.type = existingType;
	}
	
	public static void register(RegistryEvent.Register<IRecipeSerializer<?>> event) {
		ShapedRecipe.setCraftingSize(9, 9);

		for (RecipeTypeList r : RecipeTypeList.values()) {
			if (r.type == null)
				r.type = customType(Lang.asId(r.name()));

			r.serializer = r.supplier.get();
			ResourceLocation location = new ResourceLocation(CreatePlus.MODID, Lang.asId(r.name()));
			event.getRegistry()
				.register(r.serializer.setRegistryName(location));
			//System.out.println(r.serializer.getRegistryName());
		}
	}
	
	private static <T extends IRecipe<?>> IRecipeType<T> customType(String id) {
		return Registry.register(Registry.RECIPE_TYPE, new ResourceLocation(CreatePlus.MODID, id), new IRecipeType<T>() {
			public String toString() {
				return CreatePlus.MODID + ":" + id;
			}
		});
	}
}

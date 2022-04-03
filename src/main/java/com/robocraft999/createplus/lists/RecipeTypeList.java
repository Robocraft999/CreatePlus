package com.robocraft999.createplus.lists;

import java.util.function.Supplier;

import com.robocraft999.createplus.CreatePlus;
import com.robocraft999.createplus.item.crafting.GHelmetCrafingRecipe;
import com.simibubi.create.foundation.utility.Lang;

import net.minecraft.world.Container;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.Registry;
import net.minecraftforge.event.RegistryEvent;

public enum RecipeTypeList {

	HELMET_CRAFTING(GHelmetCrafingRecipe.Serializer::new, RecipeType.CRAFTING);
	
	public RecipeSerializer<?> serializer;
	public final Supplier<RecipeSerializer<?>> supplier;
	public RecipeType<? extends Recipe<? extends Container>> type;

	RecipeTypeList(Supplier<RecipeSerializer<?>> supplier,RecipeType<? extends Recipe<? extends Container>> existingType) {
		this.supplier = supplier;
		this.type = existingType;
	}
	
	public static void register(RegistryEvent.Register<RecipeSerializer<?>> event) {
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
	
	private static <T extends Recipe<?>> RecipeType<T> customType(String id) {
		return Registry.register(Registry.RECIPE_TYPE, new ResourceLocation(CreatePlus.MODID, id), new RecipeType<T>() {
			public String toString() {
				return CreatePlus.MODID + ":" + id;
			}
		});
	}
}

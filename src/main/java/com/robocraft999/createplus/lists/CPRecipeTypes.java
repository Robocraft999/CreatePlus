package com.robocraft999.createplus.lists;

import com.robocraft999.createplus.CreatePlus;
import com.robocraft999.createplus.item.crafting.NBTCraftingRecipe;

import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class CPRecipeTypes {

	public static final DeferredRegister<RecipeSerializer<?>> RECIPE_REGISTER = DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, CreatePlus.MODID);

	public static final RegistryObject<RecipeSerializer<?>>
			CRAFTING_NBT = RECIPE_REGISTER.register("crafting_nbt", NBTCraftingRecipe.Serializer::new);

	public static void register(IEventBus bus) {
		RECIPE_REGISTER.register(bus);
	}
}

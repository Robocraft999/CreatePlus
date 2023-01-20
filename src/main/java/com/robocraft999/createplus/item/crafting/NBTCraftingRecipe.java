package com.robocraft999.createplus.item.crafting;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;

import com.google.gson.JsonObject;
import com.robocraft999.createplus.CreatePlus;
import com.robocraft999.createplus.registry.CPRecipeTypes;

import net.minecraft.world.item.crafting.*;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.core.NonNullList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;

import javax.annotation.Nonnull;

public record NBTCraftingRecipe(ShapedRecipe recipe) implements CraftingRecipe {

	@Override
	public boolean matches(@Nonnull CraftingContainer inv, @Nonnull Level worldIn) {
		return recipe().matches(inv, worldIn);
	}

	@Nonnull
	@Override
	public NonNullList<Ingredient> getIngredients() {
		return recipe.getIngredients();
	}

	@Nonnull
	@Override
	public ItemStack assemble(CraftingContainer inv) {
		HashMap<Enchantment, Integer> allEnchants = new HashMap<>();
		ItemStack nbtItemResult = ItemStack.EMPTY;
		for (int slot = 0; slot < inv.getContainerSize(); slot++) {
			ItemStack nbtItem = inv.getItem(slot).copy();
			if(nbtItem.isEmpty() || (!nbtItem.isEnchantable() && !nbtItem.isEnchanted()))continue;
			if(nbtItem.isEnchanted()) {
				allEnchants.putAll(EnchantmentHelper.getEnchantments(nbtItem));
			}
			if(nbtItemResult.isEmpty()) {
				nbtItemResult = new ItemStack(getResultItem().getItem());
			}
		}
		if(!allEnchants.isEmpty() || !nbtItemResult.isEmpty()){
			for (Entry<Enchantment, Integer> entry : allEnchants.entrySet()) {
				Enchantment enchantment = entry.getKey();
				if (!enchantment.isCurse() && EnchantmentHelper.getTagEnchantmentLevel(enchantment, nbtItemResult) == 0) {
					nbtItemResult.enchant(enchantment, entry.getValue());
				}
			}
		}
		return nbtItemResult;
	}

	@Nonnull
	@Override
	public ItemStack getResultItem() {
		return recipe().getResultItem();
	}

	@Nonnull
	@Override
	public ResourceLocation getId() {
		return recipe().getId();
	}

	@Nonnull
	@Override
	public RecipeSerializer<?> getSerializer() {
		return CPRecipeTypes.CRAFTING_NBT.get();
	}

	@Override
	public boolean canCraftInDimensions(int width, int height) {
		return recipe().canCraftInDimensions(width, height);
	}


	public static class Serializer implements RecipeSerializer<NBTCraftingRecipe> {

		@Nonnull
		@Override
		public NBTCraftingRecipe fromJson(@Nonnull ResourceLocation recipeId, @Nonnull JsonObject json) {
			ShapedRecipe recipe = RecipeSerializer.SHAPED_RECIPE.fromJson(recipeId, json);
			return new NBTCraftingRecipe(recipe);
		}

		@Override
		public NBTCraftingRecipe fromNetwork(@Nonnull ResourceLocation recipeId, @Nonnull FriendlyByteBuf buffer) {
			ShapedRecipe recipe = RecipeSerializer.SHAPED_RECIPE.fromNetwork(recipeId, buffer);
			return new NBTCraftingRecipe(recipe);

		}

		@Override
		public void toNetwork(@Nonnull FriendlyByteBuf buffer, NBTCraftingRecipe recipe) {
			RecipeSerializer.SHAPED_RECIPE.toNetwork(buffer, recipe.recipe());
		}

	}


}

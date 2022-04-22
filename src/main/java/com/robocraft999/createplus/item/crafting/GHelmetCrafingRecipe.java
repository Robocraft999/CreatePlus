package com.robocraft999.createplus.item.crafting;

import java.util.Map;
import java.util.Map.Entry;

import com.google.gson.JsonObject;
import com.robocraft999.createplus.lists.ItemList;
import com.robocraft999.createplus.lists.RecipeTypeList;

import com.simibubi.create.AllItems;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.core.NonNullList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.ForgeRegistryEntry;

import javax.annotation.Nonnull;

public class GHelmetCrafingRecipe implements CraftingRecipe{
	
	private final ShapedRecipe recipe;
	
	public GHelmetCrafingRecipe(ShapedRecipe recipe) {
		this.recipe = recipe;
	}

	@Override
	public boolean matches(@Nonnull CraftingContainer inv, @Nonnull Level worldIn) {
		//System.out.println("matches: "+getRecipe().matches(inv, worldIn));
		return getRecipe().matches(inv, worldIn);
	}

	@Nonnull
	@Override
	public NonNullList<Ingredient> getIngredients() {
		return recipe.getIngredients();
	}

	@Nonnull
	@Override
	public ItemStack assemble(CraftingContainer inv) {
		for (int slot = 0; slot < inv.getContainerSize(); slot++) {
			ItemStack helmet = inv.getItem(slot).copy();

			ItemStack goggleHelmet = ItemStack.EMPTY;
			if(helmet.is(Items.CHAINMAIL_HELMET))goggleHelmet = new ItemStack(ItemList.goggle_chainmail_helmet.get());
			if(helmet.is(Items.DIAMOND_HELMET))goggleHelmet = new ItemStack(ItemList.goggle_diamond_helmet.get());
			if(helmet.is(Items.GOLDEN_HELMET))goggleHelmet = new ItemStack(ItemList.goggle_golden_helmet.get());
			if(helmet.is(Items.IRON_HELMET))goggleHelmet = new ItemStack(ItemList.goggle_iron_helmet.get());
			if(helmet.is(Items.LEATHER_HELMET))goggleHelmet = new ItemStack(ItemList.goggle_leather_helmet.get());
			if(helmet.is(Items.TURTLE_HELMET))goggleHelmet = new ItemStack(ItemList.goggle_turtle_helmet.get());
			if(helmet.is(Items.NETHERITE_HELMET))goggleHelmet = new ItemStack(ItemList.goggle_netherite_helmet.get());
			if(helmet.is(AllItems.DIVING_HELMET.get()))goggleHelmet = new ItemStack(ItemList.goggle_diving_helmet.get());

			if (goggleHelmet.isEmpty())continue;
			Map<Enchantment, Integer> map;
			map = EnchantmentHelper.getEnchantments(helmet);
			//System.out.println("got enchantments "+map);
				
			for(Entry<Enchantment, Integer> entry : map.entrySet()) {
				Enchantment enchantment = entry.getKey();
				if (!enchantment.isCurse() || EnchantmentHelper.getItemEnchantmentLevel(enchantment, goggleHelmet) == 0) {
					goggleHelmet.enchant(enchantment, entry.getValue());
					//System.out.println("added enchantment");
				}
			}
			return goggleHelmet;
		}
		return ItemStack.EMPTY;
	}

	@Nonnull
	@Override
	public ItemStack getResultItem() {
		return getRecipe().getResultItem();
	}

	@Nonnull
	@Override
	public ResourceLocation getId() {
		return getRecipe().getId();
	}

	@Nonnull
	@Override
	public RecipeSerializer<?> getSerializer() {
		return RecipeTypeList.HELMET_CRAFTING.serializer;
	}
	
	@Override
	public boolean canCraftInDimensions(int width, int height) {
		return getRecipe().canCraftInDimensions(width, height);
	}

	public ShapedRecipe getRecipe() {
		return recipe;
	}
	public static class Serializer extends ForgeRegistryEntry<RecipeSerializer<?>> implements RecipeSerializer<GHelmetCrafingRecipe> {

		@Nonnull
		@Override
		public GHelmetCrafingRecipe fromJson(@Nonnull ResourceLocation recipeId, @Nonnull JsonObject json) {
			ShapedRecipe recipe = RecipeSerializer.SHAPED_RECIPE.fromJson(recipeId, json);
			
			return new GHelmetCrafingRecipe(recipe);
		}

		@Override
		public GHelmetCrafingRecipe fromNetwork(@Nonnull ResourceLocation recipeId, @Nonnull FriendlyByteBuf buffer) {
			ShapedRecipe recipe = RecipeSerializer.SHAPED_RECIPE.fromNetwork(recipeId, buffer);
			
			return new GHelmetCrafingRecipe(recipe);
			
		}

		@Override
		public void toNetwork(@Nonnull FriendlyByteBuf buffer, GHelmetCrafingRecipe recipe) {
			RecipeSerializer.SHAPED_RECIPE.toNetwork(buffer, recipe.getRecipe());
		}
		
	}
	

	

}

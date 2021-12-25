package com.robocraft999.createplus.item.crafting;


import java.util.Map;
import java.util.Map.Entry;

import com.google.common.collect.Maps;
import com.google.gson.JsonObject;
import com.robocraft999.createplus.lists.ItemList;
import com.robocraft999.createplus.lists.RecipeTypeList;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.ICraftingRecipe;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.ShapedRecipe;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.registries.ForgeRegistryEntry;

public class GHelmetCrafingRecipe implements ICraftingRecipe{
	
	private ShapedRecipe recipe;
	
	public GHelmetCrafingRecipe(ShapedRecipe recipe) {
		this.recipe = recipe;
	}

	@Override
	public boolean matches(CraftingInventory inv, World worldIn) {
		//System.out.println("matches: "+getRecipe().matches(inv, worldIn));
		return getRecipe().matches(inv, worldIn);
	}
	
	@Override
	public NonNullList<Ingredient> getIngredients() {
		return recipe.getIngredients();
	}

	@Override
	public ItemStack assemble(CraftingInventory inv) {
		//System.out.println("getCraftingResult");
		for (int slot = 0; slot < inv.getContainerSize(); slot++) {
			ItemStack helmet = inv.getItem(slot).copy();
			
			boolean chainmail = Items.CHAINMAIL_HELMET == helmet.getItem();
			boolean diamond = Items.DIAMOND_HELMET == helmet.getItem();
			boolean golden = Items.GOLDEN_HELMET == helmet.getItem();
			boolean iron = Items.IRON_HELMET == helmet.getItem();
			boolean leather = Items.LEATHER_HELMET == helmet.getItem();
			boolean turtle = Items.TURTLE_HELMET == helmet.getItem();
			boolean netherite = Items.NETHERITE_HELMET == helmet.getItem();
			
			boolean HelmetInGrid = chainmail || diamond || golden || iron || leather || turtle || netherite;
			
			ItemStack goggleHelmet = ItemStack.EMPTY;
			if(chainmail)goggleHelmet = new ItemStack(ItemList.goggle_chainmail_helmet);
			if(diamond)goggleHelmet = new ItemStack(ItemList.goggle_diamond_helmet);
			if(golden)goggleHelmet = new ItemStack(ItemList.goggle_golden_helmet);
			if(iron)goggleHelmet = new ItemStack(ItemList.goggle_iron_helmet);
			if(leather)goggleHelmet = new ItemStack(ItemList.goggle_leather_helmet);
			if(turtle)goggleHelmet = new ItemStack(ItemList.goggle_turtle_helmet);
			if(netherite)goggleHelmet = new ItemStack(ItemList.goggle_netherite_helmet);
			
			//System.out.println(HelmetInGrid);
			
			if (!(HelmetInGrid))continue;
				Map<Enchantment, Integer> map = Maps.newLinkedHashMap();
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

	@Override
	public ItemStack getResultItem() {
		ItemStack helmet = getRecipe().getResultItem();
		return helmet;
	}

	@Override
	public ResourceLocation getId() {
		return getRecipe().getId();
	}

	@Override
	public IRecipeSerializer<?> getSerializer() {
		return RecipeTypeList.HELMET_CRAFTING.serializer;
	}
	
	@Override
	public boolean canCraftInDimensions(int width, int height) {
		return getRecipe().canCraftInDimensions(width, height);
	}

	public ShapedRecipe getRecipe() {
		return recipe;
	}
	
	
	public static class Serializer extends ForgeRegistryEntry<IRecipeSerializer<?>> implements IRecipeSerializer<GHelmetCrafingRecipe> {

		@Override
		public GHelmetCrafingRecipe fromJson(ResourceLocation recipeId, JsonObject json) {
			ShapedRecipe recipe = IRecipeSerializer.SHAPED_RECIPE.fromJson(recipeId, json);
			
			return new GHelmetCrafingRecipe(recipe);
		}

		@Override
		public GHelmetCrafingRecipe fromNetwork(ResourceLocation recipeId, PacketBuffer buffer) {
			ShapedRecipe recipe = IRecipeSerializer.SHAPED_RECIPE.fromNetwork(recipeId, buffer);
			
			return new GHelmetCrafingRecipe(recipe);
			
		}

		@Override
		public void toNetwork(PacketBuffer buffer, GHelmetCrafingRecipe recipe) {
			IRecipeSerializer.SHAPED_RECIPE.toNetwork(buffer, recipe.getRecipe());
		}
		
	}
	

	

}

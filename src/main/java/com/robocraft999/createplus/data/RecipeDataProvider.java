package com.robocraft999.createplus.data;

import com.robocraft999.createplus.CreatePlus;
import com.robocraft999.createplus.registry.CPItems;
import com.simibubi.create.AllItems;
import com.simibubi.create.content.contraptions.components.crusher.CrushingRecipe;
import com.simibubi.create.content.contraptions.processing.ProcessingRecipeBuilder;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;

import java.util.function.Consumer;

public class RecipeDataProvider extends RecipeProvider {
    public RecipeDataProvider(DataGenerator gen) {
        super(gen);
    }

    @Override
    protected void buildCraftingRecipes(Consumer<FinishedRecipe> consumer) {
        helmetRecipe(CPItems.CHAINMAIL_GOGGLE_HELMET.get(), Items.CHAINMAIL_HELMET, consumer);
        helmetRecipe(CPItems.DIAMOND_GOGGLE_HELMET.get(), Items.DIAMOND_HELMET, consumer);
        helmetRecipe(CPItems.GOLDEN_GOGGLE_HELMET.get(), Items.GOLDEN_HELMET, consumer);
        helmetRecipe(CPItems.IRON_GOGGLE_HELMET.get(), Items.IRON_HELMET, consumer);
        helmetRecipe(CPItems.LEATHER_GOGGLE_HELMET.get(), Items.LEATHER_HELMET, consumer);
        helmetRecipe(CPItems.TURTLE_GOGGLE_HELMET.get(), Items.TURTLE_HELMET, consumer);
        helmetRecipe(CPItems.NETHERITE_GOGGLE_HELMET.get(), Items.NETHERITE_HELMET, consumer);
        helmetRecipe(CPItems.DIVING_GOGGLE_HELMET.get(), AllItems.DIVING_HELMET.get(), consumer);

        backtankRecipe(CPItems.CHAINMAIL_BACKTANK.get(), Items.CHAINMAIL_CHESTPLATE, consumer);
        backtankRecipe(CPItems.DIAMOND_BACKTANK.get(), Items.DIAMOND_CHESTPLATE, consumer);
        backtankRecipe(CPItems.GOLDEN_BACKTANK.get(), Items.GOLDEN_CHESTPLATE, consumer);
        backtankRecipe(CPItems.IRON_BACKTANK.get(), Items.IRON_CHESTPLATE, consumer);
        backtankRecipe(CPItems.LEATHER_BACKTANK.get(), Items.LEATHER_CHESTPLATE, consumer);
        backtankRecipe(CPItems.NETHERITE_BACKTANK.get(), Items.NETHERITE_CHESTPLATE, consumer);
    }

    private void helmetRecipe(ItemLike result, ItemLike helmet, Consumer<FinishedRecipe> writer){
        CreatePlusRecipeBuilder.shaped(result)
                .pattern("hg")
                .define('h', helmet)
                .define('g', AllItems.GOGGLES.get())
                .unlockedBy("has_helmet", has(helmet))
                .save(writer);
        crushingGoggle(result, helmet, writer);
    }

    private void backtankRecipe(ItemLike result, ItemLike chestplate, Consumer<FinishedRecipe> writer){
        CreatePlusRecipeBuilder.shaped(result)
                .pattern("cb")
                .define('c', chestplate)
                .define('b', AllItems.COPPER_BACKTANK.get())
                .unlockedBy("has_backtank", has(chestplate))
                .save(writer);
        crushing(result, chestplate, AllItems.COPPER_BACKTANK.get(), writer);
    }

    private void crushingGoggle(ItemLike input, ItemLike output, Consumer<FinishedRecipe> writer){
        crushing(input, output, AllItems.GOGGLES.get(), writer);
    }

    private void crushing(ItemLike input, ItemLike output1, ItemLike output2, Consumer<FinishedRecipe> writer){
        ProcessingRecipeBuilder<CrushingRecipe> crushingRecipeBuilder =
                new ProcessingRecipeBuilder<>(CrushingRecipe::new, new ResourceLocation(CreatePlus.MODID, output1.asItem().toString()));
        crushingRecipeBuilder.withItemIngredients(Ingredient.of(input))
                .output(output1)
                .output(output2)
                .duration(150)
                .build(writer);
    }

    /*private void mekModule(ItemLike module, ItemLike input, Consumer<FinishedRecipe> writer){
        //ConditionalRecipe.builder().addCondition(new ModLoadedCondition("mekanism")).addRecipe(writer.);
        ShapedRecipeBuilder.shaped(module)
                .pattern("A#A")
                .pattern("ABA")
                .pattern("HHH")
                .define('A', )
                .define('#', input)
                //.define('B', )
                //.define('H', )
                .save(writer);
    }*/
}

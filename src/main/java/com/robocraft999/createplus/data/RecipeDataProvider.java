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
    }

    private void helmetRecipe(ItemLike result, ItemLike helmet, Consumer<FinishedRecipe> writer){
        CreatePlusRecipeBuilder.shaped(result)
                .pattern("hg")
                .define('h', helmet)
                .define('g', AllItems.GOGGLES.get())
                .unlockedBy("has_helmet", has(helmet))
                .save(writer);
        crushing(result, helmet, writer);
    }

    private void crushing(ItemLike result, ItemLike input, Consumer<FinishedRecipe> writer){
        ProcessingRecipeBuilder<CrushingRecipe> crushingRecipeBuilder =
                new ProcessingRecipeBuilder<>(CrushingRecipe::new, new ResourceLocation(CreatePlus.MODID, input.asItem().toString()));
        crushingRecipeBuilder.withItemIngredients(Ingredient.of(result))
                .output(input)
                .output(AllItems.GOGGLES.get())
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

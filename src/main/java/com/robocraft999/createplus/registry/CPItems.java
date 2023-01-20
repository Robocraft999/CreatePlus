package com.robocraft999.createplus.registry;

import static com.robocraft999.createplus.CreatePlus.REGISTRATE;

import com.robocraft999.createplus.item.backtank.ArmoredBacktankBlock;
import com.robocraft999.createplus.item.backtank.BacktankArmor;
import com.robocraft999.createplus.item.backtank.DyableBacktankArmor;
import com.robocraft999.createplus.item.goggle.DivingGoggleArmor;
import com.robocraft999.createplus.item.goggle.DyableGoggleArmor;
import com.robocraft999.createplus.item.goggle.GoggleArmor;

import com.simibubi.create.AllTags;
import com.simibubi.create.content.AllSections;
import com.simibubi.create.content.contraptions.goggles.GogglesItem;
import com.simibubi.create.content.curiosities.armor.AllArmorMaterials;
import com.simibubi.create.foundation.data.AssetLookup;
import com.tterrag.registrate.util.entry.BlockEntry;
import com.tterrag.registrate.util.entry.ItemEntry;
import mekanism.api.MekanismAPI;
import mekanism.api.providers.IModuleDataProvider;
import net.minecraft.world.item.*;

import java.util.function.Supplier;

public class CPItems {

	static {
		REGISTRATE.creativeModeTab(() -> CreativeModeTab.TAB_COMBAT);
	}

	static {
		REGISTRATE.startSection(AllSections.KINETICS);
	}

	//-------------------Goggle Helmets-------------------

	public static final ItemEntry<GoggleArmor>
			CHAINMAIL_GOGGLE_HELMET = goggleHelmet("goggle_chainmail_helmet", ArmorMaterials.CHAIN),
			DIAMOND_GOGGLE_HELMET = goggleHelmet("goggle_diamond_helmet", ArmorMaterials.DIAMOND),
			GOLDEN_GOGGLE_HELMET = goggleHelmet("goggle_golden_helmet", ArmorMaterials.GOLD),
			IRON_GOGGLE_HELMET = goggleHelmet("goggle_iron_helmet", ArmorMaterials.IRON),
			TURTLE_GOGGLE_HELMET = goggleHelmet("goggle_turtle_helmet", ArmorMaterials.TURTLE),
			NETHERITE_GOGGLE_HELMET = goggleHelmet("goggle_netherite_helmet", ArmorMaterials.NETHERITE);

	public static final ItemEntry<? extends GoggleArmor>
			LEATHER_GOGGLE_HELMET = REGISTRATE
					.item("goggle_leather_helmet", p -> new DyableGoggleArmor(ArmorMaterials.LEATHER, p))
					.model((ctx, p) -> p.generated(ctx::getEntry, p.modLoc("item/goggle_leather_helmet"), p.modLoc("item/goggle_leather_helmet_overlay")))
					.color(() -> DyableGoggleArmor.DyableGoggleArmorColor::new)
					.register(),
			DIVING_GOGGLE_HELMET = REGISTRATE
					.item("goggle_diving_helmet", p -> new DivingGoggleArmor(AllArmorMaterials.COPPER, p))
					.register();

	//-------------------Backtanks-------------------
	public static final ItemEntry<BacktankArmor.ArmoredBacktankBlockItem>
			CHAINMAIL_BACKTANK_PLACEABLE = backtank_placable("chainmail_backtank", () -> CPBlocks.CHAINMAIL_BACKTANK_BLOCK),
			DIAMOND_BACKTANK_PLACABLE = backtank_placable("diamond_backtank", () -> CPBlocks.DIAMOND_BACKTANK_BLOCK),
			GOLDEN_BACKTANK_PLACEABLE = backtank_placable("golden_backtank", () -> CPBlocks.GOLDEN_BACKTANK_BLOCK),
			IRON_BACKTANK_PLACEABLE = backtank_placable("iron_backtank", () -> CPBlocks.IRON_BACKTANK_BLOCK),
			NETHERITE_BACKTANK_PLACABLE = backtank_placable("netherite_backtank", () -> CPBlocks.NETHERITE_BACKTANK_BLOCK),
			LEATHER_BACKTANK_PLACEABLE = backtank_placable("leather_backtank", () -> CPBlocks.LEATHER_BACKTANK_BLOCK);

	public static final ItemEntry<BacktankArmor>
			CHAINMAIL_BACKTANK = backtank("chainmail_backtank", ArmorMaterials.CHAIN),
			DIAMOND_BACKTANK = backtank("diamond_backtank", ArmorMaterials.DIAMOND),
			GOLDEN_BACKTANK = backtank("golden_backtank", ArmorMaterials.GOLD),
			IRON_BACKTANK = backtank("iron_backtank", ArmorMaterials.IRON),
			NETHERITE_BACKTANK = backtank("netherite_backtank", ArmorMaterials.NETHERITE);
	public static final ItemEntry<DyableBacktankArmor>
			LEATHER_BACKTANK = REGISTRATE
				.item("leather_backtank", p -> new DyableBacktankArmor(ArmorMaterials.LEATHER, "leather_backtank_placeable", p))
				.model(AssetLookup.customGenericItemModel("_", "item"))
				.color(() -> DyableGoggleArmor.DyableGoggleArmorColor::new)
				.tag(AllTags.AllItemTags.PRESSURIZED_AIR_SOURCES.tag)
				.register();


    public static final ItemEntry<Item>
			GOGGLE_UNIT = registerGoggleModule();

	public static void register() {
		GogglesItem.addIsWearingPredicate(GoggleArmor::isGoggleHelmet);
	}

	private static ItemEntry<Item> registerGoggleModule(){
		if (ModCompat.MEKANISM.isLoaded()) {
			return REGISTRATE
					.item("module_goggle_unit", p -> MekanismAPI.getModuleHelper().createModuleItem((IModuleDataProvider) CPModules.GOGGLE_MODULE::get, p.tab(CreativeModeTab.TAB_SEARCH)))
					.register();
		}
		return null;
	}

	private static ItemEntry<GoggleArmor> goggleHelmet(String name, ArmorMaterial material){
		return REGISTRATE.item(name, p -> new GoggleArmor(material, p)).register();
	}

    private static ItemEntry<BacktankArmor.ArmoredBacktankBlockItem> backtank_placable(String name, Supplier<BlockEntry<ArmoredBacktankBlock>> block){
        return REGISTRATE
                .item(name + "_placeable", p -> new BacktankArmor.ArmoredBacktankBlockItem(block.get().get(), p))
                .model((c, p) -> p.withExistingParent(c.getName(), p.mcLoc("item/barrier")))
                .register();
    }

	private static ItemEntry<BacktankArmor> backtank(String name, ArmorMaterial material){
		return REGISTRATE
				.item(name, p -> new BacktankArmor(material, name + "_placeable", p))
				.model(AssetLookup.customGenericItemModel("_", "item"))
				.tag(AllTags.AllItemTags.PRESSURIZED_AIR_SOURCES.tag)
				.register();
	}
}

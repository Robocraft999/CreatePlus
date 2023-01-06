package com.robocraft999.createplus.registry;

import static com.robocraft999.createplus.CreatePlus.REGISTRATE;

import static com.simibubi.create.foundation.data.TagGen.pickaxeOnly;

import com.robocraft999.createplus.item.backtank.ArmoredBacktankBlock;
import com.robocraft999.createplus.item.backtank.BacktankArmor;
import com.simibubi.create.content.AllSections;
import com.simibubi.create.foundation.data.AssetLookup;
import com.simibubi.create.foundation.data.SharedProperties;
import com.tterrag.registrate.util.entry.BlockEntry;
import com.tterrag.registrate.util.entry.ItemEntry;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.LootTable.Builder;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.CopyNameFunction;
import net.minecraft.world.level.storage.loot.functions.CopyNbtFunction;
import net.minecraft.world.level.storage.loot.predicates.ExplosionCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.providers.nbt.ContextNbtProvider;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;

public class CPBlocks {

    static {
        REGISTRATE.startSection(AllSections.CURIOSITIES);
    }

    public static final BlockEntry<ArmoredBacktankBlock>
            CHAINMAIL_BACKTANK_BLOCK = backtankBlock("chainmail_backtank", CPItems.CHAINMAIL_BACKTANK),
            DIAMOND_BACKTANK_BLOCK = backtankBlock("diamond_backtank", CPItems.DIAMOND_BACKTANK),
            GOLDEN_BACKTANK_BLOCK = backtankBlock("golden_backtank", CPItems.GOLDEN_BACKTANK),
            IRON_BACKTANK_BLOCK = backtankBlock("iron_backtank", CPItems.IRON_BACKTANK),
            TURTLE_BACKTANK_BLOCK = backtankBlock("turtle_backtank", CPItems.TURTLE_BACKTANK),
            NETHERITE_BACKTANK_BLOCK = backtankBlock("netherite_backtank", CPItems.NETHERITE_BACKTANK),
            LEATHER_BACKTANK_BLOCK = backtankBlock("leather_backtank", CPItems.LEATHER_BACKTANK);

    private static BlockEntry<ArmoredBacktankBlock> backtankBlock(String name, ItemEntry<? extends BacktankArmor> type){
        return REGISTRATE.block(name, p -> new ArmoredBacktankBlock(name, p))
                .initialProperties(SharedProperties::copperMetal)
                .blockstate((c, p) -> p.horizontalBlock(c.getEntry(), AssetLookup.partialBaseModel(c,p)))
                .transform(pickaxeOnly())
                .loot((lt, block) -> {
                    Builder builder = LootTable.lootTable();
                    LootItemCondition.Builder survivesExplosion = ExplosionCondition.survivesExplosion();
                    lt.add(block, builder.withPool(LootPool.lootPool()
                            .when(survivesExplosion)
                            .setRolls(ConstantValue.exactly(1))
                            .add(LootItem.lootTableItem(type.get())
                                    .apply(CopyNameFunction.copyName(CopyNameFunction.NameSource.BLOCK_ENTITY))
                                    .apply(CopyNbtFunction.copyData(ContextNbtProvider.BLOCK_ENTITY)
                                            .copy("Air", "Air"))
                                    .apply(CopyNbtFunction.copyData(ContextNbtProvider.BLOCK_ENTITY)
                                            .copy("Enchantments", "Enchantments")))));
                })
                .register();
    }

    public static void register() {}
}

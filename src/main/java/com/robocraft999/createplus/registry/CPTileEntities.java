package com.robocraft999.createplus.registry;

import com.simibubi.create.content.curiosities.armor.CopperBacktankInstance;
import com.simibubi.create.content.curiosities.armor.CopperBacktankRenderer;
import com.simibubi.create.content.curiosities.armor.CopperBacktankTileEntity;
import com.tterrag.registrate.util.entry.BlockEntityEntry;
import net.minecraftforge.registries.ForgeRegistries;

import static com.robocraft999.createplus.CreatePlus.REGISTRATE;

public class CPTileEntities {

    public static final BlockEntityEntry<CopperBacktankTileEntity>
            CHAINMAIL_BACKTANK_TILE = backtankTile("chainmail_backtank"),
            DIAMOND_BACKTANK_TILE = backtankTile("diamond_backtank"),
            GOLDEN_BACKTANK_TILE = backtankTile("golden_backtank"),
            IRON_BACKTANK_TILE = backtankTile("iron_backtank"),
            TURTLE_BACKTANK_TILE = backtankTile("turtle_backtank"),
            NETHERITE_BACKTANK_TILE = backtankTile("netherite_backtank"),
            LEATHER_BACKTANK_TILE = backtankTile("leather_backtank");


    private static BlockEntityEntry<CopperBacktankTileEntity> backtankTile(String name){
        return REGISTRATE
                .tileEntity(name, CopperBacktankTileEntity::new)
                .instance(() -> CopperBacktankInstance::new)
                .validBlocks(REGISTRATE.get(name, ForgeRegistries.Keys.BLOCKS))
                .renderer(() -> CopperBacktankRenderer::new)
                .register();
    }

    public static void register() {}

}

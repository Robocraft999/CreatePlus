package com.robocraft999.createplus.item.backtank;

import static com.robocraft999.createplus.CreatePlus.REGISTRATE;

import com.simibubi.create.content.curiosities.armor.CopperBacktankBlock;
import com.simibubi.create.content.curiosities.armor.CopperBacktankTileEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Optional;

public class ArmoredBacktankBlock extends CopperBacktankBlock {
    String typeId;

    public ArmoredBacktankBlock(String id, Properties properties) {
        super(properties);
        this.typeId = id;
    }

    @Override
    public ItemStack getCloneItemStack(BlockGetter p_185473_1_, BlockPos p_185473_2_, BlockState p_185473_3_) {
        ItemStack item = new ItemStack(REGISTRATE.get(typeId, ForgeRegistries.Keys.ITEMS).get());
        Optional<CopperBacktankTileEntity> tileEntityOptional = getTileEntityOptional(p_185473_1_, p_185473_2_);

        int air = tileEntityOptional.map(CopperBacktankTileEntity::getAirLevel)
                .orElse(0);
        CompoundTag tag = item.getOrCreateTag();
        tag.putInt("Air", air);

        ListTag enchants = tileEntityOptional.map(CopperBacktankTileEntity::getEnchantmentTag)
                .orElse(new ListTag());
        if (!enchants.isEmpty()) {
            ListTag enchantmentTagList = item.getEnchantmentTags();
            enchantmentTagList.addAll(enchants);
            tag.put("Enchantments", enchantmentTagList);
        }

        tileEntityOptional.map(CopperBacktankTileEntity::getCustomName).ifPresent(item::setHoverName);
        return item;
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return REGISTRATE.get(this.typeId, ForgeRegistries.Keys.BLOCK_ENTITY_TYPES).get().create(pos, state);
    }
}

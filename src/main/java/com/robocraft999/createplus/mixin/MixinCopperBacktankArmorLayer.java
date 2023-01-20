package com.robocraft999.createplus.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import com.robocraft999.createplus.item.backtank.BacktankArmor;
import com.simibubi.create.content.curiosities.armor.BackTankUtil;
import com.simibubi.create.content.curiosities.armor.CopperArmorItem;
import com.simibubi.create.content.curiosities.armor.CopperBacktankArmorLayer;
import com.simibubi.create.content.curiosities.armor.CopperBacktankBlock;
import com.tterrag.registrate.util.entry.BlockEntry;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.registries.ForgeRegistries;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Redirect;

import static com.robocraft999.createplus.CreatePlus.REGISTRATE;

@Mixin(CopperBacktankArmorLayer.class)
public class MixinCopperBacktankArmorLayer {

    @Redirect(
            method = "render",
            at = @At(
                    value = "INVOKE",
                    target = "Lcom/simibubi/create/content/curiosities/armor/CopperArmorItem;isWornBy(Lnet/minecraft/world/entity/Entity;)Z"
            ),
            remap = false
    )
    public boolean isWornByProxy(CopperArmorItem item, Entity entity){
        return item.isWornBy(entity) || BacktankArmor.isWornBy((LivingEntity) entity);
    }

    @ModifyVariable(method = "render", at=@At("STORE"), remap = false)
    protected BlockState onRenderedBlockstate(BlockState renderedState, PoseStack ms, MultiBufferSource buffer, int light, LivingEntity entity){
        if(!BackTankUtil.get(entity).is(Blocks.AIR.asItem())) {
            String s = BackTankUtil.get(entity).getDescriptionId().split("[.]")[2];
            return ((BlockEntry<? extends Block>)REGISTRATE.get(s, ForgeRegistries.Keys.BLOCKS)).getDefaultState().setValue(CopperBacktankBlock.HORIZONTAL_FACING, Direction.SOUTH);
        }
        return renderedState;
    }
}

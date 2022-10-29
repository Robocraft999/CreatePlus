package com.robocraft999.createplus.mixin;

import com.robocraft999.createplus.item.backtank.BacktankArmor;
import com.simibubi.create.content.curiosities.armor.CopperArmorItem;
import com.simibubi.create.content.curiosities.armor.CopperBacktankArmorLayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

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
}

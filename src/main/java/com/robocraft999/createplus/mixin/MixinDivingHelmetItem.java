package com.robocraft999.createplus.mixin;

import com.robocraft999.createplus.item.goggle.DivingGoggleArmor;
import com.simibubi.create.content.curiosities.armor.CopperArmorItem;
import com.simibubi.create.content.curiosities.armor.DivingHelmetItem;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(DivingHelmetItem.class)
public class MixinDivingHelmetItem {
    @Redirect(
            method = "breatheUnderwater",
            at = @At(
                    value = "INVOKE",
                    target = "Lcom/simibubi/create/content/curiosities/armor/CopperArmorItem;isWornBy(Lnet/minecraft/world/entity/Entity;)Z"
            ),
            remap = false
    )
    private static boolean isWornByProxy(CopperArmorItem item, Entity entity){
        return item.isWornBy(entity) || DivingGoggleArmor.isWornBy((LivingEntity) entity);
    }
}

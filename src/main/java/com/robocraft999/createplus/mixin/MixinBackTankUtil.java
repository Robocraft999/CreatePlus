package com.robocraft999.createplus.mixin;

import com.robocraft999.createplus.registry.ModCompat;
import com.simibubi.create.AllItems;
import com.simibubi.create.content.curiosities.armor.BackTankUtil;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import top.theillusivec4.curios.api.CuriosCapability;
import top.theillusivec4.curios.api.type.inventory.ICurioStacksHandler;

import java.util.ArrayList;
import java.util.Collection;

@Mixin(BackTankUtil.class)
public class MixinBackTankUtil {
    @Redirect(
            method = "get",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/LivingEntity;getArmorSlots()Ljava/lang/Iterable;"
            ),
            remap = false
    )
    private static Iterable<ItemStack> getArmorSlotsProxy(LivingEntity entity){
        ArrayList<ItemStack> armorSlots = new ArrayList<>((Collection<? extends ItemStack>) entity.getArmorSlots());
        if(ModCompat.CURIOS.isLoaded()){
            entity.getCapability(CuriosCapability.INVENTORY).ifPresent(handler -> {
                ICurioStacksHandler stacksHandler = handler.getCurios().get("body");
                if (stacksHandler != null) {
                    for (int i = 0; i < stacksHandler.getSlots(); i++) {
                        if (AllItems.COPPER_BACKTANK.isIn(stacksHandler.getStacks().getStackInSlot(i))) {
                            armorSlots.add(stacksHandler.getStacks().getStackInSlot(i));
                            break;
                        }
                    }
                }
            });
        }
        return armorSlots;
    }
}

package com.robocraft999.createplus.mixin;

import org.apache.commons.lang3.tuple.ImmutableTriple;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.simibubi.create.AllItems;
import com.simibubi.create.content.curiosities.armor.BackTankUtil;

import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.crafting.conditions.ModLoadedCondition;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandlerModifiable;
import top.theillusivec4.curios.api.CuriosApi;

@Mixin(BackTankUtil.class)
public class MixinBacktankUtil {
	
	
	@Inject(method = "get", at = @At("RETURN"), cancellable = true)
	private static void getMoreTanks(CallbackInfoReturnable<ItemStack> cir) {
		
		ItemStack backtank = cir.getReturnValue();
		
		if(backtank == ItemStack.EMPTY) {
			Minecraft mc = Minecraft.getInstance();
			ModLoadedCondition curiosloaded = new ModLoadedCondition("curios");
			if(curiosloaded.test()) {
				
				LazyOptional<IItemHandlerModifiable> test = CuriosApi.getCuriosHelper().getEquippedCurios(mc.player);
				IItemHandlerModifiable test2 = test.orElse(null);
				if(test2 != null) {
					for(int i = 0; i < test2.getSlots();i++) {
						ItemStack curiosSlot = test2.getStackInSlot(i);
						if(curiosSlot.getItem() == AllItems.COPPER_BACKTANK.get())backtank = curiosSlot;
						//if(backtank != ItemStack.EMPTY)System.out.println(backtank.getOrCreateTag().getFloat("Air"));
						//if(backtank != ItemStack.EMPTY)System.out.println(backtank.getOrCreateTag().getFloat("VisualBacktankAir"));
					}
				}
			}
		}
		
		cir.setReturnValue(backtank);
	}
	
	@Inject(method = "consumeAir", at = @At("HEAD"), cancellable = true)
	private static void onConsumeAir(CallbackInfo ci) {
		//System.out.println(ci.toString());
	}

}

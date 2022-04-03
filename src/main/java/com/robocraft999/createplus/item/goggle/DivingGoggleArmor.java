package com.robocraft999.createplus.item.goggle;

import com.robocraft999.createplus.lists.ItemList;
import com.simibubi.create.AllItems;
import com.simibubi.create.content.curiosities.armor.BackTankUtil;

import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.crafting.conditions.ModLoadedCondition;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.items.IItemHandlerModifiable;
import top.theillusivec4.curios.api.CuriosApi;

@EventBusSubscriber
public class DivingGoggleArmor extends GoggleArmor {

	public DivingGoggleArmor(ArmorMaterial material, Properties properties) {
		super(material, properties);
	}
	
	//@SubscribeEvent(priority = EventPriority.LOW)
	@SuppressWarnings("unused")
	public static void breatheUnderwater(LivingUpdateEvent event) {
		LivingEntity entity = event.getEntityLiving();
		Level world = entity.level;
		boolean second = world.getGameTime() % 20 == 0;
		boolean drowning = entity.getAirSupply() == 0;

		if (world.isClientSide)
			entity.getPersistentData()
				.remove("VisualBacktankAir");

		if (!AllItems.DIVING_HELMET.get()
			.isWornBy(entity))
			return;
		if (!entity.isEyeInFluid(FluidTags.WATER))
			return;
		if (entity instanceof Player && ((Player) entity).isCreative())
			return;

		ItemStack backtank = getBackTank(entity);
		if (backtank.isEmpty())
			return;
		if (!BackTankUtil.hasAirRemaining(backtank))
			return;

		if (drowning)
			entity.setAirSupply(10);

		if (world.isClientSide) {
			entity.getPersistentData()
				.putInt("VisualBacktankAir", (int) BackTankUtil.getAir(backtank));

		if (!second)
			return;

		entity.setAirSupply(Math.min(entity.getMaxAirSupply(), entity.getAirSupply() + 10));
		entity.addEffect(new MobEffectInstance(MobEffects.WATER_BREATHING, 30, 0, true, false, true));
		BackTankUtil.consumeAir(backtank, 1);
	}
	
	private static ItemStack getBackTank(LivingEntity entity) {
		ItemStack backtank;
		
		backtank = BackTankUtil.get(entity);
		
		if(backtank == ItemStack.EMPTY) {
			Minecraft mc = Minecraft.getInstance();
			ModLoadedCondition curiosloaded = new ModLoadedCondition("curios");
			if(curiosloaded.test()) {
				LazyOptional<IItemHandlerModifiable> test = CuriosApi.getCuriosHelper().getEquippedCurios(mc.player);
				IItemHandlerModifiable test2 = test.orElse(null);
				for(int i = 0; i < test2.getSlots();i++) {
					ItemStack curiosSlot = test2.getStackInSlot(i);
					if(curiosSlot.getItem() == AllItems.COPPER_BACKTANK.get())backtank = curiosSlot;
				}
			}
		}
		
		return backtank;
	}

}

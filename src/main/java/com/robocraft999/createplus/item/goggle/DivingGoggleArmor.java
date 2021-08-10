package com.robocraft999.createplus.item.goggle;

import com.robocraft999.createplus.ClientEvents;
import com.robocraft999.createplus.lists.ItemList;
import com.simibubi.create.AllItems;
import com.simibubi.create.content.curiosities.armor.BackTankUtil;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.IArmorMaterial;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.World;
import net.minecraftforge.common.crafting.conditions.ModLoadedCondition;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.items.IItemHandlerModifiable;
import top.theillusivec4.curios.api.CuriosApi;

@EventBusSubscriber
public class DivingGoggleArmor extends GoggleArmor {

	public DivingGoggleArmor(IArmorMaterial material, Properties properties) {
		super(material, properties);
	}
	
	@SubscribeEvent(priority = EventPriority.LOW)
	public static void breatheUnderwater(LivingUpdateEvent event) {
		LivingEntity entity = event.getEntityLiving();
		World world = entity.world;
		boolean second = world.getGameTime() % 20 == 0;
		boolean drowning = entity.getAir() == 0;

		if (world.isRemote)
			entity.getPersistentData()
				.remove("VisualBacktankAirCP");
		if (!(((GoggleArmor)ItemList.goggle_diving_helmet).isWornBy(entity)))
			return;
		if (!entity.areEyesInFluid(FluidTags.WATER))
			return;
		if (entity instanceof PlayerEntity && ((PlayerEntity) entity).isCreative())
			return;

		ItemStack backtank = getBackTank(entity);
		if (backtank.isEmpty())
			return;
		if (!BackTankUtil.hasAirRemaining(backtank))
			return;

		if (drowning)
			entity.setAir(10);

		if (world.isRemote) {
			entity.getPersistentData()
				.putInt("VisualBacktankAirCP", (int) BackTankUtil.getAir(backtank));
			System.out.println(entity.getPersistentData().getInt("VisualBacktankAirCP"));
			//ClientEvents.timeLeft = (int) BackTankUtil.getAir(backtank);
		}
		
		if (!second)
			return;

		entity.setAir(Math.min(entity.getMaxAir(), entity.getAir() + 10));
		entity.addPotionEffect(new EffectInstance(Effects.WATER_BREATHING, 30, 0, true, false, true));
		BackTankUtil.consumeAir(backtank, 1);
	}
	
	private static ItemStack getBackTank(LivingEntity entity) {
		ItemStack backtank = ItemStack.EMPTY;
		
		backtank = BackTankUtil.get(entity);
		
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
					}
				}
			}
		}
		
		return backtank;
	}

}

package com.robocraft999.createplus.item.goggle;

import com.simibubi.create.Create;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fml.common.Mod;
import org.jetbrains.annotations.Nullable;

@Mod.EventBusSubscriber
public class DivingGoggleArmor extends GoggleArmor {
	public static final ResourceLocation TEXTURE = Create.asResource("textures/models/armor/copper.png");
	private static final String TEXTURE_STRING = TEXTURE.toString();

	public DivingGoggleArmor(ArmorMaterial material, Properties properties) {
		super(material, properties);
	}

	/*
	private static ItemStack getBackTank(LivingEntity entity) {
		ItemStack backtank = BackTankUtil.get(entity);

		
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
	}*/

	public boolean isWornByLiving(LivingEntity entity){
		return entity.getItemBySlot(EquipmentSlot.HEAD).getItem() == this;
	}

	@Override
	public @Nullable String getArmorTexture(ItemStack stack, Entity entity, EquipmentSlot slot, String type) {
		return TEXTURE_STRING;
	}
}

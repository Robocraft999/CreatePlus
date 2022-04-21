package com.robocraft999.createplus;

import static net.minecraft.util.text.TextFormatting.GRAY;

import java.util.List;
import java.util.Map;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.robocraft999.createplus.item.goggle.GoggleOverlayRenderer;
import com.robocraft999.createplus.lists.ItemList;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.AllItems;
import com.simibubi.create.Create;
import com.simibubi.create.content.contraptions.base.IRotate;
import com.simibubi.create.content.contraptions.base.IRotate.SpeedLevel;
import com.simibubi.create.content.contraptions.base.IRotate.StressImpact;
import com.simibubi.create.content.contraptions.components.crank.ValveHandleBlock;
import com.simibubi.create.content.contraptions.components.flywheel.engine.EngineBlock;
import com.simibubi.create.foundation.config.AllConfigs;
import com.simibubi.create.foundation.config.CKinetics;
import com.simibubi.create.foundation.gui.GuiGameElement;
import com.simibubi.create.foundation.item.ItemDescription;
import com.simibubi.create.foundation.utility.ColorHelper;
import com.simibubi.create.foundation.utility.Lang;

import net.minecraft.block.Block;
import net.minecraft.client.MainWindow;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.client.renderer.IRenderTypeBuffer.Impl;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StringUtils;
import net.minecraft.util.text.IFormattableTextComponent;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.common.ForgeConfigSpec.ConfigValue;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber(value = Dist.CLIENT)
public class ClientEvents {
	
	private static final String itemPrefix = "item." + Create.ID;
	private static final String blockPrefix = "block." + Create.ID;
	public  static int timeLeft = 0;
	
	@SubscribeEvent(priority=EventPriority.LOW)
	public static void onRenderOverlay(RenderGameOverlayEvent.Post event) {
		MatrixStack ms = event.getMatrixStack();
		Impl buffers = Minecraft.getInstance()
			.getBufferBuilders()
			.getEntityVertexConsumers();
		int light = 0xF000F0;
		int overlay = OverlayTexture.DEFAULT_UV;
		float pt = event.getPartialTicks();

		if (event.getType() == ElementType.AIR) {
			//Minecraft.getInstance().player.getPersistentData().putInt("VisualBacktankAir",timeLeft);
			renderRemainingAirOverlay(ms, buffers, light, overlay, pt);
			//System.out.println(Minecraft.getInstance().player.getPersistentData().getInt("VisualBacktankAir"));
		}
		if (event.getType() != ElementType.HOTBAR)
			return;

		GoggleOverlayRenderer.renderOverlay(ms, buffers, light, overlay, pt);
	}
	
	public static void renderRemainingAirOverlay(MatrixStack ms, Impl buffers, int light, int overlay, float pt) {
		ClientPlayerEntity player = Minecraft.getInstance().player;
		if (player == null)
			return;
		if (player.isSpectator() || player.isCreative())
			return;
		if (!player.getPersistentData()
			.contains("VisualBacktankAirCP"))
			return;
		if (!player.areEyesInFluid(FluidTags.WATER))
			return;

		int timeLeft = player.getPersistentData()
			.getInt("VisualBacktankAirCP");

		ms.push();

		MainWindow window = Minecraft.getInstance()
			.getWindow();
		ms.translate(window.getScaledWidth() / 2 + 90, window.getScaledHeight() - 53, 0);

		ITextComponent text = new StringTextComponent(StringUtils.ticksToElapsedTime(timeLeft * 20));
		GuiGameElement.of(AllItems.COPPER_BACKTANK.asStack())
			.at(0, 0)
			.render(ms);
		int color = 0xFF_FFFFFF;
		if (timeLeft < 60 && timeLeft % 2 == 0) {
			color = ColorHelper.mixColors(0xFF_FF0000, color, Math.max(timeLeft / 60f, .25f));
		}
		Minecraft.getInstance().fontRenderer.drawWithShadow(ms, text, 16, 5, color);
		buffers.draw();

		ms.pop();
	}
	
	@SubscribeEvent(priority=EventPriority.LOW)
	public static void addToItemTooltip(ItemTooltipEvent event) {
		if (!AllConfigs.CLIENT.tooltips.get())
			return;
		if (event.getPlayer() == null)
			return;
		
		ItemStack stack = event.getItemStack();
		String translationKey = stack.getItem()
			.getTranslationKey(stack);
		
		if (!translationKey.startsWith(blockPrefix))
			return;
		
		List<ITextComponent> itemTooltip = event.getToolTip();
			
		Block itemblock = Block.getBlockFromItem(stack.getItem());
		if(itemblock == null || !(itemblock instanceof IRotate))return;
			
		boolean isEngine = itemblock instanceof EngineBlock;
		boolean isHandle = itemblock instanceof ValveHandleBlock;
		CKinetics config = AllConfigs.SERVER.kinetics;
		SpeedLevel minimumRequiredSpeedLevel = isEngine ? SpeedLevel.NONE : ((IRotate) itemblock).getMinimumRequiredSpeedLevel();
		boolean hasSpeedRequirement = minimumRequiredSpeedLevel != SpeedLevel.NONE;
		ResourceLocation id = itemblock.getRegistryName();
		Map<ResourceLocation, ConfigValue<Double>> impacts = config.stressValues.getImpacts();
		Map<ResourceLocation, ConfigValue<Double>> capacities = config.stressValues.getCapacities();
		boolean hasStressImpact = impacts.containsKey(id) && impacts.get(id)
			.get() > 0 && StressImpact.isEnabled();
		boolean hasStressCapacity = (isHandle || capacities.containsKey(id)) && StressImpact.isEnabled();
		ItemStack headSlot = event.getPlayer().getItemStackFromSlot(EquipmentSlotType.HEAD);
		boolean hasGlasses = ItemList.isGoggleHelmet(headSlot);
		ITextComponent rpmUnit = Lang.translate("generic.unit.rpm");
			
		if(!hasGlasses)return;
			
		if(hasSpeedRequirement) {
			int index = 1 + itemTooltip.indexOf(Lang.translate("tooltip.speedRequirement").formatted(GRAY));
			IFormattableTextComponent level = new StringTextComponent(ItemDescription.makeProgressBar(3, minimumRequiredSpeedLevel.ordinal()))
					.formatted(minimumRequiredSpeedLevel.getTextColor());
			level.append(String.valueOf(minimumRequiredSpeedLevel.getSpeedValue())).append(rpmUnit).append("+");
			if(index > 0) {
				itemTooltip.set(index, level);
			}
		}
		if(hasStressImpact && !(!isEngine && ((IRotate) itemblock).hideStressImpact())) {
			int index = 1 + itemTooltip.indexOf(Lang.translate("tooltip.stressImpact").formatted(GRAY));
			double impact = impacts.get(id).get();
			StressImpact impactId = impact >= config.highStressImpact.get() ? StressImpact.HIGH : (impact >= config.mediumStressImpact.get() ? StressImpact.MEDIUM : StressImpact.LOW);
			IFormattableTextComponent level = new StringTextComponent(ItemDescription.makeProgressBar(3, impactId.ordinal()))
					.formatted(impactId.getAbsoluteColor());
			level.append(impacts.get(id).get() + "x ").append(rpmUnit);
			if(index > 0) {
				itemTooltip.set(index, level);
			}
		}
		if(hasStressCapacity) {
			int index = 1 + itemTooltip.indexOf(Lang.translate("tooltip.capacityProvided").formatted(GRAY));
			double capacity = capacities.get(isHandle ? AllBlocks.HAND_CRANK.getId() : id).get();
			StressImpact impactId = capacity >= config.highCapacity.get() ? StressImpact.LOW : (capacity >= config.mediumCapacity.get() ? StressImpact.MEDIUM : StressImpact.HIGH);
			IFormattableTextComponent level = new StringTextComponent(ItemDescription.makeProgressBar(3, StressImpact.values().length - 2 - impactId.ordinal()))
					.formatted(impactId.getAbsoluteColor());
			level.append(capacity + "x ").append(rpmUnit);
			if(index > 0) {
				itemTooltip.set(index, level);
			}
		}
	}
	
}

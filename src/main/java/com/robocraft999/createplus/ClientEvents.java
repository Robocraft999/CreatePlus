package com.robocraft999.createplus;

import com.mojang.blaze3d.platform.Window;
import com.mojang.blaze3d.vertex.PoseStack;
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
import com.simibubi.create.foundation.block.BlockStressValues;
import com.simibubi.create.foundation.config.AllConfigs;
import com.simibubi.create.foundation.config.CKinetics;
import com.simibubi.create.foundation.gui.element.GuiGameElement;
import com.simibubi.create.foundation.item.ItemDescription;

import com.simibubi.create.foundation.utility.Lang;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.MultiBufferSource.BufferSource;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.FastColor;
import net.minecraft.util.StringUtil;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.common.ForgeConfigSpec.ConfigValue;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

import java.util.List;
import java.util.Map;

@EventBusSubscriber(Dist.CLIENT)
public class ClientEvents {
	
	private static final String itemPrefix = "item." + Create.ID;
	private static final String blockPrefix = "block." + Create.ID;
	public  static int timeLeft = 0;
	

	@SubscribeEvent(priority=EventPriority.LOW)
	public static void onRenderOverlay(RenderGameOverlayEvent.Post event) {
		PoseStack ms = event.getMatrixStack();
		BufferSource buffers = Minecraft.getInstance().renderBuffers().bufferSource();
		int light = 0xF000F0;
		int overlay = OverlayTexture.NO_OVERLAY;
		float pt = event.getPartialTicks();
	}
	
	public static void renderRemainingAirOverlay(PoseStack ms, BufferSource buffers, int light, int overlay, float pt) {
		LocalPlayer player = Minecraft.getInstance().player;
		if (player == null)
			return;
		if (player.isSpectator() || player.isCreative())
			return;
		if (!player.getPersistentData()
			.contains("VisualBacktankAir"))
			return;
		if (!player.isEyeInFluid(FluidTags.WATER))
			return;

		int timeLeft = player.getPersistentData()
			.getInt("VisualBacktankAir");

		ms.pushPose();


		Window window = Minecraft.getInstance().getWindow();
		ms.translate(window.getGuiScaledWidth() / 2f + 90, window.getGuiScaledHeight() - 53, 0);

		Component text = new TextComponent(StringUtil.formatTickDuration(timeLeft * 20));

		GuiGameElement.of(AllItems.COPPER_BACKTANK.asStack())
			.at(0, 0)
			.render(ms);
		int color = 0xFF_FFFFFF;
		if (timeLeft < 60 && timeLeft % 2 == 0) {
			color = FastColor.ARGB32.color(1, 0xFF_FF0000, color, (int) Math.max(timeLeft / 60f, .25f));
		}
		Minecraft.getInstance().font.drawShadow(ms, text, 16, 5, color);
		buffers.endBatch();

		ms.popPose();
	}
	
	@SubscribeEvent(priority=EventPriority.LOW)
	public static void addToItemTooltip(ItemTooltipEvent event) {
		if (!AllConfigs.CLIENT.tooltips.get())
			return;
		if (event.getPlayer() == null)
			return;
		
		ItemStack stack = event.getItemStack();
		String translationKey = stack.getItem()
			.getDescriptionId(stack);
		
		if (!translationKey.startsWith(blockPrefix))
			return;
		
		List<Component> itemTooltip = event.getToolTip();

		BlockItem item = (BlockItem) stack.getItem();

		ItemStack headSlot = event.getPlayer().getItemBySlot(EquipmentSlot.HEAD);
		boolean hasGlasses = ItemList.isGoggleHelmet(headSlot);
		Component rpmUnit = Lang.translate("generic.unit.rpm");
		CKinetics config = AllConfigs.SERVER.kinetics;

		if(!hasGlasses)return;

		SpeedLevel minimumRequiredSpeedLevel;
		boolean showStressImpact;
		if (!(item.getBlock() instanceof IRotate)) {
			minimumRequiredSpeedLevel = SpeedLevel.NONE;
			showStressImpact = true;
		} else {
			minimumRequiredSpeedLevel = ((IRotate) item.getBlock()).getMinimumRequiredSpeedLevel();
			showStressImpact = !((IRotate) item.getBlock()).hideStressImpact();
		}

		boolean hasSpeedRequirement = minimumRequiredSpeedLevel != SpeedLevel.NONE && minimumRequiredSpeedLevel != SpeedLevel.SLOW;
		boolean hasStressImpact = StressImpact.isEnabled() && showStressImpact && BlockStressValues.getImpact(item.getBlock()) > 0;
		boolean hasStressCapacity = StressImpact.isEnabled() && BlockStressValues.hasCapacity(item.getBlock());


		if(hasSpeedRequirement) {
			int index = 1 + itemTooltip.indexOf(Lang.translate("tooltip.speedRequirement").withStyle(ChatFormatting.GRAY));
			MutableComponent level = new TextComponent(ItemDescription.makeProgressBar(3, minimumRequiredSpeedLevel.ordinal())).withStyle(minimumRequiredSpeedLevel.getTextColor());
			level.append(String.valueOf(minimumRequiredSpeedLevel.getSpeedValue())).append(rpmUnit).append("+");
			if(index > 0) {
				itemTooltip.set(index, level);
			}
		}

		if(hasStressImpact/* && !(!isEngine && ((IRotate) itemblock).hideStressImpact())*/) {

			int index = 1 + itemTooltip.indexOf(Lang.translate("tooltip.stressImpact").withStyle(ChatFormatting.GRAY));
			double impact = BlockStressValues.getImpact(item.getBlock());
			StressImpact impactId = impact >= config.highStressImpact.get() ? StressImpact.HIGH : (impact >= config.mediumStressImpact.get() ? StressImpact.MEDIUM : StressImpact.LOW);
			MutableComponent level = new TextComponent(ItemDescription.makeProgressBar(3, impactId.ordinal() + 1)).withStyle(impactId.getAbsoluteColor());
			level.append(impact + "x ").append(rpmUnit);
			if(index > 0) {
				itemTooltip.set(index, level);
			}
		}
		if(hasStressCapacity) {
			int index = 1 + itemTooltip.indexOf(Lang.translate("tooltip.capacityProvided").withStyle(ChatFormatting.GRAY));
			double capacity = BlockStressValues.getCapacity(item.getBlock());
			StressImpact impactId = capacity >= config.highCapacity.get() ? StressImpact.LOW : (capacity >= config.mediumCapacity.get() ? StressImpact.MEDIUM : StressImpact.HIGH);
			MutableComponent level = new TextComponent(ItemDescription.makeProgressBar(3, StressImpact.values().length - 1 - impactId.ordinal())).withStyle(impactId.getAbsoluteColor());
			level.append(capacity + "x ").append(rpmUnit);
			if(index > 0) {
				itemTooltip.set(index, level);
			}
		}
	}
	
}

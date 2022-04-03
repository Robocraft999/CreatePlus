package com.robocraft999.createplus.item.goggle;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

import com.mojang.blaze3d.vertex.PoseStack;
import com.robocraft999.createplus.lists.ItemList;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.AllItems;
import com.simibubi.create.CreateClient;
import com.simibubi.create.content.contraptions.components.structureMovement.piston.MechanicalPistonBlock;
import com.simibubi.create.content.contraptions.components.structureMovement.piston.PistonExtensionPoleBlock;
import com.simibubi.create.content.contraptions.goggles.IHaveGoggleInformation;
import com.simibubi.create.content.contraptions.goggles.IHaveHoveringInformation;
import com.simibubi.create.foundation.config.AllConfigs;

import com.simibubi.create.foundation.gui.element.GuiGameElement;
import com.simibubi.create.foundation.tileEntity.behaviour.ValueBox;
import com.simibubi.create.foundation.utility.Color;
import com.simibubi.create.foundation.utility.Iterate;
import com.simibubi.create.foundation.utility.Lang;
import com.simibubi.create.foundation.utility.outliner.Outline;
import com.simibubi.create.foundation.utility.outliner.Outliner.OutlineEntry;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.client.Minecraft;

import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.core.Direction;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.network.chat.TextComponent;


public class GoggleOverlayRenderer {

	//private static final List<Supplier<Boolean>> customGogglePredicates = new LinkedList<>();
	//private static final Map<Object, OutlineEntry> outlines = CreateClient.OUTLINER.getOutlines();

	@SuppressWarnings("unused")
	public static void renderOverlay(PoseStack ms, MultiBufferSource buffer, int light, int overlay, float partialTicks) {
		HitResult objectMouseOver = Minecraft.getInstance().hitResult;

		if (!(objectMouseOver instanceof BlockHitResult result))
			return;

		for (OutlineEntry entry : outlines.values()) {
			if (!entry.isAlive())
				continue;
			Outline outline = entry.getOutline();
			if (outline instanceof ValueBox && !((ValueBox) outline).isPassive) 
				return;
		}

		Minecraft mc = Minecraft.getInstance();
		ClientLevel world = mc.level;
		BlockPos pos = result.getBlockPos();
		assert mc.player != null;
		ItemStack headSlot = mc.player.getItemBySlot(EquipmentSlot.HEAD);
		assert world != null;
		BlockEntity te = world.getBlockEntity(pos);

		boolean wearingGoggles = ItemList.isGoggleHelmet(headSlot);

		boolean hasGoggleInformation = te instanceof IHaveGoggleInformation;
		boolean hasHoveringInformation = te instanceof IHaveHoveringInformation;

		boolean goggleAddedInformation = false;
		boolean hoverAddedInformation = false;

		List<Component> tooltip = new ArrayList<>();
		/*
		if (hasGoggleInformation && wearingGoggles) {
			IHaveGoggleInformation gte = (IHaveGoggleInformation) te;
			goggleAddedInformation = gte.addToGoggleTooltip(tooltip, mc.player.isSneaking());
		}

		if (hasHoveringInformation) {
			if (!tooltip.isEmpty())
				tooltip.add(StringTextComponent.EMPTY);
			IHaveHoveringInformation hte = (IHaveHoveringInformation) te;
			hoverAddedInformation = hte.addToTooltip(tooltip, mc.player.isSneaking());

			if (goggleAddedInformation && !hoverAddedInformation)
				tooltip.remove(tooltip.size() - 1);
		}

		if (te instanceof IDisplayAssemblyExceptions) {
			boolean exceptionAdded = ((IDisplayAssemblyExceptions) te).addExceptionToTooltip(tooltip);
			if (exceptionAdded) {
				hasHoveringInformation = true;
				hoverAddedInformation = true;
			}
		}*//*
		
		if (hasGoggleInformation && wearingGoggles) {
			IHaveGoggleInformation gte = (IHaveGoggleInformation) te;
			if(hasHoveringInformation) {
				IHaveHoveringInformation hte = (IHaveHoveringInformation) te;
				hoverAddedInformation = hte.addToTooltip(tooltip, mc.player.isShiftKeyDown());
			}
			if(!tooltip.isEmpty())tooltip.add(TextComponent.EMPTY);
			goggleAddedInformation = gte.addToGoggleTooltip(tooltip, mc.player.isShiftKeyDown());
		}

		// break early if goggle or hover returned false when present
		if ((hasGoggleInformation && !goggleAddedInformation) && (hasHoveringInformation && !hoverAddedInformation))
			return;

		// check for piston poles if goggles are worn
		BlockState state = world.getBlockState(pos);
		if (wearingGoggles && AllBlocks.PISTON_EXTENSION_POLE.has(state)) {
			Direction[] directions = Iterate.directionsInAxis(state.getValue(PistonExtensionPoleBlock.FACING)
				.getAxis());
			int poles = 1;
			boolean pistonFound = false;
			for (Direction dir : directions) {
				int attachedPoles = PistonExtensionPoleBlock.PlacementHelper.get()
					.attachedPoles(world, pos, dir);
				poles += attachedPoles;
				pistonFound |= world.getBlockState(pos.relative(dir, attachedPoles + 1))
					.getBlock() instanceof MechanicalPistonBlock;
			}

			if (!pistonFound)
				return;
			if (!tooltip.isEmpty())
				tooltip.add(TextComponent.EMPTY);

			tooltip.add(IHaveGoggleInformation.componentSpacing.plainCopy()
				.append(Lang.translate("gui.goggles.pole_length"))
				.append(new TextComponent(" " + poles)));
		}

		if (tooltip.isEmpty())
			return;

		ms.pushPose();
		Screen tooltipScreen = new TooltipScreen(new TextComponent(""));
		tooltipScreen.init(mc, mc.getWindow().getGuiScaledWidth(),
			mc.getWindow().getGuiScaledHeight());

		int tooltipTextWidth = 0;
		for (FormattedText textLine : tooltip) {
			int textLineWidth = mc.font.width(textLine.getString());
			if (textLineWidth > tooltipTextWidth)
				tooltipTextWidth = textLineWidth;
		}

		int tooltipHeight = 8;
		if (tooltip.size() > 1) {
			tooltipHeight += (tooltip.size() - 1) * 10;
			tooltipHeight += 2; // gap between title lines and next lines
		}

		int posX = tooltipScreen.width / 2 + AllConfigs.CLIENT.overlayOffsetX.get();
		int posY = tooltipScreen.height / 2 + AllConfigs.CLIENT.overlayOffsetY.get();
		
		posX = Math.min(posX, tooltipScreen.width - tooltipTextWidth - 20);
		posY = Math.min(posY, tooltipScreen.height - tooltipHeight - 20);

		for (Component iTextComponent : tooltip) {
			tooltipScreen.renderTooltip(ms, iTextComponent, posX, posY);
		}

		ItemStack item = AllItems.GOGGLES.asStack();
		GuiGameElement.of(item)
			.at(posX + 10, posY - 16, 450)
			.render(ms);
		ms.popPose();
	}

	public static final class TooltipScreen extends Screen {
		public TooltipScreen(Component text) {
			super(text);
		}

		/*@Override
		public void init(Minecraft mc, int width, int height) {
			this.minecraft = mc;
			this.itemRenderer = mc.getItemRenderer();
			this.font = mc.font;
			this.width = width;
			this.height = height;
		}*/
	}



}

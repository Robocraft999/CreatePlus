package com.robocraft999.createplus.item.goggle;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


import com.mojang.blaze3d.matrix.MatrixStack;
import com.robocraft999.createplus.lists.ItemList;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.AllItems;
import com.simibubi.create.CreateClient;
import com.simibubi.create.content.contraptions.components.structureMovement.piston.MechanicalPistonBlock;
import com.simibubi.create.content.contraptions.components.structureMovement.piston.PistonExtensionPoleBlock;
import com.simibubi.create.content.contraptions.goggles.IHaveGoggleInformation;
import com.simibubi.create.content.contraptions.goggles.IHaveHoveringInformation;
import com.simibubi.create.foundation.config.AllConfigs;
import com.simibubi.create.foundation.gui.GuiGameElement;
import com.simibubi.create.foundation.tileEntity.behaviour.ValueBox;
import com.simibubi.create.foundation.utility.Iterate;
import com.simibubi.create.foundation.utility.Lang;
import com.simibubi.create.foundation.utility.outliner.Outline;
import com.simibubi.create.foundation.utility.outliner.Outliner.OutlineEntry;

/*import mekanism.common.content.gear.Modules;
import mekanism.common.item.gear.ItemMekaSuitArmor;
import mekanism.common.registries.MekanismItems;*/
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.IReorderingProcessor;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.common.crafting.conditions.ModLoadedCondition;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoader;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.config.ConfigFileTypeHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.registries.ForgeRegistry;
import top.theillusivec4.curios.api.CuriosApi;


@EventBusSubscriber(value = Dist.CLIENT)
public class GoggleOverlayRenderer {

	private static final Map<Object, OutlineEntry> outlines = CreateClient.outliner.getOutlines();

	@SubscribeEvent
	public static void lookingAtBlocksThroughGogglesShowsTooltip(RenderGameOverlayEvent.Post event) {
		MatrixStack ms = event.getMatrixStack();
		if (event.getType() != ElementType.TEXT)
			return;

		RayTraceResult objectMouseOver = Minecraft.getInstance().objectMouseOver;
		if (!(objectMouseOver instanceof BlockRayTraceResult))
			return;

		for (OutlineEntry entry : outlines.values()) {
			if (!entry.isAlive())
				continue;
			Outline outline = entry.getOutline();
			if (outline instanceof ValueBox && !((ValueBox) outline).isPassive) {
				return;
			}
		}

		BlockRayTraceResult result = (BlockRayTraceResult) objectMouseOver;
		Minecraft mc = Minecraft.getInstance();
		ClientWorld world = mc.world;
		BlockPos pos = result.getPos();
		ItemStack headSlot = mc.player.getItemStackFromSlot(EquipmentSlotType.HEAD);
		TileEntity te = world.getTileEntity(pos);

		boolean wearingGoggles = 
				   ItemList.goggle_chainmail_helmet == headSlot.getItem()
				|| ItemList.goggle_diamond_helmet == headSlot.getItem()
				|| ItemList.goggle_golden_helmet == headSlot.getItem()
				|| ItemList.goggle_iron_helmet == headSlot.getItem()
				|| ItemList.goggle_leather_helmet == headSlot.getItem()
				|| ItemList.goggle_turtle_helmet == headSlot.getItem()
				|| ItemList.goggle_netherite_helmet == headSlot.getItem();
		
		//if(AllItems.GOGGLES.isIn(headSlot))wearingGoggles = true;
		
		ModLoadedCondition curiosloaded = new ModLoadedCondition("curios");
		if(curiosloaded.test()) {
			LazyOptional<IItemHandlerModifiable> test = CuriosApi.getCuriosHelper().getEquippedCurios(mc.player);
			IItemHandlerModifiable test2 = test.orElse(null);
			if(test2 != null) {
				for(int i = 0; i < test2.getSlots();i++) {
					ItemStack curiosSlot = test2.getStackInSlot(i);
					if(curiosSlot.getItem() == AllItems.GOGGLES.get())wearingGoggles = true;
				}
			}
			
		
		}
		/*	
		ModLoadedCondition mekloaded = new ModLoadedCondition("mekanism");
		if(mekloaded.test()) {
			if(headSlot.getItem() == MekanismItems.MEKASUIT_HELMET.asItem()) {
				ItemMekaSuitArmor helmet = (ItemMekaSuitArmor)headSlot.getItem();
				if(helmet.hasModule(headSlot, Modules.VISION_ENHANCEMENT_UNIT)) {//TODO add own Module
					if(helmet.isModuleEnabled(headSlot, Modules.VISION_ENHANCEMENT_UNIT)) {//TODO add own Module
						wearingGoggles = true;
					}
				}
			}
		}*/

		boolean hasGoggleInformation = te instanceof IHaveGoggleInformation;
		boolean hasHoveringInformation = te instanceof IHaveHoveringInformation;

		boolean goggleAddedInformation = false;
		boolean hoverAddedInformation = false;

		List<ITextComponent> tooltip = new ArrayList<>();
		//List<IReorderingProcessor> tooltip = new ArrayList<>();

		if (hasGoggleInformation && wearingGoggles) {
			IHaveGoggleInformation gte = (IHaveGoggleInformation) te;
			if(hasHoveringInformation) {
				IHaveHoveringInformation hte = (IHaveHoveringInformation) te;
				hoverAddedInformation = hte.addToTooltip(tooltip, mc.player.isSneaking());
			}
			if(!tooltip.isEmpty())tooltip.add(StringTextComponent.EMPTY);
			goggleAddedInformation = gte.addToGoggleTooltip(tooltip, mc.player.isSneaking());
		}
		//System.out.println(goggleAddedInformation);
		
		if (hasHoveringInformation && false) {
			if (!tooltip.isEmpty())
				tooltip.add(StringTextComponent.EMPTY);
			IHaveHoveringInformation hte = (IHaveHoveringInformation) te;
			AllConfigs.CLIENT.enableOverstressedTooltip.set(true);
			hoverAddedInformation = hte.addToTooltip(tooltip, mc.player.isSneaking());
			AllConfigs.CLIENT.enableOverstressedTooltip.set(false);

			if (goggleAddedInformation && !hoverAddedInformation)
				tooltip.remove(tooltip.size() - 1);
		}
		
		
		//System.out.println(hoverAddedInformation);

		// break early if goggle or hover returned false when present
		if ((hasGoggleInformation && !goggleAddedInformation) && (hasHoveringInformation && !hoverAddedInformation)) {
			return;
		}

		// check for piston poles if goggles are worn
		BlockState state = world.getBlockState(pos);
		if (wearingGoggles && AllBlocks.PISTON_EXTENSION_POLE.has(state)) {
			Direction[] directions = Iterate.directionsInAxis(state.get(PistonExtensionPoleBlock.FACING)
				.getAxis());
			int poles = 1;
			boolean pistonFound = false;
			for (Direction dir : directions) {
				int attachedPoles = PistonExtensionPoleBlock.PlacementHelper.get().attachedPoles(world, pos, dir);
				poles += attachedPoles;
				pistonFound |= world.getBlockState(pos.offset(dir, attachedPoles + 1))
					.getBlock() instanceof MechanicalPistonBlock;
			}

			if (!pistonFound)
				return;
			if (!tooltip.isEmpty())
				tooltip.add(StringTextComponent.EMPTY);

			tooltip.add(IHaveGoggleInformation.componentSpacing.copyRaw()
				.append(Lang.translate("gui.goggles.pole_length"))
				.append(new StringTextComponent(" " + poles)));
		}

		if (tooltip.isEmpty())
			return;

		ms.push();
		Screen tooltipScreen = new TooltipScreen(null);
		tooltipScreen.init(mc, mc.getMainWindow()
			.getScaledWidth(),
			mc.getMainWindow()
				.getScaledHeight());
		int posX = tooltipScreen.width / 2 + AllConfigs.CLIENT.overlayOffsetX.get();
		int posY = tooltipScreen.height / 2 + AllConfigs.CLIENT.overlayOffsetY.get();
		// tooltipScreen.renderTooltip(tooltip, tooltipScreen.width / 2,
		// tooltipScreen.height / 2);
		////tooltipScreen.renderTooltip(ms, tooltip, posX, posY);
		//tooltipScreen.renderTooltip(ms, tooltip, posX, posY);
		//System.out.println(tooltip);
		tooltipScreen.func_243308_b(ms, tooltip, posX, posY);
		//tooltipScreen.renderWrappedToolTip(ms, tooltip, posX, posY, Minecraft.getInstance().fontRenderer);
		
		ItemStack item = AllItems.GOGGLES.asStack();
		// GuiGameElement.of(item).at(tooltipScreen.width / 2 + 10, tooltipScreen.height
		// / 2 - 16).render();
		GuiGameElement.of(item)
			.atLocal(posX + 10, posY, 450)
			.render(ms);
		ms.pop();
	}

	private static final class TooltipScreen extends Screen {
		private TooltipScreen(ITextComponent p_i51108_1_) {
			super(p_i51108_1_);
		}
		
		

		@Override
		public void init(Minecraft mc, int width, int height) {
			//this.client = mc;
			this.minecraft = mc;
			this.itemRenderer = mc.getItemRenderer();
			//this.textRenderer = mc.fontRenderer;
			this.font = mc.fontRenderer;
			this.width = width;
			this.height = height;
			
		}
	}

}
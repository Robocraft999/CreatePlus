package com.robocraft999.createplus;

import java.util.ArrayList;
import java.util.List;

import com.robocraft999.createplus.lists.ItemList;
import com.simibubi.create.Create;
import com.simibubi.create.content.contraptions.base.IRotate;
import com.simibubi.create.content.contraptions.base.IRotate.SpeedLevel;
import com.simibubi.create.content.contraptions.components.flywheel.engine.EngineBlock;
import com.simibubi.create.foundation.item.ItemDescription;
import com.simibubi.create.foundation.item.TooltipHelper;
import com.simibubi.create.foundation.utility.Lang;

import net.minecraft.block.AirBlock;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.IFormattableTextComponent;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import static net.minecraft.util.text.TextFormatting.GRAY;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber(value = Dist.CLIENT)
public class ClientEvents {
	
	//private static final String itemPrefix = "item." + Create.ID;
	private static final String blockPrefix = "block." + Create.ID;

	//@SubscribeEvent
	public static void addToItemTooltip(ItemTooltipEvent event){
		
			//System.out.println(TooltipHelper.cachedTooltips);
			ItemStack stack = event.getItemStack();
			String translationKey = stack.getItem()
					.getTranslationKey(stack);
			if (/*!translationKey.startsWith(itemPrefix) && */!translationKey.startsWith(blockPrefix))
				return;
			if(stack == null)return;
			if (TooltipHelper.hasTooltip(stack, event.getPlayer())) {
				List<ITextComponent> itemTooltip = event.getToolTip();
				List<ITextComponent> toolTip = new ArrayList<>();
				toolTip.add(itemTooltip.remove(0));
				//TooltipHelper.getTooltip(stack).addInformation(toolTip);
				
				Block itemblock = Block.getBlockFromItem(stack.getItem());
				if(itemblock == null || itemblock instanceof AirBlock)return;
				boolean isEngine = itemblock instanceof EngineBlock;
				//System.out.println("isnengine: "+isEngine);
				SpeedLevel minimumRequiredSpeedLevel = isEngine ? SpeedLevel.NONE : ((IRotate) itemblock).getMinimumRequiredSpeedLevel();
				int speedReqindex = 1 + itemTooltip.indexOf(Lang.translate("tooltip.speedRequirement").mergeStyle(GRAY));
				System.out.println(itemTooltip);
				//if(speedReqindex == 0)return;
				System.out.println("index: "+speedReqindex);
				
				StringTextComponent levelline = null;
				if(ItemList.goggle_chainmail_helmet == event.getPlayer().getItemStackFromSlot(EquipmentSlotType.HEAD).getItem()) {
					//System.out.println("helm in slot");
					ITextComponent rpmUnit = Lang.translate("generic.unit.rpm");
					IFormattableTextComponent level = null;
					levelline = (StringTextComponent) itemTooltip.get(speedReqindex);
					level = new StringTextComponent(" (").appendString(""+minimumRequiredSpeedLevel.getSpeedValue()).append(rpmUnit).appendString("+)");
					if(!levelline.getString().contains(level.getString())) {
						levelline.append(level);//level.appendString(" (" + minimumRequiredSpeedLevel.getSpeedValue()).append(rpmUnit).appendString("+)");
					}
					if(speedReqindex > 0) {
						toolTip.set(speedReqindex, levelline);
						itemTooltip.set(speedReqindex, levelline);
					}
					//toolTip.add(new StringTextComponent("test"));
				}
				//if(level != null)
				//itemTooltip.addAll(0, toolTip);
			}
			//: {block.create.crushing_wheel.tooltip=com.simibubi.create.foundation.item.ItemDescription@62e099d7}
	}
	
}

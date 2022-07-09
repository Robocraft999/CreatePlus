package com.robocraft999.createplus;

import com.mojang.blaze3d.platform.Window;
import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.AllItems;
import com.simibubi.create.Create;
import com.simibubi.create.foundation.gui.element.GuiGameElement;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.MultiBufferSource.BufferSource;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.FastColor;
import net.minecraft.util.StringUtil;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

import java.util.List;

@EventBusSubscriber(Dist.CLIENT)
public class ClientEvents {
	
	private static final String itemPrefix = "item." + Create.ID;
	private static final String blockPrefix = "block." + Create.ID;
	public  static int timeLeft = 0;
	
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
}

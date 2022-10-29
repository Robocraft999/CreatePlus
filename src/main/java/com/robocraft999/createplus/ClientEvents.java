package com.robocraft999.createplus;

import com.robocraft999.createplus.item.goggle.GoggleArmorLayer;
import com.simibubi.create.Create;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber(Dist.CLIENT)
public class ClientEvents {
	
	private static final String itemPrefix = "item." + Create.ID;
	private static final String blockPrefix = "block." + Create.ID;

	@EventBusSubscriber(value = Dist.CLIENT, bus = EventBusSubscriber.Bus.MOD)
	public static class ModBusEvents {

		@SubscribeEvent
		public static void addEntityRendererLayers(EntityRenderersEvent.AddLayers event){
			EntityRenderDispatcher dispatcher = Minecraft.getInstance().getEntityRenderDispatcher();
			GoggleArmorLayer.registerOnAll(dispatcher);
		}

	}
}

package com.robocraft999.createplus.item.goggle;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import com.robocraft999.createplus.CPConfig;
import com.simibubi.create.AllItems;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import top.theillusivec4.curios.api.CuriosCapability;
import top.theillusivec4.curios.api.type.inventory.ICurioStacksHandler;

import java.util.concurrent.atomic.AtomicBoolean;

public class GoggleArmorLayer<T extends LivingEntity, M extends EntityModel<T>> extends RenderLayer<T, M> {

    public GoggleArmorLayer(RenderLayerParent<T, M> renderer) {
        super(renderer);
    }

    @Override
    public void render(PoseStack ms, MultiBufferSource buffer, int light, LivingEntity entity, float yaw, float pitch, float pt, float p_117356_, float p_117357_, float p_117358_) {
        if (entity.getPose() == Pose.SLEEPING)
            return;
        if (!GoggleArmor.isWornBy(entity) || isWearingGoggleInCurio(entity))
            return;

        M entityModel = getParentModel();
        if (!(entityModel instanceof HumanoidModel))
            return;

        HumanoidModel<?> model = (HumanoidModel<?>) entityModel;
        ItemStack stack = new ItemStack(AllItems.GOGGLES.get());

        // Translate and rotate with our head
        ms.pushPose();
        ms.translate(model.head.x / 16.0, model.head.y / 16.0, model.head.z / 16.0);
        ms.mulPose(Vector3f.YP.rotation(model.head.yRot));
        ms.mulPose(Vector3f.XP.rotation(model.head.xRot));

        // Translate and scale to our head
        ms.translate(0, -0.25, -0.05);
        ms.mulPose(Vector3f.ZP.rotationDegrees(180.0f));
        ms.scale(0.625f, 0.625f, 0.625f);

        if(CPConfig.CLIENT.moveGoggleToEyes.get()) {
            ms.mulPose(Vector3f.ZP.rotationDegrees(180.0f));
            ms.translate(0, -0.25, 0);
        }

        // Render
        Minecraft.getInstance().getItemRenderer().renderStatic(stack, ItemTransforms.TransformType.HEAD, light, OverlayTexture.NO_OVERLAY, ms, buffer, 0);
        ms.popPose();
    }

    public static void registerOnAll(EntityRenderDispatcher renderManager) {
        for (EntityRenderer<? extends Player> renderer : renderManager.getSkinMap().values())
            registerOn(renderer);
        for (EntityRenderer<?> renderer : renderManager.renderers.values())
            registerOn(renderer);
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    public static void registerOn(EntityRenderer<?> entityRenderer) {
        if (!(entityRenderer instanceof LivingEntityRenderer<?, ?> livingRenderer))
            return;
        if (!(livingRenderer.getModel() instanceof HumanoidModel))
            return;
        GoggleArmorLayer<?, ?> layer = new GoggleArmorLayer<>(livingRenderer);
        livingRenderer.addLayer((GoggleArmorLayer) layer);
    }

    private static boolean isWearingGoggleInCurio(LivingEntity entity){
        AtomicBoolean hasGoggles = new AtomicBoolean(false);
        entity.getCapability(CuriosCapability.INVENTORY).ifPresent(handler -> {
            ICurioStacksHandler stacksHandler = handler.getCurios().get("head");
            if(stacksHandler != null) hasGoggles.set(stacksHandler.getStacks().getStackInSlot(0).getItem() == AllItems.GOGGLES.get());
        });
        return hasGoggles.get();
    }
}

package com.titammods.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.titammods.block.BasinBlockEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.client.extensions.common.IClientFluidTypeExtensions;
import net.neoforged.neoforge.fluids.FluidStack;
import org.joml.Matrix4f;

public class BasinRenderer implements BlockEntityRenderer<BasinBlockEntity> {

    private static final float OUTPUT_SCALE = 12.0f / 16.0f;
    private static final float CAST_SCALE   = 11.95f / 16.0f;

    public BasinRenderer(BlockEntityRendererProvider.Context context) {}

    @Override
    public void render(BasinBlockEntity entity, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, int packedOverlay) {

        ItemStack output = entity.inventory.getStackInSlot(BasinBlockEntity.OUTPUT);

        if (!output.isEmpty()) {
            renderItem(entity, output, OUTPUT_SCALE, poseStack, bufferSource, packedLight, packedOverlay);
            return;
        }

        FluidStack fluidStack = entity.tank.getFluid();
        if (!fluidStack.isEmpty() && entity.renderTimer <= 0) {
            renderFluid(entity, fluidStack, poseStack, bufferSource, packedLight);
        }

        ItemStack cast = entity.inventory.getStackInSlot(BasinBlockEntity.CAST);
        if (!cast.isEmpty()) {
            renderItem(entity, cast, CAST_SCALE, poseStack, bufferSource, packedLight, packedOverlay);
        }
    }

    private static void renderItem(BasinBlockEntity entity, ItemStack stack, float scale, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
        poseStack.pushPose();
        poseStack.translate(0.5D, 0.625D, 0.5D);
        poseStack.scale(scale, scale, scale);
        Minecraft.getInstance().getItemRenderer().renderStatic(
                stack, ItemDisplayContext.NONE, packedLight, packedOverlay, poseStack, bufferSource, entity.getLevel(), 0
        );
        poseStack.popPose();
    }

    private void renderFluid(BasinBlockEntity entity, FluidStack fluidStack, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {

        IClientFluidTypeExtensions clientFluid = IClientFluidTypeExtensions.of(fluidStack.getFluid());
        TextureAtlasSprite sprite = Minecraft.getInstance().getTextureAtlas(InventoryMenu.BLOCK_ATLAS).apply(clientFluid.getStillTexture(fluidStack));

        int recipeAmount = entity.fillTarget > 0 ? entity.fillTarget : entity.tank.getCapacity();
        float fillPercentage = Math.min(1f, (float) fluidStack.getAmount() / recipeAmount);

        float minX = 0.126f, maxX = 0.874f;
        float minZ = 0.126f, maxZ = 0.874f;
        float minY = 0.251f;
        float maxY = 0.95f;
        float height = minY + (fillPercentage * (maxY - minY));

        int color = clientFluid.getTintColor(fluidStack);
        float a = ((color >> 24) & 0xFF) / 255f;
        float r = ((color >> 16) & 0xFF) / 255f;
        float g = ((color >> 8) & 0xFF) / 255f;
        float b = (color & 0xFF) / 255f;

        int fluidLuminosity = fluidStack.getFluid().getFluidType().getLightLevel(fluidStack);
        int blockLight = Math.max(packedLight & 0xFFFF, fluidLuminosity << 4);
        int skyLight = (packedLight >> 16) & 0xFFFF;
        int light = blockLight | (skyLight << 16);

        VertexConsumer builder = bufferSource.getBuffer(RenderType.translucent());
        Matrix4f matrix = poseStack.last().pose();

        renderCuboid(builder, matrix, minX, minY, minZ, maxX, height, maxZ, sprite, r, g, b, a, light);
    }

    private void renderCuboid(VertexConsumer builder, Matrix4f matrix, float minX, float minY, float minZ, float maxX, float maxY, float maxZ, TextureAtlasSprite sprite, float r, float g, float b, float a, int light) {
        addVertex(builder, matrix, minX, maxY, maxZ, u(sprite, minX), v(sprite, maxZ), r, g, b, a, light, 0, 1, 0);
        addVertex(builder, matrix, maxX, maxY, maxZ, u(sprite, maxX), v(sprite, maxZ), r, g, b, a, light, 0, 1, 0);
        addVertex(builder, matrix, maxX, maxY, minZ, u(sprite, maxX), v(sprite, minZ), r, g, b, a, light, 0, 1, 0);
        addVertex(builder, matrix, minX, maxY, minZ, u(sprite, minX), v(sprite, minZ), r, g, b, a, light, 0, 1, 0);
        addVertex(builder, matrix, minX, minY, minZ, u(sprite, minX), v(sprite, minZ), r, g, b, a, light, 0, -1, 0);
        addVertex(builder, matrix, maxX, minY, minZ, u(sprite, maxX), v(sprite, minZ), r, g, b, a, light, 0, -1, 0);
        addVertex(builder, matrix, maxX, minY, maxZ, u(sprite, maxX), v(sprite, maxZ), r, g, b, a, light, 0, -1, 0);
        addVertex(builder, matrix, minX, minY, maxZ, u(sprite, minX), v(sprite, maxZ), r, g, b, a, light, 0, -1, 0);
        addVertex(builder, matrix, maxX, minY, minZ, u(sprite, maxX), v(sprite, 1-minY), r, g, b, a, light, 0, 0, -1);
        addVertex(builder, matrix, minX, minY, minZ, u(sprite, minX), v(sprite, 1-minY), r, g, b, a, light, 0, 0, -1);
        addVertex(builder, matrix, minX, maxY, minZ, u(sprite, minX), v(sprite, 1-maxY), r, g, b, a, light, 0, 0, -1);
        addVertex(builder, matrix, maxX, maxY, minZ, u(sprite, maxX), v(sprite, 1-maxY), r, g, b, a, light, 0, 0, -1);
        addVertex(builder, matrix, minX, minY, maxZ, u(sprite, minX), v(sprite, 1-minY), r, g, b, a, light, 0, 0, 1);
        addVertex(builder, matrix, maxX, minY, maxZ, u(sprite, maxX), v(sprite, 1-minY), r, g, b, a, light, 0, 0, 1);
        addVertex(builder, matrix, maxX, maxY, maxZ, u(sprite, maxX), v(sprite, 1-maxY), r, g, b, a, light, 0, 0, 1);
        addVertex(builder, matrix, minX, maxY, maxZ, u(sprite, minX), v(sprite, 1-maxY), r, g, b, a, light, 0, 0, 1);
        addVertex(builder, matrix, minX, minY, minZ, u(sprite, minZ), v(sprite, 1-minY), r, g, b, a, light, -1, 0, 0);
        addVertex(builder, matrix, minX, minY, maxZ, u(sprite, maxZ), v(sprite, 1-minY), r, g, b, a, light, -1, 0, 0);
        addVertex(builder, matrix, minX, maxY, maxZ, u(sprite, maxZ), v(sprite, 1-maxY), r, g, b, a, light, -1, 0, 0);
        addVertex(builder, matrix, minX, maxY, minZ, u(sprite, minZ), v(sprite, 1-maxY), r, g, b, a, light, -1, 0, 0);
        addVertex(builder, matrix, maxX, minY, maxZ, u(sprite, maxZ), v(sprite, 1-minY), r, g, b, a, light, 1, 0, 0);
        addVertex(builder, matrix, maxX, minY, minZ, u(sprite, minZ), v(sprite, 1-minY), r, g, b, a, light, 1, 0, 0);
        addVertex(builder, matrix, maxX, maxY, minZ, u(sprite, minZ), v(sprite, 1-maxY), r, g, b, a, light, 1, 0, 0);
        addVertex(builder, matrix, maxX, maxY, maxZ, u(sprite, maxZ), v(sprite, 1-maxY), r, g, b, a, light, 1, 0, 0);
    }

    private float u(TextureAtlasSprite s, float c) { return s.getU0() + (s.getU1() - s.getU0()) * c; }
    private float v(TextureAtlasSprite s, float c) { return s.getV0() + (s.getV1() - s.getV0()) * c; }
    private void addVertex(VertexConsumer b, Matrix4f m, float x, float y, float z, float u, float v, float r, float g, float bl, float a, int l, float nx, float ny, float nz) {
        b.addVertex(m, x, y, z).setColor(r, g, bl, a).setUv(u, v).setLight(l).setNormal(nx, ny, nz);
    }
}
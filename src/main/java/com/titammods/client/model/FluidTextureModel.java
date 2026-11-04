package com.titammods.client.model;

import com.titammods.TitamMods;
import com.titammods.setup.ModBlocks;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.BlockModelShaper;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.client.Minecraft;
import net.neoforged.neoforge.client.event.ModelEvent;
import net.neoforged.neoforge.client.extensions.common.IClientFluidTypeExtensions;
import net.neoforged.neoforge.client.model.BakedModelWrapper;
import net.neoforged.neoforge.client.model.IQuadTransformer;
import net.neoforged.neoforge.client.model.QuadTransformers;
import net.neoforged.neoforge.client.model.data.ModelData;
import net.neoforged.neoforge.fluids.FluidStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public final class FluidTextureModel {

    private static final ResourceLocation FLUID_PLACEHOLDER = ResourceLocation.fromNamespaceAndPath(TitamMods.MODID, "block/black");

    private FluidTextureModel() {}

    public static void onModifyBakingResult(ModelEvent.ModifyBakingResult event) {
        Map<ModelResourceLocation, BakedModel> models = event.getModels();
        wrap(models, ModBlocks.SMELTERY_CONTROLLER.get());
        wrap(models, ModBlocks.SEARED_DRAIN.get());
        wrap(models, ModBlocks.SEARED_CHUTE.get());
    }

    private static void wrap(Map<ModelResourceLocation, BakedModel> models, Block block) {
        Property<?> property = block.getStateDefinition().getProperty("in_structure");
        if (!(property instanceof BooleanProperty inStructure)) return;

        for (BlockState state : block.getStateDefinition().getPossibleStates()) {
            if (!state.getValue(inStructure)) continue;
            ModelResourceLocation location = BlockModelShaper.stateToModelLocation(state);
            BakedModel model = models.get(location);
            if (model != null && !(model instanceof Baked)) {
                models.put(location, new Baked(model));
            }
        }
    }

    private record CacheKey(@Nullable BlockState state, @Nullable Direction side, @Nullable RenderType renderType, Fluid fluid) {}

    private static final class Baked extends BakedModelWrapper<BakedModel> {
        private final Map<CacheKey, List<BakedQuad>> cache = new ConcurrentHashMap<>();

        private Baked(BakedModel original) {
            super(original);
        }

        @Override
        public @NotNull List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side, @NotNull RandomSource rand,
                                                 @NotNull ModelData data, @Nullable RenderType renderType) {
            List<BakedQuad> quads = super.getQuads(state, side, rand, data, renderType);
            FluidStack fluid = data.get(ModelProperties.FLUID_STACK);
            if (fluid == null || fluid.isEmpty() || quads.isEmpty()) return quads;

            return cache.computeIfAbsent(new CacheKey(state, side, renderType, fluid.getFluid()),
                    key -> retexture(quads, fluid));
        }
    }

    private static List<BakedQuad> retexture(List<BakedQuad> quads, FluidStack fluid) {
        IClientFluidTypeExtensions clientFluid = IClientFluidTypeExtensions.of(fluid.getFluid());
        ResourceLocation stillTexture = clientFluid.getStillTexture(fluid);
        if (stillTexture == null) return quads;

        TextureAtlasSprite sprite = Minecraft.getInstance().getTextureAtlas(InventoryMenu.BLOCK_ATLAS).apply(stillTexture);
        int color = clientFluid.getTintColor(fluid) | 0xFF000000;
        int light = fluid.getFluidType().getLightLevel(fluid);

        IQuadTransformer colorTransformer = QuadTransformers.applyingColor(color);
        IQuadTransformer lightTransformer = light > 0 ? QuadTransformers.settingEmissivity(light) : null;

        List<BakedQuad> result = new ArrayList<>(quads.size());
        for (BakedQuad quad : quads) {
            if (!quad.getSprite().contents().name().equals(FLUID_PLACEHOLDER)) {
                result.add(quad);
                continue;
            }

            BakedQuad moved = moveToSprite(quad, sprite);
            moved = colorTransformer.process(moved);
            if (lightTransformer != null) moved = lightTransformer.process(moved);
            result.add(moved);
        }
        return List.copyOf(result);
    }

    private static BakedQuad moveToSprite(BakedQuad quad, TextureAtlasSprite sprite) {
        TextureAtlasSprite old = quad.getSprite();
        int[] vertices = quad.getVertices().clone();

        for (int i = 0; i < 4; i++) {
            int offset = i * IQuadTransformer.STRIDE + IQuadTransformer.UV0;
            float u = Float.intBitsToFloat(vertices[offset]);
            float v = Float.intBitsToFloat(vertices[offset + 1]);
            vertices[offset]     = Float.floatToRawIntBits(sprite.getU(old.getUOffset(u)));
            vertices[offset + 1] = Float.floatToRawIntBits(sprite.getV(old.getVOffset(v)));
        }

        return new BakedQuad(vertices, -1, quad.getDirection(), sprite, quad.isShade(), quad.hasAmbientOcclusion());
    }
}

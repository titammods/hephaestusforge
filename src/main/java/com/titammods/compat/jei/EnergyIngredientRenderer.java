package com.titammods.compat.jei;

import com.titammods.TitamMods;
import com.titammods.common.blockentities.SmelteryControllerBlockEntity;
import mezz.jei.api.ingredients.IIngredientRenderer;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.TooltipFlag;

import java.util.List;

public class EnergyIngredientRenderer implements IIngredientRenderer<EnergyIngredient> {

    private static final Identifier TEXTURE = Identifier.fromNamespaceAndPath(TitamMods.MODID, "textures/gui/smeltery_energy.png");
    private static final int TEX_W = 16;
    private static final int TEX_H = 90;

    private final int width;
    private final int height;

    public EnergyIngredientRenderer(int width, int height) {
        this.width = width;
        this.height = height;
    }

    @Override
    public void render(GuiGraphicsExtractor graphics, EnergyIngredient ingredient) {
        int h = Math.min(height, TEX_H);
        int y = height - h;
        float v = TEX_H - h;
        int left = Math.min(width, TEX_W) / 2;
        int right = Math.min(width, TEX_W) - left;
        graphics.blit(RenderPipelines.GUI_TEXTURED, TEXTURE, 0, y, 0f, v, left, h, TEX_W, TEX_H);
        graphics.blit(RenderPipelines.GUI_TEXTURED, TEXTURE, left, y, TEX_W - right, v, right, h, TEX_W, TEX_H);
    }

    @SuppressWarnings("removal")
    @Override
    public List<Component> getTooltip(EnergyIngredient ingredient, TooltipFlag tooltipFlag) {
        return List.of(
                Component.translatable("gui.hephaestus.jei.energy").withStyle(ChatFormatting.RED),
                Component.translatable("gui.hephaestus.jei.energy_cost",
                        ingredient.amount(), SmelteryControllerBlockEntity.FUEL_PER_ENERGY).withStyle(ChatFormatting.GRAY),
                Component.translatable("gui.hephaestus.jei.energy_requires").withStyle(ChatFormatting.DARK_GRAY));
    }

    @Override
    public int getWidth() {
        return width;
    }

    @Override
    public int getHeight() {
        return height;
    }
}
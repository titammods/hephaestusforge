package com.titammods.compat.jei;

import com.titammods.client.screen.HoveredFluid;
import com.titammods.client.screen.MelterScreen;
import com.titammods.client.screen.SmelteryScreen;
import mezz.jei.api.gui.handlers.IGuiContainerHandler;
import mezz.jei.api.ingredients.ITypedIngredient;
import mezz.jei.api.neoforge.NeoForgeTypes;
import mezz.jei.api.registration.IGuiHandlerRegistration;
import mezz.jei.api.runtime.IClickableIngredient;
import mezz.jei.api.runtime.IIngredientManager;
import net.minecraft.client.renderer.Rect2i;
import net.neoforged.neoforge.fluids.FluidStack;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;

public class HephaestusGuiHandlers {

    private static IIngredientManager ingredientManager;

    private HephaestusGuiHandlers() {}

    public static void setIngredientManager(@Nullable IIngredientManager manager) {
        ingredientManager = manager;
    }

    public static void register(IGuiHandlerRegistration registration) {
        registration.addGuiContainerHandler(SmelteryScreen.class, new IGuiContainerHandler<SmelteryScreen>() {
            @Override
            public List<Rect2i> getGuiExtraAreas(SmelteryScreen screen) {
                return screen.getSidePanelAreas();
            }

            @Override
            public Optional<IClickableIngredient<?>> getClickableIngredientUnderMouse(SmelteryScreen screen, double mouseX, double mouseY) {
                return clickableFluid(screen.getHoveredFluid(mouseX, mouseY));
            }
        });

        registration.addGuiContainerHandler(MelterScreen.class, new IGuiContainerHandler<MelterScreen>() {
            @Override
            public Optional<IClickableIngredient<?>> getClickableIngredientUnderMouse(MelterScreen screen, double mouseX, double mouseY) {
                return clickableFluid(screen.getHoveredFluid(mouseX, mouseY));
            }
        });
    }

    private static Optional<IClickableIngredient<?>> clickableFluid(@Nullable HoveredFluid hovered) {
        if (hovered == null || hovered.fluid().isEmpty() || ingredientManager == null) return Optional.empty();
        Rect2i area = new Rect2i(hovered.x(), hovered.y(), hovered.width(), hovered.height());
        return ingredientManager.createTypedIngredient(NeoForgeTypes.FLUID_STACK, hovered.fluid().copy())
                .<IClickableIngredient<?>>map(typed -> new ClickableFluid(typed, area));
    }

    private record ClickableFluid(ITypedIngredient<FluidStack> typed, Rect2i area) implements IClickableIngredient<FluidStack> {
        @Override
        public ITypedIngredient<FluidStack> getTypedIngredient() {
            return typed;
        }

        @Override
        public Rect2i getArea() {
            return area;
        }
    }
}
package com.titammods.client.screen;

import net.neoforged.neoforge.fluids.FluidStack;

public record HoveredFluid(FluidStack fluid, int x, int y, int width, int height) {
}
package com.titammods.compat.jei;

import com.titammods.setup.ModBlocks;
import com.titammods.setup.ModRecipes;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.RecipeType;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.material.Fluids;

import java.util.List;

public class SmelteryCategory extends MelterCategory {

    private static int energyTemperature = 1000;

    private final IDrawable icon;

    public SmelteryCategory(IGuiHelper guiHelper) {
        super(guiHelper);
        this.icon = guiHelper.createDrawableItemStack(new ItemStack(ModBlocks.SMELTERY_CONTROLLER.get()));
    }

    public static void updateEnergyTemperature(List<ModRecipes.MeltingRecipe> recipes) {
        int max = 0;
        for (ModRecipes.MeltingRecipe recipe : recipes) {
            if (recipe.fuel().getFluid().isSame(Fluids.LAVA)) max = Math.max(max, recipe.temperature());
        }
        energyTemperature = max > 0 ? max : 1000;
    }

    public static boolean acceptsEnergy(int temperature) {
        return temperature <= energyTemperature;
    }

    @SuppressWarnings("removal")
    @Override public RecipeType<ModRecipes.MeltingRecipe> getRecipeType() { return TitamModsJEIPlugin.SMELTERY_TYPE; }
    @Override public Component getTitle() { return Component.translatable("block.hephaestus.smeltery_controller"); }

    @SuppressWarnings("removal")
    @Override public IDrawable getIcon() { return icon; }

    @Override
    protected boolean acceptsEnergy(ModRecipes.MeltingRecipe recipe) {
        return acceptsEnergy(recipe.temperature());
    }
}
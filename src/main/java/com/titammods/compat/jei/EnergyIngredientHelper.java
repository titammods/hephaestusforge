package com.titammods.compat.jei;

import mezz.jei.api.ingredients.IIngredientHelper;
import mezz.jei.api.ingredients.IIngredientType;
import mezz.jei.api.ingredients.subtypes.UidContext;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import org.jspecify.annotations.Nullable;

public class EnergyIngredientHelper implements IIngredientHelper<EnergyIngredient> {

    @Override
    public IIngredientType<EnergyIngredient> getIngredientType() {
        return EnergyIngredient.TYPE;
    }

    @Override
    public String getDisplayName(EnergyIngredient ingredient) {
        return Component.translatable("gui.hephaestus.jei.energy").getString();
    }

    @Override
    public Object getUid(EnergyIngredient ingredient, UidContext context) {
        return EnergyIngredient.ID;
    }

    @Override
    public long getAmount(EnergyIngredient ingredient) {
        return ingredient.amount();
    }

    @Override
    public EnergyIngredient copyWithAmount(EnergyIngredient ingredient, long amount) {
        return new EnergyIngredient((int) Math.min(Integer.MAX_VALUE, Math.max(0, amount)));
    }

    @Override
    public Identifier getIdentifier(EnergyIngredient ingredient) {
        return EnergyIngredient.ID;
    }

    @Override
    public EnergyIngredient copyIngredient(EnergyIngredient ingredient) {
        return new EnergyIngredient(ingredient.amount());
    }

    @Override
    public String getErrorInfo(@Nullable EnergyIngredient ingredient) {
        return ingredient == null ? "null energy" : "energy " + ingredient.amount() + " FE";
    }
}
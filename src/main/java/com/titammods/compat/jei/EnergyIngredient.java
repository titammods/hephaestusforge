package com.titammods.compat.jei;

import com.mojang.serialization.Codec;
import com.titammods.TitamMods;
import com.titammods.common.blockentities.SmelteryControllerBlockEntity;
import mezz.jei.api.ingredients.IIngredientType;
import net.minecraft.resources.Identifier;

public record EnergyIngredient(int amount) {

    public static final Identifier ID = Identifier.fromNamespaceAndPath(TitamMods.MODID, "energy");

    public static final IIngredientType<EnergyIngredient> TYPE = () -> EnergyIngredient.class;

    public static final Codec<EnergyIngredient> CODEC = Codec.INT.xmap(EnergyIngredient::new, EnergyIngredient::amount);

    public static final EnergyIngredient FUEL = new EnergyIngredient(SmelteryControllerBlockEntity.ENERGY_PER_FUEL);
}
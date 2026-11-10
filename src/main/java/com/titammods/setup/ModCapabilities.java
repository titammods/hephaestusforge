package com.titammods.setup;

import com.titammods.TitamMods;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.transfer.item.WorldlyContainerWrapper;
import net.minecraft.core.Direction;

@EventBusSubscriber(modid = TitamMods.MODID)
public final class ModCapabilities {
    private ModCapabilities() {}

    @SubscribeEvent
    public static void register(RegisterCapabilitiesEvent event) {
        event.registerBlockEntity(Capabilities.Fluid.BLOCK, ModBlockEntities.SEARED_TANK.get(),
                (tank, side) -> tank.getFluidResourceHandler());
        event.registerBlockEntity(Capabilities.Fluid.BLOCK, ModBlockEntities.MELTER.get(),
                (melter, side) -> melter.getFluidResourceHandler());
        event.registerBlockEntity(Capabilities.Fluid.BLOCK, ModBlockEntities.TABLE.get(),
                (table, side) -> table.getFluidResourceHandler());
        event.registerBlockEntity(Capabilities.Fluid.BLOCK, ModBlockEntities.BASIN.get(),
                (basin, side) -> basin.getFluidResourceHandler());
        event.registerBlockEntity(Capabilities.Fluid.BLOCK, ModBlockEntities.SEARED_DRAIN.get(),
                (drain, side) -> drain.getFluidResourceHandler());
        event.registerBlockEntity(Capabilities.Item.BLOCK, ModBlockEntities.BASIN.get(),
                (basin, side) -> new WorldlyContainerWrapper(basin, side == null ? Direction.DOWN : side));
        event.registerBlockEntity(Capabilities.Energy.BLOCK, ModBlockEntities.SEARED_ENERGY_PORT.get(),
                (port, side) -> port.getEnergyHandler());
        event.registerBlockEntity(Capabilities.Item.BLOCK, ModBlockEntities.MELTER.get(),
                (melter, side) -> melter.getItemResourceHandler());
        event.registerBlockEntity(Capabilities.Item.BLOCK, ModBlockEntities.SEARED_CHUTE.get(),
                (chute, side) -> chute.getControllerPos() == null ? null : chute.getItemResourceHandler());
    }
}
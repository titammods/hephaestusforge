package com.titammods.common.blockentities;

import com.titammods.common.blocks.SearedBatteryBlock;
import com.titammods.setup.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.neoforged.neoforge.transfer.energy.SimpleEnergyHandler;

public class SearedBatteryBlockEntity extends BlockEntity {

    public static final int CAPACITY = 60000;

    private final SimpleEnergyHandler energyHandler = new SimpleEnergyHandler(CAPACITY, CAPACITY, CAPACITY) {
        @Override
        protected void onEnergyChanged(int previousAmount) {
            setChanged();
            if (level instanceof ServerLevel serverLevel) updateChargeState(serverLevel);
        }
    };

    public SearedBatteryBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.SEARED_BATTERY.get(), pos, state);
    }

    public SimpleEnergyHandler getEnergyHandler() { return energyHandler; }

    private void updateChargeState(ServerLevel serverLevel) {
        BlockState current = getBlockState();
        if (!current.hasProperty(SearedBatteryBlock.CHARGE)) return;
        int energy = energyHandler.getAmountAsInt();
        int charge = energy <= 0 ? 0 : Mth.clamp(Mth.ceil(energy * (float) SearedBatteryBlock.MAX_CHARGE / CAPACITY), 1, SearedBatteryBlock.MAX_CHARGE);
        if (current.getValue(SearedBatteryBlock.CHARGE) != charge)
            serverLevel.setBlock(worldPosition, current.setValue(SearedBatteryBlock.CHARGE, charge), Block.UPDATE_CLIENTS);
    }

    @Override
    public void onLoad() {
        super.onLoad();
        if (level instanceof ServerLevel serverLevel) updateChargeState(serverLevel);
    }

    @Override
    protected void saveAdditional(ValueOutput output) {
        super.saveAdditional(output);
        energyHandler.serialize(output);
    }

    @Override
    protected void loadAdditional(ValueInput input) {
        super.loadAdditional(input);
        energyHandler.deserialize(input);
    }
}

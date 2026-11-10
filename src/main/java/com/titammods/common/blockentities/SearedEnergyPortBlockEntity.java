package com.titammods.common.blockentities;

import com.titammods.common.blocks.SmelteryControllerBlock;
import com.titammods.setup.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.neoforged.neoforge.transfer.TransferPreconditions;
import net.neoforged.neoforge.transfer.energy.EnergyHandler;
import net.neoforged.neoforge.transfer.transaction.TransactionContext;
import org.jspecify.annotations.Nullable;

import java.util.List;
import java.util.Objects;

public class SearedEnergyPortBlockEntity extends BlockEntity {

    private @Nullable BlockPos controllerPos;

    private final EnergyHandler energyHandler = new EnergyHandler() {
        @Override
        public long getAmountAsLong() {
            long total = 0;
            for (SearedBatteryBlockEntity battery : batteries()) total += battery.getEnergyHandler().getAmountAsLong();
            return total;
        }

        @Override
        public long getCapacityAsLong() {
            long total = 0;
            for (SearedBatteryBlockEntity battery : batteries()) total += battery.getEnergyHandler().getCapacityAsLong();
            return total;
        }

        @Override
        public int insert(int amount, TransactionContext transaction) {
            TransferPreconditions.checkNonNegative(amount);
            int remaining = amount;
            for (SearedBatteryBlockEntity battery : batteries()) {
                if (remaining <= 0) break;
                remaining -= battery.getEnergyHandler().insert(remaining, transaction);
            }
            return amount - remaining;
        }

        @Override
        public int extract(int amount, TransactionContext transaction) {
            TransferPreconditions.checkNonNegative(amount);
            return 0;
        }
    };

    public SearedEnergyPortBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.SEARED_ENERGY_PORT.get(), pos, state);
    }

    public void setControllerPos(@Nullable BlockPos pos) {
        if (Objects.equals(controllerPos, pos)) return;
        this.controllerPos = pos;
        setChanged();
        invalidateCapabilities();
    }

    public @Nullable BlockPos getControllerPos() { return controllerPos; }

    public @Nullable EnergyHandler getEnergyHandler() {
        return controllerPos == null ? null : energyHandler;
    }

    private List<SearedBatteryBlockEntity> batteries() {
        if (controllerPos == null || level == null || !level.hasChunkAt(controllerPos)) return List.of();
        if (level.getBlockEntity(controllerPos) instanceof SmelteryControllerBlockEntity controller
                && controller.getBlockState().getValue(SmelteryControllerBlock.IN_STRUCTURE)) {
            return controller.getBatteries();
        }
        return List.of();
    }

    @Override
    protected void saveAdditional(ValueOutput output) {
        super.saveAdditional(output);
        if (controllerPos != null) {
            output.putInt("cx", controllerPos.getX());
            output.putInt("cy", controllerPos.getY());
            output.putInt("cz", controllerPos.getZ());
        }
    }

    @Override
    protected void loadAdditional(ValueInput input) {
        super.loadAdditional(input);
        controllerPos = null;
        if (input.getIntOr("cy", Integer.MIN_VALUE) != Integer.MIN_VALUE)
            controllerPos = new BlockPos(input.getIntOr("cx", 0), input.getIntOr("cy", 0), input.getIntOr("cz", 0));
    }
}

package com.titammods.common.blocks;

import com.mojang.serialization.MapCodec;
import com.titammods.common.blockentities.SearedBatteryBlockEntity;
import com.titammods.setup.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.ProblemReporter;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.storage.TagValueOutput;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import org.jspecify.annotations.Nullable;

import java.util.List;

public class SearedBatteryBlock extends BaseEntityBlock {

    public static final int MAX_CHARGE = 10;
    public static final IntegerProperty CHARGE = IntegerProperty.create("charge", 0, MAX_CHARGE);

    public static final MapCodec<SearedBatteryBlock> CODEC = simpleCodec(SearedBatteryBlock::new);
    @Override protected MapCodec<? extends BaseEntityBlock> codec() { return CODEC; }

    public SearedBatteryBlock(Properties properties) {
        super(properties);
        registerDefaultState(stateDefinition.any().setValue(CHARGE, 0));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(CHARGE);
    }

    @Override public RenderShape getRenderShape(BlockState state) { return RenderShape.MODEL; }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new SearedBatteryBlockEntity(pos, state);
    }

    @Override
    public ItemStack getCloneItemStack(LevelReader level, BlockPos pos, BlockState state, boolean includeData) {
        ItemStack stack = super.getCloneItemStack(level, pos, state, includeData);
        if (includeData && level.getBlockEntity(pos) instanceof SearedBatteryBlockEntity battery
                && battery.getEnergyHandler().getAmountAsInt() > 0) {
            var output = TagValueOutput.createWithContext(ProblemReporter.DISCARDING, level.registryAccess());
            battery.saveWithId(output);
            BlockItem.setBlockEntityData(stack, ModBlockEntities.SEARED_BATTERY.get(), output);
        }
        return stack;
    }

    @Override
    public List<ItemStack> getDrops(BlockState state, LootParams.Builder params) {
        List<ItemStack> drops = super.getDrops(state, params);
        var be = params.getOptionalParameter(LootContextParams.BLOCK_ENTITY);
        if (be instanceof SearedBatteryBlockEntity battery && battery.getEnergyHandler().getAmountAsInt() > 0
                && params.getLevel() instanceof ServerLevel sl) {
            var output = TagValueOutput.createWithContext(ProblemReporter.DISCARDING, sl.registryAccess());
            battery.saveWithId(output);
            drops.forEach(drop -> {
                if (drop.getItem() == asItem())
                    BlockItem.setBlockEntityData(drop, ModBlockEntities.SEARED_BATTERY.get(), output);
            });
        }
        return drops;
    }
}

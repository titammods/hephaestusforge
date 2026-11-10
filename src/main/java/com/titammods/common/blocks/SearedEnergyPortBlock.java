package com.titammods.common.blocks;

import com.mojang.serialization.MapCodec;
import com.titammods.common.blockentities.SearedEnergyPortBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import org.jspecify.annotations.Nullable;

public class SearedEnergyPortBlock extends BaseEntityBlock {

    public static final BooleanProperty IN_STRUCTURE = BooleanProperty.create("in_structure");

    public static final MapCodec<SearedEnergyPortBlock> CODEC = simpleCodec(SearedEnergyPortBlock::new);
    @Override protected MapCodec<? extends BaseEntityBlock> codec() { return CODEC; }

    public SearedEnergyPortBlock(Properties properties) {
        super(properties);
        registerDefaultState(stateDefinition.any().setValue(IN_STRUCTURE, false));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(IN_STRUCTURE);
    }

    @Override public RenderShape getRenderShape(BlockState state) { return RenderShape.MODEL; }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new SearedEnergyPortBlockEntity(pos, state);
    }
}

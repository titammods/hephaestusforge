package com.titammods.common.blockentities.multiblock;

import com.titammods.common.blocks.SearedBatteryBlock;
import com.titammods.common.blocks.SearedTankBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

import java.util.ArrayList;
import java.util.List;

public class SmelteryMultiblock {

    private final Level level;
    private final BlockPos controllerPos;
    private static final int MAX_SIDE = 15;

    public boolean isValid       = false;
    public BlockPos minInner;
    public BlockPos maxInner;
    public int internalVolume    = 0;
    public boolean mixedFuel     = false;

    public final List<BlockPos> tanks = new ArrayList<>();
    public final List<BlockPos> batteries = new ArrayList<>();
    public final List<BlockPos> walls = new ArrayList<>();
    public final List<BlockPos> floor = new ArrayList<>();

    public SmelteryMultiblock(Level level, BlockPos controllerPos) {
        this.level         = level;
        this.controllerPos = controllerPos;
    }

    public void scanStructure(Direction facing) {
        isValid = false;
        mixedFuel = false;
        tanks.clear();
        batteries.clear();
        walls.clear();
        floor.clear();
        internalVolume = 0;

        BlockState controllerState = level.getBlockState(controllerPos);
        if (controllerState.isAir()) return;

        BlockPos startInner = controllerPos.relative(facing.getOpposite());
        BlockPos floorCenter = startInner;
        boolean foundFloor   = false;

        for (int i = 0; i < 16; i++) {
            if (!isInnerBlock(level.getBlockState(floorCenter))) {
                foundFloor = true;
                break;
            }
            floorCenter = floorCenter.below();
        }
        if (!foundFloor) return;
        if (!isValidFloor(level.getBlockState(floorCenter))) return;

        int floorY = floorCenter.getY();

        int minX = floorCenter.getX(), maxX = floorCenter.getX();
        int minZ = floorCenter.getZ(), maxZ = floorCenter.getZ();

        int xBlocks = 1;
        int yBlocks = 1;

        while(xBlocks <= MAX_SIDE && isInnerBlock(level.getBlockState(new BlockPos(minX - 1, floorY + 1, floorCenter.getZ())))) {xBlocks++; minX--;}
        while(xBlocks <= MAX_SIDE && isInnerBlock(level.getBlockState(new BlockPos(maxX + 1, floorY + 1, floorCenter.getZ())))) {xBlocks++; maxX++;}
        if(xBlocks > MAX_SIDE) return;
        while (yBlocks <= MAX_SIDE && isInnerBlock(level.getBlockState(new BlockPos(floorCenter.getX(), floorY + 1, minZ - 1)))) {yBlocks++; minZ--;}
        while (yBlocks <= MAX_SIDE && isInnerBlock(level.getBlockState(new BlockPos(floorCenter.getX(), floorY + 1, maxZ + 1)))) {yBlocks++; maxZ++;}
        if(yBlocks > MAX_SIDE) return;

        for (int x = minX; x <= maxX; x++) {
            for (int z = minZ; z <= maxZ; z++) {
                BlockPos fPos = new BlockPos(x, floorY, z);
                if (!isValidFloor(level.getBlockState(fPos))) return;
                floor.add(fPos);
            }
        }

        int currentY = floorY + 1;
        boolean controllerFound = false;

        List<BlockPos> tempWalls = new ArrayList<>();
        List<BlockPos> tempTanks = new ArrayList<>();
        List<BlockPos> tempBatteries = new ArrayList<>();

        while (currentY < floorY + 64) {
            boolean isLayerValid  = true;
            boolean hasWalls      = false;
            tempWalls.clear();
            tempTanks.clear();
            tempBatteries.clear();

            for (int x = minX - 1; x <= maxX + 1; x++) {
                for (int z = minZ - 1; z <= maxZ + 1; z++) {
                    BlockPos pos  = new BlockPos(x, currentY, z);
                    BlockState st = level.getBlockState(pos);

                    boolean isXEdge = (x == minX - 1 || x == maxX + 1);
                    boolean isZEdge = (z == minZ - 1 || z == maxZ + 1);

                    if (isXEdge && isZEdge) continue;

                    if (isXEdge || isZEdge) {
                        if (pos.equals(controllerPos)) {
                            controllerFound = true;
                            hasWalls = true;
                            continue;
                        }
                        if (isValidWall(st)) {
                            tempWalls.add(pos);
                            hasWalls = true;
                            if (isTank(st)) tempTanks.add(pos);
                            if (isBattery(st)) tempBatteries.add(pos);
                        } else {
                            isLayerValid = false;
                        }
                    } else {
                        if (!isInnerBlock(st)) isLayerValid = false;
                    }
                }
            }

            if (hasWalls) {
                if (isLayerValid) {
                    walls.addAll(tempWalls);
                    tanks.addAll(tempTanks);
                    batteries.addAll(tempBatteries);
                } else {
                    return;
                }
            } else {
                break;
            }
            currentY++;
        }

        int height = (currentY - 1) - floorY;
        if (!tanks.isEmpty() && !batteries.isEmpty()) {
            mixedFuel = true;
            return;
        }
        if (height > 0 && controllerFound) {
            this.minInner      = new BlockPos(minX, floorY + 1, minZ);
            this.maxInner      = new BlockPos(maxX, currentY - 1, maxZ);
            this.internalVolume = (maxX - minX + 1) * (maxZ - minZ + 1) * height;
            this.isValid       = true;
        }
    }

    private boolean isValidFloor(BlockState state) {
        return state.is(BlockTags.create(Identifier.fromNamespaceAndPath("c", "smeltery_floor")));
    }

    private boolean isValidWall(BlockState state) {
        return state.is(BlockTags.create(Identifier.fromNamespaceAndPath("c", "smeltery_wall")))
                || state.is(BlockTags.create(Identifier.fromNamespaceAndPath("c", "smeltery_floor")))
                || isTank(state)
                || isBattery(state);
    }

    private boolean isBattery(BlockState state) {
        return state.getBlock() instanceof SearedBatteryBlock
                || state.is(BlockTags.create(Identifier.fromNamespaceAndPath("c", "smeltery_batteries")));
    }

    private boolean isTank(BlockState state) {
        return state.getBlock() instanceof SearedTankBlock
                || state.is(BlockTags.create(Identifier.fromNamespaceAndPath("c", "smeltery_tanks")));
    }

    private boolean isInnerBlock(BlockState state) {
        if (isValidWall(state)) return false;
        return state.isAir() || !state.getFluidState().isEmpty();
    }
}
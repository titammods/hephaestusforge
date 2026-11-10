package com.titammods.registry.fluids;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.fluids.FluidType;

public class MoltenFluidType extends FluidType {

    public final int tintColor;

    public MoltenFluidType(Properties properties, int tintColor) {
        super(properties);
        this.tintColor = tintColor;
    }

    @Override
    public boolean move(LivingEntity entity, Vec3 movementVector, double gravity) {
        double fluidHeight = entity.getFluidTypeHeight(this);
        boolean isFalling = entity.getDeltaMovement().y <= 0.0;
        double oldY = entity.getY();
        entity.moveRelative(0.02F, movementVector);
        entity.move(MoverType.SELF, entity.getDeltaMovement());
        if (fluidHeight <= entity.getFluidJumpThreshold()) {
            entity.setDeltaMovement(entity.getDeltaMovement().multiply(0.5, 0.8F, 0.5));
            entity.setDeltaMovement(entity.getFluidFallingAdjustedMovement(gravity, isFalling, entity.getDeltaMovement()));
        } else {
            entity.setDeltaMovement(entity.getDeltaMovement().scale(0.5));
        }
        if (gravity != 0.0) {
            entity.setDeltaMovement(entity.getDeltaMovement().add(0.0, -gravity / 4.0, 0.0));
        }
        Vec3 movement = entity.getDeltaMovement();
        if (entity.horizontalCollision && entity.isFree(movement.x, movement.y + 0.6F - entity.getY() + oldY, movement.z)) {
            entity.setDeltaMovement(movement.x, 0.3F, movement.z);
        }
        return true;
    }
}
package com.titammods.block;

import com.titammods.recipe.HephaestusRecipeCache;
import com.titammods.setup.ModBlockEntities;
import com.titammods.setup.ModRecipes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Containers;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.fluids.capability.IFluidHandler.FluidAction;
import net.neoforged.neoforge.fluids.capability.templates.FluidTank;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemHandlerHelper;
import net.neoforged.neoforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class BasinBlockEntity extends BlockEntity {

    public static final int OUTPUT = 0;
    public static final int CAST   = 1;

    public int coolingTime   = 0;
    public int renderTimer   = 0;
    public int ejectCooldown = 0;
    public int fillTarget    = 0;
    private boolean drainable = false;
    private long lastFillTime = 0;
    private int lastAmount    = 0;

    public final ItemStackHandler inventory = new ItemStackHandler(2) {
        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
            if (level != null && !level.isClientSide) {
                level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), Block.UPDATE_ALL);
            }
        }

        @Override
        public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
            return false;
        }
    };

    public final FluidTank tank = new FluidTank(10000) {

        @Override
        public int fill(FluidStack resource, IFluidHandler.FluidAction action) {
            if (!inventory.getStackInSlot(OUTPUT).isEmpty() || renderTimer > 0) return 0;

            if (!fluid.isEmpty() && !FluidStack.isSameFluidSameComponents(fluid, resource)) return 0;

            ModRecipes.CastingBasinRecipe recipe = findRecipeByType(inventory.getStackInSlot(CAST), resource.getFluid());
            if (recipe == null) return 0;
            fillTarget = recipe.input().getAmount();

            int spaceLeft = recipe.input().getAmount() - fluid.getAmount();
            if (spaceLeft <= 0) return 0;

            FluidStack limited = resource.copy();
            limited.setAmount(Math.min(resource.getAmount(), spaceLeft));
            return super.fill(limited, action);
        }

        @Override
        protected void onContentsChanged() {
            int amount = fluid.getAmount();
            if (amount > lastAmount) {
                drainable = false;
                if (level != null) lastFillTime = level.getGameTime();
            }
            if (amount == 0) drainable = false;
            lastAmount = amount;
            setChanged();
            if (level != null && !level.isClientSide) {
                level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), Block.UPDATE_ALL);
            }
        }
    };

    public final IFluidHandler externalFluidHandler = new IFluidHandler() {
        @Override public int getTanks() { return 1; }
        @Override public FluidStack getFluidInTank(int t) { return tank.getFluidInTank(t); }
        @Override public int getTankCapacity(int t) { return tank.getTankCapacity(t); }
        @Override public boolean isFluidValid(int t, FluidStack stack) { return tank.isFluidValid(t, stack); }
        @Override public int fill(FluidStack resource, FluidAction action) { return tank.fill(resource, action); }

        @Override
        public FluidStack drain(FluidStack resource, FluidAction action) {
            if (!canExtractFluid()) return FluidStack.EMPTY;
            return tank.drain(resource, action);
        }

        @Override
        public FluidStack drain(int maxDrain, FluidAction action) {
            if (!canExtractFluid()) return FluidStack.EMPTY;
            return tank.drain(maxDrain, action);
        }
    };

    public final IItemHandler externalItemHandler = new IItemHandler() {
        @Override public int getSlots() { return 1; }
        @Override public ItemStack getStackInSlot(int slot) { return inventory.getStackInSlot(OUTPUT); }
        @Override public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) { return stack; }
        @Override public int getSlotLimit(int slot) { return inventory.getSlotLimit(OUTPUT); }
        @Override public boolean isItemValid(int slot, ItemStack stack) { return false; }

        @Override
        public ItemStack extractItem(int slot, int amount, boolean simulate) {
            if (ejectCooldown > 0 || renderTimer > 0) return ItemStack.EMPTY;
            return inventory.extractItem(OUTPUT, amount, simulate);
        }
    };

    public BasinBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.BASIN.get(), pos, state);
    }

    private boolean canExtractFluid() {
        if (level == null || level.isClientSide) return false;
        if (renderTimer > 0 || !inventory.getStackInSlot(OUTPUT).isEmpty()) return false;
        FluidStack fluid = tank.getFluid();
        if (!drainable || fluid.isEmpty()) return false;
        return findRecipe(inventory.getStackInSlot(CAST), fluid.getFluid(), fluid.getAmount()) == null;
    }

    public void dropContents() {
        if (level == null || level.isClientSide) return;
        for (int slot = 0; slot < inventory.getSlots(); slot++) {
            Containers.dropItemStack(level, worldPosition.getX(), worldPosition.getY(), worldPosition.getZ(), inventory.getStackInSlot(slot));
        }
    }

    public void interact(Player player) {
        if (renderTimer > 0) return;

        ItemStack output = inventory.getStackInSlot(OUTPUT);
        if (!output.isEmpty()) {
            ItemHandlerHelper.giveItemToPlayer(player, output, player.getInventory().selected);
            inventory.setStackInSlot(OUTPUT, ItemStack.EMPTY);
            tank.setFluid(FluidStack.EMPTY);
            ejectCooldown = 0;
            setChanged();
            if (level != null) level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), Block.UPDATE_ALL);
            return;
        }

        if (tank.isEmpty()) {
            ItemStack cast = inventory.getStackInSlot(CAST);
            if (!cast.isEmpty()) {
                ItemHandlerHelper.giveItemToPlayer(player, cast, player.getInventory().selected);
                inventory.setStackInSlot(CAST, ItemStack.EMPTY);
            } else if (!player.getMainHandItem().isEmpty()) {
                ItemStack handItem = player.getMainHandItem().copy();
                handItem.setCount(1);
                inventory.setStackInSlot(CAST, handItem);
                if (!player.isCreative()) player.getMainHandItem().shrink(1);
            }
            setChanged();
            if (level != null) level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), Block.UPDATE_ALL);
        }
    }

    public void tick() {
        if (level == null || level.isClientSide) return;

        if (ejectCooldown > 0) ejectCooldown--;

        if (renderTimer > 0) {
            renderTimer--;
            return;
        }

        if (!inventory.getStackInSlot(OUTPUT).isEmpty()) {
            coolingTime = 0;
            return;
        }

        FluidStack currentFluid = tank.getFluid();
        if (currentFluid.isEmpty()) {
            coolingTime = 0;
            return;
        }

        ModRecipes.CastingBasinRecipe matchedRecipe =
                findRecipe(inventory.getStackInSlot(CAST), currentFluid.getFluid(), currentFluid.getAmount());

        if (matchedRecipe == null && !drainable && level.getGameTime() - lastFillTime >= 20) drainable = true;

        if (matchedRecipe != null) {
            coolingTime++;
            if (coolingTime >= matchedRecipe.time()) {
                coolingTime = 0;
                tank.drain(matchedRecipe.input().getAmount(), FluidAction.EXECUTE);
                if (matchedRecipe.hasCast()) inventory.setStackInSlot(CAST, ItemStack.EMPTY);
                inventory.setStackInSlot(OUTPUT, matchedRecipe.output().copy());
                renderTimer   = 20;
                ejectCooldown = 50;
                level.playSound(null, worldPosition, SoundEvents.LAVA_EXTINGUISH, SoundSource.BLOCKS,
                        0.5F, 2.6F + (level.random.nextFloat() - level.random.nextFloat()) * 0.8F);
                setChanged();
                level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), Block.UPDATE_ALL);
            }
        } else {
            coolingTime = 0;
        }
    }

    @Nullable
    private ModRecipes.CastingBasinRecipe findRecipeByType(ItemStack cast, Fluid fluid) {
        return HephaestusRecipeCache.basin(level, cast, fluid);
    }

    @Nullable
    private ModRecipes.CastingBasinRecipe findRecipe(ItemStack cast, Fluid fluid, int amount) {
        return HephaestusRecipeCache.basin(level, cast, fluid, amount);
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        tag.putInt("coolingTime", coolingTime);
        tag.putInt("renderTimer", renderTimer);
        tag.putInt("ejectCooldown", ejectCooldown);
        tag.putInt("fillTarget", fillTarget);
        tag.put("slot0", inventory.getStackInSlot(OUTPUT).saveOptional(registries));
        tag.put("cast", inventory.getStackInSlot(CAST).saveOptional(registries));
        tag.put("tank", tank.writeToNBT(registries, new CompoundTag()));
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        coolingTime   = tag.getInt("coolingTime");
        renderTimer   = tag.getInt("renderTimer");
        ejectCooldown = tag.getInt("ejectCooldown");
        fillTarget    = tag.getInt("fillTarget");
        if (tag.contains("slot0")) {
            inventory.setStackInSlot(OUTPUT, ItemStack.parseOptional(registries, tag.getCompound("slot0")));
            inventory.setStackInSlot(CAST, ItemStack.parseOptional(registries, tag.getCompound("cast")));
        } else if (tag.contains("inventory")) {
            ItemStackHandler legacy = new ItemStackHandler(1);
            legacy.deserializeNBT(registries, tag.getCompound("inventory"));
            inventory.setStackInSlot(OUTPUT, legacy.getSlots() > 0 ? legacy.getStackInSlot(0) : ItemStack.EMPTY);
            inventory.setStackInSlot(CAST, ItemStack.EMPTY);
        } else {
            inventory.setStackInSlot(OUTPUT, ItemStack.EMPTY);
            inventory.setStackInSlot(CAST, ItemStack.EMPTY);
        }
        tank.readFromNBT(registries, tag.getCompound("tank"));
        lastAmount = tank.getFluidAmount();
        drainable  = false;
    }

    @Override public CompoundTag getUpdateTag(HolderLookup.Provider registries) { CompoundTag tag = new CompoundTag(); saveAdditional(tag, registries); return tag; }
    @Override public Packet<ClientGamePacketListener> getUpdatePacket() { return ClientboundBlockEntityDataPacket.create(this); }
}

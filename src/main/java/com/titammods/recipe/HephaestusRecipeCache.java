package com.titammods.recipe;

import com.titammods.setup.ModRecipes;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.fluids.FluidStack;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public final class HephaestusRecipeCache {

    private static final int MAX_ENTRIES = 4096;

    private record StackKey(Item item, DataComponentPatch patch) {}

    private record CastKey(StackKey cast, Fluid fluid) {}

    private static final StackKey EMPTY_KEY = new StackKey(Items.AIR, DataComponentPatch.EMPTY);

    private static final Map<StackKey, Optional<ModRecipes.MeltingRecipe>> MELTING = new ConcurrentHashMap<>();
    private static final Map<CastKey, List<ModRecipes.CastingTableRecipe>> TABLE = new ConcurrentHashMap<>();
    private static final Map<CastKey, List<ModRecipes.CastingBasinRecipe>> BASIN = new ConcurrentHashMap<>();
    private static volatile Map<Fluid, Integer> fuelTemperatures;
    private static RecipeManager recipeManager;

    private HephaestusRecipeCache() {}

    private static StackKey key(ItemStack stack) {
        return stack.isEmpty() ? EMPTY_KEY : new StackKey(stack.getItem(), stack.getComponentsPatch());
    }

    private static <K, V> void limit(Map<K, V> map) {
        if (map.size() > MAX_ENTRIES) map.clear();
    }

    @Nullable
    private static synchronized RecipeManager validate(@Nullable Level level) {
        if (level == null) return null;
        RecipeManager current = level.getRecipeManager();
        if (level.isClientSide()) return current;
        if (current != recipeManager) {
            recipeManager = current;
            MELTING.clear();
            TABLE.clear();
            BASIN.clear();
            fuelTemperatures = null;
        }
        return current;
    }

    @Nullable
    public static ModRecipes.MeltingRecipe melting(@Nullable Level level, ItemStack stack) {
        RecipeManager manager = validate(level);
        if (manager == null || stack.isEmpty()) return null;
        boolean useCache = !level.isClientSide();
        StackKey key = key(stack);
        Optional<ModRecipes.MeltingRecipe> cached = useCache ? MELTING.get(key) : null;
        if (cached == null) {
            ModRecipes.MeltingRecipe found = null;
            for (var holder : manager.getAllRecipesFor(ModRecipes.MELTING_TYPE.get())) {
                if (holder.value().input().test(stack)) {
                    found = holder.value();
                    break;
                }
            }
            cached = Optional.ofNullable(found);
            if (useCache) {
                limit(MELTING);
                MELTING.put(key, cached);
            }
        }
        return cached.orElse(null);
    }

    public static int fuelTemperature(@Nullable Level level, FluidStack fluid) {
        RecipeManager manager = validate(level);
        if (manager == null || fluid.isEmpty()) return 0;
        boolean useCache = !level.isClientSide();
        Map<Fluid, Integer> temps = useCache ? fuelTemperatures : null;
        if (temps == null) {
            temps = new HashMap<>();
            for (var holder : manager.getAllRecipesFor(ModRecipes.MELTING_TYPE.get())) {
                ModRecipes.MeltingRecipe recipe = holder.value();
                FluidStack fuel = recipe.fuel();
                if (fuel.isEmpty()) continue;
                temps.merge(fuel.getFluid(), recipe.temperature(), Math::max);
            }
            if (useCache) fuelTemperatures = temps;
        }
        int max = 0;
        for (var entry : temps.entrySet()) {
            if (entry.getKey().isSame(fluid.getFluid())) max = Math.max(max, entry.getValue());
        }
        return max;
    }

    private static List<ModRecipes.CastingTableRecipe> tableCandidates(RecipeManager manager, ItemStack mold, Fluid fluid, boolean useCache) {
        CastKey key = new CastKey(key(mold), fluid);
        List<ModRecipes.CastingTableRecipe> cached = useCache ? TABLE.get(key) : null;
        if (cached == null) {
            List<ModRecipes.CastingTableRecipe> found = new ArrayList<>();
            for (var holder : manager.getAllRecipesFor(ModRecipes.CASTING_TABLE_TYPE.get())) {
                ModRecipes.CastingTableRecipe recipe = holder.value();
                boolean moldMatches = (recipe.cast().isEmpty() && mold.isEmpty()) || recipe.cast().test(mold);
                if (moldMatches && recipe.fluid().getFluid() == fluid) found.add(recipe);
            }
            cached = List.copyOf(found);
            if (useCache) {
                limit(TABLE);
                TABLE.put(key, cached);
            }
        }
        return cached;
    }

    @Nullable
    public static ModRecipes.CastingTableRecipe table(@Nullable Level level, ItemStack mold, Fluid fluid) {
        RecipeManager manager = validate(level);
        if (manager == null) return null;
        List<ModRecipes.CastingTableRecipe> candidates = tableCandidates(manager, mold, fluid, !level.isClientSide());
        return candidates.isEmpty() ? null : candidates.get(0);
    }

    @Nullable
    public static ModRecipes.CastingTableRecipe table(@Nullable Level level, ItemStack mold, Fluid fluid, int amount) {
        RecipeManager manager = validate(level);
        if (manager == null) return null;
        for (ModRecipes.CastingTableRecipe recipe : tableCandidates(manager, mold, fluid, !level.isClientSide())) {
            if (amount >= recipe.fluid().getAmount()) return recipe;
        }
        return null;
    }

    private static List<ModRecipes.CastingBasinRecipe> basinCandidates(RecipeManager manager, ItemStack cast, Fluid fluid, boolean useCache) {
        CastKey key = new CastKey(key(cast), fluid);
        List<ModRecipes.CastingBasinRecipe> cached = useCache ? BASIN.get(key) : null;
        if (cached == null) {
            List<ModRecipes.CastingBasinRecipe> found = new ArrayList<>();
            for (var holder : manager.getAllRecipesFor(ModRecipes.CASTING_BASIN_TYPE.get())) {
                ModRecipes.CastingBasinRecipe recipe = holder.value();
                if (recipe.matchesCast(cast) && recipe.input().getFluid() == fluid) found.add(recipe);
            }
            cached = List.copyOf(found);
            if (useCache) {
                limit(BASIN);
                BASIN.put(key, cached);
            }
        }
        return cached;
    }

    @Nullable
    public static ModRecipes.CastingBasinRecipe basin(@Nullable Level level, ItemStack cast, Fluid fluid) {
        RecipeManager manager = validate(level);
        if (manager == null) return null;
        List<ModRecipes.CastingBasinRecipe> candidates = basinCandidates(manager, cast, fluid, !level.isClientSide());
        return candidates.isEmpty() ? null : candidates.get(0);
    }

    @Nullable
    public static ModRecipes.CastingBasinRecipe basin(@Nullable Level level, ItemStack cast, Fluid fluid, int amount) {
        RecipeManager manager = validate(level);
        if (manager == null) return null;
        for (ModRecipes.CastingBasinRecipe recipe : basinCandidates(manager, cast, fluid, !level.isClientSide())) {
            if (amount >= recipe.input().getAmount()) return recipe;
        }
        return null;
    }
}

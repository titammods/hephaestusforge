package com.titammods.datagen;

import com.titammods.TitamMods;
import com.titammods.setup.ModFluids;
import com.titammods.setup.ModItems;
import com.titammods.setup.ModRecipes;
import com.titammods.setup.ModBlocks;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.serialization.JsonOps;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.neoforged.neoforge.common.conditions.ICondition;
import net.neoforged.neoforge.common.conditions.IConditionBuilder;
import net.neoforged.neoforge.common.conditions.WithConditions;
import net.neoforged.neoforge.fluids.FluidStack;
import com.titammods.registry.HephaestusFluids;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public class ModRecipeProvider extends RecipeProvider implements IConditionBuilder {

    private static final String EXTERNAL_FLUID_PLACEHOLDER = BuiltInRegistries.FLUID.getKey(Fluids.WATER).toString();

    private final PackOutput.PathProvider externalFluidRecipePath;
    private final Map<ResourceLocation, ResourceLocation> externalFluidRecipes = new HashMap<>();
    private final List<CompletableFuture<?>> externalFluidFutures = new ArrayList<>();
    private CachedOutput externalFluidCache;
    private HolderLookup.Provider externalFluidLookup;

    public ModRecipeProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries);
        this.externalFluidRecipePath = output.createRegistryElementsPathProvider(Registries.RECIPE);
    }

    @Override
    protected CompletableFuture<?> run(CachedOutput cache, HolderLookup.Provider registries) {
        this.externalFluidCache = cache;
        this.externalFluidLookup = registries;
        this.externalFluidRecipes.clear();
        this.externalFluidFutures.clear();
        CompletableFuture<?> recipes = super.run(cache, registries);
        List<CompletableFuture<?>> all = new ArrayList<>(this.externalFluidFutures);
        all.add(recipes);
        return CompletableFuture.allOf(all.toArray(CompletableFuture[]::new));
    }

    @Override
    protected void buildRecipes(RecipeOutput output, HolderLookup.Provider registries) {
        buildRecipes(new RecipeOutput() {
            @Override
            public void accept(ResourceLocation id, Recipe<?> recipe, AdvancementHolder advancement, ICondition... conditions) {
                ResourceLocation fluid = externalFluidRecipes.get(id);
                if (fluid == null) {
                    output.accept(id, recipe, advancement, conditions);
                    return;
                }
                JsonElement json = Recipe.CONDITIONAL_CODEC.encodeStart(
                        externalFluidLookup.createSerializationContext(JsonOps.INSTANCE),
                        Optional.of(new WithConditions<Recipe<?>>(recipe, conditions))).getOrThrow();
                replaceExternalFluid(json, fluid.toString());
                externalFluidFutures.add(DataProvider.saveStable(externalFluidCache, json, externalFluidRecipePath.json(id)));
            }

            @Override
            public Advancement.Builder advancement() {
                return output.advancement();
            }
        });
    }

    private static void replaceExternalFluid(JsonElement element, String fluid) {
        if (element.isJsonObject()) {
            JsonObject object = element.getAsJsonObject();
            if (object.has("id") && object.has("amount") && EXTERNAL_FLUID_PLACEHOLDER.equals(object.get("id").getAsString())) {
                object.addProperty("id", fluid);
            }
            for (var entry : object.entrySet()) replaceExternalFluid(entry.getValue(), fluid);
        } else if (element.isJsonArray()) {
            for (JsonElement child : element.getAsJsonArray()) replaceExternalFluid(child, fluid);
        }
    }

    private FluidStack fluidStack(ResourceLocation fluidId, int amount, ResourceLocation recipeId) {
        Fluid fluid = BuiltInRegistries.FLUID.get(fluidId);
        if (fluid == Fluids.EMPTY) {
            externalFluidRecipes.put(recipeId, fluidId);
            fluid = Fluids.WATER;
        }
        return new FluidStack(fluid, amount);
    }

    @Override
    protected void buildRecipes(RecipeOutput output) {
        generateDecorative(output, ModBlocks.SEARED_STONE.get(), ModBlocks.SEARED_STONE_SLAB.get(), ModBlocks.SEARED_STONE_STAIRS.get(), ModBlocks.SEARED_STONE_WALL.get());
        generateDecorative(output, ModBlocks.SEARED_COBBLE.get(), ModBlocks.SEARED_COBBLE_SLAB.get(), ModBlocks.SEARED_COBBLE_STAIRS.get(), ModBlocks.SEARED_COBBLE_WALL.get());
        generateDecorative(output, ModBlocks.SEARED_PAVER.get(), ModBlocks.SEARED_PAVER_SLAB.get(), ModBlocks.SEARED_PAVER_STAIRS.get(), ModBlocks.SEARED_PAVER_WALL.get());
        generateDecorative(output, ModBlocks.SEARED_BRICKS.get(), ModBlocks.SEARED_BRICKS_SLAB.get(), ModBlocks.SEARED_BRICKS_STAIRS.get(), ModBlocks.SEARED_BRICKS_WALL.get());
        generateDecorative(output, ModBlocks.SEARED_CRACKED_BRICKS.get(), ModBlocks.SEARED_CRACKED_BRICKS_SLAB.get(), ModBlocks.SEARED_CRACKED_BRICKS_STAIRS.get(), ModBlocks.SEARED_CRACKED_BRICKS_WALL.get());
        generateDecorative(output, ModBlocks.SEARED_FANCY_BRICKS.get(), ModBlocks.SEARED_FANCY_BRICKS_SLAB.get(), ModBlocks.SEARED_FANCY_BRICKS_STAIRS.get(), ModBlocks.SEARED_FANCY_BRICKS_WALL.get());
        generateDecorative(output, ModBlocks.SEARED_TRIANGLE_BRICKS.get(), ModBlocks.SEARED_TRIANGLE_BRICKS_SLAB.get(), ModBlocks.SEARED_TRIANGLE_BRICKS_STAIRS.get(), ModBlocks.SEARED_TRIANGLE_BRICKS_WALL.get());
        generateDecorative(output, ModBlocks.SEARED_CREEPER.get(), ModBlocks.SEARED_CREEPER_SLAB.get(), ModBlocks.SEARED_CREEPER_STAIRS.get(), ModBlocks.SEARED_CREEPER_WALL.get());
        generateDecorative(output, ModBlocks.SEARED_ROAD.get(), ModBlocks.SEARED_ROAD_SLAB.get(), ModBlocks.SEARED_ROAD_STAIRS.get(), ModBlocks.SEARED_ROAD_WALL.get());
        generateDecorative(output, ModBlocks.SEARED_SMALL_BRICKS.get(), ModBlocks.SEARED_SMALL_BRICKS_SLAB.get(), ModBlocks.SEARED_SMALL_BRICKS_STAIRS.get(), ModBlocks.SEARED_SMALL_BRICKS_WALL.get());
        generateDecorative(output, ModBlocks.SEARED_SQUARE_BRICKS.get(), ModBlocks.SEARED_SQUARE_BRICKS_SLAB.get(), ModBlocks.SEARED_SQUARE_BRICKS_STAIRS.get(), ModBlocks.SEARED_SQUARE_BRICKS_WALL.get());
        generateDecorative(output, ModBlocks.SEARED_TILE.get(), ModBlocks.SEARED_TILE_SLAB.get(), ModBlocks.SEARED_TILE_STAIRS.get(), ModBlocks.SEARED_TILE_WALL.get());

        createCastRecipe(output, "ingots", ModItems.INGOT_CAST.get(), "ingot_cast");
        createCastRecipe(output, "nuggets", ModItems.NUGGET_CAST.get(), "nugget_cast");
        createCastRecipe(output, "gems", ModItems.GEM_CAST.get(), "gem_cast");
        createCastRecipe(output, "plates", ModItems.PLATE_CAST.get(), "plate_cast");
        createCastRecipe(output, "gears", ModItems.GEAR_CAST.get(), "gear_cast");
        createRodCastRecipe(output);

        registerMetal(output, HephaestusFluids.SETS.get(HephaestusFluids.Material.MOLTEN_IRON).source.get(), 900, "iron", Items.IRON_BLOCK, Items.IRON_INGOT, Items.RAW_IRON, Items.IRON_NUGGET, null, null, null, null, "");
        registerMetal(output, HephaestusFluids.SETS.get(HephaestusFluids.Material.MOLTEN_GOLD).source.get(), 900, "gold", Items.GOLD_BLOCK, Items.GOLD_INGOT, Items.RAW_GOLD, Items.GOLD_NUGGET, null, null, null, null, "");
        registerMetal(output, HephaestusFluids.SETS.get(HephaestusFluids.Material.MOLTEN_COPPER).source.get(), 900, "copper", Items.COPPER_BLOCK, Items.COPPER_INGOT, Items.RAW_COPPER, null, null, null, null, null, "");
        registerMetal(output, HephaestusFluids.SETS.get(HephaestusFluids.Material.MOLTEN_STEEL).source.get(), 900, "steel", ModBlocks.STEEL_BLOCK.get(), ModItems.STEEL_INGOT.get(), ModItems.RAW_STEEL.get(), ModItems.STEEL_NUGGET.get(), ModItems.STEEL_POWDER.get(), null, null, null, "");
        registerMetal(output, HephaestusFluids.SETS.get(HephaestusFluids.Material.MOLTEN_NETHERITE).source.get(), 1500, "netherite", Items.NETHERITE_BLOCK, Items.NETHERITE_INGOT, null, null, null, null, null, null, "");

        registerMetal(output, ModFluids.MOLTEN_COBALT.source.get(), 1100, "cobalt", ModBlocks.COBALT_BLOCK.get(), ModItems.COBALT_INGOT.get(), ModItems.RAW_COBALT.get(), ModItems.COBALT_NUGGET.get(), ModItems.COBALT_POWDER.get(), null, null, null, "");

        registerGem(output, ModFluids.MOLTEN_DIAMOND.source.get(), 1400, "diamond", "storage_blocks/diamond", "gems/diamond", Items.DIAMOND_BLOCK, Items.DIAMOND);
        registerGem(output, ModFluids.MOLTEN_EMERALD.source.get(), 1200, "emerald", "storage_blocks/emerald", "gems/emerald", Items.EMERALD_BLOCK, Items.EMERALD);
        registerGem(output, ModFluids.MOLTEN_AMETHYST.source.get(), 1000, "amethyst", "", "gems/amethyst", Items.AMETHYST_BLOCK, Items.AMETHYST_SHARD, 360);
        registerGem(output, ModFluids.MOLTEN_QUARTZ.source.get(), 800, "quartz", "", "gems/quartz", Items.QUARTZ_BLOCK, Items.QUARTZ, 360);

        addMeltingItem(output, Items.GLOWSTONE,       HephaestusFluids.SETS.get(HephaestusFluids.Material.MOLTEN_GLOWSTONE).source.get(), 200, 800,  120, "misc/glowstone/block");
        addMeltingItem(output, Items.GLOWSTONE_DUST,  HephaestusFluids.SETS.get(HephaestusFluids.Material.MOLTEN_GLOWSTONE).source.get(),  50, 800,  60,  "misc/glowstone/dust");
        addCastingBasin(output, HephaestusFluids.SETS.get(HephaestusFluids.Material.MOLTEN_GLOWSTONE).source.get(), 200, Items.GLOWSTONE,     120, "misc/glowstone/block");
        addMeltingItem(output, Items.REDSTONE_BLOCK, HephaestusFluids.SETS.get(HephaestusFluids.Material.MOLTEN_REDSTONE).source.get(), 90, 600, 120, "misc/redstone/block");
        addMeltingItem(output, Items.REDSTONE,       HephaestusFluids.SETS.get(HephaestusFluids.Material.MOLTEN_REDSTONE).source.get(), 10, 600, 40, "misc/redstone/dust");
        addCastingBasin(output, HephaestusFluids.SETS.get(HephaestusFluids.Material.MOLTEN_REDSTONE).source.get(), 90, Items.REDSTONE_BLOCK, 120, "misc/redstone/block");
        addMeltingItem(output, Items.OBSIDIAN, HephaestusFluids.SETS.get(HephaestusFluids.Material.MOLTEN_OBSIDIAN).source.get(), 288, 1400, 200, "misc/obsidian");
        addCastingBasin(output, HephaestusFluids.SETS.get(HephaestusFluids.Material.MOLTEN_OBSIDIAN).source.get(), 288, Items.OBSIDIAN, 200, "misc/obsidian");
        addMeltingItem(output, Items.GLASS, HephaestusFluids.SETS.get(HephaestusFluids.Material.MOLTEN_GLASS).source.get(), 250, 1000, 100, "misc/glass_from_block");
        addMeltingItem(output, Items.GLASS_PANE, HephaestusFluids.SETS.get(HephaestusFluids.Material.MOLTEN_GLASS).source.get(), 90, 1000, 60, "misc/glass_from_pane");
        addMeltingItem(output, Items.SAND, HephaestusFluids.SETS.get(HephaestusFluids.Material.MOLTEN_GLASS).source.get(), 250, 1000, 100, "misc/glass_from_sand");
        addCastingBasin(output, HephaestusFluids.SETS.get(HephaestusFluids.Material.MOLTEN_GLASS).source.get(), 250, ModBlocks.CLEAR_GLASS.get(), 100, "misc/glass");
        addMeltingItem(output, Items.LAPIS_LAZULI, HephaestusFluids.SETS.get(HephaestusFluids.Material.MOLTEN_LAPIS).source.get(), 10, 900, 40, "misc/lapis/gem");
        addMeltingTag(output, "storage_blocks/lapis", HephaestusFluids.SETS.get(HephaestusFluids.Material.MOLTEN_LAPIS).source.get(), 90, 900, 120, "misc/lapis/block");
        addCastingBasin(output, HephaestusFluids.SETS.get(HephaestusFluids.Material.MOLTEN_LAPIS).source.get(), 90, Items.LAPIS_BLOCK, 120, "misc/lapis/block");
        addMeltingItem(output, Items.ENDER_PEARL, HephaestusFluids.SETS.get(HephaestusFluids.Material.MOLTEN_ENDER).source.get(), 90, 1000, 100, "misc/ender/pearl");
        addMeltingItem(output, Items.ENDER_EYE, HephaestusFluids.SETS.get(HephaestusFluids.Material.MOLTEN_ENDER).source.get(), 90, 1000, 100, "misc/ender/eye");
        addCastingTable(output, HephaestusFluids.SETS.get(HephaestusFluids.Material.MOLTEN_ENDER).source.get(), 90, ModItems.GEM_CAST.get(), false, Items.ENDER_PEARL, 60, "misc/ender/pearl");
        addMeltingItem(output, Items.ANCIENT_DEBRIS, HephaestusFluids.SETS.get(HephaestusFluids.Material.MOLTEN_ANCIENT_DEBRIS).source.get(), 90, 2000, 300, "misc/ancient_debris");
        addMeltingItem(output, Items.NETHERITE_SCRAP, HephaestusFluids.SETS.get(HephaestusFluids.Material.MOLTEN_ANCIENT_DEBRIS).source.get(), 90, 2000, 200, "misc/ancient_debris_from_scrap");
        addMeltingItem(output, Items.NETHERITE_INGOT, HephaestusFluids.SETS.get(HephaestusFluids.Material.MOLTEN_NETHERITE).source.get(), 90, 2000, 200, "misc/netherite_from_ingot");
        addMeltingItem(output, Items.SHULKER_SHELL, HephaestusFluids.SETS.get(HephaestusFluids.Material.MOLTEN_SHULKER_SHELL).source.get(), 90, 1200, 120, "misc/shulker_shell");
        addCastingTable(output, HephaestusFluids.SETS.get(HephaestusFluids.Material.MOLTEN_SHULKER_SHELL).source.get(), 90, null, false, Items.SHULKER_SHELL, 120, "misc/shulker_shell");
        addMeltingItem(output, Items.SLIME_BALL,  HephaestusFluids.SETS.get(HephaestusFluids.Material.MOLTEN_SLIME).source.get(), 50,  300, 60, "misc/slime/ball");
        addMeltingItem(output, Items.SLIME_BLOCK, HephaestusFluids.SETS.get(HephaestusFluids.Material.MOLTEN_SLIME).source.get(), 450, 300, 120, "misc/slime/block");
        addCastingBasin(output, HephaestusFluids.SETS.get(HephaestusFluids.Material.MOLTEN_SLIME).source.get(), 450, Items.SLIME_BLOCK, 120, "misc/slime/block");
        addMeltingItem(output, Items.MAGMA_CREAM, HephaestusFluids.SETS.get(HephaestusFluids.Material.MOLTEN_MAGMA_CREAM).source.get(), 90, 700, 80, "misc/magma_cream");
        addMeltingItem(output, Items.MAGMA_BLOCK, net.minecraft.world.level.material.Fluids.LAVA, 250, 700, 120, "misc/magma_block");
        addCastingTable(output, HephaestusFluids.SETS.get(HephaestusFluids.Material.MOLTEN_MAGMA_CREAM).source.get(), 90, ModItems.GEM_CAST.get(), false, Items.MAGMA_CREAM, 30, "misc/magma_cream");
        addMeltingItem(output, Items.HEAVY_CORE, HephaestusFluids.SETS.get(HephaestusFluids.Material.MOLTEN_HEAVY_CORE).source.get(), 810, 2000, 180, "misc/heavy_core");
        addCastingTable(output, HephaestusFluids.SETS.get(HephaestusFluids.Material.MOLTEN_HEAVY_CORE).source.get(), 810, null, false, Items.HEAVY_CORE, 180, "misc/heavy_core");
        addMeltingItem(output, Items.CHARCOAL, HephaestusFluids.SETS.get(HephaestusFluids.Material.MOLTEN_CARBON).source.get(), 90, 600, 100, "misc/carbon/charcoal");
        addMeltingItem(output, Items.COAL,     HephaestusFluids.SETS.get(HephaestusFluids.Material.MOLTEN_CARBON).source.get(), 90, 600, 100, "misc/carbon/coal");
        addMeltingTag(output, "storage_blocks/coal", HephaestusFluids.SETS.get(HephaestusFluids.Material.MOLTEN_CARBON).source.get(), 810, 600, 200, "misc/carbon/coal_block");
        addCastingBasin(output, HephaestusFluids.SETS.get(HephaestusFluids.Material.MOLTEN_CARBON).source.get(), 810, Items.COAL_BLOCK, 200, "misc/carbon/coal_block");
        addMeltingItem(output, Items.HONEYCOMB, HephaestusFluids.SETS.get(HephaestusFluids.Material.MOLTEN_WAX).source.get(), 50, 320, 60, "misc/wax_from_honeycomb");
        addMeltingItem(output, Items.HONEYCOMB_BLOCK, HephaestusFluids.SETS.get(HephaestusFluids.Material.MOLTEN_WAX).source.get(), 200, 320, 120, "misc/wax_from_block");
        RecipeOutput mekOutput = output.withConditions(modLoaded("mekanism"));
        addMeltingTag(mekOutput, "storage_blocks/refined_glowstone", HephaestusFluids.SETS.get(HephaestusFluids.Material.MOLTEN_REFINED_GLOWSTONE).source.get(), 810,  900, 120, "misc/refined_glowstone/block");
        addMeltingTag(mekOutput, "ingots/refined_glowstone",         HephaestusFluids.SETS.get(HephaestusFluids.Material.MOLTEN_REFINED_GLOWSTONE).source.get(),  90,  900,  60, "misc/refined_glowstone/ingot");
        addCastingTableById(mekOutput, net.minecraft.core.registries.BuiltInRegistries.FLUID.getKey(HephaestusFluids.SETS.get(HephaestusFluids.Material.MOLTEN_REFINED_GLOWSTONE).source.get()),  90, ModItems.INGOT_CAST.get(), false, ResourceLocation.fromNamespaceAndPath("mekanism", "ingot_refined_glowstone"),  60, "misc/refined_glowstone/ingot_cast");
        addCastingBasinById(mekOutput, net.minecraft.core.registries.BuiltInRegistries.FLUID.getKey(HephaestusFluids.SETS.get(HephaestusFluids.Material.MOLTEN_REFINED_GLOWSTONE).source.get()), 810, ResourceLocation.fromNamespaceAndPath("mekanism", "block_refined_glowstone"), 120, "misc/refined_glowstone/block_cast");
        addMeltingTag(mekOutput, "storage_blocks/refined_obsidian",  HephaestusFluids.SETS.get(HephaestusFluids.Material.MOLTEN_REFINED_OBSIDIAN).source.get(),  810, 1400, 120, "misc/refined_obsidian/block");
        addMeltingTag(mekOutput, "ingots/refined_obsidian",          HephaestusFluids.SETS.get(HephaestusFluids.Material.MOLTEN_REFINED_OBSIDIAN).source.get(),   90, 1400,  60, "misc/refined_obsidian/ingot");
        addCastingTableById(mekOutput, net.minecraft.core.registries.BuiltInRegistries.FLUID.getKey(HephaestusFluids.SETS.get(HephaestusFluids.Material.MOLTEN_REFINED_OBSIDIAN).source.get()),  90, ModItems.INGOT_CAST.get(), false, ResourceLocation.fromNamespaceAndPath("mekanism", "ingot_refined_obsidian"),  60, "misc/refined_obsidian/ingot_cast");
        addCastingBasinById(mekOutput, net.minecraft.core.registries.BuiltInRegistries.FLUID.getKey(HephaestusFluids.SETS.get(HephaestusFluids.Material.MOLTEN_REFINED_OBSIDIAN).source.get()), 810, ResourceLocation.fromNamespaceAndPath("mekanism", "block_refined_obsidian"), 120, "misc/refined_obsidian/block_cast");
        addMeltingItem(output, Items.PORKCHOP, HephaestusFluids.SETS.get(HephaestusFluids.Material.LIQUID_MEAT).source.get(), 40, 200, 60, "misc/meat/porkchop");
        addMeltingItem(output, Items.BEEF,     HephaestusFluids.SETS.get(HephaestusFluids.Material.LIQUID_MEAT).source.get(), 40, 200, 60, "misc/meat/beef");
        addMeltingItem(output, Items.CHICKEN,  HephaestusFluids.SETS.get(HephaestusFluids.Material.LIQUID_MEAT).source.get(), 40, 200, 60, "misc/meat/chicken");
        addMeltingItem(output, Items.MUTTON,   HephaestusFluids.SETS.get(HephaestusFluids.Material.LIQUID_MEAT).source.get(), 40, 200, 60, "misc/meat/mutton");
        addMeltingItem(output, Items.RABBIT,   HephaestusFluids.SETS.get(HephaestusFluids.Material.LIQUID_MEAT).source.get(), 40, 200, 60, "misc/meat/rabbit");
        addMeltingItem(output, Items.ROTTEN_FLESH, HephaestusFluids.SETS.get(HephaestusFluids.Material.LIQUID_MEAT).source.get(), 20, 200, 40, "misc/meat/rotten_flesh");

        addToolArmorMelting(output);

        RecipeOutput atoOutput = output.withConditions(modLoaded("alltheores"));
        registerExternalMetal(atoOutput, HephaestusFluids.SETS.get(HephaestusFluids.Material.MOLTEN_ALUMINUM).source.get(), 660, "aluminum", "alltheores", "alltheores_compat/");
        registerExternalMetal(atoOutput, HephaestusFluids.SETS.get(HephaestusFluids.Material.MOLTEN_BRASS).source.get(), 930, "brass", "alltheores", "alltheores_compat/");
        registerExternalMetal(atoOutput, HephaestusFluids.SETS.get(HephaestusFluids.Material.MOLTEN_BRONZE).source.get(), 950, "bronze", "alltheores", "alltheores_compat/");
        registerExternalMetal(atoOutput, HephaestusFluids.SETS.get(HephaestusFluids.Material.MOLTEN_CONSTANTAN).source.get(), 1220, "constantan", "alltheores", "alltheores_compat/");
        registerExternalMetal(atoOutput, HephaestusFluids.SETS.get(HephaestusFluids.Material.MOLTEN_ELECTRUM).source.get(), 1000, "electrum", "alltheores", "alltheores_compat/");
        registerExternalMetal(atoOutput, HephaestusFluids.SETS.get(HephaestusFluids.Material.MOLTEN_ENDERIUM).source.get(), 1450, "enderium", "alltheores", "alltheores_compat/");
        registerExternalMetal(atoOutput, HephaestusFluids.SETS.get(HephaestusFluids.Material.MOLTEN_INVAR).source.get(), 1420, "invar", "alltheores", "alltheores_compat/");
        registerExternalMetal(atoOutput, HephaestusFluids.SETS.get(HephaestusFluids.Material.MOLTEN_IRIDIUM).source.get(), 1440, "iridium", "alltheores", "alltheores_compat/");
        registerExternalMetal(atoOutput, HephaestusFluids.SETS.get(HephaestusFluids.Material.MOLTEN_LEAD).source.get(), 327, "lead", "alltheores", "alltheores_compat/");
        registerExternalMetal(atoOutput, HephaestusFluids.SETS.get(HephaestusFluids.Material.MOLTEN_LUMIUM).source.get(), 1000, "lumium", "alltheores", "alltheores_compat/");
        registerExternalMetal(atoOutput, HephaestusFluids.SETS.get(HephaestusFluids.Material.MOLTEN_NICKEL).source.get(), 1450, "nickel", "alltheores", "alltheores_compat/");
        registerExternalMetal(atoOutput, HephaestusFluids.SETS.get(HephaestusFluids.Material.MOLTEN_OSMIUM).source.get(), 1990, "osmium", "alltheores", "alltheores_compat/");
        registerExternalMetal(atoOutput, HephaestusFluids.SETS.get(HephaestusFluids.Material.MOLTEN_PLATINUM).source.get(), 1768, "platinum", "alltheores", "alltheores_compat/");
        registerExternalMetal(atoOutput, HephaestusFluids.SETS.get(HephaestusFluids.Material.MOLTEN_SIGNALUM).source.get(), 1000, "signalum", "alltheores", "alltheores_compat/");
        registerExternalMetal(atoOutput, HephaestusFluids.SETS.get(HephaestusFluids.Material.MOLTEN_SILVER).source.get(), 960, "silver", "alltheores", "alltheores_compat/");
        registerExternalMetal(atoOutput, HephaestusFluids.SETS.get(HephaestusFluids.Material.MOLTEN_TIN).source.get(), 230, "tin", "alltheores", "alltheores_compat/");
        registerExternalMetal(atoOutput, HephaestusFluids.SETS.get(HephaestusFluids.Material.MOLTEN_URANIUM).source.get(), 1130, "uranium", "alltheores", "alltheores_compat/");
        registerExternalMetal(atoOutput, HephaestusFluids.SETS.get(HephaestusFluids.Material.MOLTEN_ZINC).source.get(), 419, "zinc", "alltheores", "alltheores_compat/");

        registerFtbMaterialsCompat(output);
        registerFtbArmoryCompat(output);
        registerFtbIcCompat(output);
        registerGeoreCompat(output);

        registerAllTheModiumCompat(output);
        registerAe2Compat(output);
        registerArsMagicaLegacyCompat(output);
        registerCakeStickLibCompat(output);
        registerColossalReactorsCompat(output);
        registerEnderIoCompat(output);
        registerEnderIoEvolutionCompat(output);
        registerEnergizedPowerCompat(output);
        registerEvilCraftCompat(output);
        registerExtendedAeCompat(output);
        registerIronsJewelryCompat(output);
        registerJustDireThingsCompat(output);
        registerLtxiCompat(output);
        registerMysticalAgradditionsCompat(output);
        registerNeoVitaeCompat(output);
        registerNodeworksCompat(output);
        registerOccultismCompat(output);
        registerOritechCompat(output);
        registerStellarisCompat(output);
        registerThaumaturgeCompat(output);
        registerXyCraftWorldCompat(output);

        addAlloyRecipe(output,
                List.of(
                        new FluidStack(HephaestusFluids.SETS.get(HephaestusFluids.Material.MOLTEN_COPPER).source.get(), 100),
                        new FluidStack(HephaestusFluids.SETS.get(HephaestusFluids.Material.MOLTEN_ZINC).source.get(), 100)
                ),
                new FluidStack(HephaestusFluids.SETS.get(HephaestusFluids.Material.MOLTEN_BRASS).source.get(), 200),
                650,
                "brass");
        addAlloyRecipe(output,
                List.of(
                        new FluidStack(HephaestusFluids.SETS.get(HephaestusFluids.Material.MOLTEN_COPPER).source.get(), 300),
                        new FluidStack(HephaestusFluids.SETS.get(HephaestusFluids.Material.MOLTEN_TIN).source.get(), 100)
                ),
                new FluidStack(HephaestusFluids.SETS.get(HephaestusFluids.Material.MOLTEN_BRONZE).source.get(), 400),
                700,
                "bronze");
        addAlloyRecipe(output,
                List.of(
                        new FluidStack(HephaestusFluids.SETS.get(HephaestusFluids.Material.MOLTEN_GOLD).source.get(), 100),
                        new FluidStack(HephaestusFluids.SETS.get(HephaestusFluids.Material.MOLTEN_SILVER).source.get(), 100)
                ),
                new FluidStack(HephaestusFluids.SETS.get(HephaestusFluids.Material.MOLTEN_ELECTRUM).source.get(), 200),
                760,
                "electrum");
        addAlloyRecipe(output,
                List.of(
                        new FluidStack(HephaestusFluids.SETS.get(HephaestusFluids.Material.MOLTEN_IRON).source.get(), 200),
                        new FluidStack(HephaestusFluids.SETS.get(HephaestusFluids.Material.MOLTEN_NICKEL).source.get(), 100)
                ),
                new FluidStack(HephaestusFluids.SETS.get(HephaestusFluids.Material.MOLTEN_INVAR).source.get(), 300),
                810,
                "invar");
        addAlloyRecipe(output,
                List.of(
                        new FluidStack(HephaestusFluids.SETS.get(HephaestusFluids.Material.MOLTEN_COPPER).source.get(), 100),
                        new FluidStack(HephaestusFluids.SETS.get(HephaestusFluids.Material.MOLTEN_NICKEL).source.get(), 100)
                ),
                new FluidStack(HephaestusFluids.SETS.get(HephaestusFluids.Material.MOLTEN_CONSTANTAN).source.get(), 200),
                920,
                "constantan");
        addAlloyRecipe(output,
                List.of(
                        new FluidStack(HephaestusFluids.SETS.get(HephaestusFluids.Material.MOLTEN_ANCIENT_DEBRIS).source.get(), 270),
                        new FluidStack(HephaestusFluids.SETS.get(HephaestusFluids.Material.MOLTEN_GOLD).source.get(), 270)
                ),
                new FluidStack(HephaestusFluids.SETS.get(HephaestusFluids.Material.MOLTEN_NETHERITE).source.get(), 270),
                2000,
                "netherite");

        addEntityMelting(output, net.minecraft.world.entity.EntityType.BLAZE,
                new FluidStack(ModFluids.MOLTEN_BLAZE.source.get(), 45), 2, "blaze");
        addEntityMelting(output, net.minecraft.world.entity.EntityType.IRON_GOLEM,
                new FluidStack(HephaestusFluids.SETS.get(HephaestusFluids.Material.MOLTEN_IRON).source.get(), 36), 4, "iron_golem");

    }

    private FluidStack fuelForTemp(int temp) {
        if (temp <= 1000) {
            return new FluidStack(Fluids.LAVA, 50);
        } else {
            return new FluidStack(ModFluids.MOLTEN_BLAZE.source.get(), 50);
        }
    }

    private void registerExternalMetal(RecipeOutput output, Fluid fluid, int temp, String name, String modid, String prefix) {
        ItemLike block = getExternalItem(modid, name + "_block");
        ItemLike ingot = getExternalItem(modid, name + "_ingot");
        ItemLike raw = getExternalItem(modid, "raw_" + name);
        ItemLike nugget = getExternalItem(modid, name + "_nugget");
        ItemLike dust = getExternalItem(modid, name + "_dust");
        ItemLike plate = getExternalItem(modid, name + "_plate");
        ItemLike gear = getExternalItem(modid, name + "_gear");
        ItemLike rod = getExternalItem(modid, name + "_rod");
        registerMetal(output, fluid, temp, name, block, ingot, raw, nugget, dust, plate, gear, rod, prefix);
    }

    private void registerExternalVanillaSupplements(RecipeOutput output, String name, Fluid fluid, int temp, String modid, String prefix) {
        int baseTime = 100;
        ItemLike dust = getExternalItem(modid, name + "_dust");
        ItemLike plate = getExternalItem(modid, name + "_plate");
        ItemLike gear = getExternalItem(modid, name + "_gear");
        ItemLike rod = getExternalItem(modid, name + "_rod");

        if (dust != null) addMeltingTag(output, "dusts/" + name, fluid, 90, temp, baseTime, prefix + "vanilla/" + name + "_dust");
        if (plate != null) {
            addMeltingTag(output, "plates/" + name, fluid, 90, temp, baseTime, prefix + "vanilla/" + name + "_plate");
            addCastingTable(output, fluid, 90, ModItems.PLATE_CAST.get(), false, plate, baseTime, prefix + "vanilla/" + name + "_plate_cast"); }
        if (gear != null) {
            addMeltingTag(output, "gears/" + name, fluid, 360, temp, baseTime * 2, prefix + "vanilla/" + name + "_gear");
            addCastingTable(output, fluid, 360, ModItems.GEAR_CAST.get(), false, gear, baseTime * 2, prefix + "vanilla/" + name + "_gear_cast"); }
        if (rod != null) {
            addMeltingTag(output, "rods/" + name, fluid, 45, temp, baseTime / 2, prefix + "vanilla/" + name + "_rod");
            addCastingTable(output, fluid, 45, ModItems.ROD_CAST.get(), false, rod, baseTime / 2, prefix + "vanilla/" + name + "_rod_cast"); }
    }

    private ItemLike getExternalItem(String modid, String path) {
        ItemLike item = BuiltInRegistries.ITEM.get(ResourceLocation.fromNamespaceAndPath(modid, path));
        return item == Items.AIR ? null : item;
    }

    private void addCastingTableById(RecipeOutput output, ResourceLocation fluidId, int fluidAmount,
                                     ItemLike castItem, boolean consumesCast,
                                     ResourceLocation resultId, int time, String savePath) {
        ResourceLocation recipeId = ResourceLocation.fromNamespaceAndPath(TitamMods.MODID, "smeltery/casting/table/" + savePath);
        Ingredient castIngredient = castItem == null ? Ingredient.EMPTY : Ingredient.of(castItem);
        ModRecipes.CastingTableRecipe recipe = new ModRecipes.CastingTableRecipe(
                castIngredient, consumesCast,
                fluidStack(fluidId, fluidAmount, recipeId),
                resultId, 1, time);
        output.accept(recipeId, recipe, null);
    }

    private void addCastingBasinById(RecipeOutput output, ResourceLocation fluidId, int fluidAmount,
                                     ResourceLocation resultId, int time, String savePath) {
        ResourceLocation recipeId = ResourceLocation.fromNamespaceAndPath(TitamMods.MODID, "smeltery/casting/basin/" + savePath);
        ModRecipes.CastingBasinRecipe recipe = new ModRecipes.CastingBasinRecipe(
                fluidStack(fluidId, fluidAmount, recipeId),
                resultId, 1, time);
        output.accept(recipeId, recipe, null);
    }

    private void addMeltingTagById(RecipeOutput output, String tagPath, ResourceLocation fluidId, int amount,
                                   int temperature, int time, String savePath) {
        addMeltingTagById(output, ResourceLocation.fromNamespaceAndPath("c", tagPath), fluidId, amount, temperature, time, savePath);
    }

    private void addMeltingTagById(RecipeOutput output, ResourceLocation tag, ResourceLocation fluidId, int amount,
                                   int temperature, int time, String savePath) {
        ResourceLocation recipeId = ResourceLocation.fromNamespaceAndPath(TitamMods.MODID, "smeltery/melting/" + savePath);
        Ingredient ingredient = Ingredient.of(ItemTags.create(tag));
        ModRecipes.MeltingRecipe recipe = new ModRecipes.MeltingRecipe(ingredient,
                fluidStack(fluidId, amount, recipeId), fuelForTemp(temperature), temperature, time);
        output.accept(recipeId, recipe, null);
    }

    private void registerMetalById(RecipeOutput output, ResourceLocation fluid, int temp, String name,
                                   ResourceLocation block, ResourceLocation ingot, ResourceLocation raw, ResourceLocation nugget,
                                   ResourceLocation dust, ResourceLocation plate, ResourceLocation gear, ResourceLocation rod,
                                   String prefix) {
        int bt = 100;
        String path = prefix + "metal/" + name + "/";

        if (block  != null) addMeltingTagById(output, "storage_blocks/" + name, fluid, 810, temp, bt * 2, path + "block");
        if (raw != null && !name.equals("steel") && !name.equals("brass"))
            addMeltingTagById(output, "storage_blocks/raw_" + name, fluid, 810, temp, (int)(bt * 2.5), path + "raw_block");
        if (ingot  != null) addMeltingTagById(output, "ingots/"  + name, fluid,  90, temp, bt,     path + "ingot");
        if (raw != null && !name.equals("brass"))
            addMeltingTagById(output, "raw_materials/" + name, fluid, 90, temp, (int)(bt * 1.5), path + "raw");
        if (nugget != null) addMeltingTagById(output, "nuggets/" + name, fluid,  10, temp, bt / 3, path + "nugget");
        if (dust   != null) addMeltingTagById(output, "dusts/"   + name, fluid,  90, temp, bt,     path + "dust");
        if (plate  != null) addMeltingTagById(output, "plates/"  + name, fluid,  90, temp, bt,     path + "plate");
        if (gear   != null) addMeltingTagById(output, "gears/"   + name, fluid, 360, temp, bt * 2, path + "gear");
        if (rod    != null) addMeltingTagById(output, "rods/"    + name, fluid,  45, temp, bt / 2, path + "rod");

        if (ingot  != null) addCastingTableById(output, fluid,  90, ModItems.INGOT_CAST.get(),  false, ingot,  bt,     path + "ingot_cast");
        if (nugget != null) addCastingTableById(output, fluid,  10, ModItems.NUGGET_CAST.get(), false, nugget, bt / 3, path + "nugget_cast");
        if (plate  != null) addCastingTableById(output, fluid,  90, ModItems.PLATE_CAST.get(),  false, plate,  bt,     path + "plate_cast");
        if (gear   != null) addCastingTableById(output, fluid, 360, ModItems.GEAR_CAST.get(),   false, gear,   bt * 2, path + "gear_cast");
        if (rod    != null) addCastingTableById(output, fluid,  45, ModItems.ROD_CAST.get(),    false, rod,    bt / 2, path + "rod_cast");
        if (block  != null) addCastingBasinById(output, fluid, 810, block, bt * 2, path + "block_cast");
    }

    private void registerGemById(RecipeOutput output, ResourceLocation fluid, int temp, String name,
                                 ResourceLocation block, ResourceLocation gem, String prefix) {
        int bt = 120;
        String path = prefix + "gem/" + name + "/";

        if (block != null) {
            addMeltingTagById(output, "storage_blocks/" + name, fluid, 810, temp, bt * 2, path + "block");
            addCastingBasinById(output, fluid, 810, block, bt * 2, path + "block_cast");
        }
        addMeltingTagById(output, "gems/" + name, fluid, 90, temp, bt, path + "gem");
        addCastingTableById(output, fluid, 90, ModItems.GEM_CAST.get(), false, gem, bt, path + "gem_cast");
    }

    private void registerExternalMetalById(RecipeOutput output, ResourceLocation fluid, String modid, int temp,
                                           String name, boolean hasRaw, String prefix) {
        registerMetalById(output, fluid, temp, name,
                id(modid, name + "_block"), id(modid, name + "_ingot"),
                hasRaw ? id(modid, "raw_" + name) : null,
                id(modid, name + "_nugget"), id(modid, name + "_dust"),
                id(modid, name + "_plate"), id(modid, name + "_gear"), id(modid, name + "_rod"),
                prefix);
    }

    private ResourceLocation moltenId(HephaestusFluids.Material material) {
        return ResourceLocation.fromNamespaceAndPath(TitamMods.MODID, "molten_" + material.name);
    }

    private ResourceLocation id(String namespace, String path) {
        return ResourceLocation.fromNamespaceAndPath(namespace, path);
    }

    private void registerFtbMaterialsCompat(RecipeOutput output) {
        RecipeOutput ftbOutput = output.withConditions(modLoaded("ftbmaterials"));
        String mod = "ftbmaterials", prefix = "ftbmaterials_compat/";

        registerExternalMetalById(ftbOutput, moltenId(HephaestusFluids.Material.MOLTEN_ALUMINUM),   mod,  660, "aluminum",   true,  prefix);
        registerExternalMetalById(ftbOutput, moltenId(HephaestusFluids.Material.MOLTEN_BRONZE),     mod,  950, "bronze",     false, prefix);
        registerExternalMetalById(ftbOutput, moltenId(HephaestusFluids.Material.MOLTEN_CONSTANTAN), mod, 1220, "constantan", false, prefix);
        registerExternalMetalById(ftbOutput, moltenId(HephaestusFluids.Material.MOLTEN_IRIDIUM),    mod, 2440, "iridium",    true,  prefix);
        registerExternalMetalById(ftbOutput, moltenId(HephaestusFluids.Material.MOLTEN_LEAD),       mod,  327, "lead",       true,  prefix);
        registerExternalMetalById(ftbOutput, moltenId(HephaestusFluids.Material.MOLTEN_NICKEL),     mod, 1000, "nickel",     true,  prefix);
        registerExternalMetalById(ftbOutput, moltenId(HephaestusFluids.Material.MOLTEN_PLATINUM),   mod, 1768, "platinum",   true,  prefix);
        registerExternalMetalById(ftbOutput, moltenId(HephaestusFluids.Material.MOLTEN_SILVER),     mod,  960, "silver",     true,  prefix);
        registerExternalMetalById(ftbOutput, moltenId(HephaestusFluids.Material.MOLTEN_TIN),        mod,  230, "tin",        true,  prefix);
        registerExternalMetalById(ftbOutput, moltenId(HephaestusFluids.Material.MOLTEN_URANIUM),    mod, 1130, "uranium",    true,  prefix);
        registerExternalMetalById(ftbOutput, moltenId(HephaestusFluids.Material.MOLTEN_ZINC),       mod,  419, "zinc",       true,  prefix);
        registerExternalMetalById(ftbOutput, moltenId(HephaestusFluids.Material.MOLTEN_BRASS),      mod,  930, "brass",      false, prefix);
        registerExternalMetalById(ftbOutput, moltenId(HephaestusFluids.Material.MOLTEN_ELECTRUM),   mod, 1000, "electrum",   false, prefix);
        registerExternalMetalById(ftbOutput, moltenId(HephaestusFluids.Material.MOLTEN_INVAR),      mod, 1000, "invar",      false, prefix);
        registerExternalMetalById(ftbOutput, moltenId(HephaestusFluids.Material.MOLTEN_CHROMIUM),   mod, 1000, "chromium",   true,  prefix);
        registerExternalMetalById(ftbOutput, moltenId(HephaestusFluids.Material.MOLTEN_TITANIUM),   mod, 1000, "titanium",   true,  prefix);
        registerExternalMetalById(ftbOutput.withConditions(modLoaded("colossal_reactors")),
                id("colossal_reactors", "molten_stainless_steel"), mod, 1000, "stainless_steel", false, prefix);

        int bt = 100;
        for (String name : new String[]{"copper", "gold", "iron"}) {
            ResourceLocation fluid = moltenId(HephaestusFluids.Material.valueOf("MOLTEN_" + name.toUpperCase()));
            String path = prefix + "metal/" + name + "/";

            addMeltingTagById(ftbOutput, "dusts/"  + name, fluid,  90, 900, bt,     path + "dust");
            addMeltingTagById(ftbOutput, "plates/" + name, fluid,  90, 900, bt,     path + "plate");
            addMeltingTagById(ftbOutput, "gears/"  + name, fluid, 360, 900, bt * 2, path + "gear");
            addMeltingTagById(ftbOutput, "rods/"   + name, fluid,  45, 900, bt / 2, path + "rod");

            addCastingTableById(ftbOutput, fluid,  90, ModItems.PLATE_CAST.get(), false, id(mod, name + "_plate"), bt,     path + "plate_cast");
            addCastingTableById(ftbOutput, fluid, 360, ModItems.GEAR_CAST.get(),  false, id(mod, name + "_gear"),  bt * 2, path + "gear_cast");
            addCastingTableById(ftbOutput, fluid,  45, ModItems.ROD_CAST.get(),   false, id(mod, name + "_rod"),   bt / 2, path + "rod_cast");
        }

        ResourceLocation steel = moltenId(HephaestusFluids.Material.MOLTEN_STEEL);
        String steelPath = prefix + "metal/steel/";
        addMeltingTagById(ftbOutput, "plates/steel", steel,  90, 1000, bt,     steelPath + "plate");
        addMeltingTagById(ftbOutput, "gears/steel",  steel, 360, 1000, bt * 2, steelPath + "gear");
        addMeltingTagById(ftbOutput, "rods/steel",   steel,  45, 1000, bt / 2, steelPath + "rod");
        addCastingTableById(ftbOutput, steel,  90, ModItems.PLATE_CAST.get(), false, id(mod, "steel_plate"), bt,     steelPath + "plate_cast");
        addCastingTableById(ftbOutput, steel, 360, ModItems.GEAR_CAST.get(),  false, id(mod, "steel_gear"),  bt * 2, steelPath + "gear_cast");
        addCastingTableById(ftbOutput, steel,  45, ModItems.ROD_CAST.get(),   false, id(mod, "steel_rod"),   bt / 2, steelPath + "rod_cast");

        ResourceLocation netherite = moltenId(HephaestusFluids.Material.MOLTEN_NETHERITE);
        String netheritePath = prefix + "metal/netherite/";
        addMeltingTagById(ftbOutput, "nuggets/netherite", netherite,  10, 2000, bt / 3, netheritePath + "nugget");
        addMeltingTagById(ftbOutput, "dusts/netherite",   netherite,  90, 2000, bt,     netheritePath + "dust");
        addMeltingTagById(ftbOutput, "plates/netherite",  netherite,  90, 2000, bt,     netheritePath + "plate");
        addMeltingTagById(ftbOutput, "gears/netherite",   netherite, 360, 2000, bt * 2, netheritePath + "gear");
        addMeltingTagById(ftbOutput, "rods/netherite",    netherite,  45, 2000, bt / 2, netheritePath + "rod");
        addCastingTableById(ftbOutput, netherite,  10, ModItems.NUGGET_CAST.get(), false, id(mod, "netherite_nugget"), bt / 3, netheritePath + "nugget_cast");
        addCastingTableById(ftbOutput, netherite,  90, ModItems.PLATE_CAST.get(),  false, id(mod, "netherite_plate"),  bt,     netheritePath + "plate_cast");
        addCastingTableById(ftbOutput, netherite, 360, ModItems.GEAR_CAST.get(),   false, id(mod, "netherite_gear"),   bt * 2, netheritePath + "gear_cast");
        addCastingTableById(ftbOutput, netherite,  45, ModItems.ROD_CAST.get(),    false, id(mod, "netherite_rod"),    bt / 2, netheritePath + "rod_cast");

        registerGemById(ftbOutput, moltenId(HephaestusFluids.Material.MOLTEN_DIMENSIONAL_SHARD), 1000, "dimensional_shard",
                id(mod, "dimensional_shard_block"), id(mod, "dimensional_shard_gem"), prefix);
        registerGemById(ftbOutput, moltenId(HephaestusFluids.Material.MOLTEN_RUBY), 1000, "ruby",
                id(mod, "ruby_block"), id(mod, "ruby_gem"), prefix);
        registerGemById(ftbOutput, moltenId(HephaestusFluids.Material.MOLTEN_SAPPHIRE), 1000, "sapphire",
                id(mod, "sapphire_block"), id(mod, "sapphire_gem"), prefix);

        addMeltingTagById(ftbOutput, "ingots/graphite", moltenId(HephaestusFluids.Material.MOLTEN_CARBON), 90, 700, 100,
                prefix + "misc/carbon_from_graphite");
    }

    private void registerFtbArmoryCompat(RecipeOutput output) {
        RecipeOutput armoryOutput = output.withConditions(modLoaded("ftbarmory"));
        String prefix = "ftbarmory_compat/";

        HephaestusFluids.Material[] metals = {
                HephaestusFluids.Material.MOLTEN_AETERNIUM,
                HephaestusFluids.Material.MOLTEN_ADAMANTITE,
                HephaestusFluids.Material.MOLTEN_AURICHALCUM
        };
        for (HephaestusFluids.Material metal : metals) {
            registerExternalMetalById(armoryOutput, moltenId(metal), "ftbarmory", metal.temperature, metal.name, true, prefix);
        }
    }

    private void registerFtbIcCompat(RecipeOutput output) {
        RecipeOutput icOutput = output.withConditions(modLoaded("ftbic"));
        String prefix = "ftbic_compat/";

        registerExternalMetalById(icOutput, moltenId(HephaestusFluids.Material.MOLTEN_ENDERIUM), "ftbic", 1450, "enderium", false, prefix);
    }

    private void registerAllTheModiumCompat(RecipeOutput output) {
        RecipeOutput modOutput = output.withConditions(modLoaded("allthemodium"));
        String prefix = "allthemodium_compat/";

        HephaestusFluids.Material[] metals = {
                HephaestusFluids.Material.MOLTEN_ALLTHEMODIUM,
                HephaestusFluids.Material.MOLTEN_VIBRANIUM,
                HephaestusFluids.Material.MOLTEN_UNOBTAINIUM
        };
        for (HephaestusFluids.Material metal : metals) {
            registerExternalMetalById(modOutput, moltenId(metal), "allthemodium", metal.temperature, metal.name, true, prefix);
        }
    }

    private void registerAe2Compat(RecipeOutput output) {
        RecipeOutput modOutput = output.withConditions(modLoaded("ae2"));
        String mod = "ae2", prefix = "ae2_compat/";

        registerGemById(modOutput, moltenId(HephaestusFluids.Material.MOLTEN_CERTUS_QUARTZ), 800, "certus_quartz",
                null, id(mod, "certus_quartz_crystal"), prefix);
        registerGemById(modOutput, moltenId(HephaestusFluids.Material.MOLTEN_FLUIX), 1000, "fluix",
                null, id(mod, "fluix_crystal"), prefix);
    }

    private void registerArsMagicaLegacyCompat(RecipeOutput output) {
        RecipeOutput modOutput = output.withConditions(modLoaded("arsmagicalegacy"));
        String mod = "arsmagicalegacy", prefix = "arsmagicalegacy_compat/";

        registerGemById(modOutput, moltenId(HephaestusFluids.Material.MOLTEN_MOONSTONE), 800, "moonstone",
                id(mod, "moonstone_block"), id(mod, "moonstone"), prefix);
        registerGemById(modOutput, moltenId(HephaestusFluids.Material.MOLTEN_TOPAZ), 1000, "topaz",
                id(mod, "topaz_block"), id(mod, "topaz"), prefix);
    }

    private void registerCakeStickLibCompat(RecipeOutput output) {
        RecipeOutput modOutput = output.withConditions(modLoaded("cakesticklib"));
        String mod = "cakesticklib", prefix = "cakesticklib_compat/";

        registerMetalById(modOutput, moltenId(HephaestusFluids.Material.MOLTEN_WROUGHT_IRON), 800, "wrought_iron",
                id(mod, "wrought_iron_block"), id(mod, "wrought_iron_ingot"), null,
                id(mod, "wrought_iron_nugget"), null, null, null, null, prefix);
    }

    private void registerColossalReactorsCompat(RecipeOutput output) {
        RecipeOutput modOutput = output.withConditions(modLoaded("colossal_reactors"));
        String mod = "colossal_reactors", prefix = "colossal_reactors_compat/";

        registerMetalById(modOutput, id(mod, "molten_tough_alloy"), 1000, "tough_alloy",
                id(mod, "tough_alloy_block"), id(mod, "tough_alloy_ingot"), null,
                null, null, null, null, null, prefix);
    }

    private void registerEnderIoCompat(RecipeOutput output) {
        RecipeOutput modOutput = output.withConditions(modLoaded("enderio"));
        String mod = "enderio", prefix = "enderio_compat/";

        HephaestusFluids.Material[] alloys = {
                HephaestusFluids.Material.MOLTEN_CONDUCTIVE_ALLOY,
                HephaestusFluids.Material.MOLTEN_DARK_STEEL,
                HephaestusFluids.Material.MOLTEN_END_STEEL,
                HephaestusFluids.Material.MOLTEN_ENERGETIC_ALLOY,
                HephaestusFluids.Material.MOLTEN_PULSATING_ALLOY,
                HephaestusFluids.Material.MOLTEN_REDSTONE_ALLOY,
                HephaestusFluids.Material.MOLTEN_SOULARIUM,
                HephaestusFluids.Material.MOLTEN_VIBRANT_ALLOY
        };
        int[] temps = {800, 1000, 1600, 800, 800, 800, 800, 1000};
        for (int i = 0; i < alloys.length; i++) {
            String name = alloys[i].name;
            registerMetalById(modOutput, moltenId(alloys[i]), temps[i], name,
                    id(mod, name + "_block"), id(mod, name + "_ingot"), null,
                    id(mod, name + "_nugget"), null, null, null, null, prefix);
        }
    }

    private void registerEnderIoEvolutionCompat(RecipeOutput output) {
        RecipeOutput modOutput = output.withConditions(modLoaded("enderio_evolution"));
        String mod = "enderio_evolution", prefix = "enderio_evolution_compat/";

        HephaestusFluids.Material[] alloys = {
                HephaestusFluids.Material.MOLTEN_ENERGETIC_SILVER,
                HephaestusFluids.Material.MOLTEN_VIVID_ALLOY
        };
        int[] temps = {800, 1000};
        for (int i = 0; i < alloys.length; i++) {
            String name = alloys[i].name;
            registerMetalById(modOutput, moltenId(alloys[i]), temps[i], name,
                    id(mod, name + "_block"), id(mod, name + "_ingot"), null,
                    id(mod, name + "_nugget"), null, null, null, null, prefix);
        }
    }

    private void registerEnergizedPowerCompat(RecipeOutput output) {
        RecipeOutput modOutput = output.withConditions(modLoaded("energizedpower"));
        String mod = "energizedpower", prefix = "energizedpower_compat/";

        registerMetalById(modOutput, moltenId(HephaestusFluids.Material.MOLTEN_ADVANCED_ALLOY), 1000, "advanced_alloy",
                id(mod, "advanced_alloy_block"), id(mod, "advanced_alloy_ingot"), null,
                null, null, null, null, null, prefix);
        registerMetalById(modOutput, moltenId(HephaestusFluids.Material.MOLTEN_CRYSTALLIZED_ALLOY), 1000, "crystallized_alloy",
                null, id(mod, "crystallized_alloy_ingot"), null,
                null, null, null, null, null, prefix);
        registerMetalById(modOutput, moltenId(HephaestusFluids.Material.MOLTEN_ENERGIZED_ALLOY), 1000, "energized_alloy",
                null, id(mod, "energized_alloy_ingot"), null,
                null, null, null, null, null, prefix);
        registerMetalById(modOutput, moltenId(HephaestusFluids.Material.MOLTEN_ENERGIZED_COPPER), 800, "energized_copper",
                null, id(mod, "energized_copper_ingot"), null,
                null, null, null, null, null, prefix);
        registerMetalById(modOutput, moltenId(HephaestusFluids.Material.MOLTEN_ENERGIZED_GOLD), 800, "energized_gold",
                null, id(mod, "energized_gold_ingot"), null,
                null, null, null, null, null, prefix);
    }

    private void registerEvilCraftCompat(RecipeOutput output) {
        RecipeOutput modOutput = output.withConditions(modLoaded("evilcraft"));
        String mod = "evilcraft", prefix = "evilcraft_compat/";

        registerGemById(modOutput, moltenId(HephaestusFluids.Material.MOLTEN_DARK_GEM), 800, "dark_gem",
                id(mod, "dark_block"), id(mod, "dark_gem"), prefix);
    }

    private void registerExtendedAeCompat(RecipeOutput output) {
        RecipeOutput modOutput = output.withConditions(modLoaded("extendedae"));
        String mod = "extendedae", prefix = "extendedae_compat/";

        registerGemById(modOutput, moltenId(HephaestusFluids.Material.MOLTEN_ENTRO), 1000, "entro",
                null, id(mod, "entro_crystal"), prefix);
    }

    private void registerIronsJewelryCompat(RecipeOutput output) {
        RecipeOutput modOutput = output.withConditions(modLoaded("irons_jewelry"));
        String mod = "irons_jewelry", prefix = "irons_jewelry_compat/";

        registerGemById(modOutput, moltenId(HephaestusFluids.Material.MOLTEN_GARNET), 1000, "garnet",
                null, id(mod, "garnet"), prefix);
        registerGemById(modOutput, moltenId(HephaestusFluids.Material.MOLTEN_ONYX), 1000, "onyx",
                null, id(mod, "onyx"), prefix);
        registerGemById(modOutput, moltenId(HephaestusFluids.Material.MOLTEN_PERIDOT), 1000, "peridot",
                null, id(mod, "peridot"), prefix);
    }

    private void registerJustDireThingsCompat(RecipeOutput output) {
        RecipeOutput modOutput = output.withConditions(modLoaded("justdirethings"));
        String mod = "justdirethings", prefix = "justdirethings_compat/";

        registerMetalById(modOutput, moltenId(HephaestusFluids.Material.MOLTEN_BLAZEGOLD), 1000, "blazegold",
                null, id(mod, "blazegold_ingot"), null,
                null, null, null, null, null, prefix);
        registerMetalById(modOutput, moltenId(HephaestusFluids.Material.MOLTEN_ECLIPSE_ALLOY), 1600, "eclipsealloy",
                null, id(mod, "eclipsealloy_ingot"), null,
                null, null, null, null, null, prefix);
        registerMetalById(modOutput, moltenId(HephaestusFluids.Material.MOLTEN_FERRICORE), 800, "ferricore",
                null, id(mod, "ferricore_ingot"), null,
                null, null, null, null, null, prefix);
    }

    private void registerLtxiCompat(RecipeOutput output) {
        RecipeOutput modOutput = output.withConditions(modLoaded("ltxi"));
        String mod = "ltxi", prefix = "ltxi_compat/";

        registerMetalById(modOutput, moltenId(HephaestusFluids.Material.MOLTEN_NIOBIUM), 1000, "niobium",
                id(mod, "niobium_block"), id(mod, "niobium_ingot"), null,
                id(mod, "niobium_nugget"), null, null, null, null, prefix);
        registerMetalById(modOutput, moltenId(HephaestusFluids.Material.MOLTEN_SLATESTEEL), 1000, "slatesteel",
                id(mod, "slatesteel_block"), id(mod, "slatesteel_ingot"), null,
                id(mod, "slatesteel_nugget"), null, null, null, null, prefix);
        registerMetalById(modOutput, moltenId(HephaestusFluids.Material.MOLTEN_TUNGSTEN_SLATESTEEL), 1600, "tungsten_slatesteel",
                null, id(mod, "tungsten_slatesteel_ingot"), null,
                null, null, null, null, null, prefix);
        registerGemById(modOutput, moltenId(HephaestusFluids.Material.MOLTEN_OLIVINE), 800, "olivine",
                null, id(mod, "olivine"), prefix);
    }

    private void registerMysticalAgradditionsCompat(RecipeOutput output) {
        RecipeOutput modOutput = output.withConditions(modLoaded("mysticalagradditions"));
        String mod = "mysticalagradditions", prefix = "mysticalagradditions_compat/";

        String[] essences = {"prosperity", "soulium", "tertium", "prudentium",
                "imperium", "supremium", "awakened_supremium"};
        int[] temps       = {800, 800, 800, 800, 1000, 1000, 1600};
        for (int i = 0; i < essences.length; i++) {
            registerGemById(modOutput, id(mod, "molten_" + essences[i]), temps[i], essences[i],
                    null, id("mysticalagriculture", essences[i] + "_gemstone"), prefix);
        }
        registerGemById(modOutput, id(mod, "molten_insanium"), 1600, "insanium",
                null, id(mod, "insanium_gemstone"), prefix);
    }

    private void registerNeoVitaeCompat(RecipeOutput output) {
        RecipeOutput modOutput = output.withConditions(modLoaded("neovitae"));
        String mod = "neovitae", prefix = "neovitae_compat/";

        registerMetalById(modOutput, moltenId(HephaestusFluids.Material.MOLTEN_HELLFORGED), 1600, "hellforged",
                id(mod, "hellforged_block"), id(mod, "ingot_hellforged"), null,
                null, null, null, null, null, prefix);
    }

    private void registerNodeworksCompat(RecipeOutput output) {
        RecipeOutput modOutput = output.withConditions(modLoaded("nodeworks"));
        String mod = "nodeworks", prefix = "nodeworks_compat/";

        registerGemById(modOutput, moltenId(HephaestusFluids.Material.MOLTEN_CELESTINE), 800, "celestine",
                null, id(mod, "celestine_shard"), prefix);
    }

    private void registerOccultismCompat(RecipeOutput output) {
        RecipeOutput modOutput = output.withConditions(modLoaded("occultism"));
        String mod = "occultism", prefix = "occultism_compat/";

        registerMetalById(modOutput, moltenId(HephaestusFluids.Material.MOLTEN_IESNIUM), 1200, "iesnium",
                id(mod, "iesnium_block"), id(mod, "iesnium_ingot"), null,
                id(mod, "iesnium_nugget"), null, null, null, null, prefix);
        registerGemById(modOutput, moltenId(HephaestusFluids.Material.MOLTEN_SPIRIT_ATTUNED), 1000, "spirit_attuned",
                id(mod, "spirit_attuned_block"), id(mod, "spirit_attuned_gem"), prefix);
    }

    private void registerOritechCompat(RecipeOutput output) {
        RecipeOutput modOutput = output.withConditions(modLoaded("oritech"));
        String mod = "oritech", prefix = "oritech_compat/";

        registerMetalById(modOutput, moltenId(HephaestusFluids.Material.MOLTEN_ADAMANT), 1000, "adamant",
                id(mod, "adamant"), id(mod, "adamant_ingot"), null,
                null, null, null, null, null, prefix);
        registerMetalById(modOutput, moltenId(HephaestusFluids.Material.MOLTEN_DURATIUM), 1600, "duratium",
                id(mod, "duratium"), id(mod, "duratium_ingot"), null,
                null, null, null, null, null, prefix);
        registerMetalById(modOutput, moltenId(HephaestusFluids.Material.MOLTEN_PROMETHEUM), 1600, "prometheum",
                null, id(mod, "prometheum_ingot"), null,
                null, null, null, null, null, prefix);
        registerGemById(modOutput, moltenId(HephaestusFluids.Material.MOLTEN_FLUXITE), 1000, "fluxite",
                id(mod, "fluxite_block"), id(mod, "fluxite"), prefix);
    }

    private void registerStellarisCompat(RecipeOutput output) {
        RecipeOutput modOutput = output.withConditions(modLoaded("stellaris"));
        String mod = "stellaris", prefix = "stellaris_compat/";

        registerMetalById(modOutput, moltenId(HephaestusFluids.Material.MOLTEN_DESH), 1000, "desh",
                id(mod, "desh_block"), id(mod, "desh_ingot"), null,
                null, null, null, null, null, prefix);
    }

    private void registerThaumaturgeCompat(RecipeOutput output) {
        RecipeOutput modOutput = output.withConditions(modLoaded("thaumaturge"));
        String mod = "thaumaturge", prefix = "thaumaturge_compat/";

        registerMetalById(modOutput, moltenId(HephaestusFluids.Material.MOLTEN_THAUMIUM), 1000, "thaumium",
                id(mod, "metal_thaumium"), id(mod, "ingot_thaumium"), null,
                id(mod, "nugget_thaumium"), null, null, null, null, prefix);
        registerMetalById(modOutput, moltenId(HephaestusFluids.Material.MOLTEN_VOID_METAL), 1600, "void_metal",
                id(mod, "metal_void"), id(mod, "ingot_void"), null,
                id(mod, "nugget_void"), null, null, null, null, prefix);
    }

    private void registerXyCraftWorldCompat(RecipeOutput output) {
        RecipeOutput modOutput = output.withConditions(modLoaded("xycraft_world"));
        String mod = "xycraft_world", prefix = "xycraft_world_compat/";

        registerGemById(modOutput, moltenId(HephaestusFluids.Material.MOLTEN_XYCHORIUM), 800, "xychorium",
                null, id(mod, "xychorium_gem_blue"), prefix);
    }

    private void registerGeoreCompat(RecipeOutput output) {
        RecipeOutput modOutput = output.withConditions(modLoaded("geore"));
        String prefix = "geore_compat/";

        String[] names = {"aluminum", "copper", "gold", "iron", "lead", "nickel",
                "platinum", "silver", "tin", "uranium", "zinc"};
        int[] temps    = {660, 900, 900, 900, 327, 1000, 1768, 960, 230, 1130, 419};
        for (int i = 0; i < names.length; i++) {
            String name = names[i];
            addMeltingTagById(modOutput, id("geore", "geore_shards/" + name),
                    moltenId(HephaestusFluids.Material.valueOf("MOLTEN_" + name.toUpperCase())), 90, temps[i], 100,
                    prefix + "metal/" + name + "/shard");
        }
    }

    private void registerMetal(RecipeOutput output, Fluid fluid, int temp, String name,
                               ItemLike block, ItemLike ingot, ItemLike raw, ItemLike nugget, ItemLike dust,
                               ItemLike plate, ItemLike gear, ItemLike rod, String prefix) {
        int baseTime = 100;

        addMeltingTag(output, "storage_blocks/" + name, fluid, 810, temp, baseTime * 2, prefix + "metal/" + name + "/block");
        if (raw != null && !name.equals("steel") && !name.equals("brass")) {

            addMeltingTag(output, "storage_blocks/raw_" + name, fluid, 810, temp, (int)(baseTime * 2.5), prefix + "metal/" + name + "/raw_block"); }
        addMeltingTag(output, "ingots/" + name, fluid, 90, temp, baseTime, prefix + "metal/" + name + "/ingot");
        if (raw != null && !name.equals("brass")) {

            addMeltingTag(output, "raw_materials/" + name, fluid, 90, temp, (int)(baseTime * 1.5), prefix + "metal/" + name + "/raw"); }
        addMeltingTag(output, "nuggets/" + name, fluid, 10, temp, baseTime / 3, prefix + "metal/" + name + "/nugget");
        if (dust != null) addMeltingTag(output, "dusts/" + name, fluid, 90, temp, baseTime, prefix + "metal/" + name + "/dust");
        if (plate != null) addMeltingTag(output, "plates/" + name, fluid, 90, temp, baseTime, prefix + "metal/" + name + "/plate");
        if (gear != null) addMeltingTag(output, "gears/" + name, fluid, 360, temp, baseTime * 2, prefix + "metal/" + name + "/gear");
        if (rod != null) addMeltingTag(output, "rods/" + name, fluid, 45, temp, baseTime / 2, prefix + "metal/" + name + "/rod");
        if (block != null) addCastingBasin(output, fluid, 810, block, baseTime * 2, prefix + "metal/" + name + "/block");
        if (ingot != null) addCastingTable(output, fluid, 90, ModItems.INGOT_CAST.get(), false, ingot, baseTime, prefix + "metal/" + name + "/ingot_cast");
        if (nugget != null) addCastingTable(output, fluid, 10, ModItems.NUGGET_CAST.get(), false, nugget, baseTime / 3, prefix + "metal/" + name + "/nugget_cast");
        if (plate != null) addCastingTable(output, fluid, 90, ModItems.PLATE_CAST.get(), false, plate, baseTime, prefix + "metal/" + name + "/plate_cast");
        if (gear != null) addCastingTable(output, fluid, 360, ModItems.GEAR_CAST.get(), false, gear, baseTime * 2, prefix + "metal/" + name + "/gear_cast");
        if (rod != null) addCastingTable(output, fluid, 45, ModItems.ROD_CAST.get(), false, rod, baseTime / 2, prefix + "metal/" + name + "/rod_cast");
    }

    private void registerGem(RecipeOutput output, Fluid fluid, int temp, String name, String blockTag, String gemTag, ItemLike block, ItemLike gem) {
        registerGem(output, fluid, temp, name, blockTag, gemTag, block, gem, 810);
    }

    private void registerGem(RecipeOutput output, Fluid fluid, int temp, String name, String blockTag, String gemTag, ItemLike block, ItemLike gem, int blockAmount) {
        int baseTime = 120;
        if (blockTag != null && !blockTag.isEmpty()) {
            addMeltingTag(output, blockTag, fluid, blockAmount, temp, baseTime * 2, "gem/" + name + "/block"); } else if (block != null) {
            addMeltingItem(output, block, fluid, blockAmount, temp, baseTime * 2, "gem/" + name + "/block"); } if (gemTag != null && !gemTag.isEmpty()) {
            addMeltingTag(output, gemTag, fluid, 90, temp, baseTime, "gem/" + name + "/gem"); } else if (gem != null) {
            addMeltingItem(output, gem, fluid, 90, temp, baseTime, "gem/" + name + "/gem"); }
        if (block != null) addCastingBasin(output, fluid, blockAmount, block, baseTime * 2, "gem/" + name + "/block");
        if (gem != null) addCastingTable(output, fluid, 90, ModItems.GEM_CAST.get(), false, gem, baseTime, "gem/" + name + "/gem_cast");
    }

    private void createCastRecipe(RecipeOutput output, String tagPath, ItemLike castResult, String savePath) {
        Ingredient tagIngredient = Ingredient.of(ItemTags.create(ResourceLocation.fromNamespaceAndPath("c", tagPath)));
        ModRecipes.CastingTableRecipe recipe = new ModRecipes.CastingTableRecipe(tagIngredient, true, new FluidStack(HephaestusFluids.SETS.get(HephaestusFluids.Material.MOLTEN_COPPER).source.get(), 90), new ItemStack(castResult), 60);
        output.accept(ResourceLocation.fromNamespaceAndPath(TitamMods.MODID, "smeltery/casting/casts/" + savePath), recipe, null);
    }

    private void createRodCastRecipe(RecipeOutput output) {
        Ingredient rodOrStick = Ingredient.of(Items.STICK, Items.BLAZE_ROD);
        ModRecipes.CastingTableRecipe recipe = new ModRecipes.CastingTableRecipe(
                rodOrStick, true,
                new FluidStack(HephaestusFluids.SETS.get(HephaestusFluids.Material.MOLTEN_COPPER).source.get(), 90),
                new ItemStack(ModItems.ROD_CAST.get()), 60);
        output.accept(ResourceLocation.fromNamespaceAndPath(TitamMods.MODID, "smeltery/casting/casts/rod_cast"), recipe, null);
    }

    private void addMeltingTag(RecipeOutput output, String tagPath, Fluid fluid, int amount, int temperature, int time, String savePath) {
        Ingredient ingredient = Ingredient.of(ItemTags.create(ResourceLocation.fromNamespaceAndPath("c", tagPath)));
        FluidStack fuel = fuelForTemp(temperature);
        ModRecipes.MeltingRecipe recipe = new ModRecipes.MeltingRecipe(ingredient, new FluidStack(fluid, amount), fuel, temperature, time);
        output.accept(ResourceLocation.fromNamespaceAndPath(TitamMods.MODID, "smeltery/melting/" + savePath), recipe, null);
    }

    private void addMeltingItem(RecipeOutput output, ItemLike item, Fluid fluid, int amount, int temperature, int time, String savePath) {
        Ingredient ingredient = Ingredient.of(item);
        FluidStack fuel = fuelForTemp(temperature);
        ModRecipes.MeltingRecipe recipe = new ModRecipes.MeltingRecipe(ingredient, new FluidStack(fluid, amount), fuel, temperature, time);
        output.accept(ResourceLocation.fromNamespaceAndPath(TitamMods.MODID, "smeltery/melting/" + savePath), recipe, null);
    }

    private void addMeltingDamageable(RecipeOutput output, ItemLike item, Fluid fluid, int amount, int temperature, int time, String savePath) {
        Ingredient ingredient = Ingredient.of(item);
        FluidStack fuel = fuelForTemp(temperature);
        ModRecipes.MeltingRecipe recipe = new ModRecipes.MeltingRecipe(ingredient, new FluidStack(fluid, amount), fuel, temperature, time, true);
        output.accept(ResourceLocation.fromNamespaceAndPath(TitamMods.MODID, "smeltery/melting/" + savePath), recipe, null);
    }

    private void addToolArmorMelting(RecipeOutput output) {
        Fluid iron = HephaestusFluids.SETS.get(HephaestusFluids.Material.MOLTEN_IRON).source.get();
        Fluid gold = HephaestusFluids.SETS.get(HephaestusFluids.Material.MOLTEN_GOLD).source.get();
        Fluid netherite = HephaestusFluids.SETS.get(HephaestusFluids.Material.MOLTEN_NETHERITE).source.get();
        Fluid diamond = ModFluids.MOLTEN_DIAMOND.source.get();

        addMeltingDamageable(output, Items.IRON_PICKAXE, iron, 270, 900, 200, "tools/iron_pickaxe");
        addMeltingDamageable(output, Items.IRON_AXE,     iron, 270, 900, 200, "tools/iron_axe");
        addMeltingDamageable(output, Items.IRON_SWORD,   iron, 180, 900, 150, "tools/iron_sword");
        addMeltingDamageable(output, Items.IRON_SHOVEL,  iron,  90, 900, 100, "tools/iron_shovel");
        addMeltingDamageable(output, Items.IRON_HOE,     iron, 180, 900, 150, "tools/iron_hoe");
        addMeltingDamageable(output, Items.IRON_HELMET,     iron, 450, 900, 250, "armor/iron_helmet");
        addMeltingDamageable(output, Items.IRON_CHESTPLATE, iron, 720, 900, 350, "armor/iron_chestplate");
        addMeltingDamageable(output, Items.IRON_LEGGINGS,   iron, 630, 900, 300, "armor/iron_leggings");
        addMeltingDamageable(output, Items.IRON_BOOTS,      iron, 360, 900, 200, "armor/iron_boots");
        addMeltingDamageable(output, Items.CHAINMAIL_HELMET,     iron, 450, 900, 250, "armor/chainmail_helmet");
        addMeltingDamageable(output, Items.CHAINMAIL_CHESTPLATE, iron, 720, 900, 350, "armor/chainmail_chestplate");
        addMeltingDamageable(output, Items.CHAINMAIL_LEGGINGS,   iron, 630, 900, 300, "armor/chainmail_leggings");
        addMeltingDamageable(output, Items.CHAINMAIL_BOOTS,      iron, 360, 900, 200, "armor/chainmail_boots");
        addMeltingDamageable(output, Items.GOLDEN_PICKAXE, gold, 270, 900, 200, "tools/golden_pickaxe");
        addMeltingDamageable(output, Items.GOLDEN_AXE,     gold, 270, 900, 200, "tools/golden_axe");
        addMeltingDamageable(output, Items.GOLDEN_SWORD,   gold, 180, 900, 150, "tools/golden_sword");
        addMeltingDamageable(output, Items.GOLDEN_SHOVEL,  gold,  90, 900, 100, "tools/golden_shovel");
        addMeltingDamageable(output, Items.GOLDEN_HOE,     gold, 180, 900, 150, "tools/golden_hoe");
        addMeltingDamageable(output, Items.GOLDEN_HELMET,     gold, 450, 900, 250, "armor/golden_helmet");
        addMeltingDamageable(output, Items.GOLDEN_CHESTPLATE, gold, 720, 900, 350, "armor/golden_chestplate");
        addMeltingDamageable(output, Items.GOLDEN_LEGGINGS,   gold, 630, 900, 300, "armor/golden_leggings");
        addMeltingDamageable(output, Items.GOLDEN_BOOTS,      gold, 360, 900, 200, "armor/golden_boots");
        addMeltingDamageable(output, Items.DIAMOND_PICKAXE, diamond, 270, 1400, 200, "tools/diamond_pickaxe");
        addMeltingDamageable(output, Items.DIAMOND_AXE,     diamond, 270, 1400, 200, "tools/diamond_axe");
        addMeltingDamageable(output, Items.DIAMOND_SWORD,   diamond, 180, 1400, 150, "tools/diamond_sword");
        addMeltingDamageable(output, Items.DIAMOND_SHOVEL,  diamond,  90, 1400, 100, "tools/diamond_shovel");
        addMeltingDamageable(output, Items.DIAMOND_HOE,     diamond, 180, 1400, 150, "tools/diamond_hoe");
        addMeltingDamageable(output, Items.DIAMOND_HELMET,     diamond, 450, 1400, 250, "armor/diamond_helmet");
        addMeltingDamageable(output, Items.DIAMOND_CHESTPLATE, diamond, 720, 1400, 350, "armor/diamond_chestplate");
        addMeltingDamageable(output, Items.DIAMOND_LEGGINGS,   diamond, 630, 1400, 300, "armor/diamond_leggings");
        addMeltingDamageable(output, Items.DIAMOND_BOOTS,      diamond, 360, 1400, 200, "armor/diamond_boots");
        addMeltingDamageable(output, Items.NETHERITE_PICKAXE, netherite, 90, 2000, 300, "tools/netherite_pickaxe");
        addMeltingDamageable(output, Items.NETHERITE_AXE,     netherite, 90, 2000, 300, "tools/netherite_axe");
        addMeltingDamageable(output, Items.NETHERITE_SWORD,   netherite, 90, 2000, 300, "tools/netherite_sword");
        addMeltingDamageable(output, Items.NETHERITE_SHOVEL,  netherite, 90, 2000, 300, "tools/netherite_shovel");
        addMeltingDamageable(output, Items.NETHERITE_HOE,     netherite, 90, 2000, 300, "tools/netherite_hoe");
        addMeltingDamageable(output, Items.NETHERITE_HELMET,     netherite, 90, 2000, 350, "armor/netherite_helmet");
        addMeltingDamageable(output, Items.NETHERITE_CHESTPLATE, netherite, 90, 2000, 350, "armor/netherite_chestplate");
        addMeltingDamageable(output, Items.NETHERITE_LEGGINGS,   netherite, 90, 2000, 350, "armor/netherite_leggings");
        addMeltingDamageable(output, Items.NETHERITE_BOOTS,      netherite, 90, 2000, 350, "armor/netherite_boots");
    }

    private void addCastingBasin(RecipeOutput output, Fluid fluid, int amount, ItemLike result, int time, String savePath) {
        ModRecipes.CastingBasinRecipe recipe = new ModRecipes.CastingBasinRecipe(new FluidStack(fluid, amount), new ItemStack(result), time);
        output.accept(ResourceLocation.fromNamespaceAndPath(TitamMods.MODID, "smeltery/casting/basin/" + savePath), recipe, null);
    }

    private void addCastingTable(RecipeOutput output, Fluid fluid, int fluidAmount, ItemLike castItem, boolean consumesCast, ItemLike resultItem, int time, String savePath) {
        Ingredient castIngredient = castItem == null ? Ingredient.EMPTY : Ingredient.of(castItem);
        ModRecipes.CastingTableRecipe recipe = new ModRecipes.CastingTableRecipe(castIngredient, consumesCast, new FluidStack(fluid, fluidAmount), new ItemStack(resultItem), time);
        output.accept(ResourceLocation.fromNamespaceAndPath(TitamMods.MODID, "smeltery/casting/table/" + savePath), recipe, null);
    }

    private void generateDecorative(RecipeOutput output, Block baseBlock, Block slab, Block stairs, Block wall) {
        String baseName = BuiltInRegistries.BLOCK.getKey(baseBlock).getPath();
        if (slab != null) ShapedRecipeBuilder.shaped(RecipeCategory.MISC, slab, 6).pattern("BBB").define('B', baseBlock).unlockedBy("has_" + baseName, has(baseBlock)).save(output, ResourceLocation.fromNamespaceAndPath(TitamMods.MODID, "decoration/" + BuiltInRegistries.BLOCK.getKey(slab).getPath()));
        if (stairs != null) ShapedRecipeBuilder.shaped(RecipeCategory.MISC, stairs, 4).pattern("B  ").pattern("BB ").pattern("BBB").define('B', baseBlock).unlockedBy("has_" + baseName, has(baseBlock)).save(output, ResourceLocation.fromNamespaceAndPath(TitamMods.MODID, "decoration/" + BuiltInRegistries.BLOCK.getKey(stairs).getPath()));
        if (wall != null) ShapedRecipeBuilder.shaped(RecipeCategory.MISC, wall, 6).pattern("BBB").pattern("BBB").define('B', baseBlock).unlockedBy("has_" + baseName, has(baseBlock)).save(output, ResourceLocation.fromNamespaceAndPath(TitamMods.MODID, "decoration/" + BuiltInRegistries.BLOCK.getKey(wall).getPath()));
    }

    private void addAlloyRecipe(RecipeOutput output, List<FluidStack> inputs, FluidStack result, int temperature, String savePath) {
        com.titammods.recipe.AlloyRecipe recipe = new com.titammods.recipe.AlloyRecipe(inputs, result, temperature);
        output.accept(ResourceLocation.fromNamespaceAndPath(TitamMods.MODID, "smeltery/alloying/" + savePath), recipe, null);
    }

    private void addEntityMelting(RecipeOutput output, net.minecraft.world.entity.EntityType<?> entityType,
                                  FluidStack result, int damage, String savePath) {
        ModRecipes.EntityMeltingRecipe recipe = new ModRecipes.EntityMeltingRecipe(entityType, result, damage);
        output.accept(ResourceLocation.fromNamespaceAndPath(TitamMods.MODID, "smeltery/entity_melting/" + savePath), recipe, null);
    }
}
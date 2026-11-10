package com.titammods.datagen;

import com.titammods.TitamMods;
import com.titammods.registry.HephaestusFluids;
import com.titammods.setup.ModFluids;
import com.titammods.setup.ModItems;
import com.titammods.setup.ModBlocks;
import com.titammods.setup.ModRecipes;
import net.minecraft.core.HolderLookup;
import net.minecraft.world.level.block.Block;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.common.conditions.ICondition;
import net.neoforged.neoforge.common.conditions.ModLoadedCondition;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.data.recipes.SimpleCookingRecipeBuilder;
import net.minecraft.world.item.crafting.CookingBookCategory;
import net.minecraft.world.item.crafting.Ingredient;

public class ModRecipeProvider extends RecipeProvider {

    public ModRecipeProvider(HolderLookup.Provider registries, RecipeOutput output) {
        super(registries, output);
    }

    @Override
    protected void buildRecipes() {

        registerMetal(HephaestusFluids.SETS.get(HephaestusFluids.Material.MOLTEN_IRON).source.get(),
                900, "iron", Items.IRON_BLOCK, Items.IRON_INGOT, Items.RAW_IRON, Items.IRON_NUGGET, null, null, null, null, "");

        registerMetal(HephaestusFluids.SETS.get(HephaestusFluids.Material.MOLTEN_GOLD).source.get(),
                900, "gold", Items.GOLD_BLOCK, Items.GOLD_INGOT, Items.RAW_GOLD, Items.GOLD_NUGGET, null, null, null, null, "");

        registerMetal(HephaestusFluids.SETS.get(HephaestusFluids.Material.MOLTEN_COPPER).source.get(),
                900, "copper", Items.COPPER_BLOCK, Items.COPPER_INGOT, Items.RAW_COPPER, null, null, null, null, null, "");

        registerMetal(HephaestusFluids.SETS.get(HephaestusFluids.Material.MOLTEN_STEEL).source.get(),
                1000, "steel", ModBlocks.STEEL_BLOCK.get(), ModItems.STEEL_INGOT.get(), ModItems.RAW_STEEL.get(),
                ModItems.STEEL_NUGGET.get(), ModItems.STEEL_POWDER.get(), null, null, null, "");

        registerMetal(HephaestusFluids.SETS.get(HephaestusFluids.Material.MOLTEN_NETHERITE).source.get(),
                2000, "netherite", Items.NETHERITE_BLOCK, Items.NETHERITE_INGOT, null, null, null, null, null, null, "");

        registerMetal(ModFluids.MOLTEN_COBALT.source.get(), 1100, "cobalt",
                ModBlocks.COBALT_BLOCK.get(), ModItems.COBALT_INGOT.get(), ModItems.RAW_COBALT.get(),
                ModItems.COBALT_NUGGET.get(), ModItems.COBALT_POWDER.get(), null, null, null, "");

        registerGem(ModFluids.MOLTEN_DIAMOND.source.get(),  1400, "diamond", "storage_blocks/diamond", "gems/diamond",  Items.DIAMOND_BLOCK,  Items.DIAMOND);
        registerGem(ModFluids.MOLTEN_EMERALD.source.get(),  1200, "emerald", "storage_blocks/emerald", "gems/emerald",  Items.EMERALD_BLOCK,  Items.EMERALD);
        registerGem(ModFluids.MOLTEN_AMETHYST.source.get(), 1000, "amethyst", "", "gems/amethyst", Items.AMETHYST_BLOCK, Items.AMETHYST_SHARD, 360);
        registerGem(ModFluids.MOLTEN_QUARTZ.source.get(),    800, "quartz", "", "gems/quartz",   Items.QUARTZ_BLOCK,   Items.QUARTZ, 360);

        registerAllTheOresCompat();
        registerFtbMaterialsCompat();
        registerFtbArmoryCompat();
        registerFtbIcCompat();
        registerGeoreCompat();

        registerAe2Compat();
        registerArsMagicaLegacyCompat();
        registerCakeStickLibCompat();
        registerColossalReactorsCompat();
        registerEnderIoCompat();
        registerEnderIoEvolutionCompat();
        registerEnergizedPowerCompat();
        registerEvilCraftCompat();
        registerExtendedAeCompat();
        registerIronsJewelryCompat();
        registerJustDireThingsCompat();
        registerLtxiCompat();
        registerMysticalAgradditionsCompat();
        registerNeoVitaeCompat();
        registerNodeworksCompat();
        registerOccultismCompat();
        registerOritechCompat();
        registerReplicationCompat();
        registerStellarisCompat();
        registerThaumaturgeCompat();
        registerXyCraftWorldCompat();
        addVanillaRecipes();
        addMeltingMiscRecipes();

        generateDecorative(items, ModBlocks.SEARED_STONE.get(),
                ModBlocks.SEARED_STONE_SLAB.get(),
                ModBlocks.SEARED_STONE_STAIRS.get(),
                ModBlocks.SEARED_STONE_WALL.get());
        generateDecorative(items, ModBlocks.SEARED_COBBLE.get(),
                ModBlocks.SEARED_COBBLE_SLAB.get(),
                ModBlocks.SEARED_COBBLE_STAIRS.get(),
                ModBlocks.SEARED_COBBLE_WALL.get());
        generateDecorative(items, ModBlocks.SEARED_PAVER.get(),
                ModBlocks.SEARED_PAVER_SLAB.get(),
                ModBlocks.SEARED_PAVER_STAIRS.get(),
                ModBlocks.SEARED_PAVER_WALL.get());
        generateDecorative(items, ModBlocks.SEARED_BRICKS.get(),
                ModBlocks.SEARED_BRICKS_SLAB.get(),
                ModBlocks.SEARED_BRICKS_STAIRS.get(),
                ModBlocks.SEARED_BRICKS_WALL.get());
        generateDecorative(items, ModBlocks.SEARED_CRACKED_BRICKS.get(),
                ModBlocks.SEARED_CRACKED_BRICKS_SLAB.get(),
                ModBlocks.SEARED_CRACKED_BRICKS_STAIRS.get(),
                ModBlocks.SEARED_CRACKED_BRICKS_WALL.get());
        generateDecorative(items, ModBlocks.SEARED_FANCY_BRICKS.get(),
                ModBlocks.SEARED_FANCY_BRICKS_SLAB.get(),
                ModBlocks.SEARED_FANCY_BRICKS_STAIRS.get(),
                ModBlocks.SEARED_FANCY_BRICKS_WALL.get());
        generateDecorative(items, ModBlocks.SEARED_TRIANGLE_BRICKS.get(),
                ModBlocks.SEARED_TRIANGLE_BRICKS_SLAB.get(),
                ModBlocks.SEARED_TRIANGLE_BRICKS_STAIRS.get(),
                ModBlocks.SEARED_TRIANGLE_BRICKS_WALL.get());
        generateDecorative(items, ModBlocks.SEARED_CREEPER.get(),
                ModBlocks.SEARED_CREEPER_SLAB.get(),
                ModBlocks.SEARED_CREEPER_STAIRS.get(),
                ModBlocks.SEARED_CREEPER_WALL.get());
        generateDecorative(items, ModBlocks.SEARED_ROAD.get(),
                ModBlocks.SEARED_ROAD_SLAB.get(),
                ModBlocks.SEARED_ROAD_STAIRS.get(),
                ModBlocks.SEARED_ROAD_WALL.get());
        generateDecorative(items, ModBlocks.SEARED_SMALL_BRICKS.get(),
                ModBlocks.SEARED_SMALL_BRICKS_SLAB.get(),
                ModBlocks.SEARED_SMALL_BRICKS_STAIRS.get(),
                ModBlocks.SEARED_SMALL_BRICKS_WALL.get());
        generateDecorative(items, ModBlocks.SEARED_SQUARE_BRICKS.get(),
                ModBlocks.SEARED_SQUARE_BRICKS_SLAB.get(),
                ModBlocks.SEARED_SQUARE_BRICKS_STAIRS.get(),
                ModBlocks.SEARED_SQUARE_BRICKS_WALL.get());
        generateDecorative(items, ModBlocks.SEARED_TILE.get(),
                ModBlocks.SEARED_TILE_SLAB.get(),
                ModBlocks.SEARED_TILE_STAIRS.get(),
                ModBlocks.SEARED_TILE_WALL.get());

        addAlloyRecipes();
        addEntityMeltingRecipes();
        addFixRecipes();

        createCastRecipe("ingots",   ModItems.INGOT_CAST.get(),  "ingot_cast");
        createCastRecipe("nuggets",  ModItems.NUGGET_CAST.get(), "nugget_cast");
        createCastRecipe("gems",     ModItems.GEM_CAST.get(),    "gem_cast");
        createCastRecipe("plates",   ModItems.PLATE_CAST.get(),  "plate_cast");
        createCastRecipe("gears",    ModItems.GEAR_CAST.get(),   "gear_cast");
        createRodCastRecipe();

    }

    private void addVanillaRecipes() {
        var items = this.registries.lookupOrThrow(Registries.ITEM);

        ShapedRecipeBuilder.shaped(items, RecipeCategory.MISC, ModItems.FORGE_BRICK.get(), 16)
                .pattern("CCS").pattern("GLS").pattern("GCC")
                .define('G', Items.GRAVEL).define('C', Items.COAL)
                .define('S', Items.SAND).define('L', Items.CLAY_BALL)
                .unlockedBy("has_clay", this.has(Items.CLAY_BALL))
                .save(this.output, rk("forge_brick"));

        ShapedRecipeBuilder.shaped(items, RecipeCategory.MISC, ModBlocks.SEARED_BRICKS.get(), 4)
                .pattern("BB").pattern("BB")
                .define('B', ModItems.FORGE_BRICK.get())
                .unlockedBy("has_forge_brick", this.has(ModItems.FORGE_BRICK.get()))
                .save(this.output, rk("seared_bricks"));

        ShapedRecipeBuilder.shaped(items, RecipeCategory.MISC, ModBlocks.SEARED_COBBLE.get(), 4)
                .pattern("BS").pattern("SB")
                .define('B', ModItems.FORGE_BRICK.get()).define('S', Items.COBBLESTONE)
                .unlockedBy("has_forge_brick", this.has(ModItems.FORGE_BRICK.get()))
                .save(this.output, rk("seared_cobble"));

        ShapedRecipeBuilder.shaped(items, RecipeCategory.MISC, ModBlocks.SEARED_INGOT_TANK.get())
                .pattern("BBB").pattern("B B").pattern("BBB")
                .define('B', ModBlocks.SEARED_BRICKS.get())
                .unlockedBy("has_seared_bricks", this.has(ModBlocks.SEARED_BRICKS.get()))
                .save(this.output, rk("seared_ingot_tank"));

        ShapedRecipeBuilder.shaped(items, RecipeCategory.MISC, ModBlocks.SEARED_FUEL_TANK.get())
                .pattern("BBB").pattern("BLB").pattern("BBB")
                .define('B', ModBlocks.SEARED_BRICKS.get()).define('L', Items.LAVA_BUCKET)
                .unlockedBy("has_seared_bricks", this.has(ModBlocks.SEARED_BRICKS.get()))
                .save(this.output, rk("seared_fuel_tank"));

        ShapedRecipeBuilder.shaped(items, RecipeCategory.MISC, ModBlocks.SEARED_MELTER.get())
                .pattern("BTB").pattern("BFB").pattern("BSB")
                .define('B', ModItems.FORGE_BRICK.get())
                .define('T', ModBlocks.SEARED_INGOT_TANK.get())
                .define('S', ModBlocks.SEARED_BRICKS.get())
                .define('F', Items.BLAST_FURNACE)
                .unlockedBy("has_seared_bricks", this.has(ModBlocks.SEARED_BRICKS.get()))
                .save(this.output, rk("seared_melter"));

        ShapedRecipeBuilder.shaped(items, RecipeCategory.MISC, ModBlocks.SEARED_FAUCET.get(), 2)
                .pattern("B B").pattern(" C ")
                .define('B', ModBlocks.SEARED_BRICKS.get()).define('C', Items.COPPER_INGOT)
                .unlockedBy("has_seared_bricks", this.has(ModBlocks.SEARED_BRICKS.get()))
                .save(this.output, rk("seared_faucet"));

        ShapedRecipeBuilder.shaped(items, RecipeCategory.MISC, ModBlocks.SEARED_TABLE.get())
                .pattern("BBB").pattern("B B").pattern("B B")
                .define('B', ModBlocks.SEARED_BRICKS.get())
                .unlockedBy("has_seared_bricks", this.has(ModBlocks.SEARED_BRICKS.get()))
                .save(this.output, rk("seared_table"));

        ShapedRecipeBuilder.shaped(items, RecipeCategory.MISC, ModBlocks.SEARED_BASIN.get())
                .pattern("B B").pattern("B B").pattern("BBB")
                .define('B', ModBlocks.SEARED_BRICKS.get())
                .unlockedBy("has_seared_bricks", this.has(ModBlocks.SEARED_BRICKS.get()))
                .save(this.output, rk("seared_basin"));

        ShapedRecipeBuilder.shaped(items, RecipeCategory.MISC, ModBlocks.SEARED_SMALL_BRICKS.get(), 4)
                .pattern("BB").pattern("BB")
                .define('B', ModBlocks.SEARED_BRICKS.get())
                .unlockedBy("has_seared_bricks", this.has(ModBlocks.SEARED_BRICKS.get()))
                .save(this.output, rk("seared_small_bricks"));

        ShapedRecipeBuilder.shaped(items, RecipeCategory.MISC, ModBlocks.SEARED_SQUARE_BRICKS.get(), 4)
                .pattern("BB").pattern("BB")
                .define('B', ModBlocks.SEARED_SMALL_BRICKS.get())
                .unlockedBy("has_seared_bricks", this.has(ModBlocks.SEARED_BRICKS.get()))
                .save(this.output, rk("seared_square_bricks"));

        ShapedRecipeBuilder.shaped(items, RecipeCategory.MISC, ModBlocks.SEARED_TILE.get(), 4)
                .pattern("BB").pattern("BB")
                .define('B', ModBlocks.SEARED_STONE.get())
                .unlockedBy("has_seared_stone", this.has(ModBlocks.SEARED_STONE.get()))
                .save(this.output, rk("seared_tile"));

        ShapedRecipeBuilder.shaped(items, RecipeCategory.MISC, ModBlocks.SEARED_FANCY_BRICKS.get(), 4)
                .pattern("BB").pattern("BB")
                .define('B', ModBlocks.SEARED_TILE.get())
                .unlockedBy("has_seared_tile", this.has(ModBlocks.SEARED_TILE.get()))
                .save(this.output, rk("seared_fancy_bricks"));

        ShapedRecipeBuilder.shaped(items, RecipeCategory.MISC, ModBlocks.SEARED_TRIANGLE_BRICKS.get(), 4)
                .pattern("BS").pattern("SB")
                .define('B', ModBlocks.SEARED_BRICKS.get()).define('S', ModBlocks.SEARED_STONE.get())
                .unlockedBy("has_seared_bricks", this.has(ModBlocks.SEARED_BRICKS.get()))
                .save(this.output, rk("seared_triangle_bricks"));

        ShapedRecipeBuilder.shaped(items, RecipeCategory.MISC, ModBlocks.SEARED_ROAD.get())
                .pattern("B").pattern("B")
                .define('B', ModBlocks.SEARED_BRICKS.get())
                .unlockedBy("has_seared_bricks", this.has(ModBlocks.SEARED_BRICKS.get()))
                .save(this.output, rk("seared_road"));

        SimpleCookingRecipeBuilder.smelting(
                        Ingredient.of(ModBlocks.SEARED_COBBLE.get()),
                        RecipeCategory.MISC, CookingBookCategory.BLOCKS,
                        ModBlocks.SEARED_STONE.get(), 0.1f, 200)
                .unlockedBy("has_seared_cobble", this.has(ModBlocks.SEARED_COBBLE.get()))
                .save(this.output, rk("seared_stone_from_smelting"));

        SimpleCookingRecipeBuilder.smelting(
                        Ingredient.of(ModBlocks.SEARED_STONE.get()),
                        RecipeCategory.MISC, CookingBookCategory.BLOCKS,
                        ModBlocks.SEARED_PAVER.get(), 0.1f, 200)
                .unlockedBy("has_seared_stone", this.has(ModBlocks.SEARED_STONE.get()))
                .save(this.output, rk("seared_paver_from_smelting"));

        SimpleCookingRecipeBuilder.smelting(
                        Ingredient.of(ModBlocks.SEARED_BRICKS.get()),
                        RecipeCategory.MISC, CookingBookCategory.BLOCKS,
                        ModBlocks.SEARED_CRACKED_BRICKS.get(), 0.1f, 200)
                .unlockedBy("has_seared_bricks", this.has(ModBlocks.SEARED_BRICKS.get()))
                .save(this.output, rk("seared_cracked_bricks_from_smelting"));

        SimpleCookingRecipeBuilder.smelting(
                        Ingredient.of(ModBlocks.SEARED_SMALL_BRICKS.get()),
                        RecipeCategory.MISC, CookingBookCategory.BLOCKS,
                        ModBlocks.SEARED_CREEPER.get(), 0.1f, 200)
                .unlockedBy("has_seared_small_bricks", this.has(ModBlocks.SEARED_SMALL_BRICKS.get()))
                .save(this.output, rk("seared_creeper_from_smelting"));

        ShapedRecipeBuilder.shaped(items, RecipeCategory.MISC, ModBlocks.SEARED_CHUTE.get(), 1)
                .pattern("BGB").pattern("B B").pattern("BGB")
                .define('B', ModItems.FORGE_BRICK.get())
                .define('G', net.minecraft.tags.ItemTags.create(Identifier.fromNamespaceAndPath("c", "ingots/copper")))
                .unlockedBy("has_forge_brick", this.has(ModItems.FORGE_BRICK.get()))
                .save(this.output, rk("seared_chute"));

        ShapedRecipeBuilder.shaped(items, RecipeCategory.MISC, ModBlocks.SEARED_DRAIN.get(), 1)
                .pattern("BBB").pattern("G G").pattern("BBB")
                .define('B', ModItems.FORGE_BRICK.get())
                .define('G', net.minecraft.tags.ItemTags.create(Identifier.fromNamespaceAndPath("c", "ingots/copper")))
                .unlockedBy("has_forge_brick", this.has(ModItems.FORGE_BRICK.get()))
                .save(this.output, rk("seared_drain"));

        ShapedRecipeBuilder.shaped(items, RecipeCategory.MISC, ModBlocks.SEARED_BATTERY.get(), 1)
                .pattern("BBB").pattern("BRB").pattern("BBB")
                .define('B', ModBlocks.SEARED_BRICKS.get())
                .define('R', net.minecraft.tags.ItemTags.create(Identifier.fromNamespaceAndPath("c", "storage_blocks/redstone")))
                .unlockedBy("has_seared_bricks", this.has(ModBlocks.SEARED_BRICKS.get()))
                .save(this.output, rk("seared_battery"));

        ShapedRecipeBuilder.shaped(items, RecipeCategory.MISC, ModBlocks.SEARED_ENERGY_PORT.get(), 1)
                .pattern("BBB").pattern("R R").pattern("BBB")
                .define('B', ModItems.FORGE_BRICK.get())
                .define('R', net.minecraft.tags.ItemTags.create(Identifier.fromNamespaceAndPath("c", "dusts/redstone")))
                .unlockedBy("has_forge_brick", this.has(ModItems.FORGE_BRICK.get()))
                .save(this.output, rk("seared_energy_port"));

        addCastingBasin(HephaestusFluids.SETS.get(HephaestusFluids.Material.MOLTEN_COPPER).source.get(), 810,
                ModBlocks.SEARED_BRICKS.get(), ModBlocks.SMELTERY_CONTROLLER.get(), 200,
                "smeltery_controller");

        ShapedRecipeBuilder.shaped(items, RecipeCategory.MISC, ModBlocks.SEARED_GLASS.get(), 4)
                .pattern("GGG")
                .pattern("GBG")
                .pattern("GGG")
                .define('G', Items.GLASS)
                .define('B', ModItems.FORGE_BRICK.get())
                .unlockedBy("has_forge_brick", this.has(ModItems.FORGE_BRICK.get()))
                .save(this.output, rk("seared_glass"));

        ShapedRecipeBuilder.shaped(items, RecipeCategory.MISC, ModBlocks.SEARED_TINTED_GLASS.get(), 4)
                .pattern("GGG")
                .pattern("GBG")
                .pattern("GGG")
                .define('G', Items.TINTED_GLASS)
                .define('B', ModItems.FORGE_BRICK.get())
                .unlockedBy("has_forge_brick", this.has(ModItems.FORGE_BRICK.get()))
                .save(this.output, rk("seared_tinted_glass"));

        ShapedRecipeBuilder.shaped(items, RecipeCategory.MISC, ModBlocks.CLEAR_GLASS.get(), 4)
                .pattern("GG")
                .pattern("GG")
                .define('G', Items.GLASS)
                .unlockedBy("has_glass", this.has(Items.GLASS))
                .save(this.output, rk("clear_glass"));

        ShapedRecipeBuilder.shaped(items, RecipeCategory.MISC, ModBlocks.CLEAR_STAINED_GLASS.get(), 8)
                .pattern("GGG")
                .pattern("GDG")
                .pattern("GGG")
                .define('G', ModBlocks.CLEAR_GLASS.get())
                .define('D', ItemTags.create(Identifier.fromNamespaceAndPath("c", "dyes")))
                .unlockedBy("has_clear_glass", this.has(ModBlocks.CLEAR_GLASS.get()))
                .save(this.output, rk("clear_stained_glass"));

        ShapedRecipeBuilder.shaped(items, RecipeCategory.MISC, ModBlocks.CLEAR_TINTED_GLASS.get(), 2)
                .pattern(" A ")
                .pattern("AGA")
                .pattern(" A ")
                .define('G', ModBlocks.CLEAR_GLASS.get())
                .define('A', Items.AMETHYST_SHARD)
                .unlockedBy("has_clear_glass", this.has(ModBlocks.CLEAR_GLASS.get()))
                .save(this.output, rk("clear_tinted_glass"));

        ShapedRecipeBuilder.shaped(items, RecipeCategory.MISC, ModBlocks.SEARED_LAMP.get(), 2)
                .pattern(" B ").pattern("BGB").pattern(" B ")
                .define('B', ModBlocks.SEARED_BRICKS.get())
                .define('G', net.minecraft.world.item.Items.GLOWSTONE)
                .unlockedBy("has_seared_bricks", has(ModBlocks.SEARED_BRICKS.get()))
                .save(this.output, ResourceKey.create(Registries.RECIPE,
                        Identifier.fromNamespaceAndPath(TitamMods.MODID, "seared_lamp")));

        ShapedRecipeBuilder.shaped(items, RecipeCategory.MISC, ModBlocks.SEARED_LADDER.get(), 3)
                .pattern("B B").pattern("BSB").pattern("B B")
                .define('B', ModBlocks.SEARED_BRICKS.get())
                .define('S', net.minecraft.world.item.Items.STICK)
                .unlockedBy("has_seared_bricks", has(ModBlocks.SEARED_BRICKS.get()))
                .save(this.output, ResourceKey.create(Registries.RECIPE,
                        Identifier.fromNamespaceAndPath(TitamMods.MODID, "seared_ladder")));
    }

    private void registerAllTheOresCompat() {
        ICondition cond = new ModLoadedCondition("alltheores");
        String prefix = "alltheores_compat/";

        registerExternalMetal(HephaestusFluids.SETS.get(HephaestusFluids.Material.MOLTEN_ALUMINUM).source.get(),   660, "aluminum",  "alltheores", prefix, cond);
        registerExternalMetal(HephaestusFluids.SETS.get(HephaestusFluids.Material.MOLTEN_BRASS).source.get(),      930, "brass",     "alltheores", prefix, cond);
        registerExternalMetal(HephaestusFluids.SETS.get(HephaestusFluids.Material.MOLTEN_BRONZE).source.get(),     950, "bronze",    "alltheores", prefix, cond);
        registerExternalMetal(HephaestusFluids.SETS.get(HephaestusFluids.Material.MOLTEN_CONSTANTAN).source.get(),1220, "constantan","alltheores", prefix, cond);
        registerExternalMetal(HephaestusFluids.SETS.get(HephaestusFluids.Material.MOLTEN_ELECTRUM).source.get(),  1000, "electrum",  "alltheores", prefix, cond);
        registerExternalMetal(HephaestusFluids.SETS.get(HephaestusFluids.Material.MOLTEN_ENDERIUM).source.get(),  1450, "enderium",  "alltheores", prefix, cond);
        registerExternalMetal(HephaestusFluids.SETS.get(HephaestusFluids.Material.MOLTEN_INVAR).source.get(),     1420, "invar",     "alltheores", prefix, cond);
        registerExternalMetal(HephaestusFluids.SETS.get(HephaestusFluids.Material.MOLTEN_IRIDIUM).source.get(),   1440, "iridium",   "alltheores", prefix, cond);
        registerExternalMetal(HephaestusFluids.SETS.get(HephaestusFluids.Material.MOLTEN_LEAD).source.get(),       327, "lead",      "alltheores", prefix, cond);
        registerExternalMetal(HephaestusFluids.SETS.get(HephaestusFluids.Material.MOLTEN_LUMIUM).source.get(),    1000, "lumium",    "alltheores", prefix, cond);
        registerExternalMetal(HephaestusFluids.SETS.get(HephaestusFluids.Material.MOLTEN_NICKEL).source.get(),    1450, "nickel",    "alltheores", prefix, cond);
        registerExternalMetal(HephaestusFluids.SETS.get(HephaestusFluids.Material.MOLTEN_OSMIUM).source.get(),    1990, "osmium",    "alltheores", prefix, cond);
        registerExternalMetal(HephaestusFluids.SETS.get(HephaestusFluids.Material.MOLTEN_PLATINUM).source.get(),  1768, "platinum",  "alltheores", prefix, cond);
        registerExternalMetal(HephaestusFluids.SETS.get(HephaestusFluids.Material.MOLTEN_SIGNALUM).source.get(),  1000, "signalum",  "alltheores", prefix, cond);
        registerExternalMetal(HephaestusFluids.SETS.get(HephaestusFluids.Material.MOLTEN_SILVER).source.get(),     960, "silver",    "alltheores", prefix, cond);
        registerExternalMetal(HephaestusFluids.SETS.get(HephaestusFluids.Material.MOLTEN_TIN).source.get(),        230, "tin",       "alltheores", prefix, cond);
        registerExternalMetal(HephaestusFluids.SETS.get(HephaestusFluids.Material.MOLTEN_URANIUM).source.get(),   1130, "uranium",   "alltheores", prefix, cond);
        registerExternalMetal(HephaestusFluids.SETS.get(HephaestusFluids.Material.MOLTEN_ZINC).source.get(),       419, "zinc",      "alltheores", prefix, cond);
    }

    private void registerFtbMaterialsCompat() {
        ICondition cond = new ModLoadedCondition("ftbmaterials");
        String prefix = "ftbmaterials_compat/";

        registerExternalMetalById(HephaestusFluids.SETS.get(HephaestusFluids.Material.MOLTEN_ALUMINUM).source.get(),    660, "aluminum",   true,  prefix, cond);
        registerExternalMetalById(HephaestusFluids.SETS.get(HephaestusFluids.Material.MOLTEN_BRONZE).source.get(),      950, "bronze",     false, prefix, cond);
        registerExternalMetalById(HephaestusFluids.SETS.get(HephaestusFluids.Material.MOLTEN_CONSTANTAN).source.get(), 1220, "constantan", false, prefix, cond);
        registerExternalMetalById(HephaestusFluids.SETS.get(HephaestusFluids.Material.MOLTEN_IRIDIUM).source.get(),    2440, "iridium",    true,  prefix, cond);
        registerExternalMetalById(HephaestusFluids.SETS.get(HephaestusFluids.Material.MOLTEN_LEAD).source.get(),        327, "lead",       true,  prefix, cond);
        registerExternalMetalById(HephaestusFluids.SETS.get(HephaestusFluids.Material.MOLTEN_NICKEL).source.get(),     1000, "nickel",     true,  prefix, cond);
        //registerExternalMetalById(HephaestusFluids.SETS.get(HephaestusFluids.Material.MOLTEN_OSMIUM).source.get(),     1990, "osmium",     true,  prefix, cond);
        registerExternalMetalById(HephaestusFluids.SETS.get(HephaestusFluids.Material.MOLTEN_PLATINUM).source.get(),   1768, "platinum",   true,  prefix, cond);
        registerExternalMetalById(HephaestusFluids.SETS.get(HephaestusFluids.Material.MOLTEN_SILVER).source.get(),      960, "silver",     true,  prefix, cond);
        registerExternalMetalById(HephaestusFluids.SETS.get(HephaestusFluids.Material.MOLTEN_TIN).source.get(),         230, "tin",        true,  prefix, cond);
        registerExternalMetalById(HephaestusFluids.SETS.get(HephaestusFluids.Material.MOLTEN_URANIUM).source.get(),    1130, "uranium",    true,  prefix, cond);
        registerExternalMetalById(HephaestusFluids.SETS.get(HephaestusFluids.Material.MOLTEN_ZINC).source.get(),        419, "zinc",       true,  prefix, cond);
        registerExternalMetalById(HephaestusFluids.SETS.get(HephaestusFluids.Material.MOLTEN_BRASS).source.get(),       930, "brass",      false, prefix, cond);
        registerExternalMetalById(HephaestusFluids.SETS.get(HephaestusFluids.Material.MOLTEN_ELECTRUM).source.get(),   1000, "electrum",   false, prefix, cond);
        registerExternalMetalById(HephaestusFluids.SETS.get(HephaestusFluids.Material.MOLTEN_INVAR).source.get(),      1000, "invar",      false, prefix, cond);
        //registerExternalMetalById(HephaestusFluids.SETS.get(HephaestusFluids.Material.MOLTEN_LUMIUM).source.get(),     1000, "lumium",     false, prefix, cond);
        registerExternalMetalById(HephaestusFluids.SETS.get(HephaestusFluids.Material.MOLTEN_CHROMIUM).source.get(),   1000, "chromium",   true,  prefix, cond);
        registerExternalMetalById(HephaestusFluids.SETS.get(HephaestusFluids.Material.MOLTEN_TITANIUM).source.get(),   1000, "titanium",   true,  prefix, cond);
        registerExternalMetalById(Identifier.fromNamespaceAndPath("colossal_reactors", "molten_stainless_steel"),
                "ftbmaterials", 1000, "stainless_steel", false, prefix,
                new net.neoforged.neoforge.common.conditions.AndCondition(
                        java.util.List.of(cond, new ModLoadedCondition("colossal_reactors"))));

        String mod = "ftbmaterials";
        int bt = 100;
        for (String name : new String[]{"copper", "gold", "iron"}) {
            Identifier fluid = moltenId(HephaestusFluids.Material.valueOf("MOLTEN_" + name.toUpperCase()));
            String path = prefix + "metal/" + name + "/";

            addMeltingTag("dusts/"  + name, fluid,  90, 900, bt,     path + "dust",  cond);
            addMeltingTag("plates/" + name, fluid,  90, 900, bt,     path + "plate", cond);
            addMeltingTag("gears/"  + name, fluid, 360, 900, bt * 2, path + "gear",  cond);
            addMeltingTag("rods/"   + name, fluid,  45, 900, bt / 2, path + "rod",   cond);

            addCastingTable(fluid,  90, ModItems.PLATE_CAST.get(), false, id(mod, name + "_plate"), bt,     path + "plate_cast", cond);
            addCastingTable(fluid, 360, ModItems.GEAR_CAST.get(),  false, id(mod, name + "_gear"),  bt * 2, path + "gear_cast",  cond);
            addCastingTable(fluid,  45, ModItems.ROD_CAST.get(),   false, id(mod, name + "_rod"),   bt / 2, path + "rod_cast",   cond);
        }

        Identifier steel = moltenId(HephaestusFluids.Material.MOLTEN_STEEL);
        String steelPath = prefix + "metal/steel/";
        addMeltingTag("plates/steel", steel,  90, 1000, bt,     steelPath + "plate", cond);
        addMeltingTag("gears/steel",  steel, 360, 1000, bt * 2, steelPath + "gear",  cond);
        addMeltingTag("rods/steel",   steel,  45, 1000, bt / 2, steelPath + "rod",   cond);
        addCastingTable(steel,  90, ModItems.PLATE_CAST.get(), false, id(mod, "steel_plate"), bt,     steelPath + "plate_cast", cond);
        addCastingTable(steel, 360, ModItems.GEAR_CAST.get(),  false, id(mod, "steel_gear"),  bt * 2, steelPath + "gear_cast",  cond);
        addCastingTable(steel,  45, ModItems.ROD_CAST.get(),   false, id(mod, "steel_rod"),   bt / 2, steelPath + "rod_cast",   cond);

        Identifier netherite = moltenId(HephaestusFluids.Material.MOLTEN_NETHERITE);
        String netheritePath = prefix + "metal/netherite/";
        addMeltingTag("nuggets/netherite", netherite,  10, 2000, bt / 3, netheritePath + "nugget", cond);
        addMeltingTag("dusts/netherite",   netherite,  90, 2000, bt,     netheritePath + "dust",   cond);
        addMeltingTag("plates/netherite",  netherite,  90, 2000, bt,     netheritePath + "plate",  cond);
        addMeltingTag("gears/netherite",   netherite, 360, 2000, bt * 2, netheritePath + "gear",   cond);
        addMeltingTag("rods/netherite",    netherite,  45, 2000, bt / 2, netheritePath + "rod",    cond);
        addCastingTable(netherite,  10, ModItems.NUGGET_CAST.get(), false, id(mod, "netherite_nugget"), bt / 3, netheritePath + "nugget_cast", cond);
        addCastingTable(netherite,  90, ModItems.PLATE_CAST.get(),  false, id(mod, "netherite_plate"),  bt,     netheritePath + "plate_cast",  cond);
        addCastingTable(netherite, 360, ModItems.GEAR_CAST.get(),   false, id(mod, "netherite_gear"),   bt * 2, netheritePath + "gear_cast",   cond);
        addCastingTable(netherite,  45, ModItems.ROD_CAST.get(),    false, id(mod, "netherite_rod"),    bt / 2, netheritePath + "rod_cast",    cond);

        registerGem(moltenId(HephaestusFluids.Material.MOLTEN_DIMENSIONAL_SHARD), 1000, "dimensional_shard",
                id(mod, "dimensional_shard_block"), id(mod, "dimensional_shard_gem"), prefix, cond);
        registerGem(moltenId(HephaestusFluids.Material.MOLTEN_RUBY), 1000, "ruby",
                id(mod, "ruby_block"), id(mod, "ruby_gem"), prefix, cond);
        registerGem(moltenId(HephaestusFluids.Material.MOLTEN_SAPPHIRE), 1000, "sapphire",
                id(mod, "sapphire_block"), id(mod, "sapphire_gem"), prefix, cond);

        addMeltingTag("ingots/graphite", fluidOf("MOLTEN_CARBON"), 90, 700, 100,
                prefix + "misc/carbon_from_graphite", cond);
    }

    private void registerFtbIcCompat() {
        ICondition cond = new ModLoadedCondition("ftbic");
        String prefix = "ftbic_compat/";

        registerExternalMetalById(fluidId(fluidOf("MOLTEN_ENDERIUM")), "ftbic", 1450, "enderium", false, prefix, cond);
    }

    private void registerAe2Compat() {
        ICondition cond = new ModLoadedCondition("ae2");
        String mod = "ae2", prefix = "ae2_compat/";

        registerGem(moltenId(HephaestusFluids.Material.MOLTEN_CERTUS_QUARTZ), 800, "certus_quartz",
                null, id(mod, "certus_quartz_crystal"), prefix, cond);
        registerGem(moltenId(HephaestusFluids.Material.MOLTEN_FLUIX), 1000, "fluix",
                null, id(mod, "fluix_crystal"), prefix, cond);
    }

    private void registerArsMagicaLegacyCompat() {
        ICondition cond = new ModLoadedCondition("arsmagicalegacy");
        String mod = "arsmagicalegacy", prefix = "arsmagicalegacy_compat/";

        registerGem(moltenId(HephaestusFluids.Material.MOLTEN_MOONSTONE), 800, "moonstone",
                id(mod, "moonstone_block"), id(mod, "moonstone"), prefix, cond);
        registerGem(moltenId(HephaestusFluids.Material.MOLTEN_TOPAZ), 1000, "topaz",
                id(mod, "topaz_block"), id(mod, "topaz"), prefix, cond);
    }

    private void registerCakeStickLibCompat() {
        ICondition cond = new ModLoadedCondition("cakesticklib");
        String mod = "cakesticklib", prefix = "cakesticklib_compat/";

        registerMetal(moltenId(HephaestusFluids.Material.MOLTEN_WROUGHT_IRON), 800, "wrought_iron",
                id(mod, "wrought_iron_block"), id(mod, "wrought_iron_ingot"), null,
                id(mod, "wrought_iron_nugget"), null, null, null, null, prefix, cond);
    }

    private void registerColossalReactorsCompat() {
        ICondition cond = new ModLoadedCondition("colossal_reactors");
        String mod = "colossal_reactors", prefix = "colossal_reactors_compat/";

        registerMetal(id(mod, "molten_tough_alloy"), 1000, "tough_alloy",
                id(mod, "tough_alloy_block"), id(mod, "tough_alloy_ingot"), null,
                null, null, null, null, null, prefix, cond);
    }

    private void registerEnderIoCompat() {
        ICondition cond = new ModLoadedCondition("enderio");
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
            registerMetal(moltenId(alloys[i]), temps[i], name,
                    id(mod, name + "_block"), id(mod, name + "_ingot"), null,
                    id(mod, name + "_nugget"), null, null, null, null, prefix, cond);
        }
    }

    private void registerEnderIoEvolutionCompat() {
        ICondition cond = new ModLoadedCondition("enderio_evolution");
        String mod = "enderio_evolution", prefix = "enderio_evolution_compat/";

        HephaestusFluids.Material[] alloys = {
                HephaestusFluids.Material.MOLTEN_ENERGETIC_SILVER,
                HephaestusFluids.Material.MOLTEN_VIVID_ALLOY
        };
        int[] temps = {800, 1000};
        for (int i = 0; i < alloys.length; i++) {
            String name = alloys[i].name;
            registerMetal(moltenId(alloys[i]), temps[i], name,
                    id(mod, name + "_block"), id(mod, name + "_ingot"), null,
                    id(mod, name + "_nugget"), null, null, null, null, prefix, cond);
        }
    }

    private void registerEnergizedPowerCompat() {
        ICondition cond = new ModLoadedCondition("energizedpower");
        String mod = "energizedpower", prefix = "energizedpower_compat/";

        registerMetal(moltenId(HephaestusFluids.Material.MOLTEN_ADVANCED_ALLOY), 1000, "advanced_alloy",
                id(mod, "advanced_alloy_block"), id(mod, "advanced_alloy_ingot"), null,
                null, null, null, null, null, prefix, cond);
        registerMetal(moltenId(HephaestusFluids.Material.MOLTEN_CRYSTALLIZED_ALLOY), 1000, "crystallized_alloy",
                null, id(mod, "crystallized_alloy_ingot"), null,
                null, null, null, null, null, prefix, cond);
        registerMetal(moltenId(HephaestusFluids.Material.MOLTEN_ENERGIZED_ALLOY), 1000, "energized_alloy",
                null, id(mod, "energized_alloy_ingot"), null,
                null, null, null, null, null, prefix, cond);
        registerMetal(moltenId(HephaestusFluids.Material.MOLTEN_ENERGIZED_COPPER), 800, "energized_copper",
                null, id(mod, "energized_copper_ingot"), null,
                null, null, null, null, null, prefix, cond);
        registerMetal(moltenId(HephaestusFluids.Material.MOLTEN_ENERGIZED_GOLD), 800, "energized_gold",
                null, id(mod, "energized_gold_ingot"), null,
                null, null, null, null, null, prefix, cond);
    }

    private void registerEvilCraftCompat() {
        ICondition cond = new ModLoadedCondition("evilcraft");
        String mod = "evilcraft", prefix = "evilcraft_compat/";

        registerGem(moltenId(HephaestusFluids.Material.MOLTEN_DARK_GEM), 800, "dark_gem",
                id(mod, "dark_block"), id(mod, "dark_gem"), prefix, cond);
    }

    private void registerExtendedAeCompat() {
        ICondition cond = new ModLoadedCondition("extendedae");
        String mod = "extendedae", prefix = "extendedae_compat/";

        registerGem(moltenId(HephaestusFluids.Material.MOLTEN_ENTRO), 1000, "entro",
                null, id(mod, "entro_crystal"), prefix, cond);
    }

    private void registerIronsJewelryCompat() {
        ICondition cond = new ModLoadedCondition("irons_jewelry");
        String mod = "irons_jewelry", prefix = "irons_jewelry_compat/";

        registerGem(moltenId(HephaestusFluids.Material.MOLTEN_GARNET), 1000, "garnet",
                null, id(mod, "garnet"), prefix, cond);
        registerGem(moltenId(HephaestusFluids.Material.MOLTEN_ONYX), 1000, "onyx",
                null, id(mod, "onyx"), prefix, cond);
        registerGem(moltenId(HephaestusFluids.Material.MOLTEN_PERIDOT), 1000, "peridot",
                null, id(mod, "peridot"), prefix, cond);
    }

    private void registerJustDireThingsCompat() {
        ICondition cond = new ModLoadedCondition("justdirethings");
        String mod = "justdirethings", prefix = "justdirethings_compat/";

        registerMetal(moltenId(HephaestusFluids.Material.MOLTEN_BLAZEGOLD), 1000, "blazegold",
                null, id(mod, "blazegold_ingot"), null,
                null, null, null, null, null, prefix, cond);
        registerMetal(moltenId(HephaestusFluids.Material.MOLTEN_ECLIPSE_ALLOY), 1600, "eclipsealloy",
                null, id(mod, "eclipsealloy_ingot"), null,
                null, null, null, null, null, prefix, cond);
        registerMetal(moltenId(HephaestusFluids.Material.MOLTEN_FERRICORE), 800, "ferricore",
                null, id(mod, "ferricore_ingot"), null,
                null, null, null, null, null, prefix, cond);
    }

    private void registerLtxiCompat() {
        ICondition cond = new ModLoadedCondition("ltxi");
        String mod = "ltxi", prefix = "ltxi_compat/";

        registerMetal(moltenId(HephaestusFluids.Material.MOLTEN_NIOBIUM), 1000, "niobium",
                id(mod, "niobium_block"), id(mod, "niobium_ingot"), null,
                id(mod, "niobium_nugget"), null, null, null, null, prefix, cond);
        registerMetal(moltenId(HephaestusFluids.Material.MOLTEN_SLATESTEEL), 1000, "slatesteel",
                id(mod, "slatesteel_block"), id(mod, "slatesteel_ingot"), null,
                id(mod, "slatesteel_nugget"), null, null, null, null, prefix, cond);
        registerMetal(moltenId(HephaestusFluids.Material.MOLTEN_TUNGSTEN_SLATESTEEL), 1600, "tungsten_slatesteel",
                null, id(mod, "tungsten_slatesteel_ingot"), null,
                null, null, null, null, null, prefix, cond);
        registerGem(moltenId(HephaestusFluids.Material.MOLTEN_OLIVINE), 800, "olivine",
                null, id(mod, "olivine"), prefix, cond);
    }

    private void registerMysticalAgradditionsCompat() {
        ICondition cond = new ModLoadedCondition("mysticalagradditions");
        String mod = "mysticalagradditions", prefix = "mysticalagradditions_compat/";

        String[] essences = {"prosperity", "soulium", "tertium", "prudentium",
                "imperium", "supremium", "awakened_supremium"};
        int[] temps       = {800, 800, 800, 800, 1000, 1000, 1600};
        for (int i = 0; i < essences.length; i++) {
            registerGem(id(mod, "molten_" + essences[i]), temps[i], essences[i],
                    null, id("mysticalagriculture", essences[i] + "_gemstone"), prefix, cond);
        }
        registerGem(id(mod, "molten_insanium"), 1600, "insanium",
                null, id(mod, "insanium_gemstone"), prefix, cond);
    }

    private void registerNeoVitaeCompat() {
        ICondition cond = new ModLoadedCondition("neovitae");
        String mod = "neovitae", prefix = "neovitae_compat/";

        registerMetal(moltenId(HephaestusFluids.Material.MOLTEN_HELLFORGED), 1600, "hellforged",
                id(mod, "hellforged_block"), id(mod, "ingot_hellforged"), null,
                null, null, null, null, null, prefix, cond);
    }

    private void registerNodeworksCompat() {
        ICondition cond = new ModLoadedCondition("nodeworks");
        String mod = "nodeworks", prefix = "nodeworks_compat/";

        registerGem(moltenId(HephaestusFluids.Material.MOLTEN_CELESTINE), 800, "celestine",
                null, id(mod, "celestine_shard"), prefix, cond);
    }

    private void registerOccultismCompat() {
        ICondition cond = new ModLoadedCondition("occultism");
        String mod = "occultism", prefix = "occultism_compat/";

        registerMetal(moltenId(HephaestusFluids.Material.MOLTEN_IESNIUM), 1200, "iesnium",
                id(mod, "iesnium_block"), id(mod, "iesnium_ingot"), null,
                id(mod, "iesnium_nugget"), null, null, null, null, prefix, cond);
        registerGem(moltenId(HephaestusFluids.Material.MOLTEN_SPIRIT_ATTUNED), 1000, "spirit_attuned",
                id(mod, "spirit_attuned_block"), id(mod, "spirit_attuned_gem"), prefix, cond);
    }

    private void registerOritechCompat() {
        ICondition cond = new ModLoadedCondition("oritech");
        String mod = "oritech", prefix = "oritech_compat/";

        registerMetal(moltenId(HephaestusFluids.Material.MOLTEN_ADAMANT), 1000, "adamant",
                id(mod, "adamant"), id(mod, "adamant_ingot"), null,
                null, null, null, null, null, prefix, cond);
        registerMetal(moltenId(HephaestusFluids.Material.MOLTEN_DURATIUM), 1600, "duratium",
                id(mod, "duratium"), id(mod, "duratium_ingot"), null,
                null, null, null, null, null, prefix, cond);
        registerMetal(moltenId(HephaestusFluids.Material.MOLTEN_PROMETHEUM), 1600, "prometheum",
                null, id(mod, "prometheum_ingot"), null,
                null, null, null, null, null, prefix, cond);
        registerGem(moltenId(HephaestusFluids.Material.MOLTEN_FLUXITE), 1000, "fluxite",
                id(mod, "fluxite_block"), id(mod, "fluxite"), prefix, cond);
    }

    private void registerReplicationCompat() {
        ICondition cond = new ModLoadedCondition("replication");
        String mod = "replication", prefix = "replication_compat/";

        registerMetal(moltenId(HephaestusFluids.Material.MOLTEN_REPLICA), 1000, "replica",
                id(mod, "replica_block"), id(mod, "replica_ingot"), id(mod, "raw_replica"),
                null, null, null, null, null, prefix, cond);
    }

    private void registerStellarisCompat() {
        ICondition cond = new ModLoadedCondition("stellaris");
        String mod = "stellaris", prefix = "stellaris_compat/";

        registerMetal(moltenId(HephaestusFluids.Material.MOLTEN_DESH), 1000, "desh",
                id(mod, "desh_block"), id(mod, "desh_ingot"), null,
                null, null, null, null, null, prefix, cond);
    }

    private void registerThaumaturgeCompat() {
        ICondition cond = new ModLoadedCondition("thaumaturge");
        String mod = "thaumaturge", prefix = "thaumaturge_compat/";

        registerMetal(moltenId(HephaestusFluids.Material.MOLTEN_THAUMIUM), 1000, "thaumium",
                id(mod, "metal_thaumium"), id(mod, "ingot_thaumium"), null,
                id(mod, "nugget_thaumium"), null, null, null, null, prefix, cond);
        registerMetal(moltenId(HephaestusFluids.Material.MOLTEN_VOID_METAL), 1600, "void_metal",
                id(mod, "metal_void"), id(mod, "ingot_void"), null,
                id(mod, "nugget_void"), null, null, null, null, prefix, cond);
    }

    private void registerXyCraftWorldCompat() {
        ICondition cond = new ModLoadedCondition("xycraft_world");
        String mod = "xycraft_world", prefix = "xycraft_world_compat/";

        registerGem(moltenId(HephaestusFluids.Material.MOLTEN_XYCHORIUM), 800, "xychorium",
                null, id(mod, "xychorium_gem_blue"), prefix, cond);
    }

    private void registerGeoreCompat() {
        ICondition cond = new ModLoadedCondition("geore");
        String prefix = "geore_compat/";

        String[] names = {"aluminum", "copper", "gold", "iron", "lead", "nickel",
                "platinum", "silver", "tin", "uranium", "zinc"};
        int[] temps    = {660, 900, 900, 900, 327, 1000, 1768, 960, 230, 1130, 419};
        for (int i = 0; i < names.length; i++) {
            String name = names[i];
            int temp    = temps[i];

            Ingredient ingredient = Ingredient.of(
                    this.registries.lookupOrThrow(Registries.ITEM).getOrThrow(
                            ItemTags.create(Identifier.fromNamespaceAndPath("geore", "geore_shards/" + name))
                    )
            );
            ModRecipes.MeltingRecipe recipe = new ModRecipes.MeltingRecipe(
                    ingredient, fluidId(fluidOf("MOLTEN_" + name.toUpperCase())), 90, fuelId(temp), 50, temp, 100);

            this.output.withConditions(cond).accept(
                    rk("smeltery/melting/" + prefix + "metal/" + name + "/shard"), recipe, null);
        }
    }

    private void registerFtbArmoryCompat() {
        ICondition cond = new ModLoadedCondition("ftbarmory");
        String prefix = "ftbarmory_compat/";

        HephaestusFluids.Material[] metals = {
                HephaestusFluids.Material.MOLTEN_AETERNIUM,
                HephaestusFluids.Material.MOLTEN_ADAMANTITE,
                HephaestusFluids.Material.MOLTEN_AURICHALCUM
        };
        for (HephaestusFluids.Material metal : metals) {
            registerExternalMetalById(
                    Identifier.fromNamespaceAndPath(TitamMods.MODID, "molten_" + metal.name),
                    "ftbarmory", metal.temperature, metal.name, true, prefix, cond);
        }
    }

    private void registerExternalMetalById(Fluid fluid, int temp, String name,
                                           boolean hasRaw, String prefix, ICondition cond) {
        registerExternalMetalById(fluidId(fluid), "ftbmaterials", temp, name, hasRaw, prefix, cond);
    }

    private void registerExternalMetalById(Identifier fluid, String modid, int temp, String name,
                                           boolean hasRaw, String prefix, ICondition cond) {
        registerMetal(fluid, temp, name,
                id(modid, name + "_block"), id(modid, name + "_ingot"),
                hasRaw ? id(modid, "raw_" + name) : null,
                id(modid, name + "_nugget"), id(modid, name + "_dust"),
                id(modid, name + "_plate"), id(modid, name + "_gear"), id(modid, name + "_rod"),
                prefix, cond);
    }

    private void addEntityMeltingRecipes() {
        addEntityMeltingRecipe(
                net.minecraft.world.entity.EntityType.BLAZE,
                Identifier.fromNamespaceAndPath(TitamMods.MODID, "molten_blaze"),
                90, 2, "blaze");

        addEntityMeltingRecipe(
                net.minecraft.world.entity.EntityType.MAGMA_CUBE,
                Identifier.fromNamespaceAndPath("minecraft", "lava"),
                45, 2, "magma_cube");
    }

    private void addEntityMeltingRecipe(net.minecraft.world.entity.EntityType<?> entityType,
                                        Identifier fluidId, int amount, int damage,
                                        String savePath) {
        com.titammods.setup.ModRecipes.EntityMeltingRecipe recipe =
                new com.titammods.setup.ModRecipes.EntityMeltingRecipe(entityType, fluidId, amount, damage);
        this.output.accept(
                ResourceKey.create(Registries.RECIPE,
                        Identifier.fromNamespaceAndPath(TitamMods.MODID, "entity_melting/" + savePath)),
                recipe, null);
    }

    private Identifier fuelId(int temp) {
        return temp <= 1000
                ? Identifier.fromNamespaceAndPath("minecraft", "lava")
                : Identifier.fromNamespaceAndPath("hephaestus", "molten_blaze");
    }

    private net.minecraft.resources.ResourceKey<net.minecraft.world.item.crafting.Recipe<?>> rk(String path) {
        return ResourceKey.create(Registries.RECIPE,
                Identifier.fromNamespaceAndPath(TitamMods.MODID, path));
    }

    private Identifier fluidId(Fluid fluid) {
        return BuiltInRegistries.FLUID.getResourceKey(fluid)
                .map(ResourceKey::identifier)
                .orElseThrow(() -> new IllegalStateException("Fluid not registered: " + fluid));
    }

    private Identifier moltenId(HephaestusFluids.Material material) {
        return Identifier.fromNamespaceAndPath(TitamMods.MODID, "molten_" + material.name);
    }

    private Identifier id(String namespace, String path) {
        return Identifier.fromNamespaceAndPath(namespace, path);
    }

    private void addMeltingTag(String tagPath, Fluid fluid, int amount, int temperature,
                               int time, String savePath, ICondition... conditions) {
        addMeltingTag(tagPath, fluidId(fluid), amount, temperature, time, savePath, conditions);
    }

    private void addMeltingTag(String tagPath, Identifier fluid, int amount, int temperature,
                               int time, String savePath, ICondition... conditions) {

        Ingredient ingredient = Ingredient.of(
                this.registries.lookupOrThrow(Registries.ITEM).getOrThrow(
                        ItemTags.create(Identifier.fromNamespaceAndPath("c", tagPath))
                )
        );

        ModRecipes.MeltingRecipe recipe = new ModRecipes.MeltingRecipe(
                ingredient, fluid, amount, fuelId(temperature), 50, temperature, time);

        this.output.withConditions(conditions).accept(
                ResourceKey.create(Registries.RECIPE,
                        Identifier.fromNamespaceAndPath(TitamMods.MODID, "smeltery/melting/" + savePath)),
                recipe, null);
    }

    private void addMeltingItem(ItemLike item, Fluid fluid, int amount, int temperature,
                                int time, String savePath, ICondition... conditions) {
        Ingredient ingredient = Ingredient.of(item);

        ModRecipes.MeltingRecipe recipe = new ModRecipes.MeltingRecipe(
                ingredient,
                fluidId(fluid), amount,
                fuelId(temperature), 50,
                temperature, time);

        this.output.withConditions(conditions).accept(
                ResourceKey.create(Registries.RECIPE,
                        Identifier.fromNamespaceAndPath(TitamMods.MODID, "smeltery/melting/" + savePath)),
                recipe, null);
    }

    private void addMeltingDamageable(ItemLike item, Fluid fluid, int amount, int temperature,
                                      int time, String savePath, ICondition... conditions) {
        Ingredient ingredient = Ingredient.of(item);
        ModRecipes.MeltingRecipe recipe = new ModRecipes.MeltingRecipe(
                ingredient, fluidId(fluid), amount,
                fuelId(temperature), 50, temperature, time, true);
        this.output.withConditions(conditions).accept(
                ResourceKey.create(Registries.RECIPE,
                        Identifier.fromNamespaceAndPath(TitamMods.MODID, "smeltery/melting/" + savePath)),
                recipe, null);
    }

    private void createCastRecipe(String tagPath, net.minecraft.world.level.ItemLike castResult, String savePath) {
        Ingredient tagIngredient = Ingredient.of(
                this.registries.lookupOrThrow(Registries.ITEM).getOrThrow(
                        net.minecraft.tags.ItemTags.create(Identifier.fromNamespaceAndPath("c", tagPath))
                )
        );
        Identifier copperFluidId = fluidId(
                com.titammods.registry.HephaestusFluids.SETS.get(
                        com.titammods.registry.HephaestusFluids.Material.MOLTEN_COPPER).source.get()
        );
        Identifier resultId = BuiltInRegistries.ITEM.getKey(castResult.asItem());
        ModRecipes.CastingTableRecipe recipe = new ModRecipes.CastingTableRecipe(
                java.util.Optional.of(tagIngredient), true,
                copperFluidId, 90,
                resultId, 1,
                60);
        this.output.accept(
                net.minecraft.resources.ResourceKey.create(Registries.RECIPE,
                        Identifier.fromNamespaceAndPath(TitamMods.MODID, "smeltery/casting/casts/" + savePath)),
                recipe, null);
    }

    private void createRodCastRecipe() {
        Ingredient rodOrStick = Ingredient.of(
                net.minecraft.world.item.Items.STICK,
                net.minecraft.world.item.Items.BLAZE_ROD
        );
        Identifier copperFluidId = fluidId(
                com.titammods.registry.HephaestusFluids.SETS.get(
                        com.titammods.registry.HephaestusFluids.Material.MOLTEN_COPPER).source.get()
        );
        Identifier resultId = BuiltInRegistries.ITEM.getKey(ModItems.ROD_CAST.get().asItem());
        ModRecipes.CastingTableRecipe recipe = new ModRecipes.CastingTableRecipe(
                java.util.Optional.of(rodOrStick), true,
                copperFluidId, 90,
                resultId, 1,
                60);
        this.output.accept(
                net.minecraft.resources.ResourceKey.create(Registries.RECIPE,
                        Identifier.fromNamespaceAndPath(TitamMods.MODID, "smeltery/casting/casts/rod_cast")),
                recipe, null);
    }

    private void addCastingTable(Fluid fluid, int fluidAmount,
                                 ItemLike castItem, boolean consumesCast,
                                 ItemLike resultItem, int time,
                                 String savePath, ICondition... conditions) {
        addCastingTable(fluidId(fluid), fluidAmount, castItem, consumesCast,
                BuiltInRegistries.ITEM.getKey(resultItem.asItem()), time, savePath, conditions);
    }

    private void addCastingTable(Identifier fluid, int fluidAmount,
                                 ItemLike castItem, boolean consumesCast,
                                 Identifier result, int time,
                                 String savePath, ICondition... conditions) {
        java.util.Optional<net.minecraft.world.item.crafting.Ingredient> castOpt =
                (castItem == null)
                        ? java.util.Optional.empty()
                        : java.util.Optional.of(net.minecraft.world.item.crafting.Ingredient.of(castItem));

        ModRecipes.CastingTableRecipe recipe = new ModRecipes.CastingTableRecipe(
                castOpt, consumesCast, fluid, fluidAmount, result, 1, time);

        this.output.withConditions(conditions).accept(
                ResourceKey.create(Registries.RECIPE,
                        Identifier.fromNamespaceAndPath(TitamMods.MODID, "smeltery/casting/table/" + savePath)),
                recipe, null);
    }

    private void addCastingBasin(Fluid fluid, int fluidAmount,
                                 ItemLike resultItem, int time,
                                 String savePath, ICondition... conditions) {
        addCastingBasin(fluidId(fluid), fluidAmount,
                BuiltInRegistries.ITEM.getKey(resultItem.asItem()), time, savePath, conditions);
    }

    private void addCastingBasin(Identifier fluid, int fluidAmount,
                                 Identifier result, int time,
                                 String savePath, ICondition... conditions) {
        ModRecipes.CastingBasinRecipe recipe = new ModRecipes.CastingBasinRecipe(
                fluid, fluidAmount, result, 1, time);

        this.output.withConditions(conditions).accept(
                ResourceKey.create(Registries.RECIPE,
                        Identifier.fromNamespaceAndPath(TitamMods.MODID, "smeltery/casting/basin/" + savePath)),
                recipe, null);
    }

    private void addCastingBasin(Fluid fluid, int fluidAmount,
                                 ItemLike castItem, ItemLike resultItem, int time,
                                 String savePath, ICondition... conditions) {
        Identifier fluidId  = fluidId(fluid);
        Identifier resultId = BuiltInRegistries.ITEM.getKey(resultItem.asItem());

        ModRecipes.CastingBasinRecipe recipe = new ModRecipes.CastingBasinRecipe(
                java.util.Optional.of(Ingredient.of(castItem)),
                fluidId, fluidAmount, resultId, 1, time);

        this.output.withConditions(conditions).accept(
                ResourceKey.create(Registries.RECIPE,
                        Identifier.fromNamespaceAndPath(TitamMods.MODID, "smeltery/casting/basin/" + savePath)),
                recipe, null);
    }

    private void registerMetal(Fluid fluid, int temp, String name,
                               ItemLike block, ItemLike ingot, ItemLike raw, ItemLike nugget,
                               ItemLike dust, ItemLike plate, ItemLike gear, ItemLike rod,
                               String prefix, ICondition... conditions) {
        int bt = 100;

        addMeltingTag("storage_blocks/" + name, fluid, 810, temp, bt * 2,        prefix + "metal/" + name + "/block",    conditions);
        if (raw != null && !name.equals("steel") && !name.equals("brass"))
            addMeltingTag("storage_blocks/raw_" + name, fluid, 810, temp, (int)(bt * 2.5), prefix + "metal/" + name + "/raw_block", conditions);
        addMeltingTag("ingots/" + name,           fluid,  90, temp, bt,            prefix + "metal/" + name + "/ingot",   conditions);
        if (raw != null && !name.equals("brass"))
            addMeltingTag("raw_materials/" + name, fluid,  90, temp, (int)(bt * 1.5), prefix + "metal/" + name + "/raw",      conditions);
        if (nugget != null) addMeltingTag("nuggets/" + name, fluid,  10, temp, bt / 3,  prefix + "metal/" + name + "/nugget", conditions);
        if (dust   != null) addMeltingTag("dusts/"   + name, fluid,  90, temp, bt,      prefix + "metal/" + name + "/dust",   conditions);
        if (plate  != null) addMeltingTag("plates/"  + name, fluid,  90, temp, bt,      prefix + "metal/" + name + "/plate",  conditions);
        if (gear   != null) addMeltingTag("gears/"   + name, fluid, 360, temp, bt * 2,  prefix + "metal/" + name + "/gear",   conditions);
        if (rod    != null) addMeltingTag("rods/"    + name, fluid,  45, temp, bt / 2,  prefix + "metal/" + name + "/rod",    conditions);

        if (ingot  != null) addCastingTable(fluid,  90, ModItems.INGOT_CAST.get(),  false, ingot,  bt,       prefix + "metal/" + name + "/ingot_cast",  conditions);
        if (nugget != null) addCastingTable(fluid,  10, ModItems.NUGGET_CAST.get(), false, nugget, bt / 3,   prefix + "metal/" + name + "/nugget_cast", conditions);
        if (plate  != null) addCastingTable(fluid,  90, ModItems.PLATE_CAST.get(),  false, plate,  bt,       prefix + "metal/" + name + "/plate_cast",  conditions);
        if (gear   != null) addCastingTable(fluid, 360, ModItems.GEAR_CAST.get(),   false, gear,   bt * 2,   prefix + "metal/" + name + "/gear_cast",   conditions);
        if (rod    != null) addCastingTable(fluid,  45, ModItems.ROD_CAST.get(),    false, rod,    bt / 2,   prefix + "metal/" + name + "/rod_cast",    conditions);
        if (block  != null) addCastingBasin(fluid, 810, block, bt * 2, prefix + "metal/" + name + "/block_cast", conditions);
    }

    private void registerMetal(Identifier fluid, int temp, String name,
                               Identifier block, Identifier ingot, Identifier raw, Identifier nugget,
                               Identifier dust, Identifier plate, Identifier gear, Identifier rod,
                               String prefix, ICondition... conditions) {
        int bt = 100;
        String path = prefix + "metal/" + name + "/";

        if (block  != null) addMeltingTag("storage_blocks/" + name, fluid, 810, temp, bt * 2, path + "block", conditions);
        if (raw != null && !name.equals("steel") && !name.equals("brass"))
            addMeltingTag("storage_blocks/raw_" + name, fluid, 810, temp, (int)(bt * 2.5), path + "raw_block", conditions);
        if (ingot  != null) addMeltingTag("ingots/"  + name, fluid,  90, temp, bt,              path + "ingot",  conditions);
        if (raw != null && !name.equals("brass"))
            addMeltingTag("raw_materials/" + name, fluid, 90, temp, (int)(bt * 1.5), path + "raw", conditions);
        if (nugget != null) addMeltingTag("nuggets/" + name, fluid,  10, temp, bt / 3,          path + "nugget", conditions);
        if (dust   != null) addMeltingTag("dusts/"   + name, fluid,  90, temp, bt,              path + "dust",   conditions);
        if (plate  != null) addMeltingTag("plates/"  + name, fluid,  90, temp, bt,              path + "plate",  conditions);
        if (gear   != null) addMeltingTag("gears/"   + name, fluid, 360, temp, bt * 2,          path + "gear",   conditions);
        if (rod    != null) addMeltingTag("rods/"    + name, fluid,  45, temp, bt / 2,          path + "rod",    conditions);

        if (ingot  != null) addCastingTable(fluid,  90, ModItems.INGOT_CAST.get(),  false, ingot,  bt,     path + "ingot_cast",  conditions);
        if (nugget != null) addCastingTable(fluid,  10, ModItems.NUGGET_CAST.get(), false, nugget, bt / 3, path + "nugget_cast", conditions);
        if (plate  != null) addCastingTable(fluid,  90, ModItems.PLATE_CAST.get(),  false, plate,  bt,     path + "plate_cast",  conditions);
        if (gear   != null) addCastingTable(fluid, 360, ModItems.GEAR_CAST.get(),   false, gear,   bt * 2, path + "gear_cast",   conditions);
        if (rod    != null) addCastingTable(fluid,  45, ModItems.ROD_CAST.get(),    false, rod,    bt / 2, path + "rod_cast",    conditions);
        if (block  != null) addCastingBasin(fluid, 810, block, bt * 2, path + "block_cast", conditions);
    }

    private void registerGem(Identifier fluid, int temp, String name, Identifier block, Identifier gem,
                             String prefix, ICondition... conditions) {
        int bt = 120;
        String path = prefix + "gem/" + name + "/";

        if (block != null) {
            addMeltingTag("storage_blocks/" + name, fluid, 810, temp, bt * 2, path + "block", conditions);
            addCastingBasin(fluid, 810, block, bt * 2, path + "block_cast", conditions);
        }
        addMeltingTag("gems/" + name, fluid, 90, temp, bt, path + "gem", conditions);
        addCastingTable(fluid, 90, ModItems.GEM_CAST.get(), false, gem, bt, path + "gem_cast", conditions);
    }

    private void registerGem(Fluid fluid, int temp, String name, String blockTag, String gemTag,
                             ItemLike block, ItemLike gem, ICondition... conditions) {
        registerGem(fluid, temp, name, blockTag, gemTag, block, gem, 810, conditions);
    }

    private void registerGem(Fluid fluid, int temp, String name, String blockTag, String gemTag,
                             ItemLike block, ItemLike gem, int blockAmount, ICondition... conditions) {
        int bt = 120;

        if (blockTag != null && !blockTag.isEmpty())
            addMeltingTag(blockTag, fluid, blockAmount, temp, bt * 2, "gem/" + name + "/block", conditions);
        else if (block != null)
            addMeltingItem(block, fluid, blockAmount, temp, bt * 2, "gem/" + name + "/block", conditions);

        if (gemTag != null && !gemTag.isEmpty())
            addMeltingTag(gemTag, fluid, 90, temp, bt, "gem/" + name + "/gem", conditions);
        else if (gem != null)
            addMeltingItem(gem, fluid, 90, temp, bt, "gem/" + name + "/gem", conditions);

        if (gem   != null) addCastingTable(fluid, 90, ModItems.GEM_CAST.get(), false, gem,  bt,       "gem/" + name + "/gem_cast",   conditions);
        if (block != null) addCastingBasin(fluid, blockAmount, block, bt * 2, "gem/" + name + "/block_cast", conditions);
    }

    private void registerExternalMetal(Fluid fluid, int temp, String name,
                                       String modid, String prefix, ICondition condition) {
        ItemLike block  = getExternalItem(modid, name + "_block");
        ItemLike ingot  = getExternalItem(modid, name + "_ingot");
        ItemLike raw    = getExternalItem(modid, "raw_" + name);
        ItemLike nugget = getExternalItem(modid, name + "_nugget");
        ItemLike dust   = getExternalItem(modid, name + "_dust");
        ItemLike plate  = getExternalItem(modid, name + "_plate");
        ItemLike gear   = getExternalItem(modid, name + "_gear");
        ItemLike rod    = getExternalItem(modid, name + "_rod");
        registerMetal(fluid, temp, name, block, ingot, raw, nugget, dust, plate, gear, rod, prefix, condition);
    }

    private ItemLike getExternalItem(String modid, String path) {
        var item = BuiltInRegistries.ITEM.getValue(Identifier.fromNamespaceAndPath(modid, path));
        return item == Items.AIR ? null : item;
    }

    private void addFixRecipes() {
        addMeltingItem(net.minecraft.world.item.Items.COAL,
                fluidOf("MOLTEN_CARBON"), 90, 600, 100, "misc/carbon_from_coal");
        addMeltingItem(net.minecraft.world.item.Items.CHARCOAL,
                fluidOf("MOLTEN_CARBON"), 90, 600, 100, "misc/carbon_from_charcoal");
        addMeltingTag("storage_blocks/coal",
                fluidOf("MOLTEN_CARBON"), 810, 600, 200, "misc/carbon_from_coal_block");
        addMeltingItem(net.minecraft.world.item.Items.SLIME_BALL,
                fluidOf("MOLTEN_SLIME"), 50, 300, 60, "misc/slime_from_ball");
        addMeltingItem(net.minecraft.world.item.Items.SLIME_BLOCK,
                fluidOf("MOLTEN_SLIME"), 450, 300, 120, "misc/slime_from_block");
        addMeltingItem(net.minecraft.world.item.Items.MAGMA_CREAM,
                fluidOf("MOLTEN_MAGMA_CREAM"), 90, 700, 80, "misc/magma_cream");
        addMeltingItem(net.minecraft.world.item.Items.MAGMA_BLOCK,
                net.minecraft.world.level.material.Fluids.LAVA, 250, 700, 120, "misc/magma_block");
        addMeltingItem(net.minecraft.world.item.Items.ENDER_PEARL,
                fluidOf("MOLTEN_ENDER"), 90, 1000, 100, "misc/ender_from_pearl");
        addMeltingItem(net.minecraft.world.item.Items.ENDER_EYE,
                fluidOf("MOLTEN_ENDER"), 90, 1000, 100, "misc/ender_from_eye");
        addMeltingItem(net.minecraft.world.item.Items.REDSTONE,
                fluidOf("MOLTEN_REDSTONE"), 10, 600, 40, "misc/redstone_from_dust");
        addMeltingTag("storage_blocks/redstone",
                fluidOf("MOLTEN_REDSTONE"), 90, 600, 120, "misc/redstone_from_block");
        addMeltingItem(net.minecraft.world.item.Items.GLOWSTONE_DUST,
                fluidOf("MOLTEN_GLOWSTONE"), 50, 800, 60, "misc/glowstone_from_dust");
        addMeltingItem(net.minecraft.world.item.Items.GLOWSTONE,
                fluidOf("MOLTEN_GLOWSTONE"), 200, 800, 120, "misc/glowstone_from_block");
        addMeltingItem(net.minecraft.world.item.Items.GLASS,
                fluidOf("MOLTEN_GLASS"), 250, 1000, 100, "misc/glass_from_block");
        addMeltingItem(net.minecraft.world.item.Items.GLASS_PANE,
                fluidOf("MOLTEN_GLASS"), 90, 1000, 60, "misc/glass_from_pane");
        addMeltingItem(net.minecraft.world.item.Items.SAND,
                fluidOf("MOLTEN_GLASS"), 250, 1000, 100, "misc/glass_from_sand");
        addMeltingItem(net.minecraft.world.item.Items.LAPIS_LAZULI,
                fluidOf("MOLTEN_LAPIS"), 10, 900, 40, "misc/lapis_from_gem");
        addMeltingTag("storage_blocks/lapis",
                fluidOf("MOLTEN_LAPIS"), 90, 900, 120, "misc/lapis_from_block");
        addMeltingItem(net.minecraft.world.item.Items.OBSIDIAN,
                fluidOf("MOLTEN_OBSIDIAN"), 288, 1400, 200, "misc/obsidian");
        addMeltingItem(net.minecraft.world.item.Items.ANCIENT_DEBRIS,
                fluidOf("MOLTEN_ANCIENT_DEBRIS"), 90, 2000, 300, "misc/ancient_debris");
        addMeltingItem(net.minecraft.world.item.Items.NETHERITE_SCRAP,
                fluidOf("MOLTEN_ANCIENT_DEBRIS"), 90, 2000, 200, "misc/ancient_debris_from_scrap");
        addMeltingItem(net.minecraft.world.item.Items.NETHERITE_INGOT,
                fluidOf("MOLTEN_NETHERITE"), 90, 2000, 200, "misc/netherite_from_ingot");
        addMeltingItem(net.minecraft.world.item.Items.SHULKER_SHELL,
                fluidOf("MOLTEN_SHULKER_SHELL"), 90, 1200, 120, "misc/shulker_shell");
        addMeltingItem(net.minecraft.world.item.Items.HONEYCOMB,
                fluidOf("MOLTEN_WAX"), 50, 320, 60, "misc/wax_from_honeycomb");
        addMeltingItem(net.minecraft.world.item.Items.HONEYCOMB_BLOCK,
                fluidOf("MOLTEN_WAX"), 200, 320, 120, "misc/wax_from_block");
        addMeltingItem(net.minecraft.world.item.Items.PORKCHOP,
                fluidOf("LIQUID_MEAT"), 40, 200, 60, "misc/meat_from_porkchop");
        addMeltingItem(net.minecraft.world.item.Items.BEEF,
                fluidOf("LIQUID_MEAT"), 40, 200, 60, "misc/meat_from_beef");
        addMeltingItem(net.minecraft.world.item.Items.CHICKEN,
                fluidOf("LIQUID_MEAT"), 40, 200, 60, "misc/meat_from_chicken");
        addMeltingItem(net.minecraft.world.item.Items.MUTTON,
                fluidOf("LIQUID_MEAT"), 40, 200, 60, "misc/meat_from_mutton");
        addMeltingItem(net.minecraft.world.item.Items.RABBIT,
                fluidOf("LIQUID_MEAT"), 40, 200, 60, "misc/meat_from_rabbit");
        addMeltingItem(net.minecraft.world.item.Items.ROTTEN_FLESH,
                fluidOf("LIQUID_MEAT"), 20, 200, 40, "misc/meat_from_rotten_flesh");

        addToolArmorMelting();
    }

    private void addToolArmorMelting() {
        Fluid iron = fluidOf("MOLTEN_IRON");
        Fluid gold = fluidOf("MOLTEN_GOLD");
        Fluid netherite = fluidOf("MOLTEN_NETHERITE");

        addMeltingDamageable(net.minecraft.world.item.Items.IRON_PICKAXE, iron, 270, 900, 200, "tools/iron_pickaxe");
        addMeltingDamageable(net.minecraft.world.item.Items.IRON_AXE,     iron, 270, 900, 200, "tools/iron_axe");
        addMeltingDamageable(net.minecraft.world.item.Items.IRON_SWORD,   iron, 180, 900, 150, "tools/iron_sword");
        addMeltingDamageable(net.minecraft.world.item.Items.IRON_SHOVEL,  iron,  90, 900, 100, "tools/iron_shovel");
        addMeltingDamageable(net.minecraft.world.item.Items.IRON_HOE,     iron, 180, 900, 150, "tools/iron_hoe");
        addMeltingDamageable(net.minecraft.world.item.Items.IRON_HELMET,     iron, 450, 900, 250, "armor/iron_helmet");
        addMeltingDamageable(net.minecraft.world.item.Items.IRON_CHESTPLATE, iron, 720, 900, 350, "armor/iron_chestplate");
        addMeltingDamageable(net.minecraft.world.item.Items.IRON_LEGGINGS,   iron, 630, 900, 300, "armor/iron_leggings");
        addMeltingDamageable(net.minecraft.world.item.Items.IRON_BOOTS,      iron, 360, 900, 200, "armor/iron_boots");
        addMeltingDamageable(net.minecraft.world.item.Items.CHAINMAIL_HELMET,     iron, 450, 900, 250, "armor/chainmail_helmet");
        addMeltingDamageable(net.minecraft.world.item.Items.CHAINMAIL_CHESTPLATE, iron, 720, 900, 350, "armor/chainmail_chestplate");
        addMeltingDamageable(net.minecraft.world.item.Items.CHAINMAIL_LEGGINGS,   iron, 630, 900, 300, "armor/chainmail_leggings");
        addMeltingDamageable(net.minecraft.world.item.Items.CHAINMAIL_BOOTS,      iron, 360, 900, 200, "armor/chainmail_boots");

        addMeltingDamageable(net.minecraft.world.item.Items.GOLDEN_PICKAXE, gold, 270, 900, 200, "tools/golden_pickaxe");
        addMeltingDamageable(net.minecraft.world.item.Items.GOLDEN_AXE,     gold, 270, 900, 200, "tools/golden_axe");
        addMeltingDamageable(net.minecraft.world.item.Items.GOLDEN_SWORD,   gold, 180, 900, 150, "tools/golden_sword");
        addMeltingDamageable(net.minecraft.world.item.Items.GOLDEN_SHOVEL,  gold,  90, 900, 100, "tools/golden_shovel");
        addMeltingDamageable(net.minecraft.world.item.Items.GOLDEN_HOE,     gold, 180, 900, 150, "tools/golden_hoe");
        addMeltingDamageable(net.minecraft.world.item.Items.GOLDEN_HELMET,     gold, 450, 900, 250, "armor/golden_helmet");
        addMeltingDamageable(net.minecraft.world.item.Items.GOLDEN_CHESTPLATE, gold, 720, 900, 350, "armor/golden_chestplate");
        addMeltingDamageable(net.minecraft.world.item.Items.GOLDEN_LEGGINGS,   gold, 630, 900, 300, "armor/golden_leggings");
        addMeltingDamageable(net.minecraft.world.item.Items.GOLDEN_BOOTS,      gold, 360, 900, 200, "armor/golden_boots");

        addMeltingDamageable(net.minecraft.world.item.Items.NETHERITE_PICKAXE, netherite, 90, 2000, 300, "tools/netherite_pickaxe");
        addMeltingDamageable(net.minecraft.world.item.Items.NETHERITE_AXE,     netherite, 90, 2000, 300, "tools/netherite_axe");
        addMeltingDamageable(net.minecraft.world.item.Items.NETHERITE_SWORD,   netherite, 90, 2000, 300, "tools/netherite_sword");
        addMeltingDamageable(net.minecraft.world.item.Items.NETHERITE_SHOVEL,  netherite, 90, 2000, 300, "tools/netherite_shovel");
        addMeltingDamageable(net.minecraft.world.item.Items.NETHERITE_HOE,     netherite, 90, 2000, 300, "tools/netherite_hoe");
        addMeltingDamageable(net.minecraft.world.item.Items.NETHERITE_HELMET,     netherite, 90, 2000, 350, "armor/netherite_helmet");
        addMeltingDamageable(net.minecraft.world.item.Items.NETHERITE_CHESTPLATE, netherite, 90, 2000, 350, "armor/netherite_chestplate");
        addMeltingDamageable(net.minecraft.world.item.Items.NETHERITE_LEGGINGS,   netherite, 90, 2000, 350, "armor/netherite_leggings");
        addMeltingDamageable(net.minecraft.world.item.Items.NETHERITE_BOOTS,      netherite, 90, 2000, 350, "armor/netherite_boots");

        Fluid diamond = com.titammods.setup.ModFluids.MOLTEN_DIAMOND.source.get();
        addMeltingDamageable(net.minecraft.world.item.Items.DIAMOND_PICKAXE, diamond, 270, 1400, 200, "tools/diamond_pickaxe");
        addMeltingDamageable(net.minecraft.world.item.Items.DIAMOND_AXE,     diamond, 270, 1400, 200, "tools/diamond_axe");
        addMeltingDamageable(net.minecraft.world.item.Items.DIAMOND_SWORD,   diamond, 180, 1400, 150, "tools/diamond_sword");
        addMeltingDamageable(net.minecraft.world.item.Items.DIAMOND_SHOVEL,  diamond,  90, 1400, 100, "tools/diamond_shovel");
        addMeltingDamageable(net.minecraft.world.item.Items.DIAMOND_HOE,     diamond, 180, 1400, 150, "tools/diamond_hoe");
        addMeltingDamageable(net.minecraft.world.item.Items.DIAMOND_HELMET,     diamond, 450, 1400, 250, "armor/diamond_helmet");
        addMeltingDamageable(net.minecraft.world.item.Items.DIAMOND_CHESTPLATE, diamond, 720, 1400, 350, "armor/diamond_chestplate");
        addMeltingDamageable(net.minecraft.world.item.Items.DIAMOND_LEGGINGS,   diamond, 630, 1400, 300, "armor/diamond_leggings");
        addMeltingDamageable(net.minecraft.world.item.Items.DIAMOND_BOOTS,      diamond, 360, 1400, 200, "armor/diamond_boots");
    }

    private Fluid fluidOf(String materialName) {
        return com.titammods.registry.HephaestusFluids.SETS.get(
                com.titammods.registry.HephaestusFluids.Material.valueOf(materialName)).source.get();
    }

    private void addMeltingMiscRecipes() {
        addMeltingItem(net.minecraft.world.item.Items.BLAZE_ROD,
                com.titammods.setup.ModFluids.MOLTEN_BLAZE.source.get(),
                250, 800, 100,
                "misc/molten_blaze");
    }

    private void addAlloyRecipes() {
        Identifier copper    = fluidId(com.titammods.registry.HephaestusFluids.SETS.get(com.titammods.registry.HephaestusFluids.Material.MOLTEN_COPPER).source.get());
        Identifier zinc      = fluidId(com.titammods.registry.HephaestusFluids.SETS.get(com.titammods.registry.HephaestusFluids.Material.MOLTEN_ZINC).source.get());
        Identifier tin       = fluidId(com.titammods.registry.HephaestusFluids.SETS.get(com.titammods.registry.HephaestusFluids.Material.MOLTEN_TIN).source.get());
        Identifier gold      = fluidId(com.titammods.registry.HephaestusFluids.SETS.get(com.titammods.registry.HephaestusFluids.Material.MOLTEN_GOLD).source.get());
        Identifier silver    = fluidId(com.titammods.registry.HephaestusFluids.SETS.get(com.titammods.registry.HephaestusFluids.Material.MOLTEN_SILVER).source.get());
        Identifier iron      = fluidId(com.titammods.registry.HephaestusFluids.SETS.get(com.titammods.registry.HephaestusFluids.Material.MOLTEN_IRON).source.get());
        Identifier nickel    = fluidId(com.titammods.registry.HephaestusFluids.SETS.get(com.titammods.registry.HephaestusFluids.Material.MOLTEN_NICKEL).source.get());
        Identifier brass     = fluidId(com.titammods.registry.HephaestusFluids.SETS.get(com.titammods.registry.HephaestusFluids.Material.MOLTEN_BRASS).source.get());
        Identifier bronze    = fluidId(com.titammods.registry.HephaestusFluids.SETS.get(com.titammods.registry.HephaestusFluids.Material.MOLTEN_BRONZE).source.get());
        Identifier electrum  = fluidId(com.titammods.registry.HephaestusFluids.SETS.get(com.titammods.registry.HephaestusFluids.Material.MOLTEN_ELECTRUM).source.get());
        Identifier invar     = fluidId(com.titammods.registry.HephaestusFluids.SETS.get(com.titammods.registry.HephaestusFluids.Material.MOLTEN_INVAR).source.get());
        Identifier constantan= fluidId(com.titammods.registry.HephaestusFluids.SETS.get(com.titammods.registry.HephaestusFluids.Material.MOLTEN_CONSTANTAN).source.get());
        Identifier steel     = fluidId(com.titammods.registry.HephaestusFluids.SETS.get(com.titammods.registry.HephaestusFluids.Material.MOLTEN_STEEL).source.get());
        Identifier carbon    = fluidId(com.titammods.registry.HephaestusFluids.SETS.get(com.titammods.registry.HephaestusFluids.Material.MOLTEN_CARBON).source.get());
        Identifier netherite = fluidId(com.titammods.registry.HephaestusFluids.SETS.get(com.titammods.registry.HephaestusFluids.Material.MOLTEN_NETHERITE).source.get());
        Identifier ancientDebris = fluidId(com.titammods.registry.HephaestusFluids.SETS.get(com.titammods.registry.HephaestusFluids.Material.MOLTEN_ANCIENT_DEBRIS).source.get());

        addAlloyRecipe(java.util.List.of(new FluidEntry(copper, 90), new FluidEntry(zinc, 90)),
                brass, 180, 650, "brass");
        addAlloyRecipe(java.util.List.of(new FluidEntry(copper, 270), new FluidEntry(tin, 90)),
                bronze, 360, 700, "bronze");
        addAlloyRecipe(java.util.List.of(new FluidEntry(gold, 90), new FluidEntry(silver, 90)),
                electrum, 180, 760, "electrum");
        addAlloyRecipe(java.util.List.of(new FluidEntry(iron, 180), new FluidEntry(nickel, 90)),
                invar, 270, 900, "invar");
        addAlloyRecipe(java.util.List.of(new FluidEntry(copper, 90), new FluidEntry(nickel, 90)),
                constantan, 180, 920, "constantan");
        addAlloyRecipe(java.util.List.of(new FluidEntry(iron, 90), new FluidEntry(carbon, 90)),
                steel, 90, 1000, "steel");
        addAlloyRecipe(java.util.List.of(new FluidEntry(gold, 270), new FluidEntry(ancientDebris, 270)),
                netherite, 360, 2000, "netherite");
    }

    private record FluidEntry(Identifier id, int amount) {}

    private void addAlloyRecipe(java.util.List<FluidEntry> inputs,
                                Identifier resultId, int resultAmount,
                                int temperature, String savePath) {
        var inputRefs = inputs.stream()
                .map(e -> new com.titammods.setup.ModRecipes.AlloyRecipe.FluidRef(e.id(), e.amount()))
                .toList();
        com.titammods.setup.ModRecipes.AlloyRecipe recipe =
                new com.titammods.setup.ModRecipes.AlloyRecipe(inputRefs, resultId, resultAmount, temperature);
        this.output.accept(
                ResourceKey.create(Registries.RECIPE,
                        Identifier.fromNamespaceAndPath(TitamMods.MODID, "smeltery/alloying/" + savePath)),
                recipe, null);
    }

    private void generateDecorative(net.minecraft.core.HolderGetter<net.minecraft.world.item.Item> items,
                                    Block baseBlock,
                                    Block slab, Block stairs, Block wall) {
        String baseName = BuiltInRegistries.BLOCK.getKey(baseBlock).getPath();
        String base = "has_" + baseName;
        if (slab != null)
            ShapedRecipeBuilder.shaped(items, RecipeCategory.MISC, slab, 6)
                    .pattern("BBB").define('B', baseBlock)
                    .unlockedBy(base, has(baseBlock))
                    .save(this.output, ResourceKey.create(Registries.RECIPE,
                            Identifier.fromNamespaceAndPath(TitamMods.MODID,
                                    "decoration/" + BuiltInRegistries.BLOCK.getKey(slab).getPath())));
        if (stairs != null)
            ShapedRecipeBuilder.shaped(items, RecipeCategory.MISC, stairs, 4)
                    .pattern("B  ").pattern("BB ").pattern("BBB").define('B', baseBlock)
                    .unlockedBy(base, has(baseBlock))
                    .save(this.output, ResourceKey.create(Registries.RECIPE,
                            Identifier.fromNamespaceAndPath(TitamMods.MODID,
                                    "decoration/" + BuiltInRegistries.BLOCK.getKey(stairs).getPath())));
        if (wall != null)
            ShapedRecipeBuilder.shaped(items, RecipeCategory.MISC, wall, 6)
                    .pattern("BBB").pattern("BBB").define('B', baseBlock)
                    .unlockedBy(base, has(baseBlock))
                    .save(this.output, ResourceKey.create(Registries.RECIPE,
                            Identifier.fromNamespaceAndPath(TitamMods.MODID,
                                    "decoration/" + BuiltInRegistries.BLOCK.getKey(wall).getPath())));
    }

}
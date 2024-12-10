package net.yochu.christmas.registry;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroupEntries;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.AxeItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.item.SwordItem;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.yochu.christmas.ChristmasWeapons;
import net.yochu.christmas.item.FrostBowItem;
import net.yochu.christmas.item.IceboltItem;
import net.yochu.christmas.item.RockItem;

public class ModItems {
    public static final Item CANDY_CANE_SWORD = registerItem("candy_cane_sword",
            new SwordItem(ModToolMaterial.CHRISTMAS, 4, -2.2f, new FabricItemSettings()));
    public static final Item FROSTBITE_AXE = registerItem("frostbite_axe",
            new AxeItem(ModToolMaterial.CHRISTMAS, 5, -3f, new FabricItemSettings()));
    public static final Item ROCK = registerItem("rock_debug",
            new RockItem(new FabricItemSettings()));
    public static final Item ICEBOLT = registerItem("icebolt_debug",
            new IceboltItem(new FabricItemSettings()));
    public static final Item FROSTBOW = registerItem("frostbow",
            new FrostBowItem(new FabricItemSettings().maxDamage(1024)));

    private static void addItemsToIngredientItemGroup(FabricItemGroupEntries entries) {
        entries.add(CANDY_CANE_SWORD);
        entries.add(FROSTBITE_AXE);
        entries.add(ROCK);
        entries.add(ICEBOLT);
    }

    private static Item registerItem(String name, Item item) {
        return Registry.register(Registries.ITEM, new Identifier(ChristmasWeapons.MOD_ID, name), item);
    }

    public static void registerModItems() {
        ChristmasWeapons.LOGGER.info("Registering Mod Items for " + ChristmasWeapons.MOD_ID);

        ItemGroupEvents.modifyEntriesEvent(ItemGroups.INGREDIENTS).register(ModItems::addItemsToIngredientItemGroup);
    }
}

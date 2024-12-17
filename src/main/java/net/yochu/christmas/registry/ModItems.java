package net.yochu.christmas.registry;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroupEntries;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.item.SwordItem;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.yochu.christmas.ChristmasWeapons;
import net.yochu.christmas.item.custom.*;

public class ModItems {
    public static final Item CANDY_CANE_SWORD = registerItem("candy_cane_sword",
            new SwordItem(ModToolMaterial.CHRISTMAS, 3, -2.2f, new FabricItemSettings().food(ModFoodComponents.CANDY_CANE_SWORD)));
    public static final Item PINE_GRENADE = registerItem("pine_grenade",
            new PineGrenadeItem(new FabricItemSettings()));
    public static final Item ROCK = registerItem("rock_debug",
            new RockItem(new FabricItemSettings()));
    public static final Item ICEBOLT = registerItem("icebolt_debug",
            new IceboltItem(new FabricItemSettings()));
    public static final Item ICICLE_TRIDENT = registerItem("icicle_trident",
            new IcicleTridentItem(new FabricItemSettings().maxCount(1).maxDamage(1024)));
    public static final Item GINGERBREAD_BOOMERANG = registerItem("gingerbread_boomerang",
            new GingerbreadBoomerangItem(new FabricItemSettings().maxCount(1)));
    public static final Item TOY_HAMMER = registerItem("toy_hammer",
            new ToyHammerItem(ModToolMaterial.CHRISTMAS, 0, -3.0f, new FabricItemSettings()));

    private static void addItemsToIngredientItemGroup(FabricItemGroupEntries entries) {
        entries.add(CANDY_CANE_SWORD);
        entries.add(PINE_GRENADE);
        entries.add(ICICLE_TRIDENT);
        entries.add(GINGERBREAD_BOOMERANG);
        entries.add(TOY_HAMMER);
    }

    private static Item registerItem(String name, Item item) {
        return Registry.register(Registries.ITEM, new Identifier(ChristmasWeapons.MOD_ID, name), item);
    }

    public static void registerModItems() {
        ChristmasWeapons.LOGGER.info("Registering Mod Items for " + ChristmasWeapons.MOD_ID);

        ItemGroupEvents.modifyEntriesEvent(ItemGroups.INGREDIENTS).register(ModItems::addItemsToIngredientItemGroup);
    }
}

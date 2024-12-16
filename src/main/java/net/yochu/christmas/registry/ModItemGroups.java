package net.yochu.christmas.registry;

import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.item.ItemGroup;

import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.yochu.christmas.ChristmasWeapons;

public class ModItemGroups {
    public static final ItemGroup WEAPON_GROUP = Registry.register(Registries.ITEM_GROUP,
            new Identifier(ChristmasWeapons.MOD_ID, "weapons"),
            FabricItemGroup.builder().displayName(Text.translatable("itemgroup.weapons"))
                    .icon(() -> new ItemStack(ModItems.CANDY_CANE_SWORD)).entries((displayContext, entries) -> {
                        entries.add(ModItems.CANDY_CANE_SWORD);
                        entries.add(ModItems.PINE_GRENADE);
                        entries.add(ModItems.ICICLE_TRIDENT);
                        entries.add(ModItems.GINGERBREAD_BOOMERANG);

                    }).build());

    public static void registerItemGroups() {
        ChristmasWeapons.LOGGER.info("Registering Item Groups for " + ChristmasWeapons.MOD_ID);
    }
}

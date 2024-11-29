package net.yochu.christmas;

import net.fabricmc.api.ModInitializer;

import net.yochu.christmas.registry.ModItemGroups;
import net.yochu.christmas.registry.ModItems;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ChristmasWeapons implements ModInitializer {
	public static final String MOD_ID = "christmas-weapons";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		ModItemGroups.registerItemGroups();
		ModItems.registerModItems();
	}
}
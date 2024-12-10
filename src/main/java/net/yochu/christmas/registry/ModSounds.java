package net.yochu.christmas.registry;

import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import net.yochu.christmas.ChristmasWeapons;

public class ModSounds {
    public static final SoundEvent SHOCKWAVE = registerSoundEvent("shockwave");
    public static final SoundEvent SCREAM = registerSoundEvent("scream");

    private static SoundEvent registerSoundEvent(String name) {
        Identifier id = new Identifier(ChristmasWeapons.MOD_ID, name);
        return Registry.register(Registries.SOUND_EVENT, id, SoundEvent.of(id));
    }

    public static void registerSounds() {
        ChristmasWeapons.LOGGER.info("Registering Sounds for "+ChristmasWeapons.MOD_ID);
    }
}

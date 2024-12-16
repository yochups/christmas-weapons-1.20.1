package net.yochu.christmas.registry;

import net.fabricmc.fabric.api.event.player.AttackEntityCallback;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.stat.Stat;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Identifier;
import net.yochu.christmas.ChristmasWeapons;
import net.yochu.christmas.effect.FrozenEffect;

public class ModEffects {
    public static final StatusEffect FROZEN_EFFECT = new FrozenEffect();

    public static void registerEffects() {
        ChristmasWeapons.LOGGER.info("Registering Status Effects for " + ChristmasWeapons.MOD_ID);

        Registry.register(Registries.STATUS_EFFECT, new Identifier(ChristmasWeapons.MOD_ID, "frozen"), FROZEN_EFFECT);
        registerCallbacks();
    }

    public static void registerCallbacks() {
        AttackEntityCallback.EVENT.register((player, world, hand, entity, hitResult) -> {
            if (entity instanceof LivingEntity livingEntity) {
                if (livingEntity.hasStatusEffect(FROZEN_EFFECT)) {
                    livingEntity.removeStatusEffect(FROZEN_EFFECT);
                    return ActionResult.SUCCESS; // Indicate the event was handled
                }
            }
            return ActionResult.PASS; // Let other listeners process the event
        });
    }
}

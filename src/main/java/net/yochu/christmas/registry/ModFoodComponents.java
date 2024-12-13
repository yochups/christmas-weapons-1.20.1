package net.yochu.christmas.registry;

import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.FoodComponent;

public class ModFoodComponents {
    public static final FoodComponent CANDY_CANE_SWORD = new FoodComponent.Builder()
            .hunger(5)
            .saturationModifier(0.5f)
            .statusEffect(new StatusEffectInstance(StatusEffects.REGENERATION, 200), 1.0f)
            .alwaysEdible()
            .build();
}

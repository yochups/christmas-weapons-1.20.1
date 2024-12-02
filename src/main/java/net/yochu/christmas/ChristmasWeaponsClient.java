package net.yochu.christmas;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.render.entity.FlyingItemEntityRenderer;
import net.yochu.christmas.registry.ModEntities;

public class ChristmasWeaponsClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        EntityRendererRegistry.register(ModEntities.ROCK_PROJECTILE, FlyingItemEntityRenderer::new);
    }
}

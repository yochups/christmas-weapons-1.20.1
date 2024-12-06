package net.yochu.christmas;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.yochu.christmas.particle.custom.FrostbiteSlashParticle;
import net.yochu.christmas.particle.custom.ShockwaveParticle;
import net.yochu.christmas.registry.ModEntities;
import net.yochu.christmas.registry.ModParticles;
import net.yochu.christmas.renderers.VelocityItemEntityRenderer;

public class ChristmasWeaponsClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        EntityRendererRegistry.register(ModEntities.ROCK_PROJECTILE, VelocityItemEntityRenderer::new);

        ParticleFactoryRegistry.getInstance().register(ModParticles.FROSTBITE_SWEEP_ATTACK, FrostbiteSlashParticle.Factory::new);
        ParticleFactoryRegistry.getInstance().register(ModParticles.SHOCKWAVE, ShockwaveParticle.Factory::new);
    }
}

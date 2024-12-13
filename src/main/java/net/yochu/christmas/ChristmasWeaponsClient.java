package net.yochu.christmas;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.yochu.christmas.particle.custom.CandyCaneSlashParticle;
import net.yochu.christmas.particle.custom.FrostbiteSlashParticle;
import net.yochu.christmas.particle.custom.ShockwaveParticle;
import net.yochu.christmas.particle.custom.ToyHammerSlamParticle;
import net.yochu.christmas.registry.ModEntities;
import net.yochu.christmas.registry.ModParticles;
import net.yochu.christmas.renderers.*;
import net.yochu.christmas.util.ModModelPredicateProvider;

public class ChristmasWeaponsClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        EntityRendererRegistry.register(ModEntities.ROCK_PROJECTILE, RockEntityRenderer::new);
        EntityRendererRegistry.register(ModEntities.ICEBOLT_PROJECTILE, IceboltEntityRenderer::new);
        EntityRendererRegistry.register(ModEntities.PINE_GRENADE_PROJECTILE, PineGrenadeEntityRenderer::new);
        EntityRendererRegistry.register(ModEntities.ICICLE_TRIDENT_PROJECTILE, IcicleTridentEntityRenderer::new);
        EntityRendererRegistry.register(ModEntities.GINGERBREAD_BOOMERANG_PROJECTILE, BoomerangEntityRenderer::new);

        ParticleFactoryRegistry.getInstance().register(ModParticles.FROSTBITE_SWEEP_ATTACK, FrostbiteSlashParticle.Factory::new);
        ParticleFactoryRegistry.getInstance().register(ModParticles.CANDY_CANE_SWEEP_ATTACK, CandyCaneSlashParticle.Factory::new);
        ParticleFactoryRegistry.getInstance().register(ModParticles.TOY_HAMMER_SLAM_ATTACK, ToyHammerSlamParticle.Factory::new);
        ParticleFactoryRegistry.getInstance().register(ModParticles.SHOCKWAVE, ShockwaveParticle.Factory::new);

        ModModelPredicateProvider.registerModModels();
    }
}

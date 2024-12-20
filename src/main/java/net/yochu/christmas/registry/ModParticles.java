package net.yochu.christmas.registry;

import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.yochu.christmas.ChristmasWeapons;

public class ModParticles {
    public static final DefaultParticleType FROSTBITE_SWEEP_ATTACK = FabricParticleTypes.simple();
    public static final DefaultParticleType SHOCKWAVE = FabricParticleTypes.simple();
    public static final DefaultParticleType CANDY_CANE_SWEEP_ATTACK = FabricParticleTypes.simple();
    public static final DefaultParticleType TOY_HAMMER_SLAM_ATTACK = FabricParticleTypes.simple();
    public static final DefaultParticleType FAIRYLIGHT_WHIP_SWEEP_ATTACK = FabricParticleTypes.simple();

    public static void registerParticles() {
        Registry.register(Registries.PARTICLE_TYPE, new Identifier(ChristmasWeapons.MOD_ID, "frostbite_sweep_attack"),
                FROSTBITE_SWEEP_ATTACK);
        Registry.register(Registries.PARTICLE_TYPE, new Identifier(ChristmasWeapons.MOD_ID, "candy_cane_sweep_attack"),
                CANDY_CANE_SWEEP_ATTACK);
        Registry.register(Registries.PARTICLE_TYPE, new Identifier(ChristmasWeapons.MOD_ID, "toy_hammer_slam_attack"),
                TOY_HAMMER_SLAM_ATTACK);
        Registry.register(Registries.PARTICLE_TYPE, new Identifier(ChristmasWeapons.MOD_ID, "shockwave"),
                SHOCKWAVE);
        Registry.register(Registries.PARTICLE_TYPE, new Identifier(ChristmasWeapons.MOD_ID, "fairylight_sweep_attack"),
                FAIRYLIGHT_WHIP_SWEEP_ATTACK);
    }
}


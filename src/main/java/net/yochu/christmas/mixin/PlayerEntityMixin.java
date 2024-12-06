package net.yochu.christmas.mixin;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.world.World;
import net.yochu.christmas.registry.ModItems;
import net.yochu.christmas.registry.ModParticles;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends LivingEntity {
    protected PlayerEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }

    @ModifyArg(
            method = "spawnSweepAttackParticles",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/server/world/ServerWorld;spawnParticles(Lnet/minecraft/particle/ParticleEffect;DDDIDDDD)I"), index = 0
    )
    private <T extends ParticleEffect> T modifySweepParticle(T particle) {
        return getMainHandStack().isOf(ModItems.CANDY_CANE_SWORD) ? (T) ModParticles.FROSTBITE_SWEEP_ATTACK : particle;
    }
}

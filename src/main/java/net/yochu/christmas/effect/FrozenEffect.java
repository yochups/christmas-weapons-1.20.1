package net.yochu.christmas.effect;

import net.minecraft.block.Blocks;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.AttributeContainer;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.particle.BlockStateParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.world.World;

public class FrozenEffect extends StatusEffect {
    public FrozenEffect() {
        super(StatusEffectCategory.HARMFUL, 0xADD8E6); // Light blue color
    }

    @Override
    public void onApplied(LivingEntity entity, AttributeContainer attributes, int amplifier) {
        super.onApplied(entity, attributes, amplifier);

        if (!entity.getWorld().isClient) {
            World world = entity.getWorld();
            spawnIceParticles(world, entity);
        }
    }

    @Override
    public void onRemoved(LivingEntity entity, AttributeContainer attributes, int amplifier) {
        super.onRemoved(entity, attributes, amplifier);

        if (!entity.getWorld().isClient) {
            World world = entity.getWorld();
            spawnIceParticles(world, entity);
            world.playSound(null, entity.getBlockPos(), SoundEvents.BLOCK_GLASS_BREAK, SoundCategory.PLAYERS, 1.0F, 1.0F);
        }
    }

    private void spawnIceParticles(World world, LivingEntity entity) {
        for (int i = 0; i < 20; i++) {
            double offsetX = (world.random.nextDouble() - 0.5) * 2.0;
            double offsetY = world.random.nextDouble() * 2.0;
            double offsetZ = (world.random.nextDouble() - 0.5) * 2.0;

            world.addParticle(
                    new BlockStateParticleEffect(ParticleTypes.BLOCK, Blocks.ICE.getDefaultState()),
                    entity.getX() + offsetX,
                    entity.getY() + offsetY,
                    entity.getZ() + offsetZ,
                    offsetX * 0.5,  // Velocity X
                    offsetY * 0.5,  // Velocity Y
                    offsetZ * 0.5   // Velocity Z
            );
        }
    }

    @Override
    public void applyUpdateEffect(LivingEntity entity, int amplifier) {
        if (!entity.getWorld().isClient) {

            entity.setVelocity(0, entity.getVelocity().y, 0);

            entity.setYaw(entity.getYaw());
            entity.setPitch(entity.getPitch());

            entity.setFrozenTicks(20);
        }
    }

    @Override
    public boolean canApplyUpdateEffect(int duration, int amplifier) {
        return true; // Apply the effect every tick
    }
}

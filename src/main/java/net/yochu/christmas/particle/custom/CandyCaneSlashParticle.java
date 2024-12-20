package net.yochu.christmas.particle.custom;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.particle.*;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.DefaultParticleType;

import java.util.Random;

public class CandyCaneSlashParticle extends SpriteBillboardParticle {
    private final SpriteProvider spriteProvider;

    CandyCaneSlashParticle(ClientWorld world, double x, double y, double z, double d, SpriteProvider spriteProvider) {
        super(world, x, y, z, 0.0, 0.0, 0.0);
        this.spriteProvider = spriteProvider;
        this.maxAge = 4;
        this.scale = 1.0F - (float)d * 0.5F;
        this.setSpriteForAge(spriteProvider);
        chooseRandomColor();
    }

    @Override
    public int getBrightness(float tint) {
        return 15728880;
    }

    @Override
    public void tick() {
        this.prevPosX = this.x;
        this.prevPosY = this.y;
        this.prevPosZ = this.z;
        if (this.age++ >= this.maxAge) {
            this.markDead();
        } else {
            this.setSpriteForAge(this.spriteProvider);
        }
    }

    private void chooseRandomColor() {
        Random random = new Random();
        if (random.nextBoolean()) {
            this.red = 0.8F;
            this.green = 0.0F;
            this.blue = 0.0F;
        } else {
            this.red = 0.9F;
            this.green = 0.9F;
            this.blue = 0.9F;
        }
    }

    @Override
    public ParticleTextureSheet getType() {
        return ParticleTextureSheet.PARTICLE_SHEET_OPAQUE;
    }

    @Environment(EnvType.CLIENT)
    public static class Factory implements ParticleFactory<DefaultParticleType> {
        private final SpriteProvider spriteProvider;

        public Factory(SpriteProvider spriteProvider) {
            this.spriteProvider = spriteProvider;
        }

        public Particle createParticle(DefaultParticleType defaultParticleType, ClientWorld clientWorld, double d, double e, double f, double g, double h, double i) {
            return new CandyCaneSlashParticle(clientWorld, d, e, f, g, this.spriteProvider);
        }
    }
}

package net.yochu.christmas.entity.custom;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.thrown.ThrownItemEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.EntitySpawnS2CPacket;
import net.minecraft.network.packet.s2c.play.GameStateChangeS2CPacket;
import net.minecraft.particle.*;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.world.World;
import net.yochu.christmas.registry.ModEntities;
import net.yochu.christmas.registry.ModItems;
import net.yochu.christmas.registry.ModParticles;
import net.yochu.christmas.registry.ModSounds;

import java.util.Random;

public class RockProjectileEntity extends ThrownItemEntity {

    public RockProjectileEntity(EntityType<? extends ThrownItemEntity> entityType, World world) {
        super(entityType, world);
    }

    public RockProjectileEntity(LivingEntity livingEntity, World world) {
        super(ModEntities.ROCK_PROJECTILE, livingEntity, world);
    }

    @Override
    protected Item getDefaultItem() {
        return Blocks.OAK_LEAVES.asItem();
    }

    @Override
    public Packet<ClientPlayPacketListener> createSpawnPacket() {
        return new EntitySpawnS2CPacket(this);
    }

    @Override
    public void onSpawnPacket(EntitySpawnS2CPacket packet) {
        super.onSpawnPacket(packet);
    }

    @Environment(EnvType.CLIENT)
    private ParticleEffect getParticleParameters() {
        ItemStack itemStack = this.getItem();
        if (itemStack == null) {
            itemStack = getDefaultItem().getDefaultStack();
        }
        return (ParticleEffect)(itemStack.isEmpty() ? ParticleTypes.BLOCK : new ItemStackParticleEffect(ParticleTypes.ITEM, itemStack));
    }

    @Environment(EnvType.CLIENT)
    public void handleStatus(byte status) {
        if (status == 3) {
            ParticleEffect particleEffect = this.getParticleParameters();

            Random random = new Random();
            this.getWorld().addParticle(ModParticles.SHOCKWAVE, this.getX(), this.getY(), this.getZ(), 0D, 0D, 0D);
            for(int i = 0; i < 30; ++i) {
                int offset = 6; //this is divided by 10
                float xoffset = (float) (random.nextInt(-offset, offset));
                float yoffset = (float) (random.nextInt(-offset, offset));
                float zoffset = (float) (random.nextInt(-offset, offset));

                float yvel = (float) (random.nextInt(0, 2));
                this.getWorld().addParticle(particleEffect, this.getX() + (xoffset / 10), this.getY() + (yoffset / 10), this.getZ() + (zoffset / 10), ((xoffset / 5) / 10), (yvel / 10), ((zoffset / 5) / 10));
            }
        }
    }

    protected void onHit(LivingEntity target) {
    }

    @Override
    public void onEntityHit(EntityHitResult entityHitResult) {
        Entity entity = entityHitResult.getEntity();

        Entity entity2 = this.getOwner();
        DamageSource damageSource;
        if (entity2 == null) {
            damageSource = this.getDamageSources().fallingBlock(this);
        } else if (entity2 instanceof PlayerEntity) {
            damageSource = this.getDamageSources().playerAttack((PlayerEntity) entity2);
        } else {
            damageSource = this.getDamageSources().mobAttack((LivingEntity) this.getOwner());
            if (entity2 instanceof LivingEntity) {
                ((LivingEntity)entity2).onAttacking(entity);
            }
        }

        if (entity instanceof LivingEntity livingEntity) {
            this.onHit(livingEntity);
            entity.damage(damageSource, 2.5f);
            //entity.damage(getWorld().getDamageSources().fallingBlock(this.getOwner()),2.5f+item.getDefaultStack().getDamage());
        }

        this.getWorld().sendEntityStatus(this, (byte)3);
        Item item = this.getItem().getItem();
        if (item instanceof BlockItem blockItem) {
            Block block = blockItem.getBlock();
            BlockState blockState = block.getDefaultState();
            BlockSoundGroup soundGroup = blockState.getSoundGroup();
            SoundEvent breakSound = soundGroup.getBreakSound();

            this.playSound(breakSound, 1f, 0.8f);
        }
        this.playSound(ModSounds.SHOCKWAVE, 0.6f, 1f);

        this.discard();
        super.onEntityHit(entityHitResult);
    }

    @Override
    protected void onBlockHit(BlockHitResult blockHitResult) {
        if (!this.getWorld().isClient) {
            this.getWorld().sendEntityStatus(this, (byte)3);

            Item item = this.getItem().getItem();
            if (item instanceof BlockItem blockItem) {
                Block block = blockItem.getBlock();
                BlockState blockState = block.getDefaultState();
                BlockSoundGroup soundGroup = blockState.getSoundGroup();
                SoundEvent breakSound = soundGroup.getBreakSound();

                this.playSound(breakSound, 1f, 0.8f);
            }
            this.playSound(ModSounds.SHOCKWAVE, 0.6f, 1f);
        }

        this.discard();
        super.onBlockHit(blockHitResult);
    }
}

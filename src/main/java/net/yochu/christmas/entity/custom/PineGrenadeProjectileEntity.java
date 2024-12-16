package net.yochu.christmas.entity.custom;

import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.thrown.ThrownItemEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.EntitySpawnS2CPacket;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.yochu.christmas.registry.ModEntities;
import net.yochu.christmas.registry.ModItems;
import net.yochu.christmas.registry.ModSounds;

public class PineGrenadeProjectileEntity extends ThrownItemEntity {
    private float spinAngle = 0.0f;
    private static final float SPIN_SPEED = 10.0f;
    private int ticksExisted = 0;
    private static final int EXPLOSION_DELAY = 40; // 2 seconds (40 ticks)
    private static final float BOUNCE_DAMPEN = 0.7f;

    public PineGrenadeProjectileEntity(EntityType<? extends ThrownItemEntity> type, World world) {
        super(type, world);
    }

    @Override
    protected Item getDefaultItem() {
        return ModItems.PINE_GRENADE;
    }

    public PineGrenadeProjectileEntity(World world, PlayerEntity thrower) {
        super(ModEntities.PINE_GRENADE_PROJECTILE, thrower, world);
        this.setPosition(thrower.getX(), thrower.getY(), thrower.getZ());
    }

    @Override
    public void tick() {
        super.tick();

        Vec3d velocity = this.getVelocity();
        double speed = velocity.length();
        this.velocityDirty = true;

        spinAngle += (float) (SPIN_SPEED * speed);
        spinAngle %= 360;

        ticksExisted++;

        // If the grenade has been in the world for long enough, trigger explosion
        if (ticksExisted >= EXPLOSION_DELAY) {
            if (!this.isRemoved()) {
             triggerExplosion();
            }
            this.remove(RemovalReason.DISCARDED); // Remove the grenade entity after exploding
        }
    }

    public float getSpinAngle() {
        return spinAngle;
    }

    @Override
    public Packet<ClientPlayPacketListener> createSpawnPacket() {
        return new EntitySpawnS2CPacket(this);
    }

    @Override
    public void onSpawnPacket(EntitySpawnS2CPacket packet) {
        super.onSpawnPacket(packet);
    }

    @Override
    protected void onBlockHit(BlockHitResult blockHitResult) {
        if (!this.getWorld().isClient) {
            //this.getWorld().sendEntityStatus(this, (byte)3);
            Vec3d velocity = this.getVelocity();
            Vec3d normal = new Vec3d(blockHitResult.getSide().getUnitVector());
            Vec3d reflectedVelocity = velocity.subtract(normal.multiply(2 * velocity.dotProduct(normal)));

            this.setVelocity(reflectedVelocity.multiply(BOUNCE_DAMPEN));

            this.playSound(ModSounds.PINECONE_LAND, 0.4f, 1f);
            //this.playSound(ModSounds.SHOCKWAVE, 0.6f, 1f);
        }

        super.onBlockHit(blockHitResult);
    }

    @Override
    protected void onEntityHit(EntityHitResult entityHitResult) {
        Entity entity = entityHitResult.getEntity();

        if (entity instanceof LivingEntity) {
            triggerExplosion();
            this.remove(RemovalReason.DISCARDED);
        } else {
            Vec3d entityToProjectile = this.getPos().subtract(entity.getPos()).normalize();
            Vec3d velocity = this.getVelocity();
            double dotProduct = velocity.dotProduct(entityToProjectile);
            Vec3d reflection = velocity.subtract(entityToProjectile.multiply(2 * dotProduct));

            this.setVelocity(reflection.multiply(BOUNCE_DAMPEN));

            this.playSound(SoundEvents.ENTITY_PLAYER_ATTACK_CRIT, 0.5f ,1f);
            this.playSound(ModSounds.PINECONE_LAND, 0.4f, 1f);
        }

        super.onEntityHit(entityHitResult);
    }

    private void triggerExplosion() {
        // Create the explosion at the grenade's position
        BlockPos pos = new BlockPos((int) this.getX(), (int) this.getY(), (int) this.getZ());
        getWorld().createExplosion(this, pos.getX(), pos.getY(), pos.getZ(), 2.0F, World.ExplosionSourceType.NONE);

        // Apply effects (e.g., slowness) to nearby entities
        //for (Entity entity : getWorld().getEntitiesByClass(PlayerEntity.class, this.getBoundingBox().expand(5), e -> e instanceof PlayerEntity)) {
        //    if (entity instanceof PlayerEntity) {
        //        // Apply status effects like Slowness to players near the explosion
        //        ((PlayerEntity) entity).addStatusEffect(new StatusEffectInstance(StatusEffect.byRawId(2), 100, 1)); // Slowness effect
        //    }
        //}
    }

    @Override
    public ItemStack getStack() {
        return new ItemStack(this.getDefaultItem()); // Return the grenade item stack
    }
}

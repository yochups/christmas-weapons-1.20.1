package net.yochu.christmas.entity.custom;

import com.ibm.icu.text.MessagePattern;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.block.Block;
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
import net.minecraft.particle.BlockStateParticleEffect;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.yochu.christmas.registry.ModEntities;
import net.yochu.christmas.registry.ModItems;
import net.yochu.christmas.registry.ModParticles;
import net.yochu.christmas.registry.ModSounds;
import net.yochu.christmas.util.SoundUtils;
import org.apache.commons.compress.compressors.lz77support.LZ77Compressor;

import java.io.InputStream;
import java.util.Random;

public class GingerbreadBoomerangEntity extends ThrownItemEntity {
    private float spinAngle = 0.0f;
    private static final float SPIN_SPEED = 10.0f;
    private int ticksExisted = 0;
    private boolean returning = false;
    private static final int lifetime = 20;
    private static final double speed = 1.0d;

    private int durability = 48;
    private Hand ownerHand;

    private int soundCooldown = 0;

    public GingerbreadBoomerangEntity(EntityType<? extends ThrownItemEntity> entityType, World world) {
        super(entityType, world);
        this.setReturning(false);
    }

    public GingerbreadBoomerangEntity(World world, PlayerEntity thrower) {
        super(ModEntities.GINGERBREAD_BOOMERANG_PROJECTILE, thrower, world);
        this.setPosition(thrower.getX(), thrower.getY(), thrower.getZ());
    }

    public void setTicksExisted(int ticks) {
        this.ticksExisted = ticks;
    }

    public void spawnParticle(BlockHitResult blockHitResult) {
        BlockPos blockPos = blockHitResult.getBlockPos();
        BlockState blockState = getWorld().getBlockState(blockPos);
        ParticleEffect particleEffect = new BlockStateParticleEffect(ParticleTypes.BLOCK, blockState);

        Random random = new Random();
        this.getWorld().addParticle(ModParticles.SHOCKWAVE, this.getX(), this.getY(), this.getZ(), 0D, 0D, 0D);
        for(int i = 0; i < 30; ++i) {
            int offset = 6;
            float xoffset = (float) (random.nextInt(-offset, offset));
            float yoffset = (float) (random.nextInt(-offset, offset));
            float zoffset = (float) (random.nextInt(-offset, offset));

            float yvel = (float) (random.nextInt(0, 2));
            this.getWorld().addParticle(particleEffect, this.getX() + (xoffset / 10), this.getY() + (yoffset / 10), this.getZ() + (zoffset / 10), ((xoffset / 5) / 10), (yvel / 10), ((zoffset / 5) / 10));
        }
    }

    @Override
    public void tick() {
        super.tick();

        if (!this.getWorld().isClient && soundCooldown <= 0) {
            this.getWorld().playSound(null, this.getX(), this.getY(), this.getZ(),
                    SoundEvents.ENTITY_EGG_THROW, SoundCategory.PLAYERS, 0.3F, 1.1F);
            soundCooldown = 5;
        }

        if (soundCooldown > 0) {
            soundCooldown--;
        }

        Vec3d velocity = this.getVelocity();
        Vec3d normalizedVelocity = velocity.normalize();
        this.setVelocity(normalizedVelocity.multiply(speed));
        this.velocityDirty = true;

        spinAngle += (SPIN_SPEED * 2);
        spinAngle %= 360;

        ticksExisted++;

        if (ticksExisted >= lifetime) {
            this.setReturning(true);
        }

        if (this.getReturning()) {
            returnToOwner();
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
    public boolean hasNoGravity() {
        return true; // Disable gravity for this entity
    }

    @Override
    public void setNoGravity(boolean noGravity) {
        // This method is ignored because hasNoGravity() is always true
    }

    public void setOwnerHand(Hand hand) {
        this.ownerHand = hand;
    }

    public Hand getOwnerHand() {
        return this.ownerHand;
    }


    @Override
    protected void onEntityHit(EntityHitResult entityHitResult) {
        if (!(entityHitResult.getEntity() == this.getOwner())) {
            if (entityHitResult.getEntity() instanceof LivingEntity target) {
                target.damage(getWorld().getDamageSources().thrown(this, this.getOwner()), 4.0F);
                this.playSound(SoundEvents.ENTITY_PLAYER_ATTACK_CRIT, 0.5f ,1f);
            }

            //this.playSound(breakSound, 0.7f, 1f);
            this.setReturning(true);
        }
    }

    @Override
    protected void onBlockHit(BlockHitResult blockHitResult) {
        if (!this.noClip) {
            spawnParticle(blockHitResult);

            if (!this.getWorld().isClient) {
                Vec3d velocity = this.getVelocity();
                Vec3d normal = new Vec3d(blockHitResult.getSide().getUnitVector());
                Vec3d reflectedVelocity = velocity.subtract(normal.multiply(2 * velocity.dotProduct(normal)));

                this.setVelocity(reflectedVelocity);
            }
            BlockPos blockPos = blockHitResult.getBlockPos();
            BlockState blockState = getWorld().getBlockState(blockPos);
            BlockSoundGroup soundGroup = blockState.getSoundGroup();
            SoundEvent breakSound = soundGroup.getBreakSound();

            this.playSound(breakSound, 0.5f, 1f);
            super.onBlockHit(blockHitResult);
        }
    }

    public void returnToOwner() {
        if (this.getOwner() instanceof PlayerEntity owner) {
            this.noClip = true;
            Vec3d toOwner = owner.getPos().subtract(this.getPos()).normalize();
            this.setVelocity(toOwner.multiply(speed));
            if (this.distanceTo(owner) < 1.5) {
                if (!owner.getAbilities().creativeMode) {
                    Item item = ModItems.GINGERBREAD_BOOMERANG;
                    ItemStack itemStack = new ItemStack(item);
                    itemStack.setDamage(this.durability);
                    itemStack.damage(1, (LivingEntity) getOwner(), (Entity) -> Entity.sendToolBreakStatus(getOwnerHand()));

                    owner.getInventory().insertStack(itemStack);
                    owner.getItemCooldownManager().set(item, 40);
                }

                this.remove(RemovalReason.DISCARDED);
            }
        }
    }

    @Override
    protected Item getDefaultItem() {
        return ModItems.GINGERBREAD_BOOMERANG;
    }

    public boolean getReturning() {
        return returning;
    }

    public void setReturning(boolean returning) {
        this.returning = returning;
    }

    public int getDurability() {
        return durability;
    }

    public void setDurability(int durability) {
        this.durability = durability;
    }
}

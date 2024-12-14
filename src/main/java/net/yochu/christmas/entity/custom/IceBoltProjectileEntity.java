package net.yochu.christmas.entity.custom;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.thrown.ThrownItemEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.EntitySpawnS2CPacket;
import net.minecraft.particle.ItemStackParticleEffect;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.yochu.christmas.registry.ModEntities;
import net.yochu.christmas.registry.ModItems;

import java.util.Random;

public class IceBoltProjectileEntity extends ThrownItemEntity {
    //private static final TrackedData<Byte> PROJECTILE_FLAGS = DataTracker.registerData(IceBoltProjectileEntity.class, TrackedDataHandlerRegistry.BYTE);
    private static final int CRITICAL_FLAG = 1;
    private double damage = 2.0f;
    private int punch;

    public IceBoltProjectileEntity(EntityType<? extends ThrownItemEntity> entityType, World world) {
        super(entityType, world);
    }

    public IceBoltProjectileEntity(LivingEntity livingEntity, World world) {
        super(ModEntities.ICEBOLT_PROJECTILE, livingEntity, world);
    }

    @Environment(EnvType.CLIENT)
    private ParticleEffect getParticleParameters() {
        ItemStack itemStack = getDefaultItem().getDefaultStack();
        return (ParticleEffect)(itemStack.isEmpty() ? ParticleTypes.BLOCK : new ItemStackParticleEffect(ParticleTypes.ITEM, itemStack));
    }

    @Override
    protected Item getDefaultItem() {
        return ModItems.ICEBOLT;
    }

    @Environment(EnvType.CLIENT)
    public void handleStatus(byte status) {
        if (status == 3) {
            ParticleEffect particleEffect = this.getParticleParameters();

            Random random = new Random();
            for(int i = 0; i < 30; ++i) {
                int offset = 3; //this is divided by 10
                float xoffset = (float) (random.nextInt(-offset, offset));
                float yoffset = (float) (random.nextInt(-offset, offset));
                float zoffset = (float) (random.nextInt(-offset, offset));

                float yvel = (float) (random.nextInt(0, 2));
                this.getWorld().addParticle(particleEffect, this.getX() + (xoffset / 10), this.getY() + (yoffset / 10), this.getZ() + (zoffset / 10), ((xoffset / 2) / 10), (yvel / 10), ((zoffset / 2) / 10));
            }
        }
    }

    @Override
    public void tick() {
        super.tick();

        if (this.getWorld().isClient) {
            ParticleEffect particleEffect = this.getParticleParameters();

            Random random = new Random();
            for(int i = 0; i < 1; ++i) {
                //int offset = 3; //this is divided by 10
                //float xvel = (float) (random.nextInt(-offset, offset));
                //float zvel = (float) (random.nextInt(-offset, offset));
                float xvel = (float) this.getVelocity().x;
                float yvel = (float) (random.nextInt(0, 2));
                float zvel = (float) this.getVelocity().z;

                this.getWorld().addParticle(particleEffect, this.getX(), this.getY(), this.getZ(), (xvel / 10), (yvel / 10), (zvel / 10));
            }
        }
    }

    protected void onHit(LivingEntity target) {
    }

    @Override
    public void onEntityHit(EntityHitResult entityHitResult) {
        Entity entity = entityHitResult.getEntity();

        float f = (float)this.getVelocity().length();
        int i = MathHelper.ceil(MathHelper.clamp((double)f * this.damage, 0.0, 2.147483647E9));
        //if (this.isCritical()) {
        //    long l = (long)this.random.nextInt(i / 2 + 2);
        //    i = (int)Math.min(l + (long)i, 2147483647L);
        //}

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
            if (this.punch > 0) {
                double d = Math.max(0.0, 1.0 - livingEntity.getAttributeValue(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE));
                Vec3d vec3d = this.getVelocity().multiply(1.0, 0.0, 1.0).normalize().multiply((double)this.punch * 0.6 * d);
                if (vec3d.lengthSquared() > 0.0) {
                    livingEntity.addVelocity(vec3d.x, 0.1, vec3d.z);
                }
            }

            if (!this.getWorld().isClient && entity2 instanceof LivingEntity) {
                EnchantmentHelper.onUserDamaged(livingEntity, entity2);
                EnchantmentHelper.onTargetDamaged((LivingEntity)entity2, livingEntity);
            }

            this.onHit(livingEntity);
            if (this.getDamage() <= 0) {
                this.setDamage(2.0F);
            };
            entity.damage(damageSource, this.getDamage());

            this.getWorld().sendEntityStatus(this, (byte)3);
            Item item = this.getItem().getItem();

            this.playSound(SoundEvents.BLOCK_GLASS_BREAK, 1f, 1f);
            //this.playSound(ModSounds.SHOCKWAVE, 0.6f, 1f);

            //entity.damage(getWorld().getDamageSources().fallingBlock(this.getOwner()),2.5f+item.getDefaultStack().getDamage());
        }

        this.remove(RemovalReason.DISCARDED);
        super.onEntityHit(entityHitResult);
    }

    @Override
    protected void onBlockHit(BlockHitResult blockHitResult) {
        if (!this.getWorld().isClient) {
            this.getWorld().sendEntityStatus(this, (byte)3);

            this.playSound(SoundEvents.BLOCK_GLASS_BREAK, 1f, 1f);
            //this.playSound(ModSounds.SHOCKWAVE, 0.6f, 1f);
        }

        this.remove(RemovalReason.DISCARDED);
        super.onBlockHit(blockHitResult);
    }

    //public void setCritical(boolean critical) {
    //    this.setProjectileFlag(CRITICAL_FLAG, critical);
    //}

    //private void setProjectileFlag(int index, boolean flag) {
    //    byte b = this.dataTracker.get(PROJECTILE_FLAGS);
    //    if (flag) {
    //        this.dataTracker.set(PROJECTILE_FLAGS, (byte)(b | index));
    //    } else {
    //        this.dataTracker.set(PROJECTILE_FLAGS, (byte)(b & ~index));
    //    }
    //}

    public void setDamage(double damage) {
        this.damage = damage;
    }
    public float getDamage() {
        return (float) this.damage;
    }
    public void setPunch(int punch) {
        this.punch = punch;
    }
    public int getPunch() {
        return this.punch;
    }

    //public boolean isCritical() {
       // byte b = this.dataTracker.get(PROJECTILE_FLAGS);
      //  return (b & 1) != 0;
    //}

    public void applyEnchantmentEffects(LivingEntity entity, float damageModifier) {
        int i = EnchantmentHelper.getEquipmentLevel(Enchantments.POWER, entity);
        int j = EnchantmentHelper.getEquipmentLevel(Enchantments.PUNCH, entity);
        this.setDamage((double)(damageModifier * 2.0F) + this.random.nextTriangular((double)this.getWorld().getDifficulty().getId() * 0.11, 0.57425));
        if (i > 0) {
            this.setDamage(this.getDamage() + (double)i * 0.5 + 0.5);
        }

        if (j > 0) {
            this.setPunch(j);
        }
    }

    @Override
    public Packet<ClientPlayPacketListener> createSpawnPacket() {
        return new EntitySpawnS2CPacket(this);
    }

    @Override
    public void onSpawnPacket(EntitySpawnS2CPacket packet) {
        super.onSpawnPacket(packet);
    }
}

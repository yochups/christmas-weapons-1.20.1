package net.yochu.christmas.mixin;

import com.mojang.datafixers.util.Pair;
import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.HungerManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stat;
import net.minecraft.stat.Stats;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;
import net.yochu.christmas.registry.ModItems;
import net.yochu.christmas.registry.ModParticles;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Objects;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends LivingEntity {
    @Shadow protected abstract void attackLivingEntity(LivingEntity target);
    @Shadow protected HungerManager hungerManager = new HungerManager();

    protected PlayerEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }

    @ModifyArg(
            method = "spawnSweepAttackParticles",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/server/world/ServerWorld;spawnParticles(Lnet/minecraft/particle/ParticleEffect;DDDIDDDD)I"), index = 0
    )
    private <T extends ParticleEffect> T modifySweepParticle(T particle) {
        ItemStack stack = getMainHandStack();
        if (stack.isOf(ModItems.CANDY_CANE_SWORD)) {
            return (T) ModParticles.CANDY_CANE_SWEEP_ATTACK;
        } else if (stack.isOf(ModItems.TOY_HAMMER)) {
            return (T) ModParticles.TOY_HAMMER_SLAM_ATTACK;
        } else if (stack.isOf(ModItems.FAIRY_LIGHT_WHIP)) {
            return (T) ModParticles.FAIRYLIGHT_WHIP_SWEEP_ATTACK;
        }
        return particle;
    }
}

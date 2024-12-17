package net.yochu.christmas.item.custom;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.yochu.christmas.registry.ModParticles;
import net.yochu.christmas.registry.ModSounds;

public class ToyHammerItem extends SwordItem {

    public ToyHammerItem(ToolMaterial toolMaterial, int attackDamage, float attackSpeed, Settings settings) {
        super(toolMaterial, attackDamage, attackSpeed, settings);
    }

    @Override
    public boolean postHit(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        World world = attacker.getWorld();

        if (!world.isClient) {
            if (target.isBlocking() && target.getActiveItem().getItem() instanceof ShieldItem shieldItem) {
                if (target instanceof PlayerEntity player) {
                 player.getItemCooldownManager().set(shieldItem, 100);
                }

                world.playSound(null, target.getBlockPos(), SoundEvents.ITEM_SHIELD_BREAK, SoundCategory.PLAYERS, 1.0F, 1.0F);
            }

            if (isCriticalHit(attacker)) {
                knockUp(target, attacker);
            } else {
                knockBack(target, attacker);
            }
            stack.damage(1, attacker, (p) -> p.sendToolBreakStatus(attacker.getActiveHand()));
        }

        return super.postHit(stack, target, attacker);
    }

    private boolean isCriticalHit(LivingEntity attacker) {
        return !attacker.isOnGround() && !attacker.isClimbing() && !attacker.isTouchingWater() && attacker.fallDistance > 0.0F;
    }

    private void knockBack(LivingEntity target, LivingEntity attacker) {
        Vec3d knockbackDirection = target.getPos().subtract(attacker.getPos()).normalize();

        double knockbackStrength = 1.6;
        target.setVelocity(knockbackDirection.x * knockbackStrength, 0.4, knockbackDirection.z * knockbackStrength);
        attacker.getWorld().playSound(null, target.getBlockPos(), ModSounds.SQUEAKY_HIT,
                SoundCategory.PLAYERS, 1.0F, 1.0F);
    }

    private void knockUp(LivingEntity target, LivingEntity attacker) {
        Vec3d knockbackDirection = target.getPos().subtract(attacker.getPos()).normalize();

        double horizontalStrength = 0.5;
        double verticalStrength = 0.8;
        target.setVelocity(knockbackDirection.x * horizontalStrength, verticalStrength, knockbackDirection.z * horizontalStrength);
        attacker.getWorld().playSound(null, attacker.getBlockPos(), ModSounds.SQUEAKY_HIT,
                SoundCategory.PLAYERS, 1.0F, 1.2F);
    }
}
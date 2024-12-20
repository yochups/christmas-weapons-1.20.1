package net.yochu.christmas.item.custom;

import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.item.ToolMaterial;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.math.Vec3d;
import net.yochu.christmas.registry.ModSounds;

public class FairylightWhipItem extends SwordItem {
    private final double extendedReach;

    public FairylightWhipItem(ToolMaterial material, int attackDamage, float attackSpeed, double extendedReach, Item.Settings settings) {
        super(material, attackDamage, attackSpeed, settings);
        this.extendedReach = extendedReach;
    }

    public double getExtendedReach() {
        return extendedReach;
    }

    @Override
    public boolean postHit(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        // Check if the attack is a critical hit
        boolean isCritical = attacker.fallDistance > 0.0F && !attacker.isOnGround() && !attacker.isClimbing() && !attacker.isSwimming() && !attacker.hasVehicle();

        if (isCritical) {
            // Calculate the vector from the target to the attacker
            Vec3d attackerPos = new Vec3d(attacker.getX(), target.getY(), attacker.getZ());
            Vec3d targetPos = target.getPos();
            Vec3d pullVector = attackerPos.subtract(targetPos).normalize().multiply(1); // Adjust the multiplier for strength

            // Apply the pull effect to the target
            target.addVelocity(pullVector.x, pullVector.y, pullVector.z);
            target.velocityModified = true; // Ensure the velocity update is processed
        }
        attacker.getWorld().playSound(null, attacker.getBlockPos(), ModSounds.WHIP_CRACK,
                SoundCategory.PLAYERS, 1.0F, 1.0F);

        return super.postHit(stack, target, attacker);
    }
}

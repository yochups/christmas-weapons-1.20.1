package net.yochu.christmas.mixin;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.yochu.christmas.item.custom.FairylightWhipItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntitySweepMixin {

    @Inject(method = "attack", at = @At("HEAD"))
    private void modifySweepingAttack(Entity target, CallbackInfo ci) {
        PlayerEntity player = (PlayerEntity) (Object) this; // Cast to PlayerEntity
        ItemStack stack = player.getMainHandStack(); // Access the player's main hand item stack

        if (stack == null || stack.isEmpty()) {
            return; // Exit if no item is being held
        }

        if (stack.getItem() instanceof FairylightWhipItem) {
            double sweepRadius = 3.5; // Adjust the radius as needed
            double sweepDamage = 2.0; // Adjust the damage as needed

            for (Entity entity : player.getWorld().getOtherEntities(player, player.getBoundingBox().expand(sweepRadius))) {
                if (entity instanceof LivingEntity livingEntity && entity != target && player.canSee(entity)) {
                    livingEntity.damage(player.getWorld().getDamageSources().playerAttack(player), (float) sweepDamage);
                }
            }
        }
    }
}

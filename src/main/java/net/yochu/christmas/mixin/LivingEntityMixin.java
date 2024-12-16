package net.yochu.christmas.mixin;

import com.mojang.datafixers.util.Pair;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.item.FoodComponent;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.yochu.christmas.registry.ModEffects;
import net.yochu.christmas.registry.ModItems;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {


    @Inject(method = "eatFood", at = @At("HEAD"), cancellable = true)
    private void onEatFood(World world, ItemStack stack, CallbackInfoReturnable<ItemStack> cir) {
        if (stack.isOf(ModItems.CANDY_CANE_SWORD)) {
            stack.damage(100, (LivingEntity) (Object) this, p -> p.sendToolBreakStatus(Hand.MAIN_HAND));

            FoodComponent foodComponent = stack.getItem().getFoodComponent();
            if (foodComponent != null) {
                for (Pair<StatusEffectInstance, Float> effect : foodComponent.getStatusEffects()) {
                    StatusEffectInstance statusEffect = effect.getFirst();
                    // Apply the effect to the entity, force reapplication each time the sword is "eaten"
                    ((LivingEntity) (Object) this).addStatusEffect(new StatusEffectInstance(statusEffect));
                }
            }

            cir.setReturnValue(stack);
            cir.cancel();
        }
    }
}

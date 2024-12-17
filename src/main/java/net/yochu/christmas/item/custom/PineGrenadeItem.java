package net.yochu.christmas.item.custom;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import net.yochu.christmas.entity.custom.PineGrenadeProjectileEntity;

public class PineGrenadeItem extends Item {
    public PineGrenadeItem(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack itemStack = user.getStackInHand(hand);

        world.playSound(null, user.getX(), user.getY(), user.getZ(), SoundEvents.ENTITY_SNOWBALL_THROW, SoundCategory.PLAYERS, 0.5f, 0.4f);

        if (!world.isClient) {
            PineGrenadeProjectileEntity pineGrenadeProjectileEntity = new PineGrenadeProjectileEntity(world, user);
            pineGrenadeProjectileEntity.setPosition(pineGrenadeProjectileEntity.getX(), pineGrenadeProjectileEntity.getY()+1.6D,pineGrenadeProjectileEntity.getZ());
            pineGrenadeProjectileEntity.setVelocity(user, user.getPitch(), user.getYaw(), 0.0F, 1.0F, 1.0F);
            world.spawnEntity(pineGrenadeProjectileEntity); // Spawn the thrown grenade entity
        }

        user.incrementStat(Stats.USED.getOrCreateStat(this));

        if (!user.getAbilities().creativeMode) {
            itemStack.decrement(1); // Decrease the grenade item count
        }

        return new TypedActionResult<>(ActionResult.SUCCESS, itemStack);
    }
}

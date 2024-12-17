package net.yochu.christmas.item.custom;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import net.yochu.christmas.entity.custom.IceBoltProjectileEntity;

public class IceboltItem extends Item {
    public IceboltItem(Settings settings) {
        super(settings);
    }

    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        world.playSound(null, user.getX(), user.getY(), user.getZ(), SoundEvents.ENTITY_ARROW_SHOOT, SoundCategory.PLAYERS, 0.5f, 0.4f); // plays a globalSoundEvent
        if (!world.isClient) {
            IceBoltProjectileEntity iceBoltProjectileEntity = new IceBoltProjectileEntity(user, world);
            iceBoltProjectileEntity.setPosition(iceBoltProjectileEntity.getX(), iceBoltProjectileEntity.getY()-0.4D,iceBoltProjectileEntity.getZ());
            iceBoltProjectileEntity.setVelocity(user, user.getPitch(), user.getYaw(), 0.0F, 3.0F, 1.0F);
            world.spawnEntity(iceBoltProjectileEntity);
        }

        user.incrementStat(Stats.USED.getOrCreateStat(this));

        return TypedActionResult.success(new ItemStack(this.asItem()), world.isClient());
    }
}

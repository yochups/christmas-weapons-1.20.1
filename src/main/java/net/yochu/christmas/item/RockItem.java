package net.yochu.christmas.item;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.yochu.christmas.entity.custom.RockProjectileEntity;

public class RockItem extends Item {
    public RockItem(Settings settings) {
        super(settings);
    }

    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        BlockPos posBelow = BlockPos.ofFloored(user.getPos().subtract(0,1,0));
        BlockState state = user.getWorld().getBlockState(posBelow);
        Block block = state.getBlock();
        if (!state.isSolidBlock(world, posBelow)) {
            block = Blocks.STONE;
        };
        ItemStack blockItem = new ItemStack(block);

        ItemStack itemStack = user.getMainHandStack();

        world.playSound(null, user.getX(), user.getY(), user.getZ(), SoundEvents.ENTITY_EGG_THROW, SoundCategory.NEUTRAL, 0.5f, 0.4f); // plays a globalSoundEvent
        if (!world.isClient) {
            RockProjectileEntity rockProjectileEntity = new RockProjectileEntity(user, world);
            rockProjectileEntity.setPosition(rockProjectileEntity.getX(), rockProjectileEntity.getY()-0.4D,rockProjectileEntity.getZ());
            rockProjectileEntity.setItem(blockItem);
            rockProjectileEntity.setVelocity(user, user.getPitch(), user.getYaw(), 0.0F, 0.8F, 1.0F);
            world.spawnEntity(rockProjectileEntity);
        }

        user.incrementStat(Stats.USED.getOrCreateStat(this));
        if (!user.getAbilities().creativeMode) {
            itemStack.decrement(1);
        }

        return TypedActionResult.success(itemStack, world.isClient());
    }
}
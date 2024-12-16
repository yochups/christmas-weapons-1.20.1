package net.yochu.christmas.item;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import net.yochu.christmas.entity.custom.GingerbreadBoomerangEntity;

public class GingerbreadBoomerangItem extends Item {

    public GingerbreadBoomerangItem(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack stack = user.getStackInHand(hand);
        if (!world.isClient) {
            // Spawn the boomerang entity
            GingerbreadBoomerangEntity boomerangEntity = new GingerbreadBoomerangEntity(world, user);
            boomerangEntity.setItem(stack);
            boomerangEntity.setPosition(boomerangEntity.getX(), boomerangEntity.getY()+1.5D,boomerangEntity.getZ());
            boomerangEntity.setVelocity(user, user.getPitch(), user.getYaw(), 0.0F, 1.5F, 1.0F);
            world.spawnEntity(boomerangEntity);

            stack.damage(1, user, (p) -> p.sendToolBreakStatus(hand));
        }

        if (!user.getAbilities().creativeMode) {
            stack.decrement(1);
        }

        return TypedActionResult.success(user.getStackInHand(hand), world.isClient());
    }
}

package net.yochu.christmas.registry;

import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.yochu.christmas.ChristmasWeapons;
import net.yochu.christmas.entity.custom.IceBoltProjectileEntity;
import net.yochu.christmas.entity.custom.RockProjectileEntity;

public class ModEntities {
    public static final EntityType<RockProjectileEntity> ROCK_PROJECTILE = Registry.register(Registries.ENTITY_TYPE,
            new Identifier(ChristmasWeapons.MOD_ID, "rock_projectile"),
            FabricEntityTypeBuilder.<RockProjectileEntity>create(SpawnGroup.MISC, RockProjectileEntity::new)
                    .dimensions(EntityDimensions.fixed(1.0f, 1.0f))
                    .build());
    public static final EntityType<IceBoltProjectileEntity> ICEBOLT_PROJECTILE = Registry.register(Registries.ENTITY_TYPE,
            new Identifier(ChristmasWeapons.MOD_ID, "icebolt_projectile"),
            FabricEntityTypeBuilder.<IceBoltProjectileEntity>create(SpawnGroup.MISC, IceBoltProjectileEntity::new)
                    .dimensions(EntityDimensions.fixed(0.4f, 0.4f))
                    .build());

    public static void registerModEntities() {
        ChristmasWeapons.LOGGER.info("Registering Entities for " + ChristmasWeapons.MOD_ID);
    }
}

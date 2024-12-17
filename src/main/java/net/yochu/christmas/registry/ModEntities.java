package net.yochu.christmas.registry;

import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.yochu.christmas.ChristmasWeapons;
import net.yochu.christmas.entity.custom.*;

public class ModEntities {
    public static final EntityType<RockProjectileEntity> ROCK_PROJECTILE = Registry.register(Registries.ENTITY_TYPE,
            new Identifier(ChristmasWeapons.MOD_ID, "rock_projectile"),
            FabricEntityTypeBuilder.<RockProjectileEntity>create(SpawnGroup.MISC, RockProjectileEntity::new)
                    .dimensions(EntityDimensions.fixed(1.0f, 1.0f))
                    .trackRangeBlocks(64)
                    .trackedUpdateRate(1)
                    .build());
    public static final EntityType<IceBoltProjectileEntity> ICEBOLT_PROJECTILE = Registry.register(Registries.ENTITY_TYPE,
            new Identifier(ChristmasWeapons.MOD_ID, "icebolt_projectile"),
            FabricEntityTypeBuilder.<IceBoltProjectileEntity>create(SpawnGroup.MISC, IceBoltProjectileEntity::new)
                    .dimensions(EntityDimensions.fixed(0.54f, 0.5f))
                    .trackRangeBlocks(128)
                    .trackedUpdateRate(1)
                    .build());
    public static final EntityType<PineGrenadeProjectileEntity> PINE_GRENADE_PROJECTILE = Registry.register(Registries.ENTITY_TYPE,
            new Identifier(ChristmasWeapons.MOD_ID, "pine_grenade_projectile"),
            FabricEntityTypeBuilder.<PineGrenadeProjectileEntity>create(SpawnGroup.MISC, PineGrenadeProjectileEntity::new)
                    .dimensions(EntityDimensions.fixed(0.5f,0.5f))
                    .trackRangeBlocks(64)
                    .trackedUpdateRate(1)
                    .build());
    public static final EntityType<IcicleTridentEntity> ICICLE_TRIDENT_PROJECTILE = Registry.register(Registries.ENTITY_TYPE,
            new Identifier(ChristmasWeapons.MOD_ID, "icicle_trident_projectile"),
            FabricEntityTypeBuilder.<IcicleTridentEntity>create(SpawnGroup.MISC, IcicleTridentEntity::new)
                    .dimensions(EntityDimensions.fixed(0.5F, 0.5F))
                    .trackRangeBlocks(64)
                    .trackedUpdateRate(1)
                    .build());
    public static final EntityType<GingerbreadBoomerangEntity> GINGERBREAD_BOOMERANG_PROJECTILE = Registry.register(Registries.ENTITY_TYPE,
            new Identifier(ChristmasWeapons.MOD_ID, "gingerbread_boomerang_projectile"),
            FabricEntityTypeBuilder.<GingerbreadBoomerangEntity>create(SpawnGroup.MISC, GingerbreadBoomerangEntity::new)
                    .dimensions(EntityDimensions.fixed(0.5f, 0.5f))
                    .trackRangeBlocks(64)
                    .trackedUpdateRate(1)
                    .build());

    public static void registerModEntities() {
        ChristmasWeapons.LOGGER.info("Registering Entities for " + ChristmasWeapons.MOD_ID);
    }
}

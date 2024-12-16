package net.yochu.christmas.renderers;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.FlyingItemEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;
import net.yochu.christmas.entity.custom.GingerbreadBoomerangEntity;
import net.yochu.christmas.entity.custom.PineGrenadeProjectileEntity;

@Environment(EnvType.CLIENT)
public class BoomerangEntityRenderer<T extends Entity & FlyingItemEntity> extends EntityRenderer<T> {
    private static final float MIN_DISTANCE = 12.25F;
    private final ItemRenderer itemRenderer;
    private final float scale;
    private final boolean lit;

    public BoomerangEntityRenderer(EntityRendererFactory.Context ctx, float scale, boolean lit) {
        super(ctx);
        this.itemRenderer = ctx.getItemRenderer();
        this.scale = scale;
        this.lit = lit;
    }

    public BoomerangEntityRenderer(EntityRendererFactory.Context context) {
        this(context, 1.0F, false);
    }

    @Override
    protected int getBlockLight(T entity, BlockPos pos) {
        return this.lit ? 15 : super.getBlockLight(entity, pos);
    }

    @Override
    public void render(T entity, float yaw, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light) {
        if (entity.age >= 2 || !(this.dispatcher.camera.getFocusedEntity().squaredDistanceTo(entity) < 12.25)) {
            matrices.push();
            matrices.scale(this.scale*2f, this.scale*2f, this.scale*2f);
            matrices.translate(0,0.1,0);
            matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(entity.getYaw() - 90.0F));
            matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(entity.getPitch()));
            matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(90.0F));
            matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(90.0F));
            if (entity instanceof GingerbreadBoomerangEntity boomerangEntity) {
                float spinAngle = boomerangEntity.getSpinAngle();
                matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(spinAngle));
            }
            this.itemRenderer
                    .renderItem(
                            entity.getStack(), ModelTransformationMode.GROUND, light, OverlayTexture.DEFAULT_UV, matrices, vertexConsumers, entity.getWorld(), entity.getId()
                    );
            matrices.pop();
            super.render(entity, yaw, tickDelta, matrices, vertexConsumers, light);
        }
    }

    @Override
    public Identifier getTexture(Entity entity) {
        return SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE;
    }
}


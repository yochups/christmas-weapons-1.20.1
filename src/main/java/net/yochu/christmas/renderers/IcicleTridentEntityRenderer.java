package net.yochu.christmas.renderers;

import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.yochu.christmas.entity.custom.IcicleTridentEntity;

public class IcicleTridentEntityRenderer extends EntityRenderer<IcicleTridentEntity> {
    private final ItemRenderer itemRenderer;

    public IcicleTridentEntityRenderer(EntityRendererFactory.Context context) {
        super(context);
        this.itemRenderer = context.getItemRenderer();
        //EntityModelLoader loader = context.getModelLoader();
        //this.model = new IcicleTridentModel(loader.getModelPart(ChristmasWeapons.ICIC));
    }

    @Override
    public void render(IcicleTridentEntity entity, float yaw, float tickDelta, MatrixStack matrices,
                       VertexConsumerProvider vertexConsumers, int light) {
        matrices.push();

        matrices.multiply(net.minecraft.util.math.RotationAxis.POSITIVE_Y.rotationDegrees(yaw - 135));
        matrices.multiply(net.minecraft.util.math.RotationAxis.POSITIVE_Z.rotationDegrees(entity.getPitch()));

        // Get the ItemStack from the entity
        if (!entity.getTridentStack().isEmpty()) {
            this.itemRenderer
                    .renderItem(
                            entity.getTridentStack(), ModelTransformationMode.GROUND, light, OverlayTexture.DEFAULT_UV, matrices, vertexConsumers, entity.getWorld(), entity.getId()
                    );
        }

        matrices.pop();

        super.render(entity, yaw, tickDelta, matrices, vertexConsumers, light);
    }

    @Override
    public Identifier getTexture(IcicleTridentEntity icicleTridentEntity) {
        return null;
    }
}

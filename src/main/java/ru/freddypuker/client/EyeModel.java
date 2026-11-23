package ru.freddypuker.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.world.entity.LivingEntity;
import ru.freddypuker.FreddyPukerMod;

public class EyeModel<T extends LivingEntity> extends EntityModel<T> {
   public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(FreddyPukerMod.id("eyes"), "main");
   private final ModelPart bbMain;

   public EyeModel(ModelPart root) {
      this.bbMain = root.getChild("bb_main");
   }

   public static LayerDefinition createBodyLayer() {
      MeshDefinition mesh = new MeshDefinition();
      mesh.getRoot()
         .addOrReplaceChild(
            "bb_main",
            CubeListBuilder.create().texOffs(2, 2).addBox(-1.0F, -34.0F, 0.0F, 2.0F, 2.0F, 0.0F, new CubeDeformation(5.0F)),
            PartPose.offset(0.0F, 24.0F, 0.0F)
         );
      return LayerDefinition.create(mesh, 8, 8);
   }

   public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
   }

   public void renderToBuffer(PoseStack poseStack, VertexConsumer consumer, int light, int overlay, int color) {
      this.bbMain.render(poseStack, consumer, light, overlay, color);
   }
}

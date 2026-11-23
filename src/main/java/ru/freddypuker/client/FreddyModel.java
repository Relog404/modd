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
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.world.entity.LivingEntity;
import ru.freddypuker.FreddyPukerMod;

public class FreddyModel<T extends LivingEntity> extends EntityModel<T> {
   public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(FreddyPukerMod.id("freddy_puker"), "main");
   private final ModelPart bone;
   private final ModelPart bbMain;

   public FreddyModel(ModelPart root) {
      this.bone = root.getChild("bone");
      this.bbMain = root.getChild("bb_main");
   }

   public static LayerDefinition createBodyLayer() {
      MeshDefinition mesh = new MeshDefinition();
      PartDefinition root = mesh.getRoot();
      root.addOrReplaceChild("bone", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));
      root.addOrReplaceChild(
         "bb_main",
         CubeListBuilder.create()
            .texOffs(0, 0)
            .addBox(-8.0F, -50.0F, -8.0F, 16.0F, 50.0F, 1.0F, new CubeDeformation(0.0F))
            .texOffs(39, 0)
            .addBox(-3.0F, -28.0F, -7.9F, 10.0F, 7.0F, 9.0F, new CubeDeformation(0.0F))
            .texOffs(34, 16)
            .addBox(3.0F, -41.0F, -10.0F, 3.0F, 4.0F, 2.0F, new CubeDeformation(0.0F))
            .texOffs(34, 22)
            .addBox(-2.0F, -41.0F, -10.0F, 3.0F, 4.0F, 2.0F, new CubeDeformation(0.0F))
            .texOffs(34, 28)
            .addBox(0.0F, -25.0F, -10.0F, 3.0F, 4.0F, 2.0F, new CubeDeformation(0.0F)),
         PartPose.offset(0.0F, 24.0F, 0.0F)
      );
      return LayerDefinition.create(mesh, 128, 128);
   }

   public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
      this.bbMain.yRot = netHeadYaw * (float) (Math.PI / 180.0);
      this.bbMain.xRot = headPitch * (float) (Math.PI / 180.0);
   }

   public void renderToBuffer(PoseStack poseStack, VertexConsumer consumer, int light, int overlay, int color) {
      this.bone.render(poseStack, consumer, light, overlay, color);
      this.bbMain.render(poseStack, consumer, light, overlay, color);
   }
}

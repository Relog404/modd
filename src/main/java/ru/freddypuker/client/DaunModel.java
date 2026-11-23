package ru.freddypuker.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import ru.freddypuker.FreddyPukerMod;
import ru.freddypuker.entity.WormEntity;

public class DaunModel<T extends LivingEntity> extends EntityModel<T> {
   public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(FreddyPukerMod.id("daun"), "main");
   private final ModelPart rear;
   private final ModelPart middle;
   private final ModelPart head;
   private final ModelPart jaw;

   public DaunModel(ModelPart root) {
      this.rear = root.getChild("rear");
      this.middle = root.getChild("middle");
      this.head = root.getChild("head");
      this.jaw = this.head.getChild("jaw");
   }

   public static LayerDefinition createBodyLayer() {
      MeshDefinition mesh = new MeshDefinition();
      PartDefinition root = mesh.getRoot();
      root.addOrReplaceChild("rear", CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, -4.0F, -2.0F, 8.0F, 8.0F, 8.0F), PartPose.offset(0.0F, 20.0F, 5.0F));
      root.addOrReplaceChild(
         "middle", CubeListBuilder.create().texOffs(0, 16).addBox(-4.0F, -5.0F, -5.0F, 8.0F, 10.0F, 10.0F), PartPose.offset(0.0F, 19.0F, -1.0F)
      );
      PartDefinition head = root.addOrReplaceChild(
         "head",
         CubeListBuilder.create().texOffs(0, 26).addBox(-5.0F, -5.0F, -7.0F, 10.0F, 9.0F, 8.0F).texOffs(0, 43).addBox(-4.0F, -3.0F, -10.0F, 8.0F, 5.0F, 4.0F),
         PartPose.offset(0.0F, 18.0F, -7.0F)
      );
      PartDefinition jaw = head.addOrReplaceChild(
         "jaw", CubeListBuilder.create().texOffs(0, 50).addBox(-5.0F, 0.0F, -8.0F, 10.0F, 3.0F, 8.0F), PartPose.offset(0.0F, 2.0F, -2.0F)
      );
      jaw.addOrReplaceChild(
         "teeth",
         CubeListBuilder.create()
            .texOffs(0, 61)
            .addBox(-4.0F, -1.0F, -7.0F, 1.0F, 1.0F, 3.0F)
            .texOffs(0, 61)
            .addBox(-1.0F, -1.0F, -7.0F, 1.0F, 1.0F, 3.0F)
            .texOffs(0, 61)
            .addBox(2.0F, -1.0F, -7.0F, 1.0F, 1.0F, 3.0F),
         PartPose.ZERO
      );
      return LayerDefinition.create(mesh, 64, 64);
   }

   public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
      float walkWave = Mth.sin(limbSwing * 0.9F) * Math.min(limbSwingAmount, 1.0F);
      float walkWaveOpposite = Mth.sin(limbSwing * 0.9F + (float) Math.PI) * Math.min(limbSwingAmount, 1.0F);
      float attack = entity.getAttackAnim(0.0F);
      boolean isAttacking = attack > 0.0F || entity instanceof Mob mob && mob.isAggressive();
      float bite = isAttacking ? Mth.sin(attack * (float) Math.PI) * 1.15F : Mth.abs(Mth.sin(ageInTicks * 0.35F)) * 0.65F;
      boolean nearPlayer = entity instanceof WormEntity worm && worm.isNearPlayer();
      this.rear.xRot = walkWaveOpposite * 0.22F + (nearPlayer ? 0.18F : 0.0F);
      this.middle.xRot = walkWave * 0.32F + (nearPlayer ? -0.65F : 0.0F);
      this.head.xRot = walkWaveOpposite * 0.46F + (nearPlayer ? -0.95F : 0.0F);
      this.jaw.xRot = 0.08F + bite;
      this.head.yRot = netHeadYaw * (float) (Math.PI / 180.0) * 0.65F;
      this.head.xRot += headPitch * (float) (Math.PI / 180.0) * 0.35F;
   }

   public void renderToBuffer(PoseStack poseStack, VertexConsumer consumer, int light, int overlay, int color) {
      this.rear.render(poseStack, consumer, light, overlay, color);
      this.middle.render(poseStack, consumer, light, overlay, color);
      this.head.render(poseStack, consumer, light, overlay, color);
   }
}

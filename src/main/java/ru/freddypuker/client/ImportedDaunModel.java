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

public class ImportedDaunModel<T extends LivingEntity> extends EntityModel<T> {
   public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(FreddyPukerMod.id("daun"), "main");
   private final ModelPart bone3;
   private final ModelPart bone4;
   private final ModelPart bone6;
   private final ModelPart golovka;
   private final ModelPart bone5;
   private final ModelPart bone24;
   private final ModelPart bone7;
   private final ModelPart past;
   private final ModelPart bone;
   private final ModelPart bone8;
   private final ModelPart bone9;
   private final ModelPart bone10;
   private final ModelPart bone11;
   private final ModelPart bone12;
   private final ModelPart bone13;
   private final ModelPart bone14;
   private final ModelPart bone15;
   private final ModelPart bone2;
   private final ModelPart bone16;
   private final ModelPart bone17;
   private final ModelPart bone18;
   private final ModelPart bone19;
   private final ModelPart bone20;
   private final ModelPart bone21;
   private final ModelPart bone22;
   private final ModelPart bone23;

   public ImportedDaunModel(ModelPart root) {
      this.bone3 = root.getChild("bone3");
      this.bone4 = root.getChild("bone4");
      this.bone6 = root.getChild("bone6");
      this.golovka = root.getChild("golovka");
      this.bone5 = this.golovka.getChild("bone5");
      this.bone24 = this.bone5.getChild("bone24");
      this.bone7 = this.golovka.getChild("bone7");
      this.past = this.golovka.getChild("past");
      this.bone = this.past.getChild("bone");
      this.bone8 = this.bone.getChild("bone8");
      this.bone9 = this.bone8.getChild("bone9");
      this.bone10 = this.bone8.getChild("bone10");
      this.bone11 = this.bone8.getChild("bone11");
      this.bone12 = this.bone8.getChild("bone12");
      this.bone13 = this.bone8.getChild("bone13");
      this.bone14 = this.bone8.getChild("bone14");
      this.bone15 = this.bone8.getChild("bone15");
      this.bone2 = this.past.getChild("bone2");
      this.bone16 = this.bone2.getChild("bone16");
      this.bone17 = this.bone16.getChild("bone17");
      this.bone18 = this.bone16.getChild("bone18");
      this.bone19 = this.bone16.getChild("bone19");
      this.bone20 = this.bone16.getChild("bone20");
      this.bone21 = this.bone16.getChild("bone21");
      this.bone22 = this.bone16.getChild("bone22");
      this.bone23 = this.bone16.getChild("bone23");
   }

   public static LayerDefinition createBodyLayer() {
      MeshDefinition meshdefinition = new MeshDefinition();
      PartDefinition partdefinition = meshdefinition.getRoot();
      PartDefinition bone3 = partdefinition.addOrReplaceChild(
         "bone3",
         CubeListBuilder.create().texOffs(-14, -12).addBox(-3.0F, 1.0F, -14.0F, 6.0F, 6.0F, 14.0F, new CubeDeformation(0.0F)),
         PartPose.offset(0.0F, 17.0F, 1.0F)
      );
      PartDefinition cube_r1 = bone3.addOrReplaceChild(
         "cube_r1",
         CubeListBuilder.create().texOffs(-11, -6).addBox(-6.0F, -5.0F, -4.0F, 7.0F, 7.0F, 8.0F, new CubeDeformation(0.0F)),
         PartPose.offsetAndRotation(0.0F, 5.0F, -10.0F, 0.0F, 1.5708F, 0.0F)
      );
      PartDefinition bone4 = partdefinition.addOrReplaceChild("bone4", CubeListBuilder.create(), PartPose.offset(-9.0F, 18.0F, 9.0F));
      PartDefinition cube_r2 = bone4.addOrReplaceChild(
         "cube_r2",
         CubeListBuilder.create().texOffs(-14, -12).addBox(-3.0F, -6.0F, -13.0F, 6.0F, 6.0F, 14.0F, new CubeDeformation(0.0F)),
         PartPose.offsetAndRotation(1.0F, 6.0F, -1.0F, 0.0F, -0.7854F, 0.0F)
      );
      PartDefinition cube_r3 = bone4.addOrReplaceChild(
         "cube_r3",
         CubeListBuilder.create().texOffs(-11, -6).addBox(-6.0F, -5.0F, -4.0F, 7.0F, 7.0F, 8.0F, new CubeDeformation(0.0F)),
         PartPose.offsetAndRotation(7.0F, 4.0F, -7.0F, 0.0F, 0.7854F, 0.0F)
      );
      PartDefinition bone6 = partdefinition.addOrReplaceChild("bone6", CubeListBuilder.create(), PartPose.offset(0.0F, 18.0F, -12.0F));
      PartDefinition cube_r4 = bone6.addOrReplaceChild(
         "cube_r4",
         CubeListBuilder.create()
            .texOffs(-1, 0)
            .addBox(-2.0F, -2.0F, -1.0F, 3.0F, 1.0F, 2.0F, new CubeDeformation(0.0F))
            .texOffs(2, 0)
            .addBox(1.0F, -1.0F, -1.0F, 1.0F, 4.0F, 2.0F, new CubeDeformation(0.0F))
            .texOffs(-2, 0)
            .addBox(-3.0F, -1.0F, -1.0F, 1.0F, 4.0F, 2.0F, new CubeDeformation(0.0F))
            .texOffs(-1, 0)
            .addBox(-2.0F, 2.0F, -1.0F, 3.0F, 1.0F, 2.0F, new CubeDeformation(0.0F))
            .texOffs(0, 0)
            .addBox(-1.0F, -1.0F, -1.0F, 1.0F, 3.0F, 2.0F, new CubeDeformation(0.0F))
            .texOffs(-1, 0)
            .addBox(-2.0F, 3.0F, -1.0F, 3.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)),
         PartPose.offsetAndRotation(5.0F, 2.0F, -13.0F, 0.0F, -0.3927F, 0.0F)
      );
      PartDefinition cube_r5 = bone6.addOrReplaceChild(
         "cube_r5",
         CubeListBuilder.create().texOffs(-14, -12).addBox(-3.0F, -6.0F, -13.0F, 6.0F, 6.0F, 14.0F, new CubeDeformation(0.0F)),
         PartPose.offsetAndRotation(0.0F, 6.0F, -1.0F, 0.0F, -0.3927F, 0.0F)
      );
      PartDefinition golovka = partdefinition.addOrReplaceChild("golovka", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));
      PartDefinition bone5 = golovka.addOrReplaceChild("bone5", CubeListBuilder.create(), PartPose.offset(-7.0F, -3.0F, 9.0F));
      PartDefinition cube_r6 = bone5.addOrReplaceChild(
         "cube_r6",
         CubeListBuilder.create().texOffs(-15, -12).addBox(-4.0F, -6.0F, -13.0F, 6.0F, 6.0F, 14.0F, new CubeDeformation(0.0F)),
         PartPose.offsetAndRotation(-1.0F, 3.0F, -1.0F, 0.0F, 1.5708F, 0.0F)
      );
      PartDefinition bone24 = bone5.addOrReplaceChild("bone24", CubeListBuilder.create(), PartPose.offset(7.0F, 3.0F, -9.0F));
      PartDefinition bone7 = golovka.addOrReplaceChild(
         "bone7",
         CubeListBuilder.create()
            .texOffs(-12, -7)
            .addBox(-7.0F, -4.0F, -5.0F, 7.0F, 7.0F, 9.0F, new CubeDeformation(0.0F))
            .texOffs(-12, -7)
            .addBox(-14.0F, -5.0F, -5.0F, 7.0F, 8.0F, 9.0F, new CubeDeformation(0.0F)),
         PartPose.offset(-14.0F, -3.0F, 9.0F)
      );
      PartDefinition past = golovka.addOrReplaceChild("past", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));
      PartDefinition bone = past.addOrReplaceChild(
         "bone",
         CubeListBuilder.create()
            .texOffs(-9, -1)
            .addBox(-10.0F, 0.0F, -2.0F, 10.0F, 4.0F, 3.0F, new CubeDeformation(0.0F))
            .texOffs(-8, 0)
            .addBox(-10.0F, 0.0F, -4.0F, 10.0F, 1.0F, 2.0F, new CubeDeformation(0.0F))
            .texOffs(-9, -1)
            .addBox(-10.0F, 4.0F, -2.0F, 10.0F, 4.0F, 3.0F, new CubeDeformation(0.0F))
            .texOffs(-4, 0)
            .addBox(-16.0F, 3.0F, -4.0F, 6.0F, 5.0F, 2.0F, new CubeDeformation(0.0F))
            .texOffs(-4, 0)
            .addBox(-16.0F, 5.0F, -2.0F, 6.0F, 3.0F, 2.0F, new CubeDeformation(0.0F)),
         PartPose.offset(-28.0F, -8.0F, 12.0F)
      );
      PartDefinition bone8 = bone.addOrReplaceChild(
         "bone8",
         CubeListBuilder.create()
            .texOffs(-1, -2)
            .addBox(-31.0F, -1.0F, 6.0F, 1.0F, 1.0F, 4.0F, new CubeDeformation(0.0F))
            .texOffs(-1, -2)
            .addBox(-29.0F, -1.0F, 6.0F, 1.0F, 1.0F, 4.0F, new CubeDeformation(0.0F)),
         PartPose.offset(28.0F, 8.0F, -12.0F)
      );
      PartDefinition bone9 = bone8.addOrReplaceChild(
         "bone9",
         CubeListBuilder.create()
            .texOffs(-1, -2)
            .addBox(-31.0F, -1.0F, 6.0F, 1.0F, 1.0F, 4.0F, new CubeDeformation(0.0F))
            .texOffs(-1, -2)
            .addBox(-29.0F, -1.0F, 6.0F, 1.0F, 1.0F, 4.0F, new CubeDeformation(0.0F)),
         PartPose.offset(-4.0F, 0.0F, 0.0F)
      );
      PartDefinition bone10 = bone8.addOrReplaceChild(
         "bone10",
         CubeListBuilder.create()
            .texOffs(-1, -2)
            .addBox(-31.0F, -3.0F, 6.0F, 1.0F, 1.0F, 4.0F, new CubeDeformation(0.0F))
            .texOffs(-1, -2)
            .addBox(-31.0F, -1.0F, 6.0F, 1.0F, 1.0F, 4.0F, new CubeDeformation(0.0F))
            .texOffs(-1, -2)
            .addBox(-31.0F, 1.0F, 6.0F, 1.0F, 1.0F, 4.0F, new CubeDeformation(0.0F))
            .texOffs(-1, -2)
            .addBox(-29.0F, -1.0F, 6.0F, 1.0F, 1.0F, 4.0F, new CubeDeformation(0.0F)),
         PartPose.offset(-6.0F, -2.0F, 0.0F)
      );
      PartDefinition bone11 = bone8.addOrReplaceChild(
         "bone11",
         CubeListBuilder.create()
            .texOffs(-1, -2)
            .addBox(-31.0F, -1.0F, 6.0F, 1.0F, 1.0F, 4.0F, new CubeDeformation(0.0F))
            .texOffs(-1, -2)
            .addBox(-29.0F, -1.0F, 6.0F, 1.0F, 1.0F, 4.0F, new CubeDeformation(0.0F))
            .texOffs(-1, -2)
            .addBox(-27.0F, -1.0F, 6.0F, 1.0F, 1.0F, 4.0F, new CubeDeformation(0.0F)),
         PartPose.offset(-2.0F, -2.0F, 0.0F)
      );
      PartDefinition bone12 = bone8.addOrReplaceChild(
         "bone12",
         CubeListBuilder.create()
            .texOffs(-1, -2)
            .addBox(-31.0F, -1.0F, 6.0F, 1.0F, 1.0F, 4.0F, new CubeDeformation(0.0F))
            .texOffs(-1, -2)
            .addBox(-29.0F, -1.0F, 6.0F, 1.0F, 1.0F, 4.0F, new CubeDeformation(0.0F)),
         PartPose.offset(0.0F, -4.0F, 0.0F)
      );
      PartDefinition bone13 = bone8.addOrReplaceChild(
         "bone13",
         CubeListBuilder.create()
            .texOffs(-1, -2)
            .addBox(-31.0F, -1.0F, 6.0F, 1.0F, 1.0F, 4.0F, new CubeDeformation(0.0F))
            .texOffs(-1, -2)
            .addBox(-29.0F, -1.0F, 6.0F, 1.0F, 1.0F, 4.0F, new CubeDeformation(0.0F)),
         PartPose.offset(-4.0F, -4.0F, 0.0F)
      );
      PartDefinition bone14 = bone8.addOrReplaceChild(
         "bone14",
         CubeListBuilder.create()
            .texOffs(-1, -2)
            .addBox(-31.0F, -1.0F, 6.0F, 1.0F, 1.0F, 4.0F, new CubeDeformation(0.0F))
            .texOffs(-1, -2)
            .addBox(-29.0F, -1.0F, 6.0F, 1.0F, 1.0F, 4.0F, new CubeDeformation(0.0F)),
         PartPose.offset(-1.0F, -6.0F, 0.0F)
      );
      PartDefinition bone15 = bone8.addOrReplaceChild(
         "bone15",
         CubeListBuilder.create()
            .texOffs(-1, -2)
            .addBox(-31.0F, -1.0F, 6.0F, 1.0F, 1.0F, 4.0F, new CubeDeformation(0.0F))
            .texOffs(-1, -2)
            .addBox(-33.0F, -1.0F, 6.0F, 1.0F, 1.0F, 4.0F, new CubeDeformation(0.0F))
            .texOffs(-1, -2)
            .addBox(-29.0F, -1.0F, 6.0F, 1.0F, 1.0F, 4.0F, new CubeDeformation(0.0F)),
         PartPose.offset(-5.0F, -6.0F, 0.0F)
      );
      PartDefinition bone2 = past.addOrReplaceChild(
         "bone2",
         CubeListBuilder.create()
            .texOffs(-8, 0)
            .addBox(-10.0F, 0.0F, 2.0F, 10.0F, 1.0F, 2.0F, new CubeDeformation(0.0F))
            .texOffs(-9, -1)
            .addBox(-10.0F, 0.0F, -1.0F, 10.0F, 4.0F, 3.0F, new CubeDeformation(0.0F))
            .texOffs(-9, -1)
            .addBox(-10.0F, 4.0F, -1.0F, 10.0F, 4.0F, 3.0F, new CubeDeformation(0.0F))
            .texOffs(-4, 0)
            .addBox(-16.0F, 3.0F, 2.0F, 6.0F, 5.0F, 2.0F, new CubeDeformation(0.0F))
            .texOffs(-4, 0)
            .addBox(-16.0F, 5.0F, 0.0F, 6.0F, 3.0F, 2.0F, new CubeDeformation(0.0F)),
         PartPose.offset(-28.0F, -8.0F, 4.0F)
      );
      PartDefinition bone16 = bone2.addOrReplaceChild(
         "bone16",
         CubeListBuilder.create()
            .texOffs(-1, -2)
            .addBox(-31.0F, -1.0F, 6.0F, 1.0F, 1.0F, 4.0F, new CubeDeformation(0.0F))
            .texOffs(-1, -2)
            .addBox(-39.0F, -1.0F, 6.0F, 1.0F, 1.0F, 4.0F, new CubeDeformation(0.0F)),
         PartPose.offset(29.0F, 8.0F, -4.0F)
      );
      PartDefinition bone17 = bone16.addOrReplaceChild(
         "bone17",
         CubeListBuilder.create()
            .texOffs(-1, -2)
            .addBox(-31.0F, -1.0F, 6.0F, 1.0F, 1.0F, 4.0F, new CubeDeformation(0.0F))
            .texOffs(-1, -2)
            .addBox(-29.0F, -1.0F, 6.0F, 1.0F, 1.0F, 4.0F, new CubeDeformation(0.0F)),
         PartPose.offset(-4.0F, 0.0F, 0.0F)
      );
      PartDefinition bone18 = bone16.addOrReplaceChild(
         "bone18",
         CubeListBuilder.create()
            .texOffs(-1, -2)
            .addBox(-31.0F, -3.0F, 6.0F, 1.0F, 1.0F, 4.0F, new CubeDeformation(0.0F))
            .texOffs(-1, -2)
            .addBox(-31.0F, -1.0F, 6.0F, 1.0F, 1.0F, 4.0F, new CubeDeformation(0.0F))
            .texOffs(-1, -2)
            .addBox(-31.0F, 1.0F, 6.0F, 1.0F, 1.0F, 4.0F, new CubeDeformation(0.0F))
            .texOffs(-1, -2)
            .addBox(-29.0F, -1.0F, 6.0F, 1.0F, 1.0F, 4.0F, new CubeDeformation(0.0F)),
         PartPose.offset(-6.0F, -2.0F, 0.0F)
      );
      PartDefinition bone19 = bone16.addOrReplaceChild(
         "bone19",
         CubeListBuilder.create()
            .texOffs(-1, -2)
            .addBox(-31.0F, -1.0F, 6.0F, 1.0F, 1.0F, 4.0F, new CubeDeformation(0.0F))
            .texOffs(-1, -2)
            .addBox(-29.0F, -1.0F, 6.0F, 1.0F, 1.0F, 4.0F, new CubeDeformation(0.0F))
            .texOffs(-1, -2)
            .addBox(-37.0F, -1.0F, 6.0F, 1.0F, 1.0F, 4.0F, new CubeDeformation(0.0F)),
         PartPose.offset(-2.0F, -2.0F, 0.0F)
      );
      PartDefinition bone20 = bone16.addOrReplaceChild(
         "bone20",
         CubeListBuilder.create()
            .texOffs(-1, -2)
            .addBox(-31.0F, -1.0F, 6.0F, 1.0F, 1.0F, 4.0F, new CubeDeformation(0.0F))
            .texOffs(-1, -2)
            .addBox(-39.0F, -1.0F, 6.0F, 1.0F, 1.0F, 4.0F, new CubeDeformation(0.0F)),
         PartPose.offset(0.0F, -4.0F, 0.0F)
      );
      PartDefinition bone21 = bone16.addOrReplaceChild(
         "bone21",
         CubeListBuilder.create()
            .texOffs(-1, -2)
            .addBox(-31.0F, -1.0F, 6.0F, 1.0F, 1.0F, 4.0F, new CubeDeformation(0.0F))
            .texOffs(-1, -2)
            .addBox(-29.0F, -1.0F, 6.0F, 1.0F, 1.0F, 4.0F, new CubeDeformation(0.0F)),
         PartPose.offset(-4.0F, -4.0F, 0.0F)
      );
      PartDefinition bone22 = bone16.addOrReplaceChild(
         "bone22",
         CubeListBuilder.create()
            .texOffs(-1, -2)
            .addBox(-31.0F, -1.0F, 6.0F, 1.0F, 1.0F, 4.0F, new CubeDeformation(0.0F))
            .texOffs(-1, -2)
            .addBox(-29.0F, -1.0F, 6.0F, 1.0F, 1.0F, 4.0F, new CubeDeformation(0.0F)),
         PartPose.offset(-1.0F, -6.0F, 0.0F)
      );
      PartDefinition bone23 = bone16.addOrReplaceChild(
         "bone23",
         CubeListBuilder.create()
            .texOffs(-1, -2)
            .addBox(-31.0F, -1.0F, 6.0F, 1.0F, 1.0F, 4.0F, new CubeDeformation(0.0F))
            .texOffs(-1, -2)
            .addBox(-33.0F, -1.0F, 6.0F, 1.0F, 1.0F, 4.0F, new CubeDeformation(0.0F))
            .texOffs(-1, -2)
            .addBox(-29.0F, -1.0F, 6.0F, 1.0F, 1.0F, 4.0F, new CubeDeformation(0.0F)),
         PartPose.offset(-5.0F, -6.0F, 0.0F)
      );
      return LayerDefinition.create(meshdefinition, 16, 16);
   }

   public void setupAnim(LivingEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
   }

   public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, int packedColor) {
      this.bone3.render(poseStack, vertexConsumer, packedLight, packedOverlay, packedColor);
      this.bone4.render(poseStack, vertexConsumer, packedLight, packedOverlay, packedColor);
      this.bone6.render(poseStack, vertexConsumer, packedLight, packedOverlay, packedColor);
      this.golovka.render(poseStack, vertexConsumer, packedLight, packedOverlay, packedColor);
   }
}

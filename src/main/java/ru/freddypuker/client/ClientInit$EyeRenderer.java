package ru.freddypuker.client;

import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.resources.ResourceLocation;
import ru.freddypuker.FreddyPukerMod;
import ru.freddypuker.entity.EyeEntity;

final class ClientInit$EyeRenderer extends MobRenderer<EyeEntity, EyeModel<EyeEntity>> {
   ClientInit$EyeRenderer(Context context) {
      super(context, new EyeModel(context.bakeLayer(EyeModel.LAYER_LOCATION)), 0.0F);
   }

   public ResourceLocation getTextureLocation(EyeEntity entity) {
      return FreddyPukerMod.id("textures/entity/eyes.png");
   }
}

package ru.freddypuker.client;

import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.resources.ResourceLocation;
import ru.freddypuker.FreddyPukerMod;
import ru.freddypuker.entity.FreddyEntity;

final class ClientInit$FreddyRenderer extends MobRenderer<FreddyEntity, FreddyModel<FreddyEntity>> {
   private final ResourceLocation texture;

   ClientInit$FreddyRenderer(Context context, String textureName, float scale) {
      super(context, new FreddyModel(context.bakeLayer(FreddyModel.LAYER_LOCATION)), scale);
      this.texture = FreddyPukerMod.id("textures/entity/" + textureName);
   }

   public ResourceLocation getTextureLocation(FreddyEntity entity) {
      return this.texture;
   }
}

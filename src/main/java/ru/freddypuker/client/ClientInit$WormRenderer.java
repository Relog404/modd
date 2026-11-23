package ru.freddypuker.client;

import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.resources.ResourceLocation;
import ru.freddypuker.entity.WormEntity;

final class ClientInit$WormRenderer extends MobRenderer<WormEntity, ImportedDaunModel<WormEntity>> {
   ClientInit$WormRenderer(Context context) {
      super(context, new ImportedDaunModel(context.bakeLayer(ImportedDaunModel.LAYER_LOCATION)), 0.45F);
   }

   public ResourceLocation getTextureLocation(WormEntity entity) {
      return ResourceLocation.withDefaultNamespace("textures/block/white_concrete.png");
   }
}

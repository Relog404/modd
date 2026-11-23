package ru.freddypuker.client;

import net.minecraft.world.entity.EntityType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent.RegisterLayerDefinitions;
import net.minecraftforge.client.event.EntityRenderersEvent.RegisterRenderers;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import ru.freddypuker.FreddyPukerMod;

@EventBusSubscriber(modid = "freddypuker", bus = Bus.MOD, value = Dist.CLIENT)
public final class ClientInit {
   private ClientInit() {
   }

   @SubscribeEvent
   public static void registerLayers(RegisterLayerDefinitions event) {
      event.registerLayerDefinition(FreddyModel.LAYER_LOCATION, FreddyModel::createBodyLayer);
      event.registerLayerDefinition(ImportedDaunModel.LAYER_LOCATION, ImportedDaunModel::createBodyLayer);
      event.registerLayerDefinition(EyeModel.LAYER_LOCATION, EyeModel::createBodyLayer);
   }

   @SubscribeEvent
   public static void registerRenderers(RegisterRenderers event) {
      event.registerEntityRenderer((EntityType)FreddyPukerMod.FREDDY.get(), ctx -> new ClientInit$FreddyRenderer(ctx, "freddy.png", 1.05F));
      event.registerEntityRenderer((EntityType)FreddyPukerMod.WORM.get(), ctx -> new ClientInit$WormRenderer(ctx));
      event.registerEntityRenderer((EntityType)FreddyPukerMod.EYE.get(), ClientInit$EyeRenderer::new);
   }
}

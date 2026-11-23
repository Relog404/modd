package ru.freddypuker;

import net.minecraft.world.entity.EntityType;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import ru.freddypuker.entity.EyeEntity;
import ru.freddypuker.entity.FreddyEntity;
import ru.freddypuker.entity.WormEntity;

public final class ModEntities {
   private ModEntities() {
   }

   @SubscribeEvent
   public static void registerAttributes(EntityAttributeCreationEvent event) {
      event.put((EntityType)FreddyPukerMod.FREDDY.get(), FreddyEntity.createAttributes().build());
      event.put((EntityType)FreddyPukerMod.WORM.get(), WormEntity.createAttributes().build());
      event.put((EntityType)FreddyPukerMod.EYE.get(), EyeEntity.createAttributes().build());
   }
}

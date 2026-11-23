package ru.freddypuker.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.TickEvent.ClientTickEvent;
import net.minecraftforge.event.TickEvent.Phase;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import ru.freddypuker.entity.FreddyEntity;

@EventBusSubscriber(modid = "freddypuker", bus = Bus.FORGE, value = Dist.CLIENT)
public final class ClientInit$ClientEvents {
   private static int cooldown;

   private ClientInit$ClientEvents() {
   }

   @SubscribeEvent
   public static void onClientTick(ClientTickEvent event) {
      if (event.phase == Phase.END) {
         Minecraft minecraft = Minecraft.getInstance();
         if (cooldown > 0) {
            cooldown--;
         }

         if (cooldown == 0
            && minecraft.player != null
            && minecraft.level != null
            && minecraft.level
               .getEntitiesOfClass(FreddyEntity.class, minecraft.player.getBoundingBox().inflate(16.0))
               .stream()
               .anyMatch(FreddyEntity::isJumpscare)) {
            minecraft.setScreen(new ClientInit$JumpscareScreen());
            cooldown = 20;
         }

         if (minecraft.player != null && minecraft.level != null) {
            for (FreddyEntity freddy : minecraft.level.getEntitiesOfClass(FreddyEntity.class, minecraft.player.getBoundingBox().inflate(64.0))) {
               trackFreddyHead(freddy, minecraft.player);
            }
         }
      }
   }

   private static void trackFreddyHead(FreddyEntity freddy, LocalPlayer player) {
      Vec3 source = freddy.getEyePosition();
      Vec3 target = player.getEyePosition();
      double dx = target.x - source.x;
      double dy = target.y - source.y;
      double dz = target.z - source.z;
      double horizontal = Math.sqrt(dx * dx + dz * dz);
      if (!(horizontal < 0.001)) {
         float yaw = (float)(Mth.atan2(dz, dx) * (180.0 / Math.PI)) - 90.0F;
         float pitch = (float)(-(Mth.atan2(dy, horizontal) * (180.0 / Math.PI)));
         freddy.setYHeadRot(yaw);
         freddy.yHeadRotO = yaw;
         freddy.setXRot(pitch);
         freddy.xRotO = pitch;
      }
   }
}

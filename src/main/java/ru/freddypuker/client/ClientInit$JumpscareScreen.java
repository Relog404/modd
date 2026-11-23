package ru.freddypuker.client;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import ru.freddypuker.FreddyPukerMod;

final class ClientInit$JumpscareScreen extends Screen {
   private int age;

   ClientInit$JumpscareScreen() {
      super(Component.empty());
   }

   public void tick() {
      if (++this.age >= 6) {
         this.minecraft.setScreen(null);
      }
   }

   public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
      if ((this.age & 1) == 0) {
         graphics.fill(0, 0, this.width, this.height, -1);
      } else {
         graphics.blit(FreddyPukerMod.id("textures/entity/freddy.png"), 0, 0, this.width, this.height, 0.0F, 0.0F, 128, 128, 128, 128);
         graphics.fill(0, 0, this.width, this.height, 1428160512);
      }
   }

   public boolean isPauseScreen() {
      return false;
   }

   public boolean shouldCloseOnEsc() {
      return false;
   }
}

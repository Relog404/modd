package ru.freddypuker.entity;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier.Builder;
import net.minecraft.world.level.Level;

public class EyeEntity extends Mob {
   public EyeEntity(EntityType<? extends Mob> type, Level level) {
      super(type, level);
      this.setNoGravity(true);
      this.setInvulnerable(true);
   }

   public static Builder createAttributes() {
      return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 1.0);
   }

   protected void registerGoals() {
   }

   public boolean isPushable() {
      return false;
   }

   public void tick() {
      super.tick();
      if (!this.level().isClientSide) {
         double driftX = Math.sin((this.tickCount + this.getId() * 17) * 0.025) * 0.012;
         double driftZ = Math.cos((this.tickCount + this.getId() * 11) * 0.021) * 0.012;
         this.setDeltaMovement(driftX, Math.sin(this.tickCount * 0.04) * 0.006, driftZ);
      }
   }
}

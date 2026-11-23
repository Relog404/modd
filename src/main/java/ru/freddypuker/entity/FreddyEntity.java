package ru.freddypuker.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.network.syncher.SynchedEntityData.Builder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public class FreddyEntity extends Monster {
   private static final EntityDataAccessor<Boolean> JUMPSCARE = SynchedEntityData.defineId(FreddyEntity.class, EntityDataSerializers.BOOLEAN);

   public FreddyEntity(EntityType<? extends Monster> type, Level level) {
      super(type, level);
      this.setPersistenceRequired();
      this.setInvulnerable(true);
   }

   protected void defineSynchedData(Builder builder) {
      super.defineSynchedData(builder);
      builder.define(JUMPSCARE, false);
   }

   public void setJumpscare(boolean jumpscare) {
      this.entityData.set(JUMPSCARE, jumpscare);
   }

   public boolean isJumpscare() {
      return (Boolean)this.entityData.get(JUMPSCARE);
   }

   public static net.minecraft.world.entity.ai.attributes.AttributeSupplier.Builder createAttributes() {
      return Monster.createMonsterAttributes()
         .add(Attributes.MAX_HEALTH, 1000000.0)
         .add(Attributes.MOVEMENT_SPEED, 0.5)
         .add(Attributes.ATTACK_DAMAGE, 20.0)
         .add(Attributes.FOLLOW_RANGE, 48.0)
         .add(Attributes.KNOCKBACK_RESISTANCE, 1.0);
   }

   protected void registerGoals() {
      this.goalSelector.addGoal(1, new MeleeAttackGoal(this, 1.0, true));
      this.targetSelector.addGoal(1, new NearestAttackableTargetGoal(this, Player.class, false));
   }

   protected float getWaterSlowDown() {
      return 1.0F;
   }

   public void aiStep() {
      if (!this.level().isClientSide && this.getTarget() != null) {
         BlockPos ahead = this.blockPosition().relative(this.getDirection());

         for (int y = 0; y <= 1; y++) {
            BlockPos obstacle = ahead.above(y);
            if (!this.level().getBlockState(obstacle).isAir() && this.level().getBlockState(obstacle).getDestroySpeed(this.level(), obstacle) >= 0.0F) {
               this.level().destroyBlock(obstacle, true, this);
            }
         }
      }

      super.aiStep();
   }

   public void setTarget(LivingEntity target) {
      super.setTarget(target);
      if (target != null && this.getAttribute(Attributes.MOVEMENT_SPEED) != null) {
         this.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(0.5);
      }
   }

   public boolean canBeAffected(MobEffectInstance effect) {
      return false;
   }

   public void tick() {
      super.tick();
      if (!this.level().isClientSide && this.getPersistentData().contains("fp_death_scare_ticks")) {
         int ticks = this.getPersistentData().getInt("fp_death_scare_ticks") - 1;
         if (ticks <= 0) {
            this.discard();
         } else {
            this.getPersistentData().putInt("fp_death_scare_ticks", ticks);
         }
      }

      if (!this.level().isClientSide && this.isJumpscare() && this.tickCount > 12 && !this.getPersistentData().contains("fp_death_scare_ticks")) {
         this.setJumpscare(false);
      }

      if (!this.level().isClientSide && this.getPersistentData().contains("fp_ghost_ticks")) {
         int ticks = this.getPersistentData().getInt("fp_ghost_ticks") - 1;
         if (ticks <= 0) {
            this.discard();
         } else {
            this.getPersistentData().putInt("fp_ghost_ticks", ticks);
         }
      }
   }
}

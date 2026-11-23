package ru.freddypuker.entity;

import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier.Builder;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import ru.freddypuker.ModSounds;

public class WormEntity extends Monster {
   private static final EntityDataAccessor<Boolean> NEAR_PLAYER = SynchedEntityData.defineId(WormEntity.class, EntityDataSerializers.BOOLEAN);

   public WormEntity(EntityType<? extends Monster> type, Level level) {
      super(type, level);
      this.setPersistenceRequired();
      this.setInvulnerable(true);
   }

   public static Builder createAttributes() {
      return Monster.createMonsterAttributes()
         .add(Attributes.MAX_HEALTH, 1000000.0)
         .add(Attributes.MOVEMENT_SPEED, 0.15)
         .add(Attributes.ATTACK_DAMAGE, 8.0)
         .add(Attributes.FOLLOW_RANGE, 16.0)
         .add(Attributes.KNOCKBACK_RESISTANCE, 1.0);
   }

   protected void registerGoals() {
      this.goalSelector.addGoal(1, new MeleeAttackGoal(this, 1.5, true));
      this.goalSelector.addGoal(2, new RandomStrollGoal(this, 0.8));
      this.targetSelector.addGoal(1, new NearestAttackableTargetGoal(this, Player.class, true));
   }

   protected void defineSynchedData(net.minecraft.network.syncher.SynchedEntityData.Builder builder) {
      super.defineSynchedData(builder);
      builder.define(NEAR_PLAYER, false);
   }

   public boolean isNearPlayer() {
      return (Boolean)this.entityData.get(NEAR_PLAYER);
   }

   public void aiStep() {
      Player nearbyPlayer = null;
      boolean shouldRetreat = false;
      Vec3 retreatDirection = Vec3.ZERO;
      if (!this.level().isClientSide) {
         LivingEntity target = this.getTarget();
         nearbyPlayer = this.level().getNearestPlayer(this, 5.0);
         boolean wasNearPlayer = this.isNearPlayer();
         boolean nearPlayer = target instanceof Player && this.distanceToSqr(target) < 25.0;
         if (target instanceof Player targetPlayer && this.distanceToSqr(targetPlayer) > 25.0) {
            nearbyPlayer = targetPlayer;
            nearPlayer = false;
         }

         if (!nearPlayer && nearbyPlayer != null) {
            nearPlayer = true;
         }

         this.entityData.set(NEAR_PLAYER, nearPlayer);
         if (nearPlayer && !wasNearPlayer) {
            this.playSound((SoundEvent)ModSounds.WORM_HISS.get(), 1.0F, 0.8F + this.random.nextFloat() * 0.25F);
         }

         if (nearbyPlayer != null && this.distanceToSqr(nearbyPlayer) > 9.0) {
            shouldRetreat = true;
            retreatDirection = this.position().subtract(nearbyPlayer.position()).normalize();
         }
      }

      super.aiStep();
      if (!this.level().isClientSide && shouldRetreat && nearbyPlayer != null) {
         this.setTarget(null);
         Vec3 retreatTarget = this.position().add(retreatDirection.scale(8.0));
         this.getNavigation().moveTo(retreatTarget.x, retreatTarget.y, retreatTarget.z, 1.5);
      }
   }

   public boolean doHurtTarget(Entity target) {
      boolean hit = super.doHurtTarget(target);
      if (hit && target instanceof LivingEntity living) {
         living.addEffect(new MobEffectInstance(MobEffects.POISON, 100, 1));
      }

      return hit;
   }
}

package ru.freddypuker;

import net.minecraft.core.registries.Registries;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public final class ModSounds {
   public static final DeferredRegister<SoundEvent> SOUNDS = DeferredRegister.create(Registries.SOUND_EVENT, "freddypuker");
   public static final RegistryObject<SoundEvent> FREDDY_SCREAM = register("freddy_scream");
   public static final RegistryObject<SoundEvent> METAL_SCREECH = register("metal_screech");
   public static final RegistryObject<SoundEvent> HEAVY_STEPS = register("heavy_steps");
   public static final RegistryObject<SoundEvent> HEARTBEAT = register("heartbeat");
   public static final RegistryObject<SoundEvent> WHISPER = register("whisper");
   public static final RegistryObject<SoundEvent> WORM_HISS = register("worm_hiss");
   public static final RegistryObject<SoundEvent> CHILD_CRY = register("child_cry");
   public static final RegistryObject<SoundEvent> THUNDER = register("thunder");
   public static final RegistryObject<SoundEvent> PORTAL_ENTER = register("portal_enter");
   public static final RegistryObject<SoundEvent> JUMPSCARE = register("jumpscare");
   public static final RegistryObject<SoundEvent> WIND = register("wind");
   public static final RegistryObject<SoundEvent> DISC_13_HORROR = register("disc_13_horror");

   private ModSounds() {
   }

   private static RegistryObject<SoundEvent> register(String name) {
      return SOUNDS.register(name, () -> SoundEvent.createVariableRangeEvent(FreddyPukerMod.id(name)));
   }
}

package ru.freddypuker;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.EntityType.Builder;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Item.Properties;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import ru.freddypuker.entity.EyeEntity;
import ru.freddypuker.entity.FreddyEntity;
import ru.freddypuker.entity.WormEntity;

@Mod("freddypuker")
public final class FreddyPukerMod {
   public static final String MOD_ID = "freddypuker";
   public static final String MOD_NAME = "Freddy Half Puker";
   public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(Registries.ENTITY_TYPE, "freddypuker");
   public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(Registries.ITEM, "freddypuker");
   public static final ResourceKey<Level> DREAM_LEVEL = ResourceKey.create(Registries.DIMENSION, id("dream"));
   public static final RegistryObject<EntityType<FreddyEntity>> FREDDY = ENTITIES.register(
      "freddy_puker",
      () -> Builder.of(FreddyEntity::new, MobCategory.MONSTER).sized(1.25F, 3.6F).clientTrackingRange(12).updateInterval(2).build("freddy_puker")
   );
   public static final RegistryObject<EntityType<WormEntity>> WORM = ENTITIES.register(
      "worm", () -> Builder.of(WormEntity::new, MobCategory.MONSTER).sized(1.3F, 1.1F).clientTrackingRange(10).updateInterval(2).build("worm")
   );
   public static final RegistryObject<EntityType<EyeEntity>> EYE = ENTITIES.register(
      "eye", () -> Builder.of(EyeEntity::new, MobCategory.MISC).sized(0.5F, 0.5F).clientTrackingRange(12).updateInterval(2).build("eye")
   );
   public static final RegistryObject<Item> FREDDY_CLAWS = ITEMS.register("freddy_claws", () -> new Item(new Properties()));
   public static final RegistryObject<Item> FREDDY_PANTS = ITEMS.register("freddy_pants", () -> new Item(new Properties()));
   public static final RegistryObject<Item> FREDDY_HAT = ITEMS.register("freddy_hat", () -> new Item(new Properties()));
   public static final RegistryObject<Item> FREDDY_BOOTS = ITEMS.register("freddy_boots", () -> new Item(new Properties()));
   public static final RegistryObject<Item> FREDDY_BRAIN = ITEMS.register("freddy_brain", () -> new Item(new Properties()));

   public FreddyPukerMod() {
      this(FMLJavaModLoadingContext.get().getModEventBus());
   }

   public FreddyPukerMod(IEventBus modBus) {
      ENTITIES.register(modBus);
      ITEMS.register(modBus);
      ModSounds.SOUNDS.register(modBus);
      modBus.addListener(ModEntities::registerAttributes);
      MinecraftForge.EVENT_BUS.register(HorrorController.class);
   }

   public static ResourceLocation id(String path) {
      return ResourceLocation.fromNamespaceAndPath("freddypuker", path);
   }
}

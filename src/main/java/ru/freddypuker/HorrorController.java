package ru.freddypuker;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.Filterable;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BiomeTags;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Spider;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.WritableBookContent;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ClipContext.Fluid;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.block.entity.LecternBlockEntity;
import net.minecraft.world.level.block.entity.SignBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap.Types;
import net.minecraft.world.level.saveddata.SavedData.Factory;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.HitResult.Type;
import net.minecraftforge.event.TickEvent.Phase;
import net.minecraftforge.event.TickEvent.PlayerTickEvent;
import net.minecraftforge.event.TickEvent.ServerTickEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.player.PlayerSleepInBedEvent;
import net.minecraftforge.event.entity.player.PlayerEvent.PlayerRespawnEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.RightClickBlock;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import ru.freddypuker.HorrorController.StrangePlayerData;
import ru.freddypuker.entity.EyeEntity;
import ru.freddypuker.entity.FreddyEntity;
import ru.freddypuker.entity.WormEntity;

public final class HorrorController {
   private static final String OAK_TICKS = "fp_oak_ticks";
   private static final String STORY_STARTED = "fp_story_started";
   private static final String EVENT_ACTIVE = "fp_event_active";
   private static final String EVENT_TICKS = "fp_event_ticks";
   private static final String ARENA_MIN_X = "fp_arena_min_x";
   private static final String ARENA_MAX_X = "fp_arena_max_x";
   private static final String ARENA_MIN_Z = "fp_arena_min_z";
   private static final String ARENA_MAX_Z = "fp_arena_max_z";
   private static final String ARENA_DIM = "fp_arena_dim";
   private static final String ARENA_CHESTS = "fp_arena_chests";
   private static final String ARENA_WALL_SNAPSHOT = "fp_arena_wall_snapshot";
   private static final String CHESTS_OPENED = "fp_chests_opened";
   private static final String CHASE_ACTIVE = "fp_chase_active";
   private static final String CHASE_TICKS = "fp_chase_ticks";
   private static final String MAZE_ACTIVE = "fp_maze_active";
   private static final String MAZE_MIN_X = "fp_maze_min_x";
   private static final String MAZE_MIN_Z = "fp_maze_min_z";
   private static final String MAZE_DIM = "fp_maze_dim";
   private static final String FALSE_PLATFORM = "fp_false_platform";
   private static final String DREAM_READY = "fp_dream_ready";
   private static final String DREAM_SENT = "fp_dream_sent";
   private static final String DREAM_ACTIVE = "fp_dream_active";
   private static final String DREAM_BUILT = "fp_dream_built";
   private static final String FINAL_FREDDY_ACTIVE = "fp_final_freddy_active";
   private static final String FINAL_FREDDY_TICKS = "fp_final_freddy_ticks";
   private static final String EYE_LOOK = "fp_eye_look";
   private static final String EYE_TIMER = "fp_eye_timer";
   private static final String EYE_TIMER_ACTIVE = "fp_eye_timer_active";
   private static final String RETURN_TO_SPAWN = "fp_return_to_spawn";
   private static final String RETURN_TO_SKY_PLATFORM = "fp_return_to_sky_platform";
   private static final String SKY_PLATFORM_X = "fp_sky_platform_x";
   private static final String SKY_PLATFORM_Z = "fp_sky_platform_z";
   private static final String DREAM_SLEEP_REQUESTED = "fp_dream_sleep_requested";
   private static final String STRANGE_PLAYER_DATA = "fp_strange_player";
   private static final int OAK_FOREST_DELAY = 3800;
   private static final int SEARCH_TIME = 6000;
   private static final int DREAM_KILL_TIME = 1200;
   private static final int FINAL_FREDDY_CHASE_TIME = 240;
   private static final int MAX_ARENA_HEIGHT = 254;
   private static final long TICKS_PER_GAME_DAY = 24000L;
   private static final long STRANGE_PLAYER_MESSAGE_INTERVAL = 40L;
   private static final String STRANGE_PLAYER_NAME = "unknown_player";
   private static final List<String> STRANGE_PLAYER_MESSAGES = List.of("сын бляди, я тебя найду", "ты умрешь в муках", "пидорас вай биииляяя");

   private HorrorController() {
   }

   @SubscribeEvent
   public static void onServerTick(ServerTickEvent event) {
      if (event.phase == Phase.END) {
         ServerLevel overworld = event.getServer().overworld();
         StrangePlayerData data = (StrangePlayerData)overworld.getDataStorage()
            .computeIfAbsent(new Factory(StrangePlayerData::new, StrangePlayerData::load, null), "fp_strange_player");
         long gameTime = overworld.getGameTime();
         if (data.scheduledAt < 0L) {
            int delayInDays = 3 + overworld.random.nextInt(3);
            data.scheduledAt = gameTime + delayInDays * 24000L;
            data.setDirty();
         }

         if (!data.triggered && gameTime >= data.scheduledAt) {
            data.triggered = true;
            data.messageIndex = overworld.random.nextInt(STRANGE_PLAYER_MESSAGES.size());
            data.nextMessageAt = gameTime;
            data.setDirty();
         }

         if (data.triggered && data.nextMessageAt >= 0L && gameTime >= data.nextMessageAt) {
            broadcastStrangePlayerMessage(event.getServer(), data.messageIndex);
            data.nextMessageAt = -1L;
            data.setDirty();
         }
      }
   }

   private static void broadcastStrangePlayerMessage(MinecraftServer server, int index) {
      Component message = Component.literal("<unknown_player> " + (String)STRANGE_PLAYER_MESSAGES.get(Math.floorMod(index, STRANGE_PLAYER_MESSAGES.size())))
         .withStyle(ChatFormatting.DARK_RED);
      server.getPlayerList().broadcastSystemMessage(message, false);

      for (ServerPlayer player : server.getPlayerList().getPlayers()) {
         player.playSound(SoundEvents.UI_TOAST_CHALLENGE_COMPLETE, 0.3F, 0.65F);
      }
   }

   @SubscribeEvent
   public static void onPlayerTick(PlayerTickEvent event) {
      if (event.phase == Phase.END) {
         if (event.player instanceof ServerPlayer player && !player.level().isClientSide()) {
            CompoundTag data = player.getPersistentData();
            if (!data.getBoolean("fp_event_active")
               && !data.getBoolean("fp_chase_active")
               && !data.getBoolean("fp_maze_active")
               && !data.getBoolean("fp_dream_active")
               && !data.getBoolean("fp_final_freddy_active")) {
               tickOakForest(player, data);
            }

            if (data.getBoolean("fp_event_active")) {
               tickSearchEvent(player, data);
            }

            if (data.getBoolean("fp_chase_active")) {
               tickFreddyChase(player, data);
            }

            if (data.getBoolean("fp_maze_active")) {
               tickMaze(player, data);
            }

            if (data.getBoolean("fp_false_platform")) {
               tickFalsePlatform(player, data);
            }

            if (data.getBoolean("fp_dream_active")) {
               tickDream(player, data);
            }

            if (data.getBoolean("fp_final_freddy_active")) {
               tickFinalFreddy(player, data);
            }

            if (data.getBoolean("fp_dream_sleep_requested") && data.getBoolean("fp_dream_ready") && !data.getBoolean("fp_dream_sent")) {
               data.putBoolean("fp_dream_sleep_requested", false);
               enterDream(player, data);
            }
         }
      }
   }

   @SubscribeEvent
   public static void onPlayerSleep(PlayerSleepInBedEvent event) {
      if (event.getEntity() instanceof ServerPlayer player && player.level().dimension() == Level.OVERWORLD) {
         CompoundTag data = player.getPersistentData();
         if (data.getBoolean("fp_dream_active") || data.getBoolean("fp_final_freddy_active")) {
            return;
         }

         // One roll per sleep attempt: mansion 40%, woods 40%, vanilla sleep 20%.
         int roll = player.getRandom().nextInt(100);
         data.putBoolean("fp_dream_sent", false);
         data.putBoolean("fp_dream_ready", roll < 80);
         data.putBoolean("fp_dream_sleep_requested", roll < 80);
         if (roll < 40) {
            data.putString("fp_sleep_outcome", "mansion");
         } else if (roll < 80) {
            data.putString("fp_sleep_outcome", "woods");
         } else {
            data.remove("fp_sleep_outcome");
         }
      }
   }

   @SubscribeEvent
   public static void onPlayerRespawn(PlayerRespawnEvent event) {
      if (event.getEntity() instanceof ServerPlayer player) {
         CompoundTag data = player.getPersistentData();
         if (data.getBoolean("fp_return_to_sky_platform")) {
            data.putBoolean("fp_return_to_sky_platform", false);
            data.putBoolean("fp_return_to_spawn", false);
            ServerLevel target = player.server.overworld();
            int x = data.getInt("fp_sky_platform_x");
            int z = data.getInt("fp_sky_platform_z");
            buildSkyDeathPlatform(target, x, z, player.getGameProfile().getName());
            data.putBoolean("fp_sky_platform_return", true);
            data.putBoolean("fp_false_platform", true);
            data.putBoolean("fp_dream_active", false);
            data.putBoolean("fp_final_freddy_active", false);
            player.gameMode.changeGameModeForPlayer(GameType.SURVIVAL);
            player.teleportTo(target, x + 3.5, 246.1, z + 3.5, player.getYRot(), player.getXRot());
            player.displayClientMessage(
               Component.literal("Фредди убил тебя. Ты очнулся на платформе в небесах.")
                  .withStyle(new ChatFormatting[]{ChatFormatting.DARK_RED, ChatFormatting.BOLD}),
               false
            );
         } else if (data.getBoolean("fp_return_to_spawn")) {
            data.putBoolean("fp_return_to_spawn", false);
            player.gameMode.changeGameModeForPlayer(GameType.SURVIVAL);
            ServerLevel overworld = player.server.overworld();
            BlockPos spawn = overworld.getSharedSpawnPos();
            player.teleportTo(overworld, spawn.getX() + 0.5, spawn.getY() + 1.0, spawn.getZ() + 0.5, player.getYRot(), player.getXRot());
            player.displayClientMessage(Component.literal("Фредди убил тебя. Ты вернулся в обычный мир.").withStyle(ChatFormatting.GRAY), false);
         }
      }
   }

   @SubscribeEvent
   public static void onLivingDeath(LivingDeathEvent event) {
      if (event.getEntity() instanceof ServerPlayer player) {
         CompoundTag var3 = player.getPersistentData();
         if (event.getSource().getEntity() instanceof FreddyEntity) {
            if ("woods".equals(var3.getString("fp_sleep_outcome"))) {
               var3.putBoolean("fp_return_to_spawn", true);
               var3.putBoolean("fp_return_to_sky_platform", false);
            } else {
               prepareSkyPlatformReturn(player, var3);
            }
            var3.putBoolean("fp_dream_active", false);
            var3.putBoolean("fp_final_freddy_active", false);
         }

         if (var3.getBoolean("fp_event_active") || var3.getBoolean("fp_chase_active")) {
            removeArena(player, var3);
            removeArenaFreddies(player, var3);
            var3.putBoolean("fp_event_active", false);
            var3.putBoolean("fp_chase_active", false);
            var3.putInt("fp_event_ticks", 0);
            var3.putInt("fp_chase_ticks", 0);
            if (!var3.getBoolean("fp_return_to_sky_platform")) {
               var3.putBoolean("fp_return_to_spawn", true);
            }
         }
      }
   }

   @SubscribeEvent
   public static void onChestOpened(RightClickBlock event) {
      if (event.getLevel() instanceof ServerLevel level
         && event.getEntity() instanceof ServerPlayer player
         && player.getPersistentData().getBoolean("fp_event_active")
         && level.getBlockEntity(event.getPos()) instanceof ChestBlockEntity chest
         && chest.isEmpty()
         && !hasOpenedChest(player.getPersistentData(), event.getPos())) {
         markChestOpened(player.getPersistentData(), event.getPos());
         level.playSound(null, event.getPos(), SoundEvents.CHEST_LOCKED, SoundSource.BLOCKS, 1.0F, 0.7F);

         for (int i = 0; i < 3 + level.random.nextInt(2); i++) {
            Spider spider = (Spider)EntityType.SPIDER.create(level);
            if (spider != null) {
               spider.moveTo(
                  event.getPos().getX() + 0.5 + (level.random.nextDouble() - 0.5) * 2.0,
                  event.getPos().getY() + 1.0,
                  event.getPos().getZ() + 0.5 + (level.random.nextDouble() - 0.5) * 2.0,
                  level.random.nextFloat() * 360.0F,
                  0.0F
               );
               level.addFreshEntity(spider);
            }
         }

         level.playSound(null, event.getPos(), (SoundEvent)ModSounds.JUMPSCARE.get(), SoundSource.HOSTILE, 1.8F, 0.7F);
         FreddyEntity ghost = (FreddyEntity)((EntityType)FreddyPukerMod.FREDDY.get()).create(level);
         if (ghost != null) {
            ghost.setNoAi(true);
            ghost.getPersistentData().putInt("fp_ghost_ticks", 40);
            ghost.moveTo(event.getPos().getX() + 0.5, event.getPos().getY() + 1.0, event.getPos().getZ() + 0.5, player.getYRot(), 0.0F);
            level.addFreshEntity(ghost);
            level.playSound(null, event.getPos(), (SoundEvent)ModSounds.JUMPSCARE.get(), SoundSource.HOSTILE, 1.6F, 0.65F);
         }
      }
   }

   private static boolean hasOpenedChest(CompoundTag data, BlockPos pos) {
      ListTag opened = data.getList("fp_chests_opened", 10);

      for (int i = 0; i < opened.size(); i++) {
         CompoundTag entry = opened.getCompound(i);
         if (entry.getInt("x") == pos.getX() && entry.getInt("y") == pos.getY() && entry.getInt("z") == pos.getZ()) {
            return true;
         }
      }

      return false;
   }

   private static void markChestOpened(CompoundTag data, BlockPos pos) {
      ListTag opened = data.getList("fp_chests_opened", 10);
      CompoundTag entry = new CompoundTag();
      entry.putInt("x", pos.getX());
      entry.putInt("y", pos.getY());
      entry.putInt("z", pos.getZ());
      opened.add(entry);
      data.put("fp_chests_opened", opened);
   }

   private static void tickOakForest(ServerPlayer player, CompoundTag data) {
      if (!data.getBoolean("fp_story_started")) {
         if (player.level().getBiome(player.blockPosition()).is(BiomeTags.IS_FOREST)) {
            int ticks = data.getInt("fp_oak_ticks") + 1;
            data.putInt("fp_oak_ticks", ticks);
            ServerLevel level = player.serverLevel();
            if (ticks % 600 == 0) {
               level.playSound(null, player.blockPosition(), (SoundEvent)ModSounds.WHISPER.get(), SoundSource.AMBIENT, 0.35F, 0.65F);
               player.displayClientMessage(Component.literal("Шёпот доносится из темноты...").withStyle(ChatFormatting.DARK_RED), true);
            }

            if (ticks % 1200 == 0) {
               level.playSound(null, player.blockPosition(), SoundEvents.WOOD_STEP, SoundSource.BLOCKS, 0.75F, 0.5F);
            }

            if (ticks >= 2400 && ticks % 100 == 0) {
               player.addEffect(new MobEffectInstance(MobEffects.DARKNESS, 120, 0, false, false));
            }

            if (ticks >= 3800) {
               startSearchEvent(player, data);
            } else if (ticks % 100 == 0 && ticks > 3600) {
               int seconds = Math.max(1, (3800 - ticks) / 20);
               player.displayClientMessage(Component.literal("В дубовом лесу что-то просыпается... " + seconds + " с").withStyle(ChatFormatting.DARK_RED), true);
            }
         } else {
            data.putInt("fp_oak_ticks", 0);
         }
      }
   }

   private static void startSearchEvent(ServerPlayer player, CompoundTag data) {
      ServerLevel level = (ServerLevel)player.level();
      data.putBoolean("fp_story_started", true);
      data.putInt("fp_oak_ticks", 0);
      int centerChunkX = player.chunkPosition().x;
      int centerChunkZ = player.chunkPosition().z;
      int minX = (centerChunkX - 2) * 16;
      int maxX = (centerChunkX + 3) * 16 - 1;
      int minZ = (centerChunkZ - 2) * 16;
      int maxZ = (centerChunkZ + 3) * 16 - 1;
      data.putBoolean("fp_event_active", true);
      data.putInt("fp_event_ticks", 0);
      data.putInt("fp_arena_min_x", minX);
      data.putInt("fp_arena_max_x", maxX);
      data.putInt("fp_arena_min_z", minZ);
      data.putInt("fp_arena_max_z", maxZ);
      data.putString("fp_arena_dim", level.dimension().location().toString());
      buildArena(level, player, data, minX, maxX, minZ, maxZ);
      player.gameMode.changeGameModeForPlayer(GameType.ADVENTURE);
      player.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 6000, 1, false, true));
      player.addEffect(new MobEffectInstance(MobEffects.BLINDNESS, 200, 0, false, false));
      level.playSound(null, player.blockPosition(), (SoundEvent)ModSounds.THUNDER.get(), SoundSource.HOSTILE, 1.4F, 0.7F);
      level.playSound(null, player.blockPosition(), (SoundEvent)ModSounds.JUMPSCARE.get(), SoundSource.HOSTILE, 2.0F, 0.55F);
      player.displayClientMessage(
         Component.literal("Фредди Полупокер проснулся. Найдите 6 предметов за 5 минут.")
            .withStyle(new ChatFormatting[]{ChatFormatting.RED, ChatFormatting.BOLD}),
         false
      );
      player.displayClientMessage(Component.literal("Территория поиска: 5 × 5 чанков. В сундуках есть две пустышки.").withStyle(ChatFormatting.GRAY), false);
   }

   private static void buildArena(ServerLevel level, ServerPlayer player, CompoundTag data, int minX, int maxX, int minZ, int maxZ) {
      for (int x = minX; x <= maxX; x++) {
         placeWallColumn(level, x, minZ, data);
         placeWallColumn(level, x, maxZ, data);
      }

      for (int z = minZ + 1; z < maxZ; z++) {
         placeWallColumn(level, minX, z, data);
         placeWallColumn(level, maxX, z, data);
      }

      List<BlockPos> positions = new ArrayList();
      int inset = 8;
      positions.add(surfacePosition(level, minX + inset, minZ + inset));
      positions.add(surfacePosition(level, maxX - inset, minZ + inset));
      positions.add(surfacePosition(level, minX + inset, maxZ - inset));
      positions.add(surfacePosition(level, maxX - inset, maxZ - inset));
      positions.add(surfacePosition(level, (minX + maxX) / 2, minZ + 24));
      positions.add(surfacePosition(level, (minX + maxX) / 2, maxZ - 24));
      positions.add(surfacePosition(level, minX + 24, (minZ + maxZ) / 2));
      positions.add(surfacePosition(level, maxX - 24, (minZ + maxZ) / 2));
      Item[] loot = new Item[]{
         (Item)FreddyPukerMod.FREDDY_CLAWS.get(),
         (Item)FreddyPukerMod.FREDDY_PANTS.get(),
         (Item)FreddyPukerMod.FREDDY_HAT.get(),
         (Item)FreddyPukerMod.FREDDY_BOOTS.get(),
         (Item)FreddyPukerMod.FREDDY_BRAIN.get(),
         Items.ROTTEN_FLESH
      };

      for (int i = positions.size() - 1; i > 0; i--) {
         int swapIndex = level.random.nextInt(i + 1);
         BlockPos swap = (BlockPos)positions.get(i);
         positions.set(i, (BlockPos)positions.get(swapIndex));
         positions.set(swapIndex, swap);
      }

      for (int i = 0; i < positions.size(); i++) {
         BlockPos pos = (BlockPos)positions.get(i);
         level.setBlock(pos, Blocks.CHEST.defaultBlockState(), 3);
         if (level.getBlockEntity(pos) instanceof ChestBlockEntity chest && i < loot.length) {
            chest.setItem(13, new ItemStack(loot[i]));
            chest.setChanged();
         }
      }

      ListTag chestPositions = new ListTag();

      for (BlockPos pos : positions) {
         CompoundTag chestTag = new CompoundTag();
         chestTag.putInt("x", pos.getX());
         chestTag.putInt("y", pos.getY());
         chestTag.putInt("z", pos.getZ());
         chestPositions.add(chestTag);
      }

      data.put("fp_arena_chests", chestPositions);
   }

   private static BlockPos surfacePosition(ServerLevel level, int x, int z) {
      int surfaceY = level.getHeight(Types.MOTION_BLOCKING_NO_LEAVES, x, z);
      surfaceY = Math.max(level.getMinBuildHeight() + 1, Math.min(253, surfaceY));
      return new BlockPos(x, surfaceY, z);
   }

   private static void placeWallColumn(ServerLevel level, int x, int z, CompoundTag data) {
      ListTag snapshot = data.getList("fp_arena_wall_snapshot", 10);

      for (int y = 0; y <= 254; y++) {
         BlockPos pos = new BlockPos(x, y, z);
         CompoundTag original = new CompoundTag();
         original.putInt("x", x);
         original.putInt("y", y);
         original.putInt("z", z);
         original.putInt("state", Block.getId(level.getBlockState(pos)));
         snapshot.add(original);
         level.setBlock(pos, Blocks.BEDROCK.defaultBlockState(), 3);
      }

      data.put("fp_arena_wall_snapshot", snapshot);
   }

   private static void tickSearchEvent(ServerPlayer player, CompoundTag data) {
      ServerLevel level = player.serverLevel();
      int ticks = data.getInt("fp_event_ticks") + 1;
      data.putInt("fp_event_ticks", ticks);
      if (ticks % 40 == 0) {
         player.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 60, 1, false, true));
      }

      if (hasAllSearchItems(player)) {
         removeSearchItems(player);
         removeArena(player, data);
         data.putBoolean("fp_event_active", false);
         data.putBoolean("fp_dream_ready", true);
         player.gameMode.changeGameModeForPlayer(GameType.SURVIVAL);
         player.level().playSound(null, player.blockPosition(), (SoundEvent)SoundEvents.MUSIC_CREATIVE.value(), SoundSource.PLAYERS, 0.8F, 0.8F);
         ((ServerLevel)player.level()).sendParticles(ParticleTypes.HAPPY_VILLAGER, player.getX(), player.getY() + 1.0, player.getZ(), 24, 0.8, 1.0, 0.8, 0.1);
         player.displayClientMessage(Component.literal("Все вещи найдены. Стены исчезли.").withStyle(ChatFormatting.GREEN), false);
      } else {
         int tickInterval = ticks >= 5400 ? 40 : 200;
         if (ticks % tickInterval == 0) {
            level.playSound(
               null, player.blockPosition(), (SoundEvent)SoundEvents.NOTE_BLOCK_PLING.value(), SoundSource.AMBIENT, 0.8F, ticks >= 5800 ? 1.35F : 0.95F
            );
            int seconds = Math.max(0, (6000 - ticks) / 20);
            int bars = Math.max(0, Math.min(20, (6000 - ticks) * 20 / 6000));
            player.displayClientMessage(
               Component.literal("КРОВЬ " + "█".repeat(bars) + "░".repeat(20 - bars) + "  " + seconds + " с").withStyle(ChatFormatting.RED), true
            );
         }

         if (ticks >= 5800 && ticks % 20 == 0) {
            player.displayClientMessage(
               Component.literal(Integer.toString(Math.max(0, (6000 - ticks) / 20))).withStyle(new ChatFormatting[]{ChatFormatting.RED, ChatFormatting.BOLD}),
               true
            );
         }

         if (ticks >= 6000) {
            level.playSound(null, player.blockPosition(), (SoundEvent)ModSounds.JUMPSCARE.get(), SoundSource.HOSTILE, 1.8F, 0.7F);
            data.putBoolean("fp_event_active", false);
            data.putBoolean("fp_chase_active", true);
            data.putInt("fp_chase_ticks", 0);
            player.addEffect(new MobEffectInstance(MobEffects.BLINDNESS, 60, 255));
            player.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 60, 255));
            spawnFreddy(player);
            player.displayClientMessage(
               Component.literal("Ты не успел. Он уже за спиной...").withStyle(new ChatFormatting[]{ChatFormatting.DARK_RED, ChatFormatting.BOLD}), false
            );
         }
      }
   }

   private static boolean hasAllSearchItems(ServerPlayer player) {
      return hasItem(player, (Item)FreddyPukerMod.FREDDY_CLAWS.get())
         && hasItem(player, (Item)FreddyPukerMod.FREDDY_PANTS.get())
         && hasItem(player, (Item)FreddyPukerMod.FREDDY_HAT.get())
         && hasItem(player, (Item)FreddyPukerMod.FREDDY_BOOTS.get())
         && hasItem(player, (Item)FreddyPukerMod.FREDDY_BRAIN.get())
         && hasItem(player, Items.ROTTEN_FLESH);
   }

   private static boolean hasItem(ServerPlayer player, Item item) {
      return player.getInventory().items.stream().anyMatch(stack -> stack.is(item));
   }

   private static void removeSearchItems(ServerPlayer player) {
      List<Item> items = List.of(
         (Item)FreddyPukerMod.FREDDY_CLAWS.get(),
         (Item)FreddyPukerMod.FREDDY_PANTS.get(),
         (Item)FreddyPukerMod.FREDDY_HAT.get(),
         (Item)FreddyPukerMod.FREDDY_BOOTS.get(),
         (Item)FreddyPukerMod.FREDDY_BRAIN.get(),
         Items.ROTTEN_FLESH
      );

      for (int slot = 0; slot < player.getInventory().items.size(); slot++) {
         ItemStack stack = (ItemStack)player.getInventory().items.get(slot);
         if (items.contains(stack.getItem())) {
            player.getInventory().items.set(slot, ItemStack.EMPTY);
         }
      }
   }

   private static void removeArena(ServerPlayer player, CompoundTag data) {
      ServerLevel level = player.serverLevel();
      ListTag snapshot = data.getList("fp_arena_wall_snapshot", 10);
      if (!snapshot.isEmpty()) {
         for (int i = 0; i < snapshot.size(); i++) {
            CompoundTag original = snapshot.getCompound(i);
            BlockPos pos = new BlockPos(original.getInt("x"), original.getInt("y"), original.getInt("z"));
            level.setBlock(pos, Block.stateById(original.getInt("state")), 3);
         }

         data.remove("fp_arena_wall_snapshot");
      } else {
         int minX = data.getInt("fp_arena_min_x");
         int maxX = data.getInt("fp_arena_max_x");
         int minZ = data.getInt("fp_arena_min_z");
         int maxZ = data.getInt("fp_arena_max_z");

         for (int x = minX; x <= maxX; x++) {
            removeWallColumn(level, x, minZ);
            removeWallColumn(level, x, maxZ);
         }

         for (int z = minZ + 1; z < maxZ; z++) {
            removeWallColumn(level, minX, z);
            removeWallColumn(level, maxX, z);
         }
      }

      ListTag chestPositions = data.getList("fp_arena_chests", 10);

      for (int i = 0; i < chestPositions.size(); i++) {
         CompoundTag chestTag = chestPositions.getCompound(i);
         BlockPos pos = new BlockPos(chestTag.getInt("x"), chestTag.getInt("y"), chestTag.getInt("z"));
         if (level.getBlockState(pos).is(Blocks.CHEST)) {
            level.setBlock(pos, Blocks.AIR.defaultBlockState(), 3);
         }
      }

      data.remove("fp_arena_chests");
   }

   private static void removeArenaFreddies(ServerPlayer player, CompoundTag data) {
      ServerLevel level = player.serverLevel();
      int minX = data.getInt("fp_arena_min_x");
      int maxX = data.getInt("fp_arena_max_x");
      int minZ = data.getInt("fp_arena_min_z");
      int maxZ = data.getInt("fp_arena_max_z");
      AABB arena = new AABB(minX, level.getMinBuildHeight(), minZ, maxX + 1.0, level.getMaxBuildHeight(), maxZ + 1.0);

      for (FreddyEntity freddy : level.getEntitiesOfClass(FreddyEntity.class, arena)) {
         freddy.discard();
      }
   }

   private static void removeWallColumn(ServerLevel level, int x, int z) {
      for (int y = 0; y <= 254; y++) {
         if (level.getBlockState(new BlockPos(x, y, z)).is(Blocks.BEDROCK)) {
            level.setBlock(new BlockPos(x, y, z), Blocks.AIR.defaultBlockState(), 3);
         }
      }
   }

   private static void spawnFreddy(ServerPlayer player) {
      ServerLevel level = player.serverLevel();
      Vec3 behind = player.position().subtract(player.getLookAngle().scale(10.0));
      FreddyEntity freddy = (FreddyEntity)((EntityType)FreddyPukerMod.FREDDY.get()).create(level);
      if (freddy != null) {
         freddy.moveTo(behind.x, player.getY(), behind.z, player.getYRot() + 180.0F, 0.0F);
         freddy.setHealth(freddy.getMaxHealth());
         freddy.setCustomName(Component.literal("Фредди Полупокер"));
         freddy.setCustomNameVisible(true);
         freddy.setJumpscare(true);
         freddy.setTarget(player);
         level.addFreshEntity(freddy);
         level.playSound(null, player.blockPosition(), (SoundEvent)ModSounds.FREDDY_SCREAM.get(), SoundSource.HOSTILE, 1.0F, 0.8F);
         level.playSound(null, player.blockPosition(), (SoundEvent)ModSounds.THUNDER.get(), SoundSource.HOSTILE, 1.1F, 0.7F);
      }
   }

   private static void tickFreddyChase(ServerPlayer player, CompoundTag data) {
      ServerLevel level = player.serverLevel();
      int chaseTicks = data.getInt("fp_chase_ticks") + 1;
      data.putInt("fp_chase_ticks", chaseTicks);
      FreddyEntity closest = null;
      double closestDistance = Double.MAX_VALUE;

      for (FreddyEntity freddy : player.serverLevel().getEntitiesOfClass(FreddyEntity.class, player.getBoundingBox().inflate(128.0))) {
         freddy.setTarget(player);
         double distance = freddy.distanceToSqr(player);
         if (distance < closestDistance) {
            closest = freddy;
            closestDistance = distance;
         }
      }

      if (closest != null && chaseTicks % 40 == 0) {
         float distance = (float)Math.sqrt(closestDistance);
         float volume = distance > 30.0F ? 0.2F : (distance < 10.0F ? 1.5F : 0.2F + (30.0F - distance) / 20.0F * 1.3F);
         float pitch = distance < 10.0F ? 0.7F : 1.0F;
         BlockPos source = closest.blockPosition();
         level.playSound(null, source, (SoundEvent)ModSounds.HEAVY_STEPS.get(), SoundSource.HOSTILE, volume * 0.9F, pitch);
         level.playSound(null, source, (SoundEvent)ModSounds.METAL_SCREECH.get(), SoundSource.HOSTILE, volume * 0.8F, pitch);
         level.playSound(null, source, (SoundEvent)ModSounds.HEARTBEAT.get(), SoundSource.HOSTILE, volume * 0.5F, pitch);
         level.playSound(null, source, (SoundEvent)ModSounds.DISC_13_HORROR.get(), SoundSource.HOSTILE, volume, pitch);
      }

      if (closest != null && chaseTicks % 60 == 0) {
         float distance = (float)Math.sqrt(closestDistance);
         float volume = distance > 30.0F ? 0.2F : (distance < 10.0F ? 1.5F : 0.8F);
         level.playSound(null, closest.blockPosition(), (SoundEvent)ModSounds.FREDDY_SCREAM.get(), SoundSource.HOSTILE, volume, distance < 10.0F ? 0.7F : 0.9F);
      }

      if (closest != null
         && chaseTicks % 100 == 0
         && player.serverLevel().getBlockState(player.blockPosition()).isSuffocating(player.serverLevel(), player.blockPosition())) {
         closest.teleportTo(player.getX(), player.getY(), player.getZ());
         level.sendParticles(ParticleTypes.PORTAL, player.getX(), player.getY() + 1.0, player.getZ(), 20, 0.4, 0.8, 0.4, 0.05);
      }

      if (chaseTicks % 40 == 0) {
         player.displayClientMessage(Component.literal("Фредди Полупокер преследует тебя...").withStyle(ChatFormatting.DARK_RED), true);
      }

      if (closest == null && chaseTicks >= 20) {
         player.displayClientMessage(Component.literal("Он исчез. Фредди Полупокер снова появляется за спиной.").withStyle(ChatFormatting.DARK_RED), false);
         spawnFreddy(player);
      } else {
         if (closest != null && closestDistance < 2.75) {
            level.playSound(null, player.blockPosition(), (SoundEvent)ModSounds.JUMPSCARE.get(), SoundSource.HOSTILE, 1.8F, 0.65F);
            player.addEffect(new MobEffectInstance(MobEffects.BLINDNESS, 10, 0, false, false));
            startMaze(player, data);
            closest.discard();
         }
      }
   }

   private static void startMaze(ServerPlayer player, CompoundTag data) {
      ServerLevel level = player.serverLevel();
      int minX = player.blockPosition().getX() + 80;
      int minZ = player.blockPosition().getZ() + 80;
      removeArena(player, data);
      buildMaze(level, minX, minZ);
      data.putBoolean("fp_chase_active", false);
      data.putBoolean("fp_maze_active", true);
      data.putInt("fp_maze_min_x", minX);
      data.putInt("fp_maze_min_z", minZ);
      data.putString("fp_maze_dim", level.dimension().location().toString());
      player.teleportTo(level, minX + 2.5, 64.0, minZ + 2.5, player.getYRot(), player.getXRot());
      level.playSound(null, player.blockPosition(), (SoundEvent)ModSounds.PORTAL_ENTER.get(), SoundSource.HOSTILE, 1.2F, 0.8F);
      player.gameMode.changeGameModeForPlayer(GameType.ADVENTURE);
      WormEntity worm = (WormEntity)((EntityType)FreddyPukerMod.WORM.get()).create(level);
      if (worm != null) {
         worm.moveTo(minX + 8.5, 64.0, minZ + 8.5, 0.0F, 0.0F);
         worm.setHealth(worm.getMaxHealth());
         worm.setTarget(player);
         level.addFreshEntity(worm);
      }

      player.displayClientMessage(Component.literal("Фредди поймал тебя. Найди настоящий портал в лабиринте.").withStyle(ChatFormatting.DARK_RED), false);
   }

   private static void buildMaze(ServerLevel level, int minX, int minZ) {
      for (int x = minX; x < minX + 16; x++) {
         for (int z = minZ; z < minZ + 16; z++) {
            for (int y = 63; y <= 68; y++) {
               boolean border = x == minX || x == minX + 15 || z == minZ || z == minZ + 15;
               if (y == 63) {
                  level.setBlock(new BlockPos(x, y, z), Blocks.DARK_OAK_PLANKS.defaultBlockState(), 3);
               } else if (!border && y != 68) {
                  level.setBlock(new BlockPos(x, y, z), Blocks.AIR.defaultBlockState(), 3);
               } else {
                  level.setBlock(new BlockPos(x, y, z), Blocks.STONE_BRICKS.defaultBlockState(), 3);
               }
            }
         }
      }

      boolean[][] visited = new boolean[7][7];
      carveMaze(level, minX, minZ, 0, 0, visited);

      for (int cx = 0; cx < 7; cx++) {
         for (int cz = 0; cz < 7; cz++) {
            if (level.random.nextFloat() < 0.2F) {
               int x = minX + 1 + cx * 2;
               int z = minZ + 1 + cz * 2;
               level.setBlock(new BlockPos(x, 64, z), Blocks.REDSTONE_TORCH.defaultBlockState(), 3);
            } else if (level.random.nextFloat() < 0.35F) {
               int x = minX + 1 + cx * 2;
               int z = minZ + 1 + cz * 2;
               level.setBlock(new BlockPos(x, 64, z), Blocks.REDSTONE_WIRE.defaultBlockState(), 3);
            }
         }
      }

      buildPortal(level, minX + 2, minZ + 12);
      buildPortal(level, minX + 12, minZ + 2);
   }

   private static void carveMaze(ServerLevel level, int minX, int minZ, int cellX, int cellZ, boolean[][] visited) {
      visited[cellX][cellZ] = true;
      int[][] directions = new int[][]{{1, 0}, {-1, 0}, {0, 1}, {0, -1}};

      for (int i = directions.length - 1; i > 0; i--) {
         int swap = level.random.nextInt(i + 1);
         int[] direction = directions[i];
         directions[i] = directions[swap];
         directions[swap] = direction;
      }

      int centerX = minX + 1 + cellX * 2;
      int centerZ = minZ + 1 + cellZ * 2;
      clearMazeColumn(level, centerX, centerZ);

      for (int[] direction : directions) {
         int nextX = cellX + direction[0];
         int nextZ = cellZ + direction[1];
         if (nextX >= 0 && nextX < 7 && nextZ >= 0 && nextZ < 7 && !visited[nextX][nextZ]) {
            clearMazeColumn(level, centerX + direction[0], centerZ + direction[1]);
            carveMaze(level, minX, minZ, nextX, nextZ, visited);
         }
      }
   }

   private static void clearMazeColumn(ServerLevel level, int x, int z) {
      for (int y = 64; y <= 67; y++) {
         level.setBlock(new BlockPos(x, y, z), Blocks.AIR.defaultBlockState(), 3);
      }
   }

   private static void buildPortal(ServerLevel level, int x, int z) {
      for (int dx = -2; dx <= 2; dx++) {
         for (int dz = -2; dz <= 2; dz++) {
            if (Math.abs(dx) != 2 && Math.abs(dz) != 2) {
               level.setBlock(new BlockPos(x + dx, 64, z + dz), Blocks.PURPLE_STAINED_GLASS.defaultBlockState(), 3);
            } else {
               level.setBlock(new BlockPos(x + dx, 64, z + dz), Blocks.END_PORTAL_FRAME.defaultBlockState(), 3);
            }
         }
      }

      for (int dx = -1; dx <= 1; dx++) {
         for (int dz = -1; dz <= 1; dz++) {
            level.setBlock(new BlockPos(x + dx, 65, z + dz), Blocks.PURPLE_STAINED_GLASS.defaultBlockState(), 3);
         }
      }
   }

   private static void tickMaze(ServerPlayer player, CompoundTag data) {
      ServerLevel level = player.serverLevel();
      int minX = data.getInt("fp_maze_min_x");
      int minZ = data.getInt("fp_maze_min_z");
      BlockPos feet = player.blockPosition();
      BlockPos real = new BlockPos(minX + 2, 64, minZ + 12);
      BlockPos fake = new BlockPos(minX + 12, 64, minZ + 2);
      if (player.distanceToSqr(Vec3.atCenterOf(real)) < 4.0) {
         finishMaze(player, data, false);
      } else if (player.distanceToSqr(Vec3.atCenterOf(fake)) < 4.0) {
         data.putBoolean("fp_maze_active", false);
         data.putBoolean("fp_false_platform", true);
         int platformX = minX + 12;
         int platformZ = minZ + 2;
         buildFalsePlatform(level, player, platformX, platformZ);
         player.teleportTo(level, platformX + 2.5, 246.0, platformZ + 2.5, player.getYRot(), player.getXRot());
         player.displayClientMessage(Component.literal("Это был не настоящий портал. Прыгай вниз.").withStyle(ChatFormatting.DARK_PURPLE), false);
      } else if (feet.getY() < 40) {
         finishMaze(player, data, true);
      }
   }

   private static void tickFalsePlatform(ServerPlayer player, CompoundTag data) {
      if (player.getY() >= 180.0) {
         return;
      }

      if (data.getBoolean("fp_sky_platform_return")) {
         data.putBoolean("fp_sky_platform_return", false);
         data.putBoolean("fp_false_platform", false);
         data.putBoolean("fp_return_to_sky_platform", false);
         data.putBoolean("fp_dream_sent", false);
         data.putBoolean("fp_dream_ready", true);
         enterDream(player, data);
      } else {
         finishMaze(player, data, true);
      }
   }

   private static void buildFalsePlatform(ServerLevel level, ServerPlayer player, int x, int z) {
      for (int dx = 0; dx < 6; dx++) {
         for (int dz = 0; dz < 6; dz++) {
            level.setBlock(new BlockPos(x + dx, 245, z + dz), Blocks.STONE_BRICKS.defaultBlockState(), 3);
         }
      }

      int graveX = x + 2;
      int graveZ = z + 2;
      level.setBlock(new BlockPos(graveX, 246, graveZ), Blocks.SOUL_SOIL.defaultBlockState(), 3);
      BlockPos signPos = new BlockPos(graveX, 247, graveZ);
      level.setBlock(signPos, Blocks.OAK_SIGN.defaultBlockState(), 3);
      String playerName = player.getGameProfile().getName();
      if (level.getBlockEntity(signPos) instanceof SignBlockEntity sign) {
         sign.updateText(
            text -> text.setMessage(0, Component.literal("Здесь лежит"))
               .setMessage(1, Component.literal(playerName + "..."))
               .setMessage(2, Component.literal("Думал, что"))
               .setMessage(3, Component.literal("выживет.")),
            true
         );
         sign.setChanged();
      }

      player.sendSystemMessage(Component.literal("На табличке: " + playerName).withStyle(ChatFormatting.GRAY));
      player.getPersistentData().putInt("fp_false_platform".hashCode() + "_x", x);
      player.getPersistentData().putInt("fp_false_platform".hashCode() + "_z", z);
   }

   private static void finishMaze(ServerPlayer player, CompoundTag data, boolean fromFall) {
      int minX = data.getInt("fp_maze_min_x");
      int minZ = data.getInt("fp_maze_min_z");
      ServerLevel level = player.serverLevel();
      if (data.getBoolean("fp_false_platform")) {
         removeFalsePlatform(level, player);
      }

      for (int x = minX; x < minX + 16; x++) {
         for (int z = minZ; z < minZ + 16; z++) {
            for (int y = 63; y <= 68; y++) {
               level.setBlock(new BlockPos(x, y, z), Blocks.AIR.defaultBlockState(), 3);
            }
         }
      }

      for (WormEntity worm : level.getEntitiesOfClass(WormEntity.class, new AABB(minX, 63.0, minZ, minX + 16, 70.0, minZ + 16))) {
         worm.discard();
      }

      data.putBoolean("fp_maze_active", false);
      data.putBoolean("fp_false_platform", false);
      data.putBoolean("fp_dream_ready", true);
      player.gameMode.changeGameModeForPlayer(GameType.SURVIVAL);
      teleportToSpawn(player);
      player.displayClientMessage(
         Component.literal(fromFall ? "Ты покинул платформу и вернулся на точку спавна." : "Настоящий портал вывел тебя. Когда уснёшь, начнётся следующий акт.")
            .withStyle(ChatFormatting.GREEN),
         false
      );
   }

   private static void removeFalsePlatform(ServerLevel level, ServerPlayer player) {
      String suffix = "fp_false_platform".hashCode() + "_";
      CompoundTag persistent = player.getPersistentData();
      int x = persistent.getInt(suffix + "x");
      int z = persistent.getInt(suffix + "z");

      for (int dx = 0; dx < 6; dx++) {
         for (int dz = 0; dz < 6; dz++) {
            level.setBlock(new BlockPos(x + dx, 245, z + dz), Blocks.AIR.defaultBlockState(), 3);
         }
      }

      level.setBlock(new BlockPos(x + 2, 246, z + 2), Blocks.AIR.defaultBlockState(), 3);
      level.setBlock(new BlockPos(x + 2, 247, z + 2), Blocks.AIR.defaultBlockState(), 3);
   }

   private static void prepareSkyPlatformReturn(ServerPlayer player, CompoundTag data) {
      BlockPos spawn = player.server.overworld().getSharedSpawnPos();
      int offset = Math.floorMod(player.getUUID().hashCode(), 96) + 32;
      data.putBoolean("fp_return_to_sky_platform", true);
      data.putBoolean("fp_return_to_spawn", false);
      data.putInt("fp_sky_platform_x", spawn.getX() + offset);
      data.putInt("fp_sky_platform_z", spawn.getZ() + offset);
   }

   private static void buildSkyDeathPlatform(ServerLevel level, int x, int z, String playerName) {
      for (int dx = 0; dx < 7; dx++) {
         for (int dz = 0; dz < 7; dz++) {
            level.setBlock(new BlockPos(x + dx, 245, z + dz), Blocks.POLISHED_BLACKSTONE.defaultBlockState(), 3);
         }
      }

      for (int dx = 1; dx < 6; dx++) {
         for (int dz = 1; dz < 6; dz++) {
            level.setBlock(new BlockPos(x + dx, 246, z + dz), Blocks.AIR.defaultBlockState(), 3);
         }
      }

      // A small grave and dated nameplate sit safely away from the landing spot.
      BlockPos grave = new BlockPos(x + 1, 246, z + 1);
      level.setBlock(grave, Blocks.POLISHED_BLACKSTONE_BRICKS.defaultBlockState(), 3);
      BlockPos signPos = grave.above();
      level.setBlock(signPos, Blocks.OAK_SIGN.defaultBlockState(), 3);
      if (level.getBlockEntity(signPos) instanceof SignBlockEntity sign) {
         String date = LocalDate.now().toString();
         sign.updateText(
            text -> text.setMessage(0, Component.literal("Погиб от Фредди"))
               .setMessage(1, Component.literal(playerName))
               .setMessage(2, Component.literal(date))
               .setMessage(3, Component.literal("Покойся с миром")),
            true
         );
         sign.setChanged();
      }

   }

   private static void teleportToSpawn(ServerPlayer player) {
      ResourceKey<Level> dimension = player.getRespawnDimension();
      ServerLevel target = player.server.getLevel(dimension);
      BlockPos spawn = player.getRespawnPosition();
      if (target == null) {
         target = player.server.overworld();
      }

      if (spawn == null) {
         spawn = target.getSharedSpawnPos();
      }

      player.teleportTo(target, spawn.getX() + 0.5, spawn.getY() + 1, spawn.getZ() + 0.5, player.getYRot(), player.getXRot());
   }

   private static void enterDream(ServerPlayer player, CompoundTag data) {
      player.stopSleeping();
      ServerLevel dream = player.server.getLevel(FreddyPukerMod.DREAM_LEVEL);
      if (dream == null) {
         player.displayClientMessage(Component.literal("Мир сна не зарегистрирован. Проверь ресурсы мода.").withStyle(ChatFormatting.RED), false);
         return;
      }

      int baseX = Math.floorMod(player.getUUID().hashCode(), 20000) - 10000;
      int baseZ = Math.floorMod(player.getUUID().hashCode() * 31, 20000) - 10000;
      boolean mansion = "mansion".equals(data.getString("fp_sleep_outcome"));
      data.putBoolean("fp_dream_sent", true);
      data.putBoolean("fp_dream_active", mansion);
      data.putInt("fp_dream_x", baseX);
      data.putInt("fp_dream_z", baseZ);
      buildDream(dream, baseX, baseZ, mansion);
      player.teleportTo(dream, baseX + 50.5, 6.0, baseZ + 50.5, 0.0F, 0.0F);
      dream.playSound(null, new BlockPos(baseX + 50, 6, baseZ + 50), (SoundEvent)ModSounds.WIND.get(), SoundSource.AMBIENT, 0.8F, 0.8F);
      dream.playSound(null, new BlockPos(baseX + 50, 6, baseZ + 50), (SoundEvent)ModSounds.CHILD_CRY.get(), SoundSource.AMBIENT, 0.7F, 0.7F);
      if (mansion) {
         player.displayClientMessage(Component.literal("Ты уснул и оказался в огромном особняке.").withStyle(ChatFormatting.DARK_PURPLE), false);
      } else {
         player.displayClientMessage(Component.literal("Ты уснул и оказался на ровной поляне среди симметричных деревьев.").withStyle(ChatFormatting.DARK_PURPLE), false);
         startFinalFreddy(player, data);
      }
   }

   private static void buildDream(ServerLevel level, int baseX, int baseZ, boolean mansion) {
      for (int x = baseX; x < baseX + 100; x++) {
         for (int z = baseZ; z < baseZ + 100; z++) {
            level.setBlock(new BlockPos(x, 1, z), Blocks.GRASS_BLOCK.defaultBlockState(), 3);
            level.setBlock(new BlockPos(x, 0, z), Blocks.BEDROCK.defaultBlockState(), 3);
         }
      }

      if (mansion) {
         buildMansion(level, baseX + 38, baseZ + 40);
         return;
      }

      int[][] trees = new int[][]{{8, 8}, {82, 8}, {8, 82}, {82, 82}, {20, 20}, {70, 20}, {20, 70}, {70, 70}};
      for (int[] tree : trees) {
         buildTree(level, baseX + tree[0], baseZ + tree[1]);
      }
   }

   private static void buildTree(ServerLevel level, int x, int z) {
      for (int y = 2; y < 8; y++) {
         level.setBlock(new BlockPos(x, y, z), Blocks.OAK_LOG.defaultBlockState(), 3);
      }

      for (int dx = -2; dx <= 2; dx++) {
         for (int dz = -2; dz <= 2; dz++) {
            for (int y = 6; y <= 8; y++) {
               if (Math.abs(dx) + Math.abs(dz) <= 3) {
                  level.setBlock(new BlockPos(x + dx, y, z + dz), Blocks.OAK_LEAVES.defaultBlockState(), 3);
               }
            }
         }
      }
   }

   private static void buildMansion(ServerLevel level, int x, int z) {
      for (int dx = 0; dx < 25; dx++) {
         for (int dz = 0; dz < 20; dz++) {
            for (int y = 2; y <= 12; y++) {
               boolean outerWall = dx == 0 || dx == 24 || dz == 0 || dz == 19;
               boolean floor = y == 5 || y == 9;
               boolean basementWall = y <= 4 && outerWall;
               boolean upperWall = y >= 5 && outerWall;
               BlockState state = !floor && !basementWall
                  ? (upperWall ? Blocks.DARK_OAK_PLANKS.defaultBlockState() : Blocks.AIR.defaultBlockState())
                  : Blocks.STONE_BRICKS.defaultBlockState();
               level.setBlock(new BlockPos(x + dx, y, z + dz), state, 3);
            }
         }
      }

      for (int dx = 0; dx < 25; dx++) {
         for (int dz = 0; dz < 20; dz++) {
            level.setBlock(new BlockPos(x + dx, 13, z + dz), Blocks.DARK_OAK_SLAB.defaultBlockState(), 3);
         }
      }

      for (int dx : new int[]{3, 7, 12, 17, 21}) {
         for (int y : new int[]{7, 11}) {
            level.setBlock(new BlockPos(x + dx, y, z), Blocks.GRAY_STAINED_GLASS_PANE.defaultBlockState(), 3);
            level.setBlock(new BlockPos(x + dx, y, z + 19), Blocks.GRAY_STAINED_GLASS_PANE.defaultBlockState(), 3);
         }
      }

      for (int dz : new int[]{4, 9, 14}) {
         for (int y : new int[]{7, 11}) {
            level.setBlock(new BlockPos(x, y, z + dz), Blocks.GRAY_STAINED_GLASS_PANE.defaultBlockState(), 3);
            level.setBlock(new BlockPos(x + 24, y, z + dz), Blocks.GRAY_STAINED_GLASS_PANE.defaultBlockState(), 3);
         }
      }

      for (int y = 5; y <= 6; y++) {
         level.setBlock(new BlockPos(x + 12, y, z), Blocks.AIR.defaultBlockState(), 3);
         level.setBlock(new BlockPos(x + 12, y, z + 19), Blocks.AIR.defaultBlockState(), 3);
      }

      for (int y = 2; y <= 12; y++) {
         level.setBlock(new BlockPos(x + 3, y, z + 3), Blocks.COBWEB.defaultBlockState(), 3);
         level.setBlock(new BlockPos(x + 21, y, z + 16), Blocks.COBWEB.defaultBlockState(), 3);
      }

      for (int[] torch : new int[][]{{3, 6, 3}, {21, 6, 16}, {3, 10, 16}, {21, 10, 3}}) {
         level.setBlock(new BlockPos(x + torch[0], torch[1], z + torch[2]), Blocks.REDSTONE_TORCH.defaultBlockState(), 3);
      }

      for (int y : new int[]{4, 8}) {
         level.setBlock(new BlockPos(x + 12, y, z + 10), Blocks.LADDER.defaultBlockState(), 3);
      }

      BlockPos lecternPos = new BlockPos(x + 4, 3, z + 4);
      level.setBlock(lecternPos, Blocks.LECTERN.defaultBlockState(), 3);
      if (level.getBlockEntity(lecternPos) instanceof LecternBlockEntity lectern) {
         lectern.setBook(createDiary());
         lectern.setChanged();
      }

      spawnEyes(level, x + 3, z + 3);
      spawnEyes(level, x + 12, z + 3);
      spawnEyes(level, x + 3, z + 9);
      spawnEyes(level, x + 12, z + 9);
   }

   private static ItemStack createDiary() {
      ItemStack diary = new ItemStack(Items.WRITABLE_BOOK);
      List<String> pages = List.of(
         "День 1.\nЯ создал этот мир сегодня, чувствую этот день будет интересным.",
         "День 2.\nПостроив свой домик, я пошел разведать местность и нашел собаку. Я ей дал кость и теперь она мой друг.",
         "День 3.\nЯ нашел первые алмазы! Сейчас сделаю себе броню, а то надоели криперы и скелеты.",
         "День 4.\nЯ был в деревне жителей и увидел там существо с длинными конечностями.\nДумаю, мне показалось.",
         "День 5.\nОн идет.\n\nилюха монеси [YBA]"
      );
      diary.set(DataComponents.WRITABLE_BOOK_CONTENT, new WritableBookContent(pages.stream().map(Filterable::passThrough).toList()));
      return diary;
   }

   private static void spawnEyes(ServerLevel level, int x, int z) {
      EyeEntity eye = (EyeEntity)((EntityType)FreddyPukerMod.EYE.get()).create(level);
      if (eye != null) {
         eye.moveTo(x + 0.5, 5.5, z + 0.5, 0.0F, 0.0F);
         level.addFreshEntity(eye);
      }
   }

   private static void tickDream(ServerPlayer player, CompoundTag data) {
      ServerLevel level = player.serverLevel();
      EyeEntity lookedAt = findLookedAtEye(player);
      if (lookedAt != null) {
         int lookTicks = data.getInt("fp_eye_look") + 1;
         data.putInt("fp_eye_look", lookTicks);
         if (lookTicks >= 31 && !data.getBoolean("fp_eye_timer_active")) {
            player.addEffect(new MobEffectInstance(MobEffects.BLINDNESS, 100, 0, false, true));
            player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 100, 0, false, true));
            level.playSound(null, lookedAt.blockPosition(), (SoundEvent)ModSounds.DISC_13_HORROR.get(), SoundSource.HOSTILE, 1.2F, 0.8F);
            // The negative starting value prevents the trigger tick from consuming a second.
            data.putInt("fp_eye_timer", -1);
            data.putBoolean("fp_eye_timer_active", true);
         }
      } else if (!data.getBoolean("fp_eye_timer_active")) {
         data.putInt("fp_eye_look", 0);
      }

      if (data.getBoolean("fp_eye_timer_active")) {
         int timer = data.getInt("fp_eye_timer") + 1;
         data.putInt("fp_eye_timer", timer);
         if (timer >= 100) {
            startFinalFreddy(player, data);
         }
      }
   }

   private static void startFinalFreddy(ServerPlayer player, CompoundTag data) {
      ServerLevel level = player.serverLevel();
      data.putBoolean("fp_dream_active", false);
      data.putBoolean("fp_final_freddy_active", true);
      data.putInt("fp_final_freddy_ticks", 0);
      data.putInt("fp_eye_timer", 0);
      data.putBoolean("fp_eye_timer_active", false);
      data.putInt("fp_eye_look", 0);
      FreddyEntity halfPuker = (FreddyEntity)((EntityType)FreddyPukerMod.FREDDY.get()).create(level);
      if (halfPuker != null) {
         Vec3 behind = player.position().subtract(player.getLookAngle().scale(7.0));
         halfPuker.moveTo(behind.x, player.getY(), behind.z, player.getYRot() + 180.0F, 0.0F);
         halfPuker.setHealth(halfPuker.getMaxHealth());
         halfPuker.setCustomName(Component.literal("Фредди Полупокер"));
         halfPuker.setCustomNameVisible(true);
         halfPuker.setJumpscare(true);
         halfPuker.setTarget(player);
         level.addFreshEntity(halfPuker);
      }

      level.playSound(null, player.blockPosition(), (SoundEvent)ModSounds.FREDDY_SCREAM.get(), SoundSource.HOSTILE, 1.8F, 0.75F);
      player.displayClientMessage(
         Component.literal("Фредди Полупокер пришёл за тобой.").withStyle(new ChatFormatting[]{ChatFormatting.DARK_RED, ChatFormatting.BOLD}), false
      );
   }

   private static void tickFinalFreddy(ServerPlayer player, CompoundTag data) {
      int ticks = data.getInt("fp_final_freddy_ticks") + 1;
      data.putInt("fp_final_freddy_ticks", ticks);
      ServerLevel level = player.serverLevel();
      FreddyEntity closest = null;
      double closestDistance = Double.MAX_VALUE;

      for (FreddyEntity freddy : level.getEntitiesOfClass(FreddyEntity.class, player.getBoundingBox().inflate(40.0))) {
         double distance = freddy.distanceToSqr(player);
         if (distance < closestDistance) {
            closest = freddy;
            closestDistance = distance;
         }
      }

      if (closest != null) {
         closest.setTarget(player);
         if (ticks % 20 == 0) {
            level.playSound(null, player.blockPosition(), (SoundEvent)ModSounds.HEAVY_STEPS.get(), SoundSource.HOSTILE, 2.0F, 0.6F);
         }
      } else if (ticks == 20) {
         player.displayClientMessage(Component.literal("Полупокер не исчез — он уже рядом.").withStyle(ChatFormatting.DARK_RED), false);
         spawnFreddy(player);
      }

      // Keep the chase active until Freddy's own attack kills the player.
      if (closest != null && closestDistance < 3.0) {
         closest.setTarget(player);
      }
   }

   private static EyeEntity findLookedAtEye(ServerPlayer player) {
      Vec3 eyePos = player.getEyePosition();
      Vec3 look = player.getViewVector(1.0F);
      EyeEntity closest = null;
      double closestDistance = 24.0;

      for (EyeEntity eye : player.serverLevel().getEntitiesOfClass(EyeEntity.class, player.getBoundingBox().inflate(24.0))) {
         Vec3 eyeCenter = eye.position().add(0.0, eye.getBbHeight() / 2.0, 0.0);
         Vec3 direction = eyeCenter.subtract(eyePos);
         double distance = direction.length();
         if (distance > 0.0
            && distance < closestDistance
            && look.dot(direction.normalize()) > 0.985
            && player.serverLevel()
                  .clip(new ClipContext(eyePos, eyeCenter, net.minecraft.world.level.ClipContext.Block.COLLIDER, Fluid.NONE, player))
                  .getType()
               == Type.MISS) {
            closest = eye;
            closestDistance = distance;
         }
      }

      return closest;
   }
}

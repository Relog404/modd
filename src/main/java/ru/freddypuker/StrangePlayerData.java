package ru.freddypuker;

import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.saveddata.SavedData;

final class StrangePlayerData extends SavedData {
   long scheduledAt = -1L;
   boolean triggered;
   int messageIndex;
   long nextMessageAt = -1L;

   StrangePlayerData() {
   }

   static StrangePlayerData load(CompoundTag tag, Provider provider) {
      StrangePlayerData data = new StrangePlayerData();
      data.scheduledAt = tag.getLong("scheduled_at");
      data.triggered = tag.getBoolean("triggered");
      data.messageIndex = tag.getInt("message_index");
      data.nextMessageAt = tag.getLong("next_message_at");
      return data;
   }

   public CompoundTag save(CompoundTag tag, Provider provider) {
      tag.putLong("scheduled_at", this.scheduledAt);
      tag.putBoolean("triggered", this.triggered);
      tag.putInt("message_index", this.messageIndex);
      tag.putLong("next_message_at", this.nextMessageAt);
      return tag;
   }
}

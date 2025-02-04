package net.minecraft.world;

import net.minecraft.entity.player.PlayerCapabilities;
import net.minecraft.world.storage.WorldInfo;

public final class WorldSettings {
   private final long seed;
   private final WorldSettings.GameType theGameType;
   private final boolean mapFeaturesEnabled;
   private final boolean hardcoreEnabled;
   private final WorldType terrainType;
   private boolean commandsAllowed;
   private boolean bonusChestEnabled;
   private String worldName = "";

   public WorldSettings(long seedIn, WorldSettings.GameType gameType, boolean enableMapFeatures, boolean hardcoreMode, WorldType worldTypeIn) {
      this.seed = seedIn;
      this.theGameType = gameType;
      this.mapFeaturesEnabled = enableMapFeatures;
      this.hardcoreEnabled = hardcoreMode;
      this.terrainType = worldTypeIn;
   }

   public WorldSettings(WorldInfo info) {
      this(info.getSeed(), info.getGameType(), info.isMapFeaturesEnabled(), info.isHardcoreModeEnabled(), info.getTerrainType());
   }

   public void enableBonusChest() {
      this.bonusChestEnabled = true;
   }

   public void enableCommands() {
      this.commandsAllowed = true;
   }

   public void setWorldName(String name) {
      this.worldName = name;
   }

   public boolean isBonusChestEnabled() {
      return this.bonusChestEnabled;
   }

   public long getSeed() {
      return this.seed;
   }

   public WorldSettings.GameType getGameType() {
      return this.theGameType;
   }

   public boolean getHardcoreEnabled() {
      return this.hardcoreEnabled;
   }

   public boolean isMapFeaturesEnabled() {
      return this.mapFeaturesEnabled;
   }

   public WorldType getTerrainType() {
      return this.terrainType;
   }

   public boolean areCommandsAllowed() {
      return this.commandsAllowed;
   }

   public static WorldSettings.GameType getGameTypeById(int id) {
      return WorldSettings.GameType.getByID(id);
   }

   public String getWorldName() {
      return this.worldName;
   }

   public static enum GameType {
      NOT_SET(-1, ""),
      SURVIVAL(0, "survival"),
      CREATIVE(1, "creative"),
      ADVENTURE(2, "adventure"),
      SPECTATOR(3, "spectator");

      final int id;
      final String name;

      private GameType(int typeId, String nameIn) {
         this.id = typeId;
         this.name = nameIn;
      }

      public int getID() {
         return this.id;
      }

      public String getName() {
         return this.name;
      }

      public void configurePlayerCapabilities(PlayerCapabilities capabilities) {
         if (this == CREATIVE) {
            capabilities.allowFlying = true;
            capabilities.isCreativeMode = true;
            capabilities.disableDamage = true;
         } else if (this == SPECTATOR) {
            capabilities.allowFlying = true;
            capabilities.isCreativeMode = false;
            capabilities.disableDamage = true;
            capabilities.isFlying = true;
         } else {
            capabilities.allowFlying = false;
            capabilities.isCreativeMode = false;
            capabilities.disableDamage = false;
            capabilities.isFlying = false;
         }

         capabilities.allowEdit = !this.isAdventure();
      }

      public boolean isAdventure() {
         return this == ADVENTURE || this == SPECTATOR;
      }

      public boolean isCreative() {
         return this == CREATIVE;
      }

      public boolean isSurvivalOrAdventure() {
         return this == SURVIVAL || this == ADVENTURE;
      }

      public static WorldSettings.GameType getByID(int idIn) {
         for(WorldSettings.GameType worldsettings$gametype : values()) {
            if (worldsettings$gametype.id == idIn) {
               return worldsettings$gametype;
            }
         }

         return SURVIVAL;
      }

      public static WorldSettings.GameType getByName(String p_77142_0_) {
         for(WorldSettings.GameType worldsettings$gametype : values()) {
            if (worldsettings$gametype.name.equals(p_77142_0_)) {
               return worldsettings$gametype;
            }
         }

         return SURVIVAL;
      }
   }
}

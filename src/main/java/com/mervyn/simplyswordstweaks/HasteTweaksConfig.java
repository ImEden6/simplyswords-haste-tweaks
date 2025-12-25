package com.mervyn.simplyswordstweaks;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.loader.api.FabricLoader;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Configuration for customizing Haste effects on SimplySwords weapons.
 * 
 * The config file will be created at: .minecraft/config/simplyswords-haste-tweaks.json
 */
public class HasteTweaksConfig {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final Path CONFIG_PATH = FabricLoader.getInstance().getConfigDir().resolve("simplyswords-haste-tweaks.json");

    // =====================================================
    // OWNER HASTE SETTINGS (when standing near your banner)
    // Applies to both Harbinger and Enigma swords
    // =====================================================
    
    /** Enable/disable Haste effect for the owner standing near their banner */
    public static boolean ownerHasteEnabled = true;
    
    /** 
     * Maximum Haste level for the owner (0 = Haste I, 1 = Haste II, etc.)
     * Original value: 7 (Haste VIII)
     * The effect increments by 1 each tick up to this max level
     */
    public static int ownerHasteMaxLevel = 4;
    
    /** 
     * Duration of owner Haste effect in ticks (20 ticks = 1 second)
     * Original value: 60 ticks (3 seconds)
     */
    public static int ownerHasteDuration = 60;
    
    // =====================================================
    // ALLY HASTE SETTINGS (AOE pulse from Harbinger banner)
    // Only applies to Harbinger sword, NOT Enigma
    // =====================================================
    
    /** Enable/disable Haste effect for allies near Harbinger's banner */
    public static boolean allyHasteEnabled = true;
    
    /** 
     * Haste level for allies in range of Harbinger's banner
     * Original value: 2 (Haste III)
     */
    public static int allyHasteLevel = 4;
    
    /** 
     * Duration of ally Haste effect in ticks
     * Original value: 90 ticks (4.5 seconds)
     */
    public static int allyHasteDuration = 90;

    // =====================================================
    // Config data class for JSON serialization
    // =====================================================
    
    private static class ConfigData {
        boolean ownerHasteEnabled = true;
        int ownerHasteMaxLevel = 4;
        int ownerHasteDuration = 60;
        boolean allyHasteEnabled = true;
        int allyHasteLevel = 4;
        int allyHasteDuration = 90;
    }

    public static void load() {
        if (Files.exists(CONFIG_PATH)) {
            try {
                String json = Files.readString(CONFIG_PATH);
                ConfigData data = GSON.fromJson(json, ConfigData.class);
                
                ownerHasteEnabled = data.ownerHasteEnabled;
                ownerHasteMaxLevel = data.ownerHasteMaxLevel;
                ownerHasteDuration = data.ownerHasteDuration;
                allyHasteEnabled = data.allyHasteEnabled;
                allyHasteLevel = data.allyHasteLevel;
                allyHasteDuration = data.allyHasteDuration;
                
                SimplySwordsHasteTweaks.LOGGER.info("Config loaded from {}", CONFIG_PATH);
            } catch (IOException e) {
                SimplySwordsHasteTweaks.LOGGER.error("Failed to load config, using defaults", e);
                save();
            }
        } else {
            SimplySwordsHasteTweaks.LOGGER.info("Config not found, creating default config at {}", CONFIG_PATH);
            save();
        }
    }

    public static void save() {
        ConfigData data = new ConfigData();
        data.ownerHasteEnabled = ownerHasteEnabled;
        data.ownerHasteMaxLevel = ownerHasteMaxLevel;
        data.ownerHasteDuration = ownerHasteDuration;
        data.allyHasteEnabled = allyHasteEnabled;
        data.allyHasteLevel = allyHasteLevel;
        data.allyHasteDuration = allyHasteDuration;

        try {
            Files.createDirectories(CONFIG_PATH.getParent());
            Files.writeString(CONFIG_PATH, GSON.toJson(data));
            SimplySwordsHasteTweaks.LOGGER.info("Config saved to {}", CONFIG_PATH);
        } catch (IOException e) {
            SimplySwordsHasteTweaks.LOGGER.error("Failed to save config", e);
        }
    }
}

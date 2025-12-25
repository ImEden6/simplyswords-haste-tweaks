package com.mervyn.simplyswordstweaks;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.loader.api.FabricLoader;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Configuration for customizing Haste effects from the Battle Standard (banner entity).
 * Config file: .minecraft/config/simplyswords-haste-tweaks.json
 */
public class HasteTweaksConfig {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final Path CONFIG_PATH = FabricLoader.getInstance().getConfigDir().resolve("simplyswords-haste-tweaks.json");

    /** Enable/disable the Haste effect entirely */
    public static boolean hasteEnabled = true;
    
    /** 
     * Haste level to apply (0 = Haste I, 1 = Haste II, ..., 4 = Haste V)
     * Default: 3 (Haste IV)
     */
    public static int hasteLevel = 3;
    
    /** 
     * Duration of Haste effect in ticks (20 ticks = 1 second)
     * Default: 60 ticks (3 seconds)
     */
    public static int hasteDuration = 60;
    
    /** Enable/disable logging */
    public static boolean loggingEnabled = false;

    private static class ConfigData {
        boolean hasteEnabled = true;
        int hasteLevel = 3;
        int hasteDuration = 60;
        boolean loggingEnabled = false;
    }

    public static void load() {
        if (Files.exists(CONFIG_PATH)) {
            try {
                String json = Files.readString(CONFIG_PATH);
                ConfigData data = GSON.fromJson(json, ConfigData.class);
                
                hasteEnabled = data.hasteEnabled;
                hasteLevel = data.hasteLevel;
                hasteDuration = data.hasteDuration;
                loggingEnabled = data.loggingEnabled;
                
                if (loggingEnabled) {
                    SimplySwordsHasteTweaks.LOGGER.info("Config loaded from {}", CONFIG_PATH);
                }
            } catch (IOException e) {
                SimplySwordsHasteTweaks.LOGGER.error("Failed to load config, using defaults", e);
                save();
            }
        } else {
            save();
        }
    }

    public static void save() {
        ConfigData data = new ConfigData();
        data.hasteEnabled = hasteEnabled;
        data.hasteLevel = hasteLevel;
        data.hasteDuration = hasteDuration;
        data.loggingEnabled = loggingEnabled;

        try {
            Files.createDirectories(CONFIG_PATH.getParent());
            Files.writeString(CONFIG_PATH, GSON.toJson(data));
        } catch (IOException e) {
            SimplySwordsHasteTweaks.LOGGER.error("Failed to save config", e);
        }
    }
}

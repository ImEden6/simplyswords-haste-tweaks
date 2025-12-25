package com.mervyn.simplyswordstweaks;

import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SimplySwordsHasteTweaks implements ModInitializer {
    public static final String MOD_ID = "simplyswords_haste_tweaks";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitialize() {
        // Load config
        HasteTweaksConfig.load();
        
        LOGGER.info("SimplySwords Haste Tweaks loaded!");
        LOGGER.info("Owner Haste Max Level: {}", HasteTweaksConfig.ownerHasteMaxLevel);
        LOGGER.info("Ally Haste Level (Harbinger): {}", HasteTweaksConfig.allyHasteLevel);
        LOGGER.info("Owner Haste Enabled: {}", HasteTweaksConfig.ownerHasteEnabled);
        LOGGER.info("Ally Haste Enabled (Harbinger): {}", HasteTweaksConfig.allyHasteEnabled);
    }
}

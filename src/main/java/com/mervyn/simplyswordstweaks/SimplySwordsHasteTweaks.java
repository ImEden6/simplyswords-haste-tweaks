package com.mervyn.simplyswordstweaks;

import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SimplySwordsHasteTweaks implements ModInitializer {
    public static final String MOD_ID = "simplyswords_haste_tweaks";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitialize() {
        HasteTweaksConfig.load();
        
        if (HasteTweaksConfig.loggingEnabled) {
            LOGGER.info("SimplySwords Haste Tweaks loaded!");
            LOGGER.info("Haste Enabled: {}", HasteTweaksConfig.hasteEnabled);
            LOGGER.info("Haste Level: {}", HasteTweaksConfig.hasteLevel);
            LOGGER.info("Haste Duration: {} ticks", HasteTweaksConfig.hasteDuration);
        }
    }
}

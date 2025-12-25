package com.mervyn.simplyswordstweaks.mixin;

import com.mervyn.simplyswordstweaks.HasteTweaksConfig;
import com.mervyn.simplyswordstweaks.SimplySwordsHasteTweaks;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.sweenus.simplyswords.entity.BattleStandardDarkEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(value = BattleStandardDarkEntity.class, remap = false)
public class BattleStandardDarkMixin {

    /**
     * Redirects the incrementStatusEffect call in baseTick() to cap Haste at
     * configured level.
     * Only affects the BattleStandardDarkEntity, other swords are unaffected.
     */
    @SuppressWarnings("target")
    @Redirect(method = "baseTick", at = @At(value = "INVOKE", target = "Lnet/sweenus/simplyswords/util/HelperMethods;incrementStatusEffect(Lnet/minecraft/class_1309;Lnet/minecraft/class_1291;III)V"))
    private void redirectHasteApplication(LivingEntity entity, StatusEffect effect,
            int duration, int amplifier, int maxAmplifier) {

        // Check if mod is disabled in config
        if (!HasteTweaksConfig.hasteEnabled) {
            if (entity.hasStatusEffect(StatusEffects.HASTE)) {
                entity.removeStatusEffect(StatusEffects.HASTE);
            }
            return; // Do nothing - effect disabled
        }

        int targetLevel = HasteTweaksConfig.hasteLevel;

        // Apply Haste at configured max level directly
        entity.addStatusEffect(new StatusEffectInstance(
                StatusEffects.HASTE,
                HasteTweaksConfig.hasteDuration,
                targetLevel,
                false, false, true));

        if (HasteTweaksConfig.loggingEnabled) {
            SimplySwordsHasteTweaks.LOGGER.info("Applied Battle Standard Haste at level {}", targetLevel);
        }
    }
}

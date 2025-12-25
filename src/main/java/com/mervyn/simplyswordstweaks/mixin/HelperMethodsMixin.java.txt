package com.mervyn.simplyswordstweaks.mixin;

import com.mervyn.simplyswordstweaks.HasteTweaksConfig;
import com.mervyn.simplyswordstweaks.SimplySwordsHasteTweaks;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.sweenus.simplyswords.util.HelperMethods;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = HelperMethods.class, remap = false)
public class HelperMethodsMixin {

    // Using intermediary class names for the method descriptor since SimplySwords
    // is compiled against intermediary
    // class_1309 = LivingEntity, class_1291 = StatusEffect
    @SuppressWarnings("target") // suppressed cus i cba
    @Inject(method = "incrementStatusEffect(Lnet/minecraft/class_1309;Lnet/minecraft/class_1291;III)V", at = @At("HEAD"), cancellable = true, remap = false)
    private static void onIncrementStatusEffect(LivingEntity entity, StatusEffect effect, int duration, int amplifier,
            int maxAmplifier, CallbackInfo ci) {
        // Only target Haste effect
        if (effect == StatusEffects.HASTE) {

            // Check if mod is disabled in config
            if (!HasteTweaksConfig.hasteEnabled) {
                if (entity.hasStatusEffect(StatusEffects.HASTE)) {
                    entity.removeStatusEffect(StatusEffects.HASTE);
                }
                ci.cancel();
                return;
            }

            int targetLevel = HasteTweaksConfig.hasteLevel;

            // Just apply/refresh Haste at configured max level directly
            entity.addStatusEffect(new StatusEffectInstance(
                    StatusEffects.HASTE,
                    HasteTweaksConfig.hasteDuration,
                    targetLevel,
                    false, false, true));

            ci.cancel();

            if (HasteTweaksConfig.loggingEnabled) {
                SimplySwordsHasteTweaks.LOGGER.info("Applied Haste at level {}", targetLevel);
            }
        }
    }
}

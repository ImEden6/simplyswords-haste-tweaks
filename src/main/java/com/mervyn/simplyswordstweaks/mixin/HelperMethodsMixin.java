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

    // Guard against recursion
    private static final ThreadLocal<Boolean> isApplyingHaste = ThreadLocal.withInitial(() -> false);

    // Using intermediary class names for the method descriptor since SimplySwords is compiled against intermediary
    // class_1309 = LivingEntity, class_1291 = StatusEffect
    @Inject(
        method = "incrementStatusEffect(Lnet/minecraft/class_1309;Lnet/minecraft/class_1291;III)V",
        at = @At("HEAD"),
        cancellable = true,
        remap = false
    )
    private static void onIncrementStatusEffect(LivingEntity entity, StatusEffect effect, int duration, int amplifier, int maxAmplifier, CallbackInfo ci) {
        // Recursion guard
        if (isApplyingHaste.get()) {
            return;
        }

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
            StatusEffectInstance currentEffect = entity.getStatusEffect(StatusEffects.HASTE);
            int currentAmplifier = (currentEffect != null) ? currentEffect.getAmplifier() : -1;
            
            try {
                isApplyingHaste.set(true);
                
                if (currentAmplifier < targetLevel) {
                     int nextAmplifier = currentAmplifier + 1;
                     int newAmplifier = Math.min(nextAmplifier, targetLevel);
                     
                     entity.addStatusEffect(new StatusEffectInstance(
                         StatusEffects.HASTE,
                         HasteTweaksConfig.hasteDuration,
                         newAmplifier,
                         false, false, true
                     ));
                } else {
                    // Refresh duration if at target level
                    if (currentAmplifier == targetLevel) {
                         entity.addStatusEffect(new StatusEffectInstance(
                             StatusEffects.HASTE,
                             HasteTweaksConfig.hasteDuration,
                             targetLevel,
                             false, false, true
                         ));
                    }
                    // If > targetLevel, ignore and cancel to prevent incrementing further
                }
                
                ci.cancel();
                if (HasteTweaksConfig.loggingEnabled) {
                    SimplySwordsHasteTweaks.LOGGER.info("Intercepted Haste application. Current: {}, Target: {}", currentAmplifier, targetLevel);
                }

            } finally {
                isApplyingHaste.set(false);
            }
        }
    }
}

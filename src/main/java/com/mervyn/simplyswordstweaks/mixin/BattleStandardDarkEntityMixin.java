package com.mervyn.simplyswordstweaks.mixin;

import com.mervyn.simplyswordstweaks.HasteTweaksConfig;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.sweenus.simplyswords.entity.BattleStandardDarkEntity;
import net.sweenus.simplyswords.util.HelperMethods;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

/**
 * Mixin to customize Haste effects from the BattleStandardDarkEntity
 * (used by both Harbinger and Enigma swords).
 * 
 * Uses @Redirect to REPLACE the original haste calls with our custom values,
 * preventing double-stacking issues.
 */
@Mixin(BattleStandardDarkEntity.class)
public class BattleStandardDarkEntityMixin {

    /**
     * Redirect the owner Haste effect (when owner is within 3 blocks of banner).
     * 
     * Original SimplySwords code:
     * HelperMethods.incrementStatusEffect(ownerEntity, StatusEffects.HASTE, 60, 1, 7);
     * 
     * We intercept this call and either:
     * - Apply with our custom config values (if enabled)
     * - Skip entirely (if disabled)
     * - Pass through unchanged (if not a Haste effect)
     */
    @Redirect(
        method = "baseTick",
        at = @At(
            value = "INVOKE",
            target = "Lnet/sweenus/simplyswords/util/HelperMethods;incrementStatusEffect(Lnet/minecraft/entity/LivingEntity;Lnet/minecraft/entity/effect/StatusEffect;III)V"
        ),
        remap = false  // SimplySwords method, no remapping needed
    )
    private void redirectOwnerHaste(LivingEntity entity, 
                                     StatusEffect effect, 
                                     int duration, 
                                     int amplifierIncrement, 
                                     int maxAmplifier) {
        // Check if this is the Haste effect
        if (effect == StatusEffects.HASTE) {
            if (HasteTweaksConfig.ownerHasteEnabled) {
                // Apply with our custom config values instead
                simplyswords_haste_tweaks$incrementHaste(
                    entity,
                    HasteTweaksConfig.ownerHasteDuration,
                    amplifierIncrement,
                    HasteTweaksConfig.ownerHasteMaxLevel
                );
            }
            // If disabled, do nothing (skip the original call entirely)
        } else {
            // Not Haste - pass through to original method unchanged
            HelperMethods.incrementStatusEffect(entity, effect, duration, amplifierIncrement, maxAmplifier);
        }
    }

    /**
     * Redirect the ally Haste effect (AOE pulse from Harbinger's banner).
     * 
     * Original SimplySwords code:
     * le.addStatusEffect(new StatusEffectInstance(StatusEffects.HASTE, 90, 2), this);
     * 
     * Note: There are multiple addStatusEffect calls in baseTick:
     * - ordinal 0: SLOWNESS to enemies
     * - ordinal 1: HASTE to allies (this is what we target)
     */
    @Redirect(
        method = "baseTick",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/entity/LivingEntity;addStatusEffect(Lnet/minecraft/entity/effect/StatusEffectInstance;Lnet/minecraft/entity/Entity;)Z",
            ordinal = 1  // Target the second addStatusEffect call (ally Haste)
        ),
        remap = true  // Vanilla MC method, needs remapping
    )
    private boolean redirectAllyHaste(LivingEntity entity,
                                       StatusEffectInstance effect,
                                       Entity source) {
        // Check if this is the Haste effect for allies
        if (effect.getEffectType() == StatusEffects.HASTE) {
            if (HasteTweaksConfig.allyHasteEnabled) {
                // Create new effect with our custom config values
                StatusEffectInstance customEffect = new StatusEffectInstance(
                    StatusEffects.HASTE,
                    HasteTweaksConfig.allyHasteDuration,
                    HasteTweaksConfig.allyHasteLevel,
                    false, false, true
                );
                return entity.addStatusEffect(customEffect, source);
            }
            // If disabled, skip applying (return false = effect not applied)
            return false;
        }
        
        // Not Haste - pass through unchanged
        return entity.addStatusEffect(effect, source);
    }

    /**
     * Custom haste incrementer that mimics SimplySwords' HelperMethods.incrementStatusEffect
     * but with our configurable values.
     */
    @Unique
    private void simplyswords_haste_tweaks$incrementHaste(LivingEntity entity, int duration, int amplifierIncrement, int maxLevel) {
        StatusEffectInstance current = entity.getStatusEffect(StatusEffects.HASTE);
        
        if (current != null) {
            int currentDuration = current.getDuration();
            int currentAmplifier = current.getAmplifier();
            
            if (currentAmplifier >= maxLevel) {
                // Already at max level, just refresh duration
                entity.addStatusEffect(new StatusEffectInstance(
                    StatusEffects.HASTE,
                    Math.max(currentDuration, duration),
                    currentAmplifier,
                    false, false, true
                ));
            } else {
                // Increment amplifier up to max
                entity.addStatusEffect(new StatusEffectInstance(
                    StatusEffects.HASTE,
                    Math.max(currentDuration, duration),
                    Math.min(maxLevel, currentAmplifier + amplifierIncrement),
                    false, false, true
                ));
            }
        } else {
            // No existing effect, start at level 0
            entity.addStatusEffect(new StatusEffectInstance(
                StatusEffects.HASTE,
                duration,
                0,
                false, false, true
            ));
        }
    }
}

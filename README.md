# SimplySwords Haste Tweaks

A Fabric mixin that lets you customize the Haste effect levels for **BattleStandardDarkEntity** entity from the [SimplySwords](https://github.com/Sweenus/SimplySwords) mod.

## Features

- **Customize Owner Haste**: When you stand near your banner
- **Customize Ally Haste**: The AOE Haste pulse from Harbinger's banner
- **Enable/Disable Effects**: Completely disable either Haste effect
- **JSON Config**: Easy-to-edit config file

## Requirements

- Minecraft 1.20.1
- Fabric Loader 0.15.0+
- Fabric API
- SimplySwords mod

## Configuration

After first launch, a config file will be created at:
```
.minecraft/config/simplyswords-haste-tweaks.json
```

### Config Options

```json
{
  "ownerHasteEnabled": true,
  "ownerHasteMaxLevel": 4,
  "ownerHasteDuration": 60,
  "allyHasteEnabled": true,
  "allyHasteLevel": 4,
  "allyHasteDuration": 90
}
```

| Option | Description | Default | Notes |
|--------|-------------|---------|-------|
| `ownerHasteEnabled` | Enable Haste for you when near your banner | `true` | Applies to both Harbinger & Enigma |
| `ownerHasteMaxLevel` | Max Haste level (0=I, 1=II, 2=III, etc.) | `4` (Haste V) | Effect ramps up to this level |
| `ownerHasteDuration` | Duration in ticks (20 = 1 second) | `60` (3 seconds) | How long the effect lasts |
| `allyHasteEnabled` | Enable AOE Haste pulse for allies | `true` | **Harbinger only**, not Enigma |
| `allyHasteLevel` | Haste level for allies | `4` (Haste V) | Applied every 80 ticks |
| `allyHasteDuration` | Duration in ticks for allies | `90` (4.5 seconds) | - |

### Example: Disable all Haste effects
```json
{
  "ownerHasteEnabled": false,
  "allyHasteEnabled": false
}
```

### Example: Maximum Haste (Haste X for owner)
```json
{
  "ownerHasteMaxLevel": 9,
  "ownerHasteDuration": 100,
  "allyHasteLevel": 5
}
```

## How It Works

This mod uses [Mixin](https://github.com/SpongePowered/Mixin) to intercept the Haste effect applications in SimplySwords' `BattleStandardDarkEntity` class (the banner entity spawned by both swords).

### Haste Effects in SimplySwords

| Effect | Swords | Original Behavior |
|--------|--------|-------------------|
| **Owner Haste** | Both | Haste I→VIII when within 3 blocks of banner |
| **Ally Haste** | Harbinger only | Haste III to nearby allies every 4 seconds |

## License

MIT License - see [LICENSE](LICENSE)

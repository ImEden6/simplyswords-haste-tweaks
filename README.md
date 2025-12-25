# SimplySwords Haste Tweaks

A Fabric mod that lets you customize the Haste effect from the Battle Standard (banner entity) in [SimplySwords](https://github.com/Sweenus/SimplySwords).

By default, SimplySwords grants Haste up to level VIII. This mod lets you cap it or disable it entirely.

## Requirements

- Minecraft 1.20.1
- Fabric Loader 0.15.0+
- Fabric API
- SimplySwords mod

## Configuration

Config file: `.minecraft/config/simplyswords-haste-tweaks.json`

```json
{
  "hasteEnabled": true,
  "hasteLevel": 3,
  "hasteDuration": 60,
  "loggingEnabled": false
}
```

| Option | Description | Default |
|--------|-------------|---------|
| `hasteEnabled` | Enable/disable Haste effect from banner | `true` |
| `hasteLevel` | Max Haste level (0=I, 1=II, 2=III, 3=IV) | `3` (Haste IV) |
| `hasteDuration` | Duration in ticks (20 = 1 second) | `60` (3 seconds) |
| `loggingEnabled` | Enable debug logging | `false` |

## Examples

**Disable Haste entirely:**
```json
{ "hasteEnabled": false }
```

**Cap at Haste II:**
```json
{ "hasteLevel": 1 }
```

**Haste X with 5 second duration:**
```json
{ "hasteLevel": 9, "hasteDuration": 100 }
```

## How It Works

This mod uses a Mixin to intercept `HelperMethods.incrementStatusEffect()` in SimplySwords. When Haste is being applied, the mod checks the configured max level and prevents it from exceeding that value.

## Building

```bash
./gradlew build
```

Output: `build/libs/simplyswords-haste-tweaks-1.0.0.jar`

## License

MIT License

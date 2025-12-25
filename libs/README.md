# SimplySwords Haste Tweaks - Local JAR Setup

To build this mod, you need to place the SimplySwords jar file in the `libs/` folder.

## Quick Setup

1. Copy your SimplySwords jar file to this folder
2. Rename it to `simplyswords.jar`

## Where to find the jar

**Option A: From your mods folder**
```
.minecraft/mods/simplyswords-fabric-1.56.0-1.20.1.jar
```

**Option B: Download from Modrinth**
https://modrinth.com/mod/simply-swords/version/1.56.0-1.20.1-fabric

## After placing the jar

Run from the project root:
```bash
gradle build
```

The output will be in `build/libs/simplyswords-haste-tweaks-1.0.0.jar`

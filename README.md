# Actually Additions: Tiers

Iron, Gold, Diamond, and Netherite Display Stand variants for [Actually Additions](https://www.curseforge.com/minecraft/mc-mods/actually-additions). Larger energy buffers, faster input rates, drop-in compatible with the Empowerer. Pack-dev-configurable per tier.

**Minecraft 1.21.1 · NeoForge · Java 21 · Requires Actually Additions 1.3.x+**

## Why

Stock Display Stands cap at 80,000 FE and accept 1,000 FE/tick of input. Late-game empowering recipes routinely demand more, and the Empowerer stalls waiting for cables to refill the stands. These tiered stands raise the ceiling.

## Tiers

| Tier | Buffer (FE) | Input rate (FE/tick) |
|---|---|---|
| Iron | 400,000 | 5,000 |
| Gold | 1,000,000 | 15,000 |
| Diamond | 4,000,000 | 40,000 |
| Netherite | 8,000,000 | 100,000 |

## Recipes

8 of the tier material around the previous-tier stand. Iron is crafted from a vanilla Display Stand; each subsequent tier upgrades from the one below. No smithing table.

## Configuration

Server config: `config/aatiers-server.toml`. Each tier exposes `capacity` (FE) and `maxInput` (FE/tick). Reload the world after editing.

## Credits

All the underlying mechanics for the Display Stand block entity, Empowerer recipe system, energy storage, renderer all come from Actually Additions. This mod just adds the higher-capacity block variants.

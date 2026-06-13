# TrimEditor

A lightweight [Paper](https://papermc.io/) plugin for editing **armor trims** on
the item in your hand. Run a command, pick a pattern, pick a material, confirm.
No anvils, no smithing-table grind, no NMS.

```
/te        →   [ pick pattern ]  →   [ pick material ]  →   [ confirm ]
```

## Why TrimEditor

- **Held-item editing.** The editor opens only when you hold trimmable armor and
  edits the real item in place — it never rebuilds the armor from its material,
  so enchantments, name, lore, durability and custom data are preserved.
- **Safe by design.** The held item is fingerprinted when the editor opens and
  re-checked on apply. If it changed in the meantime, the edit is cancelled and
  nothing is consumed.
- **Free by default, flexible when you need it.** Template requirements,
  material requirements, and Vault economy are off out of the box. Costs are
  only taken on a successful apply.
- **Quiet, sharp UI.** Compact pattern/material pickers, per-action sounds, and
  a final preview before apply.

## Compatibility

| | |
|---|---|
| Server | Paper / Paper fork / Folia, **1.21+** |
| Java | 21 |
| Economy | optional, via [Vault](https://www.spigotmc.org/resources/vault.34315/) |

## Quick start

1. [Install the plugin](installation.md).
2. Hold a piece of trimmable armor.
3. Run `/te` (or `/trimeditor`).

That's it — the editor is usable with zero configuration. When you want costs or
permission gating, head to [Configuration](configuration.md) and
[Permissions](permissions.md).

!!! note "Project status"
    TrimEditor is in early development (`0.x`). The held-item flow is the first
    supported mode; the session model is built so later modes can be added
    without changing it.

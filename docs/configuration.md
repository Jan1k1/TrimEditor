# Configuration

TrimEditor generates `plugins/TrimEditor/config.yml` on first run. Reload changes
in-game with [`/te reload`](commands.md#te-reload).

## `config-version`

```yaml
config-version: 1
```

Tracks the config schema. **Do not edit this by hand.** On startup TrimEditor
compares it against the current version and runs [migration](#migration) if they
differ.

## `requirements`

```yaml
requirements:
  enabled: false
```

When `enabled` is `false` (the default), editing trims is free and every pattern
and material is offered.

When `true`:

- `/te` only opens if the player owns at least one usable trim **template** and
  one usable trim **material**; otherwise they get a message instead of a
  dead-end GUI.
- The pattern and material pickers are filtered to options the player both has
  [permission](permissions.md) for and owns.
- Costs are validated and consumed **only on a successful apply** — after the
  held item's [fingerprint](#safety) passes. Players with
  `trimeditor.bypass.cost` skip this entirely.

## `gui`

```yaml
gui:
  cooldown-ms: 300
```

`cooldown-ms` is the minimum delay between accepted button clicks, per player, in
milliseconds. It guards against double-clicks and click-spam. Set it to `0` to
disable the cooldown.

## `sounds`

Each editor action plays a configurable sound:

```yaml
sounds:
  open:
    enabled: true
    sound: minecraft:block.smithing_table.use
    volume: 0.75
    pitch: 1.15
```

| Field | Meaning |
|-------|---------|
| `enabled` | Whether to play this sound at all |
| `sound` | A namespaced sound key (`minecraft:…` or a resource-pack key) |
| `volume` | `0.0` and up; also controls how far the sound carries |
| `pitch` | `0.5` (low) to `2.0` (high) |

Available actions: `open`, `select-pattern`, `select-material`, `apply`, `page`,
`close`, `deny`.

## Language

Player-facing text lives in a separate language file using
[MiniMessage](https://docs.advntr.dev/minimessage/format.html) formatting. The
`prefix` is prepended to admin messages; normal player messages are sent without
a prefix. Edit the strings to retheme or translate, then `/te reload`.

## Migration

On every startup TrimEditor:

1. reads your `config.yml`,
2. adds any keys that are missing from your file (new options in an update),
   leaving all existing values untouched,
3. bumps `config-version` to the current schema,
4. saves the file only if something actually changed.

This means upgrading the jar never overwrites your settings — you simply gain the
new defaults for keys you did not have yet.

## Safety

Independent of configuration, every apply is guarded by an item fingerprint. When
the editor opens, TrimEditor records a fingerprint of the held armor that
includes its type, current trim, enchantments, name, lore and custom data (but
ignores durability). On apply it re-checks the held item against that
fingerprint; if the item is gone or has changed, the edit is cancelled and
nothing — items or money — is consumed.

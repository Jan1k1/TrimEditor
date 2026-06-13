# Commands

TrimEditor registers a single command with one alias.

| Command | Alias | Permission | Description |
|---------|-------|------------|-------------|
| `/trimeditor` | `/te` | `trimeditor.use` | Open the trim editor for the armor in your hand |
| `/trimeditor reload` | `/te reload` | `trimeditor.reload` | Reload config and language files |

## `/te`

Opens the editor for the item in your main hand.

- You must be a player (the base command cannot be run from console).
- You must hold a piece of trimmable armor; otherwise the command reports that
  you need to hold armor and does **not** open an empty GUI.
- When [requirements](configuration.md#requirements) are enabled, the editor only
  opens if you own at least one usable template and one usable material.

The editor then walks through three screens:

1. **Pattern** — choose the trim pattern (paged).
2. **Material** — choose the trim material (paged).
3. **Confirm** — apply, or go back.

Only normal left/right clicks are accepted inside the editor. Shift-click, quick
move, number keys, drag, and drop actions are ignored, and button clicks honour
the configurable [cooldown](configuration.md#gui).

## `/te reload`

Reloads `config.yml` and the language file without restarting the server. It:

- closes any open editor sessions,
- re-reads configuration and cached values (including the click cooldown),
- reports how long the reload took.

Requires `trimeditor.reload` (granted to operators by default). `reload` is also
offered as a tab-completion for players who hold that permission.

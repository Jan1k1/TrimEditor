# Permissions

TrimEditor ships sensible defaults: anyone can use the editor, and admin actions
are restricted to operators. You only need a permissions plugin if you want to
change that.

## Nodes

| Node | Default | Description |
|------|---------|-------------|
| `trimeditor.use` | `true` (everyone) | Open the editor with `/te` |
| `trimeditor.reload` | `op` | Run `/te reload` |
| `trimeditor.admin` | `op` | Parent node; includes `trimeditor.reload` |
| `trimeditor.bypass.cost` | `op` | Skip requirement and economy costs on apply |
| `trimeditor.pattern.<key>` | `true` (everyone) | Use a specific trim pattern |
| `trimeditor.material.<key>` | `true` (everyone) | Use a specific trim material |

## Per-pattern and per-material access

`trimeditor.pattern.<key>` and `trimeditor.material.<key>` gate individual trim
options, where `<key>` is the trim's key (for example `trimeditor.pattern.sentry`
or `trimeditor.material.netherite`).

When a player lacks the node for an option, that option is **hidden** from the
picker rather than shown as a locked placeholder — no dead clicks, no clutter.

Both families default to `true`, so trims are fully available until you start
revoking them. To run an allowlist instead, revoke the wildcard and grant only
what you want:

```yaml
# Example with LuckPerms
permissions:
  - trimeditor.pattern.*: false
  - trimeditor.material.*: false
  - trimeditor.pattern.sentry: true
  - trimeditor.material.diamond: true
```

## Bypassing costs

`trimeditor.bypass.cost` lets a player apply trims without meeting
[requirements](configuration.md#requirements) or paying the economy cost. It is
op-only by default — useful for staff and creative builders.

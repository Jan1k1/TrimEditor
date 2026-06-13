# TrimEditor

A lightweight Paper plugin for editing **armor trims** on the item in your hand.
Run a command, pick a pattern, pick a material, confirm. No anvils, no smithing
table grind, no NMS.

[![Build](https://github.com/Jan1k1/TrimEditor/actions/workflows/ci.yml/badge.svg)](https://github.com/Jan1k1/TrimEditor/actions/workflows/ci.yml)
[![Docs](https://github.com/Jan1k1/TrimEditor/actions/workflows/docs.yml/badge.svg)](https://jan1k1.github.io/TrimEditor/)
[![Paper](https://img.shields.io/badge/Paper-1.21+-orange)](https://papermc.io/)
[![License](https://img.shields.io/badge/license-MIT-blue)](LICENSE)

> **Docs:** https://jan1k1.github.io/TrimEditor/

## Features

- **Held-item editing** — opens only when you hold trimmable armor; edits the
  real item in place and never rebuilds it from scratch.
- **Three-step flow** — pattern → material → confirm, with a live preview and a
  back button at every step.
- **Safe by design** — the held item is fingerprinted on open and re-checked on
  apply; if it changed, the edit is cancelled and nothing is spent.
- **Optional requirements** — off by default. When enabled, players must own a
  usable trim template and material, and the lists are filtered per permission
  and inventory.
- **Optional economy** — soft-depends on [Vault](https://www.spigotmc.org/resources/vault.34315/);
  costs are charged only on a successful apply.
- **Configurable sounds** and reloadable config/language files.
- **Folia-aware** scheduling.

## Requirements

| | |
|---|---|
| Server | Paper (or a Paper fork / Folia) **1.21+** |
| Java | 21 |
| Optional | Vault + an economy plugin, for costs |

## Installation

1. Download `TrimEditor-<version>.jar` from the [releases page](https://github.com/Jan1k1/TrimEditor/releases).
2. Drop it into your server's `plugins/` folder.
3. Restart the server. A default `config.yml` is generated on first run.

See [Installation](https://jan1k1.github.io/TrimEditor/installation/) for details.

## Usage

Hold a piece of trimmable armor and run:

```
/trimeditor      # alias: /te
/te reload       # reload config + language (requires trimeditor.reload)
```

## Permissions

| Node | Default | Description |
|------|---------|-------------|
| `trimeditor.use` | everyone | Open the editor |
| `trimeditor.reload` | op | Reload config and language |
| `trimeditor.admin` | op | All admin nodes |
| `trimeditor.bypass.cost` | op | Skip requirements and economy costs |
| `trimeditor.pattern.<key>` | everyone | Use a specific trim pattern |
| `trimeditor.material.<key>` | everyone | Use a specific trim material |

Full reference: [Permissions](https://jan1k1.github.io/TrimEditor/permissions/).

## Building from source

```bash
git clone https://github.com/Jan1k1/TrimEditor.git
cd TrimEditor
./gradlew build        # runs tests and produces build/libs/TrimEditor-<version>.jar
./gradlew runServer    # boots a local Paper 1.21 test server with the plugin
```

## License

[MIT](LICENSE)

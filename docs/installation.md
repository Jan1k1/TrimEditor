# Installation

## Requirements

- A **Paper 1.21+** server (or a Paper fork / Folia).
- **Java 21** or newer.
- *Optional:* [Vault](https://www.spigotmc.org/resources/vault.34315/) and an
  economy plugin if you want trims to cost money.

## Steps

1. Download `TrimEditor-<version>.jar` from
   [Spigot](https://www.spigotmc.org/resources/trimeditor-modern-armor-trim-gui-paper-1-21.136146/),
   the [releases page](https://github.com/Jan1k1/TrimEditor/releases), or
   [build it from source](building.md).
2. Place the jar in your server's `plugins/` directory.
3. *(Optional)* Install Vault and an economy provider for costs.
4. Start or restart the server.

On first start TrimEditor writes a default `config.yml` and an English language
file. No further setup is required — the editor works immediately.

## Verifying

After the server boots:

- Run `/te` while holding a piece of trimmable armor — the editor should open.
- Run `/te reload` as an operator — you should see a reload confirmation.

If `/te` reports that you must hold armor, you are either holding a non-armor
item or a piece that cannot be trimmed (for example leather armor variants
without a trimmable model).

## Updating

Replace the old jar with the new one and restart. On startup TrimEditor migrates
your `config.yml`, adding any new keys while leaving your existing values
untouched. See [Configuration](configuration.md#migration) for how migration
works.

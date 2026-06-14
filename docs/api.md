# API

TrimEditor registers a Bukkit service when the plugin enables. Other plugins can
use it to open the held-item editor or reuse the trim item logic without touching
internal classes.

## What KiteEditor should use

Use the API when KiteEditor wants one of these:

- open the normal TrimEditor GUI for the item the player is holding
- check whether an item can receive armor trims
- clone an item with a trim already applied
- build a custom picker from TrimEditor's filtered pattern/material lists

Do not call TrimEditor's command executor or GUI classes directly. They are
internal and can change.

## Plugin setup

Add TrimEditor as a soft dependency in your plugin:

```yaml
softdepend:
  - TrimEditor
```

Compile against the TrimEditor jar. Until releases publish an artifact, keep a
copy in your plugin's `libs/` folder:

```kotlin
dependencies {
    compileOnly(files("libs/TrimEditor-0.1.1.jar"))
}
```

If TrimEditor is missing at runtime, your plugin should keep working and simply
hide the trim feature.

## Open the editor

```kotlin
import org.jan1k.trimeditor.api.TrimEditorOpenResult
import org.jan1k.trimeditor.api.TrimEditorProvider

val api = TrimEditorProvider.get() ?: return
if (api.apiVersion() != 1) return

when (api.openEditor(player)) {
    TrimEditorOpenResult.OPENED -> Unit
    TrimEditorOpenResult.NO_PERMISSION -> player.sendMessage("No permission.")
    TrimEditorOpenResult.NOT_ARMOR -> player.sendMessage("Hold armor.")
    TrimEditorOpenResult.MISSING_REQUIREMENTS -> player.sendMessage("Missing trim items.")
}
```

Use `openEditor(player, false)` if your plugin already handled permission checks.

## KiteEditor-style command

```kotlin
fun openTrimEditor(player: Player) {
    val api = TrimEditorProvider.get()
    if (api == null) {
        player.sendMessage("TrimEditor is not installed.")
        return
    }

    when (api.openEditor(player)) {
        TrimEditorOpenResult.OPENED -> return
        TrimEditorOpenResult.NO_PERMISSION -> player.sendMessage("No permission.")
        TrimEditorOpenResult.NOT_ARMOR -> player.sendMessage("Hold armor.")
        TrimEditorOpenResult.MISSING_REQUIREMENTS -> player.sendMessage("Missing template or material.")
    }
}
```

## Edit an item yourself

```kotlin
import org.bukkit.inventory.meta.trim.TrimMaterial
import org.bukkit.inventory.meta.trim.TrimPattern

val edited = api.withTrim(item, TrimPattern.SENTRY, TrimMaterial.DIAMOND)
if (edited != null) {
    player.inventory.setItem(slot, edited)
}
```

`withTrim` returns a clone and keeps item type, amount, durability, enchantments,
name, lore, and custom data. It returns `null` for non-armor items.

## Build your own picker

```kotlin
val patterns = api.visiblePatterns(player)
val materials = api.visibleMaterials(player)
```

`visiblePatterns` and `visibleMaterials` apply TrimEditor's permission and
requirement filters. Use `patterns()` and `materials()` when you need the full
vanilla catalog.

## Result rules

`openEditor` does not send messages. It returns a result so your plugin can use
its own wording and style.

`withTrim` does not edit the original item. It returns a cloned item with the trim
applied, or `null` if the item is not trimmable armor.

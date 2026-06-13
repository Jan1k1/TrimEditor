# API

TrimEditor registers a Bukkit service when the plugin enables. Other plugins can
use it to open the held-item editor or reuse the trim item logic without touching
internal classes.

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
    compileOnly(files("libs/TrimEditor-0.1.0.jar"))
}
```

## Open the editor

```kotlin
import org.jan1k.plugin.trimeditor.api.TrimEditorOpenResult
import org.jan1k.plugin.trimeditor.api.TrimEditorProvider

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

## Edit an item yourself

```kotlin
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

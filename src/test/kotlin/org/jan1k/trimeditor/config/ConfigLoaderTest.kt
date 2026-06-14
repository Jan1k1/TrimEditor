package org.jan1k.trimeditor.config

import org.bukkit.configuration.file.YamlConfiguration
import org.jan1k.trimeditor.lang.Lang
import java.nio.file.Files
import kotlin.io.path.createTempDirectory
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class ConfigLoaderTest {
    @Test
    fun `defaults disable requirements and use 300 ms gui cooldown`() {
        val path = createTempDirectory("trimeditor-config").resolve("config.yml")

        val result = ConfigLoader().load(path)

        assertEquals(ConfigLoader.CURRENT_VERSION, result.config.configVersion)
        assertFalse(result.config.requirements.enabled)
        assertFalse(result.config.requirements.requireTemplate)
        assertFalse(result.config.requirements.requireMaterial)
        assertTrue(result.config.requirements.consumeTemplate)
        assertTrue(result.config.requirements.consumeMaterial)
        assertFalse(result.config.economy.enabled)
        assertEquals(0.0, result.config.economy.cost)
        assertEquals(300, result.config.gui.cooldownMillis)
        assertTrue(Files.exists(path))
    }

    @Test
    fun `generated config includes help links and setting notes`() {
        val path = createTempDirectory("trimeditor-config").resolve("config.yml")

        ConfigLoader().load(path)
        val generated = Files.readString(path)

        assertTrue(generated.contains("https://jan1k1.github.io/TrimEditor/"))
        assertTrue(generated.contains("https://github.com/Jan1k1/TrimEditor/issues"))
        assertTrue(generated.contains("Default setup is free editing."))
        assertTrue(generated.contains("Do not change config-version by hand."))
    }

    @Test
    fun `migration adds missing keys without replacing existing values`() {
        val path = createTempDirectory("trimeditor-config").resolve("config.yml")
        Files.writeString(
            path,
            """
            config-version: 0
            requirements:
              enabled: true
              require-template: true
              require-material: false
              consume-template: false
              consume-material: true
            economy:
              enabled: true
              cost: 12.5
            gui:
              cooldown-ms: 125
            sounds:
              apply:
                enabled: false
                sound: minecraft:block.anvil.land
                volume: 0.35
                pitch: 0.8
            custom:
              keep: yes
            """.trimIndent(),
        )

        val result = ConfigLoader().load(path)
        val migrated = YamlConfiguration.loadConfiguration(path.toFile())
        val apply = result.config.sounds.getValue(SoundAction.APPLY)

        assertTrue(result.migrated)
        assertEquals(ConfigLoader.CURRENT_VERSION, migrated.getInt("config-version"))
        assertTrue(result.config.requirements.enabled)
        assertTrue(result.config.requirements.requireTemplate)
        assertFalse(result.config.requirements.requireMaterial)
        assertFalse(result.config.requirements.consumeTemplate)
        assertTrue(result.config.requirements.consumeMaterial)
        assertTrue(result.config.economy.enabled)
        assertEquals(12.5, result.config.economy.cost)
        assertEquals(125, result.config.gui.cooldownMillis)
        assertFalse(apply.enabled)
        assertEquals("minecraft:block.anvil.land", apply.sound)
        assertEquals(0.35f, apply.volume)
        assertEquals(0.8f, apply.pitch)
        assertEquals(true, migrated.getBoolean("custom.keep"))
        assertTrue(migrated.contains("sounds.open.sound"))
    }

    @Test
    fun `sounds parse per action`() {
        val path = createTempDirectory("trimeditor-config").resolve("config.yml")
        Files.writeString(
            path,
            """
            config-version: 1
            requirements:
              enabled: false
            gui:
              cooldown-ms: 300
            sounds:
              select-material:
                enabled: true
                sound: minecraft:item.armor.equip_netherite
                volume: 0.65
                pitch: 1.4
            """.trimIndent(),
        )

        val result = ConfigLoader().load(path)
        val sound = result.config.sounds.getValue(SoundAction.SELECT_MATERIAL)

        assertTrue(sound.enabled)
        assertEquals("minecraft:item.armor.equip_netherite", sound.sound)
        assertEquals(0.65f, sound.volume)
        assertEquals(1.4f, sound.pitch)
        assertTrue(SoundAction.entries.all { result.config.sounds.containsKey(it) })
    }

    @Test
    fun `english keeps prefix to admin reload messages`() {
        val lang = Lang.load("en")

        assertTrue(lang.message("admin.reload").startsWith(lang.prefix))
        assertTrue(lang.message("admin.reload-failed").startsWith(lang.prefix))
        assertFalse(lang.message("errors.no-permission").startsWith(lang.prefix))
        assertFalse(lang.message("errors.not-armor").startsWith(lang.prefix))
    }
}

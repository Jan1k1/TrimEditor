package org.jan1k.plugin.trimeditor.config

import org.bukkit.configuration.ConfigurationSection
import org.bukkit.configuration.file.YamlConfiguration
import java.io.InputStreamReader
import java.nio.charset.StandardCharsets
import java.nio.file.Files
import java.nio.file.Path

class ConfigLoader(
    private val defaults: YamlConfiguration = defaultConfig(),
) {
    fun load(path: Path): ConfigLoadResult {
        Files.createDirectories(path.parent)

        if (Files.notExists(path)) {
            defaults.save(path.toFile())
        }

        val yaml = YamlConfiguration.loadConfiguration(path.toFile())
        var migrated = yaml.getInt("config-version") != CURRENT_VERSION

        if (migrated) {
            yaml.set("config-version", CURRENT_VERSION)
        }

        migrated = addMissingKeys(yaml) || migrated

        if (migrated) {
            yaml.save(path.toFile())
        }

        return ConfigLoadResult(parse(yaml), migrated)
    }

    private fun addMissingKeys(yaml: YamlConfiguration): Boolean {
        var migrated = false

        for (key in defaults.getKeys(true)) {
            if (key == "config-version" || yaml.contains(key)) {
                continue
            }

            val value = defaults.get(key)
            if (value is ConfigurationSection) {
                yaml.createSection(key)
            } else {
                yaml.set(key, value)
            }
            migrated = true
        }

        return migrated
    }

    private fun parse(yaml: YamlConfiguration): PluginConfig {
        return PluginConfig(
            configVersion = yaml.getInt("config-version", CURRENT_VERSION),
            requirements = RequirementsConfig(
                enabled = yaml.getBoolean("requirements.enabled", false),
                requireTemplate = yaml.getBoolean("requirements.require-template", false),
                requireMaterial = yaml.getBoolean("requirements.require-material", false),
                consumeTemplate = yaml.getBoolean("requirements.consume-template", true),
                consumeMaterial = yaml.getBoolean("requirements.consume-material", true),
            ),
            economy = EconomyConfig(
                enabled = yaml.getBoolean("economy.enabled", false),
                cost = yaml.getDouble("economy.cost", 0.0),
            ),
            gui = GuiConfig(
                cooldownMillis = yaml.getLong("gui.cooldown-ms", 300),
            ),
            sounds = SoundAction.entries.associateWith { action -> parseSound(yaml, action) },
        )
    }

    private fun parseSound(yaml: YamlConfiguration, action: SoundAction): SoundConfig {
        val base = "sounds.${action.key}"

        return SoundConfig(
            enabled = yaml.getBoolean("$base.enabled", defaults.getBoolean("$base.enabled", true)),
            sound = yaml.getString("$base.sound") ?: defaults.getString("$base.sound").orEmpty(),
            volume = yaml.getDouble("$base.volume", defaults.getDouble("$base.volume", 1.0)).toFloat(),
            pitch = yaml.getDouble("$base.pitch", defaults.getDouble("$base.pitch", 1.0)).toFloat(),
        )
    }

    companion object {
        const val CURRENT_VERSION = 2

        fun defaultConfig(): YamlConfiguration {
            val stream = ConfigLoader::class.java.classLoader.getResourceAsStream("config.yml")
                ?: error("Missing config.yml resource")
            return stream.use {
                YamlConfiguration.loadConfiguration(InputStreamReader(it, StandardCharsets.UTF_8))
            }
        }
    }
}

data class ConfigLoadResult(
    val config: PluginConfig,
    val migrated: Boolean,
)

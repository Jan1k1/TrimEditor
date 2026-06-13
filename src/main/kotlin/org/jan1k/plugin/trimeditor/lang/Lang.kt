package org.jan1k.plugin.trimeditor.lang

import org.bukkit.configuration.ConfigurationSection
import org.bukkit.configuration.file.YamlConfiguration
import java.io.InputStreamReader
import java.nio.charset.StandardCharsets

data class Lang(
    val locale: String,
    val prefix: String,
    private val messages: Map<String, String>,
) {
    fun message(path: String): String {
        return messages[path]?.replace("<prefix>", prefix) ?: path
    }

    companion object {
        fun load(locale: String = "en"): Lang {
            val resource = "lang/$locale.yml"
            val stream = Lang::class.java.classLoader.getResourceAsStream(resource)
                ?: Lang::class.java.classLoader.getResourceAsStream("lang/en.yml")
                ?: error("Missing lang/en.yml resource")

            val yaml = stream.use {
                YamlConfiguration.loadConfiguration(InputStreamReader(it, StandardCharsets.UTF_8))
            }
            val root = yaml.getConfigurationSection("messages") ?: error("Missing messages section")

            return Lang(
                locale = yaml.getString("locale") ?: locale,
                prefix = yaml.getString("prefix").orEmpty(),
                messages = flatten(root),
            )
        }

        private fun flatten(section: ConfigurationSection): Map<String, String> {
            val values = linkedMapOf<String, String>()
            collect(section, "", values)
            return values
        }

        private fun collect(section: ConfigurationSection, path: String, values: MutableMap<String, String>) {
            for (key in section.getKeys(false)) {
                val childPath = if (path.isEmpty()) key else "$path.$key"
                val child = section.get(key)

                if (child is ConfigurationSection) {
                    collect(child, childPath, values)
                } else {
                    values[childPath] = section.getString(key).orEmpty()
                }
            }
        }
    }
}

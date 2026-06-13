package org.jan1k.plugin.trimeditor.item

import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.ArmorMeta
import org.bukkit.inventory.meta.Damageable
import org.bukkit.inventory.meta.trim.ArmorTrim
import org.bukkit.inventory.meta.trim.TrimMaterial
import org.bukkit.inventory.meta.trim.TrimPattern
import org.mockbukkit.mockbukkit.MockBukkit
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

class ItemFingerprintTest {
    @BeforeTest
    fun setUp() {
        MockBukkit.mock()
    }

    @AfterTest
    fun tearDown() {
        MockBukkit.unmock()
    }

    @Test
    fun `fingerprint ignores durability`() {
        val armor = armor(7, ArmorTrim(TrimMaterial.DIAMOND, TrimPattern.SENTRY))
        val first = ItemFingerprint.of(armor)

        val meta = armor.itemMeta as ArmorMeta
        (meta as Damageable).damage = 42
        armor.itemMeta = meta

        assertEquals(first, ItemFingerprint.of(armor))
    }

    @Test
    fun `fingerprint changes when trim changes`() {
        val sentry = armor(7, ArmorTrim(TrimMaterial.DIAMOND, TrimPattern.SENTRY))
        val dune = armor(7, ArmorTrim(TrimMaterial.DIAMOND, TrimPattern.DUNE))

        assertNotEquals(ItemFingerprint.of(sentry), ItemFingerprint.of(dune))
    }

    private fun armor(damage: Int, trim: ArmorTrim): ItemStack {
        val item = ItemStack(Material.DIAMOND_CHESTPLATE)
        val meta = item.itemMeta as ArmorMeta
        meta.trim = trim
        (meta as Damageable).damage = damage
        item.itemMeta = meta
        return item
    }
}

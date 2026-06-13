package org.jan1k.plugin.trimeditor.item

import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.enchantments.Enchantment
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.ArmorMeta
import org.bukkit.inventory.meta.Damageable
import org.bukkit.inventory.meta.trim.ArmorTrim
import org.bukkit.inventory.meta.trim.TrimMaterial
import org.bukkit.inventory.meta.trim.TrimPattern
import org.bukkit.persistence.PersistentDataType
import org.mockbukkit.mockbukkit.MockBukkit
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotSame

class ArmorTrimEditorTest {
    private val key = NamespacedKey("trimeditor", "preserve")

    @BeforeTest
    fun setUp() {
        MockBukkit.mock()
    }

    @AfterTest
    fun tearDown() {
        MockBukkit.unmock()
    }

    @Test
    fun `applying trim preserves durability and item meta`() {
        val source = ItemStack(Material.DIAMOND_CHESTPLATE)
        val meta = source.itemMeta as ArmorMeta
        meta.setDisplayName("Kept Name")
        meta.setLore(listOf("Kept Lore"))
        meta.addEnchant(Enchantment.PROTECTION, 3, true)
        meta.setCustomModelData(91)
        meta.persistentDataContainer.set(key, PersistentDataType.STRING, "kept")
        (meta as Damageable).damage = 17
        source.itemMeta = meta

        val trim = ArmorTrim(TrimMaterial.AMETHYST, TrimPattern.SPIRE)
        val edited = ArmorTrimEditor.applyTrim(source, trim)

        assertNotSame(source, edited)
        assertEquals(Material.DIAMOND_CHESTPLATE, edited.type)
        assertEquals(source.amount, edited.amount)
        val editedMeta = edited.itemMeta as ArmorMeta
        assertEquals("Kept Name", editedMeta.getDisplayName())
        assertEquals(listOf("Kept Lore"), editedMeta.getLore())
        assertEquals(3, editedMeta.getEnchantLevel(Enchantment.PROTECTION))
        assertEquals(91, editedMeta.getCustomModelData())
        assertEquals("kept", editedMeta.persistentDataContainer.get(key, PersistentDataType.STRING))
        assertEquals(17, (editedMeta as Damageable).damage)
        assertEquals(trim, editedMeta.trim)
        assertFalse((source.itemMeta as ArmorMeta).hasTrim())
    }
}

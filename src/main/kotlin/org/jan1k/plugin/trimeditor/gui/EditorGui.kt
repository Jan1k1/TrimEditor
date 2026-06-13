package org.jan1k.plugin.trimeditor.gui

import net.kyori.adventure.text.minimessage.MiniMessage
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryCloseEvent
import org.bukkit.event.inventory.InventoryDragEvent
import org.bukkit.inventory.meta.trim.ArmorTrim
import org.jan1k.plugin.trimeditor.apply.ApplyService
import org.jan1k.plugin.trimeditor.apply.TrimApplyRequest
import org.jan1k.plugin.trimeditor.apply.TrimApplyResult
import org.jan1k.plugin.trimeditor.config.PluginConfig
import org.jan1k.plugin.trimeditor.cost.EconomyService
import org.jan1k.plugin.trimeditor.cost.NoEconomyService
import org.jan1k.plugin.trimeditor.cost.RequirementService
import org.jan1k.plugin.trimeditor.cost.TrimRequirementSettings
import org.jan1k.plugin.trimeditor.item.ArmorTrimEditor
import org.jan1k.plugin.trimeditor.item.ItemFingerprint
import org.jan1k.plugin.trimeditor.lang.Lang
import org.jan1k.plugin.trimeditor.session.EditSession
import org.jan1k.plugin.trimeditor.session.SessionManager
import java.util.UUID

class EditorGui(
    private val sessions: SessionManager,
    cooldownMillis: Long = ClickGuard.DEFAULT_COOLDOWN_MILLIS,
    private val configProvider: () -> PluginConfig,
    economy: EconomyService = NoEconomyService,
    private val langProvider: () -> Lang = { Lang.load() },
) {
    private val mini = MiniMessage.miniMessage()
    private val requirements = RequirementService()
    private val applyService = ApplyService(requirements, economy)
    private val clickGuard = ClickGuard(cooldownMillis)
    private val switching = HashSet<UUID>()
    private val screens = mapOf(
        ScreenId.PATTERNS to PatternScreen(),
        ScreenId.MATERIALS to MaterialScreen(),
        ScreenId.CONFIRM to ConfirmScreen(),
    )

    fun updateCooldownMillis(cooldownMillis: Long) {
        clickGuard.cooldownMillis = cooldownMillis
    }

    fun open(player: Player): Boolean {
        val held = player.inventory.itemInMainHand
        if (!ArmorTrimEditor.isEditableArmor(held)) {
            return false
        }

        val session = EditSession(
            player.uniqueId,
            player.inventory.heldItemSlot,
            ItemFingerprint.of(held),
        ) { player.closeInventory() }
        sessions.open(session)
        show(player, session, ScreenId.PATTERNS)
        return true
    }

    @Suppress("DEPRECATION")
    fun show(player: Player, session: EditSession, screenId: ScreenId) {
        val screen = screens.getValue(screenId)
        val buttons = screen.buttons(ScreenRenderContext(player, session, configProvider(), requirements))
        val holder = EditorHolder(player.uniqueId, screenId, buttons)
        val inventory = Bukkit.createInventory(holder, screen.size, screen.title)

        holder.attach(inventory)
        session.screen = screenId
        buttons.forEach { inventory.setItem(it.slot, it.icon) }

        switching.add(player.uniqueId)
        player.openInventory(inventory)
        switching.remove(player.uniqueId)
    }

    fun close(player: Player) {
        clickGuard.clear(player.uniqueId)
        sessions.close(player.uniqueId)
    }

    fun handleClick(event: InventoryClickEvent) {
        val holder = event.view.topInventory.holder as? EditorHolder ?: return
        event.isCancelled = true

        val player = event.whoClicked as? Player ?: return
        if (holder.playerId != player.uniqueId) return
        val button = holder.button(event.rawSlot) ?: return
        if (!clickGuard.accept(player.uniqueId, event.click, event.rawSlot, event.view.topInventory.size)) return

        val session = sessions[player.uniqueId] ?: return
        button.action(GuiContext(player, session, this))
    }

    fun handleDrag(event: InventoryDragEvent) {
        val holder = event.view.topInventory.holder as? EditorHolder ?: return
        val topSlots = 0 until event.view.topInventory.size
        if (event.rawSlots.any { it in topSlots }) {
            event.isCancelled = true
        }
    }

    fun handleClose(event: InventoryCloseEvent) {
        val holder = event.view.topInventory.holder as? EditorHolder ?: return
        val player = event.player as? Player ?: return
        if (holder.playerId != player.uniqueId) return
        if (switching.remove(player.uniqueId)) return

        clickGuard.clear(player.uniqueId)
        sessions.remove(player.uniqueId)
    }

    fun forget(playerId: UUID) {
        clickGuard.clear(playerId)
        sessions.remove(playerId)
    }

    fun applySelection(player: Player, session: EditSession): TrimApplyResult {
        val pattern = session.pattern ?: return TrimApplyResult.MissingRequirements
        val material = session.material ?: return TrimApplyResult.MissingRequirements
        val fingerprint = session.fingerprint ?: return TrimApplyResult.HeldItemChanged
        val config = configProvider()

        return applyService.apply(
            player,
            TrimApplyRequest(
                handSlot = session.handSlot,
                fingerprint = fingerprint,
                trim = ArmorTrim(material, pattern),
                requirements = requirementSettings(player, config),
                moneyCost = moneyCost(player, config),
            ),
        )
    }

    fun messageFor(result: TrimApplyResult): String {
        val path = when (result) {
            TrimApplyResult.Applied -> "success.applied"
            TrimApplyResult.HeldItemChanged -> "errors.item-changed"
            TrimApplyResult.NotTrimmableArmor -> "errors.not-armor"
            TrimApplyResult.MissingRequirements -> "errors.requirements"
            TrimApplyResult.InsufficientFunds -> "errors.money"
        }
        return langProvider().message(path)
    }

    fun tell(player: Player, raw: String) {
        player.sendMessage(mini.deserialize(raw))
    }

    private fun requirementSettings(player: Player, config: PluginConfig): TrimRequirementSettings {
        return if (player.hasPermission("trimeditor.bypass.cost")) {
            TrimRequirementSettings()
        } else {
            config.requirementSettings()
        }
    }

    private fun moneyCost(player: Player, config: PluginConfig): Double {
        return if (player.hasPermission("trimeditor.bypass.cost")) 0.0 else config.moneyCost()
    }
}

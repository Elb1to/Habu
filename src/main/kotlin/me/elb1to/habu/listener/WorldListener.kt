package me.elb1to.habu.listener

import me.lucko.helper.Events
import org.bukkit.GameMode
import org.bukkit.Material
import org.bukkit.event.block.Action
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.event.block.BlockExplodeEvent
import org.bukkit.event.block.BlockPlaceEvent
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.event.entity.EntityExplodeEvent
import org.bukkit.event.entity.FoodLevelChangeEvent
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.player.PlayerDropItemEvent
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.event.player.PlayerPickupItemEvent
import org.bukkit.event.weather.WeatherChangeEvent

/**
 * @author Elb1to
 * @since 11/21/2022
 * © 2022 Habu from FrozedClub
 */
class WorldListener {

    fun setup() {
        listOf(
            WeatherChangeEvent::class,
            BlockExplodeEvent::class,
            EntityExplodeEvent::class,
            PlayerDropItemEvent::class,
            PlayerPickupItemEvent::class,
            EntityDamageEvent::class,
            FoodLevelChangeEvent::class
        ).forEach {
            Events.subscribe(it.java).handler { event ->
                event.isCancelled = true
            }
        }

        Events.subscribe(InventoryClickEvent::class.java)
            .filter { event: InventoryClickEvent ->
                event.clickedInventory == null || event.clickedInventory.name == null
            }
            .handler { event: InventoryClickEvent ->
                if (event.whoClicked.gameMode != GameMode.CREATIVE) {
                    event.isCancelled = true
                }
            }

        Events.subscribe(BlockBreakEvent::class.java).handler { event ->
            event.isCancelled = !(event.player.isOp && event.player.gameMode == GameMode.CREATIVE)
        }

        Events.subscribe(BlockPlaceEvent::class.java).handler { event ->
            event.isCancelled = !(event.player.isOp && event.player.gameMode == GameMode.CREATIVE)
        }

        Events.subscribe(PlayerInteractEvent::class.java).handler { event ->
            if (event.action == Action.PHYSICAL && event.clickedBlock.type == Material.SOIL) {
                event.isCancelled = true
            }
        }
    }
}
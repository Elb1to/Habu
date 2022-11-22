package me.elb1to.habu.user.cosmetics.armor

import me.elb1to.habu.utils.ItemBuilder
import org.bukkit.Color
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

/**
 * @author Elb1to
 * @since 11/21/2022
 * © 2022 Habu from FrozedClub
 */
data class Armor(
    val name: String,
    val permission: String,
    val icon: Material,
    val data: Int,
    val color: Color
) {

    fun getIcon(): ItemStack {
        return ItemBuilder(icon).setDurability(data).get()
    }

    fun equip(player: Player, color: Color) {
        val armor = arrayListOf(
            ItemBuilder(Material.LEATHER_HELMET).setArmorColor(color).get(),
            ItemBuilder(Material.LEATHER_CHESTPLATE).setArmorColor(color).get(),
            ItemBuilder(Material.LEATHER_LEGGINGS).setArmorColor(color).get(),
            ItemBuilder(Material.LEATHER_BOOTS).setArmorColor(color).get()
        )

        player.inventory.armorContents = armor.toTypedArray()
    }
}
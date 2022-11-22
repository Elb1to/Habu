package me.elb1to.habu.manager

import me.elb1to.habu.Habu
import me.elb1to.habu.user.cosmetics.armor.Armor
import org.bukkit.Color
import org.bukkit.Material
import org.bukkit.entity.Player

/**
 * @author Elb1to
 * @since 11/21/2022
 * © 2022 Habu from FrozedClub
 */
class ArmorManager {

    private val armors: MutableList<Armor> = ArrayList()

    init {
        val section = Habu.instance.armorCfg.config.getConfigurationSection("armors")
        if (section != null) {
            for (armor in section.getKeys(false)) {
                val name = section.getString(armor)
                val permission = "habu.armor.$armor"
                val icon = Material.valueOf(section.getString("$armor.icon"))
                val data = section.getInt("$armor.icon-data")
                val color = Color.fromRGB(section.getInt("$armor.armor-color"))

                armors.add(Armor(name, permission, icon, data, color))
            }
        }
    }

    fun getArmorAndEquip(player: Player, armor: Armor) {
        if (player.hasPermission(armor.permission)) {
            armor.equip(player, armor.color)
        }
    }

    fun getArmorByName(name: String): Armor? {
        for (armor in armors) {
            if (armor.name.equals(name, ignoreCase = true)) {
                return armor
            }
        }

        return null
    }
}
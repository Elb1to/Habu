package me.elb1to.habu

import me.elb1to.habu.layout.ScoreboardLayout
import me.elb1to.habu.listener.WorldListener
import me.elb1to.habu.manager.ArmorManager
import me.elb1to.habu.utils.config.FileConfig
import org.bukkit.plugin.java.JavaPlugin

/**
 * @author Elb1to
 * @since 11/21/2022
 * © 2022 Habu from FrozedClub
 */
class Habu : JavaPlugin() {

    val armorCfg by lazy { FileConfig(this, "armors.yml") }
    val settingsCfg by lazy { FileConfig(this, "settings.yml") }
    val messagesCfg by lazy { FileConfig(this, "messages.yml") }
    val scoreboardCfg by lazy { FileConfig(this, "scoreboard.yml") }

    companion object {
        @JvmStatic lateinit var instance: Habu
    }

    override fun onEnable() {
        instance = this

        armorCfg.save()
        settingsCfg.save()
        messagesCfg.save()
        scoreboardCfg.save()

        // Managers
        ArmorManager()
        ScoreboardLayout()

        // Listeners
        WorldListener().setup()
    }
}
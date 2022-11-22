package me.elb1to.habu.layout

import me.elb1to.habu.Habu
import me.lucko.helper.Events
import me.lucko.helper.Schedulers
import me.lucko.helper.Services
import me.lucko.helper.metadata.Metadata
import me.lucko.helper.metadata.MetadataKey
import me.lucko.helper.metadata.MetadataMap
import me.lucko.helper.scoreboard.Scoreboard
import me.lucko.helper.scoreboard.ScoreboardObjective
import me.lucko.helper.scoreboard.ScoreboardProvider
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.scoreboard.DisplaySlot
import java.util.function.BiConsumer

/**
 * @author Elb1to
 * @since 11/21/2022
 * © 2022 Habu from FrozedClub
 */
class ScoreboardLayout {

    init {
        setup()
    }

    private fun setup() {
        val key: MetadataKey<ScoreboardObjective> = MetadataKey.create("scoreboard", ScoreboardObjective::class.java)

        val updater: BiConsumer<Player, ScoreboardObjective> =
            BiConsumer<Player, ScoreboardObjective> { player: Player, scoreboard: ScoreboardObjective ->
                scoreboard.displayName = Habu.instance.scoreboardCfg.config.getString("title")

                val lines = Habu.instance.scoreboardCfg.config.getStringList("lines")
                lines.replaceAll {
                    it.replace("%player%", player.name)
                    it.replace("%online%", Habu.instance.server.onlinePlayers.size.toString())
                }

                scoreboard.applyLines(lines)
            }

        val scoreboard: Scoreboard = Services.load(ScoreboardProvider::class.java).scoreboard

        Events.subscribe(PlayerJoinEvent::class.java).handler {
            val obj: ScoreboardObjective = scoreboard.createPlayerObjective(it.player, "null", DisplaySlot.SIDEBAR)
            Metadata.provideForPlayer(it.player).put(key, obj)
            updater.accept(it.player, obj)
        }

        Schedulers.async().runRepeating(Runnable {
            for (player in Bukkit.getOnlinePlayers()) {
                val metadata: MetadataMap = Metadata.provideForPlayer(player)
                val obj: ScoreboardObjective? = metadata.getOrNull(key)
                if (obj != null) {
                    updater.accept(player, obj)
                }
            }
        }, 3L, 3L)
    }
}
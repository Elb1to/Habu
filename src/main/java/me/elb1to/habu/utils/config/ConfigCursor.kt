package me.elb1to.habu.utils.config

import org.bukkit.Bukkit
import org.bukkit.World
import java.util.UUID

class ConfigCursor(val fileConfig: FileConfig, var path: String?) {

    @JvmOverloads
    fun exists(path: String? = null): Boolean {
        return fileConfig.config.contains(this.path + if (path == null) "" else ".$path")
    }

    val keys: Set<String>
        get() = getKeys(null)

    fun getKeys(path: String?): Set<String> {
        return fileConfig.config.getConfigurationSection(this.path + if (path == null) "" else ".$path").getKeys(false)
    }

    fun getBoolean(path: String): Boolean {
        return fileConfig.config.getBoolean((if (this.path == null) "" else this.path + ".") + "." + path)
    }

    fun getInt(path: String): Int {
        return fileConfig.config.getInt((if (this.path == null) "" else this.path + ".") + "." + path)
    }

    fun getDouble(path: String): Double {
        return fileConfig.config.getDouble((if (this.path == null) "" else this.path + ".") + "." + path)
    }

    fun getDouble(path: String, defaultValue: Double): Double {
        return fileConfig.config.getDouble((if (this.path == null) "" else this.path + ".") + "." + path, defaultValue)
    }

    fun getLong(path: String): Long {
        return fileConfig.config.getLong((if (this.path == null) "" else this.path + ".") + "." + path)
    }

    fun getString(path: String): String {
        return fileConfig.config.getString((if (this.path == null) "" else this.path + ".") + "." + path)
    }

    fun getStringList(path: String): List<String> {
        return fileConfig.config.getStringList((if (this.path == null) "" else this.path + ".") + "." + path)
    }

    fun getUuid(path: String): UUID {
        return UUID.fromString(fileConfig.config.getString(this.path + "." + path))
    }

    fun getWorld(path: String): World {
        return Bukkit.getWorld(fileConfig.config.getString(this.path + "." + path))
    }

    fun set(value: Any?) {
        set(null, value)
    }

    operator fun set(path: String?, value: Any?) {
        fileConfig.config[this.path + (if (path == null) "" else ".$path")] = value
    }

    fun save() {
        fileConfig.save()
    }
}
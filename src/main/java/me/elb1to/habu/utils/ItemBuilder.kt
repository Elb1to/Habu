package me.elb1to.habu.utils

import com.mojang.authlib.GameProfile
import com.mojang.authlib.properties.Property
import org.apache.commons.codec.binary.Base64
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.Color
import org.bukkit.Material
import org.bukkit.enchantments.Enchantment
import org.bukkit.enchantments.EnchantmentTarget
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.LeatherArmorMeta
import org.bukkit.inventory.meta.SkullMeta
import java.lang.reflect.Field
import java.util.UUID
import java.util.function.Consumer

class ItemBuilder {

    private var itemStack: ItemStack

    constructor(material: Material?) {
        itemStack = ItemStack(material, 1)
    }

    constructor(itemStack: ItemStack) {
        this.itemStack = itemStack.clone()
    }

    constructor(material: Material?, damage: Int) {
        itemStack = ItemStack(material, 1, damage.toShort())
    }

    constructor(material: Material?, amount: Int, damage: Int) {
        itemStack = ItemStack(material, amount, damage.toShort())
    }

    fun setName(name: String?): ItemBuilder {
        var name = name
        if (name != null) {
            name = ChatColor.translateAlternateColorCodes('&', name)
            val meta = itemStack.itemMeta
            meta.displayName = name
            itemStack.itemMeta = meta
        }
        return this
    }

    fun setLore(lore: List<String?>?): ItemBuilder {
        if (lore != null) {
            val list: MutableList<String> = ArrayList()
            lore.forEach(Consumer { line: String? -> list.add(ChatColor.translateAlternateColorCodes('&', line)) })
            val meta = itemStack.itemMeta
            meta.lore = list
            itemStack.itemMeta = meta
        }
        return this
    }

    fun setAmount(amount: Int): ItemBuilder {
        itemStack.amount = amount
        return this
    }

    fun addEnchants(enchants: List<String>?): ItemBuilder {
        enchants?.forEach(Consumer { enchant: String ->
            val arr = enchant.replace(" ", "").split(",".toRegex()).dropLastWhile { it.isEmpty() }
                .toTypedArray()
            val enchantment = Enchantment.getByName(arr[0])
            val level = arr[1].toInt()
            itemStack.addUnsafeEnchantment(enchantment, level)
        })
        return this
    }

    fun setDurability(dur: Short): ItemBuilder {
        itemStack.durability = dur
        return this
    }

    fun setDurability(dur: Int): ItemBuilder {
        itemStack.durability = dur.toShort()
        return this
    }

    fun setHeadUrl(url: String?): ItemBuilder {
        val headMeta = itemStack.itemMeta as SkullMeta
        val profile = GameProfile(UUID.randomUUID(), null)
        val encodedData = Base64.encodeBase64(String.format("{textures:{SKIN:{url:\"%s\"}}}", url).toByteArray())
        profile.properties.put("textures", Property("textures", String(encodedData)))
        val profileField: Field
        try {
            profileField = headMeta.javaClass.getDeclaredField("profile")
            profileField.isAccessible = true
            profileField[headMeta] = profile
        } catch (e1: NoSuchFieldException) {
            e1.printStackTrace()
        } catch (e1: IllegalArgumentException) {
            e1.printStackTrace()
        } catch (e1: IllegalAccessException) {
            e1.printStackTrace()
        }
        itemStack.itemMeta = headMeta

        return this
    }

    fun setOwner(owner: String?): ItemBuilder {
        if (itemStack.type == Material.SKULL_ITEM) {
            val meta = itemStack.itemMeta as SkullMeta
            meta.owner = owner
            itemStack.itemMeta = meta
            return this
        }

        throw IllegalArgumentException("setOwner() only applicable for Skull Item")
    }

    fun setArmorColor(color: Color?): ItemBuilder {
        try {
            val leatherArmorMeta = itemStack.itemMeta as LeatherArmorMeta
            leatherArmorMeta.color = color
            itemStack.itemMeta = leatherArmorMeta
        } catch (exception: Exception) {
            Bukkit.getConsoleSender().sendMessage("Error set armor color.")
        }

        return this
    }

    fun glow(): ItemBuilder {
        val meta = itemStack.itemMeta
        meta.addEnchant(Glow(), 1, true)
        itemStack.itemMeta = meta
        return this
    }

    fun get(): ItemStack {
        return itemStack
    }

    private class Glow : Enchantment(25) {
        override fun canEnchantItem(arg0: ItemStack): Boolean {
            return false
        }

        override fun conflictsWith(arg0: Enchantment): Boolean {
            return false
        }

        override fun getItemTarget(): EnchantmentTarget? {
            return null
        }

        override fun getMaxLevel(): Int {
            return 2
        }

        override fun getName(): String? {
            return null
        }

        override fun getStartLevel(): Int {
            return 1
        }
    }

    companion object {
        fun registerGlow() {
            try {
                val f = Enchantment::class.java.getDeclaredField("acceptingNew")
                f.isAccessible = true
                f[null] = true
            } catch (e: Exception) {
                e.printStackTrace()
            }
            try {
                val glow = Glow()
                Enchantment.registerEnchantment(glow)
            } catch (e: IllegalArgumentException) {
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}
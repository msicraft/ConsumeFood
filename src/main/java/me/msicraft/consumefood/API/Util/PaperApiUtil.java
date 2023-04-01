package me.msicraft.consumefood.API.Util;

import com.destroystokyo.paper.profile.PlayerProfile;
import com.destroystokyo.paper.profile.ProfileProperty;
import me.msicraft.consumefood.ConsumeFood;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class PaperApiUtil {

    public static ItemStack getPaperApiPlayerHead_Inv(String name, int customModelData, UUID uuid, String textureValue, List<String> loreList, String internalName) {
        ItemStack itemStack = null;
        if (textureValue != null && uuid != null) {
            itemStack = new ItemStack(Material.PLAYER_HEAD, 1);
            SkullMeta skullMeta = (SkullMeta) itemStack.getItemMeta();
            PersistentDataContainer data = skullMeta.getPersistentDataContainer();
            data.set(new NamespacedKey(ConsumeFood.getPlugin(), "ConsumeFood-CustomFood-Edit"), PersistentDataType.STRING, internalName);
            if (name != null) {
                skullMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', name));
            } else {
                name = uuid.toString();
            }
            if (customModelData != -1) {
                skullMeta.setCustomModelData(customModelData);
            }
            List<String> list = new ArrayList<>();
            for (String s : loreList) {
                list.add(ChatColor.translateAlternateColorCodes('&', s));
            }
            skullMeta.setLore(list);
            PlayerProfile playerProfile = Bukkit.createProfile(uuid, name);
            playerProfile.setProperty(new ProfileProperty("textures", textureValue));
            itemStack.setItemMeta(skullMeta);
        }
        return itemStack;
    }

    public static ItemStack getCustomFood_Inv(Material material ,String name, int customModelData, List<String> loreList, String internalName) {
        ItemStack itemStack = new ItemStack(material, 1);
        ItemMeta itemMeta = itemStack.getItemMeta();
        PersistentDataContainer data = itemMeta.getPersistentDataContainer();
        data.set(new NamespacedKey(ConsumeFood.getPlugin(), "ConsumeFood-CustomFood-Edit"), PersistentDataType.STRING, internalName);
        if (name != null) {
            itemMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', name));
        }
        if (customModelData != -1) {
            itemMeta.setCustomModelData(customModelData);
        }
        List<String> list = new ArrayList<>();
        for (String s : loreList) {
            list.add(ChatColor.translateAlternateColorCodes('&', s));
        }
        itemMeta.setLore(list);
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

}

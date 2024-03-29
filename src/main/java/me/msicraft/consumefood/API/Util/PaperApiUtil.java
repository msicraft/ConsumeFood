package me.msicraft.consumefood.API.Util;

import com.destroystokyo.paper.profile.PlayerProfile;
import com.destroystokyo.paper.profile.ProfileProperty;
import me.msicraft.consumefood.ConsumeFood;
import me.msicraft.consumefood.CustomFood.CustomItemUtil;
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PaperApiUtil {

    public static UUID getUUIDToItemStack(ItemStack itemStack) {
        UUID uuid = null;
        SkullMeta skullMeta = (SkullMeta) itemStack.getItemMeta();
        PlayerProfile playerProfile = skullMeta.getPlayerProfile();
        if (playerProfile != null) {
            uuid = playerProfile.getId();
        }
        return uuid;
    }

    public static String getTextureValueToItemStack(ItemStack itemStack) {
        String value = null;
        SkullMeta skullMeta = (SkullMeta) itemStack.getItemMeta();
        PlayerProfile playerProfile = skullMeta.getPlayerProfile();
        if (playerProfile != null) {
            if (playerProfile.hasTextures()) {
                for (ProfileProperty profileProperty : playerProfile.getProperties()) {
                    value = profileProperty.getValue();
                    break;
                }
            }
        }
        return value;
    }

    public static ItemStack getPaperApiPlayerHead_Inv(String name, int customModelData, UUID uuid, String textureValue, List<String> loreList, String internalName) {
        ItemStack itemStack = null;
        if (textureValue != null && uuid != null) {
            Pattern pattern = ConsumeFood.getPlugin().getPattern();
            itemStack = new ItemStack(Material.PLAYER_HEAD, 1);
            CustomItemUtil.importType importType = CustomItemUtil.importType.SIMPLE;
            if (ConsumeFood.customFoodConfig.getConfig().contains("CustomFood." + internalName + ".ImportType")) {
                importType = CustomItemUtil.importType.valueOf(ConsumeFood.customFoodConfig.getConfig().getString("CustomFood." + internalName + ".ImportType"));
            }
            if (importType == CustomItemUtil.importType.ALL && ConsumeFood.customFoodConfig.getConfig().contains("CustomFood." + internalName + ".ImportData")) {
                String data = ConsumeFood.customFoodConfig.getConfig().getString("CustomFood." + internalName + ".ImportData");
                if (data != null) {
                    itemStack = CustomItemUtil.getItemStackToImportTypeAll(data);
                    itemStack.setType(Material.PLAYER_HEAD);
                }
            }
            SkullMeta skullMeta = (SkullMeta) itemStack.getItemMeta();
            PersistentDataContainer data = skullMeta.getPersistentDataContainer();
            data.set(new NamespacedKey(ConsumeFood.getPlugin(), "ConsumeFood-CustomFood-Edit"), PersistentDataType.STRING, internalName);
            if (name != null) {
                Matcher matcher = pattern.matcher(name);
                while (matcher.find()) {
                    String c = name.substring(matcher.start(), matcher.end());
                    name = name.replace(c, net.md_5.bungee.api.ChatColor.of(c) + "");
                    matcher = pattern.matcher(name);
                }
                skullMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', name));
            } else {
                name = uuid.toString();
            }
            if (customModelData != -1) {
                skullMeta.setCustomModelData(customModelData);
            }
            List<String> list = new ArrayList<>();
            for (String s : loreList) {
                Matcher matcher = pattern.matcher(s);
                while (matcher.find()) {
                    String c = s.substring(matcher.start(), matcher.end());
                    s = s.replace(c, net.md_5.bungee.api.ChatColor.of(c) + "");
                    matcher = pattern.matcher(s);
                }
                list.add(ChatColor.translateAlternateColorCodes('&', s));
            }
            skullMeta.setLore(list);
            PlayerProfile playerProfile = Bukkit.createProfile(uuid, name);
            playerProfile.setProperty(new ProfileProperty("textures", textureValue));
            skullMeta.setPlayerProfile(playerProfile);
            itemStack.setItemMeta(skullMeta);
        }
        return itemStack;
    }

    public static ItemStack getCustomFood_Inv(Material material ,String name, int customModelData, List<String> loreList, String internalName) {
        ItemStack itemStack = new ItemStack(material, 1);
        Pattern pattern = ConsumeFood.getPlugin().getPattern();
        CustomItemUtil.importType importType = CustomItemUtil.importType.SIMPLE;
        if (ConsumeFood.customFoodConfig.getConfig().contains("CustomFood." + internalName + ".ImportType")) {
            importType = CustomItemUtil.importType.valueOf(ConsumeFood.customFoodConfig.getConfig().getString("CustomFood." + internalName + ".ImportType"));
        }
        if (importType == CustomItemUtil.importType.ALL && ConsumeFood.customFoodConfig.getConfig().contains("CustomFood." + internalName + ".ImportData")) {
            String data = ConsumeFood.customFoodConfig.getConfig().getString("CustomFood." + internalName + ".ImportData");
            if (data != null) {
                itemStack = CustomItemUtil.getItemStackToImportTypeAll(data);
                itemStack.setType(material);
            }
        }
        ItemMeta itemMeta = itemStack.getItemMeta();
        PersistentDataContainer data = itemMeta.getPersistentDataContainer();
        data.set(new NamespacedKey(ConsumeFood.getPlugin(), "ConsumeFood-CustomFood-Edit"), PersistentDataType.STRING, internalName);
        if (name != null) {
            Matcher matcher = pattern.matcher(name);
            while (matcher.find()) {
                String c = name.substring(matcher.start(), matcher.end());
                name = name.replace(c, net.md_5.bungee.api.ChatColor.of(c) + "");
                matcher = pattern.matcher(name);
            }
            itemMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', name));
        }
        if (customModelData != -1) {
            itemMeta.setCustomModelData(customModelData);
        }
        List<String> list = new ArrayList<>();
        for (String s : loreList) {
            Matcher matcher = pattern.matcher(s);
            while (matcher.find()) {
                String c = s.substring(matcher.start(), matcher.end());
                s = s.replace(c, net.md_5.bungee.api.ChatColor.of(c) + "");
                matcher = pattern.matcher(s);
            }
            list.add(ChatColor.translateAlternateColorCodes('&', s));
        }
        itemMeta.setLore(list);
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

}

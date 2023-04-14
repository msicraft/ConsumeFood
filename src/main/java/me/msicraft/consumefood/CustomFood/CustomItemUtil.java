package me.msicraft.consumefood.CustomFood;

import me.msicraft.consumefood.API.Util.PaperApiUtil;
import me.msicraft.consumefood.API.Util.SpigotUtil;
import me.msicraft.consumefood.ConsumeFood;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.*;

public class CustomItemUtil {

    public enum importType {
        SIMPLE,ALL
    }

    public void importItemStack(Player player, String internalName, ItemStack itemStack, importType importType, int brandType) {
        UUID uuid = null;
        String textureValue = null;
        boolean isPlayerHead;
        isPlayerHead = itemStack.getType() == Material.PLAYER_HEAD;
        if (isPlayerHead) {
            switch (brandType) {
                case 0:
                    uuid = PaperApiUtil.getUUIDToItemStack(itemStack);
                    textureValue = PaperApiUtil.getTextureValueToItemStack(itemStack);
                    break;
                case 1:
                    uuid = SpigotUtil.getUUIDToItemStack(itemStack);
                    textureValue = SpigotUtil.getTextureValueToItemStack(itemStack);
                    break;
            }
            if (uuid == null || textureValue == null) {
                player.sendMessage(ChatColor.GRAY + "========================================");
                player.sendMessage(ChatColor.RED + "Failed to import custom item");
                player.sendMessage(ChatColor.RED + "Import Type: " + ChatColor.WHITE + importType.name());
                player.sendMessage(ChatColor.RED + "Bukkit Brand Type: " + ChatColor.WHITE + brandType);
                player.sendMessage(ChatColor.RED + "UUID: " + ChatColor.WHITE + uuid);
                player.sendMessage(ChatColor.RED + "TextureValue: " + ChatColor.WHITE + textureValue);
                player.sendMessage(ChatColor.GRAY + "========================================");
                return;
            }
        }
        switch (importType) {
            case SIMPLE:
                saveBySimple(internalName, itemStack, uuid, textureValue, isPlayerHead);
                break;
            case ALL:
                saveByAll(internalName, itemStack, uuid, textureValue, isPlayerHead);
                break;
        }
        player.sendMessage(ChatColor.GREEN + "Custom item successfully imported.");
    }

    public static ItemStack getItemStackToImportTypeAll(String encode) {
        ItemStack itemStack = new ItemStack(Material.STONE, 1);
        try {
            ByteArrayInputStream inputStream = new ByteArrayInputStream(Base64Coder.decodeLines(encode));
            BukkitObjectInputStream dataInput = new BukkitObjectInputStream(inputStream);
            itemStack = (ItemStack) dataInput.readObject();
            dataInput.close();
        } catch (ClassNotFoundException |IOException e) {
            Bukkit.getConsoleSender().sendMessage("Unable to decode class type." + e);
        }
        return itemStack;
    }

    private void saveBySimple(String internalName, ItemStack itemStack, UUID uuid, String textureValue, boolean isPlayerHead) {
        ItemMeta itemMeta = itemStack.getItemMeta();
        Material material = itemStack.getType();
        String displayName = "";
        List<String> lore = new ArrayList<>();
        int customModelData = -1;
        Map<Enchantment, Integer> enchantMap;
        ConsumeFood.customFoodConfig.getConfig().set("CustomFood." + internalName + ".Material", material.name().toUpperCase());
        if (itemMeta.hasDisplayName()) {
            displayName = itemMeta.getDisplayName();
        }
        ConsumeFood.customFoodConfig.getConfig().set("CustomFood." + internalName + ".Name", displayName);
        if (itemMeta.hasLore()) {
            lore = itemMeta.getLore();
        }
        ConsumeFood.customFoodConfig.getConfig().set("CustomFood." + internalName + ".Lore", lore);
        if (itemMeta.hasCustomModelData()) {
            customModelData = itemMeta.getCustomModelData();
        }
        ConsumeFood.customFoodConfig.getConfig().set("CustomFood." + internalName + ".Data", customModelData);
        if (itemMeta.hasEnchants()) {
            enchantMap = itemMeta.getEnchants();
            List<String> temp = new ArrayList<>();
            for (Enchantment enchantment : enchantMap.keySet()) {
                String key = enchantment.getKey().getKey() + ":" + enchantMap.get(enchantment);
                temp.add(key);
            }
            ConsumeFood.customFoodConfig.getConfig().set("CustomFood." + internalName + ".Enchant", temp);
        }
        if (isPlayerHead) {
            ConsumeFood.customFoodConfig.getConfig().set("CustomFood." + internalName + ".TextureValue", textureValue);
            ConsumeFood.customFoodConfig.getConfig().set("CustomFood." + internalName + ".UUID", uuid.toString());
        }
        ConsumeFood.customFoodConfig.getConfig().set("CustomFood." + internalName + ".ImportType", CustomItemUtil.importType.SIMPLE.name());
        ConsumeFood.customFoodConfig.saveConfig();
    }

    private void saveByAll(String internalName, ItemStack itemStack, UUID uuid, String textureValue, boolean isPlayerHead) {
        String encode = null;
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            BukkitObjectOutputStream dataOutput = new BukkitObjectOutputStream(outputStream);
            dataOutput.writeObject(itemStack);
            dataOutput.flush();
            byte[] serialized = outputStream.toByteArray();
            encode = Base64.getEncoder().encodeToString(serialized);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (encode != null) {
            ItemMeta itemMeta = itemStack.getItemMeta();
            Material material = itemStack.getType();
            String displayName = "";
            List<String> lore = new ArrayList<>();
            int customModelData = -1;
            ConsumeFood.customFoodConfig.getConfig().set("CustomFood." + internalName + ".Material", material.name().toUpperCase());
            if (itemMeta.hasDisplayName()) {
                displayName = itemMeta.getDisplayName();
            }
            ConsumeFood.customFoodConfig.getConfig().set("CustomFood." + internalName + ".Name", displayName);
            if (itemMeta.hasLore()) {
                lore = itemMeta.getLore();
            }
            ConsumeFood.customFoodConfig.getConfig().set("CustomFood." + internalName + ".Lore", lore);
            if (itemMeta.hasCustomModelData()) {
                customModelData = itemMeta.getCustomModelData();
            }
            ConsumeFood.customFoodConfig.getConfig().set("CustomFood." + internalName + ".Data", customModelData);
            if (isPlayerHead) {
                ConsumeFood.customFoodConfig.getConfig().set("CustomFood." + internalName + ".TextureValue", textureValue);
                ConsumeFood.customFoodConfig.getConfig().set("CustomFood." + internalName + ".UUID", uuid.toString());
            }
            ConsumeFood.customFoodConfig.getConfig().set("CustomFood." + internalName + ".ImportType", CustomItemUtil.importType.ALL.name());
            ConsumeFood.customFoodConfig.getConfig().set("CustomFood." + internalName + ".ImportData", encode);
            ConsumeFood.customFoodConfig.saveConfig();
        } else {
            Bukkit.getConsoleSender().sendMessage(ConsumeFood.prefix + ChatColor.RED + "Failed to import custom item. ["+ChatColor.WHITE + internalName + ChatColor.RED + "]");
        }
    }

}

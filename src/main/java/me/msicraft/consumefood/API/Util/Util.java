package me.msicraft.consumefood.API.Util;

import me.msicraft.consumefood.ConsumeFood;
import me.msicraft.consumefood.VanillaFood.VanillaFoodUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Set;

public class Util {

    private final VanillaFoodUtil vanillaFoodUtil = new VanillaFoodUtil();

    public void configDataCheck() {
        ConfigurationSection section = ConsumeFood.getPlugin().getConfig().getConfigurationSection("Food");
        if (section != null) {
            ArrayList<String> identified = new ArrayList<>();
            ArrayList<String> unidentified = new ArrayList<>();
            Set<String> list = section.getKeys(false);
            for (String foodName : list) {
                if (vanillaFoodUtil.isVanillaFood(foodName)) {
                    identified.add(foodName);
                } else {
                    unidentified.add(foodName);
                }
            }
            Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + ConsumeFood.prefix + " " + identified.size() + " vanilla foods identified");
            if (!unidentified.isEmpty()) {
                Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + ConsumeFood.prefix + " " + unidentified.size() + ChatColor.RED + " vanilla foods not identified");
            }
        }
        ConfigurationSection customConfigSection = ConsumeFood.customFoodConfig.getConfig().getConfigurationSection("CustomFood");
        if (customConfigSection != null) {
            Set<String> list = customConfigSection.getKeys(false);
            ArrayList<String> identified = new ArrayList<>(list);
            if (identified.size() > 0) {
                Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + ConsumeFood.prefix + " " + identified.size() + " custom foods identified");
            }
        }
    }

    public String getReachMaxFoodLevel() {
        boolean isEnabled = ConsumeFood.messageConfig.getConfig().contains("Max-foodlevel.Enabled") && ConsumeFood.messageConfig.getConfig().getBoolean("Max-foodlevel.Enabled");
        String a = "";
        if (isEnabled) {
            if (ConsumeFood.messageConfig.getConfig().contains("Max-foodlevel.Message")) {
                a = ConsumeFood.messageConfig.getConfig().getString("Max-foodlevel.Message");
            }
        }
        return a;
    }

    public String getReachMaxSaturation() {
        boolean isEnabled = ConsumeFood.messageConfig.getConfig().contains("Max-saturation.Enabled") && ConsumeFood.messageConfig.getConfig().getBoolean("Max-saturation.Enabled");
        String a = "";
        if (isEnabled) {
            if (ConsumeFood.messageConfig.getConfig().contains("Max-saturation.Message")) {
                a = ConsumeFood.messageConfig.getConfig().getString("Max-saturation.Message");
            }
        }
        return a;
    }

    public String getVanillaGlobalCooldownMessage() {
        boolean isEnabled = ConsumeFood.messageConfig.getConfig().contains("VanillaFood-GlobalCooldown.Enabled") && ConsumeFood.messageConfig.getConfig().getBoolean("VanillaFood-GlobalCooldown.Enabled");
        String a = null;
        if (isEnabled) {
            if (ConsumeFood.messageConfig.getConfig().contains("VanillaFood-GlobalCooldown.Message")) {
                a = ConsumeFood.messageConfig.getConfig().getString("VanillaFood-GlobalCooldown.Message");
            }
        }
        return a;
    }

    public String getVanillaPersonalCooldownMessage() {
        boolean isEnabled = ConsumeFood.messageConfig.getConfig().contains("VanillaFood-PersonalCooldown.Enabled") && ConsumeFood.messageConfig.getConfig().getBoolean("VanillaFood-PersonalCooldown.Enabled");
        String a = null;
        if (isEnabled) {
            if (ConsumeFood.messageConfig.getConfig().contains("VanillaFood-PersonalCooldown.Message")) {
                a = ConsumeFood.messageConfig.getConfig().getString("VanillaFood-PersonalCooldown.Message");
            }
        }
        return a;
    }

    public String getCustomFoodGlobalCooldownMessage() {
        boolean isEnabled = ConsumeFood.messageConfig.getConfig().contains("Customfood-GlobalCooldown.Enabled") && ConsumeFood.messageConfig.getConfig().getBoolean("Customfood-GlobalCooldown.Enabled");
        String a = null;
        if (isEnabled) {
            if (ConsumeFood.messageConfig.getConfig().contains("Customfood-GlobalCooldown.Message")) {
                a = ConsumeFood.messageConfig.getConfig().getString("Customfood-GlobalCooldown.Message");
            }
        }
        return a;
    }

    public String getCustomFoodPersonalCooldownMessage() {
        boolean isEnabled = ConsumeFood.messageConfig.getConfig().contains("Customfood-PersonalCooldown.Enabled") && ConsumeFood.messageConfig.getConfig().getBoolean("Customfood-PersonalCooldown.Enabled");
        String a = null;
        if (isEnabled) {
            if (ConsumeFood.messageConfig.getConfig().contains("Customfood-PersonalCooldown.Message")) {
                a = ConsumeFood.messageConfig.getConfig().getString("Customfood-PersonalCooldown.Message");
            }
        }
        return a;
    }

    public String getPermissionErrorMessage() {
        boolean isEnabled = ConsumeFood.messageConfig.getConfig().contains("Permission-Error.Enabled") && ConsumeFood.messageConfig.getConfig().getBoolean("Permission-Error.Enabled");
        String a = null;
        if (isEnabled) {
            if (ConsumeFood.messageConfig.getConfig().contains("Permission-Error.Message")) {
                a = ConsumeFood.messageConfig.getConfig().getString("Permission-Error.Message");
            }
        }
        return a;
    }

    public static boolean isReturnBowlOrBottleEnabled() {
        boolean check = false;
        if (ConsumeFood.getPlugin().getConfig().contains("CustomSetting.Return-BowlOrBottle")) {
            check = ConsumeFood.getPlugin().getConfig().getBoolean("CustomSetting.Return-BowlOrBottle");
        }
        return check;
    }

    private static final ArrayList<Material> bowlMaterials = new ArrayList<>(Arrays.asList(Material.BEETROOT_SOUP, Material.MUSHROOM_STEW, Material.RABBIT_STEW));
    private static final ArrayList<Material> bottleMaterials = new ArrayList<>(Arrays.asList(Material.HONEY_BOTTLE, Material.POTION));

    public enum putInType {
        BOWL, BOTTLE, NONE
    }

    public static putInType getInBowlOrBottleType(ItemStack itemStack) {
        Material material = itemStack.getType();
        if (bowlMaterials.contains(material)) {
            return putInType.BOWL;
        }
        if (bottleMaterials.contains(material)) {
            return putInType.BOTTLE;
        }
        return putInType.NONE;
    }

    public static int getPlayerEmptySlot(Player player) {
        int slot = -1;
        int size = 36;
        for (int a = 0; a<size; a++) {
            ItemStack itemStack = player.getInventory().getItem(a);
            if (itemStack == null) {
                slot = a;
                break;
            }

        }
        return slot;
    }

}

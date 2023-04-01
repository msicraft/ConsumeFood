package me.msicraft.consumefood.API.Util;

import me.msicraft.consumefood.ConsumeFood;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;

import java.util.ArrayList;
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
        String a = "";
        if (ConsumeFood.messageConfig.getConfig().contains("Max-foodlevel")) {
            a = ConsumeFood.messageConfig.getConfig().getString("Max-foodlevel");
        }
        return a;
    }

    public String getReachMaxSaturation() {
        String a = "";
        if (ConsumeFood.messageConfig.getConfig().contains("Max-saturation")) {
            a = ConsumeFood.messageConfig.getConfig().getString("Max-saturation");
        }
        return a;
    }

    public String getVanillaGlobalCooldownMessage() {
        String s = null;
        if (ConsumeFood.messageConfig.getConfig().contains("VanillaFood-GlobalCooldown")) {
            s = ConsumeFood.messageConfig.getConfig().getString("VanillaFood-GlobalCooldown");
        }
        return s;
    }

    public String getVanillaPersonalCooldownMessage() {
        String s = null;
        if (ConsumeFood.messageConfig.getConfig().contains("VanillaFood-PersonalCooldown")) {
            s = ConsumeFood.messageConfig.getConfig().getString("VanillaFood-PersonalCooldown");
        }
        return s;
    }

    public String getCustomFoodGlobalCooldownMessage() {
        String s = null;
        if (ConsumeFood.messageConfig.getConfig().contains("Customfood-GlobalCooldown")) {
            s = ConsumeFood.messageConfig.getConfig().getString("Customfood-GlobalCooldown");
        }
        return s;
    }

    public String getCustomFoodPersonalCooldownMessage() {
        String s = null;
        if (ConsumeFood.messageConfig.getConfig().contains("Customfood-PersonalCooldown")) {
            s = ConsumeFood.messageConfig.getConfig().getString("Customfood-PersonalCooldown");
        }
        return s;
    }

}

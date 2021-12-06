package com.msicraft.consumefood;

import com.msicraft.consumefood.command.HungerCommand;
import com.msicraft.consumefood.events.ConsumeFoodEvents;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.Objects;
import java.util.Set;


public class ConsumeFood extends JavaPlugin {

    public static ConsumeFood plugin;

    public static Set<String> foodnamelist() {
        Set<String> foodname = Objects.requireNonNull(plugin.getConfig().getConfigurationSection("Food")).getKeys(false);
        for (String foodlist : foodname) {
            if (foodlist != null) {
            }
        }
        return foodname;
    }

    @Override
    public void onEnable() {
        createFiles();
        plugin = this;
        foodnamelist();
        getCommand("hunger").setExecutor(new HungerCommand());
        getCommand("saturation").setExecutor(new HungerCommand());
        getServer().getPluginManager().registerEvents(new ConsumeFoodEvents(), this);
        getServer().getConsoleSender().sendMessage(ChatColor.GREEN + "[Consume Food] Plugin Enable");
    }

    @Override
    public void onDisable() {
        getServer().getConsoleSender().sendMessage(ChatColor.RED + "[Consume Food] Plugin Disable");
    }

    public void createFiles() {
        File configf = new File(getDataFolder(), "config.yml");

        if (!configf.exists()) {
            configf.getParentFile().mkdirs();
            saveResource("config.yml", false);
        }
        FileConfiguration config = new YamlConfiguration();

        try {
            config.load(configf);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!sender.hasPermission("consumefood.reload")) {
            sender.sendMessage(ChatColor.RED + "You do not have permission!");
        }
        if (cmd.getName().equalsIgnoreCase("consumefoodreload")) {
            if (args.length == 0) {
                plugin.reloadConfig();
                sender.sendMessage(ChatColor.GREEN + "Reloaded [Consume Food] Plugin Config");
                foodnamelist();
                getServer().getConsoleSender().sendMessage(ChatColor.GREEN + "Reloaded [Consume Food] Plugin Config");
            }
            if (args.length >= 1) {
                sender.sendMessage(ChatColor.RED + "/consumefoodreload");
            }
        }
        return true;
    }

}


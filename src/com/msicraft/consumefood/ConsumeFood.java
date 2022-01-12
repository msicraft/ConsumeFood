package com.msicraft.consumefood;

import com.msicraft.consumefood.command.HungerCommand;
import com.msicraft.consumefood.events.ConsumeFoodEvents;
import com.msicraft.consumefood.events.Food_Interact_Event;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;


public class ConsumeFood extends JavaPlugin {

    public static ConsumeFood plugin;


    public static Set<String> foodnamelist() {
        Set<String> foodname = Objects.requireNonNull(plugin.getConfig().getConfigurationSection("Food")).getKeys(false);
        for (String foodlist : foodname) {
            if (foodlist == null) {
                System.out.print("");
            }
        }
        return foodname;
    }


    public static Set<String> buff_food_list() {
        Set<String> buffdebufffoodname = Objects.requireNonNull(plugin.getConfig().getConfigurationSection("Buff-Debuff_Food")).getKeys(false);
        for (String buffdebufffoodlist : buffdebufffoodname) {
            if (buffdebufffoodlist == null) {
                System.out.print("");
            }
        }
        return  buffdebufffoodname;
    }


    protected FileConfiguration config;

    private File potiontypeconfigfile;
    private FileConfiguration potiontypeconfig;

    private File messageconfigfile;
    private FileConfiguration messageconfig;


    @Override
    public void onEnable() {
        createFiles();
        createpotiontypefile();
        create_message_file();
        plugin = this;
        final int configVersion = plugin.getConfig().contains("config-version", true) ? plugin.getConfig().getInt("config-version") : -1;
        if (configVersion != 2) {
            getServer().getConsoleSender().sendMessage(ChatColor.RED + "[Consume Food] You are using the old config");
            getServer().getConsoleSender().sendMessage(ChatColor.RED + "[Consume Food] Created the latest config.yml after replacing the old config.yml with config_old.yml");
            replaceconfig();
            createFiles();
        } else {
            getServer().getConsoleSender().sendMessage(ChatColor.GREEN + "[Consume Food] You are using the latest version of config.yml");
        }
        foodnamelist();
        buff_food_list();
        getCommand("hunger").setExecutor(new HungerCommand());
        getCommand("saturation").setExecutor(new HungerCommand());
        getCommand("gethunger").setExecutor(new HungerCommand());
        getCommand("getsaturation").setExecutor(new HungerCommand());
        getServer().getPluginManager().registerEvents(new ConsumeFoodEvents(), this);
        getServer().getPluginManager().registerEvents(new Food_Interact_Event(), this);
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


    public void createpotiontypefile() {
        potiontypeconfigfile = new File(getDataFolder(), "potiontype.yml");
        if (!potiontypeconfigfile.exists()){
            potiontypeconfigfile.getParentFile().mkdirs();
            saveResource("potiontype.yml",false);
        }
        potiontypeconfig = new YamlConfiguration();
        try {
            potiontypeconfig.load(potiontypeconfigfile);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }

    public FileConfiguration getpotiondata() {
        return this.potiontypeconfig;
    }

    public void create_message_file() {
        messageconfigfile = new File(getDataFolder(), "message.yml");
        if (!messageconfigfile.exists()){
            messageconfigfile.getParentFile().mkdirs();
            saveResource("message.yml", false);
        }
        messageconfig = new YamlConfiguration();
        try {
            messageconfig.load(messageconfigfile);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }

    public FileConfiguration getmessageconfig() {
        return this.messageconfig;
    }

    public void replaceconfig() {
        File file = new File(getDataFolder(), "config.yml");
        this.config = YamlConfiguration.loadConfiguration(file);
        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        File config_old = new File(getDataFolder(),"config_old-" + dateFormat.format(date) + ".yml");
        file.renameTo(config_old);
        getServer().getConsoleSender().sendMessage(ChatColor.GREEN + "[Consume Food] Plugin replaced the old config.yml with config_old.yml and created a new config.yml");
    }


    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!sender.hasPermission("consumefood.reload")) {
            sender.sendMessage(ChatColor.RED + "You do not have permission!");
        }
        if (cmd.getName().equalsIgnoreCase("consumefoodreload")) {
            if (args.length == 0) {
                plugin.reloadConfig();
                foodnamelist();
                buff_food_list();
                messageconfig = YamlConfiguration.loadConfiguration(messageconfigfile);
                sender.sendMessage(ChatColor.GREEN + "Reloaded [Consume Food] Plugin Config");
                getServer().getConsoleSender().sendMessage(ChatColor.GREEN + "Reloaded [Consume Food] Plugin Config");
            }
            if (args.length >= 1) {
                sender.sendMessage(ChatColor.RED + "/consumefoodreload");
            }
        }
        return true;
    }

}


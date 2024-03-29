package me.msicraft.consumefood;

import me.msicraft.consumefood.API.Util.BukkitChecker;
import me.msicraft.consumefood.API.Util.Util;
import me.msicraft.consumefood.Command.MainCommand;
import me.msicraft.consumefood.Command.MainTabComplete;
import me.msicraft.consumefood.Compatibility.PlaceholderApi.ConsumeFoodPlaceholder;
import me.msicraft.consumefood.Compatibility.QuestOrAchievementEvent;
import me.msicraft.consumefood.CustomFood.Action.ActionUtil;
import me.msicraft.consumefood.CustomFood.CustomFoodUtil;
import me.msicraft.consumefood.CustomFood.Event.*;
import me.msicraft.consumefood.Enum.CustomFoodEditEnum;
import me.msicraft.consumefood.Event.JoinQuitEvent;
import me.msicraft.consumefood.File.CustomFoodConfig;
import me.msicraft.consumefood.File.MessageConfig;
import me.msicraft.consumefood.File.PlayerData;
import me.msicraft.consumefood.FoodDiet.FoodDietUtil;
import me.msicraft.consumefood.PlayerHunger.Event.PlayerHungerEvent;
import me.msicraft.consumefood.PlayerHunger.PlayerHungerUtil;
import me.msicraft.consumefood.PlayerHunger.Task.PlayerHungerTask;
import me.msicraft.consumefood.VanillaFood.Event.FoodConsumeEvent;
import me.msicraft.consumefood.VanillaFood.VanillaFoodUtil;
import me.msicraft.consumefood.bstat.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Pattern;

public final class ConsumeFood extends JavaPlugin {

    private static ConsumeFood plugin;
    public static int bukkitBrandType; // 0 = can use paper-api | 1 = can't use paper-api

    protected FileConfiguration config;

    private final Pattern pattern = Pattern.compile("#[a-fA-F0-9]{6}");
    public Pattern getPattern() {
        return pattern;
    }

    public static boolean canUsePlaceHolderApi;
    public static boolean isQuestOrAchievementCompatibility;

    public static String prefix = "[ConsumeFood]";
    public static CustomFoodConfig customFoodConfig;
    public static MessageConfig messageConfig;
    public static PlayerData playerData;

    public static ConsumeFood getPlugin() {
        return plugin;
    }

    private final VanillaFoodUtil vanillaFoodUtil = new VanillaFoodUtil();
    private final CustomFoodUtil customFoodUtil = new CustomFoodUtil();
    private final Util util = new Util();

    public HashMap<UUID, String> customFoodPage = new HashMap<>();
    public HashMap<UUID, String> customFoodMaxPage = new HashMap<>();
    public static final HashMap<UUID, HashMap<CustomFoodEditEnum, Boolean>> editCustomFood = new HashMap<>();
    public static final HashMap<UUID, String> editingCustomFood = new HashMap<>();

    private final PlayerHungerUtil playerHungerUtil = new PlayerHungerUtil();
    public static final Map<UUID, Integer> customFoodLevel = new HashMap<>();

    public static HashMap<CustomFoodEditEnum, Boolean> getCustomFoodMap(Player player) {
        HashMap<CustomFoodEditEnum, Boolean> temp = new HashMap<>();
        if (editCustomFood.containsKey(player.getUniqueId())) {
            temp = editCustomFood.get(player.getUniqueId());
        }
        return temp;
    }

    public static void setCustomFoodEditMap(Player player, CustomFoodEditEnum customFoodEditEnum, boolean b) {
        HashMap<CustomFoodEditEnum, Boolean> map = getCustomFoodMap(player);
        map.put(customFoodEditEnum, b);
    }

    public static boolean getCustomFoodEditValue(Player player, CustomFoodEditEnum customFoodEditEnum) {
        boolean check = false;
        HashMap<CustomFoodEditEnum, Boolean> map = getCustomFoodMap(player);
        if (map.containsKey(customFoodEditEnum)) {
            check = map.get(customFoodEditEnum);
        }
        return check;
    }

    @Override
    public void onEnable() {
        createConfigFiles();
        plugin = this;
        customFoodConfig = new CustomFoodConfig(this);
        messageConfig = new MessageConfig(this);
        playerData = new PlayerData(this);
        final int configVersion = plugin.getConfig().contains("config-version", true) ? plugin.getConfig().getInt("config-version") : -1;
        if (configVersion != 5) {
            getServer().getConsoleSender().sendMessage(ChatColor.GREEN + prefix + ChatColor.RED + " You are using the old config");
            getServer().getConsoleSender().sendMessage(ChatColor.GREEN + prefix + ChatColor.RED + " Created the latest config.yml after replacing the old config.yml with config_old.yml");
            replaceConfig();
            createConfigFiles();
        } else {
            getServer().getConsoleSender().sendMessage(ChatColor.GREEN + prefix + " You are using the latest version of config.yml");
        }
        final int customConfigVersion = customFoodConfig.getConfig().contains("config-version", true) ? customFoodConfig.getConfig().getInt("config-version") : -1;
        if (customConfigVersion != 3) {
            getServer().getConsoleSender().sendMessage(ChatColor.GREEN + prefix + ChatColor.RED + " You are using the old customfood config");
            getServer().getConsoleSender().sendMessage(ChatColor.GREEN + prefix + ChatColor.RED + " Created the latest customfood.yml after replacing the old customfood.yml with customfood_old.yml");
            replaceCustomFoodConfig();
            customFoodConfig = new CustomFoodConfig(this);
        } else {
            getServer().getConsoleSender().sendMessage(ChatColor.GREEN + prefix + " You are using the latest version of customfood.yml");
        }
        configFilesReload();
        eventRegister();
        try {
            BukkitChecker.canUsePaperApiTest();
            Bukkit.getConsoleSender().sendMessage(prefix + ChatColor.GREEN + " paper-api available");
            Bukkit.getPluginManager().registerEvents(new CustomFoodEditInvEvent(), this);
            bukkitBrandType = 0;
        } catch (NoSuchMethodError e) {
            Bukkit.getConsoleSender().sendMessage(prefix + ChatColor.RED + " paper-api not available");
            if (Bukkit.getPluginManager().getPlugin("NBTAPI") == null) {
                Bukkit.getConsoleSender().sendMessage(prefix + ChatColor.RED + " Requires NBT API plugin" + ChatColor.WHITE +" | Download here-> https://www.spigotmc.org/resources/nbt-api.7939/");
                getServer().getPluginManager().disablePlugin(this);
                return;
            } else {
                Bukkit.getConsoleSender().sendMessage(prefix + ChatColor.GREEN + " Detect NBTAPI plugin");
                Bukkit.getPluginManager().registerEvents(new CustomFoodEditInvEvent(), this);
                bukkitBrandType = 1;
            }
        }
        getCommand("consumefood").setExecutor(new MainCommand());
        getCommand("consumefood").setTabCompleter(new MainTabComplete());
        if (plugin.getConfig().getBoolean("Compatibility.PlaceholderAPI")) {
            if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
                canUsePlaceHolderApi = true;
                Bukkit.getConsoleSender().sendMessage(prefix + " Detect PlaceHolderApi plugin");
                new ConsumeFoodPlaceholder(this).register();
            }
        }
        applyMessageContentChange();
        applyConfigContentChange();
        Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + prefix + " Plugin Enable");
        Bukkit.getScheduler().runTask(ConsumeFood.getPlugin(), ()-> {
            HashMap<CustomFoodEditEnum, Boolean> temp = new HashMap<>();
            for (CustomFoodEditEnum em : CustomFoodEditEnum.values()) {
                temp.put(em, false);
            }
            for (Player player : Bukkit.getOnlinePlayers()) {
                if (player.isOnline() && player.isOp()) {
                    if (!editCustomFood.containsKey(player.getUniqueId())) {
                        editCustomFood.put(player.getUniqueId(), temp);
                    }
                    if (!editingCustomFood.containsKey(player.getUniqueId())) {
                        editingCustomFood.put(player.getUniqueId(), null);
                    }
                }
                if (player.isOnline()) {
                    playerHungerUtil.loadCustomFoodLevel(player);
                    BukkitTask hungerTask = new PlayerHungerTask(player).runTaskTimer(this, 20L, 20L);
                    //Bukkit.getConsoleSender().sendMessage("TaskId: " + hungerTask.getTaskId() + " | Player: " + player.getName());
                }
            }
        });
        int pluginId = 98139;
        new BukkitChecker(this, pluginId).getPluginUpdateCheck(version -> {
            if (this.getDescription().getVersion().equals(version)) {
                getLogger().info("There is not a new update available.");
            } else {
                getLogger().info("A new version of the plugin is available: (v" + version + "), Current: v" + getDescription().getVersion());
                getLogger().info("If the current version is higher, it is the development version.");
            }
        });
        Metrics metrics = new Metrics(this, 18144);
        getLogger().info("Enabled metrics. You may opt-out by changing plugins/bStats/config.yml");
    }

    @Override
    public void onDisable() {
        playerHungerUtil.saveAllCustomFoodMap();
        getServer().getConsoleSender().sendMessage(ChatColor.GREEN + prefix + ChatColor.RED +" Plugin Disable");
    }

    private void applyMessageContentChange() {
        boolean hasChange = false;
        if (!messageConfig.getConfig().contains("Permission-Error", true)) {
            messageConfig.getConfig().set("Permission-Error", "&cYou don't have permission");
            hasChange = true;
        }
        if (hasChange) {
            getServer().getConsoleSender().sendMessage(prefix + " Configuration in message.yml has been added");
            messageConfig.getConfig().options().header(
                    "If you don't need a message, leave a blank. " +
                            "Placeholders are available (ex. %player_name%)")
                    .copyDefaults(true);
            messageConfig.saveConfig();
        }
    }

    private void applyConfigContentChange() {
        boolean hasChange = false;
        if (!plugin.getConfig().contains("CustomSetting.Return-BowlOrBottle",true)) {
            plugin.getConfig().set("CustomSetting.Return-BowlOrBottle", true);
            hasChange = true;
        }
        if (hasChange) {
            getServer().getConsoleSender().sendMessage(prefix + " Configuration in config.yml has been added");
            saveConfig();
            reloadConfig();
        }
    }

    private void eventRegister() {
        PluginManager pluginManager = Bukkit.getPluginManager();
        if (ConsumeFood.getPlugin().getConfig().getBoolean("Compatibility.Quest-or-Achievement")) {
            isQuestOrAchievementCompatibility = true;
            pluginManager.registerEvents(new QuestOrAchievementEvent(), this);
            Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + prefix + " Quest-or-Achievement enabled");
        } else {
            isQuestOrAchievementCompatibility = false;
            pluginManager.registerEvents(new FoodConsumeEvent(), this);
        }
        pluginManager.registerEvents(new JoinQuitEvent(), this);
        pluginManager.registerEvents(new CustomFoodChatEditEvent(), this);
        pluginManager.registerEvents(new PlayerHungerEvent(), this);
        pluginManager.registerEvents(new CustomFoodEvent(), this);
        pluginManager.registerEvents(new CustomFoodBlockEvent(), this);
        pluginManager.registerEvents(new CustomFoodDisableUseRecipe(), this);
    }

    public void configFilesReload() {
        reloadConfig();
        customFoodConfig.reloadConfig();
        messageConfig.reloadConfig();
        customFoodUtil.playerHeadUUIDCheck();
        util.configDataCheck();
        ActionUtil.reloadVariables();
        if (isQuestOrAchievementCompatibility) {
            QuestOrAchievementEvent.reloadVariables();
        } else {
            FoodConsumeEvent.reloadVariables();
        }
        FoodConsumeEvent.reloadBasicVariables();
        CustomFoodEvent.reloadVariables();
        FoodDietUtil.reloadVariables();
    }

    private void createConfigFiles() {
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

    private void replaceConfig() {
        File file = new File(getDataFolder(), "config.yml");
        this.config = YamlConfiguration.loadConfiguration(file);
        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
        File config_old = new File(getDataFolder(),"config_old-" + dateFormat.format(date) + ".yml");
        file.renameTo(config_old);
        getServer().getConsoleSender().sendMessage(ChatColor.GREEN + prefix + " Plugin replaced the old config.yml with config_old.yml and created a new config.yml");
    }

    private void replaceCustomFoodConfig() {
        File file = new File(getDataFolder(), "customfood.yml");
        this.config = YamlConfiguration.loadConfiguration(file);
        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
        File customfood_config_old = new File(getDataFolder(),"customfood_old-" + dateFormat.format(date) + ".yml");
        file.renameTo(customfood_config_old);
        getServer().getConsoleSender().sendMessage(ChatColor.GREEN + prefix + " Plugin replaced the old customfood.yml with customfood_old.yml and created a new customfood.yml");
    }

}

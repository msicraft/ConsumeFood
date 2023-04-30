package me.msicraft.consumefood.CustomFood.Event;

import me.msicraft.consumefood.API.CustomEvent.CustomFoodConsumeEvent;
import me.msicraft.consumefood.API.Util.Util;
import me.msicraft.consumefood.Compatibility.PlaceholderApi.PlaceHolderApiUtil;
import me.msicraft.consumefood.ConsumeFood;
import me.msicraft.consumefood.CustomFood.CustomFoodUtil;
import me.msicraft.consumefood.PlayerHunger.PlayerHungerUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.UUID;

public class CustomFoodEvent implements Listener {

    private final CustomFoodUtil customFoodUtil = new CustomFoodUtil();
    private final Util util = new Util();
    private final PlayerHungerUtil playerHungerUtil = new PlayerHungerUtil();

    public final HashMap<UUID, Long> customFood_globalCooldownMap = new HashMap<>();
    public final HashMap<UUID, HashMap<String, Long>> customFood_personalCooldownMap = new HashMap<>();

    public static void reloadVariables() {
        isEnabledMaxConsumable = ConsumeFood.customFoodConfig.getConfig().contains("CustomFood-Max-Consumable.Enabled") && ConsumeFood.customFoodConfig.getConfig().getBoolean("CustomFood-Max-Consumable.Enabled");
    }

    @EventHandler
    public void onCustomFoodConsume(PlayerItemConsumeEvent e) {
        ItemStack consumeItemStack = e.getItem();
        if (customFoodUtil.isCustomFood(consumeItemStack)) {
            String internalName = customFoodUtil.getInternalName(consumeItemStack);
            if (internalName != null) {
                Player player = e.getPlayer();
                int maxFoodLevel = playerHungerUtil.getMaxFoodLevel();
                int foodLevel = customFoodUtil.getFoodLevel(internalName);
                float saturation = customFoodUtil.getSaturation(internalName);
                int calFoodLevel = player.getFoodLevel() + foodLevel;
                if (calFoodLevel > maxFoodLevel) {
                    calFoodLevel = maxFoodLevel;
                }
                float calSaturation = player.getSaturation() + saturation;
                if (calSaturation > playerHungerUtil.getMaxSaturation()) {
                    calSaturation = playerHungerUtil.getMaxSaturation();
                }
                String cdType = ConsumeFood.customFoodConfig.getConfig().getString("CustomFood-Cooldown-Setting.Type");
                if (cdType == null) {
                    cdType = "disable";
                }
                EquipmentSlot slot = customFoodUtil.getUseHand(player, consumeItemStack);
                e.setCancelled(true);
                long time = System.currentTimeMillis();
                switch (cdType) {
                    case "disable":
                        customFoodUtil.applyConsumeCustomFood(player, calFoodLevel, calSaturation, internalName, slot, consumeItemStack);
                        Bukkit.getPluginManager().callEvent(new CustomFoodConsumeEvent(player, foodLevel, saturation, slot, cdType, consumeItemStack, true));
                        break;
                    case "global":
                        long globalCooldown = ConsumeFood.customFoodConfig.getConfig().getLong("CustomFood-Cooldown-Setting.Global_Cooldown");
                        if (customFood_globalCooldownMap.containsKey(player.getUniqueId())) {
                            if (customFood_globalCooldownMap.get(player.getUniqueId()) > time) {
                                long globalTimeLeft = (customFood_globalCooldownMap.get(player.getUniqueId()) - time) / 1000;
                                String cooldownMessage = util.getCustomFoodGlobalCooldownMessage();
                                if (cooldownMessage != null && !cooldownMessage.equals("")) {
                                    cooldownMessage = cooldownMessage.replaceAll("<customfood_global_timeleft>", String.valueOf(globalTimeLeft));
                                    if (ConsumeFood.canUsePlaceHolderApi) {
                                        cooldownMessage = PlaceHolderApiUtil.getApplyPlaceHolder(player, cooldownMessage);
                                    }
                                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', cooldownMessage));
                                }
                                Bukkit.getPluginManager().callEvent(new CustomFoodConsumeEvent(player, foodLevel, saturation, slot, cdType, consumeItemStack, false));
                                return;
                            }
                        }
                        customFood_globalCooldownMap.put(player.getUniqueId(), time + (globalCooldown * 1000));
                        customFoodUtil.applyConsumeCustomFood(player, calFoodLevel, calSaturation, internalName, slot, consumeItemStack);
                        Bukkit.getPluginManager().callEvent(new CustomFoodConsumeEvent(player, foodLevel, saturation, slot, cdType, consumeItemStack, true));
                        break;
                    case "personal":
                        double personalCooldown = customFoodUtil.getPersonalCooldown(internalName);
                        HashMap<String, Long> temp = new HashMap<>();
                        String key = player.getUniqueId() + ":" + internalName;
                        if (customFood_personalCooldownMap.containsKey(player.getUniqueId())) {
                            temp = customFood_personalCooldownMap.get(player.getUniqueId());
                            if (temp.containsKey(key) && temp.get(key) > time) {
                                long personalTimeLeft = (temp.get(key) - time) / 1000;
                                String cooldownMessage = util.getCustomFoodPersonalCooldownMessage();
                                if (cooldownMessage != null && !cooldownMessage.equals("")) {
                                    cooldownMessage = cooldownMessage.replaceAll("<customfood_personal_timeleft>", String.valueOf(personalTimeLeft));
                                    cooldownMessage = cooldownMessage.replaceAll("<customfood_name>", customFoodUtil.getName(internalName));
                                    if (ConsumeFood.canUsePlaceHolderApi) {
                                        cooldownMessage = PlaceHolderApiUtil.getApplyPlaceHolder(player, cooldownMessage);
                                    }
                                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', cooldownMessage));
                                }
                                Bukkit.getPluginManager().callEvent(new CustomFoodConsumeEvent(player, foodLevel, saturation, slot, cdType, consumeItemStack, false));
                                return;
                            }
                        }
                        long cd = (long) (time + (personalCooldown * 1000));
                        temp.put(key, cd);
                        customFood_personalCooldownMap.put(player.getUniqueId(), temp);
                        customFoodUtil.applyConsumeCustomFood(player, calFoodLevel, calSaturation, internalName, slot, consumeItemStack);
                        Bukkit.getPluginManager().callEvent(new CustomFoodConsumeEvent(player, foodLevel, saturation, slot, cdType, consumeItemStack, true));
                        break;
                }
                playerHungerUtil.addCustomFoodLevel(player, foodLevel);
                playerHungerUtil.syncPlayerHunger(player);
                if (player.getFoodLevel() >= playerHungerUtil.getMaxFoodLevel()) {
                    String reachMessage = util.getReachMaxFoodLevel();
                    if (!reachMessage.equals("")) {
                        reachMessage = reachMessage.replaceAll("<max_foodlevel>", String.valueOf(playerHungerUtil.getMaxFoodLevel()));
                        if (ConsumeFood.canUsePlaceHolderApi) {
                            reachMessage = PlaceHolderApiUtil.getApplyPlaceHolder(player, reachMessage);
                        }
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', reachMessage));
                    }
                }
                if (player.getSaturation() >= playerHungerUtil.getMaxSaturation()) {
                    String reachMessage = util.getReachMaxSaturation();
                    if (!reachMessage.equals("")) {
                        reachMessage = reachMessage.replaceAll("<max_saturation>", String.valueOf(playerHungerUtil.getMaxSaturation()));
                        if (ConsumeFood.canUsePlaceHolderApi) {
                            reachMessage = PlaceHolderApiUtil.getApplyPlaceHolder(player, reachMessage);
                        }
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', reachMessage));
                    }
                }
            }
        }
    }

    private static boolean isEnabledMaxConsumable = false;

    @EventHandler
    public void playerMaxConsumeCustomFood(PlayerInteractEvent e) {
        ItemStack consumeItemStack = e.getItem();
        if (consumeItemStack != null && consumeItemStack.getType() != Material.AIR && customFoodUtil.isCustomFood(consumeItemStack)) {
            if (e.getAction() == Action.RIGHT_CLICK_AIR) {
                Player player = e.getPlayer();
                String internalName = customFoodUtil.getInternalName(consumeItemStack);
                if (internalName != null) {
                    int maxFoodLevel = playerHungerUtil.getMaxFoodLevel();
                    float maxSaturation = playerHungerUtil.getMaxSaturation();
                    int foodLevel = customFoodUtil.getFoodLevel(internalName);
                    float saturation = customFoodUtil.getSaturation(internalName);
                    int calFoodLevel = player.getFoodLevel() + foodLevel;
                    if (calFoodLevel > maxFoodLevel) {
                        calFoodLevel = maxFoodLevel;
                    }
                    float calSaturation = player.getSaturation() + saturation;
                    if (calSaturation > maxSaturation) {
                        calSaturation = maxSaturation;
                    }
                    String cdType = ConsumeFood.customFoodConfig.getConfig().getString("CustomFood-Cooldown-Setting.Type");
                    if (cdType == null) {
                        cdType = "disable";
                    }
                    EquipmentSlot slot = customFoodUtil.getUseHand(player, consumeItemStack);
                    long time = System.currentTimeMillis();
                    if (isEnabledMaxConsumable) {
                        if (player.getFoodLevel() >= 20) { //foodlevel >= 20
                            switch (cdType) {
                                case "disable":
                                    customFoodUtil.applyConsumeCustomFood(player, calFoodLevel, calSaturation, internalName, slot, consumeItemStack);
                                    Bukkit.getPluginManager().callEvent(new CustomFoodConsumeEvent(player, foodLevel, saturation, slot, cdType, consumeItemStack, true));
                                    break;
                                case "global":
                                    long globalCooldown = ConsumeFood.customFoodConfig.getConfig().getLong("CustomFood-Cooldown-Setting.Global_Cooldown");
                                    if (customFood_globalCooldownMap.containsKey(player.getUniqueId())) {
                                        if (customFood_globalCooldownMap.get(player.getUniqueId()) > time) {
                                            long globalTimeLeft = (customFood_globalCooldownMap.get(player.getUniqueId()) - time) / 1000;
                                            String cooldownMessage = util.getCustomFoodGlobalCooldownMessage();
                                            if (cooldownMessage != null && !cooldownMessage.equals("")) {
                                                cooldownMessage = cooldownMessage.replaceAll("<customfood_global_timeleft>", String.valueOf(globalTimeLeft));
                                                if (ConsumeFood.canUsePlaceHolderApi) {
                                                    cooldownMessage = PlaceHolderApiUtil.getApplyPlaceHolder(player, cooldownMessage);
                                                }
                                                player.sendMessage(ChatColor.translateAlternateColorCodes('&', cooldownMessage));
                                            }
                                            Bukkit.getPluginManager().callEvent(new CustomFoodConsumeEvent(player, foodLevel, saturation, slot, cdType, consumeItemStack, false));
                                            return;
                                        }
                                    }
                                    customFood_globalCooldownMap.put(player.getUniqueId(), time + (globalCooldown * 1000));
                                    customFoodUtil.applyConsumeCustomFood(player, calFoodLevel, calSaturation, internalName, slot, consumeItemStack);
                                    Bukkit.getPluginManager().callEvent(new CustomFoodConsumeEvent(player, foodLevel, saturation, slot, cdType, consumeItemStack, true));
                                    break;
                                case "personal":
                                    double personalCooldown = customFoodUtil.getPersonalCooldown(internalName);
                                    HashMap<String, Long> temp = new HashMap<>();
                                    String key = player.getUniqueId() + ":" + internalName;
                                    if (customFood_personalCooldownMap.containsKey(player.getUniqueId())) {
                                        temp = customFood_personalCooldownMap.get(player.getUniqueId());
                                        if (temp.containsKey(key) && temp.get(key) > time) {
                                            long personalTimeLeft = (temp.get(key) - time) / 1000;
                                            String cooldownMessage = util.getCustomFoodPersonalCooldownMessage();
                                            if (cooldownMessage != null && !cooldownMessage.equals("")) {
                                                cooldownMessage = cooldownMessage.replaceAll("<customfood_personal_timeleft>", String.valueOf(personalTimeLeft));
                                                cooldownMessage = cooldownMessage.replaceAll("<customfood_name>", customFoodUtil.getName(internalName));
                                                if (ConsumeFood.canUsePlaceHolderApi) {
                                                    cooldownMessage = PlaceHolderApiUtil.getApplyPlaceHolder(player, cooldownMessage);
                                                }
                                                player.sendMessage(ChatColor.translateAlternateColorCodes('&', cooldownMessage));
                                            }
                                            Bukkit.getPluginManager().callEvent(new CustomFoodConsumeEvent(player, foodLevel, saturation, slot, cdType, consumeItemStack, false));
                                            return;
                                        }
                                    }
                                    long cd = (long) (time + (personalCooldown * 1000));
                                    temp.put(key, cd);
                                    customFood_personalCooldownMap.put(player.getUniqueId(), temp);
                                    customFoodUtil.applyConsumeCustomFood(player, calFoodLevel, calSaturation, internalName, slot, consumeItemStack);
                                    Bukkit.getPluginManager().callEvent(new CustomFoodConsumeEvent(player, foodLevel, saturation, slot, cdType, consumeItemStack, true));
                                    break;
                            }
                            playerHungerUtil.addCustomFoodLevel(player, foodLevel);
                            playerHungerUtil.syncPlayerHunger(player);
                            if (player.getFoodLevel() >= playerHungerUtil.getMaxFoodLevel()) {
                                String reachMessage = util.getReachMaxFoodLevel();
                                if (!reachMessage.equals("")) {
                                    reachMessage = reachMessage.replaceAll("<max_foodlevel>", String.valueOf(playerHungerUtil.getMaxFoodLevel()));
                                    if (ConsumeFood.canUsePlaceHolderApi) {
                                        reachMessage = PlaceHolderApiUtil.getApplyPlaceHolder(player, reachMessage);
                                    }
                                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', reachMessage));
                                }
                            }
                            if (player.getSaturation() >= playerHungerUtil.getMaxSaturation()) {
                                String reachMessage = util.getReachMaxSaturation();
                                if (!reachMessage.equals("")) {
                                    reachMessage = reachMessage.replaceAll("<max_saturation>", String.valueOf(playerHungerUtil.getMaxSaturation()));
                                    if (ConsumeFood.canUsePlaceHolderApi) {
                                        reachMessage = PlaceHolderApiUtil.getApplyPlaceHolder(player, reachMessage);
                                    }
                                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', reachMessage));
                                }
                            }
                        } else { //foodlevel < 20
                            if (!consumeItemStack.getType().isEdible()) {
                                switch (cdType) {
                                    case "disable":
                                        customFoodUtil.applyConsumeCustomFood(player, calFoodLevel, calSaturation, internalName, slot, consumeItemStack);
                                        Bukkit.getPluginManager().callEvent(new CustomFoodConsumeEvent(player, calFoodLevel, calSaturation, slot, cdType, consumeItemStack, true));
                                        break;
                                    case "global":
                                        long globalCooldown = ConsumeFood.customFoodConfig.getConfig().getLong("CustomFood-Cooldown-Setting.Global_Cooldown");
                                        if (customFood_globalCooldownMap.containsKey(player.getUniqueId())) {
                                            if (customFood_globalCooldownMap.get(player.getUniqueId()) > time) {
                                                long globalTimeLeft = (customFood_globalCooldownMap.get(player.getUniqueId()) - time) / 1000;
                                                String cooldownMessage = util.getCustomFoodGlobalCooldownMessage();
                                                if (cooldownMessage != null && !cooldownMessage.equals("")) {
                                                    cooldownMessage = cooldownMessage.replaceAll("<customfood_global_timeleft>", String.valueOf(globalTimeLeft));
                                                    if (ConsumeFood.canUsePlaceHolderApi) {
                                                        cooldownMessage = PlaceHolderApiUtil.getApplyPlaceHolder(player, cooldownMessage);
                                                    }
                                                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', cooldownMessage));
                                                }
                                                Bukkit.getPluginManager().callEvent(new CustomFoodConsumeEvent(player, foodLevel, saturation, slot, cdType, consumeItemStack, false));
                                                return;
                                            }
                                        }
                                        customFood_globalCooldownMap.put(player.getUniqueId(), time + (globalCooldown * 1000));
                                        customFoodUtil.applyConsumeCustomFood(player, calFoodLevel, calSaturation, internalName, slot, consumeItemStack);
                                        Bukkit.getPluginManager().callEvent(new CustomFoodConsumeEvent(player, foodLevel, saturation, slot, cdType, consumeItemStack, true));
                                        break;
                                    case "personal":
                                        double personalCooldown = customFoodUtil.getPersonalCooldown(internalName);
                                        HashMap<String, Long> temp = new HashMap<>();
                                        String key = player.getUniqueId() + ":" + internalName;
                                        if (customFood_personalCooldownMap.containsKey(player.getUniqueId())) {
                                            temp = customFood_personalCooldownMap.get(player.getUniqueId());
                                            if (temp.containsKey(key) && temp.get(key) > time) {
                                                long personalTimeLeft = (temp.get(key) - time) / 1000;
                                                String cooldownMessage = util.getCustomFoodPersonalCooldownMessage();
                                                if (cooldownMessage != null && !cooldownMessage.equals("")) {
                                                    cooldownMessage = cooldownMessage.replaceAll("<customfood_personal_timeleft>", String.valueOf(personalTimeLeft));
                                                    cooldownMessage = cooldownMessage.replaceAll("<customfood_name>", customFoodUtil.getName(internalName));
                                                    if (ConsumeFood.canUsePlaceHolderApi) {
                                                        cooldownMessage = PlaceHolderApiUtil.getApplyPlaceHolder(player, cooldownMessage);
                                                    }
                                                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', cooldownMessage));
                                                }
                                                Bukkit.getPluginManager().callEvent(new CustomFoodConsumeEvent(player, foodLevel, saturation, slot, cdType, consumeItemStack, false));
                                                return;
                                            }
                                        }
                                        long cd = (long) (time + (personalCooldown * 1000));
                                        temp.put(key, cd);
                                        customFood_personalCooldownMap.put(player.getUniqueId(), temp);
                                        customFoodUtil.applyConsumeCustomFood(player, calFoodLevel, calSaturation, internalName, slot, consumeItemStack);
                                        Bukkit.getPluginManager().callEvent(new CustomFoodConsumeEvent(player, foodLevel, saturation, slot, cdType, consumeItemStack, true));
                                        break;
                                }
                                playerHungerUtil.addCustomFoodLevel(player, foodLevel);
                                playerHungerUtil.syncPlayerHunger(player);
                                if (player.getFoodLevel() >= playerHungerUtil.getMaxFoodLevel()) {
                                    String reachMessage = util.getReachMaxFoodLevel();
                                    if (!reachMessage.equals("")) {
                                        reachMessage = reachMessage.replaceAll("<max_foodlevel>", String.valueOf(playerHungerUtil.getMaxFoodLevel()));
                                        if (ConsumeFood.canUsePlaceHolderApi) {
                                            reachMessage = PlaceHolderApiUtil.getApplyPlaceHolder(player, reachMessage);
                                        }
                                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', reachMessage));
                                    }
                                }
                                if (player.getSaturation() >= playerHungerUtil.getMaxSaturation()) {
                                    String reachMessage = util.getReachMaxSaturation();
                                    if (!reachMessage.equals("")) {
                                        reachMessage = reachMessage.replaceAll("<max_saturation>", String.valueOf(playerHungerUtil.getMaxSaturation()));
                                        if (ConsumeFood.canUsePlaceHolderApi) {
                                            reachMessage = PlaceHolderApiUtil.getApplyPlaceHolder(player, reachMessage);
                                        }
                                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', reachMessage));
                                    }
                                }
                            }
                        }
                    } else {
                        if (player.getFoodLevel() < 20 && !consumeItemStack.getType().isEdible()) {
                            switch (cdType) {
                                case "disable":
                                    customFoodUtil.applyConsumeCustomFood(player, calFoodLevel, calSaturation, internalName, slot, consumeItemStack);
                                    Bukkit.getPluginManager().callEvent(new CustomFoodConsumeEvent(player, foodLevel, saturation, slot, cdType, consumeItemStack, true));
                                    break;
                                case "global":
                                    long globalCooldown = ConsumeFood.customFoodConfig.getConfig().getLong("CustomFood-Cooldown-Setting.Global_Cooldown");
                                    if (customFood_globalCooldownMap.containsKey(player.getUniqueId())) {
                                        if (customFood_globalCooldownMap.get(player.getUniqueId()) > time) {
                                            long globalTimeLeft = (customFood_globalCooldownMap.get(player.getUniqueId()) - time) / 1000;
                                            String cooldownMessage = util.getCustomFoodGlobalCooldownMessage();
                                            if (cooldownMessage != null && !cooldownMessage.equals("")) {
                                                cooldownMessage = cooldownMessage.replaceAll("<customfood_global_timeleft>", String.valueOf(globalTimeLeft));
                                                if (ConsumeFood.canUsePlaceHolderApi) {
                                                    cooldownMessage = PlaceHolderApiUtil.getApplyPlaceHolder(player, cooldownMessage);
                                                }
                                                player.sendMessage(ChatColor.translateAlternateColorCodes('&', cooldownMessage));
                                            }
                                            Bukkit.getPluginManager().callEvent(new CustomFoodConsumeEvent(player, foodLevel, saturation, slot, cdType, consumeItemStack, false));
                                            return;
                                        }
                                    }
                                    customFood_globalCooldownMap.put(player.getUniqueId(), time + (globalCooldown * 1000));
                                    customFoodUtil.applyConsumeCustomFood(player, calFoodLevel, calSaturation, internalName, slot, consumeItemStack);
                                    Bukkit.getPluginManager().callEvent(new CustomFoodConsumeEvent(player, foodLevel, saturation, slot, cdType, consumeItemStack, true));
                                    break;
                                case "personal":
                                    double personalCooldown = customFoodUtil.getPersonalCooldown(internalName);
                                    HashMap<String, Long> temp = new HashMap<>();
                                    String key = player.getUniqueId() + ":" + internalName;
                                    if (customFood_personalCooldownMap.containsKey(player.getUniqueId())) {
                                        temp = customFood_personalCooldownMap.get(player.getUniqueId());
                                        if (temp.containsKey(key) && temp.get(key) > time) {
                                            long personalTimeLeft = (temp.get(key) - time) / 1000;
                                            String cooldownMessage = util.getCustomFoodPersonalCooldownMessage();
                                            if (cooldownMessage != null && !cooldownMessage.equals("")) {
                                                cooldownMessage = cooldownMessage.replaceAll("<customfood_personal_timeleft>", String.valueOf(personalTimeLeft));
                                                cooldownMessage = cooldownMessage.replaceAll("<customfood_name>", customFoodUtil.getName(internalName));
                                                if (ConsumeFood.canUsePlaceHolderApi) {
                                                    cooldownMessage = PlaceHolderApiUtil.getApplyPlaceHolder(player, cooldownMessage);
                                                }
                                                player.sendMessage(ChatColor.translateAlternateColorCodes('&', cooldownMessage));
                                            }
                                            Bukkit.getPluginManager().callEvent(new CustomFoodConsumeEvent(player, foodLevel, saturation, slot, cdType, consumeItemStack, false));
                                            return;
                                        }
                                    }
                                    long cd = (long) (time + (personalCooldown * 1000));
                                    temp.put(key, cd);
                                    customFood_personalCooldownMap.put(player.getUniqueId(), temp);
                                    customFoodUtil.applyConsumeCustomFood(player, calFoodLevel, calSaturation, internalName, slot, consumeItemStack);
                                    Bukkit.getPluginManager().callEvent(new CustomFoodConsumeEvent(player, foodLevel, saturation, slot, cdType, consumeItemStack, true));
                                    break;
                            }
                            playerHungerUtil.addCustomFoodLevel(player, foodLevel);
                            playerHungerUtil.syncPlayerHunger(player);
                            if (player.getFoodLevel() >= playerHungerUtil.getMaxFoodLevel()) {
                                String reachMessage = util.getReachMaxFoodLevel();
                                if (!reachMessage.equals("")) {
                                    reachMessage = reachMessage.replaceAll("<max_foodlevel>", String.valueOf(playerHungerUtil.getMaxFoodLevel()));
                                    if (ConsumeFood.canUsePlaceHolderApi) {
                                        reachMessage = PlaceHolderApiUtil.getApplyPlaceHolder(player, reachMessage);
                                    }
                                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', reachMessage));
                                }
                            }
                            if (player.getSaturation() >= playerHungerUtil.getMaxSaturation()) {
                                String reachMessage = util.getReachMaxSaturation();
                                if (!reachMessage.equals("")) {
                                    reachMessage = reachMessage.replaceAll("<max_saturation>", String.valueOf(playerHungerUtil.getMaxSaturation()));
                                    if (ConsumeFood.canUsePlaceHolderApi) {
                                        reachMessage = PlaceHolderApiUtil.getApplyPlaceHolder(player, reachMessage);
                                    }
                                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', reachMessage));
                                }
                            }
                        }
                    }
                }
            }
        }
    }

}

package me.msicraft.consumefood.VanillaFood.Event;

import me.msicraft.consumefood.API.CustomEvent.VanillaFoodConsumeEvent;
import me.msicraft.consumefood.API.Util.Util;
import me.msicraft.consumefood.Compatibility.PlaceholderApi.PlaceHolderApiUtil;
import me.msicraft.consumefood.ConsumeFood;
import me.msicraft.consumefood.CustomFood.CustomFoodUtil;
import me.msicraft.consumefood.Enum.VanillaFoodEnum;
import me.msicraft.consumefood.PlayerHunger.PlayerHungerUtil;
import me.msicraft.consumefood.VanillaFood.VanillaFoodUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
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

public class FoodConsumeEvent implements Listener {

    private final VanillaFoodUtil vanillaFoodUtil = new VanillaFoodUtil();
    private final Util util = new Util();
    private final CustomFoodUtil customFoodUtil = new CustomFoodUtil();
    private final PlayerHungerUtil playerHungerUtil = new PlayerHungerUtil();

    public final HashMap<UUID, Long> vanillaFood_globalCooldownMap = new HashMap<>();
    public final HashMap<UUID, HashMap<String, Long>> vanillaFood_personalCooldownMap = new HashMap<>();

    @EventHandler
    public void onConsumeVanillaFood(PlayerItemConsumeEvent e) {
        ItemStack consumeItemStack = e.getItem();
        String foodName = consumeItemStack.getType().name().toUpperCase();
        if (vanillaFoodUtil.isVanillaFood(foodName, consumeItemStack) && !customFoodUtil.isCustomFood(consumeItemStack)) {
            VanillaFoodEnum vanillaFoodEnum = VanillaFoodEnum.valueOf(foodName);
            Player player = e.getPlayer();
            int maxFoodLevel = playerHungerUtil.getMaxFoodLevel();
            int foodLevel = vanillaFoodUtil.getFoodLevel(vanillaFoodEnum);
            float saturation = vanillaFoodUtil.getSaturation(vanillaFoodEnum);
            int calFoodLevel = player.getFoodLevel() + foodLevel;
            if (calFoodLevel > maxFoodLevel) {
                calFoodLevel = maxFoodLevel;
            }
            float calSaturation = player.getSaturation() + saturation;
            if (calSaturation > playerHungerUtil.getMaxSaturation()) {
                calSaturation = playerHungerUtil.getMaxSaturation();
            }
            String cdType = ConsumeFood.getPlugin().getConfig().getString("Cooldown-Setting.Type");;
            if (cdType == null) {
                cdType = "disable";
            }
            EquipmentSlot slot = vanillaFoodUtil.getUseHand(player, consumeItemStack);
            e.setCancelled(true);
            long time = System.currentTimeMillis();
            switch (cdType) {
                case "disable":
                    vanillaFoodUtil.applyConsumeFood(player, calFoodLevel, calSaturation, vanillaFoodEnum, slot, consumeItemStack);
                    Bukkit.getPluginManager().callEvent(new VanillaFoodConsumeEvent(player, consumeItemStack, vanillaFoodEnum, foodLevel, saturation, slot, cdType, true));
                    break;
                case "global":
                    long globalCooldown = ConsumeFood.getPlugin().getConfig().getLong("Cooldown-Setting.Global_Cooldown");
                    if (vanillaFood_globalCooldownMap.containsKey(player.getUniqueId())) {
                        if (vanillaFood_globalCooldownMap.get(player.getUniqueId()) > time) {
                            long globalTimeLeft = (vanillaFood_globalCooldownMap.get(player.getUniqueId()) - time) / 1000;
                            String cooldownMessage = util.getVanillaGlobalCooldownMessage();
                            if (cooldownMessage != null && !cooldownMessage.equals("")) {
                                cooldownMessage = cooldownMessage.replaceAll("<vanilla_global_timeleft>", String.valueOf(globalTimeLeft));
                                if (ConsumeFood.canUsePlaceHolderApi) {
                                    cooldownMessage = PlaceHolderApiUtil.getApplyPlaceHolder(player, cooldownMessage);
                                }
                                player.sendMessage(ChatColor.translateAlternateColorCodes('&', cooldownMessage));
                            }
                            Bukkit.getPluginManager().callEvent(new VanillaFoodConsumeEvent(player, consumeItemStack, vanillaFoodEnum, foodLevel, saturation, slot, cdType, false));
                            return;
                        }
                    }
                    vanillaFood_globalCooldownMap.put(player.getUniqueId(), time + (globalCooldown * 1000));
                    vanillaFoodUtil.applyConsumeFood(player, calFoodLevel, calSaturation, vanillaFoodEnum, slot, consumeItemStack);
                    Bukkit.getPluginManager().callEvent(new VanillaFoodConsumeEvent(player, consumeItemStack, vanillaFoodEnum, foodLevel, saturation, slot, cdType, true));
                    break;
                case "personal":
                    double personalCooldown = vanillaFoodUtil.getPersonalCooldown(vanillaFoodEnum);
                    HashMap<String, Long> temp = new HashMap<>();
                    String key = player.getUniqueId() + ":" + foodName;
                    if (vanillaFood_personalCooldownMap.containsKey(player.getUniqueId())) {
                        temp = vanillaFood_personalCooldownMap.get(player.getUniqueId());
                        if (temp.containsKey(key) && temp.get(key) > time) {
                            long personalTimeLeft = (temp.get(key) - time) / 1000;
                            String cooldownMessage = util.getVanillaPersonalCooldownMessage();
                            if (cooldownMessage != null && !cooldownMessage.equals("")) {
                                cooldownMessage = cooldownMessage.replaceAll("<vanilla_personal_timeleft>", String.valueOf(personalTimeLeft));
                                cooldownMessage = cooldownMessage.replaceAll("<food_name>", foodName);
                                if (ConsumeFood.canUsePlaceHolderApi) {
                                    cooldownMessage = PlaceHolderApiUtil.getApplyPlaceHolder(player, cooldownMessage);
                                }
                                player.sendMessage(ChatColor.translateAlternateColorCodes('&', cooldownMessage));
                            }
                            Bukkit.getPluginManager().callEvent(new VanillaFoodConsumeEvent(player, consumeItemStack, vanillaFoodEnum, foodLevel, saturation, slot, cdType, false));
                            return;
                        }
                    }
                    long cd = (long) (time + (personalCooldown * 1000));
                    temp.put(key, cd);
                    vanillaFood_personalCooldownMap.put(player.getUniqueId(), temp);
                    vanillaFoodUtil.applyConsumeFood(player, calFoodLevel, calSaturation, vanillaFoodEnum, slot, consumeItemStack);
                    Bukkit.getPluginManager().callEvent(new VanillaFoodConsumeEvent(player, consumeItemStack, vanillaFoodEnum, foodLevel, saturation, slot, cdType, true));
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

    @EventHandler
    public void playerMaxConsumeVanillaFood(PlayerInteractEvent e) {
        boolean isMaxConsumable = ConsumeFood.getPlugin().getConfig().getBoolean("Max-Consumable.Enabled");
        if (isMaxConsumable) {
            ItemStack consumeItemStack = e.getItem();
            if (consumeItemStack != null && consumeItemStack.getType() != Material.AIR) {
                Player player = e.getPlayer();
                String foodName = consumeItemStack.getType().name().toUpperCase();
                if (!customFoodUtil.isCustomFood(consumeItemStack) && vanillaFoodUtil.isVanillaFood(foodName, consumeItemStack) && player.getFoodLevel() >= 20) {
                    if (e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) {
                        VanillaFoodEnum vanillaFoodEnum = VanillaFoodEnum.valueOf(foodName);
                        int maxFoodLevel = playerHungerUtil.getMaxFoodLevel();
                        float maxSaturation = playerHungerUtil.getMaxSaturation();
                        int foodLevel = vanillaFoodUtil.getFoodLevel(vanillaFoodEnum);
                        float saturation = vanillaFoodUtil.getSaturation(vanillaFoodEnum);
                        int calFoodLevel = player.getFoodLevel() + foodLevel;
                        if (calFoodLevel > maxFoodLevel) {
                            calFoodLevel = maxFoodLevel;
                        }
                        float calSaturation = player.getSaturation() + saturation;
                        if (calSaturation > maxSaturation) {
                            calSaturation = maxSaturation;
                        }
                        String cdType = ConsumeFood.getPlugin().getConfig().getString("Cooldown-Setting.Type");;
                        if (cdType == null) {
                            cdType = "disable";
                        }
                        EquipmentSlot slot = vanillaFoodUtil.getUseHand(player, consumeItemStack);
                        e.setCancelled(true);
                        long time = System.currentTimeMillis();
                        switch (cdType) {
                            case "disable":
                                vanillaFoodUtil.applyConsumeFood(player, calFoodLevel, calSaturation, vanillaFoodEnum, slot, consumeItemStack);
                                player.playSound(player.getLocation(), Sound.ENTITY_GENERIC_EAT, 1, 1);
                                Bukkit.getPluginManager().callEvent(new VanillaFoodConsumeEvent(player, consumeItemStack, vanillaFoodEnum, foodLevel, saturation, slot, cdType, true));
                                break;
                            case "global":
                                long globalCooldown = ConsumeFood.getPlugin().getConfig().getLong("Cooldown-Setting.Global_Cooldown");
                                if (vanillaFood_globalCooldownMap.containsKey(player.getUniqueId())) {
                                    if (vanillaFood_globalCooldownMap.get(player.getUniqueId()) > time) {
                                        long globalTimeLeft = (vanillaFood_globalCooldownMap.get(player.getUniqueId()) - time) / 1000;
                                        String cooldownMessage = util.getVanillaGlobalCooldownMessage();
                                        if (cooldownMessage != null && !cooldownMessage.equals("")) {
                                            cooldownMessage = cooldownMessage.replaceAll("<vanilla_global_timeleft>", String.valueOf(globalTimeLeft));
                                            if (ConsumeFood.canUsePlaceHolderApi) {
                                                cooldownMessage = PlaceHolderApiUtil.getApplyPlaceHolder(player, cooldownMessage);
                                            }
                                            player.sendMessage(ChatColor.translateAlternateColorCodes('&', cooldownMessage));
                                        }
                                        Bukkit.getPluginManager().callEvent(new VanillaFoodConsumeEvent(player, consumeItemStack, vanillaFoodEnum, foodLevel, saturation, slot, cdType, false));
                                        return;
                                    }
                                }
                                vanillaFood_globalCooldownMap.put(player.getUniqueId(), time + (globalCooldown * 1000));
                                vanillaFoodUtil.applyConsumeFood(player, calFoodLevel, calSaturation, vanillaFoodEnum, slot, consumeItemStack);
                                player.playSound(player.getLocation(), Sound.ENTITY_GENERIC_EAT, 1, 1);
                                Bukkit.getPluginManager().callEvent(new VanillaFoodConsumeEvent(player, consumeItemStack, vanillaFoodEnum, foodLevel, saturation, slot, cdType, true));
                                break;
                            case "personal":
                                double personalCooldown = vanillaFoodUtil.getPersonalCooldown(vanillaFoodEnum);
                                HashMap<String, Long> temp = new HashMap<>();
                                String key = player.getUniqueId() + ":" + foodName;
                                if (vanillaFood_personalCooldownMap.containsKey(player.getUniqueId())) {
                                    temp = vanillaFood_personalCooldownMap.get(player.getUniqueId());
                                    if (temp.containsKey(key) && temp.get(key) > time) {
                                        long personalTimeLeft = (temp.get(key) - time) / 1000;
                                        String cooldownMessage = util.getVanillaPersonalCooldownMessage();
                                        if (cooldownMessage != null && !cooldownMessage.equals("")) {
                                            cooldownMessage = cooldownMessage.replaceAll("<vanilla_personal_timeleft>", String.valueOf(personalTimeLeft));
                                            cooldownMessage = cooldownMessage.replaceAll("<food_name>", foodName);
                                            if (ConsumeFood.canUsePlaceHolderApi) {
                                                cooldownMessage = PlaceHolderApiUtil.getApplyPlaceHolder(player, cooldownMessage);
                                            }
                                            player.sendMessage(ChatColor.translateAlternateColorCodes('&', cooldownMessage));
                                        }
                                        Bukkit.getPluginManager().callEvent(new VanillaFoodConsumeEvent(player, consumeItemStack, vanillaFoodEnum, foodLevel, saturation, slot, cdType, false));
                                        return;
                                    }
                                }
                                long cd = (long) (time + (personalCooldown * 1000));
                                temp.put(key, cd);
                                vanillaFood_personalCooldownMap.put(player.getUniqueId(), temp);
                                vanillaFoodUtil.applyConsumeFood(player, calFoodLevel, calSaturation, vanillaFoodEnum, slot, consumeItemStack);
                                Bukkit.getPluginManager().callEvent(new VanillaFoodConsumeEvent(player, consumeItemStack, vanillaFoodEnum, foodLevel, saturation, slot, cdType, true));
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

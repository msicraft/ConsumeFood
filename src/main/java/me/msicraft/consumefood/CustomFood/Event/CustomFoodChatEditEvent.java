package me.msicraft.consumefood.CustomFood.Event;

import me.msicraft.consumefood.CustomFood.CustomFoodUtil;
import me.msicraft.consumefood.ConsumeFood;
import me.msicraft.consumefood.CustomFood.Inventory.CustomFoodEditInv;
import me.msicraft.consumefood.Enum.CustomFoodEditEnum;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.List;

public class CustomFoodChatEditEvent implements Listener {

    private final CustomFoodUtil customFoodUtil = new CustomFoodUtil();

    @EventHandler
    public void createCustomFood(AsyncPlayerChatEvent e) {
        Player player = e.getPlayer();
        if (ConsumeFood.getCustomFoodEditValue(player, CustomFoodEditEnum.isCreate)) {
            String message = e.getMessage();
            e.setCancelled(true);
            CustomFoodEditInv customFoodEditInv = new CustomFoodEditInv(player);
            if (message.equals("cancel")) {
                Bukkit.getScheduler().runTask(ConsumeFood.getPlugin(), ()-> {
                    player.openInventory(customFoodEditInv.getInventory());
                    customFoodEditInv.setMainInv(player);
                });
            } else {
                message = message.replaceAll("[\\s]", "_");
                if (ConsumeFood.customFoodConfig.getConfig().contains("CustomFood." + message)) {
                    player.sendMessage(ChatColor.RED + "This internal name already exists.");
                } else {
                    ConsumeFood.customFoodConfig.getConfig().set("CustomFood." + message + ".Material", "APPLE");
                    ConsumeFood.customFoodConfig.getConfig().set("CustomFood." + message + ".Name", "");
                    ConsumeFood.customFoodConfig.getConfig().set("CustomFood." + message + ".FoodLevel", 0);
                    ConsumeFood.customFoodConfig.getConfig().set("CustomFood." + message + ".Saturation", 0);
                    ConsumeFood.customFoodConfig.getConfig().set("CustomFood." + message + ".Cooldown", 0);
                    ConsumeFood.customFoodConfig.saveConfig();
                }
                Bukkit.getScheduler().runTask(ConsumeFood.getPlugin(), ()-> {
                    player.openInventory(customFoodEditInv.getInventory());
                    customFoodEditInv.setMainInv(player);
                });
            }
            ConsumeFood.setCustomFoodEditMap(player, CustomFoodEditEnum.isCreate, false);
        }
    }

    @EventHandler
    public void customFoodEdit(AsyncPlayerChatEvent e) {
        Player player = e.getPlayer();
        if (!ConsumeFood.editingCustomFood.containsKey(player.getUniqueId())) {
            ConsumeFood.editingCustomFood.put(player.getUniqueId(), null);
        }
        if (ConsumeFood.getCustomFoodEditValue(player, CustomFoodEditEnum.isEnabled)) {
            String message = e.getMessage();
            e.setCancelled(true);
            String internalName = ConsumeFood.editingCustomFood.get(player.getUniqueId());
            if (internalName == null) {
                ConsumeFood.setCustomFoodEditMap(player, CustomFoodEditEnum.isEnabled, false);
                Bukkit.getConsoleSender().sendMessage(ConsumeFood.prefix + ChatColor.RED + " Internal name not found " + ChatColor.WHITE + " | Player: " + player.getName());
                return;
            }
            CustomFoodEditEnum customFoodEditEnum = null;
            for (CustomFoodEditEnum em : CustomFoodEditEnum.values()) {
                if (em != CustomFoodEditEnum.isCreate && em != CustomFoodEditEnum.isEnabled) {
                    if (ConsumeFood.getCustomFoodEditValue(player, em)) {
                        customFoodEditEnum = em;
                        break;
                    }
                }
            }
            if (customFoodEditEnum == null) {
                ConsumeFood.setCustomFoodEditMap(player, CustomFoodEditEnum.isEnabled, false);
                Bukkit.getConsoleSender().sendMessage(ConsumeFood.prefix + ChatColor.RED + " Internal name not found " + ChatColor.WHITE + " | Player: " + player.getName());
                return;
            }
            CustomFoodEditInv customFoodEditInv = new CustomFoodEditInv(player);
            if (!message.equals("cancel")) {
                switch (customFoodEditEnum) {
                    case Material:
                        try {
                            Material material = Material.valueOf(message.toUpperCase());
                            ConsumeFood.customFoodConfig.getConfig().set("CustomFood." + internalName + "." + customFoodEditEnum.name(), material.name());
                        } catch (IllegalArgumentException | NullPointerException ex) {
                            player.sendMessage(ChatColor.RED + "Invalid material: " + ChatColor.WHITE + message);
                        }
                        break;
                    case Name:
                    case TextureValue:
                        ConsumeFood.customFoodConfig.getConfig().set("CustomFood." + internalName + "." + customFoodEditEnum.name(), message);
                        break;
                    case Cooldown:
                    case FoodLevel:
                    case Saturation:
                        ConsumeFood.customFoodConfig.getConfig().set("CustomFood." + internalName + "." + customFoodEditEnum.name(), Double.parseDouble(message));
                        break;
                    case CustomModelData:
                        ConsumeFood.customFoodConfig.getConfig().set("CustomFood." + internalName + ".Data", Integer.parseInt(message.replaceAll("[^0-9]", "")));
                        break;
                    case Lore:
                        List<String> getLore = customFoodUtil.getLore(internalName);
                        getLore.add(message);
                        ConsumeFood.customFoodConfig.getConfig().set("CustomFood." + internalName + "." + customFoodEditEnum.name(), getLore);
                        break;
                    case PotionEffect:
                        List<String> getPotionEffects = customFoodUtil.getPotionEffectList(internalName);
                        getPotionEffects.add(message);
                        ConsumeFood.customFoodConfig.getConfig().set("CustomFood." + internalName + "." + customFoodEditEnum.name(), getPotionEffects);
                        break;
                    case Command:
                        List<String> getCommands = customFoodUtil.getCommandList(internalName);
                        getCommands.add(message);
                        ConsumeFood.customFoodConfig.getConfig().set("CustomFood." + internalName + "." + customFoodEditEnum.name(), getCommands);
                        break;
                }
                ConsumeFood.customFoodConfig.saveConfig();
            }
            ConsumeFood.setCustomFoodEditMap(player, CustomFoodEditEnum.isEnabled, false);
            ConsumeFood.setCustomFoodEditMap(player, customFoodEditEnum, false);
            Bukkit.getScheduler().runTask(ConsumeFood.getPlugin(), ()-> {
                player.openInventory(customFoodEditInv.getInventory());
                customFoodEditInv.editInv(internalName);
            });
        }
    }

}

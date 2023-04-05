package me.msicraft.consumefood.CustomFood.Event;

import me.msicraft.consumefood.ConsumeFood;
import me.msicraft.consumefood.CustomFood.CustomFoodUtil;
import me.msicraft.consumefood.CustomFood.Inventory.CustomFoodEditInv;
import me.msicraft.consumefood.Enum.CustomFoodEditEnum;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class CustomFoodEditInvEvent implements Listener {

    private final CustomFoodUtil customFoodUtil = new CustomFoodUtil();
    public HashMap<UUID, String> page_count = new HashMap<>();

    @EventHandler
    public void onClick(InventoryClickEvent e) {
        if (e.getClickedInventory() == null) { return; }
        if (e.getView().getTitle().equalsIgnoreCase("Custom Food")) {
            e.setCancelled(true);
            Player player = (Player) e.getWhoClicked();
            if (e.getCurrentItem() == null) { return; }
            ItemStack selectItem = e.getCurrentItem();
            ItemMeta selectItemMeta = selectItem.getItemMeta();
            if (selectItemMeta == null) { return; }
            PersistentDataContainer selectItemData = selectItemMeta.getPersistentDataContainer();
            CustomFoodEditInv customFoodEditInv = new CustomFoodEditInv(player);
            ClickType clickType = e.getClick();
            if (selectItemData.has(new NamespacedKey(ConsumeFood.getPlugin(), "CustomFood-Edit"), PersistentDataType.STRING)) {
                String var = selectItemData.get(new NamespacedKey(ConsumeFood.getPlugin(), "CustomFood-Edit"), PersistentDataType.STRING);
                if (var != null) {
                    String maxPageObject = ConsumeFood.getPlugin().customFoodMaxPage.get(player.getUniqueId());
                    int maxPageCount = 0;
                    if (maxPageObject != null) {
                        String[] a = maxPageObject.split(":");
                        maxPageCount = Integer.parseInt(a[1]);
                    }
                    page_count.putIfAbsent(player.getUniqueId(), "page:0");
                    switch (var) {
                        case "next": {
                            String currentPageObject = page_count.get(player.getUniqueId());
                            int currentPage = 0;
                            if (currentPageObject != null) {
                                String[] a = currentPageObject.split(":");
                                currentPage = Integer.parseInt(a[1]);
                            }
                            int nextPage = currentPage + 1;
                            if (nextPage > maxPageCount) {
                                nextPage = 0;
                            }
                            String value = "page:" + nextPage;
                            page_count.put(player.getUniqueId(), value);
                            ConsumeFood.getPlugin().customFoodPage.put(player.getUniqueId(), value);
                            player.openInventory(customFoodEditInv.getInventory());
                            customFoodEditInv.setMainInv(player);
                            break;
                        }
                        case "previous": {
                            String currentPageObject = page_count.get(player.getUniqueId());
                            int currentPage = 0;
                            if (currentPageObject != null) {
                                String[] a = currentPageObject.split(":");
                                currentPage = Integer.parseInt(a[1]);
                            }
                            int nextPage = currentPage - 1;
                            if (nextPage < 0) {
                                nextPage = maxPageCount;
                            }
                            String value = "page:" + nextPage;
                            page_count.put(player.getUniqueId(), value);
                            ConsumeFood.getPlugin().customFoodPage.put(player.getUniqueId(), value);
                            player.openInventory(customFoodEditInv.getInventory());
                            customFoodEditInv.setMainInv(player);
                            break;
                        }
                        case "create":
                            player.closeInventory();
                            player.sendMessage(ChatColor.YELLOW + "========================================");
                            player.sendMessage(ChatColor.GRAY + " Please enter internal_name");
                            player.sendMessage(ChatColor.GRAY + " Cancel when entering 'cancel'");
                            player.sendMessage(ChatColor.YELLOW + "========================================");
                            ConsumeFood.setCustomFoodEditMap(player, CustomFoodEditEnum.isCreate, true);
                            break;
                    }
                }
            } else if (selectItemData.has(new NamespacedKey(ConsumeFood.getPlugin(), "ConsumeFood-CustomFood-Edit"), PersistentDataType.STRING)) {
                String internalName = selectItemData.get(new NamespacedKey(ConsumeFood.getPlugin(), "ConsumeFood-CustomFood-Edit"), PersistentDataType.STRING);
                if (internalName != null) {
                    switch (clickType) {
                        case LEFT:
                            player.openInventory(customFoodEditInv.getInventory());
                            customFoodEditInv.editInv(internalName);
                            break;
                        case RIGHT:
                            ItemStack getStack = customFoodUtil.getCustomFood(internalName, ConsumeFood.bukkitBrandType);
                            if (getStack != null) {
                                player.getInventory().addItem(getStack);
                            }
                            break;
                        case SHIFT_LEFT:
                            ConsumeFood.customFoodConfig.getConfig().set("CustomFood." + internalName, null);
                            ConsumeFood.customFoodConfig.saveConfig();
                            player.openInventory(customFoodEditInv.getInventory());
                            customFoodEditInv.setMainInv(player);
                            break;
                    }
                }
            } else if (selectItemData.has(new NamespacedKey(ConsumeFood.getPlugin(), "ConsumeFood-Edit-Var"), PersistentDataType.STRING)) {
                String var = selectItemData.get(new NamespacedKey(ConsumeFood.getPlugin(), "ConsumeFood-Edit-Var"), PersistentDataType.STRING);
                if (var != null) {
                    if (var.equals("Back")) {
                        player.openInventory(customFoodEditInv.getInventory());
                        customFoodEditInv.setMainInv(player);
                    } else {
                        if (clickType == ClickType.LEFT || clickType == ClickType.RIGHT) {
                            String internalName = null;
                            ItemStack editingItem = e.getView().getTopInventory().getItem(4);
                            if (editingItem != null) {
                                ItemMeta itemMeta = editingItem.getItemMeta();
                                PersistentDataContainer data = itemMeta.getPersistentDataContainer();
                                internalName = data.get(new NamespacedKey(ConsumeFood.getPlugin(), "ConsumeFood-CustomFood-Editing"), PersistentDataType.STRING);
                            }
                            if (internalName != null) {
                                switch (var) {
                                    case "Material":
                                        if (clickType == ClickType.LEFT) {
                                            player.sendMessage(ChatColor.YELLOW + "========================================");
                                            player.sendMessage(ChatColor.GRAY + " Please enter Material");
                                            player.sendMessage(ChatColor.GRAY + " Cancel when entering 'cancel'");
                                            player.sendMessage(ChatColor.YELLOW + "========================================");
                                        } else {
                                            ConsumeFood.customFoodConfig.getConfig().set("CustomFood." + internalName + "." + var, "stone");
                                            ConsumeFood.customFoodConfig.saveConfig();
                                        }
                                        break;
                                    case "TextureValue":
                                        if (clickType == ClickType.LEFT) {
                                            player.sendMessage(ChatColor.YELLOW + "========================================");
                                            player.sendMessage(ChatColor.GRAY + " Please enter Texture value");
                                            player.sendMessage(ChatColor.GRAY + " Cancel when entering 'cancel'");
                                            player.sendMessage(ChatColor.YELLOW + "========================================");
                                        } else {
                                            ConsumeFood.customFoodConfig.getConfig().set("CustomFood." + internalName + "." + var, "");
                                            ConsumeFood.customFoodConfig.saveConfig();
                                        }
                                        break;
                                    case "Name":
                                        if (clickType == ClickType.LEFT) {
                                            player.sendMessage(ChatColor.YELLOW + "========================================");
                                            player.sendMessage(ChatColor.GRAY + " Please enter Name");
                                            player.sendMessage(ChatColor.GRAY + " Cancel when entering 'cancel'");
                                            player.sendMessage(ChatColor.YELLOW + "========================================");
                                        } else {
                                            ConsumeFood.customFoodConfig.getConfig().set("CustomFood." + internalName + "." + var, "");
                                            ConsumeFood.customFoodConfig.saveConfig();
                                        }
                                        break;
                                    case "CustomModelData":
                                        if (clickType == ClickType.LEFT) {
                                            player.sendMessage(ChatColor.YELLOW + "========================================");
                                            player.sendMessage(ChatColor.GRAY + " Please enter Custom Model Data");
                                            player.sendMessage(ChatColor.GRAY + " Cancel when entering 'cancel'");
                                            player.sendMessage(ChatColor.YELLOW + "========================================");
                                        } else {
                                            ConsumeFood.customFoodConfig.getConfig().set("CustomFood." + internalName + ".Data", -1);
                                            ConsumeFood.customFoodConfig.saveConfig();
                                        }
                                        break;
                                    case "Lore":
                                        if (clickType == ClickType.LEFT) {
                                            player.sendMessage(ChatColor.YELLOW + "========================================");
                                            player.sendMessage(ChatColor.GRAY + " Please enter Lore");
                                            player.sendMessage(ChatColor.GRAY + " Cancel when entering 'cancel'");
                                            player.sendMessage(ChatColor.YELLOW + "========================================");
                                        } else {
                                            List<String> getLore = customFoodUtil.getLore(internalName);
                                            if (!getLore.isEmpty()) {
                                                List<String> replaceLore = new ArrayList<>(getLore);
                                                replaceLore.remove((getLore.size() - 1));
                                                ConsumeFood.customFoodConfig.getConfig().set("CustomFood." + internalName + "." + var, replaceLore);
                                                ConsumeFood.customFoodConfig.saveConfig();
                                            }
                                        }
                                        break;
                                    case "FoodLevel":
                                        if (clickType == ClickType.LEFT) {
                                            player.sendMessage(ChatColor.YELLOW + "========================================");
                                            player.sendMessage(ChatColor.GRAY + " Please enter Food Level");
                                            player.sendMessage(ChatColor.GRAY + " Cancel when entering 'cancel'");
                                            player.sendMessage(ChatColor.YELLOW + "========================================");
                                        } else {
                                            ConsumeFood.customFoodConfig.getConfig().set("CustomFood." + internalName + "." + var, 0);
                                            ConsumeFood.customFoodConfig.saveConfig();
                                        }
                                        break;
                                    case "Saturation":
                                        if (clickType == ClickType.LEFT) {
                                            player.sendMessage(ChatColor.YELLOW + "========================================");
                                            player.sendMessage(ChatColor.GRAY + " Please enter Saturation");
                                            player.sendMessage(ChatColor.GRAY + " Cancel when entering 'cancel'");
                                            player.sendMessage(ChatColor.YELLOW + "========================================");
                                        } else {
                                            ConsumeFood.customFoodConfig.getConfig().set("CustomFood." + internalName + "." + var, 0);
                                            ConsumeFood.customFoodConfig.saveConfig();
                                        }
                                        break;
                                    case "Cooldown":
                                        if (clickType == ClickType.LEFT) {
                                            player.sendMessage(ChatColor.YELLOW + "========================================");
                                            player.sendMessage(ChatColor.GRAY + " Please enter Cooldown");
                                            player.sendMessage(ChatColor.GRAY + " Cancel when entering 'cancel'");
                                            player.sendMessage(ChatColor.YELLOW + "========================================");
                                        } else {
                                            ConsumeFood.customFoodConfig.getConfig().set("CustomFood." + internalName + "." + var, 0);
                                            ConsumeFood.customFoodConfig.saveConfig();
                                        }
                                        break;
                                    case "PotionEffect":
                                        if (clickType == ClickType.LEFT) {
                                            player.sendMessage(ChatColor.YELLOW + "========================================");
                                            player.sendMessage(ChatColor.GRAY + " Please enter Potion Effect");
                                            player.sendMessage(ChatColor.GRAY + " Format: <potionType>:<level>:<duration>:<chance>");
                                            player.sendMessage(ChatColor.GRAY + " Cancel when entering 'cancel'");
                                            player.sendMessage(ChatColor.YELLOW + "========================================");
                                        } else {
                                            List<String> getLore = customFoodUtil.getPotionEffectList(internalName);
                                            if (!getLore.isEmpty()) {
                                                List<String> replaceLore = new ArrayList<>(getLore);
                                                replaceLore.remove((getLore.size() - 1));
                                                ConsumeFood.customFoodConfig.getConfig().set("CustomFood." + internalName + "." + var, replaceLore);
                                                ConsumeFood.customFoodConfig.saveConfig();
                                            }
                                        }
                                        break;
                                    case "Command":
                                        if (clickType == ClickType.LEFT) {
                                            player.sendMessage(ChatColor.YELLOW + "========================================");
                                            player.sendMessage(ChatColor.GRAY + " Please enter Execute Command");
                                            player.sendMessage(ChatColor.GRAY + " Available executeType = [console, player]");
                                            player.sendMessage(ChatColor.GRAY + " Format: <executeType>:<command>");
                                            player.sendMessage(ChatColor.GRAY + " Cancel when entering 'cancel'");
                                            player.sendMessage(ChatColor.YELLOW + "========================================");
                                        } else {
                                            List<String> getLore = customFoodUtil.getCommandList(internalName);
                                            if (!getLore.isEmpty()) {
                                                List<String> replaceLore = new ArrayList<>(getLore);
                                                replaceLore.remove((getLore.size() - 1));
                                                ConsumeFood.customFoodConfig.getConfig().set("CustomFood." + internalName + "." + var, replaceLore);
                                                ConsumeFood.customFoodConfig.saveConfig();
                                            }
                                        }
                                        break;
                                }
                                if (clickType == ClickType.LEFT) {
                                    player.closeInventory();
                                    ConsumeFood.setCustomFoodEditMap(player, CustomFoodEditEnum.isEnabled, true);
                                    ConsumeFood.setCustomFoodEditMap(player, CustomFoodEditEnum.valueOf(var), true);
                                    ConsumeFood.editingCustomFood.put(player.getUniqueId(), internalName);
                                }
                                if (clickType == ClickType.RIGHT) {
                                    player.openInventory(customFoodEditInv.getInventory());
                                    customFoodEditInv.editInv(internalName);
                                }
                            } else {
                                Bukkit.getConsoleSender().sendMessage(ConsumeFood.prefix + ChatColor.RED + " Internal name not found");
                                player.closeInventory();
                            }
                        }
                    }
                }
            }
        }
    }

}

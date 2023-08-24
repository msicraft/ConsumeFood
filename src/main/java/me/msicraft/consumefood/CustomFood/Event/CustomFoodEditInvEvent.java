package me.msicraft.consumefood.CustomFood.Event;

import me.msicraft.consumefood.API.Util.Util;
import me.msicraft.consumefood.Compatibility.PlaceholderApi.PlaceHolderApiUtil;
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
    private final Util util = new Util();

    public HashMap<UUID, String> page_count = new HashMap<>();

    private void sendPermissionMessage(Player player) {
        String permissionMessage = util.getPermissionErrorMessage();
        if (permissionMessage != null && !permissionMessage.equals("")) {
            if (ConsumeFood.canUsePlaceHolderApi) {
                permissionMessage = PlaceHolderApiUtil.getApplyPlaceHolder(player, permissionMessage);
            }
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', permissionMessage));
        }
    }

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
                            if (player.hasPermission("consumefood.customfood.create")) {
                                player.closeInventory();
                                player.sendMessage(ChatColor.YELLOW + "========================================");
                                player.sendMessage(ChatColor.GRAY + " Please enter internal_name");
                                player.sendMessage(ChatColor.GRAY + " Cancel when entering 'cancel'");
                                player.sendMessage(ChatColor.YELLOW + "========================================");
                                ConsumeFood.setCustomFoodEditMap(player, CustomFoodEditEnum.isCreate, true);
                            } else {
                                sendPermissionMessage(player);
                            }
                            break;
                    }
                }
            } else if (selectItemData.has(new NamespacedKey(ConsumeFood.getPlugin(), "ConsumeFood-CustomFood-Edit"), PersistentDataType.STRING)) {
                String internalName = selectItemData.get(new NamespacedKey(ConsumeFood.getPlugin(), "ConsumeFood-CustomFood-Edit"), PersistentDataType.STRING);
                if (internalName != null) {
                    switch (clickType) {
                        case LEFT:
                            if (player.hasPermission("consumefood.customfood.edit." + internalName)) {
                                player.openInventory(customFoodEditInv.getInventory());
                                customFoodEditInv.editInv(internalName);
                            } else {
                                sendPermissionMessage(player);
                            }
                            break;
                        case RIGHT:
                            if (player.hasPermission("consumefood.customfood.get." + internalName)) {
                                ItemStack getStack = customFoodUtil.getCustomFood(internalName, ConsumeFood.bukkitBrandType);
                                if (getStack != null) {
                                    player.getInventory().addItem(getStack);
                                }
                            } else {
                                sendPermissionMessage(player);
                            }
                            break;
                        case SHIFT_LEFT:
                            if (player.hasPermission("consumefood.customfood.remove." + internalName)) {
                                ConsumeFood.customFoodConfig.getConfig().set("CustomFood." + internalName, null);
                                ConsumeFood.customFoodConfig.saveConfig();
                                player.openInventory(customFoodEditInv.getInventory());
                                customFoodEditInv.setMainInv(player);
                            } else {
                                sendPermissionMessage(player);
                            }
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
                            boolean isUseEdit = true;
                            if (internalName != null) {
                                switch (var) {
                                    case "Material":
                                        if (clickType == ClickType.LEFT) {
                                            player.sendMessage(ChatColor.YELLOW + "========================================");
                                            player.sendMessage(ChatColor.GRAY + " Please enter Material");
                                            player.sendMessage(ChatColor.GRAY + " Cancel when entering 'cancel'");
                                            player.sendMessage(ChatColor.YELLOW + "========================================");
                                        } else {
                                            isUseEdit = false;
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
                                            isUseEdit = false;
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
                                            isUseEdit = false;
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
                                            isUseEdit = false;
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
                                            isUseEdit = false;
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
                                            isUseEdit = false;
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
                                            isUseEdit = false;
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
                                            isUseEdit = false;
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
                                            isUseEdit = false;
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
                                            isUseEdit = false;
                                            List<String> getLore = customFoodUtil.getCommandList(internalName);
                                            if (!getLore.isEmpty()) {
                                                List<String> replaceLore = new ArrayList<>(getLore);
                                                replaceLore.remove((getLore.size() - 1));
                                                ConsumeFood.customFoodConfig.getConfig().set("CustomFood." + internalName + "." + var, replaceLore);
                                                ConsumeFood.customFoodConfig.saveConfig();
                                            }
                                        }
                                        break;
                                    case "Enchant":
                                        if (clickType == ClickType.LEFT) {
                                            player.sendMessage(ChatColor.YELLOW + "========================================");
                                            player.sendMessage(ChatColor.GRAY + " Please enter enchant");
                                            player.sendMessage(ChatColor.GRAY + " Format: <enchant>:<level>");
                                            player.sendMessage(ChatColor.GRAY + " Cancel when entering 'cancel'");
                                            player.sendMessage(ChatColor.YELLOW + "========================================");
                                        } else {
                                            isUseEdit = false;
                                            List<String> getEnchants = customFoodUtil.getEnchantList(internalName);
                                            if (!getEnchants.isEmpty()) {
                                                List<String> replace = new ArrayList<>(getEnchants);
                                                replace.remove((getEnchants.size() - 1));
                                                ConsumeFood.customFoodConfig.getConfig().set("CustomFood." + internalName + "." + var, replace);
                                                ConsumeFood.customFoodConfig.saveConfig();
                                            }
                                        }
                                        break;
                                    case "HideEnchant":
                                        if (clickType == ClickType.LEFT) {
                                            isUseEdit = false;
                                            boolean value = customFoodUtil.hideEnchant(internalName);
                                            ConsumeFood.customFoodConfig.getConfig().set("CustomFood." + internalName + "." + var, !value);
                                            ConsumeFood.customFoodConfig.saveConfig();
                                            player.openInventory(customFoodEditInv.getInventory());
                                            customFoodEditInv.editInv(internalName);
                                        } else {
                                            isUseEdit = false;
                                        }
                                        break;
                                    case "HidePotionEffect":
                                        if (clickType == ClickType.LEFT) {
                                            isUseEdit = false;
                                            boolean value = customFoodUtil.hidePotionEffect(internalName);
                                            ConsumeFood.customFoodConfig.getConfig().set("CustomFood." + internalName + "." + var, !value);
                                            ConsumeFood.customFoodConfig.saveConfig();
                                            player.openInventory(customFoodEditInv.getInventory());
                                            customFoodEditInv.editInv(internalName);
                                        } else {
                                            isUseEdit = false;
                                        }
                                        break;
                                    case "DisableCrafting":
                                        if (clickType == ClickType.LEFT) {
                                            isUseEdit = false;
                                            boolean value = customFoodUtil.getDisableCrafting(internalName);
                                            ConsumeFood.customFoodConfig.getConfig().set("CustomFood." + internalName + "." + var, !value);
                                            ConsumeFood.customFoodConfig.saveConfig();
                                            player.openInventory(customFoodEditInv.getInventory());
                                            customFoodEditInv.editInv(internalName);
                                        } else {
                                            isUseEdit = false;
                                        }
                                        break;
                                    case "DisableSmelting":
                                        if (clickType == ClickType.LEFT) {
                                            isUseEdit = false;
                                            boolean value = customFoodUtil.getDisableSmelting(internalName);
                                            ConsumeFood.customFoodConfig.getConfig().set("CustomFood." + internalName + "." + var, !value);
                                            ConsumeFood.customFoodConfig.saveConfig();
                                            player.openInventory(customFoodEditInv.getInventory());
                                            customFoodEditInv.editInv(internalName);
                                        } else {
                                            isUseEdit = false;
                                        }
                                        break;
                                    case "DisableAnvil":
                                        if (clickType == ClickType.LEFT) {
                                            isUseEdit = false;
                                            boolean value = customFoodUtil.getDisableAnvil(internalName);
                                            ConsumeFood.customFoodConfig.getConfig().set("CustomFood." + internalName + "." + var, !value);
                                            ConsumeFood.customFoodConfig.saveConfig();
                                            player.openInventory(customFoodEditInv.getInventory());
                                            customFoodEditInv.editInv(internalName);
                                        } else {
                                            isUseEdit = false;
                                        }
                                        break;
                                    case "DisableEnchant":
                                        if (clickType == ClickType.LEFT) {
                                            isUseEdit = false;
                                            boolean value = customFoodUtil.getDisableEnchant(internalName);
                                            ConsumeFood.customFoodConfig.getConfig().set("CustomFood." + internalName + "." + var, !value);
                                            ConsumeFood.customFoodConfig.saveConfig();
                                            player.openInventory(customFoodEditInv.getInventory());
                                            customFoodEditInv.editInv(internalName);
                                        } else {
                                            isUseEdit = false;
                                        }
                                        break;
                                    case "Sound":
                                        if (clickType == ClickType.LEFT) {
                                            player.sendMessage(ChatColor.YELLOW + "========================================");
                                            player.sendMessage(ChatColor.GRAY + " Please enter sound");
                                            player.sendMessage(ChatColor.GRAY + " Format: <sound>:<volume>:<pitch>:<category>");
                                            player.sendMessage(ChatColor.GRAY + " Cancel when entering 'cancel'");
                                            player.sendMessage(ChatColor.YELLOW + "========================================");
                                        } else {
                                            isUseEdit = false;
                                            ConsumeFood.customFoodConfig.getConfig().set("CustomFood." + internalName + "." + var, "");
                                            ConsumeFood.customFoodConfig.saveConfig();
                                        }
                                        break;
                                    case "PotionColor":
                                        if (clickType == ClickType.LEFT) {
                                            player.sendMessage(ChatColor.YELLOW + "========================================");
                                            player.sendMessage(ChatColor.GRAY + " Please enter color name");
                                            player.sendMessage(ChatColor.GRAY + " Example: white -> FFFFFF / black -> 000000");
                                            player.sendMessage(ChatColor.GRAY + " Cancel when entering 'cancel'");
                                            player.sendMessage(ChatColor.YELLOW + "========================================");
                                        } else {
                                            isUseEdit = false;
                                            ConsumeFood.customFoodConfig.getConfig().set("CustomFood." + internalName + "." + var, null);
                                            ConsumeFood.customFoodConfig.saveConfig();
                                        }
                                }
                                if (isUseEdit) {
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

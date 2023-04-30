package me.msicraft.consumefood.CustomFood.Inventory;

import me.msicraft.consumefood.API.Util.PaperApiUtil;
import me.msicraft.consumefood.API.Util.SpigotUtil;
import me.msicraft.consumefood.ConsumeFood;
import me.msicraft.consumefood.CustomFood.CustomFoodUtil;
import me.msicraft.consumefood.Enum.CustomFoodEditEnum;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CustomFoodEditInv implements InventoryHolder {

    private Inventory customFoodEditInv;

    private final CustomFoodUtil customFoodUtil = new CustomFoodUtil();

    public CustomFoodEditInv(Player player) {
        customFoodEditInv = Bukkit.createInventory(player, 54, "Custom Food");
    }

    private final int[] slots = {19,20,21,22,23,24,25, 28,29,30,31,32,33,34, 37,38,39,40,41,42,43};
    private final List<String> tempLore = new ArrayList<>();

    public void editInv(String internalName) {
        ItemStack itemStack;
        itemStack = createNormalItem(Material.BARRIER, ChatColor.RED + "Back", tempLore, "ConsumeFood-Edit-Var", "Back");
        customFoodEditInv.setItem(0, itemStack);
        itemStack = customFoodUtil.getCustomFood(internalName, ConsumeFood.bukkitBrandType);
        removeEditTag(itemStack);
        addEditTag(itemStack, internalName);
        customFoodEditInv.setItem(4, itemStack);
        int count = 0;
        for (CustomFoodEditEnum customFoodEditEnum : CustomFoodEditEnum.values()) {
            if (customFoodEditEnum != CustomFoodEditEnum.isCreate && customFoodEditEnum != CustomFoodEditEnum.isEnabled) {
                int slot = slots[count];
                List<String> loreList = getBasicLore();
                loreList.add(ChatColor.GRAY + "Set " + customFoodEditEnum.name());
                loreList.add("");
                switch (customFoodEditEnum) {
                    case Material:
                        Material material = customFoodUtil.getMaterial(internalName);
                        loreList.add(ChatColor.GRAY + "Current Material: " + material.name());
                        itemStack = createNormalItem(material, ChatColor.WHITE + "Material", loreList, "ConsumeFood-Edit-Var", customFoodEditEnum.name());
                        break;
                    case TextureValue:
                        String textureValue = customFoodUtil.getTextureValue(internalName);
                        loreList.add(ChatColor.GRAY + "Current TextureValue: ");
                        itemStack = customFoodUtil.getHead(internalName, ConsumeFood.bukkitBrandType, ChatColor.WHITE + "Texture Value", textureValue, loreList, customFoodEditEnum);
                        break;
                    case Name:
                        String name = customFoodUtil.getName(internalName);
                        loreList.add(ChatColor.GRAY + "Current Name: " + ChatColor.translateAlternateColorCodes('&', name));
                        itemStack = createNormalItem(Material.OAK_SIGN, "Name", loreList, "ConsumeFood-Edit-Var", customFoodEditEnum.name());
                        break;
                    case CustomModelData:
                        int customModelData = customFoodUtil.getCustomModelData(internalName);
                        loreList.add(ChatColor.GRAY + "Current CustomModelData: " + customModelData);
                        itemStack = createNormalItem(Material.ITEM_FRAME, "Custom Model Data", loreList, "ConsumeFood-Edit-Var", customFoodEditEnum.name());
                        break;
                    case Lore:
                        loreList.add(ChatColor.GRAY + "Current Lore: ");
                        for (String s : customFoodUtil.getLore(internalName)) {
                            loreList.add(ChatColor.translateAlternateColorCodes('&', s));
                        }
                        itemStack = createNormalItem(Material.WRITABLE_BOOK, "Lore", loreList, "ConsumeFood-Edit-Var", customFoodEditEnum.name());
                        break;
                    case FoodLevel:
                        int foodlevel = customFoodUtil.getFoodLevel(internalName);
                        loreList.add(ChatColor.GRAY + "Current FoodLevel: " + foodlevel);
                        itemStack = createNormalItem(Material.PORKCHOP, "Food Level", loreList, "ConsumeFood-Edit-Var", customFoodEditEnum.name());
                        break;
                    case Saturation:
                        float saturation = customFoodUtil.getSaturation(internalName);
                        loreList.add(ChatColor.GRAY + "Current Saturation: " + saturation);
                        itemStack = createNormalItem(Material.COOKED_PORKCHOP, "Saturation", loreList, "ConsumeFood-Edit-Var", customFoodEditEnum.name());
                        break;
                    case Cooldown:
                        double cooldown = customFoodUtil.getPersonalCooldown(internalName);
                        loreList.add(ChatColor.GRAY + "Current Cooldown: " + cooldown);
                        itemStack = createNormalItem(Material.COMPASS, "Cooldown", loreList, "ConsumeFood-Edit-Var", customFoodEditEnum.name());
                        break;
                    case PotionEffect:
                        loreList.add(ChatColor.GRAY + "Current Potion Effects: ");
                        for (String s : customFoodUtil.getPotionEffectList(internalName)) {
                            loreList.add(ChatColor.WHITE + s);
                        }
                        itemStack = createNormalItem(Material.POTION, "Potion Effect", loreList, "ConsumeFood-Edit-Var", customFoodEditEnum.name());
                        break;
                    case Command:
                        loreList.add(ChatColor.GRAY + "Current Execute Commands: ");
                        for (String s : customFoodUtil.getCommandList(internalName)) {
                            loreList.add(ChatColor.WHITE + s);
                        }
                        itemStack = createNormalItem(Material.COMMAND_BLOCK, ChatColor.WHITE + "Execute Command", loreList, "ConsumeFood-Edit-Var", customFoodEditEnum.name());
                        break;
                    case Enchant:
                        loreList.add(ChatColor.GRAY + "Current Enchants: ");
                        for (String s : customFoodUtil.getEnchantList(internalName)) {
                            loreList.add(ChatColor.WHITE + s);
                        }
                        itemStack = createNormalItem(Material.ENCHANTED_BOOK, ChatColor.WHITE + "Enchant", loreList, "ConsumeFood-Edit-Var", customFoodEditEnum.name());
                        break;
                    case HideEnchant:
                        if (!temp.isEmpty()) {
                            temp.clear();
                        }
                        temp.add(ChatColor.YELLOW + "Left click: change value");
                        temp.add("");
                        temp.add(ChatColor.GRAY + "Current Value: " + customFoodUtil.hideEnchant(internalName));
                        itemStack = createNormalItem(Material.NETHER_STAR, ChatColor.WHITE + "Hide Enchant", temp, "ConsumeFood-Edit-Var", customFoodEditEnum.name());
                        break;
                    case DisableCrafting:
                        if (!temp.isEmpty()) {
                            temp.clear();
                        }
                        temp.add(ChatColor.YELLOW + "Left click: change value");
                        temp.add("");
                        temp.add(ChatColor.GRAY + "Current Value: " + customFoodUtil.getDisableCrafting(internalName));
                        itemStack = createNormalItem(Material.CRAFTING_TABLE, ChatColor.WHITE + "Disable crafting", temp, "ConsumeFood-Edit-Var", customFoodEditEnum.name());
                        break;
                    case DisableSmelting:
                        if (!temp.isEmpty()) {
                            temp.clear();
                        }
                        temp.add(ChatColor.YELLOW + "Left click: change value");
                        temp.add("");
                        temp.add(ChatColor.GRAY + "Current Value: " + customFoodUtil.getDisableSmelting(internalName));
                        itemStack = createNormalItem(Material.FURNACE, ChatColor.WHITE + "Disable smelting", temp, "ConsumeFood-Edit-Var", customFoodEditEnum.name());
                        break;
                    case DisableAnvil:
                        if (!temp.isEmpty()) {
                            temp.clear();
                        }
                        temp.add(ChatColor.YELLOW + "Left click: change value");
                        temp.add("");
                        temp.add(ChatColor.GRAY + "Current Value: " + customFoodUtil.getDisableAnvil(internalName));
                        itemStack = createNormalItem(Material.ANVIL, ChatColor.WHITE + "Disable anvil", temp, "ConsumeFood-Edit-Var", customFoodEditEnum.name());
                        break;
                    case DisableEnchant:
                        if (!temp.isEmpty()) {
                            temp.clear();
                        }
                        temp.add(ChatColor.YELLOW + "Left click: change value");
                        temp.add("");
                        temp.add(ChatColor.GRAY + "Current Value: " + customFoodUtil.getDisableEnchant(internalName));
                        itemStack = createNormalItem(Material.ENCHANTING_TABLE, ChatColor.WHITE + "Disable enchant", temp, "ConsumeFood-Edit-Var", customFoodEditEnum.name());
                        break;
                    case Sound:
                        String sound = customFoodUtil.getSound(internalName);
                        if (sound == null) {
                            sound = "";
                        }
                        loreList.add(ChatColor.GRAY + "Current sound: " + sound);
                        itemStack = createNormalItem(Material.JUKEBOX, ChatColor.WHITE + "Sound", loreList, "ConsumeFood-Edit-Var", customFoodEditEnum.name());
                        break;
                }
                customFoodEditInv.setItem(slot, itemStack);
                count++;
            }
        }
    }

    private final List<String> temp = new ArrayList<>();

    private List<String> getBasicLore() {
        List<String> temp = new ArrayList<>();
        temp.add("");
        temp.add(ChatColor.YELLOW + "Left Click: edit value");
        temp.add(ChatColor.YELLOW + "Right Click: remove value");
        temp.add("");
        return temp;
    }

    public void setMainInv(Player player) {
        basic_button();
        createButton();
        page_book(player);
        int maxSize = customFoodUtil.getInternalNames().size();
        int page_num = 0;
        String pageNumObject = ConsumeFood.getPlugin().customFoodPage.get(player.getUniqueId());
        if (pageNumObject != null) {
            String[] a = pageNumObject.split(":");
            page_num = Integer.parseInt(a[1]);
        }
        int gui_count = 0;
        int lastCount = page_num*45;
        ArrayList<String> getInternalNames = customFoodUtil.getInternalNames();
        List<String> loreList = new ArrayList<>();
        for (int a = lastCount; a<maxSize; a++) {
            if (!loreList.isEmpty()) {
                loreList.clear();
            }
            String internalName = getInternalNames.get(a);
            ItemStack itemStack = null;
            loreList.add(ChatColor.YELLOW + "Left Click: Edit Item");
            loreList.add(ChatColor.YELLOW + "Right Click: Get Item");
            loreList.add(ChatColor.YELLOW + "Shift + Left Click: Delete Item");
            Material material = customFoodUtil.getMaterial(internalName);
            String name = customFoodUtil.getName(internalName);
            int customModelData = customFoodUtil.getCustomModelData(internalName);
            List<String> lore = customFoodUtil.getLore(internalName);
            if (!lore.isEmpty()) {
                loreList.add("");
                loreList.addAll(lore);
            }
            if (material == Material.PLAYER_HEAD) {
                UUID uuid = customFoodUtil.getUUID(internalName);
                String textureValue = customFoodUtil.getTextureValue(internalName);
                if (ConsumeFood.bukkitBrandType == 0) {
                    itemStack = PaperApiUtil.getPaperApiPlayerHead_Inv(name, customModelData, uuid, textureValue, loreList, internalName);
                } else if (ConsumeFood.bukkitBrandType == 1){
                    itemStack = SpigotUtil.getSpigotPlayerHead_Inv(name, customModelData, uuid, textureValue, loreList, internalName);
                }
            } else {
                if (ConsumeFood.bukkitBrandType == 0) {
                    itemStack = PaperApiUtil.getCustomFood_Inv(material, name, customModelData, loreList, internalName);
                } else if (ConsumeFood.bukkitBrandType == 1){
                    itemStack = SpigotUtil.getCustomFood_Inv(material, name, customModelData, loreList, internalName);
                }
            }
            if (itemStack != null) {
                customFoodEditInv.setItem(gui_count, itemStack);
                gui_count++;
                if (gui_count >= 45) {
                    break;
                }
            }
        }
    }

    private void basic_button() {
        ItemStack itemStack = new ItemStack(Material.ARROW, 1);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName("Next");
        itemMeta.getPersistentDataContainer().set(new NamespacedKey(ConsumeFood.getPlugin(), "CustomFood-Edit"), PersistentDataType.STRING, "next");
        itemStack.setItemMeta(itemMeta);
        customFoodEditInv.setItem(50, itemStack);
        itemMeta.setDisplayName("Previous");
        itemMeta.getPersistentDataContainer().set(new NamespacedKey(ConsumeFood.getPlugin(), "CustomFood-Edit"), PersistentDataType.STRING, "previous");
        itemStack.setItemMeta(itemMeta);
        customFoodEditInv.setItem(48, itemStack);
    }

    private void createButton() {
        ItemStack itemStack = new ItemStack(Material.WRITABLE_BOOK, 1);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(ChatColor.GREEN + "Create Item");
        itemMeta.getPersistentDataContainer().set(new NamespacedKey(ConsumeFood.getPlugin(), "CustomFood-Edit"), PersistentDataType.STRING, "create");
        itemStack.setItemMeta(itemMeta);
        customFoodEditInv.setItem(53, itemStack);
    }

    public void page_book(Player player) {
        int max = customFoodUtil.getInternalNames().size();
        int maxCount = max / 45;
        String var = "max-page:" + maxCount;
        ConsumeFood.getPlugin().customFoodMaxPage.put(player.getUniqueId(), var);
        String getPage = ConsumeFood.getPlugin().customFoodPage.get(player.getUniqueId());
        String pageCount = "0";
        if (getPage != null) {
            String[] a = getPage.split(":");
            pageCount = a[1];
        }
        int count = Integer.parseInt(pageCount) + 1;
        ItemStack itemStack = new ItemStack(Material.BOOK, 1);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(ChatColor.WHITE + "Page: " + count);
        itemStack.setItemMeta(itemMeta);
        customFoodEditInv.setItem(49, itemStack);
    }

    private ItemStack createNormalItem(Material material, String name, List<String> list, String dataTag, String data) {
        ItemStack itemStack = new ItemStack(material, 1);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(name);
        itemMeta.setLore(list);
        PersistentDataContainer dataContainer = itemMeta.getPersistentDataContainer();
        dataContainer.set(new NamespacedKey(ConsumeFood.getPlugin(), dataTag), PersistentDataType.STRING, data);
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    private void removeEditTag(ItemStack itemStack) {
        ItemMeta itemMeta = itemStack.getItemMeta();
        PersistentDataContainer data = itemMeta.getPersistentDataContainer();
        data.remove(new NamespacedKey(ConsumeFood.getPlugin(), "ConsumeFood-CustomFood-Edit"));
        itemStack.setItemMeta(itemMeta);
    }

    private void addEditTag(ItemStack itemStack, String internalName) {
        ItemMeta itemMeta = itemStack.getItemMeta();
        PersistentDataContainer data = itemMeta.getPersistentDataContainer();
        data.set(new NamespacedKey(ConsumeFood.getPlugin(), "ConsumeFood-CustomFood-Editing"), PersistentDataType.STRING, internalName);
        itemStack.setItemMeta(itemMeta);
    }

    @Override
    public Inventory getInventory() {
        return customFoodEditInv;
    }
}

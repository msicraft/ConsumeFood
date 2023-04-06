package me.msicraft.consumefood.CustomFood;

import me.msicraft.consumefood.API.Util.PaperApiUtil;
import me.msicraft.consumefood.API.Util.SpigotUtil;
import me.msicraft.consumefood.API.Util.Util;
import me.msicraft.consumefood.Compatibility.PlaceholderApi.PlaceHolderApiUtil;
import me.msicraft.consumefood.ConsumeFood;
import me.msicraft.consumefood.Enum.CustomFoodEditEnum;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class CustomFoodUtil {

    public ArrayList<String> getInternalNames() {
        ArrayList<String> internalNames = new ArrayList<>();
        ConfigurationSection section = ConsumeFood.customFoodConfig.getConfig().getConfigurationSection("CustomFood");
        if (section != null) {
            Set<String> list = section.getKeys(false);
            internalNames.addAll(list);
        }
        return internalNames;
    }

    public void playerHeadUUIDCheck() {
        int count = 0;
        for (String internalName : getInternalNames()) {
            Material material = getMaterial(internalName);
            if (material == Material.PLAYER_HEAD) {
                String uuidS = ConsumeFood.customFoodConfig.getConfig().getString("CustomFood." + internalName + ".UUID");
                if (uuidS == null) {
                    UUID uuid = UUID.randomUUID();
                    ConsumeFood.customFoodConfig.getConfig().set("CustomFood." + internalName + ".UUID", uuid.toString());
                    count++;
                }
            }
        }
        if (count >= 1) {
            ConsumeFood.customFoodConfig.saveConfig();
            Bukkit.getConsoleSender().sendMessage(ConsumeFood.prefix + " " + count + ChatColor.GREEN + " new custom food(Material=player_head) uuids assigned");
        }
    }

    public boolean isCustomFood(ItemStack itemStack) {
        boolean check = false;
        ItemMeta itemMeta = itemStack.getItemMeta();
        if (itemMeta != null) {
            NamespacedKey customFoodKey = new NamespacedKey(ConsumeFood.getPlugin(), "ConsumeFood-CustomFood");
            PersistentDataContainer data = itemMeta.getPersistentDataContainer();
            if (data.has(customFoodKey, PersistentDataType.STRING)) {
                check = true;
            }
        }
        return check;
    }

    public EquipmentSlot getUseHand(Player player, ItemStack itemStack) {
        ItemStack handItem = player.getInventory().getItemInMainHand();
        if (itemStack.isSimilar(handItem)) {
            return EquipmentSlot.HAND;
        }
        ItemStack offHandItem = player.getInventory().getItemInOffHand();
        if (itemStack.isSimilar(offHandItem)) {
            return EquipmentSlot.OFF_HAND;
        }
        return null;
    }

    public String getInternalName(ItemStack itemStack) {
        String name = null;
        ItemMeta itemMeta = itemStack.getItemMeta();
        if (itemMeta != null) {
            NamespacedKey customFoodKey = new NamespacedKey(ConsumeFood.getPlugin(), "ConsumeFood-CustomFood");
            PersistentDataContainer data = itemMeta.getPersistentDataContainer();
            name = data.get(customFoodKey, PersistentDataType.STRING);
        }
        return name;
    }

    public int getFoodLevel(String internalName) {
        int value = 0;
        if (ConsumeFood.customFoodConfig.getConfig().contains("CustomFood." + internalName + ".FoodLevel")) {
            value = ConsumeFood.customFoodConfig.getConfig().getInt("CustomFood." + internalName + ".FoodLevel");
        }
        return value;
    }

    public float getSaturation(String internalName) {
        float value = 0;
        if (ConsumeFood.customFoodConfig.getConfig().contains("CustomFood." + internalName + ".FoodLevel")) {
            value = (float) ConsumeFood.customFoodConfig.getConfig().getDouble("CustomFood." + internalName + ".Saturation");
        }
        return value;
    }

    public double getPersonalCooldown(String internalName) {
        double value = 0;
        if (ConsumeFood.customFoodConfig.getConfig().contains("CustomFood." + internalName + ".Cooldown")) {
            value = ConsumeFood.customFoodConfig.getConfig().getDouble("CustomFood." + internalName + ".Cooldown");
        }
        return value;
    }

    public void addCustomFoodDataTag(ItemStack itemStack, String internalName) {
        ItemMeta itemMeta = itemStack.getItemMeta();
        if (itemMeta != null) {
            NamespacedKey customFoodKey = new NamespacedKey(ConsumeFood.getPlugin(), "ConsumeFood-CustomFood");
            PersistentDataContainer data = itemMeta.getPersistentDataContainer();
            data.set(customFoodKey, PersistentDataType.STRING, internalName);
        }
        itemStack.setItemMeta(itemMeta);
    }
    
    public Material getMaterial(String internalName) {
        Material material = Material.STONE;
        if (ConsumeFood.customFoodConfig.getConfig().contains("CustomFood." + internalName + ".Material")) {
            try {
                String materialS = ConsumeFood.customFoodConfig.getConfig().getString("CustomFood." + internalName + ".Material");
                if (materialS != null) {
                    material = Material.valueOf(materialS.toUpperCase());
                }
            } catch (IllegalArgumentException | NullPointerException e) {
                Bukkit.getConsoleSender().sendMessage(ConsumeFood.prefix + ChatColor.RED + " Invalid material name");
                Bukkit.getConsoleSender().sendMessage(ConsumeFood.prefix + ChatColor.RED + " InternalName: " + ChatColor.WHITE + internalName);
            }
        }
        return material;
    }

    public String getName(String internalName) {
        String name = null;
        if (ConsumeFood.customFoodConfig.getConfig().contains("CustomFood." + internalName + ".Name")) {
            name = ConsumeFood.customFoodConfig.getConfig().getString("CustomFood." + internalName + ".Name");
        }
        return name;
    }

    public UUID getUUID(String internalName) {
        UUID uuid = null;
        if (ConsumeFood.customFoodConfig.getConfig().contains("CustomFood." + internalName + ".UUID")) {
            String uuidS = ConsumeFood.customFoodConfig.getConfig().getString("CustomFood." + internalName + ".UUID");
            if (uuidS != null) {
                uuid = UUID.fromString(uuidS);
            }
        }
        return uuid;
    }

    public String getTextureValue(String internalName) {
        String value = null;
        if (ConsumeFood.customFoodConfig.getConfig().contains("CustomFood." + internalName + ".TextureValue")) {
            value = ConsumeFood.customFoodConfig.getConfig().getString("CustomFood." + internalName + ".TextureValue");
        }
        return value;
    }

    public int getCustomModelData(String internalName) {
        int data = -1;
        if (ConsumeFood.customFoodConfig.getConfig().contains("CustomFood." + internalName + ".Data")) {
            data = ConsumeFood.customFoodConfig.getConfig().getInt("CustomFood." + internalName + ".Data");
        }
        return data;
    }

    public List<String> getLore(String internalName) {
        List<String> list = new ArrayList<>();
        if (ConsumeFood.customFoodConfig.getConfig().contains("CustomFood." + internalName + ".Lore")) {
            list.addAll(ConsumeFood.customFoodConfig.getConfig().getStringList("CustomFood." + internalName + ".Lore"));
        }
        return list;
    }

    public List<String> getPotionEffectList(String internalName) {
        List<String> list = new ArrayList<>();
        if (ConsumeFood.customFoodConfig.getConfig().contains("CustomFood." + internalName + ".PotionEffect")) {
            list.addAll(ConsumeFood.customFoodConfig.getConfig().getStringList("CustomFood." + internalName + ".PotionEffect"));
        }
        return list;
    }

    public List<String> getCommandList(String internalName) {
        List<String> list = new ArrayList<>();
        if (ConsumeFood.customFoodConfig.getConfig().contains("CustomFood." + internalName + ".Command")) {
            list.addAll(ConsumeFood.customFoodConfig.getConfig().getStringList("CustomFood." + internalName + ".Command"));
        }
        return list;
    }

    public ItemStack getCustomFood(String internalName, int brandType) {
        ItemStack itemStack = null;
        Material material = getMaterial(internalName);
        String name = getName(internalName);
        int customModelData = getCustomModelData(internalName);
        List<String> loreList = getLore(internalName);
        if (material == Material.PLAYER_HEAD) {
            UUID uuid = getUUID(internalName);
            String textureValue = getTextureValue(internalName);
            if (brandType == 0) {
                itemStack = PaperApiUtil.getPaperApiPlayerHead_Inv(name, customModelData, uuid, textureValue, loreList, internalName);
            } else if (brandType == 1){
                itemStack = SpigotUtil.getSpigotPlayerHead_Inv(name, customModelData, uuid, textureValue, loreList, internalName);
            }
        } else {
            if (brandType == 0) {
                itemStack = PaperApiUtil.getCustomFood_Inv(material, name, customModelData, loreList, internalName);
            } else if (brandType == 1) {
                itemStack = SpigotUtil.getCustomFood_Inv(material, name, customModelData, loreList, internalName);
            }
        }
        if (itemStack != null) {
            addCustomFoodDataTag(itemStack, internalName);
        }
        return itemStack;
    }

    public ItemStack getHead(String internalName, int brandType, String name, String textureValue, List<String> loreList, CustomFoodEditEnum customFoodEditEnum) {
        ItemStack itemStack;
        UUID uuid = getUUID(internalName);
        if (brandType == 0) {
            itemStack = PaperApiUtil.getPaperApiPlayerHead_Inv(name, -1, uuid, textureValue, loreList, internalName);
        } else {
            itemStack = SpigotUtil.getSpigotPlayerHead_Inv(name, -1, uuid, textureValue, loreList, internalName);
        }
        if (itemStack != null) {
            removeEditTag(itemStack);
            addEditVarTag(itemStack, customFoodEditEnum);
        } else {
            itemStack = new ItemStack(Material.PLAYER_HEAD, 1);
            ItemMeta itemMeta = itemStack.getItemMeta();
            itemMeta.setDisplayName(ChatColor.WHITE + name);
            itemMeta.setLore(loreList);
            itemStack.setItemMeta(itemMeta);
            addEditVarTag(itemStack, customFoodEditEnum);
        }
        return itemStack;
    }

    private void removeEditTag(ItemStack itemStack) {
        ItemMeta itemMeta = itemStack.getItemMeta();
        PersistentDataContainer data = itemMeta.getPersistentDataContainer();
        data.remove(new NamespacedKey(ConsumeFood.getPlugin(), "ConsumeFood-CustomFood-Edit"));
        itemStack.setItemMeta(itemMeta);
    }

    private void addEditVarTag(ItemStack itemStack, CustomFoodEditEnum customFoodEditEnum) {
        ItemMeta itemMeta = itemStack.getItemMeta();
        PersistentDataContainer data = itemMeta.getPersistentDataContainer();
        data.set(new NamespacedKey(ConsumeFood.getPlugin(), "ConsumeFood-Edit-Var"), PersistentDataType.STRING, customFoodEditEnum.name());
        itemStack.setItemMeta(itemMeta);
    }

    public boolean hasPotionEffect(String internalName) {
        return !getPotionEffectList(internalName).isEmpty();
    }

    public void applyPotionEffect(Player player, String internalName) {
        List<String> potionEffects = getPotionEffectList(internalName);
        for (String effect : potionEffects) {
            String[] a = effect.split(":");
            PotionEffectType potionEffectType = PotionEffectType.getByName(a[0].toUpperCase());
            int level = Integer.parseInt(a[1]);
            int duration = Integer.parseInt(a[2]);
            double change = Double.parseDouble(a[3]);
            if (potionEffectType != null) {
                if (Math.random() <= change) {
                    int potionLevel = level - 1;
                    if (potionLevel < 0) {
                        potionLevel = 0;
                    }
                    player.addPotionEffect(new PotionEffect(potionEffectType, (duration * 20), potionLevel));
                }
            }  else {
                Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + ConsumeFood.prefix + ChatColor.RED + " Invalid Potion effect: " + ChatColor.WHITE + a[0] + " | Custom Food: " + internalName);
            }
        }
    }

    public boolean hasCommand(String internalName) {
        return !getCommandList(internalName).isEmpty();
    }

    public void applyExecuteCommand(Player player, String internalName) {
        List<String> commandList = getCommandList(internalName);
        for (String commands : commandList) {
            String[] a = commands.split(":");
            String sender = a[0];
            String command = a[1];
            String replace_command;
            if (ConsumeFood.canUsePlaceHolderApi) {
                replace_command = PlaceHolderApiUtil.getApplyPlaceHolder(player, command);
            } else {
                replace_command = command;
            }
            if (sender.equals("player")) {
                Bukkit.getServer().dispatchCommand(player, replace_command);
            } else if (sender.equals("console")) {
                Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), replace_command);
            }
        }
    }

    public void applyConsumeCustomFood(Player player, int foodlevel, float saturation, String internalName, EquipmentSlot slot, ItemStack itemStack) {
        player.setFoodLevel(foodlevel);
        player.setSaturation(saturation);
        if (hasPotionEffect(internalName)) {
            applyPotionEffect(player, internalName);
        }
        if (hasCommand(internalName)) {
            applyExecuteCommand(player, internalName);
        }
        if (slot == EquipmentSlot.HAND) {
            itemStack = player.getInventory().getItemInMainHand();
            player.getInventory().getItemInMainHand().setAmount(player.getInventory().getItemInMainHand().getAmount() - 1);
        } else if (slot == EquipmentSlot.OFF_HAND) {
            itemStack = player.getInventory().getItemInOffHand();
            player.getInventory().getItemInOffHand().setAmount(player.getInventory().getItemInOffHand().getAmount() - 1);
        }
        if (Util.isReturnBowlOrBottleEnabled()) {
            Util.putInType putInType = Util.getInBowlOrBottleType(itemStack);
            ItemStack putItemStack = null;
            int emptySlot = Util.getPlayerEmptySlot(player);
            switch (putInType) {
                case BOWL:
                    putItemStack = new ItemStack(Material.BOWL, 1);
                    break;
                case BOTTLE:
                    putItemStack = new ItemStack(Material.GLASS_BOTTLE, 1);
                    break;
            }
            if (putItemStack != null) {
                if (emptySlot == -1) {
                    player.getWorld().dropItemNaturally(player.getLocation(), putItemStack);
                } else {
                    player.getInventory().addItem(putItemStack);
                }
            }
        }
    }

}

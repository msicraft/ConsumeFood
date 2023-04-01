package me.msicraft.consumefood.VanillaFood;

import me.msicraft.consumefood.Compatibility.PlaceholderApi.PlaceHolderApiUtil;
import me.msicraft.consumefood.ConsumeFood;
import me.msicraft.consumefood.Enum.VanillaFoodEnum;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.List;
import java.util.Random;

public class VanillaFoodUtil {

    private final Random random = new Random();

    /*
    public void cooldownMapCheck() {
        HashMap<String, Long> temp = new HashMap<>();
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (!FoodConsumeEvent.vanillaFood_globalCooldownMap.containsKey(player.getUniqueId())) {
                FoodConsumeEvent.vanillaFood_globalCooldownMap.put(player.getUniqueId(), System.currentTimeMillis());
            }
            if (!FoodConsumeEvent.vanillaFood_personalCooldownMap.containsKey(player.getUniqueId())) {
                FoodConsumeEvent.vanillaFood_personalCooldownMap.put(player.getUniqueId(), temp);
            }
        }
    }

     */

    public boolean isVanillaFood(String foodName) {
        boolean check = false;
        String s = null;
        try {
            s = VanillaFoodEnum.valueOf(foodName.toUpperCase()).name();
            check = true;
        } catch (IllegalArgumentException e) {
            //Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + ConsumeFood.prefix + ChatColor.RED + " Invalid Food Name: " + ChatColor.WHITE + foodName);
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

    public int getFoodLevel(VanillaFoodEnum vanillaFoodEnum) {
        int value = 0;
        if (ConsumeFood.getPlugin().getConfig().contains("Food." + vanillaFoodEnum.name() + ".FoodLevel")) {
            value = ConsumeFood.getPlugin().getConfig().getInt("Food." + vanillaFoodEnum.name() + ".FoodLevel");
        }
        return value;
    }

    public float getSaturation(VanillaFoodEnum vanillaFoodEnum) {
        float value = 0;
        if (ConsumeFood.getPlugin().getConfig().contains("Food." + vanillaFoodEnum.name() + ".Saturation")) {
            value = (float) ConsumeFood.getPlugin().getConfig().getDouble("Food." + vanillaFoodEnum.name() + ".Saturation");
        }
        return value;
    }

    public double getPersonalCooldown(VanillaFoodEnum vanillaFoodEnum) {
        double value = 0;
        if (ConsumeFood.getPlugin().getConfig().contains("Food." + vanillaFoodEnum.name() + ".Cooldown")) {
            value = ConsumeFood.getPlugin().getConfig().getDouble("Food." + vanillaFoodEnum.name() + ".Cooldown");
        }
        return value;
    }

    public boolean hasPotionEffect(VanillaFoodEnum vanillaFoodEnum) {
        boolean check = false;
        if (ConsumeFood.getPlugin().getConfig().contains("Food." + vanillaFoodEnum.name() + ".PotionEffect")) {
            List<String> potionEffects = ConsumeFood.getPlugin().getConfig().getStringList("Food." + vanillaFoodEnum.name() + ".PotionEffect");
            if (!potionEffects.isEmpty()) {
                check = true;
            }
        }
        return check;
    }

    public void applyPotionEffect(Player player, VanillaFoodEnum vanillaFoodEnum) {
        List<String> potionEffects = ConsumeFood.getPlugin().getConfig().getStringList("Food." + vanillaFoodEnum.name() + ".PotionEffect");
        for (String effect : potionEffects) {
            String[] a = effect.split(":");
            PotionEffectType potionEffectType = PotionEffectType.getByName(a[0].toUpperCase());
            int level = Integer.parseInt(a[1]);
            int duration = Integer.parseInt(a[2]);
            double chance = Double.parseDouble(a[3]);
            if (potionEffectType != null) {
                if (random.nextDouble() <= chance) {
                    int potionLevel = level - 1;
                    if (potionLevel < 0) {
                        potionLevel = 0;
                    }
                    player.addPotionEffect(new PotionEffect(potionEffectType, (duration * 20), potionLevel));
                }
            } else {
                Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + ConsumeFood.prefix + ChatColor.RED + " Invalid Potion effect: " + ChatColor.WHITE + a[0] + " | Food: " + vanillaFoodEnum.name());
            }
        }
    }

    public boolean hasCommand(VanillaFoodEnum vanillaFoodEnum) {
        boolean check = false;
        if (ConsumeFood.getPlugin().getConfig().contains("Food." + vanillaFoodEnum.name() + ".Command")) {
            List<String> potionEffects = ConsumeFood.getPlugin().getConfig().getStringList("Food." + vanillaFoodEnum.name() + ".Command");
            if (!potionEffects.isEmpty()) {
                check = true;
            }
        }
        return check;
    }

    public void applyExecuteCommand(Player player, VanillaFoodEnum vanillaFoodEnum) {
        List<String> commandList = ConsumeFood.getPlugin().getConfig().getStringList("Food." + vanillaFoodEnum.name() + ".Command");
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

    public void applyConsumeFood(Player player, int foodLevel, float saturation, VanillaFoodEnum vanillaFoodEnum, EquipmentSlot slot) {
        player.setFoodLevel(foodLevel);
        player.setSaturation(saturation);
        if (hasPotionEffect(vanillaFoodEnum)) {
            applyPotionEffect(player, vanillaFoodEnum);
        }
        if (hasCommand(vanillaFoodEnum)) {
            applyExecuteCommand(player, vanillaFoodEnum);
        }
        if (slot == EquipmentSlot.HAND) {
            player.getInventory().getItemInMainHand().setAmount(player.getInventory().getItemInMainHand().getAmount() - 1);
        } else if (slot == EquipmentSlot.OFF_HAND) {
            player.getInventory().getItemInOffHand().setAmount(player.getInventory().getItemInOffHand().getAmount() - 1);
        }
    }

    public void notRemoveAmountApplyConsumeFood(Player player, int foodLevel, float saturation, VanillaFoodEnum vanillaFoodEnum) {
        player.setFoodLevel(foodLevel);
        player.setSaturation(saturation);
        if (hasPotionEffect(vanillaFoodEnum)) {
            applyPotionEffect(player, vanillaFoodEnum);
        }
        if (hasCommand(vanillaFoodEnum)) {
            applyExecuteCommand(player, vanillaFoodEnum);
        }
    }

}

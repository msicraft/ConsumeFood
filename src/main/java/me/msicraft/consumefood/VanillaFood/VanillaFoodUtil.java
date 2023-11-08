package me.msicraft.consumefood.VanillaFood;

import me.msicraft.consumefood.API.Util.Util;
import me.msicraft.consumefood.Compatibility.PlaceholderApi.PlaceHolderApiUtil;
import me.msicraft.consumefood.ConsumeFood;
import me.msicraft.consumefood.Enum.VanillaFoodEnum;
import me.msicraft.consumefood.FoodDiet.FoodDietUtil;
import me.msicraft.consumefood.PlayerHunger.PlayerHungerUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

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

    public boolean isVanillaFood(String foodName, ItemStack itemStack) {
        boolean check = false;
        String s = null;
        try {
            s = VanillaFoodEnum.valueOf(foodName.toUpperCase()).name();
            check = true;
            if (itemStack.getType() != Material.AIR) {
                if (itemStack.hasItemMeta()) {
                    check = false;
                }
            }
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
            try {
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
            } catch (ArrayIndexOutOfBoundsException | NumberFormatException | NullPointerException e) {
                Bukkit.getConsoleSender().sendMessage(ConsumeFood.prefix + ChatColor.RED + "=====Invalid PotionEffect format=====");
                Bukkit.getConsoleSender().sendMessage(ConsumeFood.prefix + ChatColor.YELLOW + "VanillaFood: " + vanillaFoodEnum.name());
                Bukkit.getConsoleSender().sendMessage(ConsumeFood.prefix + ChatColor.YELLOW + "Invalid line: " + effect);
                Bukkit.getConsoleSender().sendMessage(ConsumeFood.prefix + ChatColor.YELLOW + "Format: <potionType>:<level>:<duration>:<chance>");
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
        new BukkitRunnable() {
            private int count = 0;
            private final int max = commandList.size();
            @Override
            public void run() {
                if (count >= max) {
                    cancel();
                    return;
                }
                String commands = commandList.get(count);
                String[] a = commands.split(":");
                try {
                    String sender = a[0].toLowerCase();
                    String command = a[1];
                    String replace_command;
                    if (ConsumeFood.canUsePlaceHolderApi) {
                        replace_command = PlaceHolderApiUtil.getApplyPlaceHolder(player, command);
                    } else {
                        replace_command = command;
                    }
                    if (sender.equalsIgnoreCase("console")) {
                        Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), replace_command);
                    } else if (sender.equalsIgnoreCase("player")) {
                        Bukkit.getServer().dispatchCommand(player, replace_command);
                    }
                    count++;
                } catch (ArrayIndexOutOfBoundsException e) {
                    Bukkit.getConsoleSender().sendMessage(ConsumeFood.prefix + ChatColor.RED + "=====Invalid Command format=====");
                    Bukkit.getConsoleSender().sendMessage(ConsumeFood.prefix + ChatColor.YELLOW + "VanillaFood: " + vanillaFoodEnum.name());
                    Bukkit.getConsoleSender().sendMessage(ConsumeFood.prefix + ChatColor.YELLOW + "Invalid line: " + commands);
                    Bukkit.getConsoleSender().sendMessage(ConsumeFood.prefix + ChatColor.YELLOW + "Format: <executeType>:<command>");
                    cancel();
                }
            }
        }.runTaskTimer(ConsumeFood.getPlugin(), 0, 1);
    }

    private final FoodDietUtil foodDietUtil = new FoodDietUtil();
    private final PlayerHungerUtil playerHungerUtil = new PlayerHungerUtil();

    public void applyConsumeFood(Player player, int foodLevel, float saturation, VanillaFoodEnum vanillaFoodEnum, EquipmentSlot slot, ItemStack itemStack) {
        if (FoodDietUtil.isEnabled) {
            if (foodDietUtil.containFoodInDietMap(player, vanillaFoodEnum.name())) {
                int count = foodDietUtil.getPenaltyCount(player, vanillaFoodEnum.name());
                foodDietUtil.addPenaltyCount(player, vanillaFoodEnum.name(), 1);
                foodDietUtil.reduceOtherPenaltyCount(player, vanillaFoodEnum.name());
                foodLevel = foodDietUtil.getPenaltyFoodLevel(count, foodLevel);
                saturation = foodDietUtil.getPenaltySaturation(count, saturation);
                foodDietUtil.applyPenaltyPotionEffect(player, count);
            } else {
                foodDietUtil.addPenaltyCount(player, vanillaFoodEnum.name(), 0);
                foodDietUtil.reduceOtherPenaltyCount(player, vanillaFoodEnum.name());
                foodLevel = foodDietUtil.getPenaltyFoodLevel(0, foodLevel);
                saturation = foodDietUtil.getPenaltySaturation(0, saturation);
                foodDietUtil.applyPenaltyPotionEffect(player, 0);
            }
        }
        int maxFoodLevel = playerHungerUtil.getMaxFoodLevel();
        int calFoodLevel = player.getFoodLevel() + foodLevel;
        if (calFoodLevel > maxFoodLevel) {
            calFoodLevel = maxFoodLevel;
        }
        float calSaturation = player.getSaturation() + saturation;
        if (calSaturation > playerHungerUtil.getMaxSaturation()) {
            calSaturation = playerHungerUtil.getMaxSaturation();
        }
        player.setFoodLevel(calFoodLevel);
        player.setSaturation(calSaturation);
        if (hasPotionEffect(vanillaFoodEnum)) {
            applyPotionEffect(player, vanillaFoodEnum);
        }
        if (hasCommand(vanillaFoodEnum)) {
            applyExecuteCommand(player, vanillaFoodEnum);
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

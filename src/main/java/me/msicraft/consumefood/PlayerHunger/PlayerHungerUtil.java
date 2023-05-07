package me.msicraft.consumefood.PlayerHunger;

import me.msicraft.consumefood.API.Util.Util;
import me.msicraft.consumefood.ConsumeFood;
import me.msicraft.consumefood.FoodDiet.FoodDietUtil;
import org.bukkit.entity.Player;

import java.util.UUID;

public class PlayerHungerUtil {

    public boolean isEnabled() {
        boolean check = false;
        if (ConsumeFood.getPlugin().getConfig().contains("CustomSetting.Enabled-CustomFoodLevel")) {
            check = ConsumeFood.getPlugin().getConfig().getBoolean("CustomSetting.Enabled-CustomFoodLevel");
        }
        return check;
    }

    public boolean isEnabledSync() {
        boolean check = false;
        if (ConsumeFood.getPlugin().getConfig().contains("CustomSetting.Sync-CustomFoodLevel")) {
            check = ConsumeFood.getPlugin().getConfig().getBoolean("CustomSetting.Sync-CustomFoodLevel");
        }
        return check;
    }

    public int getFileCustomFoodLevel(Player player) {
        int food = 0;
        if (ConsumeFood.playerData.getConfig().contains("Player." + player.getUniqueId() + ".FoodLevel")) {
            food = ConsumeFood.playerData.getConfig().getInt("Player." + player.getUniqueId() + ".FoodLevel");
        }
        return food;
    }

    public int getMapCustomFoodLevel(Player player) {
        int food = 0;
        if (ConsumeFood.customFoodLevel.containsKey(player.getUniqueId())) {
            food = ConsumeFood.customFoodLevel.get(player.getUniqueId());
        }
        return food;
    }

    public void loadCustomFoodLevel(Player player) {
        int foodLevel = getFileCustomFoodLevel(player);
        ConsumeFood.customFoodLevel.put(player.getUniqueId(), foodLevel);
    }

    public void saveCustomFoodLevel(Player player) {
        if (ConsumeFood.customFoodLevel.containsKey(player.getUniqueId())) {
            int foodLevel = ConsumeFood.customFoodLevel.get(player.getUniqueId());
            ConsumeFood.playerData.getConfig().set("Player." + player.getUniqueId() + ".FoodLevel", foodLevel);
            ConsumeFood.playerData.saveConfig();
        }
    }

    public void saveAllCustomFoodMap() {
        for (UUID uuid : ConsumeFood.customFoodLevel.keySet()) {
            int foodLevel = ConsumeFood.customFoodLevel.get(uuid);
            ConsumeFood.playerData.getConfig().set("Player." + uuid + ".FoodLevel", foodLevel);
        }
        ConsumeFood.playerData.saveConfig();
    }

    public int getMaxFoodLevel() {
        int max = 20;
        if (ConsumeFood.getPlugin().getConfig().contains("CustomSetting.Max-FoodLevel")) {
            max = ConsumeFood.getPlugin().getConfig().getInt("CustomSetting.Max-FoodLevel");
        }
        return max;
    }

    public float getMaxSaturation() {
        float max = 10;
        if (ConsumeFood.getPlugin().getConfig().contains("CustomSetting.Max-Saturation")) {
            max = (float) ConsumeFood.getPlugin().getConfig().getDouble("CustomSetting.Max-Saturation");
        }
        return max;
    }

    public int getMinSprintFoodLevel() {
        int level = 6;
        if (ConsumeFood.getPlugin().getConfig().contains("CustomSetting.Sprint-FoodLevel")) {
            level = ConsumeFood.getPlugin().getConfig().getInt("CustomSetting.Sprint-FoodLevel");
        }
        return level;
    }

    private final FoodDietUtil foodDietUtil = new FoodDietUtil();

    public void addCustomFoodLevel(Player player, int amount, String food) {
        if (FoodDietUtil.isEnabled) {
            if (foodDietUtil.containFoodInDietMap(player, food)) {
                int count = foodDietUtil.getPenaltyCount(player, food);
                amount = foodDietUtil.getPenaltyFoodLevel(count, amount);
            } else {
                amount = foodDietUtil.getPenaltyFoodLevel(0, amount);
            }
        }
        int value = getMapCustomFoodLevel(player) + amount;
        if (value < 0) {
            value = 0;
        }
        if (value > getMaxFoodLevel()) {
            value = getMaxFoodLevel();
        }
        ConsumeFood.customFoodLevel.put(player.getUniqueId(), value);
    }

    public void setCustomFoodLevel(Player player, int amount) {
        if (amount < 0) {
            amount = 0;
        }
        if (amount > getMaxFoodLevel()) {
            amount = getMaxFoodLevel();
        }
        ConsumeFood.customFoodLevel.put(player.getUniqueId(), amount);
    }

    private final Util util = new Util();

    public void syncPlayerHunger(Player player) {
        if (isEnabledSync() && isEnabled()) {
            int maxFoodLevel = 20;
            int minSprintFoodLevel = getMinSprintFoodLevel();
            int getCustomFoodLevel = getMapCustomFoodLevel(player);
            double maxCustomFoodLevel = getMaxFoodLevel();
            double percent = (getCustomFoodLevel / maxCustomFoodLevel);
            int calFood = (int) Math.round(maxFoodLevel*percent);
            if (getCustomFoodLevel <= minSprintFoodLevel) {
                if (calFood >= 6) {
                    calFood = 5;
                }
            } else {
                if (calFood <= 6) {
                    calFood = 7;
                }
            }
            player.setFoodLevel(calFood);
        }
    }

}

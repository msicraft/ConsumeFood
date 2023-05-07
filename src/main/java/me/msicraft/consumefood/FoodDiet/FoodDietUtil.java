package me.msicraft.consumefood.FoodDiet;

import me.msicraft.consumefood.ConsumeFood;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.*;

public class FoodDietUtil {

    private static Map<UUID, Map<String, Integer>> foodDietMap = null; //uuid, map<food, count>

    public static void removeMap(Player player) { foodDietMap.remove(player.getUniqueId()); }

    public static boolean isEnabled = false;
    public static int maxPenaltyCount = 0;
    public static int recoveryPenaltyCount = 1;

    public static void reloadVariables() {
        isEnabled = ConsumeFood.getPlugin().getConfig().contains("FoodDiet.Enabled") && ConsumeFood.getPlugin().getConfig().getBoolean("FoodDiet.Enabled");
        maxPenaltyCount = ConsumeFood.getPlugin().getConfig().contains("FoodDiet.MaxPenaltyCount") ? ConsumeFood.getPlugin().getConfig().getInt("FoodDiet.MaxPenaltyCount") : 0;
        recoveryPenaltyCount = ConsumeFood.getPlugin().getConfig().contains("FoodDiet.RecoveryPenaltyCount") ? ConsumeFood.getPlugin().getConfig().getInt("FoodDiet.RecoveryPenaltyCount") : 1;
        if (isEnabled) {
            foodDietMap = new HashMap<>();
        }
    }

    public boolean containFoodInDietMap(Player player, String food) {
        return getDietMap(player).containsKey(food);
    }

    private Map<String, Integer> getDietMap(Player player) {
        Map<String, Integer> map;
        if (foodDietMap.containsKey(player.getUniqueId())) {
            map = foodDietMap.get(player.getUniqueId());
        } else {
            map = new HashMap<>();
        }
        return map; 
    }

    public int getPenaltyCount(Player player, String food) {
        int c = 0;
        Map<String, Integer> map = getDietMap(player);
        if (map.containsKey(food)) {
            c = map.get(food);
        }
        return c;
    }

    public void addPenaltyCount(Player player, String food, int amount) {
        Map<String, Integer> map = getDietMap(player);
        int v = 0;
        if (map.containsKey(food)) {
            v = map.get(food);
        }
        int cal = v + amount;
        if (cal < 0) {
            cal = 0;
        }
        if (cal > maxPenaltyCount) {
            cal = maxPenaltyCount;
        }
        map.put(food, cal);
    }

    public void reduceOtherPenaltyCount(Player player, String exceptFood) {
        Map<String, Integer> map = getDietMap(player);
        for (String s : map.keySet()) {
            if (!s.equals(exceptFood)) {
                int v = map.get(s) - recoveryPenaltyCount;
                if (v < 0) {
                    v = 0;
                }
                if (v > maxPenaltyCount) {
                    v = maxPenaltyCount;
                }
                map.put(s, v);
            }
        }
    }

    public void applyPenaltyPotionEffect(Player player, int count) {
        List<String> potionEffects = getPotionEffects(count);
        if (!potionEffects.isEmpty()) {
            for (String s : potionEffects) {
                String[] a = s.split(":");
                PotionEffectType potionEffectType = PotionEffectType.getByName(a[0].toUpperCase());
                int level = Integer.parseInt(a[1]);
                int duration = Integer.parseInt(a[2]);
                double chance = Double.parseDouble(a[3]);
                if (potionEffectType != null) {
                    if (Math.random() <= chance) {
                        int potionLevel = level - 1;
                        if (potionLevel < 0) {
                            potionLevel = 0;
                        }
                        player.addPotionEffect(new PotionEffect(potionEffectType, (duration * 20), potionLevel));
                    }
                } else {
                    Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + ConsumeFood.prefix + ChatColor.RED + " Invalid Potion effect: " + ChatColor.WHITE + a[0] + " | PenaltyCount: " + count);
                }
            }
        }
    }

    public int getPenaltyFoodLevel(int penaltyCount, int foodlevel) {
        double percent = getFoodLevelPercent(penaltyCount);
        int v = (int) (foodlevel - (Math.round(foodlevel * percent)));
        if (v < 0) {
            v = 0;
        }
        return v;
    }

    public float getPenaltySaturation(int penaltyCount, float saturation) {
        double percent = getSaturationPercent(penaltyCount);
        float v = (saturation - (Math.round(saturation * percent)));
        if (v < 0) {
            v = 0;
        }
        return v;
    }

    public double getFoodLevelPercent(int penaltyCount) {
        double v = 0;
        if (ConsumeFood.getPlugin().getConfig().contains("FoodDiet.Penalty." + penaltyCount + ".FoodLevel")) {
            v = ConsumeFood.getPlugin().getConfig().getInt("FoodDiet.Penalty." + penaltyCount + ".FoodLevel");
        }
        return v;
    }

    public double getSaturationPercent(int penaltyCount) {
        double v = 0;
        if (ConsumeFood.getPlugin().getConfig().contains("FoodDiet.Penalty." + penaltyCount + ".Saturation")) {
            v = ConsumeFood.getPlugin().getConfig().getInt("FoodDiet.Penalty." + penaltyCount + ".Saturation");
        }
        return v;
    }

    public List<String> getPotionEffects(int penaltyCount) {
        List<String> list = new ArrayList<>();
        if (ConsumeFood.getPlugin().getConfig().contains("FoodDiet.Penalty." + penaltyCount + ".PotionEffect")) {
            list = ConsumeFood.getPlugin().getConfig().getStringList("FoodDiet.Penalty." + penaltyCount + ".PotionEffect");
        }
        return list;
    }

}

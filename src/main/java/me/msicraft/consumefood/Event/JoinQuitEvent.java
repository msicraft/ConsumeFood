package me.msicraft.consumefood.Event;

import me.msicraft.consumefood.ConsumeFood;
import me.msicraft.consumefood.Enum.CustomFoodEditEnum;
import me.msicraft.consumefood.FoodDiet.FoodDietUtil;
import me.msicraft.consumefood.PlayerHunger.PlayerHungerUtil;
import me.msicraft.consumefood.PlayerHunger.Task.PlayerHungerTask;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashMap;

public class JoinQuitEvent implements Listener {

    private final PlayerHungerUtil playerHungerUtil = new PlayerHungerUtil();

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player player = e.getPlayer();
        if (player.isOp()) {
            if (!ConsumeFood.editCustomFood.containsKey(player.getUniqueId())) {
                HashMap<CustomFoodEditEnum, Boolean> temp =  new HashMap<>();
                for (CustomFoodEditEnum em : CustomFoodEditEnum.values()) {
                    temp.put(em, false);
                }
                ConsumeFood.editCustomFood.put(player.getUniqueId(), temp);
            }
            if (!ConsumeFood.editingCustomFood.containsKey(player.getUniqueId())) {
                ConsumeFood.editingCustomFood.put(player.getUniqueId(), null);
            }
        }
        if (!ConsumeFood.customFoodLevel.containsKey(player.getUniqueId())) {
            ConsumeFood.customFoodLevel.put(player.getUniqueId(), playerHungerUtil.getFileCustomFoodLevel(player));
        }
        if (!ConsumeFood.playerData.getConfig().contains("Player." + player.getUniqueId())) {
            int foodLevel = playerHungerUtil.getMaxFoodLevel();
            if (player.hasPlayedBefore()) {
                double percent = player.getFoodLevel() / 20.0;
                double cal = playerHungerUtil.getMaxFoodLevel() * percent;
                foodLevel = (int) Math.round(cal);
            }
            ConsumeFood.customFoodLevel.put(player.getUniqueId(), foodLevel);
            ConsumeFood.playerData.getConfig().set("Player." + player.getUniqueId() + ".FoodLevel", foodLevel);
            ConsumeFood.playerData.saveConfig();
        }
        BukkitTask playerHungerTask = new PlayerHungerTask(player).runTaskTimer(ConsumeFood.getPlugin(), 20L, 20L);
        //Bukkit.getConsoleSender().sendMessage("TaskId: " + playerHungerTask.getTaskId() + " | Player: " + player.getName());
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        Player player = e.getPlayer();
        ConsumeFood.editCustomFood.remove(player.getUniqueId());
        ConsumeFood.editingCustomFood.remove(player.getUniqueId());
        playerHungerUtil.saveCustomFoodLevel(player);
        ConsumeFood.customFoodLevel.remove(player.getUniqueId());
        FoodDietUtil.removeMap(player);
    }

}

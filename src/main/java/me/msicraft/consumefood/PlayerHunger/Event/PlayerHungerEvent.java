package me.msicraft.consumefood.PlayerHunger.Event;

import me.msicraft.consumefood.ConsumeFood;
import me.msicraft.consumefood.PlayerHunger.PlayerHungerUtil;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.FoodLevelChangeEvent;

public class PlayerHungerEvent implements Listener {

    private final PlayerHungerUtil playerHungerUtil = new PlayerHungerUtil();

    @EventHandler(priority = EventPriority.HIGH)
    public void onFoodLevelChange(FoodLevelChangeEvent e) {
        if (!ConsumeFood.isQuestOrAchievementCompatibility) {
            if (e.getEntity() instanceof Player) {
                Player player = (Player) e.getEntity();
                if (playerHungerUtil.isEnabledSync() && playerHungerUtil.isEnabled()) {
                    e.setCancelled(true);
                }
                int value = e.getFoodLevel() - player.getFoodLevel();
                int cal = playerHungerUtil.getMapCustomFoodLevel(player) + value;
                playerHungerUtil.setCustomFoodLevel(player, cal);
                playerHungerUtil.syncPlayerHunger(player);
            }
        }
    }

}

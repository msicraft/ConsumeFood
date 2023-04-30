package me.msicraft.consumefood.PlayerHunger.Task;

import me.msicraft.consumefood.PlayerHunger.PlayerHungerUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class PlayerHungerTask extends BukkitRunnable {

    private final PlayerHungerUtil playerHungerUtil = new PlayerHungerUtil();

    private Player player;

    public PlayerHungerTask(Player player) {
        this.player = player;
    }

    @Override
    public void run() {
        if (playerHungerUtil.isEnabled()) {
            if (player.isOnline()) {
                playerHungerUtil.syncPlayerHunger(player);
            } else {
                if (Bukkit.getScheduler().isCurrentlyRunning(this.getTaskId())) {
                    this.cancel();
                }
            }
        }
    }

}

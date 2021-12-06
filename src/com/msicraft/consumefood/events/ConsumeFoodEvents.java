package com.msicraft.consumefood.events;

import com.msicraft.consumefood.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.plugin.Plugin;



public class ConsumeFoodEvents implements Listener {

    Plugin plugin = ConsumeFood.getPlugin(ConsumeFood.class);

    @EventHandler
    public boolean onPlayerItemConsumeEvent(PlayerItemConsumeEvent e) {
        String foodnlist = String.valueOf(ConsumeFood.foodnamelist());
        Player player = e.getPlayer();
        String itemstack = e.getItem().getType().name().toUpperCase();
        boolean enablecheck = plugin.getConfig().getBoolean("MaxSetting.Enabled");
        int maxfoodlevel = plugin.getConfig().getInt("MaxSetting.FoodLevel");
        float maxsaturation = (float) plugin.getConfig().getDouble("MaxSetting.Saturation");
        if (foodnlist.contains(itemstack)) {
            if (foodnlist.contains(player.getInventory().getItemInOffHand().getType().name().toUpperCase())) {
                e.setCancelled(true);
                player.setFoodLevel(player.getFoodLevel() + plugin.getConfig().getInt("Food." + itemstack + ".FoodLevel"));
                player.setSaturation((float) (player.getSaturation() + plugin.getConfig().getDouble("Food." + itemstack + ".Saturation")));
                e.getPlayer().getInventory().getItemInOffHand().setAmount(e.getPlayer().getInventory().getItemInOffHand().getAmount() - 1);
                if (enablecheck) {
                    if (player.getFoodLevel() >= maxfoodlevel) {
                        player.setFoodLevel(maxfoodlevel);
                    }
                    if (player.getSaturation() >= maxsaturation) {
                        player.setSaturation(maxsaturation);
                    }
                } else {
                    if (player.getFoodLevel() >= 20) {
                        player.setFoodLevel(20);
                    }
                    if (player.getSaturation() >= 20) {
                        player.setSaturation(20);
                    }
                }
            }  else {
                e.setCancelled(true);
                player.setFoodLevel(player.getFoodLevel() + plugin.getConfig().getInt("Food." + itemstack + ".FoodLevel"));
                player.setSaturation((float) (player.getSaturation() + plugin.getConfig().getDouble("Food." + itemstack + ".Saturation")));
                e.getPlayer().getInventory().getItemInMainHand().setAmount(e.getPlayer().getInventory().getItemInMainHand().getAmount() - 1);
                if (enablecheck) {
                    if (player.getFoodLevel() >= maxfoodlevel) {
                        player.setFoodLevel(maxfoodlevel);
                    }
                    if (player.getSaturation() >= maxsaturation) {
                        player.setSaturation(maxsaturation);
                    }
                } else {
                    if (player.getFoodLevel() >= 20) {
                        player.setFoodLevel(20);
                    }
                    if (player.getSaturation() >= 20) {
                        player.setSaturation(20);
                    }
                }
            }
        }
        return true;
    }
}



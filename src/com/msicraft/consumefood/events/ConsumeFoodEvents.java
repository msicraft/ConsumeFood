package com.msicraft.consumefood.events;

import com.msicraft.consumefood.ConsumeFood;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.List;
import java.util.Random;


public class ConsumeFoodEvents implements Listener {

    Plugin plugin = ConsumeFood.getPlugin(ConsumeFood.class);

    Random randomchance = new Random();


    @EventHandler
    public boolean onPlayerItemConsumeEvent(PlayerItemConsumeEvent e) {
        String foodnlist = String.valueOf(ConsumeFood.foodnamelist());
        Player player = e.getPlayer();
        String itemstack = e.getItem().getType().name().toUpperCase();
        int maxfoodlevel = plugin.getConfig().getInt("MaxSetting.FoodLevel");
        float maxsaturation = (float) plugin.getConfig().getDouble("MaxSetting.Saturation");
        String buffdebufffoodlist = String.valueOf(ConsumeFood.buff_food_list());
        String buffdebuffpotioneffect = String.valueOf(plugin.getConfig().getStringList("Buff-Debuff_Food." + itemstack + ".PotionEffect"));
        double potioneffectchange = plugin.getConfig().getDouble("Buff-Debuff_Food." + itemstack + ".Chance");
        if (foodnlist.contains(itemstack)) {
            if (foodnlist.contains(player.getInventory().getItemInOffHand().getType().name().toUpperCase())) {
                e.setCancelled(true);
                player.setFoodLevel(player.getFoodLevel() + plugin.getConfig().getInt("Food." + itemstack + ".FoodLevel"));
                player.setSaturation((float) (player.getSaturation() + plugin.getConfig().getDouble("Food." + itemstack + ".Saturation")));
                e.getPlayer().getInventory().getItemInOffHand().setAmount(e.getPlayer().getInventory().getItemInOffHand().getAmount() - 1);
            }  else {
                e.setCancelled(true);
                player.setFoodLevel(player.getFoodLevel() + plugin.getConfig().getInt("Food." + itemstack + ".FoodLevel"));
                player.setSaturation((float) (player.getSaturation() + plugin.getConfig().getDouble("Food." + itemstack + ".Saturation")));
                e.getPlayer().getInventory().getItemInMainHand().setAmount(e.getPlayer().getInventory().getItemInMainHand().getAmount() - 1);
            }
        } else {
            if (buffdebufffoodlist.contains(itemstack)) {
                if (buffdebufffoodlist.contains(player.getInventory().getItemInOffHand().getType().name().toUpperCase())) {
                    e.setCancelled(true);
                    player.setFoodLevel(player.getFoodLevel() + plugin.getConfig().getInt("Buff-Debuff_Food." + itemstack + ".FoodLevel"));
                    player.setSaturation((float) (player.getSaturation() + plugin.getConfig().getDouble("Buff-Debuff_Food." + itemstack + ".Saturation")));
                    e.getPlayer().getInventory().getItemInOffHand().setAmount(e.getPlayer().getInventory().getItemInOffHand().getAmount() - 1);
                    if (buffdebuffpotioneffect != null) {
                        if (randomchance.nextDouble() <= potioneffectchange) {
                            List<String> getPotionEffects = plugin.getConfig().getStringList("Buff-Debuff_Food." + itemstack + ".PotionEffect");
                            for (String effectlistf : getPotionEffects) {
                                String [] effectlist = effectlistf.split(":");
                                PotionEffectType listpotiontype = PotionEffectType.getByName(effectlist[0]);
                                int listpotionlvl = Integer.parseInt(effectlist[1]);
                                int listpotionduration = Integer.parseInt(effectlist[2]);
                                if (listpotiontype != null) {
                                    player.addPotionEffect(new PotionEffect(listpotiontype, listpotionduration * 20, listpotionlvl - 1));
                                }
                            }
                        }
                    }
                }
                if (buffdebufffoodlist.contains(player.getInventory().getItemInMainHand().getType().name().toUpperCase())) {
                    e.setCancelled(true);
                    player.setFoodLevel(player.getFoodLevel() + plugin.getConfig().getInt("Buff-Debuff_Food." + itemstack + ".FoodLevel"));
                    player.setSaturation((float) (player.getSaturation() + plugin.getConfig().getDouble("Buff-Debuff_Food." + itemstack + ".Saturation")));
                    e.getPlayer().getInventory().getItemInMainHand().setAmount(e.getPlayer().getInventory().getItemInMainHand().getAmount() - 1);
                    if (buffdebuffpotioneffect != null) {
                        if (randomchance.nextDouble() <= potioneffectchange) {
                            List<String> getPotionEffects = plugin.getConfig().getStringList("Buff-Debuff_Food." + itemstack + ".PotionEffect");
                            for (String effectlistf : getPotionEffects) {
                                String [] effectlist = effectlistf.split(":");
                                PotionEffectType listpotiontype = PotionEffectType.getByName(effectlist[0]);
                                int listpotionlvl = Integer.parseInt(effectlist[1]);
                                int listpotionduration = Integer.parseInt(effectlist[2]);
                                if (listpotiontype != null) {
                                    player.addPotionEffect(new PotionEffect(listpotiontype, listpotionduration * 20, listpotionlvl - 1));
                                }
                            }
                        }
                    }
                }
            }
        }
        if (player.getFoodLevel() >= maxfoodlevel) {
            player.setFoodLevel(maxfoodlevel);
        }
        if (player.getSaturation() >= maxsaturation) {
            player.setSaturation(maxsaturation);
        }
        return true;
    }
}




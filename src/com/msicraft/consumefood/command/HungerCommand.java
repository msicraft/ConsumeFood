package com.msicraft.consumefood.command;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class HungerCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("Only Players can use that command");
            return true;
        }

        if (command.getName().equalsIgnoreCase("hunger")) {
            if (args.length == 0) {
                player.sendMessage(ChatColor.RED + "/hunger <player> <amount>");
            }
            if (args.length == 1) {
                player.sendMessage(ChatColor.RED + "Please set enter value 1 to 20");
            }
            if (args.length == 2) {
                Player target = Bukkit.getPlayerExact(args[0]);
                int hungervalue = Integer.parseInt(args[1]);
                if (target == null) {
                    player.sendMessage(ChatColor.RED + "Target Player is not Online");
                } else {
                    target.setFoodLevel(hungervalue);
                }
                if (hungervalue >= 21) {
                    if (target != null) {
                        target.setFoodLevel(20);
                    }
                }
            }
            if (args.length >= 3) {
                player.sendMessage(ChatColor.RED + "/hunger <player> <amount>");
            }
        }
        if (command.getName().equalsIgnoreCase("saturation")) {
            if (args.length == 0) {
                player.sendMessage(ChatColor.RED + "/saturation <player> <amount>");
            }
            if (args.length == 1) {
                player.sendMessage(ChatColor.RED + "Please set enter value 1 to 20");
            }
            if (args.length == 2) {
                Player target = Bukkit.getPlayerExact(args[0]);
                int saturationvalue = Integer.parseInt(args[1]);
                if (target == null) {
                    player.sendMessage(ChatColor.RED + "Target Player is not Online");
                }  else {
                    target.setSaturation(saturationvalue);
                }
                if (saturationvalue >= 21) {
                    if (target != null) {
                        target.setSaturation(20);
                    }
                }
            }
            if (args.length >= 3) {
                player.sendMessage(ChatColor.RED + "/saturation <player> <amount>");
            }
        }
        return true;
    }
}
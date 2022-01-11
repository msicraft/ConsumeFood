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
                player.sendMessage(ChatColor.RED + "Please enter a value");
            }
            if (args.length == 2) {
                Player target = Bukkit.getPlayerExact(args[0]);
                String S_target = String.valueOf(Bukkit.getPlayerExact(args[0]));
                int hungervalue = Integer.parseInt(args[1]);
                if (target == null) {
                    player.sendMessage(ChatColor.RED + S_target + " is not Online");
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
                player.sendMessage(ChatColor.RED + "Please enter a value");
            }
            if (args.length == 2) {
                Player target = Bukkit.getPlayerExact(args[0]);
                String S_target = String.valueOf(Bukkit.getPlayerExact(args[0]));
                float saturationvalue = Float.parseFloat((args[1]));
                if (target == null) {
                    player.sendMessage(ChatColor.RED + S_target + " is not Online");
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

        if (command.getName().equalsIgnoreCase("gethunger")) {
            if (args.length == 0) {
                player.sendMessage(ChatColor.RED + "/gethunger <player>");
            }
            if (args.length == 1) {
                Player gettargetfoodlevel = Bukkit.getPlayerExact(args[0]);
                String S_target = args[0];
                if (gettargetfoodlevel != null) {
                    int targetfoodlevel = gettargetfoodlevel.getFoodLevel();
                    player.sendMessage(ChatColor.GREEN + S_target + " Food Level is " + targetfoodlevel);
                }  else {
                    player.sendMessage(ChatColor.RED + S_target +" is not Online");
                }
            }
            if (args.length >= 2) {
                player.sendMessage(ChatColor.RED + "/gethunger <player>");
            }

        }

        if (command.getName().equalsIgnoreCase("getsaturation")) {
            if (args.length == 0) {
                player.sendMessage(ChatColor.RED + "/getsaturation <player>");
            }
            if (args.length == 1) {
                Player gettargetsaturaion = Bukkit.getPlayerExact(args[0]);
                String S_target = args[0];
                if (gettargetsaturaion != null) {
                    float targetsaturation = gettargetsaturaion.getSaturation();
                    player.sendMessage(ChatColor.GREEN + S_target + " Saturation is " + targetsaturation);
                }  else {
                    player.sendMessage(ChatColor.RED + S_target +" is not Online");
                }
            }
            if (args.length >= 2) {
                player.sendMessage(ChatColor.RED + "/getsaturation <player>");
            }
        }

        return true;
    }
}
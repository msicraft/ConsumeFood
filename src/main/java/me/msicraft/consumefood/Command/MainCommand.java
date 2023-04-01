package me.msicraft.consumefood.Command;

import me.msicraft.consumefood.CustomFood.CustomFoodUtil;
import me.msicraft.consumefood.ConsumeFood;
import me.msicraft.consumefood.CustomFood.Inventory.CustomFoodEditInv;
import me.msicraft.consumefood.PlayerHunger.PlayerHungerUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class MainCommand implements CommandExecutor {

    private final PlayerHungerUtil playerHungerUtil = new PlayerHungerUtil();
    private final CustomFoodUtil customFoodUtil = new CustomFoodUtil();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("consumefood")) {
            if (args.length == 0) {
                sender.sendMessage(ChatColor.RED + "/consumefood help");
            }
            if (args.length >= 1) {
                String var = args[0];
                if (var != null) {
                    switch (var) {
                        case "help":
                            if (args.length == 1) {
                                sender.sendMessage(ChatColor.YELLOW + "/consumefood help : " + ChatColor.WHITE + "Show the list of commands of the [ConsumeFood] plugin");
                                sender.sendMessage(ChatColor.YELLOW + "/consumefood reload : " + ChatColor.WHITE + "Reload the plugin config files");
                                sender.sendMessage(ChatColor.YELLOW + "/consumefood edit : " + ChatColor.WHITE + "Open Custom Food Edit Gui");
                                sender.sendMessage(ChatColor.YELLOW + "/consumefood get <player> <internalname> <amount> : " + ChatColor.WHITE + "Give custom food to <player>");
                                sender.sendMessage(ChatColor.YELLOW + "/consumefood hunger set <player> <amount> : " + ChatColor.WHITE + "Set player food level");
                                sender.sendMessage(ChatColor.YELLOW + "/consumefood hunger get <player> : " + ChatColor.WHITE + "Get player food level");
                                sender.sendMessage(ChatColor.YELLOW + "/consumefood saturation set <player> <amount> : " + ChatColor.WHITE + "Set player saturation");
                                sender.sendMessage(ChatColor.YELLOW + "/consumefood saturation get <player> : " + ChatColor.WHITE + "Get player saturation");
                                sender.sendMessage(ChatColor.YELLOW + "/consumefood customhunger get <player> : " + ChatColor.WHITE + "Get player custom food level");
                                sender.sendMessage(ChatColor.YELLOW + "/consumefood customhunger set <player> <amount> : " + ChatColor.WHITE + "Set player custom food level");
                            }
                            break;
                        case "reload":
                            if (args.length == 1) {
                                if (sender.isOp()) {
                                    ConsumeFood.getPlugin().configFilesReload();
                                    sender.sendMessage(ConsumeFood.prefix + ChatColor.GREEN + " Plugin config files reloaded");
                                }
                            }
                            break;
                        case "edit":
                            if (sender instanceof Player) {
                                Player player = (Player) sender;
                                if (player.isOp()) {
                                    CustomFoodEditInv customFoodEditInv = new CustomFoodEditInv(player);
                                    player.openInventory(customFoodEditInv.getInventory());
                                    customFoodEditInv.setMainInv(player);
                                } else {
                                    player.sendMessage(ChatColor.RED + "you don't have permission");
                                }
                            }
                            break;
                    }
                    if (args.length >= 2 && var.equals("get") && sender.isOp()) { //consume get <player> <internalname> <amount>
                        try {
                            if (args[1] != null && args[2] != null && args[3] != null) {
                                Player target = Bukkit.getPlayer(args[1]);
                                if (target != null) {
                                    String internalName = args[2];
                                    if (customFoodUtil.getInternalNames().contains(internalName)) {
                                        int amount = Integer.parseInt(args[3]);
                                        if (amount < 1) {
                                            amount = 1;
                                        }
                                        ItemStack itemStack = customFoodUtil.getCustomFood(internalName, ConsumeFood.bukkitBrandType);
                                        for (int a = 0; a<amount; a++) {
                                            target.getInventory().addItem(itemStack);
                                        }
                                    } else {
                                        sender.sendMessage(ChatColor.RED + "Invalid internalname: " + ChatColor.WHITE + internalName);
                                    }
                                } else {
                                    sender.sendMessage(ChatColor.RED + "Player is not online.");
                                }
                            } else {
                                sender.sendMessage(ChatColor.RED + "/consumefood get <player> <internalname> <amount>");
                            }
                        } catch (ArrayIndexOutOfBoundsException e) {
                            sender.sendMessage(ChatColor.RED + "/consumefood get <player> <internalname> <amount>");
                        }
                        return true;
                    }
                    if (args.length >= 2 && var.equals("hunger") && sender.isOp()) {
                        String s = args[1];
                        if (s != null) {
                            switch (s) {
                                case "get":
                                    if (args.length == 3) {
                                        Player target = Bukkit.getPlayer(args[2]);
                                        if (target != null) {
                                            int foodLevel = target.getFoodLevel();
                                            sender.sendMessage(ChatColor.GREEN + "Target Player: " + ChatColor.WHITE + target.getName());
                                            sender.sendMessage(ChatColor.GREEN + "Food Level: " + ChatColor.WHITE + foodLevel);

                                        } else {
                                            sender.sendMessage(ChatColor.RED + "Player is not online.");
                                        }
                                    } else {
                                        sender.sendMessage(ChatColor.RED + "/consumefood hunger get <player>");
                                    }
                                    break;
                                case "set":
                                    if (args.length == 4) {
                                        String valueS = args[3];
                                        if (valueS != null) {
                                            Player target = Bukkit.getPlayer(args[2]);
                                            if (target != null) {
                                                String replaceS = valueS.replaceAll("[^0-9]", "");
                                                int currentFood = target.getFoodLevel();
                                                int changeValue = currentFood;
                                                if (!replaceS.equals("")) {
                                                    changeValue = Integer.parseInt(replaceS);
                                                }
                                                target.setFoodLevel(changeValue);
                                                sender.sendMessage(ChatColor.GREEN + "Changed the player food level");
                                                sender.sendMessage(ChatColor.GREEN + "Target Player: " + ChatColor.WHITE + target.getName());
                                                sender.sendMessage(ChatColor.GREEN + "Before Food Level: " + ChatColor.WHITE + currentFood);
                                                sender.sendMessage(ChatColor.GREEN + "After Food Level: " + ChatColor.WHITE + changeValue);
                                            } else {
                                                sender.sendMessage(ChatColor.RED + "Player is not online.");
                                            }
                                        }
                                    } else {
                                        sender.sendMessage(ChatColor.RED + "/consumefood hunger set <player> <amount>");
                                    }
                            }
                        }
                        return true;
                    }
                    if (args.length >= 2 && var.equals("customhunger") && sender.isOp()) {
                        String s = args[1];
                        if (s != null) {
                            switch (s) {
                                case "get":
                                    if (args.length == 3) {
                                        Player target = Bukkit.getPlayer(args[2]);
                                        if (target != null) {
                                            int customFoodLevel = playerHungerUtil.getMapCustomFoodLevel(target);
                                            sender.sendMessage(ChatColor.GREEN + "Target Player: " + ChatColor.WHITE + target.getName());
                                            sender.sendMessage(ChatColor.GREEN + "Custom Food Level: " + ChatColor.WHITE + customFoodLevel);

                                        } else {
                                            sender.sendMessage(ChatColor.RED + "Player is not online.");
                                        }
                                    } else {
                                        sender.sendMessage(ChatColor.RED + "/consumefood customhunger get <player>");
                                    }
                                    break;
                                case "set":
                                    if (args.length == 4) {
                                        String valueS = args[3];
                                        if (valueS != null) {
                                            Player target = Bukkit.getPlayer(args[2]);
                                            if (target != null) {
                                                String replaceS = valueS.replaceAll("[^0-9]", "");
                                                int currentFood = playerHungerUtil.getMapCustomFoodLevel(target);
                                                int changeValue = currentFood;
                                                if (!replaceS.equals("")) {
                                                    changeValue = Integer.parseInt(replaceS);
                                                }
                                                playerHungerUtil.setCustomFoodLevel(target, changeValue);
                                                sender.sendMessage(ChatColor.GREEN + "Changed the player food level");
                                                sender.sendMessage(ChatColor.GREEN + "Target Player: " + ChatColor.WHITE + target.getName());
                                                sender.sendMessage(ChatColor.GREEN + "Before Custom Food Level: " + ChatColor.WHITE + currentFood);
                                                sender.sendMessage(ChatColor.GREEN + "After Custom Food Level: " + ChatColor.WHITE + changeValue);
                                            } else {
                                                sender.sendMessage(ChatColor.RED + "Player is not online.");
                                            }
                                        }
                                    } else {
                                        sender.sendMessage(ChatColor.RED + "/consumefood customhunger set <player> <amount>");
                                    }
                            }
                        }
                        return true;
                    }
                    if (args.length >= 2 && var.equals("saturation") && sender.isOp()) {
                        String s = args[1];
                        if (s != null) {
                            switch (s) {
                                case "get":
                                    if (args.length == 3) {
                                        Player target = Bukkit.getPlayer(args[2]);
                                        if (target != null) {
                                            sender.sendMessage(ChatColor.GREEN + "Target Player: " + ChatColor.WHITE + target.getName());
                                            sender.sendMessage(ChatColor.GREEN + "Saturation: " + ChatColor.WHITE + target.getSaturation());
                                        } else {
                                            sender.sendMessage(ChatColor.RED + "Player is not online.");
                                        }
                                    } else {
                                        sender.sendMessage(ChatColor.RED + "/consumefood saturation get <player>");
                                    }
                                    break;
                                case "set":
                                    if (args.length == 4) {
                                        String valueS = args[3];
                                        if (valueS != null) {
                                            Player target = Bukkit.getPlayer(args[2]);
                                            if (target != null) {
                                                String replaceS = valueS.replaceAll("[^0-9]", "");
                                                float currentFood = target.getSaturation();
                                                float changeValue = currentFood;
                                                if (!replaceS.equals("")) {
                                                    changeValue = Float.parseFloat(replaceS);
                                                }
                                                target.setSaturation(changeValue);
                                                sender.sendMessage(ChatColor.GREEN + "Changed the player food level");
                                                sender.sendMessage(ChatColor.GREEN + "Target Player: " + ChatColor.WHITE + target.getName());
                                                sender.sendMessage(ChatColor.GREEN + "Before: " + ChatColor.WHITE + currentFood);
                                                sender.sendMessage(ChatColor.GREEN + "After: " + ChatColor.WHITE + changeValue);
                                            } else {
                                                sender.sendMessage(ChatColor.RED + "Player is not online.");
                                            }
                                        }
                                    } else {
                                        sender.sendMessage(ChatColor.RED + "/consumefood saturation set <player> <amount>");
                                    }
                                    break;
                            }
                        }
                        return true;
                    }
                }
            }
        }
        return false;
    }
}

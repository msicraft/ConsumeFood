package me.msicraft.consumefood.Command;

import me.msicraft.consumefood.API.Util.Util;
import me.msicraft.consumefood.Compatibility.PlaceholderApi.PlaceHolderApiUtil;
import me.msicraft.consumefood.ConsumeFood;
import me.msicraft.consumefood.CustomFood.CustomFoodUtil;
import me.msicraft.consumefood.CustomFood.CustomItemUtil;
import me.msicraft.consumefood.CustomFood.Inventory.CustomFoodEditInv;
import me.msicraft.consumefood.Enum.CustomFoodEditEnum;
import me.msicraft.consumefood.FoodDiet.FoodDietUtil;
import me.msicraft.consumefood.PlayerHunger.PlayerHungerUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class MainCommand implements CommandExecutor {

    private final PlayerHungerUtil playerHungerUtil = new PlayerHungerUtil();
    private final CustomFoodUtil customFoodUtil = new CustomFoodUtil();
    private final Util util = new Util();
    private final CustomItemUtil customItemUtil = new CustomItemUtil();

    private void sendPermissionMessage(CommandSender sender) {
        String permissionMessage = util.getPermissionErrorMessage();
        if (permissionMessage != null && !permissionMessage.equals("")) {
            if (ConsumeFood.canUsePlaceHolderApi) {
                if (sender instanceof Player) {
                    Player player = (Player) sender;
                    permissionMessage = PlaceHolderApiUtil.getApplyPlaceHolder(player, permissionMessage);
                }
            }
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', permissionMessage));
        }
    }

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
                            if (!sender.hasPermission("consumefood.command.help")) {
                                sendPermissionMessage(sender);
                                return false;
                            }
                            if (args.length == 1) {
                                sender.sendMessage(ChatColor.YELLOW + "/consumefood help : " + ChatColor.WHITE + "Show the list of commands of the [ConsumeFood] plugin");
                                sender.sendMessage(ChatColor.YELLOW + "/consumefood reload : " + ChatColor.WHITE + "Reload the plugin config files");
                                sender.sendMessage(ChatColor.YELLOW + "/consumefood edit : " + ChatColor.WHITE + "Open Custom Food Edit Gui");
                                sender.sendMessage(ChatColor.YELLOW + "/consumefood get <player> <internalname> <amount> : " + ChatColor.WHITE + "Give custom food to <player>");
                                sender.sendMessage(ChatColor.YELLOW + "/consumefood hunger set <player> <amount> : " + ChatColor.WHITE + "Set player food level");
                                sender.sendMessage(ChatColor.YELLOW + "/consumefood hunger get <player> : " + ChatColor.WHITE + "Get player food level");
                                sender.sendMessage(ChatColor.YELLOW + "/consumefood saturation set <player> <amount> : " + ChatColor.WHITE + "Set player saturation");
                                sender.sendMessage(ChatColor.YELLOW + "/consumefood saturation get <player> : " + ChatColor.WHITE + "Get player saturation");
                                sender.sendMessage(ChatColor.YELLOW + "/consumefood customhunger set <player> <amount> : " + ChatColor.WHITE + "Set player custom food level");
                                sender.sendMessage(ChatColor.YELLOW + "/consumefood customhunger get <player> : " + ChatColor.WHITE + "Get player custom food level");
                                sender.sendMessage(ChatColor.YELLOW + "/consumefood import <internalname>: " + ChatColor.WHITE + "Import the data of an item in your hand");
                                sender.sendMessage(ChatColor.YELLOW + "/consumefood updateitem [optional:<player>, all]: " + ChatColor.WHITE + "Update customfood in inventory");
                            }
                            break;
                        case "reload":
                            if (args.length == 1) {
                                if (!sender.hasPermission("consumefood.command.reload")) {
                                    sendPermissionMessage(sender);
                                    return false;
                                }
                                ConsumeFood.getPlugin().configFilesReload();
                                sender.sendMessage(ConsumeFood.prefix + ChatColor.GREEN + " Plugin config files reloaded");
                            }
                            break;
                        case "edit":
                            if (sender instanceof Player) {
                                Player player = (Player) sender;
                                if (!player.hasPermission("consumefood.command.edit")) {
                                    sendPermissionMessage(sender);
                                    return false;
                                }
                                if (!ConsumeFood.editCustomFood.containsKey(player.getUniqueId())) {
                                    HashMap<CustomFoodEditEnum, Boolean> temp = new HashMap<>();
                                    for (CustomFoodEditEnum em : CustomFoodEditEnum.values()) {
                                        temp.put(em, false);
                                    }
                                    ConsumeFood.editCustomFood.put(player.getUniqueId(), temp);
                                }
                                if (!ConsumeFood.editingCustomFood.containsKey(player.getUniqueId())) {
                                    ConsumeFood.editingCustomFood.put(player.getUniqueId(), null);
                                }
                                CustomFoodEditInv customFoodEditInv = new CustomFoodEditInv(player);
                                player.openInventory(customFoodEditInv.getInventory());
                                customFoodEditInv.setMainInv(player);
                            }
                            break;
                    }
                    if (var.equals("updateitem")) { //consume updateitem <[optional]player, all>
                        if (!sender.hasPermission("consumefood.command.updateitem")) {
                            sendPermissionMessage(sender);
                            return false;
                        }
                        Player target = null;
                        String s = null;
                        try {
                            if (args[1].equalsIgnoreCase("all")) {
                                s = args[1];
                            } else {
                                target = Bukkit.getPlayer(args[1]);
                            }
                        } catch (ArrayIndexOutOfBoundsException e) {
                            target = (Player) sender;
                        }
                        if (s != null && s.equalsIgnoreCase("all")) {
                            customFoodUtil.updateInventory(sender);
                            return true;
                        }
                        if (target != null && target.isOnline()) {
                            customFoodUtil.updatePlayerInventory(target, sender);
                            return true;
                        } else {
                            sender.sendMessage(ChatColor.RED + "Player is not online.");
                        }
                        return false;
                    }
                    if (args.length >= 2 && var.equals("import")) { //consume import <internalname> <[optional]type:[0,1]>
                        if (!sender.hasPermission("consumefood.command.import")) {
                            sendPermissionMessage(sender);
                            return false;
                        }
                        if (sender instanceof Player) {
                            Player player = (Player) sender;
                            boolean isSuccess = false;
                            String internalName = null;
                            CustomItemUtil.importType importType = CustomItemUtil.importType.SIMPLE;
                            ItemStack itemStack = null;
                            try {
                                internalName = args[1];
                                //String type = args[2];
                                if (ConsumeFood.customFoodConfig.getConfig().contains("CustomFood." + internalName)) {
                                    player.sendMessage(ChatColor.RED + "This internalname already exists.");
                                    return false;
                                }
                                itemStack = player.getInventory().getItemInMainHand();
                                if (itemStack.getType() == Material.AIR) {
                                    player.sendMessage(ChatColor.RED + "Please hold the item in your hand");
                                    return false;
                                }
                                isSuccess = true;
                            } catch (ArrayIndexOutOfBoundsException e) {
                                player.sendMessage(ChatColor.RED + "/consumefood import <internalname> [importType:<simple,all>]");
                            }
                            if (isSuccess) {
                                try {
                                    importType = CustomItemUtil.importType.valueOf(args[2].toUpperCase());
                                } catch (ArrayIndexOutOfBoundsException | IllegalArgumentException ex) {
                                    //
                                    player.sendMessage(ChatColor.RED + "/consumefood import <internalname> [importType:<simple,all>]");
                                    return false;
                                }
                                ItemStack temp = new ItemStack(itemStack);
                                temp.setAmount(1);
                                customItemUtil.importItemStack(player, internalName, temp, importType, ConsumeFood.bukkitBrandType);
                                return true;
                            }
                        }
                    }
                    if (args.length >= 2 && var.equals("get")) { //consume get <player> <internalname> <amount>
                        if (!sender.hasPermission("consumefood.command.get")) {
                            sendPermissionMessage(sender);
                            return false;
                        }
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
                                        for (int a = 0; a < amount; a++) {
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
                    if (args.length >= 2 && var.equals("hunger")) {
                        if (!sender.hasPermission("consumefood.command.hunger")) {
                            sendPermissionMessage(sender);
                            return false;
                        }
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
                    if (args.length >= 2 && var.equals("customhunger")) {
                        if (!sender.hasPermission("consumefood.command.customhunger")) {
                            sendPermissionMessage(sender);
                            return false;
                        }
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
                    if (args.length >= 2 && var.equals("saturation")) {
                        if (!sender.hasPermission("consumefood.command.saturation")) {
                            sendPermissionMessage(sender);
                            return false;
                        }
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

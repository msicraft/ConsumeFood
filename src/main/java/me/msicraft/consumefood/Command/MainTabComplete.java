package me.msicraft.consumefood.Command;

import me.msicraft.consumefood.CustomFood.CustomFoodUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.ArrayList;
import java.util.List;

public class MainTabComplete implements TabCompleter {

    private final CustomFoodUtil customFoodUtil = new CustomFoodUtil();

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (command.getName().equalsIgnoreCase("consumefood")) {
            if (args.length == 1) {
                List<String> arguments = new ArrayList<>();
                arguments.add("help");
                arguments.add("reload");
                arguments.add("edit");
                arguments.add("get");
                arguments.add("hunger");
                arguments.add("customhunger");
                arguments.add("saturation");
                return arguments;
            }
            if (args.length == 3) {
                if (args[0].equals("get")) {
                    List<String> arguments = new ArrayList<>(customFoodUtil.getInternalNames());
                    return arguments;
                }
            }
            if (args.length == 2) {
                if (args[0].equals("hunger") || args[0].equals("customhunger") || args[0].equals("saturation")) {
                    List<String> arguments = new ArrayList<>();
                    arguments.add("get");
                    arguments.add("set");
                    return arguments;
                }
            }
        }
        return null;
    }

}

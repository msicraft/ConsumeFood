package me.msicraft.consumefood.CustomFood.Action;

import me.msicraft.consumefood.ConsumeFood;
import me.msicraft.consumefood.CustomFood.CustomFoodUtil;
import me.msicraft.consumefood.Enum.ActionMechanic;
import me.msicraft.consumefood.Enum.ActionTrigger;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ActionUtil {

    private static final Map<String, ActionTrigger> triggerMap = new HashMap<>();

    public static void reloadVariables() {
        if (!triggerMap.isEmpty()) {
            triggerMap.clear();
        }
        for (ActionTrigger trigger : ActionTrigger.values()) {
            triggerMap.put(trigger.getKey(), trigger);
        }
    }

    public static String getActionString(String internalName) {
        if (ConsumeFood.customFoodConfig.getConfig().contains("CustomFood." + internalName + ".Action")) {
            return ConsumeFood.customFoodConfig.getConfig().getString("CustomFood." + internalName + ".Action");
        }
        return null;
    }

    public static ActionTrigger getActionTrigger(String actionString) {
        try {
            String[] a = actionString.split("@");
            String triggerString = a[1].toLowerCase();
            if (triggerMap.containsKey(triggerString)) {
                return triggerMap.get(triggerString);
            }
        } catch (ArrayIndexOutOfBoundsException ignore) {
        }
        return null;
    }

    public static ActionMechanic getActionMechanic(String actionString) {
        try {
            String[] a = actionString.split("\\{");
            return ActionMechanic.valueOf(a[0].toUpperCase());
        } catch (IllegalArgumentException ignore) {
        }
        return null;
    }

    private static final Pattern MECHANIC_ATTRIBUTE_PATTERN = Pattern.compile("[{](.*?)[}]");

    public static String getMechanicAttributes(String actionString) {
        Matcher matcher = MECHANIC_ATTRIBUTE_PATTERN.matcher(actionString);
        String s = null;
        while (matcher.find()) {
            s = matcher.group(1);
            if (matcher.group(1) == null) {
                break;
            }
        }
        return s;
    }

    public static String getMechanicAttributeStringValue(String mechanicAttribute, String attribute) {
        String[] a = mechanicAttribute.split(";");
        try {
            for (String s : a) {
                if (s.contains(attribute)) {
                    return s.split("=")[1];
                }
            }
        } catch (ArrayIndexOutOfBoundsException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    private static final CustomFoodUtil customFoodUtil = new CustomFoodUtil();

    public static void applyAction(Player player, ActionMechanic actionMechanic, String mechanicAttribute) {
        try {
            switch (actionMechanic) {
                case GIVE:
                    String customFoodName = ActionUtil.getMechanicAttributeStringValue(mechanicAttribute, "internalname");
                    if (customFoodName != null) {
                        String amountS = ActionUtil.getMechanicAttributeStringValue(mechanicAttribute, "amount");
                        if (amountS != null) {
                            int amount = Integer.parseInt(amountS.replaceAll("[^0-9]", ""));
                            ItemStack customFood = customFoodUtil.getCustomFood(customFoodName, ConsumeFood.bukkitBrandType);
                            if (customFood != null) {
                                for (int a = 0; a<amount; a++) {
                                    player.getInventory().addItem(customFood);
                                }
                            }
                        }
                    }
                    break;
            }
        } catch (NumberFormatException numberFormatException) {
            numberFormatException.printStackTrace();
        }
    }

}

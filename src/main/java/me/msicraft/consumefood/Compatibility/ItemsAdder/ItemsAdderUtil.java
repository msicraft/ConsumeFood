package me.msicraft.consumefood.Compatibility.ItemsAdder;

import dev.lone.itemsadder.api.CustomStack;
import org.bukkit.inventory.ItemStack;

public class ItemsAdderUtil {

    public static boolean isItemsAdderItemStack(ItemStack itemStack) {
        CustomStack customStack = CustomStack.byItemStack(itemStack);
        return customStack == null;
    }

}

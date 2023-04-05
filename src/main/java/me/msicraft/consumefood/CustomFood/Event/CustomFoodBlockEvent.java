package me.msicraft.consumefood.CustomFood.Event;

import me.msicraft.consumefood.ConsumeFood;
import me.msicraft.consumefood.CustomFood.CustomFoodUtil;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;

public class CustomFoodBlockEvent implements Listener {

    private final CustomFoodUtil customFoodUtil = new CustomFoodUtil();

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent e) {
        if (ConsumeFood.customFoodConfig.getConfig().getBoolean("CustomFood-Disable-Block_Place.Enabled")) {
            ItemStack itemStack = e.getItemInHand();
            if (customFoodUtil.isCustomFood(itemStack)) {
                e.setCancelled(true);
            }
        }
    }

}

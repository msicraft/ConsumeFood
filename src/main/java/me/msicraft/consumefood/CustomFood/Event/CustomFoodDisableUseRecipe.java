package me.msicraft.consumefood.CustomFood.Event;

import me.msicraft.consumefood.CustomFood.CustomFoodUtil;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.enchantment.PrepareItemEnchantEvent;
import org.bukkit.event.inventory.FurnaceBurnEvent;
import org.bukkit.event.inventory.FurnaceSmeltEvent;
import org.bukkit.event.inventory.PrepareAnvilEvent;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.inventory.ItemStack;


public class CustomFoodDisableUseRecipe implements Listener {

    private final CustomFoodUtil customFoodUtil = new CustomFoodUtil();

    @EventHandler(priority = EventPriority.HIGH)
    public void customFoodDisableCrafting(PrepareItemCraftEvent e) {
        ItemStack[] itemStacks = e.getInventory().getMatrix();
        for (ItemStack itemStack : itemStacks) {
            if (itemStack != null && itemStack.getType() != Material.AIR) {
                if (customFoodUtil.hasDisableCraftingTag(itemStack)) {
                    e.getInventory().setResult(null);
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void customFoodDisableSmelting(FurnaceSmeltEvent e) {
        ItemStack itemStack = e.getSource();
        if (itemStack.getType() != Material.AIR) {
            if (customFoodUtil.hasDisableSmeltingTag(itemStack)) {
                e.setCancelled(true);
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void customFoodDisableSmelting2(FurnaceBurnEvent e) {
        ItemStack itemStack = e.getFuel();
        if (itemStack.getType() != Material.AIR) {
            if (customFoodUtil.hasDisableSmeltingTag(itemStack)) {
                e.setCancelled(true);
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void customFoodDisableAnvil(PrepareAnvilEvent e) {
        ItemStack[] itemStacks = e.getInventory().getStorageContents();
        for (ItemStack itemStack : itemStacks) {
            if (itemStack != null && itemStack.getType() != Material.AIR) {
                if (customFoodUtil.hasDisableAnvilTag(itemStack)) {
                    e.setResult(null);
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void customFoodDisableEnchant(PrepareItemEnchantEvent e) {
        ItemStack[] itemStacks = e.getInventory().getStorageContents();
        for (ItemStack itemStack : itemStacks) {
            if (itemStack != null && itemStack.getType() != Material.AIR) {
                if (customFoodUtil.hasDisableEnchantTag(itemStack)) {
                    e.setCancelled(true);
                }
            }
        }
    }

}

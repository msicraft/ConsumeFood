package me.msicraft.consumefood.API.CustomEvent;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

public class CustomFoodConsumeEvent extends Event {

    private final static HandlerList handlers = new HandlerList();

    private Player player;
    private int foodLevel;
    private float saturation;
    private EquipmentSlot useHand;
    private String cooldownType;
    private ItemStack itemStack;
    private boolean isSuccess;

    public CustomFoodConsumeEvent(Player player, int foodLevel, float saturation, EquipmentSlot useHand, String cooldownType, ItemStack itemStack, boolean isSuccess) {
        this.player = player;
        this.foodLevel = foodLevel;
        this.saturation = saturation;
        this.useHand = useHand;
        this.cooldownType = cooldownType;
        this.itemStack = itemStack;
        this.isSuccess = isSuccess;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

}

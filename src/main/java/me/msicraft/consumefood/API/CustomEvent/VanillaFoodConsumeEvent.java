package me.msicraft.consumefood.API.CustomEvent;

import me.msicraft.consumefood.Enum.VanillaFoodEnum;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

public class VanillaFoodConsumeEvent extends Event {

    private final static HandlerList handlers = new HandlerList();

    private Player player;
    private VanillaFoodEnum vanillaFoodEnum;
    private int foodLevel;
    private float saturation;
    private EquipmentSlot useHand;
    private String cooldownType;
    private ItemStack itemStack;
    private boolean isSuccess;

    public VanillaFoodConsumeEvent(Player player, ItemStack itemStack, VanillaFoodEnum vanillaFoodEnum, int foodLevel, float saturation, EquipmentSlot useHand, String cooldownType, boolean isSuccess) {
        this.player = player;
        this.vanillaFoodEnum = vanillaFoodEnum;
        this.foodLevel = foodLevel;
        this.saturation = saturation;
        this.useHand = useHand;
        this.cooldownType = cooldownType;
        this.itemStack = itemStack;
        this.isSuccess = isSuccess;
    }

    public Player getPlayer() {
        return player;
    }

    public VanillaFoodEnum getVanillaFoodEnum() {
        return vanillaFoodEnum;
    }

    public int getFoodLevel() {
        return foodLevel;
    }

    public float getSaturation() {
        return saturation;
    }

    public EquipmentSlot getUseHand() {
        return useHand;
    }

    public String getCooldownType() {
        return cooldownType;
    }

    public ItemStack getItemStack() {
        return itemStack;
    }

    public boolean isSuccess() {
        return isSuccess;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}

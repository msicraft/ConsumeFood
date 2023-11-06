package me.msicraft.consumefood.Enum;

public enum ActionTrigger {

    LEFT_CLICK("leftclick"), RIGHT_CLICK("rightclick"),
    SHIFT_LEFT_CLICK("shiftleftclick"), SHIFT_RIGHT_CLICK("shiftrightclick"),
    CONSUME("consume");

    private final String key;

    ActionTrigger(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }

}

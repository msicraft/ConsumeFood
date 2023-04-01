package me.msicraft.consumefood.Compatibility.PlaceholderApi;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import me.msicraft.consumefood.ConsumeFood;
import me.msicraft.consumefood.PlayerHunger.PlayerHungerUtil;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

public class ConsumeFoodPlaceholder extends PlaceholderExpansion {

    private final PlayerHungerUtil playerHungerUtil = new PlayerHungerUtil();

    ConsumeFood plugin;

    public ConsumeFoodPlaceholder(ConsumeFood plugin) {
        this.plugin = plugin;
    }

    @Override
    public String getIdentifier() {
        return "consumefood";
    }

    @Override
    public String getAuthor() {
        return "msicraftz";
    }

    @Override
    public String getVersion() {
        return "1.0";
    }

    @Override
    public String onRequest(OfflinePlayer player, String params) {
        if (params.equalsIgnoreCase("foodlevel")) {
            String s = "0";
            if (player.isOnline()) {
                Player onlineP = player.getPlayer();
                if (onlineP != null) {
                    s = String.valueOf(playerHungerUtil.getMapCustomFoodLevel(onlineP));
                }
            }
            return s;
        } else if (params.equalsIgnoreCase("max_foodlevel")) {
            return String.valueOf(playerHungerUtil.getMaxFoodLevel());
        } else if (params.equalsIgnoreCase("max_saturation")) {
            return String.valueOf(playerHungerUtil.getMaxSaturation());
        }
        return null;
    }

}

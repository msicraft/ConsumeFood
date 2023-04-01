package me.msicraft.consumefood.Compatibility.PlaceholderApi;

import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.entity.Player;


public class PlaceHolderApiUtil {

    public static String getApplyPlaceHolder(Player player, String message) {
        return PlaceholderAPI.setPlaceholders(player, message);
    }

}

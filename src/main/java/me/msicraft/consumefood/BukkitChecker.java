package me.msicraft.consumefood;

import com.destroystokyo.paper.profile.PlayerProfile;
import org.bukkit.Bukkit;

import java.util.UUID;

public class BukkitChecker {

    public static void canUsePaperApiTest() {
        PlayerProfile playerProfile = Bukkit.createProfile(UUID.randomUUID(), "test");
    }

}

package me.msicraft.consumefood;

import com.destroystokyo.paper.profile.PlayerProfile;
import org.bukkit.Bukkit;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Scanner;
import java.util.UUID;
import java.util.function.Consumer;

public class BukkitChecker {

    private final ConsumeFood plugin;
    private final int resourceId;

    public BukkitChecker(ConsumeFood plugin, int resourceId) {
        this.plugin = plugin;
        this.resourceId = resourceId;
    }

    public static void canUsePaperApiTest() {
        PlayerProfile playerProfile = Bukkit.createProfile(UUID.randomUUID(), "test");
    }

    public void getPluginUpdateCheck(Consumer<String> consumer) {
        Bukkit.getScheduler().runTaskAsynchronously(this.plugin, () -> {
            try (InputStream inputStream = new URL("https://api.spigotmc.org/legacy/update.php?resource=" + this.resourceId).openStream(); Scanner scanner = new Scanner(inputStream)) {
                if (scanner.hasNext()) {
                    consumer.accept(scanner.next());
                }
            } catch (IOException exception) {
                plugin.getLogger().info("Unable to check for updates: " + exception.getMessage());
            }
        });
    }

}

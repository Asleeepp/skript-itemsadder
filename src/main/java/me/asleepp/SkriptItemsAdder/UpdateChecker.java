package me.asleepp.SkriptItemsAdder;

import ch.njol.skript.util.Version;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

// Credits to ShaneBee, yoinked his Update Checker code from SkBee

public class UpdateChecker implements Listener {

    private static Version UPDATE_VERSION;

    public static void checkForUpdate(JavaPlugin plugin, String pluginVersion) {
        plugin.getLogger().info("Checking for update...");
        getLatestReleaseVersion(version -> {
            Version plugVer = new Version(pluginVersion);
            Version curVer = new Version(version);
            if (curVer.compareTo(plugVer) <= 0) {
                plugin.getLogger().info("Plugin is up to date!");
            } else {
                plugin.getLogger().warning("Plugin is not up to date!");
                plugin.getLogger().warning(" - Current version: " + pluginVersion);
                plugin.getLogger().warning(" - Available update: " + version);
                plugin.getLogger().warning(" - Download available at: https://github.com/Asleeepp/skript-itemsadder/releases");
                UPDATE_VERSION = curVer;
            }
        }, plugin);
    }

    private static void getLatestReleaseVersion(final Consumer<String> consumer, JavaPlugin plugin) {
        try {
            URL url = new URL("https://api.github.com/repos/Asleeepp/skript-itemsadder/releases/latest");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            JsonObject jsonObject = new JsonParser().parse(new InputStreamReader(connection.getInputStream())).getAsJsonObject();
            String tag_name = jsonObject.get("tag_name").getAsString();
            consumer.accept(tag_name);
        } catch (Exception e) {
            plugin.getLogger().severe("Checking for update failed: " + e.getMessage());
        }
    }


    private final SkriptItemsAdder plugin;

    public UpdateChecker(SkriptItemsAdder plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    private void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        if (!player.hasPermission("skript-itemsadder.update.check")) return;

        String currentVersion = this.plugin.getDescription().getVersion();
        CompletableFuture<Version> updateVersion = getUpdateVersion(currentVersion);

        Bukkit.getScheduler().runTaskLater(this.plugin, () -> updateVersion.thenApply(version -> {
            player.sendMessage("[skript-itemsadder] update available: " + version);
            player.sendMessage("[skript-itemsadder] download at https://github.com/Asleeepp/skript-itemsadder/releases");
            return true;
        }), 30);
    }

    private CompletableFuture<Version> getUpdateVersion(String currentVersion) {
        CompletableFuture<Version> future = new CompletableFuture<>();
        if (UPDATE_VERSION != null) {
            future.complete(UPDATE_VERSION);
        } else {
            Bukkit.getScheduler().runTaskAsynchronously(this.plugin, () -> getLatestReleaseVersion(version -> {
                Version plugVer = new Version(currentVersion);
                Version curVer = new Version(version);
                if (curVer.compareTo(plugVer) <= 0) {
                    future.cancel(true);
                } else {
                    UPDATE_VERSION = curVer;
                    future.complete(UPDATE_VERSION);
                }
            }, this.plugin));
        }
        return future;
    }
}

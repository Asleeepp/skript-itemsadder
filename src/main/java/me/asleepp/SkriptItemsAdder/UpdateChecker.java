package me.asleepp.SkriptItemsAdder;

import com.google.gson.JsonObject;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.CompletableFuture;

public class UpdateChecker implements Listener {

    private final JavaPlugin plugin;
    private final String currentVersion;
    private String latestVersion;

    public UpdateChecker(JavaPlugin plugin) {
        this.plugin = plugin;
        this.currentVersion = plugin.getDescription().getVersion();
        Bukkit.getPluginManager().registerEvents(this, plugin);
        checkForUpdate();
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        if (player.hasPermission("skript-nonplayercharacter.update.check") && latestVersion != null) {
            player.sendMessage(" ");
            player.sendMessage(MiniMessage.miniMessage().deserialize("<red>[<white>skript-itemsadder<red>] <white>skript-itemsadder is <red><bold>OUTDATED<white>!"));
            player.sendMessage(MiniMessage.miniMessage().deserialize("<red>[<white>skript-itemsadder<red>] <white>New version: " + latestVersion));
            player.sendMessage(MiniMessage.miniMessage().deserialize("<red>[<white>skript-itemsadder<red>] <white>Download at: <link>https://github.com/Asleeepp/skript-itemsadder/releases"));
            player.sendMessage(" ");
        }
    }

    private void checkForUpdate() {
        HttpClient httpClient = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://api.github.com/repos/asleeepp/skript-itemsadder/releases/latest"))
                .build();

        CompletableFuture<HttpResponse<String>> responseFuture = httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString());

        responseFuture.thenApply(HttpResponse::body)
                .thenAccept(body -> {
                    JsonObject jsonResponse = new JsonObject().getAsJsonObject();
                    latestVersion = jsonResponse.get("tag_name").getAsString();

                    if (!currentVersion.equals(latestVersion)) {
                        SkriptItemsAdder.getInstance().getLogger().warning("An update for skript-nonplayercharacter is available: " + latestVersion + " (current version: " + currentVersion + ")");
                    } else {
                        SkriptItemsAdder.getInstance().getLogger().info("skript-nonplayercharacter is up to date!");
                    }
                })
                .exceptionally(e -> {
                    SkriptItemsAdder.getInstance().getLogger().severe("Failed to check for updates: " + e.getMessage());
                    return null;
                });
    }
}
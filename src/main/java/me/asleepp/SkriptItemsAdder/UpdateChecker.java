package me.asleepp.SkriptItemsAdder;

import ch.njol.skript.util.Version;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.logging.Level;

/**
 * @author ShaneBeee, ImNotStable, Equipable
 */

public class UpdateChecker {

    public static void check(SkriptItemsAdder plugin) {
        String latestVersion = getLatestVersion();
        if (latestVersion == null)
            return;
        String currentVersion = plugin.getDescription().getVersion();
        if (new Version(currentVersion).isSmallerThan(new Version(latestVersion))) {
            SkriptItemsAdder.getInstance().getLogger().info("skript-itemsadder is NOT up to date!");
            SkriptItemsAdder.getInstance().getLogger().info("> Current Version: " + currentVersion);
            SkriptItemsAdder.getInstance().getLogger().info("> Latest Version: " + latestVersion);
            SkriptItemsAdder.getInstance().getLogger().info("> Download it at: https://github.com/Asleeepp/skript-itemsadder/releases");
            Bukkit.getPluginManager().registerEvents(new Listener() {
                @EventHandler
                public void on(PlayerJoinEvent event) {
                    Player player = event.getPlayer();
                    if (!player.hasPermission("skript-itemsadder.update.check") && !player.isOp()) return;

                    player.sendMessage(" ");
                    player.sendMessage(MiniMessage.miniMessage().deserialize("<red>[<white>skript-itemsadder<red>] <white>skript-itemsadder is <red><bold>OUTDATED<white>!"));
                    player.sendMessage(MiniMessage.miniMessage().deserialize("<red>[<white>skript-itemsadder<red>] <white>New version: " + latestVersion));
                    player.sendMessage(MiniMessage.miniMessage().deserialize("<red>[<white>skript-itemsadder<red>] <white>Download at: <link>https://github.com/Asleeepp/skript-itemsadder/releases"));
                    player.sendMessage(" ");
                }
            }, plugin);
        } else
            SkriptItemsAdder.getInstance().getLogger().info("skript-itemsadder is up to date!");
    }

    private static String getLatestVersion() {
        String url = "https://api.github.com/repos/Asleeepp/skript-itemsadder/releases/latest";
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new URL(url).openStream()))) {
            JsonObject jsonObject = new Gson().fromJson(reader, JsonObject.class);
            return jsonObject.get("tag_name").getAsString();
        } catch (Exception exception) {
            SkriptItemsAdder.getInstance().getLogger().log(Level.WARNING, "Failed to check for latest version.");
            exception.printStackTrace();
        }
        return null;
    }
}

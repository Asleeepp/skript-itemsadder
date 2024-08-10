package me.asleepp.SkriptItemsAdder.other.util;

import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Util {

    private static final String prefix = "<gradient:#04a0b5:#4287f5>skript-itemsadder</gradient> <gray>➜";

    public static void sendMiniMessage(Player player, String message) {
        player.sendMessage(MiniMessage.miniMessage().deserialize(message));
    }

    public static void sendMiniMessage(Player player, boolean usePrefix, String message) {
        String fullMessage = usePrefix ? prefix + " " + message : message;
        player.sendMessage(MiniMessage.miniMessage().deserialize(fullMessage));
    }

    public static void sendMiniMessage(CommandSender sender, String message) {
        if (sender instanceof Player) {
            sendMiniMessage((Player) sender, message);
        } else {
            sender.sendMessage(MiniMessage.miniMessage().deserialize(message));
        }
    }

    public static void sendMiniMessage(CommandSender sender, boolean usePrefix, String message) {
        String fullMessage = usePrefix ? prefix + " " + message : message;
        if (sender instanceof Player) {
            sendMiniMessage((Player) sender, fullMessage);
        } else {
            sender.sendMessage(MiniMessage.miniMessage().deserialize(fullMessage));
        }
    }

    public static String getPluginVersion(String pluginName) {
        Plugin plugin = Bukkit.getPluginManager().getPlugin(pluginName);
        if (plugin != null) {
            return plugin.getPluginMeta().getVersion();
        } else {
            return "Unknown";
        }
    }

}
package me.asleepp.SkriptItemsAdder.util;

import ch.njol.skript.Skript;
import dev.lone.itemsadder.api.ItemsAdder;
import me.asleepp.SkriptItemsAdder.SkriptItemsAdder;
import me.asleepp.SkriptItemsAdder.aliases.AliasesGenerator;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;

public class SkriptItemsAdderCommand implements CommandExecutor, TabCompleter {

    private SkriptItemsAdder plugin = SkriptItemsAdder.getInstance();
    private final AliasesGenerator aliasesGenerator = new AliasesGenerator(plugin);

    public SkriptItemsAdderCommand(SkriptItemsAdder plugin) {
        this.plugin = plugin;
        plugin.getCommand("skriptitemsadder").setExecutor(this);
        plugin.getCommand("skriptitemsadder").setTabCompleter(this);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if (args.length == 0) {
            Util.sendMiniMessage(sender, true, "<red>Please provide an argument!");
            return false;
        }
        if (args[0].equalsIgnoreCase("reload")) {
            switch (args[1]) {
                case "all":
                    Util.sendMiniMessage(sender, true, "<green>Reloading aliases and configuration...");

                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            aliasesGenerator.loadAliasesFile();
                            aliasesGenerator.loadAliasesFromFile();
                            aliasesGenerator.generateAliasesForAllItems();
                            aliasesGenerator.saveAliases();
                            aliasesGenerator.syncAliasesWithProvider();
                            sender.sendMessage("Aliases have been reloaded.");
                        }
                    }.runTaskAsynchronously(plugin);

                    plugin.reloadConfig();
                    break;
                case "config":
                    Util.sendMiniMessage(sender, true, "<green>Reloading the configuration...");
                    plugin.reloadConfig();
                    Util.sendMiniMessage(sender, true, "<green>Reloaded the configuration...");
                    break;
                case "aliases":
                    Util.sendMiniMessage(sender, true, "<green>Reloading aliases...");

                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            aliasesGenerator.loadAliasesFile();
                            aliasesGenerator.loadAliasesFromFile();
                            aliasesGenerator.generateAliasesForAllItems();
                            aliasesGenerator.saveAliases();
                            aliasesGenerator.syncAliasesWithProvider();
                            Util.sendMiniMessage(sender, true, "<green>Aliases have been reloaded.");
                        }
                    }.runTaskAsynchronously(plugin);
                    break;
            }

        }

        if (args[0].equalsIgnoreCase("info")) {
            Util.sendMiniMessage(sender, "");
            Util.sendMiniMessage(sender, "<aqua>-----------= <white>INFO <aqua>=-----------");
            Util.sendMiniMessage(sender, "");
            Util.sendMiniMessage(sender, "<gold>➥ <yellow>Total Items registered with ItemsAdder: <white>" + ItemsAdder.getAllItems().size());
            Util.sendMiniMessage(sender, "<gold>➥ <yellow>Total aliases registered with skript-itemsadder: <white>" + aliasesGenerator.getGeneratedAliases().size());
            Util.sendMiniMessage(sender, "<gold>➥ <yellow>Update Checker: <white>" + plugin.getConfig().getString("check-for-updates"));
            Util.sendMiniMessage(sender, "");
            Util.sendMiniMessage(sender, "<aqua>-----------= <white>MISC <aqua>=-----------");
            Util.sendMiniMessage(sender, "");
            Util.sendMiniMessage(sender, "<gold>➥ <yellow>Server Version: <white>" + Bukkit.getVersion());
            Util.sendMiniMessage(sender, "<gold>➥ <yellow>ItemsAdder Version: <white>" + Util.getPluginVersion("ItemsAdder"));
            Util.sendMiniMessage(sender, "<gold>➥ <yellow>LoneLibs Version: <white>" + Util.getPluginVersion("LoneLibs"));
            Util.sendMiniMessage(sender, "<gold>➥ <yellow>ProtocolLib Version: <white>" + Util.getPluginVersion("ProtocolLib"));
            Util.sendMiniMessage(sender, "<gold>➥ <yellow>Skript Version: <white>" + Skript.getVersion());
            Util.sendMiniMessage(sender, "");
            Util.sendMiniMessage(sender, "<dark_gray>➥ <gray>If you find any issues with skript-itemsadder, please report it to the GitHub: <green><click:open_url:https://github.com/Asleeepp/skript-itemsadder/issues>CLICK ME");
            Util.sendMiniMessage(sender, "");
            return true;
        }
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if (args.length == 1) {
            return List.of("reload", "info");
        } else if (args.length == 2) {
            return List.of("all", "config", "aliases");
        }
        return Collections.emptyList();
    }
}

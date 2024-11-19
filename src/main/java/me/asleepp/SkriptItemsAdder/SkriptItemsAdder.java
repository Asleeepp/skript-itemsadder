package me.asleepp.SkriptItemsAdder;

import java.io.IOException;

import ch.njol.skript.util.Version;
import lombok.Getter;
import me.asleepp.SkriptItemsAdder.aliases.AliasesGenerator;
import me.asleepp.SkriptItemsAdder.listeners.ItemsAdderEventListener;
import me.asleepp.SkriptItemsAdder.util.Metrics;
import me.asleepp.SkriptItemsAdder.util.SkriptItemsAdderCommand;
import me.asleepp.SkriptItemsAdder.util.UpdateChecker;
import me.asleepp.SkriptItemsAdder.util.Util;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import ch.njol.skript.Skript;
import ch.njol.skript.SkriptAddon;

import javax.annotation.Nullable;

public class SkriptItemsAdder extends JavaPlugin {

    private static SkriptAddon addon;
    private static SkriptItemsAdder instance;
    @Getter
    private AliasesGenerator aliasesGenerator;

    @Nullable
    public static SkriptItemsAdder getInstance() {
        return instance;
    }

    @Nullable
    public static SkriptAddon getAddonInstance() {
        return addon;
    }

    private final PluginManager manager = this.getServer().getPluginManager();

    @Override
    public void onEnable() {
        // Let's get this show on the road.
        long start = System.currentTimeMillis();
        final Plugin skript = manager.getPlugin("Skript");
        if (skript == null || !skript.isEnabled()) {
            getLogger().severe("Could not find Skript! Disabling...");
            manager.disablePlugin(this);
            return;
        } else if (Skript.getVersion().isSmallerThan(new Version(2, 7, 0))) {
            getLogger().severe("You are running an unsupported version of Skript. Disabling...");
            manager.disablePlugin(this);
            return;
        }
        if (!Skript.isAcceptRegistrations()) {
            getLogger().severe("The plugin can't load when it's already loaded! Disabling...");
            manager.disablePlugin(this);
            return;
        }

        final Plugin itemsadder = manager.getPlugin("ItemsAdder");
        if (itemsadder == null || !itemsadder.isEnabled()) {
            getLogger().severe("Could not find ItemsAdder! Disabling...");
            manager.disablePlugin(this);
            return;
        }

        this.aliasesGenerator = new AliasesGenerator(this);

        final SkriptItemsAdder plugin = this;

        // Pass the aliasesGenerator to the event listener
        ItemsAdderEventListener eventListener = new ItemsAdderEventListener(plugin, aliasesGenerator);
        manager.registerEvents(eventListener, plugin);

        int pluginId = 20971;
        Metrics metrics = new Metrics(this, pluginId);
        metrics.addCustomChart(new Metrics.SimplePie("skript_version", () -> Skript.getVersion().toString()));
        metrics.addCustomChart(new Metrics.SimplePie("itemsadder_version", () -> Util.getPluginVersion("ItemsAdder")));
        instance = this;
        addon = Skript.registerAddon(this);
        addon.setLanguageFileDirectory("lang");
        new SkriptItemsAdderCommand(this);

        saveDefaultConfig();

        try {
            addon.loadClasses("me.asleepp.SkriptItemsAdder", "elements");
        } catch (IOException error) {
            error.printStackTrace();
            manager.disablePlugin(this);
            return;
        }

        if (getConfig().getBoolean("check-for-updates", true))
            new UpdateChecker(this);

        long finish = System.currentTimeMillis() - start;
        getLogger().info("Successfully loaded skript-itemsadder in " + finish + "ms!");
        eventListener.generateAliases();
    }

    @Override
    public void onDisable() {
    }
}

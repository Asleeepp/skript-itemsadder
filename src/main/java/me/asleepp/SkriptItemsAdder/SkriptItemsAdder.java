package me.asleepp.SkriptItemsAdder;

import java.io.IOException;
import java.util.List;

import ch.njol.skript.util.Version;
import dev.lone.itemsadder.api.CustomStack;
import dev.lone.itemsadder.api.ItemsAdder;
import me.asleepp.SkriptItemsAdder.other.*;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import ch.njol.skript.Skript;
import ch.njol.skript.SkriptAddon;
import org.bukkit.scheduler.BukkitRunnable;

import javax.annotation.Nullable;

public class SkriptItemsAdder extends JavaPlugin {

    private static SkriptAddon addon;
    private static SkriptItemsAdder instance;
    private AliasesGenerator aliasesGenerator;

    @Nullable
    public static SkriptItemsAdder getInstance() {
        return instance;
    }

    @Nullable
    public static SkriptAddon getAddonInstance() {
        return addon;
    }

    public boolean itemsAdderReady = false;
    public boolean loading = true;

    @Override
    public void onEnable() {
        // Let's get this show on the road.
        long start = System.currentTimeMillis();
        final PluginManager manager = this.getServer().getPluginManager();
        final Plugin skript = manager.getPlugin("Skript");
        if (skript == null || !skript.isEnabled()) {
            getLogger().severe("Could not find Skript! Disabling...");
            manager.disablePlugin(this);
            return;
        } else if (Skript.getVersion().compareTo(new Version(2, 7, 0)) < 0) {
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
        new BukkitRunnable() {
            @Override
            public void run() {
                ItemsAdderEventListener eventListener = new ItemsAdderEventListener(plugin, aliasesGenerator);
                manager.registerEvents(eventListener, plugin);
            }
        }.runTask(this);


        getLogger().info("Waiting for ItemsAdder to finish loading.");

        getServer().getScheduler().runTaskTimer(this, () -> {
            if (itemsAdderReady) {
                return;
            }
        }, 0, 20);

        int pluginId = 20971;
        Metrics metrics = new Metrics(this, pluginId);
        metrics.addCustomChart(new Metrics.SimplePie("skript_version", () -> Skript.getVersion().toString()));
        metrics.addCustomChart(new Metrics.SimplePie("itemsadder_version", () -> Util.getPluginVersion("ItemsAdder")));
        instance = this;
        addon = Skript.registerAddon(this);
        addon.setLanguageFileDirectory("lang");
        new SkriptItemsAdderCommand(this);

        saveDefaultConfig();

        if (getConfig().getBoolean("check-for-updates", true))
            new UpdateChecker(this);

        try {
            addon.loadClasses("me.asleepp.SkriptItemsAdder");
        } catch (IOException error) {
            error.printStackTrace();
            manager.disablePlugin(this);
            return;
        }
        long finish = System.currentTimeMillis() - start;
        getLogger().info("Succesfully loaded skript-itemsadder in " + finish + "ms!");
        loading = false;
    }

    @Override
    public void onDisable() {
        aliasesGenerator.saveAliases();
    }

    public void setItemsAdderReady(boolean ready) {
        this.itemsAdderReady = ready;
    }

    public AliasesGenerator getAliasesGenerator() {
        return aliasesGenerator;
    }

}

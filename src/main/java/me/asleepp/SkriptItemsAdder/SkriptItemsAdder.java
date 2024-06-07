package me.asleepp.SkriptItemsAdder;

import java.io.IOException;


import ch.njol.skript.util.Version;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import ch.njol.skript.Skript;
import ch.njol.skript.SkriptAddon;

import javax.annotation.Nullable;

@SuppressWarnings("ALL")
public class SkriptItemsAdder extends JavaPlugin {

    private static SkriptAddon addon;

    private static SkriptItemsAdder instance;

    @Nullable
    public static SkriptItemsAdder getInstance() {
        return instance;
    }
    @Nullable
    public static SkriptAddon getAddonInstance() {
        return addon;
    }


    public void onEnable() {
        // Let's get this show on the road.
        final PluginManager manager = this.getServer().getPluginManager();
        final Plugin skript = manager.getPlugin("Skript");
        if (skript == null || !skript.isEnabled()) {
            getLogger().severe("Could not find Skript! Disabling...");
            manager.disablePlugin(this);
            return;
        } else if (Skript.getVersion().compareTo(new Version(2, 7, 0)) < 0) {
            getLogger().warning("You are running an unsupported version of Skript. Disabling...");
            manager.disablePlugin(this);
            return;
        }
        final Plugin itemsadder = manager.getPlugin("ItemsAdder");
        if (itemsadder == null || !itemsadder.isEnabled()) {
            getLogger().severe("Could not find ItemsAdder! Disabling...");
            manager.disablePlugin(this);
            return;
        }
        int pluginId = 20971;
        Metrics metrics = new Metrics(this, pluginId);
        instance = this;
        addon = Skript.registerAddon(this);
        addon.setLanguageFileDirectory("lang");
        new UpdateChecker(this);

        try {
            addon.loadClasses("me.asleepp.SkriptItemsAdder");
        } catch (IOException error) {
            error.printStackTrace();
            manager.disablePlugin(this);
            return;
        }


    }


}
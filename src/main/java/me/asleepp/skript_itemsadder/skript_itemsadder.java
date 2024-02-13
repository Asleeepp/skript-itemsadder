package me.example.addontutorial;

import java.io.IOException;

import org.bstats.bukkit.Metrics;
import org.bstats.charts.SimplePie;
import org.bukkit.plugin.java.JavaPlugin;

import ch.njol.skript.Skript;
import ch.njol.skript.SkriptAddon;

public class AddonTutorial extends JavaPlugin {

    private static AddonTutorial instance;
    private SkriptAddon addon;

    public void onEnable() {
//		if (!Skript.isRunningMinecraft(1, 13)) {
//			getPluginLoader().disablePlugin(this);
//			getLogger().info("AddonTutorial only works on 1.13+");
//			return;
//		}
        instance = this;
        try {
            addon = Skript.registerAddon(this)
                    .loadClasses("me.example.addontutorial", "elements")
                    .setLanguageFileDirectory("lang");
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Saves the raw contents of the default config.yml file to the locationretrievable by getConfig().
        saveDefaultConfig();
        if (!getDescription().getVersion().equalsIgnoreCase(getConfig().getString("version")))
            getLogger().info("There is a new configuration version! Please save your data and delete your config.yml to allow it to regenerate.");

        // Replace 1234 with your bStats plugin ID.
//		Metrics metrics = new Metrics(this, 1234);
//		metrics.addCustomChart(new SimplePie("example", () -> "some string"));
        getLogger().info("AddonTutorial has been enabled!");
    }

    public static AddonTutorial getInstance() {
        return instance;
    }

    public SkriptAddon getAddonInstance() {
        return addon;
    }

}
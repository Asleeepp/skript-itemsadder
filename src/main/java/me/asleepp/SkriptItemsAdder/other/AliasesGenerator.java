package me.asleepp.SkriptItemsAdder.other;

import dev.lone.itemsadder.api.CustomStack;
import dev.lone.itemsadder.api.ItemsAdder;
import me.asleepp.SkriptItemsAdder.SkriptItemsAdder;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class AliasesGenerator {

    private final SkriptItemsAdder plugin;
    private final File aliasesFile;
    private FileConfiguration aliasesConfig;
    private final Map<String, String> aliases = new HashMap<>();
    private final Set<String> generatedAliases = new HashSet<>();

    public AliasesGenerator(SkriptItemsAdder plugin) {
        this.plugin = plugin;
        this.aliasesFile = new File(plugin.getDataFolder(), "aliases.yml");
        loadAliasesFile();
        loadAliases();
    }

    public void loadAliasesFile() {
        if (!aliasesFile.exists()) {
            aliasesFile.getParentFile().mkdirs();
            plugin.saveResource("aliases.yml", false);
        }
        aliasesConfig = YamlConfiguration.loadConfiguration(aliasesFile);
    }

    private void loadAliases() {
        for (String key : aliasesConfig.getKeys(false)) {
            aliases.put(key, aliasesConfig.getString(key));
        }
        plugin.getLogger().info("Loaded " + aliases.size() + " aliases from file.");
    }

    public void saveAliases() {
        try {
            aliasesConfig.save(aliasesFile);
            plugin.getLogger().info("Aliases saved to " + aliasesFile.getPath());
        } catch (IOException e) {
            plugin.getLogger().severe("Failed to save aliases: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public String generateAliasesForItem(CustomStack item) {
        String name = item.getId().toLowerCase().replace("_", " ");
        String namespace = item.getNamespace();


        String existingAlias = getAliasForItem(item);
        if (existingAlias != null) {
            return existingAlias;
        } else {
            if (!aliases.containsValue(name)) {
                return name;
            } else {
                for (Map.Entry<String, String> entry : aliases.entrySet()) {
                    if (entry.getValue().equals(name) && !item.getNamespace().equals(entry.getKey().split(":")[0])) {
                        return item.getNamespace() + ":" + item.getId().toLowerCase().replace("_", " ");
                    }
                }
            }
        }
        int counter = 1;
        String uniqueAlias;
        do {
            uniqueAlias = name + "_" + counter;
            counter++;
        } while (aliases.containsValue(uniqueAlias));
        return uniqueAlias;
    }

    private String getAliasForItem(CustomStack item) {
        for (Map.Entry<String, String> entry : aliases.entrySet()) {
            if (entry.getValue().equals(item.getNamespacedID())) {
                return entry.getKey();
            }
        }
        return null;
    }

    public void generateAliasesForNewItems(List<CustomStack> newItems) {
        for (CustomStack item : newItems) {
            String alias = generateAliasesForItem(item);
            registerAlias(alias, item.getNamespacedID());
        }
        saveAliases();
    }

    public void generateAliasesForAllItems() {
        plugin.getLogger().info("Generating aliases for all items.");
        List<CustomStack> allItems = ItemsAdder.getAllItems();
        plugin.getLogger().info("Total items: " + allItems.size());
        for (CustomStack item : allItems) {
            String existingAlias = getAliasForItem(item);
            if (existingAlias == null) {
                String alias = generateAliasesForItem(item);
                plugin.getLogger().info("Generated alias: " + alias + " for item: " + item.getNamespacedID());
                registerAlias(alias, item.getNamespacedID());
            }
        }
        saveAliases();
        plugin.getLogger().info("Aliases have been generated");
    }


    public void registerAlias(String alias, String namespacedId) {
        aliases.put(alias, namespacedId);
        aliasesConfig.set(alias, namespacedId);
        generatedAliases.add(alias);
        plugin.getLogger().info("Registered alias: " + alias + " for item: " + namespacedId);
    }

    public String getNamespacedId(String alias) {
        return aliases.getOrDefault(alias, alias);
    }

    public Set<String> getGeneratedAliases() {
        return generatedAliases;
    }
}

package me.asleepp.SkriptItemsAdder.other;

import ch.njol.skript.Skript;
import ch.njol.skript.aliases.Aliases;
import ch.njol.skript.aliases.AliasesProvider;
import ch.njol.skript.aliases.InvalidMinecraftIdException;
import dev.lone.itemsadder.api.CustomStack;
import dev.lone.itemsadder.api.FontImages.FontImageWrapper;
import dev.lone.itemsadder.api.ItemsAdder;
import me.asleepp.SkriptItemsAdder.SkriptItemsAdder;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.HashSet;

public class AliasesGenerator {

    private final SkriptItemsAdder plugin;
    private final File aliasesFile;
    private FileConfiguration aliasesConfig;
    private final Map<String, String> itemAliases = new HashMap<>();
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
        if (aliasesConfig.contains("items")) {
            for (String key : aliasesConfig.getConfigurationSection("items").getKeys(false)) {
                itemAliases.put(key, aliasesConfig.getString(key));
            }
        }
        plugin.getLogger().info("Loaded " + itemAliases.size() + " aliases from file.");
    }

    public void saveAliases() {
        for (Map.Entry<String, String> entry : itemAliases.entrySet()) {
            aliasesConfig.set("items." + entry.getKey(), entry.getValue());
        }
        try {
            aliasesConfig.save(aliasesFile);
            plugin.getLogger().info("Aliases saved to " + aliasesFile.getPath());
        } catch (IOException e) {
            plugin.getLogger().severe("Failed to save aliases: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public String generateAliasForItem(CustomStack item) {
        String name = item.getId().toLowerCase().replace("_", " ");
        String namespace = item.getNamespace();

        String existingAlias = getAliasForItem(item);
        if (existingAlias != null) {
            return existingAlias;
        } else {
            if (!itemAliases.containsValue(name)) {
                return name;
            } else {
                for (Map.Entry<String, String> entry : itemAliases.entrySet()) {
                    if (entry.getValue().equals(name) && !namespace.equals(entry.getKey().split(":")[0])) {
                        return namespace + ":" + item.getId().toLowerCase().replace("_", " ");
                    }
                }
            }
        }
        int counter = 1;
        String uniqueAlias;
        do {
            uniqueAlias = name + "_" + counter;
            counter++;
        } while (itemAliases.containsValue(uniqueAlias));
        return uniqueAlias;
    }

    private String getAliasForItem(CustomStack item) {
        for (Map.Entry<String, String> entry : itemAliases.entrySet()) {
            if (entry.getValue().equals(item.getNamespacedID())) {
                return entry.getKey();
            }
        }
        return null;
    }

    public void generateAliasesForNewItems(List<CustomStack> newItems) {
        for (CustomStack item : newItems) {
            String alias = generateAliasForItem(item);
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
                String alias = generateAliasForItem(item);
                plugin.getLogger().info("Generated alias: " + alias + " for item: " + item.getNamespacedID());
                registerAlias(alias, item.getNamespacedID());
            }
        }
        saveAliases();
        plugin.getLogger().info("Aliases have been generated");
    }

    public void registerAlias(String alias, String namespacedId) {
        itemAliases.put(alias, namespacedId);
        aliasesConfig.set(alias, namespacedId);
        generatedAliases.add(alias);

        AliasesProvider addonProvider = Aliases.getAddonProvider(Skript.getAddonInstance());
        AliasesProvider.AliasName aliasName = new AliasesProvider.AliasName(alias, alias, 0);
        try {
            addonProvider.addAlias(aliasName, namespacedId, null, new HashMap<>());
            plugin.getLogger().info("Registered alias: " + alias + " for item: " + namespacedId + " with AliasesProvider.");
        } catch (NullPointerException e) {
            plugin.getLogger().severe("Failed to register alias with AliasesProvider: " + e.getMessage());
        }
    }

    public void syncAliasesWithProvider() {
        AliasesProvider addonProvider = Aliases.getAddonProvider(Skript.getAddonInstance());
        addonProvider.clearAliases();

        for (Map.Entry<String, String> entry : itemAliases.entrySet()) {
            AliasesProvider.AliasName aliasName = new AliasesProvider.AliasName(entry.getKey(), entry.getKey(), 0);
            try {
                addonProvider.addAlias(aliasName, entry.getValue(), null, new HashMap<>());
            } catch (InvalidMinecraftIdException e) {
                plugin.getLogger().severe("Failed to sync aliases! " + e.getMessage());
            }
        }
    }

    public void handleAliasChange(String alias, String newNamespacedId) {
        itemAliases.put(alias, newNamespacedId);
        aliasesConfig.set(alias, newNamespacedId);
        saveAliases();

        syncAliasesWithProvider();
    }

    public void handleItemUnregister(CustomStack item) {
        String alias = getAliasForItem(item);
        if (alias != null) {
            itemAliases.remove(alias);
            aliasesConfig.set(alias, null);
            saveAliases();
            syncAliasesWithProvider();
        }
    }

    public String getNamespacedId(String alias) {
        return itemAliases.getOrDefault(alias, alias);
    }

    public Set<String> getGeneratedAliases() {
        return generatedAliases;
    }

    public void handleRemovedItems(List<CustomStack> removedItems) {
        for (CustomStack item : removedItems) {
            handleItemUnregister(item);
        }
    }

}

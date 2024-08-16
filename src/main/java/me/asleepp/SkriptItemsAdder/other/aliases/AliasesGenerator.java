package me.asleepp.SkriptItemsAdder.other.aliases;

import ch.njol.skript.Skript;
import ch.njol.skript.aliases.Aliases;
import ch.njol.skript.aliases.AliasesProvider;
import ch.njol.skript.aliases.InvalidMinecraftIdException;
import dev.lone.itemsadder.api.CustomStack;
import dev.lone.itemsadder.api.ItemsAdder;
import lombok.Getter;
import me.asleepp.SkriptItemsAdder.SkriptItemsAdder;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class AliasesGenerator {

    private final SkriptItemsAdder plugin;
    private final File aliasesFile;
    private FileConfiguration aliasesConfig;
    private final Map<String, String> itemAliases = new ConcurrentHashMap<>();
    @Getter
    private final Set<String> generatedAliases = ConcurrentHashMap.newKeySet();

    public AliasesGenerator(SkriptItemsAdder plugin) {
        this.plugin = plugin;
        this.aliasesFile = new File(plugin.getDataFolder(), "aliases.yml");
        loadAliasesFile();
        loadAliasesFromFile();
        //watchAliasesFile();
    }

    public void loadAliasesFile() {
        if (!aliasesFile.exists()) {
            aliasesFile.getParentFile().mkdirs();
            plugin.saveResource("aliases.yml", false);
        }
        aliasesConfig = YamlConfiguration.loadConfiguration(aliasesFile);
    }

    public void loadAliasesFromFile() {
        if (aliasesConfig.contains("items")) {
            for (String key : aliasesConfig.getConfigurationSection("items").getKeys(false)) {
                itemAliases.put(key, aliasesConfig.getString("items." + key));
            }
        }
    }

    private void saveAliasesToFile() {
        new BukkitRunnable() {
            @Override
            public void run() {
                for (Map.Entry<String, String> entry : itemAliases.entrySet()) {
                    aliasesConfig.set("items." + entry.getKey(), entry.getValue());
                }
                try {
                    aliasesConfig.save(aliasesFile);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.runTaskAsynchronously(plugin);
    }

    private String generateUniqueAlias(String baseAlias) {
        String uniqueAlias = baseAlias;
        int counter = 1;
        while (itemAliases.containsKey(uniqueAlias)) {
            uniqueAlias = baseAlias + "_" + counter;
            counter++;
        }
        return uniqueAlias;
    }

    private String generateAliasForItem(CustomStack item) {
        String baseAlias = item.getId().toLowerCase().replace("_", " ");
        if (itemAliases.containsValue(baseAlias)) {
            return generateUniqueAlias(baseAlias);
        }
        return baseAlias;
    }

    private void registerAlias(String alias, String namespacedId) {
        itemAliases.put(alias, namespacedId);
        generatedAliases.add(alias);

        AliasesProvider addonProvider = Aliases.getAddonProvider(Skript.getAddonInstance());
        AliasesProvider.AliasName aliasName = new AliasesProvider.AliasName(alias, alias, 0);
        try {
            addonProvider.addAlias(aliasName, namespacedId, null, new HashMap<>());
        } catch (InvalidMinecraftIdException ignored) {
        }
    }

    public void generateAliasesForAllItems() {
        new BukkitRunnable() {
            @Override
            public void run() {
                List<CustomStack> allItems = ItemsAdder.getAllItems();

                Set<String> currentNamespaces = allItems.stream()
                        .map(CustomStack::getNamespacedID)
                        .collect(Collectors.toSet());

                boolean allAliasesExist = currentNamespaces.stream()
                        .allMatch(itemAliases::containsValue);

                if (allAliasesExist && currentNamespaces.size() == itemAliases.size()) {
                    return;
                }

                for (CustomStack item : allItems) {
                    String namespacedId = item.getNamespacedID();
                    if (!itemAliases.containsValue(namespacedId)) {
                        String alias = generateAliasForItem(item);
                        registerAlias(alias, namespacedId);
                    }
                }

                Set<String> unusedAliases = itemAliases.keySet().stream()
                        .filter(alias -> !generatedAliases.contains(alias))
                        .collect(Collectors.toSet());
                for (String unusedAlias : unusedAliases) {
                    itemAliases.remove(unusedAlias);
                }

                // Save asynchronously
                saveAliases();
            }
        }.runTaskAsynchronously(plugin);
    }

    public void syncAliasesWithProvider() {
        AliasesProvider addonProvider = Aliases.getAddonProvider(Skript.getAddonInstance());
        addonProvider.clearAliases();

        for (Map.Entry<String, String> entry : itemAliases.entrySet()) {
            AliasesProvider.AliasName aliasName = new AliasesProvider.AliasName(entry.getKey(), entry.getKey(), 0);
            try {
                addonProvider.addAlias(aliasName, entry.getValue(), null, new HashMap<>());
            } catch (InvalidMinecraftIdException ignored) {
            }
        }
    }


    public String getNamespacedId(String alias) {
        return itemAliases.get(alias);
    }

    public void saveAliases() {
        saveAliasesToFile();
    }
}

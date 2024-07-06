package me.asleepp.SkriptItemsAdder.other;

import dev.lone.itemsadder.api.CustomStack;
import dev.lone.itemsadder.api.Events.ItemsAdderLoadDataEvent;
import dev.lone.itemsadder.api.ItemsAdder;
import me.asleepp.SkriptItemsAdder.SkriptItemsAdder;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class ItemsAdderEventListener implements Listener {
    private final SkriptItemsAdder plugin;
    private final AliasesGenerator aliasesGenerator;
    private List<CustomStack> previousItems;

    public ItemsAdderEventListener(SkriptItemsAdder plugin, AliasesGenerator aliasesGenerator) {
        this.plugin = plugin;
        this.aliasesGenerator = aliasesGenerator;
        this.previousItems = ItemsAdder.getAllItems();
    }

    @EventHandler
    public void onItemsAdderLoadData(ItemsAdderLoadDataEvent event) {
        plugin.getLogger().info("ItemsAdder fully loaded, enabling skript-itemsadder.");
        plugin.setItemsAdderReady(true);
        List<CustomStack> allItems = ItemsAdder.getAllItems();
        plugin.getLogger().info("Total items loaded: " + allItems.size());

        if (previousItems == null) {
            previousItems = allItems;
            return;
        }

        List<CustomStack> newItems = findNewItems(previousItems, allItems);
        List<CustomStack> removedItems = findRemovedItems(previousItems, allItems);
        plugin.getLogger().info("New items found: " + newItems.size());
        plugin.getLogger().info("Removed items found: " + removedItems.size());

        aliasesGenerator.generateAliasesForNewItems(newItems);
        aliasesGenerator.handleRemovedItems(removedItems);
        aliasesGenerator.saveAliases();

        previousItems = allItems; // Update the previous items list
    }

    private List<CustomStack> findNewItems(List<CustomStack> previousItems, List<CustomStack> allItems) {
        Set<String> previousItemIds = previousItems.stream().map(CustomStack::getNamespacedID).collect(Collectors.toSet());
        return allItems.stream().filter(item -> !previousItemIds.contains(item.getNamespacedID())).collect(Collectors.toList());
    }

    private List<CustomStack> findRemovedItems(List<CustomStack> previousItems, List<CustomStack> allItems) {
        Set<String> currentItemIds = allItems.stream().map(CustomStack::getNamespacedID).collect(Collectors.toSet());
        return previousItems.stream().filter(item -> !currentItemIds.contains(item.getNamespacedID())).collect(Collectors.toList());
    }
}

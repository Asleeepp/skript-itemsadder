package me.asleepp.SkriptItemsAdder.other;

import dev.lone.itemsadder.api.CustomStack;
import dev.lone.itemsadder.api.Events.ItemsAdderLoadDataEvent;
import dev.lone.itemsadder.api.ItemsAdder;
import me.asleepp.SkriptItemsAdder.SkriptItemsAdder;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.ArrayList;
import java.util.List;

public class ItemsAdderEventListener implements Listener {
    private final SkriptItemsAdder plugin;
    private final AliasesGenerator aliasesGenerator;
    private List<CustomStack> initialItems;

    public ItemsAdderEventListener(SkriptItemsAdder plugin, AliasesGenerator aliasesGenerator, List<CustomStack> initialItems) {
        this.plugin = plugin;
        this.aliasesGenerator = aliasesGenerator;
        this.initialItems = initialItems;
    }

    @EventHandler
    public void onItemsAdderLoadData(ItemsAdderLoadDataEvent event) {
        plugin.getLogger().info("ItemsAdder fully loaded, enabling skript-itemsadder.");
        plugin.setItemsAdderReady(true);
        List<CustomStack> allItems = ItemsAdder.getAllItems();
        plugin.getLogger().info("Total items loaded: " + allItems.size());

        if (initialItems == null) {
            return;
        }

        List<CustomStack> newItems = findNewItems(initialItems, allItems);
        plugin.getLogger().info("New items found: " + newItems.size());
        aliasesGenerator.generateAliasesForNewItems(newItems);
        aliasesGenerator.saveAliases();
    }

    private List<CustomStack> findNewItems(List<CustomStack> initialItems, List<CustomStack> allItems) {
        List<CustomStack> newItems = new ArrayList<>();
        for (CustomStack newItem : allItems) {
            if (!initialItems.contains(newItem)) {
                newItems.add(newItem);
            }
        }
        return newItems;
    }
}

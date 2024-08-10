package me.asleepp.SkriptItemsAdder.other.listeners;

import dev.lone.itemsadder.api.Events.ItemsAdderLoadDataEvent;
import me.asleepp.SkriptItemsAdder.SkriptItemsAdder;
import me.asleepp.SkriptItemsAdder.other.aliases.AliasesGenerator;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class ItemsAdderEventListener implements Listener {
    private final SkriptItemsAdder plugin;
    private final AliasesGenerator aliasesGenerator;

    public ItemsAdderEventListener(SkriptItemsAdder plugin, AliasesGenerator aliasesGenerator) {
        this.plugin = plugin;
        this.aliasesGenerator = aliasesGenerator;
    }

    @EventHandler
    public void onItemsAdderLoadData(ItemsAdderLoadDataEvent event) {
        if (plugin.loading) {
            plugin.getLogger().info("ItemsAdder fully loaded, enabling skript-itemsadder.");
            plugin.setItemsAdderReady(true);
        }

        // run alias generation async
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            aliasesGenerator.generateAliasesForAllItems();
            aliasesGenerator.saveAliases();
        });
    }
}

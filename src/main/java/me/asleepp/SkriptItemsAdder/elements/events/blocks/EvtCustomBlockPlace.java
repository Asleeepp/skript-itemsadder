package me.asleepp.SkriptItemsAdder.elements.events.blocks;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.RequiredPlugins;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Literal;
import ch.njol.skript.lang.SkriptEvent;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.registrations.EventValues;
import ch.njol.skript.util.Getter;
import dev.lone.itemsadder.api.CustomBlock;
import dev.lone.itemsadder.api.Events.CustomBlockPlaceEvent;
import me.asleepp.SkriptItemsAdder.SkriptItemsAdder;
import me.asleepp.SkriptItemsAdder.other.aliases.AliasesGenerator;
import me.asleepp.SkriptItemsAdder.other.aliases.CustomItemType;
import me.asleepp.SkriptItemsAdder.other.util.Util;
import org.bukkit.Location;
import org.bukkit.event.Event;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class EvtCustomBlockPlace extends SkriptEvent {

    private Literal<?>[] blockNames;
    private List<String> aliases;
    private final AliasesGenerator aliasesGenerator = SkriptItemsAdder.getInstance().getAliasesGenerator();

    static {
        Skript.registerEvent("Custom Block Place", EvtCustomBlockPlace.class, CustomBlockPlaceEvent.class, "place [of] [custom] (ia|itemsadder) block[s] [%customitemtypes/strings%]")
                .description("Fires when a ItemsAdder block gets placed.")
                .examples("on place of itemsadder block:")
                .since("1.0")
                .requiredPlugins("ItemsAdder");
        EventValues.registerEventValue(CustomBlockPlaceEvent.class, CustomBlock.class, new Getter<CustomBlock, CustomBlockPlaceEvent>() {
            @Override
            public CustomBlock get(CustomBlockPlaceEvent event) {
                return CustomBlock.byAlreadyPlaced(event.getBlock());
            }
        }, 0);
        EventValues.registerEventValue(CustomBlockPlaceEvent.class, Location.class, new Getter<Location, CustomBlockPlaceEvent>() {
            @Override
            public Location get(CustomBlockPlaceEvent event) {
                return event.getBlock().getLocation();
            }
        }, 0);
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean init(Literal<?>[] args, int matchedPattern, SkriptParser.ParseResult parseResult) {
        blockNames = args;
        if (blockNames != null) {
            aliases = Arrays.stream(blockNames)
                    .map(literal -> {
                        if (literal instanceof Literal) {
                            Object value = literal.getSingle();
                            return Util.getCustomBlockId(value);
                        }
                        return null;
                    })
                    .filter(name -> name != null)
                    .collect(Collectors.toList());
        }
        return true;
    }

    @Override
    public boolean check(Event event) {
        if (!(event instanceof CustomBlockPlaceEvent)) {
            return false;
        }

        CustomBlockPlaceEvent customEvent = (CustomBlockPlaceEvent) event;
        if (customEvent.isCancelled()) {
            return false;
        }

        // Check block name
        if (aliases != null && !aliases.isEmpty()) {
            String actualBlockName = Util.getCustomBlockId(customEvent.getNamespacedID());
            return aliases.contains(actualBlockName);
        }

        return true;
    }

    @Override
    public String toString(@Nullable Event e, boolean debug) {
        return "Custom Block Place event";
    }
}

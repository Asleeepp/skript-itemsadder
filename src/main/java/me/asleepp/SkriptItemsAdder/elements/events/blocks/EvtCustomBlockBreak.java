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
import dev.lone.itemsadder.api.Events.CustomBlockBreakEvent;
import me.asleepp.SkriptItemsAdder.SkriptItemsAdder;
import me.asleepp.SkriptItemsAdder.other.aliases.AliasesGenerator;
import me.asleepp.SkriptItemsAdder.other.aliases.CustomItemType;
import org.bukkit.Location;
import org.bukkit.event.Event;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Name("On Custom Block Break")
@Description({"Fires when a ItemsAdder block gets broken."})
@Examples({"on break of custom block \"namespace:ruby_block\":", "on break of custom block ruby block:"})
@Since("1.0")
@RequiredPlugins("ItemsAdder")
public class EvtCustomBlockBreak extends SkriptEvent {

    private Literal<?>[] blockNames;
    private List<String> aliases;
    private AliasesGenerator aliasesGenerator = SkriptItemsAdder.getInstance().getAliasesGenerator();

    static {
        Skript.registerEvent("Custom Block Break", EvtCustomBlockBreak.class, CustomBlockBreakEvent.class, "break [of] [custom] (ia|itemsadder) block[s] [%customitemtypes/strings%]");
        EventValues.registerEventValue(CustomBlockBreakEvent.class, CustomBlock.class, new Getter<CustomBlock, CustomBlockBreakEvent>() {
            @Override
            public CustomBlock get(CustomBlockBreakEvent event) {
                return CustomBlock.byAlreadyPlaced(event.getBlock());
            }
        }, 0);
        EventValues.registerEventValue(CustomBlockBreakEvent.class, Location.class, new Getter<Location, CustomBlockBreakEvent>() {
            @Override
            public Location get(CustomBlockBreakEvent event) {
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
                            Object value = ((Literal<?>) literal).getSingle();
                            if (value instanceof CustomItemType) {
                                return ((CustomItemType) value).getNamespacedID();
                            } else if (value instanceof String) {
                                return (String) value;
                            }
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
        if (!(event instanceof CustomBlockBreakEvent)) {
            return false;
        }

        CustomBlockBreakEvent customEvent = (CustomBlockBreakEvent) event;
        if (customEvent.isCancelled()) {
            return false;
        }

        // Check block name
        if (aliases != null && !aliases.isEmpty()) {
            String actualBlockName = customEvent.getNamespacedID();
            return aliases.contains(aliasesGenerator.getNamespacedId(actualBlockName));
        }

        return true;
    }

    @Override
    public String toString(@Nullable Event e, boolean debug) {
        return "Custom Block Break event";
    }
}

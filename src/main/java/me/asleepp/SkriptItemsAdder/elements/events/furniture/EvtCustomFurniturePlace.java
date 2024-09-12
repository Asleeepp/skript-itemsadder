package me.asleepp.SkriptItemsAdder.elements.events.furniture;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Literal;
import ch.njol.skript.lang.SkriptEvent;
import ch.njol.skript.lang.SkriptParser;
import dev.lone.itemsadder.api.Events.FurnitureBreakEvent;
import dev.lone.itemsadder.api.Events.FurniturePlaceEvent;
import me.asleepp.SkriptItemsAdder.SkriptItemsAdder;
import me.asleepp.SkriptItemsAdder.aliases.AliasesGenerator;
import me.asleepp.SkriptItemsAdder.util.Util;
import org.bukkit.event.Event;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class EvtCustomFurniturePlace extends SkriptEvent {

    private Literal<?>[] furnitureIDs;
    private List<String> aliases;
    private AliasesGenerator aliasesGenerator = SkriptItemsAdder.getInstance().getAliasesGenerator();

    static {
        Skript.registerEvent("Custom Furniture Place", EvtCustomFurniturePlace.class, FurniturePlaceEvent.class, "place [of] [custom] (ia|itemsadder) furniture [%customitemtypes/strings%]")
                .description("Fires when a ItemsAdder furniture gets placed.")
                .examples("on place of itemsadder furniture:")
                .since("1.0")
                .requiredPlugins("ItemsAdder");
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean init(Literal<?>[] args, int matchedPattern, SkriptParser.ParseResult parseResult) {
        furnitureIDs = args;
        if (furnitureIDs != null) {
            aliases = Arrays.stream(furnitureIDs)
                    .flatMap(literal -> {
                        if (literal != null) {
                            return Arrays.stream(literal.getArray())
                                    .map(Util::getCustomBlockId);
                        }
                        return Stream.empty();
                    })
                    .filter(name -> name != null)
                    .collect(Collectors.toList());
        }
        return true;
    }

    @Override
    public boolean check(Event event) {
        if (event instanceof FurnitureBreakEvent furnEvent) {
            if (aliases != null && !aliases.isEmpty()) {
                String actualFurnitureName = Util.getCustomBlockId(furnEvent.getNamespacedID());
                return aliases.contains(actualFurnitureName);
            }

            return !furnEvent.isCancelled();
        }

        return true;
    }

    @Override
    public String toString(@Nullable Event e, boolean debug) {
        return "Custom Furniture Place event";
    }
}

package me.asleepp.SkriptItemsAdder.elements.events.furniture;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.RequiredPlugins;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Literal;
import ch.njol.skript.lang.SkriptEvent;
import ch.njol.skript.lang.SkriptParser;
import dev.lone.itemsadder.api.Events.FurniturePlaceEvent;
import me.asleepp.SkriptItemsAdder.SkriptItemsAdder;
import me.asleepp.SkriptItemsAdder.other.aliases.AliasesGenerator;
import me.asleepp.SkriptItemsAdder.other.aliases.CustomItemType;
import org.bukkit.event.Event;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Name("On Custom Furniture Place")
@Description({"Fires when a ItemsAdder furniture gets placed."})
@Examples({"on place of custom furniture:"})
@Since("1.0")
@RequiredPlugins("ItemsAdder")
public class EvtCustomFurniturePlace extends SkriptEvent {

    private Literal<?>[] furnitureIDs;
    private List<String> aliases;
    private AliasesGenerator aliasesGenerator = SkriptItemsAdder.getInstance().getAliasesGenerator();

    static {
        Skript.registerEvent("Custom Furniture Place", EvtCustomFurniturePlace.class, FurniturePlaceEvent.class, "place [of] [custom] (ia|itemsadder) furniture [%customitemtypes/strings%]");
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean init(Literal<?>[] args, int matchedPattern, SkriptParser.ParseResult parseResult) {
        furnitureIDs = args;
        if (furnitureIDs != null) {
            aliases = Arrays.stream(furnitureIDs)
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
        if (!(event instanceof FurniturePlaceEvent)) {
            return false;
        }

        FurniturePlaceEvent customEvent = (FurniturePlaceEvent) event;
        if (customEvent.isCancelled()) {
            return false;
        }

        // Check furniture name
        if (aliases != null && !aliases.isEmpty()) {
            String actualFurnitureName = customEvent.getNamespacedID();
            return aliases.contains(aliasesGenerator.getNamespacedId(actualFurnitureName));
        }

        return true;
    }

    @Override
    public String toString(@Nullable Event e, boolean debug) {
        return "Custom Furniture Place event";
    }
}

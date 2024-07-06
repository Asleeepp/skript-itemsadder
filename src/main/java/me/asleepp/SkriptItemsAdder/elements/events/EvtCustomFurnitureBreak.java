package me.asleepp.SkriptItemsAdder.elements.events;

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
import dev.lone.itemsadder.api.CustomFurniture;
import dev.lone.itemsadder.api.Events.FurnitureBreakEvent;
import me.asleepp.SkriptItemsAdder.SkriptItemsAdder;
import me.asleepp.SkriptItemsAdder.other.AliasesGenerator;
import me.asleepp.SkriptItemsAdder.other.CustomItemType;
import org.bukkit.Location;
import org.bukkit.event.Event;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Name("On Custom Furniture Break")
@Description({"Fires when a ItemsAdder furniture gets broken."})
@Examples({"on break of custom furniture:"})
@Since("1.0")
@RequiredPlugins("ItemsAdder")
public class EvtCustomFurnitureBreak extends SkriptEvent {

    private Literal<?>[] furnitureIDs;
    private List<String> aliases;
    private AliasesGenerator aliasesGenerator = SkriptItemsAdder.getInstance().getAliasesGenerator();

    static {
        Skript.registerEvent("Custom Furniture Break", EvtCustomFurnitureBreak.class, FurnitureBreakEvent.class, "break of [custom] (ia|itemsadder) furniture [%customitemtypes/strings%]");
        EventValues.registerEventValue(FurnitureBreakEvent.class, Location.class, new Getter<Location, FurnitureBreakEvent>() {
            @Override
            public @Nullable Location get(FurnitureBreakEvent event) {
                return event.getBukkitEntity().getLocation();
            }
        }, 0);
        EventValues.registerEventValue(FurnitureBreakEvent.class, CustomFurniture.class, new Getter<CustomFurniture, FurnitureBreakEvent>() {
            @Override
            public @Nullable CustomFurniture get(FurnitureBreakEvent event) {
                return event.getFurniture();
            }
        }, 0);
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
    public boolean check(Event e) {
        if (e instanceof FurnitureBreakEvent) {
            FurnitureBreakEvent event = (FurnitureBreakEvent) e;

            if (aliases != null && !aliases.isEmpty()) {
                String actualFurnitureName = event.getNamespacedID();
                return aliases.contains(aliasesGenerator.getNamespacedId(actualFurnitureName)) && !event.isCancelled();
            }

            return !event.isCancelled();
        }
        return false;
    }

    @Override
    public String toString(@Nullable Event e, boolean debug) {
        return "ItemsAdder custom furniture " + (furnitureIDs != null ? furnitureIDs.toString() : "") + " break";
    }
}

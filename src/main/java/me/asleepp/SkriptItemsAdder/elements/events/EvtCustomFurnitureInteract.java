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
import dev.lone.itemsadder.api.Events.FurnitureInteractEvent;
import me.asleepp.SkriptItemsAdder.SkriptItemsAdder;
import me.asleepp.SkriptItemsAdder.other.AliasesGenerator;
import me.asleepp.SkriptItemsAdder.other.CustomItemType;
import org.bukkit.Location;
import org.bukkit.event.Event;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Name("On Custom Furniture Interact")
@Description({"Fires when a ItemsAdder furniture gets interacted with."})
@Examples({"on interact with custom furniture:"})
@Since("1.0")
@RequiredPlugins("ItemsAdder")
public class EvtCustomFurnitureInteract extends SkriptEvent {

    private Literal<?>[] furnitureIDs;
    private List<String> aliases;
    private AliasesGenerator aliasesGenerator = SkriptItemsAdder.getInstance().getAliasesGenerator();

    static {
        Skript.registerEvent("Custom Furniture Interact", EvtCustomFurnitureInteract.class, FurnitureInteractEvent.class, "interact with [custom] (ia|itemsadder) furniture [%customitemtypes/strings%]");
        EventValues.registerEventValue(FurnitureInteractEvent.class, Location.class, new Getter<Location, FurnitureInteractEvent>() {
            @Override
            public @Nullable Location get(FurnitureInteractEvent event) {
                return event.getBukkitEntity().getLocation();
            }
        }, 0);
        EventValues.registerEventValue(FurnitureInteractEvent.class, CustomFurniture.class, new Getter<CustomFurniture, FurnitureInteractEvent>() {
            @Override
            public @Nullable CustomFurniture get(FurnitureInteractEvent event) {
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
        if (e instanceof FurnitureInteractEvent) {
            FurnitureInteractEvent event = (FurnitureInteractEvent) e;

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
        return "ItemsAdder custom furniture " + (furnitureIDs != null ? furnitureIDs.toString() : "") + " interact";
    }
}

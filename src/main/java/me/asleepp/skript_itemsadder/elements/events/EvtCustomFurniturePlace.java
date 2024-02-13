package me.asleepp.skript_itemsadder.elements.events;

import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.lang.Literal;
import ch.njol.skript.lang.SkriptEvent;
import ch.njol.skript.Skript;
import ch.njol.skript.lang.SkriptParser;
import dev.lone.itemsadder.api.Events.FurniturePlaceEvent;
import org.bukkit.event.Event;

import javax.annotation.Nullable;

@Name("On Custom Furniture Place")
@Description({"Fires when a ItemsAdder furniture gets placed."})
@Examples({"on place of custom furniture:"})
public class EvtCustomFurniturePlace extends SkriptEvent {
    private Literal<String> furnitureID;

    static {
        Skript.registerEvent("Custom Furniture Place", EvtCustomFurniturePlace.class, FurniturePlaceEvent.class, "place of (custom|ia|itemsadder) furniture [%string%]");
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean init(Literal<?>[] args, int matchedPattern, SkriptParser.ParseResult parseResult) {
        furnitureID = (Literal<String>) args[0];
        return true;
    }

    @Override
    public boolean check(Event e) {
        if (e instanceof FurniturePlaceEvent) {
            FurniturePlaceEvent event = (FurniturePlaceEvent) e;
            if (furnitureID == null) {
                return !event.isCancelled();
            } else {
                String id = event.getNamespacedID();
                if (id.equals(furnitureID.getSingle(e))) {
                    return !event.isCancelled();
                }
            }
        }
        return false;
    }

    @Override
    public String toString(@Nullable Event e, boolean debug) {
        return "ItemsAdder custom furniture " + (furnitureID != null ? furnitureID.toString(e, debug) : "") + " place";
    }
}

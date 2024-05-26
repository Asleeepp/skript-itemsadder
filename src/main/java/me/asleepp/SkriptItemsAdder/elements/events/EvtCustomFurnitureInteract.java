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
import dev.lone.itemsadder.api.Events.FurnitureInteractEvent;
import org.bukkit.Location;
import org.bukkit.event.Event;

import javax.annotation.Nullable;
@Name("On Custom Furniture Interact")
@Description({"Fires when a ItemsAdder furniture gets interacted with."})
@Examples({"on interact with custom furniture:"})
@Since("1.0")
@RequiredPlugins("ItemsAdder")
public class EvtCustomFurnitureInteract extends SkriptEvent {
    private Literal<String> furnitureID;

    static {
        Skript.registerEvent("Custom Furniture Interact", EvtCustomFurnitureInteract.class, FurnitureInteractEvent.class, "interact with [custom] (ia|itemsadder) furniture [%string%]");
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
        furnitureID = (Literal<String>) args[0];
        return true;
    }

    @Override
    public boolean check(Event e) {
        if (e instanceof FurnitureInteractEvent) {
            FurnitureInteractEvent event = (FurnitureInteractEvent) e;
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
        return "ItemsAdder custom furniture " + (furnitureID != null ? furnitureID.toString(e, debug) : "") + " interact";
    }
}

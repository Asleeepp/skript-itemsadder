package me.asleepp.SkriptItemsAdder.elements.events;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Literal;
import ch.njol.skript.lang.SkriptEvent;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.registrations.EventValues;
import ch.njol.skript.util.Getter;
import dev.lone.itemsadder.api.Events.FurniturePlaceSuccessEvent;
import org.bukkit.Location;
import org.bukkit.event.Event;

import javax.annotation.Nullable;

public class EvtCustomFurnitureSucessEvent extends SkriptEvent {

    private Literal<String> furnitureID;

    static {
        Skript.registerEvent("Custom Furniture Success", EvtCustomFurnitureSucessEvent.class, FurniturePlaceSuccessEvent.class,  "sucess[fully] plac(e|ing) [of] (custom|ia|itemsadder) furniture [%string%]");
        EventValues.registerEventValue(FurniturePlaceSuccessEvent.class, Location.class, new Getter<Location, FurniturePlaceSuccessEvent>() {
            @Override
            public Location get(FurniturePlaceSuccessEvent arg) {
                return arg.getFurniture().getEntity().getLocation();
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
        if (e instanceof FurniturePlaceSuccessEvent) {
            FurniturePlaceSuccessEvent event = (FurniturePlaceSuccessEvent) e;
            if (furnitureID != null) {
                String id = furnitureID.getSingle(e);
                if (id != null && !id.equals(event.getNamespacedID())) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    @Override
    public String toString(@Nullable Event e, boolean debug) {
        return "FurniturePlaceSuccess event" + (furnitureID != null ? " with id " + furnitureID.toString(e, debug) : "");
    }
}

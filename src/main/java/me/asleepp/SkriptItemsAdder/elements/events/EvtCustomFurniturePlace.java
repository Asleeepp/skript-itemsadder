package me.asleepp.SkriptItemsAdder.elements.events;

import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.RequiredPlugins;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Literal;
import ch.njol.skript.lang.SkriptEvent;
import ch.njol.skript.Skript;
import ch.njol.skript.lang.SkriptParser;
import dev.lone.itemsadder.api.Events.FurniturePlaceEvent;
import me.asleepp.SkriptItemsAdder.other.CustomItemType;
import org.bukkit.event.Event;

import javax.annotation.Nullable;

@Name("On Custom Furniture Place")
@Description({"Fires when a ItemsAdder furniture gets placed."})
@Examples({"on place of custom furniture:"})
@Since("1.0")
@RequiredPlugins("ItemsAdder")
public class EvtCustomFurniturePlace extends SkriptEvent {
    private Literal<CustomItemType> furnitureID;

    static {
        Skript.registerEvent("Custom Furniture Place", EvtCustomFurniturePlace.class, FurniturePlaceEvent.class, "place of [custom] (ia|itemsadder) furniture [%customitemtype%]");
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean init(Literal<?>[] args, int matchedPattern, SkriptParser.ParseResult parseResult) {
        furnitureID = (Literal<CustomItemType>) args[0];
        return true;
    }

    @Override
    public boolean check(Event e) {
        if (e instanceof FurniturePlaceEvent) {
            FurniturePlaceEvent event = (FurniturePlaceEvent) e;
            if (furnitureID == null) {
                return !event.isCancelled();
            } else {
                CustomItemType id = furnitureID.getSingle(e);
                if (id != null && id.equals(new CustomItemType(event.getNamespacedID()))) {
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

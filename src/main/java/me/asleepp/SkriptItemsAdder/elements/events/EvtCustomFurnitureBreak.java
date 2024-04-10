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
import dev.lone.itemsadder.api.Events.FurnitureBreakEvent;
import org.bukkit.event.Event;

import javax.annotation.Nullable;
@Name("On Custom Furniture Break")
@Description({"Fires when a ItemsAdder furniture gets broken."})
@Examples({"on break of custom furniture:"})
@Since("1.0")
@RequiredPlugins("ItemsAdder")
public class EvtCustomFurnitureBreak extends SkriptEvent {
    private Literal<String> furnitureID;

    static {
        Skript.registerEvent("Custom Furniture Break", EvtCustomFurnitureBreak.class, FurnitureBreakEvent.class, "break of (custom|ia|itemsadder) furniture [%string%]");
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean init(Literal<?>[] args, int matchedPattern, SkriptParser.ParseResult parseResult) {
        furnitureID = (Literal<String>) args[0];
        return true;
    }

    @Override
    public boolean check(Event e) {
        if (e instanceof FurnitureBreakEvent) {
            FurnitureBreakEvent event = (FurnitureBreakEvent) e;
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
        return "ItemsAdder custom furniture " + (furnitureID != null ? furnitureID.toString(e, debug) : "") + " break";
    }
}

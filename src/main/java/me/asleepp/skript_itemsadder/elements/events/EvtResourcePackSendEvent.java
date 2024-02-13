package me.asleepp.skript_itemsadder.elements.events;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Literal;
import ch.njol.skript.lang.SkriptEvent;
import ch.njol.skript.lang.SkriptParser;
import dev.lone.itemsadder.api.Events.ResourcePackSendEvent;
import org.bukkit.event.Event;

import javax.annotation.Nullable;

public class EvtResourcePackSendEvent extends SkriptEvent {

    private boolean isItemsAdderPack;

    static {
        Skript.registerEvent("Resource Pack Send Event", EvtResourcePackSendEvent.class, ResourcePackSendEvent.class, "[ia|itemsadder] (texture|resource) pack send");
    }

    @Override
    public boolean init(Literal<?>[] args, int matchedPattern, SkriptParser.ParseResult parseResult) {
        isItemsAdderPack = matchedPattern == 0;
        return true;
    }

    @Override
    public boolean check(Event event) {
        if (event instanceof ResourcePackSendEvent) {
            ResourcePackSendEvent rpEvent = (ResourcePackSendEvent) event;
            return isItemsAdderPack == rpEvent.isItemsAdderPack();
        }
        return false;
    }

    @Override
    public String toString(@Nullable Event e, boolean debug) {
        return "Resource Pack Send Event";
    }
}

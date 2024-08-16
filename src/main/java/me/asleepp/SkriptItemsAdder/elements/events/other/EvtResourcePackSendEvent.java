package me.asleepp.SkriptItemsAdder.elements.events.other;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.RequiredPlugins;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Literal;
import ch.njol.skript.lang.SkriptEvent;
import ch.njol.skript.lang.SkriptParser;
import dev.lone.itemsadder.api.Events.ResourcePackSendEvent;
import org.bukkit.event.Event;

import javax.annotation.Nullable;

public class EvtResourcePackSendEvent extends SkriptEvent {

    private boolean isItemsAdderPack;

    static {
        Skript.registerEvent("ItemsAdder Resource Pack Send Event", EvtResourcePackSendEvent.class, ResourcePackSendEvent.class, "[custom] [ia|itemsadder] (texture|resource) pack send")
                .description("Fires when a Resource pack from ItemsAdder gets sent to the player.")
                .examples(
                        "on resource pack send:",
                        "on ia resource pack send:"
                )
                .since("1.0")
                .requiredPlugins("ItemsAdder");
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

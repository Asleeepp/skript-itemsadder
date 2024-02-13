package me.asleepp.skript_itemsadder.elements.events;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Literal;
import ch.njol.skript.lang.SkriptEvent;
import ch.njol.skript.lang.SkriptParser;
import dev.lone.itemsadder.api.Events.CustomBlockInteractEvent;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;

import javax.annotation.Nullable;

public class EvtCustomBlockInteract extends SkriptEvent {
    private Literal<String> blockName;

    static {
        Skript.registerEvent("Custom Block Interact", EvtCustomBlockInteract.class, CustomBlockInteractEvent.class, "interact with (custom|ia|itemsadder) block [%string%]");
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean init(Literal<?>[] args, int matchedPattern, SkriptParser.ParseResult parseResult) {
        blockName = (Literal<String>) args[0];
        return true;
    }

    @Override
    public boolean check(Event event) {
        if (!(event instanceof CustomBlockInteractEvent)) {
            return false;
        }

        CustomBlockInteractEvent customEvent = (CustomBlockInteractEvent) event;
        if (customEvent.isCancelled()) {
            return false;
        }

        // check block
        if (blockName != null) {
            String specifiedBlockName = blockName.getSingle(event);
            if (specifiedBlockName == null || !specifiedBlockName.equals(customEvent.getNamespacedID())) {
                return false;
            }
        }

        return true;
    }

    @Override
    public String toString(@Nullable Event e, boolean debug) {
        return "Custom Block Interact event";
    }
}

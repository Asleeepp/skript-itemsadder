package me.asleepp.skript_itemsadder.elements.events;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Literal;
import ch.njol.skript.lang.SkriptEvent;
import ch.njol.skript.lang.SkriptParser;
import dev.lone.itemsadder.api.Events.CustomBlockBreakEvent;
import org.bukkit.block.Block;
import org.bukkit.event.Event;

import javax.annotation.Nullable;

public class EvtCustomBlockBreak extends SkriptEvent {

    private Literal<String> blockName;

    static {
        Skript.registerEvent("Custom Block Break", EvtCustomBlockBreak.class, CustomBlockBreakEvent.class, "break of (custom|ia|itemsadder) block [%string%]");
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean init(Literal<?>[] args, int matchedPattern, SkriptParser.ParseResult parseResult) {
        blockName = (Literal<String>) args[0];
        return true;
    }

    @Override
    public boolean check(Event event) {
        if (!(event instanceof CustomBlockBreakEvent)) {
            return false;
        }

        CustomBlockBreakEvent customBlockBreakEvent = (CustomBlockBreakEvent) event;
        if (customBlockBreakEvent.isCancelled()) {
            return false;
        }

        // check block
        if (blockName != null) {
            String specifiedBlockName = blockName.getSingle(event);
            Block block = customBlockBreakEvent.getBlock();
            if (block == null) {
                return false;
            }
            String actualBlockName = customBlockBreakEvent.getNamespacedID();
            if (actualBlockName == null || actualBlockName.isEmpty() || !actualBlockName.equals(specifiedBlockName)) {
                return false;
            }
        }

        return true;
    }



    @Override
    public String toString(@Nullable Event e, boolean debug) {
        return "Custom Block Break event";
    }
}

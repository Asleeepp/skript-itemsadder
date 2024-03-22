package me.asleepp.SkriptItemsAdder.elements.events;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Literal;
import ch.njol.skript.lang.SkriptEvent;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.registrations.EventValues;
import ch.njol.skript.util.Getter;
import dev.lone.itemsadder.api.CustomBlock;
import dev.lone.itemsadder.api.Events.CustomBlockBreakEvent;
import dev.lone.itemsadder.api.Events.CustomBlockInteractEvent;
import org.bukkit.block.Block;
import org.bukkit.event.Event;

import javax.annotation.Nullable;

@Name("On Custom Block Interact")
@Description({"Fires when a ItemsAdder block gets interacted with."})
@Examples({"on interact with custom block:"})
@Since("1.0")
public class EvtCustomBlockInteract extends SkriptEvent {
    private Literal<String> blockName;

    static {
        Skript.registerEvent("Custom Block Interact", EvtCustomBlockInteract.class, CustomBlockInteractEvent.class, "interact with (custom|ia|itemsadder) block [%string%]");
        EventValues.registerEventValue(CustomBlockInteractEvent.class, Block.class, new Getter<Block, CustomBlockInteractEvent>() {
            @Override
            public Block get(CustomBlockInteractEvent event) {
                return event.getBlockClicked();
            }
        }, 0);
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

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
import dev.lone.itemsadder.api.Events.CustomBlockPlaceEvent;
import org.bukkit.block.Block;
import org.bukkit.event.Event;

import javax.annotation.Nullable;

@Name("On Custom Block Place")
@Description({"Fires when a ItemsAdder block gets placed."})
@Examples({"on place of custom block:"})
@Since("1.0")
public class EvtCustomBlockPlace extends SkriptEvent {

    private Literal<String> blockName;
    private boolean isCancelled;

    static {
        Skript.registerEvent("Custom Block Place", EvtCustomBlockPlace.class, CustomBlockPlaceEvent.class, "place [of] (custom|ia|itemsadder) block [%string%]");
        EventValues.registerEventValue(CustomBlockBreakEvent.class, CustomBlock.class, new Getter<CustomBlock, CustomBlockBreakEvent>() {
            @Override
            public CustomBlock get(CustomBlockBreakEvent event) {
                return CustomBlock.byAlreadyPlaced(event.getBlock());
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
        if (!(event instanceof CustomBlockPlaceEvent)) {
            return false;
        }

        CustomBlockPlaceEvent customEvent = (CustomBlockPlaceEvent) event;
        if (customEvent.isCancelled()) {
            return false;
        }

        // check block
        if (blockName != null) {
            String specifiedBlockName = blockName.getSingle(event);
            Block block = customEvent.getBlock();
            if (block == null) {
                return false;
            }
            String actualBlockName = customEvent.getNamespacedID();
            if (actualBlockName == null || actualBlockName.isEmpty() || !actualBlockName.equals(specifiedBlockName)) {
                return false;
            }
        }
        return true;
    }




    @Override
    public String toString(@Nullable Event e, boolean debug) {
        return "Custom Block Place event";
    }
}

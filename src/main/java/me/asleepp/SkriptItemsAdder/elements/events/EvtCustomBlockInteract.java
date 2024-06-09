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
import dev.lone.itemsadder.api.Events.CustomBlockInteractEvent;
import me.asleepp.SkriptItemsAdder.SkriptItemsAdder;
import me.asleepp.SkriptItemsAdder.other.AliasesGenerator;
import me.asleepp.SkriptItemsAdder.other.CustomItemType;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.event.Event;

import javax.annotation.Nullable;

@Name("On Custom Block Interact")
@Description({"Fires when a ItemsAdder block gets interacted with."})
@Examples({"on interact with custom block:"})
@Since("1.0")
@RequiredPlugins("ItemsAdder")
public class EvtCustomBlockInteract extends SkriptEvent {
    private Literal<CustomItemType> blockName;

    static {
        Skript.registerEvent("Custom Block Interact", EvtCustomBlockInteract.class, CustomBlockInteractEvent.class, "interact with [custom] (ia|itemsadder) block [%customitemtype%]");
        EventValues.registerEventValue(CustomBlockInteractEvent.class, Block.class, new Getter<Block, CustomBlockInteractEvent>() {
            @Override
            public Block get(CustomBlockInteractEvent event) {
                return event.getBlockClicked();
            }
        }, 0);
        EventValues.registerEventValue(CustomBlockInteractEvent.class, Location.class, new Getter<Location, CustomBlockInteractEvent>() {
            @Override
            public @Nullable Location get(CustomBlockInteractEvent customBlockInteractEvent) {
                return customBlockInteractEvent.getBlockClicked().getLocation();
            }
        }, 0);
    }


    @SuppressWarnings("unchecked")
    @Override
    public boolean init(Literal<?>[] args, int matchedPattern, SkriptParser.ParseResult parseResult) {
        blockName = (Literal<CustomItemType>) args[0];
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
            CustomItemType specifiedBlockType = blockName.getSingle(event);
            AliasesGenerator aliasesGenerator = SkriptItemsAdder.getInstance().getAliasesGenerator();
            CustomItemType actualBlockType = EventValues.getEventValue(customEvent, CustomItemType.class, 0);

            if (specifiedBlockType == null || actualBlockType == null) {
                return false;
            }

            String specifiedBlockName = specifiedBlockType.getNamespacedID();
            String actualBlockName = actualBlockType.getNamespacedID();
            if (actualBlockName == null || actualBlockName.isEmpty() || !actualBlockName.equals(specifiedBlockName)) {
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

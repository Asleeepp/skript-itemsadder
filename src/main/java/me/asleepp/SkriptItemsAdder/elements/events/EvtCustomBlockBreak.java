package me.asleepp.SkriptItemsAdder.elements.events;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Literal;
import ch.njol.skript.lang.SkriptEvent;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.registrations.EventValues;
import ch.njol.skript.util.Getter;
import dev.lone.itemsadder.api.Events.CustomBlockBreakEvent;
import me.asleepp.SkriptItemsAdder.SkriptItemsAdder;
import me.asleepp.SkriptItemsAdder.other.AliasesGenerator;
import me.asleepp.SkriptItemsAdder.other.CustomItemType;
import org.bukkit.Location;
import org.bukkit.event.Event;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nullable;

public class EvtCustomBlockBreak extends SkriptEvent {

    private Literal<CustomItemType> blockName;

    static {
        Skript.registerEvent("Custom Block Break", EvtCustomBlockBreak.class, CustomBlockBreakEvent.class, "break of [custom] (ia|itemsadder) block [%customitemtype%]");
        EventValues.registerEventValue(CustomBlockBreakEvent.class, CustomItemType.class, new Getter<CustomItemType, CustomBlockBreakEvent>() {
            @Override
            public CustomItemType get(CustomBlockBreakEvent event) {
                String namespacedID = event.getNamespacedID();
                return new CustomItemType(namespacedID);
            }
        }, 0);
        EventValues.registerEventValue(CustomBlockBreakEvent.class, Location.class, new Getter<Location, CustomBlockBreakEvent>() {
            @Override
            public Location get(CustomBlockBreakEvent event) {
                return event.getBlock().getLocation();
            }
        }, 0);
        EventValues.registerEventValue(CustomBlockBreakEvent.class, ItemStack.class, new Getter<ItemStack, CustomBlockBreakEvent>() {
            @Override
            public @Nullable ItemStack get(CustomBlockBreakEvent event) {
                return event.getCustomBlockItem();
            }
        }, 0);
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean init(Literal<?>[] args, int matchedPattern, SkriptParser.ParseResult parseResult) {
        if (args.length > 0 && args[0] instanceof Literal) {
            blockName = (Literal<CustomItemType>) args[0];
        }
        return true;
    }

    @Override
    public boolean check(Event event) {
        if (!(event instanceof CustomBlockBreakEvent)) {
            return false;
        }

        CustomBlockBreakEvent customEvent = (CustomBlockBreakEvent) event;
        if (customEvent.isCancelled()) {
            return false;
        }

        // Check block name
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
        return "Custom Block Break event";
    }
}

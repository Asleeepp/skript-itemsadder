package me.asleepp.SkriptItemsAdder.elements.events.blocks;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Literal;
import ch.njol.skript.lang.SkriptEvent;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.registrations.EventValues;
import ch.njol.skript.util.Getter;
import dev.lone.itemsadder.api.CustomBlock;
import dev.lone.itemsadder.api.Events.CustomBlockInteractEvent;
import me.asleepp.SkriptItemsAdder.SkriptItemsAdder;
import me.asleepp.SkriptItemsAdder.aliases.AliasesGenerator;
import me.asleepp.SkriptItemsAdder.util.Util;
import org.bukkit.Location;
import org.bukkit.block.BlockFace;
import org.bukkit.event.Event;
import org.bukkit.event.block.Action;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public class EvtCustomBlockInteract extends SkriptEvent {

    private Literal<?>[] blockNames;
    private List<String> aliases;
    private AliasesGenerator aliasesGenerator = SkriptItemsAdder.getInstance().getAliasesGenerator();

    static {
        Skript.registerEvent("Custom Block Interact", EvtCustomBlockInteract.class, CustomBlockInteractEvent.class, "interact with [custom] (ia|itemsadder) block[s] [%customitemtypes/strings%]")
                .description("Fires when a ItemsAdder block gets interacted with.")
                .examples("on interact with itemsadder block:")
                .since("1.0")
                .requiredPlugins("ItemsAdder");
        EventValues.registerEventValue(CustomBlockInteractEvent.class, CustomBlock.class, new Getter<CustomBlock, CustomBlockInteractEvent>() {
            @Override
            public CustomBlock get(CustomBlockInteractEvent event) {
                return CustomBlock.byAlreadyPlaced(event.getBlockClicked());
            }
        }, EventValues.TIME_NOW);
        EventValues.registerEventValue(CustomBlockInteractEvent.class, Location.class, new Getter<Location, CustomBlockInteractEvent>() {
            @Override
            public Location get(CustomBlockInteractEvent event) {
                return event.getBlockClicked().getLocation();
            }
        }, EventValues.TIME_NOW);
        EventValues.registerEventValue(CustomBlockInteractEvent.class, Action.class, new Getter<Action, CustomBlockInteractEvent>() {
            @Override
            public Action get(CustomBlockInteractEvent customBlockInteractEvent) {
                return customBlockInteractEvent.getAction();
            }
        }, EventValues.TIME_NOW);
        EventValues.registerEventValue(CustomBlockInteractEvent.class, BlockFace.class, new Getter<BlockFace, CustomBlockInteractEvent>() {
            @Override
            public BlockFace get(CustomBlockInteractEvent customBlockInteractEvent) {
                return customBlockInteractEvent.getBlockFace();
            }
        }, EventValues.TIME_NOW);
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean init(Literal<?>[] args, int matchedPattern, SkriptParser.ParseResult parseResult) {
        blockNames = args;
        if (blockNames != null) {
            aliases = Arrays.stream(blockNames)
                    .flatMap(literal -> {
                        if (literal != null) {
                            return Arrays.stream(literal.getArray())
                                    .map(Util::getCustomBlockId);
                        }
                        return Stream.empty();
                    })
                    .filter(name -> name != null)
                    .collect(Collectors.toList());
        }
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

        // Check block name
        if (aliases != null && !aliases.isEmpty()) {
            String actualBlockName = Util.getCustomBlockId(customEvent.getNamespacedID());
            return aliases.contains(actualBlockName);
        }

        return true;
    }

    @Override
    public String toString(@Nullable Event e, boolean debug) {
        return "Custom Block Interact event";
    }
}

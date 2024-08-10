package me.asleepp.SkriptItemsAdder.elements.conditions;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.RequiredPlugins;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Condition;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import dev.lone.itemsadder.api.CustomBlock;
import me.asleepp.SkriptItemsAdder.other.aliases.CustomItemType;
import org.bukkit.block.Block;
import org.bukkit.event.Event;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Name("Is ItemsAdder Block")
@Description({"Checks if the block is an ItemsAdder block."})
@Examples({
        "on break:",
            "\tif event-block is a custom block with id \"iasurvival:ruby_block\":",
                "\t\tkill player",
            "\telse if event-block isn't a custom block:",
                "\t\tsend \"Good Job!\" to player"
})
@Since("1.0, 1.5 (Negative Comparison)")
@RequiredPlugins("ItemsAdder")
public class CondIsCustomBlock extends Condition {
    private Expression<Block> blocks;
    private Expression<Object> ids;
    private boolean negated;

    static {
        Skript.registerCondition(CondIsCustomBlock.class,
                "%blocks% (is [a[n]]|are) [custom] (ia|itemsadder) block[s] [[with id] %-customitemtypes/strings%]",
                "%blocks% (is[n't| not]) [a] [custom] (ia|itemsadder) block[s] [[with id] %-customitemtypes/strings%]");
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean init(Expression<?>[] expressions, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        blocks = (Expression<Block>) expressions[0];
        ids = (Expression<Object>) expressions[1];
        negated = matchedPattern == 1;
        return true;
    }

    @Override
    public boolean check(Event event) {
        List<String> expectedIds = Arrays.stream(ids.getArray(event))
                .map(id -> {
                    if (id instanceof CustomItemType) {
                        return ((CustomItemType) id).getNamespacedID();
                    } else if (id instanceof String) {
                        return (String) id;
                    }
                    return null;
                })
                .filter(id -> id != null)
                .collect(Collectors.toList());

        boolean allMatch = blocks.getArray(event).length > 0;
        for (Block block : blocks.getArray(event)) {
            CustomBlock customBlock = CustomBlock.byAlreadyPlaced(block);
            boolean matches = customBlock != null && expectedIds.contains(customBlock.getId());
            if (negated && matches || !negated && !matches) {
                allMatch = false;
                break;
            }
        }
        return allMatch;
    }

    @Override
    public String toString(@Nullable Event event, boolean debug) {
        String blockString = blocks.toString(event, debug);
        String idString = ids.toString(event, debug);
        return blockString + (negated ? " isn't" : " is") + " a custom block" + (idString != null ? " with id " + idString : "");
    }
}

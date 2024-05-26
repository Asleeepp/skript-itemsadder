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
import org.bukkit.block.Block;
import org.bukkit.event.Event;

import javax.annotation.Nullable;
@Name("Is ItemsAdder Block")
@Description({"Checks if the block is an ItemsAdder block."})
@Examples({
    "on break:",
        "\tif event-block is a custom block with id \"iasurvival:ruby_block\":",
            "\t\tkill player",
        "\telse if event-block isn't a custom block:",
            "\t\tsend \"Good Job!\" to player"})
@Since("1.0, 1.5 (Negative Comparison)")
@RequiredPlugins("ItemsAdder")
public class CondIsCustomBlock extends Condition {
    private Expression<Block> block;
    private Expression<String> blockId;

    static {
        Skript.registerCondition(CondIsCustomBlock.class, "%blocks% (is [a[n]]|are) [custom] (ia|itemsadder) block[s] [[with id] %-string%]", "%blocks% (is[n't| not]) [a] [custom] (ia|itemsadder) block[s] [[with id] %-string%]");
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        block = (Expression<Block>) exprs[0];
        blockId = (Expression<String>) exprs[1];
        setNegated(matchedPattern == 1);
        return true;
    }

    @Override
    public boolean check(Event e) {
        Block[] blocks = block.getArray(e);
        if (blocks == null) {
            return isNegated();
        }

        for (Block b : blocks) {
            CustomBlock customBlock = CustomBlock.byAlreadyPlaced(b);
            if (customBlock == null) {
                return isNegated();
            }
            if (blockId != null) {
                String id = blockId.getSingle(e);
                if (id == null || !customBlock.getId().equals(id)) {
                    return isNegated();
                }
            }
        }
        return !isNegated();
    }

    @Override
    public String toString(@Nullable Event e, boolean debug) {
        return block.toString(e, debug) + (isNegated() ? " isn't" : " is") + " a custom block" + (blockId != null ? " with id " + blockId.toString(e, debug) : "");
    }
}


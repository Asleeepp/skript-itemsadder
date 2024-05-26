package me.asleepp.SkriptItemsAdder.elements.conditions;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Condition;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import dev.lone.itemsadder.api.CustomCrop;
import org.bukkit.block.Block;
import org.bukkit.event.Event;

import javax.annotation.Nullable;

public class CondIsFullyGrown extends Condition {

    private Expression<Block> blocks;

    static {
        Skript.registerCondition(CondIsFullyGrown.class,
                "[the] [custom] (ia|itemsadder) %blocks% (is|are) full[y] grown",
                "[the] [custom] (ia|itemsadder) %blocks% (is|are) not full[y] grown");
    }

    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        blocks = (Expression<Block>) exprs[0];
        setNegated(matchedPattern == 1);
        return true;
    }

    @Override
    public boolean check(Event e) {
        Block[] bs = blocks.getArray(e);
        for (Block b : bs) {
            CustomCrop crop = CustomCrop.byAlreadyPlaced(b);
            if (crop != null && crop.isFullyGrown() != isNegated()) {
                return false;
            }
        }
        return true;
    }

    @Override
    public String toString(@Nullable Event e, boolean debug) {
        return blocks.toString(e, debug) + (isNegated() ? " is not" : " is") + " fully grown";
    }
}

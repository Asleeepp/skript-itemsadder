package me.asleepp.skript_itemsadder.elements.conditions;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Condition;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import dev.lone.itemsadder.api.CustomBlock;
import dev.lone.itemsadder.api.CustomStack;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.Event;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nullable;

public class CondIsCustomBlock extends Condition {
    private Expression<Block> block;

    static {
        Skript.registerCondition(CondIsCustomBlock.class, "%block% (is|are) ([a|an]) (custom|ia|itemsadder) block");
    }

    @Override
    public boolean check(Event e) {
        Block b = block.getSingle(e);
        if(b == null)
            return false;
        CustomBlock customBlock = CustomBlock.byAlreadyPlaced(b);
        return customBlock != null;
    }


    @Override
    public String toString(@Nullable Event e, boolean debug) {
        return block.toString(e, debug) + " is an ItemsAdder block";
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        block = (Expression<Block>) exprs[0];
        return true;
    }
}

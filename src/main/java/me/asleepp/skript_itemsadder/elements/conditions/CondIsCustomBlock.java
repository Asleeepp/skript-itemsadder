package me.asleepp.skript_itemsadder.elements.conditions;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
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
@Name("Is ItemsAdder Block")
@Description({"Checks if the block is an ItemsAdder block."})
@Examples({"if \"ruby_block\" is a custom block"})
public class CondIsCustomBlock extends Condition {
    private Expression<Block> block;

    static {
        Skript.registerCondition(CondIsCustomBlock.class, "%blocks% (is|are) ([a|an]) (custom|ia|itemsadder) block");
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        block = (Expression<Block>) exprs[0];
        return true;
    }

    @Override
    public boolean check(Event e) {
        Block[] blocks = block.getArray(e);
        if (blocks == null) {
            return false;
        }

        for (Block b : blocks) {
            CustomBlock customBlock = CustomBlock.byAlreadyPlaced(b);
            if (customBlock != null) {
                return true;
            }
        }
        return false;
    }


    @Override
    public String toString(@Nullable Event e, boolean debug) {
        return block.toString(e, debug) + " is an ItemsAdder block";
    }
}

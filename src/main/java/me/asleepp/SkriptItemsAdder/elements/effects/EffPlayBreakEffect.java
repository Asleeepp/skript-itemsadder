package me.asleepp.SkriptItemsAdder.elements.effects;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.RequiredPlugins;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import dev.lone.itemsadder.api.CustomBlock;

import org.bukkit.block.Block;
import org.bukkit.event.Event;

import javax.annotation.Nullable;

@Name("Play Break Effect")
@Description("Plays the breaking effect on an ItemsAdder block, (not the cracking in case you were wondering.)")
@Examples("play break effect on target block")
@Since("1.5")
@RequiredPlugins("ItemsAdder")
public class EffPlayBreakEffect extends Effect {

    private Expression<Block> blocks;

    static {
        Skript.registerEffect(EffPlayBreakEffect.class, "(show|play) break[ing] effect on [custom] (ia|itemsadder) %blocks%");
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        blocks = (Expression<Block>) exprs[0];
        return true;
    }

    @Override
    protected void execute(Event e) {
        Block[] blocksArray = blocks.getArray(e);
        for (Block block : blocksArray) {
            CustomBlock customBlock = CustomBlock.byAlreadyPlaced(block);
            if (customBlock != null) {
                customBlock.playBreakEffect(block);
            }
        }
    }

    @Override
    public String toString(@Nullable Event e, boolean debug) {
        return "(show|play) break[ing] effect on " + blocks.toString(e, debug);
    }
}

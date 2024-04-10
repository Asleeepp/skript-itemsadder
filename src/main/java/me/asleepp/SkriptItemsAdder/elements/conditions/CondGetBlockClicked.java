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
import org.bukkit.event.Event;
import dev.lone.itemsadder.api.Events.CustomBlockInteractEvent;

import javax.annotation.Nullable;
@Name("Is Block Clicked")
@Description({"This condition checks what block the player clicked."})
@Examples({
    "on interact with custom block:",
        "\tif clicked block is \"diamond_tiles\"",
            "\t\tsend \"That's quite valuable.\" "})
@Since("1.4")
@RequiredPlugins("ItemsAdder")
public class CondGetBlockClicked extends Condition {

    private Expression<String> block;

    static {
        Skript.registerCondition(CondGetBlockClicked.class, "[the] block clicked is %strings%");
    }

    @Override
    public boolean check(Event e) {
        if (e instanceof CustomBlockInteractEvent) {
            CustomBlockInteractEvent event = (CustomBlockInteractEvent) e;
            String clickedBlock = event.getBlockClicked().getType().toString();
            return clickedBlock.equals(block.getSingle(e));
        }
        return false;
    }

    @Override
    public String toString(@Nullable Event e, boolean debug) {
        return "[the] block clicked is " + block.toString(e, debug);
    }

    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        if (!getParser().isCurrentEvent(CustomBlockInteractEvent.class)) {
            Skript.error("You can't use 'the block clicked is ...' outside of a custom block interact event!");
            return false;
        }
        block = (Expression<String>) exprs[0];
        return true;
    }

}

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
import dev.lone.itemsadder.api.Events.CustomBlockInteractEvent;
import org.bukkit.block.BlockFace;
import org.bukkit.event.Event;

import javax.annotation.Nullable;
@Name("Is Block Face")
@Description({"This condition checks what face of a block the player has interacted with."})
@Examples({
    "on interact with custom block:",
        "/tif clicked block face is south:",
            "/t/tsend \"Why are you doing that?\""})
@Since("1.4")
@RequiredPlugins("ItemsAdder")
public class CondGetBlockFace extends Condition {

    static {
        Skript.registerCondition(CondGetBlockFace.class, "[clicked] block face is (:down|:north|:south|:east|:west|:up)");
    }

    private BlockFace face;

    @Override
    public boolean check(Event e) {
        if (!(e instanceof CustomBlockInteractEvent)) {
            return false;
        }
        CustomBlockInteractEvent event = (CustomBlockInteractEvent) e;
        return event.getBlockFace() == face;
    }


    @Override
    public String toString(@Nullable Event e, boolean debug) {
        return "clicked block face is " + face.toString();
    }

    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        if (!getParser().isCurrentEvent(CustomBlockInteractEvent.class)) {
            Skript.error("You can't use 'clicked block face is (:down|:north|:south|:east|:west|:up)' outside of a custom block interact event!");
            return false;
        }
        if (parseResult.hasTag("north")) {
            face = BlockFace.NORTH;
        } else if (parseResult.hasTag("south")) {
            face = BlockFace.SOUTH;
        } else if (parseResult.hasTag("east")) {
            face = BlockFace.EAST;
        } else if (parseResult.hasTag("west")) {
            face = BlockFace.WEST;
        } else if (parseResult.hasTag("up")) {
            face = BlockFace.UP;
        } else if (parseResult.hasTag("down")) {
            face = BlockFace.DOWN;
        }
        return true;
    }
}

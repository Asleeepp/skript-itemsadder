package me.asleepp.SkriptItemsAdder.elements.conditions;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Condition;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import org.bukkit.event.Event;
import dev.lone.itemsadder.api.Events.CustomBlockInteractEvent;
import org.bukkit.event.block.Action;

import javax.annotation.Nullable;
@Name("Is Action")
@Description({"This condition checks if the player has interacted with a custom block with either a left or a right click."})
@Examples({"on interact with custom block: /tif interact action is right click: /t/tkill event-player "})
@Since("1.4")
public class CondGetAction extends Condition {

    static {
        Skript.registerCondition(CondGetAction.class, "[custom|ia|itemsadder] [interact] action is (:right|:left) click");
    }

    private boolean isLeft;

    @Override
    public boolean check(Event e) {
        if (!(e instanceof CustomBlockInteractEvent)) {
            return false;
        }
        CustomBlockInteractEvent event = (CustomBlockInteractEvent) e;
        if (isLeft) {
            return event.getAction() == Action.LEFT_CLICK_BLOCK;
        } else {
            return event.getAction() == Action.RIGHT_CLICK_BLOCK;
        }
    }


    @Override
    public String toString(@Nullable Event e, boolean debug) {
        return "interact action is " + (isLeft ? "left" : "right") + " click";
    }

    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        if (!getParser().isCurrentEvent(CustomBlockInteractEvent.class)) {
            Skript.error("You can't use 'interact action is (:right|:left) click' outside of a custom block interact event!");
            return false;
        }
        isLeft = parseResult.hasTag("left");
        return true;
    }

}

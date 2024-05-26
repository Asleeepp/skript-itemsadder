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
import org.bukkit.event.Event;
import dev.lone.itemsadder.api.CustomBlock;
import org.bukkit.Location;

import javax.annotation.Nullable;
@Name("Replace Custom Block")
@Description({"If there is a custom block at a location, this effect will remove it and place the one you specify."})
@Examples({"replace custom block \"diamond_tiles\" at player's location"})
@Since("1.4")
@RequiredPlugins("ItemsAdder")
public class EffReplaceBlock extends Effect {

    private Expression<String> blockId;
    private Expression<Location> location;

    static {
        Skript.registerEffect(EffReplaceBlock.class, "replace [custom] (ia|itemsadder) block %string% at %location%");
    }

    @Override
    protected void execute(Event e) {
        String id = blockId.getSingle(e);
        Location loc = location.getSingle(e);

        if (id != null && loc != null) {
            CustomBlock existingBlock = CustomBlock.byAlreadyPlaced(loc.getBlock());

            if (existingBlock != null) {
                existingBlock.remove();
            }
            CustomBlock.place(id, loc);
        }
    }

    @Override
    public String toString(@Nullable Event e, boolean debug) {
        return "replace itemsadder block " + blockId.toString(e, debug) + " at " + location.toString(e, debug);
    }

    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        blockId = (Expression<String>) exprs[0];
        location = (Expression<Location>) exprs[1];
        return true;
    }
}

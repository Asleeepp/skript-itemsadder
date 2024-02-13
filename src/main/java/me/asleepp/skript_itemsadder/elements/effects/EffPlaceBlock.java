package me.asleepp.skript_itemsadder.elements.effects;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import dev.lone.itemsadder.api.CustomBlock;
import org.bukkit.Location;
import org.bukkit.event.Event;

import javax.annotation.Nullable;
@Name("PlaceCustomBlock")
@Description({"Places a custom block."})
@Examples({"set block at player's location to custom itemsadder block \"ruby_block\""})
public class EffPlaceBlock extends Effect {
    private Expression<Location> locationExpr;
    private Expression<String> customBlockIdExpr;

    static {
        Skript.registerEffect(EffPlaceBlock.class, new String[]{"(set|place) block at %locations% to (custom|ia|itemsadder) block %string%"});
    }

    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        locationExpr = (Expression<Location>) exprs[0];
        if (exprs.length > 1 && exprs[1] != null) {
            customBlockIdExpr = (Expression<String>) exprs[1];
        }
        return true;
    }

    @Override
    protected void execute(Event e) {
        Location[] locations = locationExpr.getArray(e);
        String customBlockId = customBlockIdExpr.getSingle(e);

        if (locations == null || customBlockId == null) {
            return;
        }

        for (Location location : locations) {
            CustomBlock customBlock = CustomBlock.getInstance(customBlockId);
            if (customBlock != null) {
                customBlock.place(location);
            }
        }
    }


    @Override
    public String toString(@Nullable Event e, boolean debug) {
        return "place custom block at location";
    }
}

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
import dev.lone.itemsadder.api.ItemsAdder;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;

import javax.annotation.Nullable;
@Name("Send ItemsAdder Resource Pack")
@Description({"Sends the most recently generated ItemsAdder resource pack to players."})
@Examples({"send current itemsadder resource pack to all players"})
@Since("1.0")
@RequiredPlugins("ItemsAdder")
public class EffSendIAResourcePack extends Effect {

    private Expression<Player> players;

    static {
        Skript.registerEffect(EffSendIAResourcePack.class, "(send|apply) [current] [custom] (ia|itemsadder) (texture|resource) pack to %players%");
    }
    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        players = (Expression<Player>) exprs[0];
        return true;
    }
    @Override
    protected void execute(Event e) {
        Player[] ps = players.getArray(e);

        if (ps != null) {
            for (Player p : ps) {
                ItemsAdder.applyResourcepack(p);
            }
        }
    }

    @Override
    public String toString(@Nullable Event e, boolean debug) {
        return "Apply ItemsAdder resource pack to " + players.toString(e, debug);
    }
}

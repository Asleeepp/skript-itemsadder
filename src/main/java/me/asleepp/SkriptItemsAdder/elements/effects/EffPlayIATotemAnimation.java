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
@Name("Play ItemsAdder Totem Animation")
@Description({"Play a custom Totem Animation to players."})
@Examples({"play the custom totem animation \"celebrations:you_win\" to player"})
@Since("1.0")
@RequiredPlugins("ItemsAdder")
public class EffPlayIATotemAnimation extends Effect {

    private Expression<Player> players;
    private Expression<String> totem;

    static {
        Skript.registerEffect(EffPlayIATotemAnimation.class, "(play|make) [the] [custom] (ia|itemsadder) totem [anim|animation] %string% to %players%");
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        totem = (Expression<String>) exprs[0];
        players = (Expression<Player>) exprs[1];
        return true;
    }

    @Override
    protected void execute(Event e) {
        Player[] ps = players.getArray(e);
        String totemName = totem.getSingle(e);

        if (ps != null && totemName != null) {
            for (Player p : ps) {
                ItemsAdder.playTotemAnimation(p, totemName);
            }
        }
    }

    @Override
    public String toString(@Nullable Event e, boolean debug) {
        return "play the custom totem animation " + totem.toString(e, debug) + " to " + players.toString(e, debug);
    }
}

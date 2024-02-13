package me.asleepp.skript_itemsadder.elements.effects;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import dev.lone.itemsadder.api.CustomPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;

import javax.annotation.Nullable;

public class EffPlayerEmote extends Effect {
    private Expression<Player> players;
    private Expression<String> emote;
    private int pattern;

    static {
        Skript.registerEffect(EffPlayerEmote.class,
                "(make|force) %players% [perform] [(custom|ia|itemsadder)] (emote|dance|animation) %string%",
                "(make|force) %players% [to] stop emoting");
    }

    protected void execute(Event e) {
        Player[] ps = players.getAll(e);

        if (ps != null) {
            if (pattern == 0) {
                String emoteName = emote.getSingle(e);
                if (emoteName != null) {
                    for (Player p : ps) {
                        CustomPlayer.playEmote(p, emoteName);
                    }
                }
            } else if (pattern == 1) {
                for (Player p : ps) {
                    CustomPlayer.stopEmote(p);
                }
            }
        }
    }

    @Override
    public String toString(@Nullable Event e, boolean debug) {
        return pattern == 0 ? "make " + players.toString(e, debug) + " play animation " + emote.toString(e, debug)
                : "make " + players.toString(e, debug) + " stop emoting";
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        players = (Expression<Player>) exprs[0];
        pattern = matchedPattern;
        if (matchedPattern == 0) {
            emote = (Expression<String>) exprs[1];
        }
        return true;
    }
}
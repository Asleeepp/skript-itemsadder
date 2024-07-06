package me.asleepp.SkriptItemsAdder.elements.conditions;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Condition;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import dev.lone.itemsadder.api.FontImages.PlayerHudWrapper;
import dev.lone.itemsadder.api.FontImages.PlayerHudsHolderWrapper;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;

import javax.annotation.Nullable;

public class CondHUDIsVisible extends Condition {

    private Expression<Player> players;
    private Expression<String> hudID;

    static {
        Skript.registerCondition(CondHUDIsVisible.class, "%players% can (see|view) [custom] (ia|itemsadder) hud %string%");
    }

    @Override
    public boolean init(Expression<?>[] exprs, int i, Kleenean kleenean, SkriptParser.ParseResult parseResult) {
        players = (Expression<Player>) exprs[0];
        hudID = (Expression<String>) exprs[1];
        return true;
    }

    @Override
    public boolean check(Event event) {
        Player[] p = players.getArray(event);
        String id = hudID.getSingle(event);

        if (p != null && id != null) {
            for (Player players : p) {
                PlayerHudsHolderWrapper hudsHolder = new PlayerHudsHolderWrapper(players);
                PlayerHudWrapper hudWrapper = new PlayerHudWrapper(hudsHolder, id);
                return hudWrapper.isVisible();
            }
            return false;
        }
        return false;
    }

    @Override
    public String toString(@Nullable Event event, boolean debug) {
        return "player(s) " + players.toString(event, debug) + "can view itemsadder hud " + hudID.toString(event, debug);
    }
}

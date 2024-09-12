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

public class CondHUDExists extends Condition {

    private Expression<Player> players;
    private Expression<String> hudID;

    static {
        Skript.registerCondition(CondHUDExists.class, "[custom] (ia|itemsadder) hud %string% [of %players%] exists");
    }

    @Override
    public boolean init(Expression<?>[] exprs, int i, Kleenean kleenean, SkriptParser.ParseResult parseResult) {
        hudID = (Expression<String>) exprs[0];
        players = (Expression<Player>) exprs[1];
        return true;
    }

    @Override
    public boolean check(Event event) {
        String id = hudID.getSingle(event);
        Player[] playersArray = players != null ? players.getArray(event) : new Player[]{};

        if (id != null) {
            for (Player player : playersArray) {
                PlayerHudsHolderWrapper hudHolder = new PlayerHudsHolderWrapper(player);
                PlayerHudWrapper hudWrapper = new PlayerHudWrapper(hudHolder, id);
                if (!hudWrapper.exists()) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    @Override
    public String toString(@Nullable Event event, boolean debug) {
        return "itemsadder hud " + hudID.toString(event, debug) + " of players " + (players != null ? players.toString(event, debug) : "all players") + " exists";
    }
}

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
import dev.lone.itemsadder.api.FontImages.PlayerHudsHolderWrapper;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;

import javax.annotation.Nullable;

@Name("Update HUD")
@Description("Update the player's currently enabled HUD.")
@Examples("update player's current itemsadder hud")
@Since("1.6")
@RequiredPlugins("ItemsAdder")
public class EffUpdateHUD extends Effect {

    private Expression<Player> players;

    static {
        Skript.registerEffect(EffUpdateHUD.class, "(update|refresh) %players%'[s] [current] [custom] (itemsadder|ia) hud");
    }

    @Override
    public boolean init(Expression<?>[] exprs, int i, Kleenean kleenean, SkriptParser.ParseResult parseResult) {
        players = (Expression<Player>) exprs[0];
        return true;
    }

    @Override
    protected void execute(Event event) {
        Player[] p = players.getArray(event);

        if (p != null) {
            for (Player players : p) {
                PlayerHudsHolderWrapper playerHudsHolderWrapper = new PlayerHudsHolderWrapper(players);
                if (playerHudsHolderWrapper.exists()) {
                    playerHudsHolderWrapper.sendUpdate();
                }
            }
        }
    }

    @Override
    public String toString(@Nullable Event event, boolean debug) {
        return "update " + players.toString(event, debug) + " hud";
    }

}

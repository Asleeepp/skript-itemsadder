package me.asleepp.SkriptItemsAdder.elements.effects;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import dev.lone.itemsadder.api.FontImages.TexturedInventoryWrapper;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import ch.njol.skript.variables.Variables;

import javax.annotation.Nullable;

public class EffOpenCustomInventory extends Effect {

    private Expression<Player> players;
    private Expression<String> inventoryId;

    static {
        Skript.registerEffect(EffOpenCustomInventory.class, "open [the] (custom|ia|itemsadder) [inventory] %string% to %players%");
    }

    @Override
    protected void execute(Event e) {
        Player[] ps = players.getArray(e);
        String inventoryId = this.inventoryId.getSingle(e);
        if (ps != null && inventoryId != null) {
            TexturedInventoryWrapper inventory = (TexturedInventoryWrapper) Variables.getVariable(inventoryId, e, true);
            if (inventory != null) {
                for (Player p : ps) {
                    inventory.showInventory(p);
                }
            }
        }
    }

    @Override
    public String toString(@Nullable Event e, boolean debug) {
        return "open " + inventoryId.toString(e, debug) + " to " + players.toString(e, debug);
    }

    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        inventoryId = (Expression<String>) exprs[0];
        players = (Expression<Player>) exprs[1];
        return true;
    }
}

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
import dev.lone.itemsadder.api.FontImages.TexturedInventoryWrapper;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import ch.njol.skript.variables.Variables;

import javax.annotation.Nullable;
@Name("Open ItemsAdder Inventory")
@Description("Opens an ItemsAdder inventory to a player(s), made using the ItemsAdder inventory section.")
@Examples("open custom inventory {votegui} to all players")
@Since("1.5")
@RequiredPlugins("ItemsAdder")
public class EffOpenCustomInventory extends Effect {

    private Expression<Player> players;
    private Expression<TexturedInventoryWrapper> inventory;

    static {
        Skript.registerEffect(EffOpenCustomInventory.class, "(show|open) [a[n]|the] (custom|ia|itemsadder) [inventory] %texturedinventorywrapper% to %players%");
    }

    @Override
    protected void execute(Event e) {
        Player[] ps = players.getArray(e);
        TexturedInventoryWrapper inventory = this.inventory.getSingle(e);
        if (ps != null && inventory != null) {
                for (Player p : ps) {
                    inventory.showInventory(p);
                }
            }
        }

    @Override
    public String toString(@Nullable Event e, boolean debug) {
        return "open " + inventory.toString(e, debug) + " to " + players.toString(e, debug);
    }

    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        inventory = (Expression<TexturedInventoryWrapper>) exprs[0];
        players = (Expression<Player>) exprs[1];
        return true;
    }
}

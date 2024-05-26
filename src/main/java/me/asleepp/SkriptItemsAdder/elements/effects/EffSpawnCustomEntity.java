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
import dev.lone.itemsadder.api.CustomEntity;
import org.bukkit.Location;
import org.bukkit.event.Event;

import javax.annotation.Nullable;
@Name("Spawn ItemsAdder Entity")
@Description({"Spawns an ItemsAdder entity."})
@Examples({"spawn custom entity \"bosses:john_wick\" at player's location"})
@Since("1.0")
@RequiredPlugins("ItemsAdder")
public class EffSpawnCustomEntity extends Effect {

    private Expression<String> entityIdExpr;
    private Expression<Location> locationExpr;

    static {
        Skript.registerEffect(EffSpawnCustomEntity.class, "(spawn|summon) [the] [custom] (ia|itemsadder) (entity|mob) %string% at %locations%");
    }

    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        entityIdExpr = (Expression<String>) exprs[0];
        locationExpr = (Expression<Location>) exprs[1];
        return true;
    }

    @Override
    protected void execute(Event e) {
        String entityId = entityIdExpr.getSingle(e);
        Location[] locations = locationExpr.getArray(e);

        if (entityId == null || locations == null) {
            return;
        }

        for (Location location : locations) {
            CustomEntity customEntity = CustomEntity.spawn(entityId, location);
            if (customEntity == null) {
                Skript.error("Please provide a valid CustomEntity ID.");
                return;
            }
        }
    }

    @Override
    public String toString(@Nullable Event e, boolean debug) {
        return "spawn custom entity " + entityIdExpr.toString(e, debug) + " at " + locationExpr.toString(e, debug);
    }
}

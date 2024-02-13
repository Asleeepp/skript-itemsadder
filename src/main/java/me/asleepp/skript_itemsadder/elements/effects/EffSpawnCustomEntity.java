package me.asleepp.skript_itemsadder.elements.effects;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
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
@Examples({"spawn custom entity john_wick at player's location"})
public class EffSpawnCustomEntity extends Effect {

    private Expression<String> entityIdExpr;
    private Expression<Location> locationExpr;

    static {
        Skript.registerEffect(EffSpawnCustomEntity.class, new String[] {"(spawn|summon) [the] (custom|ia|itemsadder) (entity|mob) %string% at %locations%"});
    }

    @Override
    protected void execute(Event e) {
        String entityId = entityIdExpr.getSingle(e);
        Location[] locations = locationExpr.getAll(e);

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
        return null;
    }

    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        return false;
    }
}

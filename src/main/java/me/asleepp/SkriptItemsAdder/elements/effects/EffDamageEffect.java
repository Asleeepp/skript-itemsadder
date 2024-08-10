package me.asleepp.SkriptItemsAdder.elements.effects;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import dev.lone.itemsadder.api.CustomEntity;
import org.bukkit.entity.Entity;
import org.bukkit.event.Event;

import javax.annotation.Nullable;

public class EffDamageEffect extends Effect {

    private Expression<Entity> entitiesExpr;

    static {
        Skript.registerEffect(EffDamageEffect.class, "(show|play) [the] damage effect on [custom] (itemsadder|ia) entit[y|ies] %entities%");
    }

    @Override
    public boolean init(Expression<?>[] exprs, int i, Kleenean kleenean, SkriptParser.ParseResult parseResult) {
        entitiesExpr = (Expression<Entity>) exprs[0];
        return true;
    }

    @Override
    protected void execute(Event event) {
        Entity[] entities = entitiesExpr.getArray(event);

        if (entities == null) {
            return;
        }

        for (Entity entity : entities) {
            CustomEntity customEntity = CustomEntity.byAlreadySpawned(entity);
            if (!(customEntity == null)) {
                customEntity.playDamageEffect(false);
            }

        }
    }

    @Override
    public String toString(@Nullable Event event, boolean debug) {
        return "show damage effect on itemsadder entity " + entitiesExpr.toString(event, debug);
    }
}
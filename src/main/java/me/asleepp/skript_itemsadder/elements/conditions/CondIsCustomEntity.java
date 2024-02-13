package me.asleepp.skript_itemsadder.elements.conditions;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Condition;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import dev.lone.itemsadder.api.CustomEntity;
import org.bukkit.entity.Entity;
import org.bukkit.event.Event;

import javax.annotation.Nullable;

public class CondIsCustomEntity extends Condition {

    private Expression<Entity> entities;


    static {
        Skript.registerCondition(CondIsCustomEntity.class, new String[] {"%entities% (is|are) [(a|an)] (custom|ia|itemsadder) entity"});
    }
    @Override
    public boolean check(Event e) {
        for (Entity entity : entities.getArray(e)) {
            if (!CustomEntity.isCustomEntity(entity)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public String toString(@Nullable Event e, boolean debug) {
        return entities.toString(e, debug) + " is an ItemsAdder entity";
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        entities = (Expression<Entity>) exprs[0];
        return true;
    }
}